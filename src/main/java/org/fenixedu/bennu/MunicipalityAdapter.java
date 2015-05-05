package org.fenixedu.bennu;

import java.lang.reflect.Type;

import org.springframework.core.convert.converter.Converter;

import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import pt.ist.standards.geographic.District;
import pt.ist.standards.geographic.GeographicInfoLoader;
import pt.ist.standards.geographic.Municipality;
import pt.ist.standards.geographic.Place;

/**
 * GSON serialiser/deserialiser for converting Municipality objects.
 */
public class MunicipalityAdapter implements JsonSerializer<Municipality>, JsonDeserializer<Municipality> {
    @Override
    public JsonElement serialize(Municipality src, Type typeOfSrc, JsonSerializationContext context) {

        return new JsonPrimitive(src.exportAsString());
    }

    @Override
    public Municipality deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        return (Municipality) GeographicInfoLoader.getInstance().importPlaceFromString(json.getAsString());
    }
}