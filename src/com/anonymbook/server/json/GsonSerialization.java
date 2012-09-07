package com.anonymbook.server.json;

import java.lang.reflect.Modifier;
import java.text.DateFormat;

import javax.servlet.http.HttpServletResponse;

import br.com.caelum.vraptor.ioc.Component;
import br.com.caelum.vraptor.serialization.JSONSerialization;
import br.com.caelum.vraptor.serialization.NoRootSerialization;
import br.com.caelum.vraptor.serialization.Serializer;

import com.google.appengine.api.datastore.Key;
import com.google.gson.GsonBuilder;

@Component
public class GsonSerialization implements JSONSerialization {

	private GsonBuilder gson;

	private final HttpServletResponse response;

	public GsonSerialization(HttpServletResponse response) {
		this.response = response;
		this.gson = getGsonBuilder();
	}

	@Override
	public boolean accepts(String format) {
		return "json".equals(format);
	}

	@Override
	public <T> Serializer from(T object) {
		response.setContentType("application/json");
		return serializeWithGsonAPI(object);
	}

	@Override
	public <T> Serializer from(T object, String alias) {
		return from(object);
	}

	@Override
	public <T> NoRootSerialization withoutRoot() {
		return this;
	}

	private <T> Serializer serializeWithGsonAPI(final T object) {
		return new GsonSerializer<T>(object, gson, response);

	}

	@Override
	public JSONSerialization indented() {
		gson.setPrettyPrinting();
		return this;
	}

	public static GsonBuilder getGsonBuilder() {
		return new GsonBuilder()
				.excludeFieldsWithModifiers(Modifier.STATIC, Modifier.TRANSIENT)
				.setDateFormat(DateFormat.LONG)
				.setExclusionStrategies(new FieldsExclusion("jdoDetachedState"))
				.registerTypeAdapter(Key.class, new KeyAdapter());
	}

}
