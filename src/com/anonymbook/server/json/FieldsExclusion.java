package com.anonymbook.server.json;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import com.google.gson.ExclusionStrategy;
import com.google.gson.FieldAttributes;

public class FieldsExclusion implements ExclusionStrategy {

	public List<String> exclusions;

	public FieldsExclusion(String... names) {
		this.exclusions = new LinkedList<String>(Arrays.asList(names));
	}

	public boolean shouldSkipClass(Class<?> clazz) {
		return false;
	}

	public boolean shouldSkipField(FieldAttributes f) {
		return exclusions.contains(f.getName());
	}

}
