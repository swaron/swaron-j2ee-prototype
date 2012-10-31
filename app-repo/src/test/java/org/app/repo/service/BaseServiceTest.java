package org.app.repo.service;

import org.app.test.ExposeTestContextTestExecutionListener;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestExecutionListeners;
import org.springframework.test.context.junit4.AbstractJUnit4SpringContextTests;

@ContextConfiguration({ "classpath:/test-context.xml"})
@TestExecutionListeners({ ExposeTestContextTestExecutionListener.class })
public abstract class BaseServiceTest extends AbstractJUnit4SpringContextTests {

}
