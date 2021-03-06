/* 
 * Copyright 2012 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Sang-cheon Park	2012. 9. 24.		First Draft.
 */
package com.athena.chameleon.engine.core.analyzer.support;

import java.io.File;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.apache.commons.lang.StringUtils;
import org.springframework.util.Assert;

import com.athena.chameleon.common.jcl.JarClassLoader;
import com.athena.chameleon.common.utils.ClasspathUtil;
import com.athena.chameleon.common.utils.MigrationStatusUtil;
import com.athena.chameleon.common.utils.ThreadLocalUtil;
import com.athena.chameleon.common.utils.ZipUtil;
import com.athena.chameleon.engine.constant.ChameleonConstants;
import com.athena.chameleon.engine.core.analyzer.AbstractAnalyzer;
import com.athena.chameleon.engine.core.converter.FileEncodingConverter;
import com.athena.chameleon.engine.entity.pdf.AnalyzeDefinition;
import com.athena.chameleon.engine.entity.pdf.ArchiveType;
import com.athena.chameleon.engine.entity.pdf.ClassAnalyze;
import com.athena.chameleon.engine.entity.pdf.FileSummary;
import com.athena.chameleon.engine.entity.pdf.FileType;
import com.athena.chameleon.engine.entity.pdf.PDFMetadataDefinition;
import com.athena.chameleon.engine.policy.Policy;
import com.athena.chameleon.engine.threadpool.executor.ChameleonThreadPoolExecutor;

/**
 * <pre>
 * 
 * </pre>
 * @author Sang-cheon Park
 * @version 1.0
 */
public class WarAnalyzer extends AbstractAnalyzer {
	
	// Ear Application 내에 포함된 Archive 인지의 여부
	// 재압축 시 Ear Application 내의 Archive일 경우 동일한 이름으로 재압축 하기 위해 사용됨.
	private boolean embed;

	/**
	 * <pre>
	 * Constructor
	 * </pre>
	 * @param policy
	 * @param converter
	 * @param executor
	 */
	public WarAnalyzer(Policy policy, FileEncodingConverter converter, ChameleonThreadPoolExecutor executor, AnalyzeDefinition analyzeDefinition, boolean embed) {
		super(policy, converter, executor, analyzeDefinition);
		this.embed = embed;
	}//end of Constructor
	
	/* (non-Javadoc)
	 * @see com.athena.chameleon.engine.core.analizer.Analyzer#analyze(java.io.File)
	 */
	@Override
	public String analyze(File file) {
		Assert.notNull("file", "file must not be null.");
		//Assert.isTrue(file.getName().endsWith(".war"), "file name must be ends with \".war\".");

		boolean isExploded = false;
		// 입력된 파일명을 프로젝트 이름으로 사용한다.(jboss-app.xml, jboss-web.xml 파일 생성시 사용)
		if (file.getName().endsWith(".war")) {
			if (file.isDirectory()) {
				isExploded = true;
			}
		    ThreadLocalUtil.add(ChameleonConstants.PROJECT_NAME, file.getName().substring(0, file.getName().lastIndexOf(".")));
		} else {
			isExploded = true;
		    ThreadLocalUtil.add(ChameleonConstants.PROJECT_NAME, file.getName());
		}
		
		String newFileName = null;
		
		try {
			logger.debug("[jwchoi] 임시 디렉토리에 압축을 해제합니다.");
			
			// 임시 디렉토리에 압축 해제
			if(!embed) {
				MigrationStatusUtil.setCurrentStatus(MigrationStatusUtil.STEP1);
				MigrationStatusUtil.setPercentage(this, 20);
			}
			String tempDir = null;
			
			if(isExploded) {
				tempDir = file.getAbsolutePath();
			} else {
				tempDir = policy.getUnzipDir() + File.separator + System.currentTimeMillis();
				ZipUtil.decompress(file.getAbsolutePath(), tempDir);
			}
			
			ThreadLocalUtil.add(ChameleonConstants.WAR_ROOT_DIR, tempDir);
			
			/****************************************************************************************
			 * 
			 * TODO Should be Delete. MAC 에서 테스트 애플리케이션 입력 시 __MACOSX 라는 garbage 디렉토리가 생김.
			 * 
			 ****************************************************************************************/
			if(new File(tempDir, "__MACOSX").exists()) {
				deleteDirectory(new File(tempDir, "__MACOSX"));
			}

			logger.debug("[jwchoi] 인코딩을 변경합니다.");
			
			// 인코딩 변경
			if(!embed) {
				MigrationStatusUtil.setCurrentStatus(MigrationStatusUtil.STEP2);
				MigrationStatusUtil.setPercentage(this, 40);
			}
			converter.convert(new File(tempDir), analyzeDefinition);

			logger.debug("[jwchoi] 클래스파일들이 위치한 디렉토리 => [{}]", getClassesDirPath(new File(tempDir)));
			
			// 압축 해제 디렉토리 중 classes 디렉토리를 클래스 패스에 추가한다. 
			if(!StringUtils.isEmpty(getClassesDirPath(new File(tempDir)))) {
				JarClassLoader jcl = null;
				
				if(embed) {
					jcl = ((PDFMetadataDefinition)ThreadLocalUtil.get(ChameleonConstants.PDF_METADATA_DEFINITION)).getEarDefinition().getJcl();
				} 

				logger.debug("[jwchoi] 다음 라이브러리 파일들을 클래스로더에 추가합니다.");
				
				List<String> pathList = analyzeDefinition.getLibraryFullPathList();
				for(String str : pathList) {
					
					logger.debug("[jwchoi] Library => {}", str);
					
					try {
						if(jcl == null) {
							jcl = new JarClassLoader(new File(str).toURI().toURL());
						} else {
							jcl.addJarURL(new File(str).toURI().toURL().toString());
						}
					} catch (Exception e) {
						// Ignore.
						logger.error("[{}] file can't add to Class Loader.", str);
					}
				}
				
				if(jcl == null) {
					jcl = new JarClassLoader(this.getClass().getResource("/lib/ejb-api-3.0.jar").toString());
				} else {
					jcl.addJarURL(this.getClass().getResource("/lib/ejb-api-3.0.jar").toString());
				}
				jcl.addJarURL(this.getClass().getResource("/lib/javax.servlet-api-3.0.1.jar").toString());
				jcl.addJarURL(this.getClass().getResource("/lib/javaee-api-6.0.jar").toString());
				jcl.addJarURL(this.getClass().getResource("/lib/weblogic.jar").toString());
				jcl.addJarURL(this.getClass().getResource("/lib/jeus.jar").toString());
				
				analyzeDefinition.setJcl(jcl);
				
	            ClasspathUtil.addPath(getClassesDirPath(new File(tempDir)), jcl);
			}

			logger.debug("[jwchoi] 압축 해제 디렉토리 내의 파일들을 분석합니다.");
			
			// 압축 해제 디렉토리 내의 파일을 분석한다.
			if(!embed) {
				MigrationStatusUtil.setCurrentStatus(MigrationStatusUtil.STEP3);
				MigrationStatusUtil.setPercentage(this, 60);
			}
			analyze(new File(tempDir), tempDir);
			
			logger.debug("[jwchoi] jboss-classloading.xml 파일을 생성합니다.");
			
			// jboss-classloading.xml 파일을 생성한다.
			if(embed) {
				String parentDomain = ((PDFMetadataDefinition)ThreadLocalUtil.get(ChameleonConstants.PDF_METADATA_DEFINITION)).getDeployFile();
				parentDomain = parentDomain.substring(parentDomain.lastIndexOf("/") + 1);
				makeClassLoading(getWebInfDirPath(new File(tempDir)), file.getName(), parentDomain);
			} else {
				makeClassLoading(getWebInfDirPath(new File(tempDir)), file.getName(), null);
			}
			
			if(!isExploded) {

				logger.debug("[jwchoi] 임시 디렉토리를 재 압축 후 삭제합니다.");
				
				// 임시디렉토리를 재 압축한다.
				if(!embed) {
					MigrationStatusUtil.setCurrentStatus(MigrationStatusUtil.STEP4);
					MigrationStatusUtil.setPercentage(this, 80);
				}
				newFileName = embed ? file.getAbsolutePath() : getResultFile(file);
				ZipUtil.compress(tempDir, newFileName, ArchiveType.WAR);
			
				// 임시 디렉토리를 삭제한다.
				if(!embed) {
					MigrationStatusUtil.setCurrentStatus(MigrationStatusUtil.STEP5);
					MigrationStatusUtil.setPercentage(this, 100);
				}
				deleteDirectory(new File(tempDir));
			}
			
			//*
			System.err.println("\n\n================== [Deploy File Result] ==================");
			PDFMetadataDefinition metadataDefinition = (PDFMetadataDefinition)ThreadLocalUtil.get(ChameleonConstants.PDF_METADATA_DEFINITION);
			FileType[] fileTypes = FileType.values();
			AnalyzeDefinition definition = metadataDefinition.getWarDefinitionMap().get(file.getName());
			Map<FileType, FileSummary> fileSummaryMap = definition.getFileSummaryMap();
			Map<String, Integer> directiveInfo = definition.getJspDirectiveMap();
					
			logger.info("File => {}", metadataDefinition.getDeployFile());
			
			for (FileType fileType : fileTypes) {
				logger.info("File Type : [{}], \tCount : [{}개], \tSource Encoding : [{}], \tTarget Encoding : [{}]", 
						new Object[] {String.format("%12s", fileSummaryMap.get(fileType).getFileType().toString()), 
									  String.format("%5s", fileSummaryMap.get(fileType).getFileCount()),
									  String.format("%12s", fileSummaryMap.get(fileType).getSourceEncoding()),
									  String.format("%5s", fileSummaryMap.get(fileType).getTargetEncoding())});
			}
			
			Iterator<Entry<String, Integer>> iter = directiveInfo.entrySet().iterator();
			
			Entry<String, Integer> entry = null;
			while(iter.hasNext()) {
				entry = iter.next();
				logger.info("[{}] : {}개", entry.getKey(), entry.getValue());
			}
			
			logger.info("directory count in classes : [{}], class file count in classes : [{}]", definition.getClassDirCount(), definition.getClassFileCount());
			
			List<ClassAnalyze> classAnalyzeList = definition.getClassesConstList();
			for(ClassAnalyze classAnalyze : classAnalyzeList) {
				logger.info("getFiledListStr => {}", classAnalyze.getClassName());
				logger.info("getClassModifier => {}", classAnalyze.getClassModifier());
				logger.info("getFiledListStr => {}", classAnalyze.getFiledListStr());
			}
			
			//*/
			
			ThreadLocalUtil.add(ChameleonConstants.WAR_ROOT_DIR, null);
		} catch (Exception e) {
			logger.error("Unahandled Exception has occurred : ", e);
		}
		
		return newFileName;
	}//end of analyze()

}//end of WarAnalyzer.java