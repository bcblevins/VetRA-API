package com.bcb.vetra.services.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;

public class SexIdToSexDeserializer extends JsonDeserializer<String> {

    @Override
    public String deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String value = jsonParser.getText();
        try {
            Integer.parseInt(value);
            if (value.equals("1")) {
                return "M";
            } else if (value.equals("2")) {
                return "CM";
            } else if (value.equals("3")) {
                return "F";
            } else if (value.equals("4")) {
                return "SF";
            } else {
                return "?";
            }
        } catch (NumberFormatException e) {
            return value;
        }
    }
}
