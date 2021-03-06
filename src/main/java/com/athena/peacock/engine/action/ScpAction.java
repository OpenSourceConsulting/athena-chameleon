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
 * Ji-Woong Choi	2012. 10. 24.		First Draft.
 */
package com.athena.peacock.engine.action;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.athena.peacock.engine.core.TargetHost;
import com.athena.peacock.engine.util.ScpUtil;

/**
 * <pre>
 * 지정된 호스트로 파일 또는 디렉토리를 전송하기 위한 액션 클래스
 * </pre>
 * 
 * @author Ji-Woong Choi
 * @version 1.0
 */
public class ScpAction implements Action {
	
	private static final Logger logger = LoggerFactory.getLogger(ScpAction.class);
	
	private TargetHost targetHost;
    private String source;
    private String target;

    public ScpAction(TargetHost targetHost, String source, String target) {
    	this.targetHost = targetHost;
        this.source = source;
        this.target = target;
    }
    
    /* (non-Javadoc)
     * @see com.athena.peacock.engine.action.Action#perform()
     */
    @Override
    public void perform() {
    	logger.debug("\n- Target Host Info : [{}]\n- Source : [{}]\n- Target : [{}]", new String[]{targetHost.toString(), source, target});
    	
        ScpUtil.upload(targetHost, source, target);
    }//end of perform()

}
//end of ScpAction.java