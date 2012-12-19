package org.app.repo.jpa;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.AbstractTransactionalJUnit4SpringContextTests;
import org.springframework.test.context.transaction.TransactionConfiguration;

@ContextConfiguration({"classpath:/test-context.xml"})
@TransactionConfiguration(transactionManager="appTransactionManager")
public abstract class BaseRepositoryTest extends AbstractTransactionalJUnit4SpringContextTests{
	protected JdbcTemplate jdbcTemplate;

	@Autowired
	public void setDataSource(DataSource dataSource) {
		super.setDataSource(dataSource);
		this.jdbcTemplate = new JdbcTemplate(dataSource);
	}
}
