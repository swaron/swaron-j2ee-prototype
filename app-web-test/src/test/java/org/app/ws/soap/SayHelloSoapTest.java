package org.app.ws.soap;

import javax.annotation.Resource;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.app.schemas.domain1.category1.v10.QueryOrderRequest;
import org.app.schemas.domain1.category1.v10.QueryOrderResponse;
import org.app.web.BaseTest;
import org.junit.Test;
import org.springframework.web.client.RestTemplate;
import org.springframework.ws.client.core.WebServiceTemplate;

public class SayHelloSoapTest extends BaseTest{
    @Resource(name="jsonTemplate")
    RestTemplate jsonTemplate;
    
    @Resource(name="webServiceTemplate")
    WebServiceTemplate webServiceTemplate;
    String echoUrl = "http://localhost:8080/app-ws-soap/soap";
    
    @Test
    public void testEcho() {
        QueryOrderRequest request = new QueryOrderRequest();
        QueryOrderResponse response = (QueryOrderResponse) webServiceTemplate.marshalSendAndReceive(echoUrl, request);
        System.out.println(ToStringBuilder.reflectionToString(response));
    }
}
