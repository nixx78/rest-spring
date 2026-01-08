package lv.nixx.poc.rest.configuration;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import org.springframework.boot.jackson.JacksonComponent;

import java.io.IOException;

@JacksonComponent
public class TrimStringDeserializer extends StdDeserializer<String> {

    public TrimStringDeserializer() {
        super(String.class);
    }

    @Override
    public String deserialize(JsonParser p, DeserializationContext ctxt)   throws IOException {
        String value = p.getValueAsString();
        return value == null ? null : value.trim();
    }
}

