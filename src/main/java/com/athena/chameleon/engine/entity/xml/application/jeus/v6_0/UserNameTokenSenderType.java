//
// This file was generated by the JavaTM Architecture for XML Binding(JAXB) Reference Implementation, v2.0-b52-fcs 
// See <a href="http://java.sun.com/xml/jaxb">http://java.sun.com/xml/jaxb</a> 
// Any modifications to this file will be lost upon recompilation of the source schema. 
// Generated on: 2012.09.17 at 07:02:27 오후 KST 
//


package com.athena.chameleon.engine.entity.xml.application.jeus.v6_0;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlType;


/**
 * <p>Java class for userNameTokenSenderType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="userNameTokenSenderType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="password" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *         &lt;element name="passwordType" type="{http://www.tmaxsoft.com/xml/ns/jeus}passwordTypeType" minOccurs="0"/>
 *         &lt;element name="userTokenElements" type="{http://www.w3.org/2001/XMLSchema}string" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userNameTokenSenderType", propOrder = {
    "password",
    "passwordType",
    "userTokenElements"
})
public class UserNameTokenSenderType {

    @XmlElement(namespace = "http://www.tmaxsoft.com/xml/ns/jeus")
    protected String password;
    @XmlElement(namespace = "http://www.tmaxsoft.com/xml/ns/jeus")
    protected PasswordTypeType passwordType;
    @XmlElement(namespace = "http://www.tmaxsoft.com/xml/ns/jeus")
    protected String userTokenElements;

    /**
     * Gets the value of the password property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getPassword() {
        return password;
    }

    /**
     * Sets the value of the password property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setPassword(String value) {
        this.password = value;
    }

    /**
     * Gets the value of the passwordType property.
     * 
     * @return
     *     possible object is
     *     {@link PasswordTypeType }
     *     
     */
    public PasswordTypeType getPasswordType() {
        return passwordType;
    }

    /**
     * Sets the value of the passwordType property.
     * 
     * @param value
     *     allowed object is
     *     {@link PasswordTypeType }
     *     
     */
    public void setPasswordType(PasswordTypeType value) {
        this.passwordType = value;
    }

    /**
     * Gets the value of the userTokenElements property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getUserTokenElements() {
        return userTokenElements;
    }

    /**
     * Sets the value of the userTokenElements property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setUserTokenElements(String value) {
        this.userTokenElements = value;
    }

}