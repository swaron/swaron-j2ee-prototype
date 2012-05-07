package org.app.ws.rest;

import java.util.Arrays;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.annotation.Resource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.text.StrTokenizer;
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
    public static void main(String[] args) {
        String value = "<=123.2";
        Pattern pattern = Pattern.compile("([<>=]+)\\s?([\\d.]+)");
        Matcher matcher = pattern.matcher(value);
        System.out.println(matcher.groupCount());
//        matcher.matches();
//        matcher.find();
        if(matcher.groupCount() < 2){
            System.out.println(matcher.groupCount());
        }
        String operator = matcher.group(1);
        String opValue = matcher.group(2);
        System.out.println(operator);
        System.out.println(opValue);
    }
}
