package org.app.repo.jpa;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.junit.Test;
import org.springframework.test.annotation.Rollback;

public class BlankTest extends BaseRepositoryTest {

    @PersistenceContext
    EntityManager entityManager;
    
    @Test
    @Rollback(false)
    public void doNothing() throws Exception {
        logger.debug("a blank test,test startup process.");
    }
}
