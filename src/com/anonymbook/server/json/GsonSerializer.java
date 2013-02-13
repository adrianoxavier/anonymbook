package com.anonymbook.server.json;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.serialization.Serializer;
import br.com.caelum.vraptor.view.ResultException;

import com.google.gson.GsonBuilder;

public class GsonSerializer<T> implements Serializer {

	private GsonBuilder gson;

	private final T object;

	private final HttpServletResponse response;

	public GsonSerializer(T object, GsonBuilder gson, HttpServletResponse response) {
		this.object = object;
		this.gson = gson;
		this.response = response;
	}

	@Override
	public void serialize() {
		try {
			PrintWriter writer = response.getWriter();
			writer.append(gson.create().toJson(object));
			writer.close();
		} catch (IOException e) {
			throw new ResultException("Unable to serialize data width Gson API", e);
		}
	}

	@Override
	public Serializer recursive() {
		return this;
	}

	@Override
	public Serializer include(String... names) {
		return this;
	}

	@Override
	public Serializer exclude(String... names) {
		gson.setExclusionStrategies(new FieldsExclusion(names)).create();
		return this;
	}

}
