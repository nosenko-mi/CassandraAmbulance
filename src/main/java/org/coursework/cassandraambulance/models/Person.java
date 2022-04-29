package org.coursework.cassandraambulance.models;

import java.util.UUID;

// клас, що представляє особу (водій, лікар, санітар, викликач)
public class Person {
    private String type;
    private UUID id;
    private String firstName, middleName, lastName;

    public Person(String type, UUID id, String firstName, String middleName, String lastName) {
        this.type = type;
        this.id = id;
        this.firstName = firstName;
        this.middleName = middleName;
        this.lastName = lastName;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }
}
