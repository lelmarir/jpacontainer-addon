/**
 * Copyright 2009-2013 Oy Vaadin Ltd
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.vaadin.addon.jpacontainer.testdata;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Embedded;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.persistence.UniqueConstraint;
import javax.persistence.Version;

/**
 * Entity Java bean for testing.
 * 
 * @author Petter Holmström (Vaadin Ltd)
 * @since 1.0
 */
@SuppressWarnings("serial")
@Entity
@Table(uniqueConstraints = @UniqueConstraint(columnNames = { "lastName",
        "firstName" }))
public class Person implements Serializable, Cloneable {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;
    @Version
    private Long version;
    private String firstName;
    private String lastName;
    @Temporal(TemporalType.DATE)
    private Date dateOfBirth;
    @Embedded
    private Address address;
    private transient String tempData;
    @ManyToOne(fetch = FetchType.LAZY)
    private Person manager;
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "person")
    private Set<PersonSkill> skills = new HashSet<PersonSkill>();

    private boolean male;

    private double primitiveDouble;

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public Date getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(Date dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Long getVersion() {
        return version;
    }

    public void setVersion(Long version) {
        this.version = version;
    }

    public String getFullName() {
        return getFirstName() + " " + getLastName();
    }

    public Address getTransientAddress() {
        return address;
    }

    public String getTempData() {
        return tempData;
    }

    public void setTempData(String tempData) {
        this.tempData = tempData;
    }

    public Person getManager() {
        return manager;
    }

    public void setManager(Person manager) {
        this.manager = manager;
    }

    public Set<PersonSkill> getSkills() {
        return skills;
    }

    public void setSkills(Set<PersonSkill> skills) {
        this.skills = skills;
    }

    public void addSkill(Skill skill, Integer level) {
        PersonSkill ps = new PersonSkill();
        ps.setLevel(level);
        ps.setSkill(skill);
        ps.setPerson(this);
        skills.add(ps);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof Person) {
            Person p = (Person) obj;
            if (this == p) {
                return true;
            }
            if (id == null || p.id == null) {
                return false;
            }
            return id.equals(p.id);
        }

        return super.equals(obj);
    }

    @Override
    public int hashCode() {
        return id == null ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        String dob = null;
        if (dateOfBirth != null) {
            dob = DateFormat.getDateInstance(DateFormat.SHORT).format(
                    dateOfBirth);
        }
        return lastName + ", " + firstName + ", " + dob + ", " + address
                + " (ID: " + id + ")";
    }

    @Override
    public Person clone() {
        Person p = new Person();
        p.address = address == null ? null : address.clone();
        p.dateOfBirth = dateOfBirth;
        p.firstName = firstName;
        p.id = id;
        p.lastName = lastName;
        p.tempData = tempData;
        p.version = version;
        p.manager = manager;
        p.primitiveDouble = primitiveDouble;
        p.male = male;
        p.skills = new HashSet<PersonSkill>();
        for (PersonSkill ps : skills) {
            PersonSkill cps = ps.clone();
            cps.setPerson(p);
            p.skills.add(cps);
        }
        return p;
    }

    public void setMale(boolean male) {
        this.male = male;
    }

    public boolean isMale() {
        return male;
    }

    public void setPrimitiveDouble(double primitiveDouble) {
        this.primitiveDouble = primitiveDouble;
    }

    public double getPrimitiveDouble() {
        return primitiveDouble;
    }
}
