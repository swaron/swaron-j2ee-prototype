package org.app.application;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;

public class BlankTest extends BaseTest {

    @Test
    @Rollback(false)
    public void doNothing() throws Exception {
        logger.debug("a blank test,test startup process.");
    }
}
