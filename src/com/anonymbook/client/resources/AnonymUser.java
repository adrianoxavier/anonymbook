package com.anonymbook.client.resources;

import java.util.List;

public class AnonymUser {

	private String id;

	private String name;

	private String accessToken;

	private List<String> friends;

	public AnonymUser() {
		this(null, null, null);
	}

	public AnonymUser(String id, String accessToken) {
		this(id, null, accessToken);
	}

	public AnonymUser(String id, String name, String accessToken) {
		this.id = id;
		this.name = name;
		this.accessToken = accessToken;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getAccessToken() {
		return accessToken;
	}

	public void setAccessToken(String accessToken) {
		this.accessToken = accessToken;
	}

	public List<String> getFriends() {
		return friends;
	}

	public void setFriends(List<String> friends) {
		this.friends = friends;
	}

	@Override
	public String toString() {
		return "AnonymUser [id=" + id + ", name=" + name + ", accessToken="
				+ accessToken + "]";
	}

}
