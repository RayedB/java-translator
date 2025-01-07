package org.rayedb.Model;

import jakarta.xml.bind.annotation.XmlRootElement;
import io.quarkus.runtime.annotations.RegisterForReflection;

@XmlRootElement
@RegisterForReflection
public class DataModel {
    private String name;
    private String value;
    
    // Getters and setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getValue() { return value; }
    public void setValue(String value) { this.value = value; }
}