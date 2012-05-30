package org.app.integration.spring.jackson;

import java.io.IOException;
import java.sql.Timestamp;
import java.util.Date;

import org.codehaus.jackson.JsonParser;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.DeserializationContext;
import org.codehaus.jackson.map.deser.std.StdScalarDeserializer;

public class TimestampDeserializer extends StdScalarDeserializer<Timestamp> {
    public TimestampDeserializer() {
        super(Timestamp.class);
    }

    /**
     * fix null pointer exception when value is null.
     */
    @Override
    public java.sql.Timestamp deserialize(JsonParser jp, DeserializationContext ctxt) throws IOException,
            JsonProcessingException {
        Date date = _parseDate(jp, ctxt);
        if(date == null){
            return null;
        }else{
            return new Timestamp(date.getTime());
        }
    }
}
