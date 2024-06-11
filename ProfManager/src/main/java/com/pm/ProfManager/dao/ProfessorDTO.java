/*****************************************************************
 * File:  InventoryDTO.java
 * Course materials CST 8277
 * 
 * @author (original) Mike Norman
 *
 */
package com.pm.ProfManager.dao;

import java.io.Serializable;
import java.util.Objects;

import javax.faces.view.ViewScoped;

/*
 * this scope is not like @SessionScoped or @RequestScoped about *how many* instances or *how long*
 * they are within scope in the app. Instead, it is about *where* the object is in scope, which is
 * the view - essentially even though it is *defined* in Java, @ViewScoped objects belong to
 * the XHTML part of the app.
 */
@ViewScoped
public class ProfessorDTO implements Serializable, Professor {
    private static final long serialVersionUID = 1L;

    protected int id;
    protected String professorName;
    protected String major;
    protected int age;

    @Override
    public int getId() {
        return id;
    }
    @Override
    public void setId(int id) {
        this.id = id;
    }

    @Override
    public String getProfessorName() {
        return professorName;
    }
    @Override
    public void setProfessorName(String retailerName) {
        this.professorName = retailerName;
    }
    @Override
    public String getMajor() {
        return major;
    }
    @Override
    public void setMajor(String firstName) {
        this.major = firstName;
    }

    @Override
    public int getAge() {
        return age;
    }
    @Override
    public void setAge(int level) {
        this.age = level;
    }

	/**
	 * Very important: use getter's for member variables because JPA sometimes needs to intercept those calls<br/>
	 * and go to the database to retrieve the value
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = super.hashCode();
		// only include member variables that really contribute to an object's identity
		// i.e. if variables like version/updated/name/etc. change throughout an object's lifecycle,
		// they shouldn't be part of the hashCode calculation
		return prime * result + Objects.hash(getId());
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj) {
			return true;
		}
		if (obj == null) {
			return false;
		}

		/* enhanced instanceof - yeah!
		 As of JDK 14, no need for additional 'silly' cast:
		     if (animal instanceof Cat) {
		         Cat cat = (Cat)animal;
		         cat.meow();
                 // other class Cat operations ...
             }
		*/
		if (obj instanceof ProfessorDTO invDto) {
			// see comment (above) in hashCode(): compare using only member variables that are
			// truely part of an object's identity
			return Objects.equals(this.getId(), invDto.getId());
		}
		return false;
	}

}