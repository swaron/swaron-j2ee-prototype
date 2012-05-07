package org.app.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration({ "classpath:/test-context.xml" })
public abstract class BaseTest extends AbstractJUnit4SpringContextTests {
    protected final Logger logger = LoggerFactory.getLogger(getClass());
}
