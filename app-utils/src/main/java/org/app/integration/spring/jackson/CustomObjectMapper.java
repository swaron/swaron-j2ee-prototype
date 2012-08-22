package org.app.integration.spring.jackson;

import java.sql.Timestamp;

import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.Version;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.datatype.hibernate4.Hibernate4Module;

@Component
public class CustomObjectMapper extends ObjectMapper {
    public CustomObjectMapper() {
    	registerModule(new Hibernate4Module());

        SimpleModule timestampModule = new SimpleModule("TimestampModule", new Version(1, 0, 0, null,"org.app","app-utils"));
        timestampModule.addDeserializer(Timestamp.class, new TimestampDeserializer());
        registerModule(timestampModule);
        
        configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        configure(DeserializationFeature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        configure(SerializationFeature.FAIL_ON_EMPTY_BEANS, false);
        configure(SerializationFeature.INDENT_OUTPUT, true);
    }
}
