//
// 이 파일은 JAXB(JavaTM Architecture for XML Binding) 참조 구현 2.2.6 버전을 통해 생성되었습니다. 
// <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a>를 참조하십시오. 
// 이 파일을 수정하면 소스 스키마를 재컴파일할 때 수정 사항이 손실됩니다. 
// 생성 날짜: 2012.10.05 시간 10:52:30 AM KST 
//


package com.athena.chameleon.engine.entity.xml.application.weblogic.v8_1;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;


/**
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "", propOrder = {
    "startupClass",
    "startupUri"
})
@XmlRootElement(name = "startup")
public class Startup {

    @XmlElement(name = "startup-class", required = true)
    protected String startupClass;
    @XmlElement(name = "startup-uri")
    protected String startupUri;

    /**
     * startupClass 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartupClass() {
        return startupClass;
    }

    /**
     * startupClass 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartupClass(String value) {
        this.startupClass = value;
    }

    /**
     * startupUri 속성의 값을 가져옵니다.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getStartupUri() {
        return startupUri;
    }

    /**
     * startupUri 속성의 값을 설정합니다.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setStartupUri(String value) {
        this.startupUri = value;
    }

}
