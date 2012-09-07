package com.anonymbook.server.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

@javax.persistence.Entity
public class Person implements Serializable {

	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;

	private String id;

	public Person(Entity entity) {
		this.key = entity.getKey();
		this.id = (String) entity.getProperty("id");
	}

	public Person(String id) {
		this.id = id;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	@Override
	public String toString() {
		return "Person [id=" + id + "]";
	}

}
