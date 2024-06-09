package com.bcb.vetra.services.deserializers;

import com.fasterxml.jackson.core.JacksonException;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;

import java.io.IOException;
import java.time.DateTimeException;
import java.time.LocalDate;

public class EpochToLocalDateDeserializer extends JsonDeserializer<LocalDate> {

    @Override
    public LocalDate deserialize(JsonParser jsonParser, DeserializationContext deserializationContext) throws IOException, JacksonException {
        String value = jsonParser.getText();
        try {
            long epoch = Long.parseLong(value);
            return LocalDate.ofEpochDay(epoch);
        } catch (NumberFormatException e) {
            try {
                return LocalDate.parse(value);
            } catch (DateTimeException e2) {
                return null;
            }
        }
    }
}
