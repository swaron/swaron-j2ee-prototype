package org.app.integration.spring.mvc;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurationSupport;
import org.springframework.web.servlet.mvc.UrlFilenameViewController;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;

/**
 * use either <mvc:annotation-driven />  or @Configuration @EnableWebMvc WebConfig
 * @author swaron
 *
 */
//@Configuration
//@EnableWebMvc
@Component
public class WebConfig extends WebMvcConfigurationSupport{
    @Autowired(required=false)
    private RequestMappingHandlerMapping requestMappingHandlerMapping;

    @PostConstruct
    public void init() {
        if(requestMappingHandlerMapping != null){
        	requestMappingHandlerMapping.setDefaultHandler(defaultHandler());
        }
    }
    
	@Bean
	public UrlFilenameViewController defaultHandler() {
		UrlFilenameViewController defaultHandler = new UrlFilenameViewController();
		return defaultHandler;
	}

}
