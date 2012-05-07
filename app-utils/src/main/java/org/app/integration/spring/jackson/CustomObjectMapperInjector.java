package org.app.integration.spring.jackson;

import java.util.List;

import javax.annotation.PostConstruct;

import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;

@Component
public class CustomObjectMapperInjector {
    @Autowired(required=false)
    private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

    @Autowired
    private ObjectMapper objectMapper;

    @PostConstruct
    public void init() {
        if(requestMappingHandlerAdapter != null){
            List<HttpMessageConverter<?>> messageConverters = requestMappingHandlerAdapter.getMessageConverters();
            for (HttpMessageConverter<?> messageConverter : messageConverters) {
                if (messageConverter instanceof MappingJacksonHttpMessageConverter) {
                    MappingJacksonHttpMessageConverter m = (MappingJacksonHttpMessageConverter) messageConverter;
                    m.setObjectMapper(objectMapper);
                }
            }
        }
    }

}
