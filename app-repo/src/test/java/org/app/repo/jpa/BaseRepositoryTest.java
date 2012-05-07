package org.app.repo.jpa;

import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ContextConfiguration({"classpath:/test-context.xml"})
@TransactionConfiguration(transactionManager="appTransactionManager")
public abstract class BaseRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests{

}
