package org.rayedb.Model;

import jakarta.xml.bind.annotation.XmlRootElement;
import io.quarkus.runtime.annotations.RegisterForReflection;

@XmlRootElement
@RegisterForReflection
public class Household {
    private Person person;
    
    public Person getPerson() { return person; }
    public void setPerson(Person person) { this.person = person; }
}