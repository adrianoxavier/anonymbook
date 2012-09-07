package com.anonymbook.server.json;

import java.lang.reflect.Type;

import com.google.appengine.api.datastore.Key;
import com.google.appengine.api.datastore.KeyFactory;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

public class KeyAdapter implements JsonSerializer<Key>, JsonDeserializer<Key> {

	@Override
	public Key deserialize(JsonElement element, Type type,
			JsonDeserializationContext context) throws JsonParseException {
		return KeyFactory.stringToKey(element.getAsJsonPrimitive().getAsString());
	}

	@Override
	public JsonElement serialize(Key object, Type type, JsonSerializationContext context) {
		return new JsonPrimitive(KeyFactory.keyToString(object));
	}

}
