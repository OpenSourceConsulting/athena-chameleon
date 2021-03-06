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
import javax.xml.bind.annotation.adapters.CollapsedStringAdapter;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;


/**
 * <p>Java class for bridge-entryType complex type.
 * 
 * <p>The following schema fragment specifies the expected content contained within this class.
 * 
 * <pre>
 * &lt;complexType name="bridge-entryType">
 *   &lt;complexContent>
 *     &lt;restriction base="{http://www.w3.org/2001/XMLSchema}anyType">
 *       &lt;sequence>
 *         &lt;element name="name" type="{http://www.w3.org/2001/XMLSchema}token"/>
 *         &lt;element name="source" type="{http://www.tmaxsoft.com/xml/ns/jeus}bridge-destinationType"/>
 *         &lt;element name="target" type="{http://www.tmaxsoft.com/xml/ns/jeus}bridge-destinationType"/>
 *         &lt;element name="message-selector" type="{http://www.w3.org/2001/XMLSchema}token" minOccurs="0"/>
 *         &lt;element name="timeout" type="{http://www.w3.org/2001/XMLSchema}long" minOccurs="0"/>
 *       &lt;/sequence>
 *     &lt;/restriction>
 *   &lt;/complexContent>
 * &lt;/complexType>
 * </pre>
 * 
 * 
 */
@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "bridge-entryType", propOrder = {
    "name",
    "source",
    "target",
    "messageSelector",
    "timeout"
})
public class BridgeEntryType {

    @XmlElement(namespace = "http://www.tmaxsoft.com/xml/ns/jeus", required = true)
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String name;
    @XmlElement(namespace = "http://www.tmaxsoft.com/xml/ns/jeus", required = true)
    protected BridgeDestinationType source;
    @XmlElement(namespace = "http://www.tmaxsoft.com/xml/ns/jeus", required = true)
    protected BridgeDestinationType target;
    @XmlElement(name = "message-selector", namespace = "http://www.tmaxsoft.com/xml/ns/jeus")
    @XmlJavaTypeAdapter(CollapsedStringAdapter.class)
    protected String messageSelector;
    @XmlElement(namespace = "http://www.tmaxsoft.com/xml/ns/jeus", defaultValue = "10000")
    protected Long timeout;

    /**
     * Gets the value of the name property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getName() {
        return name;
    }

    /**
     * Sets the value of the name property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setName(String value) {
        this.name = value;
    }

    /**
     * Gets the value of the source property.
     * 
     * @return
     *     possible object is
     *     {@link BridgeDestinationType }
     *     
     */
    public BridgeDestinationType getSource() {
        return source;
    }

    /**
     * Sets the value of the source property.
     * 
     * @param value
     *     allowed object is
     *     {@link BridgeDestinationType }
     *     
     */
    public void setSource(BridgeDestinationType value) {
        this.source = value;
    }

    /**
     * Gets the value of the target property.
     * 
     * @return
     *     possible object is
     *     {@link BridgeDestinationType }
     *     
     */
    public BridgeDestinationType getTarget() {
        return target;
    }

    /**
     * Sets the value of the target property.
     * 
     * @param value
     *     allowed object is
     *     {@link BridgeDestinationType }
     *     
     */
    public void setTarget(BridgeDestinationType value) {
        this.target = value;
    }

    /**
     * Gets the value of the messageSelector property.
     * 
     * @return
     *     possible object is
     *     {@link String }
     *     
     */
    public String getMessageSelector() {
        return messageSelector;
    }

    /**
     * Sets the value of the messageSelector property.
     * 
     * @param value
     *     allowed object is
     *     {@link String }
     *     
     */
    public void setMessageSelector(String value) {
        this.messageSelector = value;
    }

    /**
     * Gets the value of the timeout property.
     * 
     * @return
     *     possible object is
     *     {@link Long }
     *     
     */
    public Long getTimeout() {
        return timeout;
    }

    /**
     * Sets the value of the timeout property.
     * 
     * @param value
     *     allowed object is
     *     {@link Long }
     *     
     */
    public void setTimeout(Long value) {
        this.timeout = value;
    }

}
