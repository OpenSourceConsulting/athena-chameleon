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
 * Sang-cheon Park	2012. 10. 3.		First Draft.
 */
package com.athena.chameleon.engine.core.analyzer.parser;

import java.io.File;
import java.io.IOException;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.JAXBException;

import com.athena.chameleon.common.utils.ThreadLocalUtil;
import com.athena.chameleon.engine.constant.ChameleonConstants;
import com.athena.chameleon.engine.entity.pdf.AnalyzeDefinition;
import com.athena.chameleon.engine.entity.pdf.EjbRecommend;
import com.athena.chameleon.engine.entity.pdf.PDFMetadataDefinition;
import com.athena.chameleon.engine.entity.xml.application.jboss.v5_0.JbossApp;
import com.athena.chameleon.engine.entity.xml.application.jboss.v5_0.LoaderRepository;
import com.athena.chameleon.engine.utils.JaxbUtils;

/**
 * <pre>
 *
 * </pre>
 * 
 * @author Sang-cheon Park
 * @version 1.0
 */
public class WeblogicApplicationXMLParser extends Parser {

	/* (non-Javadoc)
	 * @see com.athena.chameleon.engine.core.analyzer.parser.Parser#parse(java.io.File, com.athena.chameleon.engine.entity.pdf.AnalyzeDefinition)
	 */
	@Override
	public Object parse(File file, AnalyzeDefinition analyzeDefinition) {
		this.analyzeDefinition = analyzeDefinition;
		
		PDFMetadataDefinition metadataDefinition = (PDFMetadataDefinition)ThreadLocalUtil.get(ChameleonConstants.PDF_METADATA_DEFINITION);
		EjbRecommend ejbRecommend = new EjbRecommend();
		
        try {
        	ejbRecommend = new EjbRecommend();
    		ejbRecommend.setItem(file.getName());
    		ejbRecommend.setTransFlag(false);
    		ejbRecommend.setLocation(removeTempDir(file.getParent()));
    		ejbRecommend.setContents(fileToString(file.getAbsolutePath()));
    		
    		metadataDefinition.getApplicationRecommendList().add(ejbRecommend);
        } catch (IOException e) {
            logger.error("IOException has occurred.", e);
        }

//        try {
//            CommonAnalyze commonAnalyze = new CommonAnalyze();
//            commonAnalyze.setItem(file.getName());
//            commonAnalyze.setLocation(removeTempDir(file.getParent()));
//            commonAnalyze.setContents(fileToString(file.getAbsolutePath()));
//            
//            analyzeDefinition.getDescripterList().add(commonAnalyze);
//        } catch (IOException e) {
//            logger.error("IOException has occurred.", e);
//        }
        
    	Object obj = null;
    	
    	try {
        	// http://www.bea.com/ns/weblogic/weblogic-application/1.0/weblogic-application.xsd
			obj = ((JAXBElement<?>)JaxbUtils.unmarshal(com.athena.chameleon.engine.entity.xml.application.weblogic.v1_0.WeblogicApplicationType.class.getPackage().getName(), file)).getValue();
    	} catch (JAXBException e1) {
			try {
	    		// http://www.bea.com/ns/weblogic/90/weblogic-application.xsd
				obj = ((JAXBElement<?>)JaxbUtils.unmarshal(com.athena.chameleon.engine.entity.xml.application.weblogic.v9_0.WeblogicApplicationType.class.getPackage().getName(), file)).getValue();
			} catch (JAXBException e2) {
	    		try {
					// http://www.bea.com/servers/wls810/dtd/weblogic-application_2_0.dtd
					obj = JaxbUtils.unmarshal(com.athena.chameleon.engine.entity.xml.application.weblogic.v8_1.WeblogicApplication.class.getPackage().getName(), file);
				} catch (JAXBException e3) {
					logger.error("JAXBException has occurred.", e3);
				}
			}
    	}
    	
		try {
			JbossApp jbossApp = new JbossApp();
			
			LoaderRepository loaderRepository = new LoaderRepository();
			loaderRepository.setvalue("com.athena.chameleon:loader=" + ThreadLocalUtil.get(ChameleonConstants.PROJECT_NAME));
			
			jbossApp.setLoaderRepository(loaderRepository);
			
			String xmlData = JaxbUtils.marshal(JbossApp.class.getPackage().getName(), jbossApp, "<!DOCTYPE jboss-app PUBLIC \"-//JBoss//DTD J2EE Application 5.0//EN\" \"http://www.jboss.org/j2ee/dtd/jboss-app_5_0.dtd\">");

			rewrite(new File(file.getParentFile(), "jboss-app.xml"), xmlData.replaceAll(" standalone=\"yes\"", ""));
			
        	ejbRecommend = new EjbRecommend();
    		ejbRecommend.setItem("jboss-app.xml");
    		ejbRecommend.setTransFlag(true);
    		ejbRecommend.setLocation(removeTempDir(file.getParent()));
    		ejbRecommend.setContents(xmlData.replaceAll(" standalone=\"yes\"", ""));
    		
    		metadataDefinition.getApplicationRecommendList().add(ejbRecommend);
    		//metadataDefinition.getTransFileList().add(ejbRecommend.getLocation() + File.separator + "jboss-app.xml");
		} catch (JAXBException e) {
			logger.error("JAXBException has occurred.", e);
		} catch (IOException e) {
			logger.error("IOException has occurred.", e);
		}
    	
		return obj;
	}//end of parse()
}
//end of WeblogicApplicationXMLParser.java