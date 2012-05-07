package org.app.repo.jpa.model;

import java.io.IOException;

import org.app.integration.spring.jackson.CustomObjectMapper;
import org.codehaus.jackson.JsonParseException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;

public class CustomObjectMapperTest {
    CustomObjectMapper customObjectMapper = new CustomObjectMapper();
    @Test
    public void testReadValueJsonParserClassOfT() throws JsonParseException, JsonMappingException, IOException {
        String content = "{\"secUserId\":\"\",\"email\":\"e11\",\"enabled\":false,\"username\":\"u11\",\"loginFailedCount\":\"2\",\"updateTime\":\"\"}";
        SecUser readValue = customObjectMapper.readValue(content, SecUser.class);
        System.out.println(readValue.getUpdateTime());
    }

}
