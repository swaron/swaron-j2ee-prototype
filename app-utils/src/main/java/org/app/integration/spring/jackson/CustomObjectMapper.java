package org.app.integration.spring.jackson;

import java.sql.Timestamp;

import org.codehaus.jackson.Version;
import org.codehaus.jackson.map.ObjectMapper;
import org.codehaus.jackson.map.module.SimpleModule;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.module.hibernate.HibernateModule;

@Component
public class CustomObjectMapper extends ObjectMapper {
    public CustomObjectMapper() {
        registerModule(new HibernateModule());

        SimpleModule timestampModule = new SimpleModule("TimestampModule", new Version(1, 0, 0, null));
        timestampModule.addDeserializer(Timestamp.class, new TimestampDeserializer());
        registerModule(timestampModule);
        
        configure(org.codehaus.jackson.map.DeserializationConfig.Feature.FAIL_ON_UNKNOWN_PROPERTIES, false);
        configure(org.codehaus.jackson.map.DeserializationConfig.Feature.ACCEPT_EMPTY_STRING_AS_NULL_OBJECT, true);

        configure(org.codehaus.jackson.map.SerializationConfig.Feature.FAIL_ON_EMPTY_BEANS, false);
        configure(org.codehaus.jackson.map.SerializationConfig.Feature.INDENT_OUTPUT, true);
    }
}
