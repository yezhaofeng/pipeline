package com.jlu.common.deserializer;

import java.io.IOException;

import org.codehaus.jackson.JsonGenerator;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.JsonSerializer;
import org.codehaus.jackson.map.SerializerProvider;
import org.codehaus.jettison.json.JSONObject;

import com.jlu.common.utils.JsonUtils;

/**
 * Created by baidu on 15/11/18.
 */
public class JSONObjectSerializer extends JsonSerializer<JSONObject> {

    @Override
    public void serialize(JSONObject value, JsonGenerator jgen, SerializerProvider provider)
            throws IOException, JsonProcessingException {

        jgen.writeTree(JsonUtils.getJsonTree(value.toString()));

    }
}
