package org.app.web;

import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.mockito.MockitoAnnotations;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration({ "classpath:/test-context.xml" })
public abstract class BaseTest extends AbstractJUnit4SpringContextTests {
    
    protected final Logger logger = LoggerFactory.getLogger(getClass());
    
    @BeforeClass
    public static void initJndi() throws Exception {
        if (SimpleNamingContextBuilder.getCurrentContextBuilder() == null) {
            SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();
            builder.bind("java:comp/env/main_location", "http://172.16.113.211:8090/");
            builder.activate();
            Assert.assertNotNull(SimpleNamingContextBuilder.getCurrentContextBuilder());
        }
    }

    @Before
    public void initMocks() {
        MockitoAnnotations.initMocks(this);
    }
}
