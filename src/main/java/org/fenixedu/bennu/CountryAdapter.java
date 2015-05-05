package org.fenixedu.bennu;

import java.lang.reflect.Type;

import org.fenixedu.commons.i18n.LocalizedString;
import org.springframework.core.convert.converter.Converter;

import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.JsonParser;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import pt.ist.standards.geographic.Country;
import pt.ist.standards.geographic.GeographicInfoLoader;
import pt.ist.standards.geographic.Place;

/**
 * GSON serialiser/deserialiser for converting Country objects.
 */
public class CountryAdapter implements JsonSerializer<Country>, JsonDeserializer<Country> {
    @Override
    public JsonElement serialize(Country src, Type typeOfSrc, JsonSerializationContext context) {

        return new JsonPrimitive(src.exportAsString());
    }

    @Override
    public Country deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return (Country) GeographicInfoLoader.getInstance().importPlaceFromString(json.getAsString());
    }
}