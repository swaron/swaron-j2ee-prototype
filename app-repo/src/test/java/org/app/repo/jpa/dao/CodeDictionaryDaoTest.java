package org.app.repo.jpa.dao;

import static org.junit.Assert.*;

import org.app.repo.jpa.BaseRepositoryTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class CodeDictionaryDaoTest  extends BaseRepositoryTest{
	@Autowired
	CodeDictionaryDao codeDictionaryDao;
	
	@Test
	public void testFindAliasCount() {
		long findAliasCount = codeDictionaryDao.findAliasCount("sec_user", "login_state");
		assertTrue(findAliasCount > 0);
		logger.debug("alias count: " + findAliasCount);
	}

}
