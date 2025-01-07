package org.rayedb.Model;

import jakarta.xml.bind.annotation.XmlAccessType;
import jakarta.xml.bind.annotation.XmlAccessorType;
import jakarta.xml.bind.annotation.XmlElement;

@XmlAccessorType(XmlAccessType.FIELD)
public class Person {
    @XmlElement
    private String name;
    @XmlElement
    private Birth birth;
    
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public Birth getBirth() { return birth; }
    public void setBirth(Birth birth) { this.birth = birth; }
} 