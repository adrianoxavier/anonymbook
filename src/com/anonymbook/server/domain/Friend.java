package com.anonymbook.server.domain;

import java.io.Serializable;

import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Transient;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Key;

@javax.persistence.Entity
public class Friend implements Serializable {

	@Transient
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Key key;

	private String user1;

	private String user2;

	public Friend(Entity entity) {
		this.key = entity.getKey();
		this.user1 = (String) entity.getProperty("user1");
		this.user2 = (String) entity.getProperty("user2");
	}

	public Friend(String user1, String user2) {
		this.user1 = user1;
		this.user2 = user2;
	}

	public Key getKey() {
		return key;
	}

	public void setKey(Key key) {
		this.key = key;
	}

	public String getUser1() {
		return user1;
	}

	public void setUser1(String user1) {
		this.user1 = user1;
	}

	public String getUser2() {
		return user2;
	}

	public void setUser2(String user2) {
		this.user2 = user2;
	}

	@Override
	public String toString() {
		return "Friend [user1=" + user1 + ", user2=" + user2 + "]";
	}

}
