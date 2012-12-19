package org.app.ws.rest;

import javax.annotation.Resource;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.app.schemas.domain1.category1.v10.QueryOrderRequest;
import org.app.schemas.domain1.category1.v10.QueryOrderResponse;
import org.app.web.BaseTest;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;

public class AppDomain1RestTest extends BaseTest{
    @Resource(name="jsonTemplate")
    RestTemplate jsonTemplate;
    
    @Resource(name="xmlTemplate")
    RestTemplate xmlTemplate;
    String echoUrl = "http://localhost:8080/app-ws-rest/domain1/category1/echo";
    String printUrl = "http://localhost:8080/app-ws-rest/domain1/category1/print.json";
    
    @Test
    public void testProcess() {
        QueryOrderRequest request = new QueryOrderRequest();
        QueryOrderResponse response = xmlTemplate.postForObject(printUrl, request, QueryOrderResponse.class);
        System.out.println(ToStringBuilder.reflectionToString(response));
    }
    @Test
    public void testPrint() {
    	QueryOrderResponse response = jsonTemplate.getForObject(printUrl, QueryOrderResponse.class);
    	System.out.println(ToStringBuilder.reflectionToString(response));
    }
}
