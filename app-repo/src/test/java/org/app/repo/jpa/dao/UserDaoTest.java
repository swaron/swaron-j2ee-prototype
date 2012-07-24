package org.app.repo.jpa.dao;

import java.sql.Timestamp;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.app.repo.jpa.BaseRepositoryTest;
import org.app.repo.jpa.po.SecUser;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.Rollback;

public class UserDaoTest extends BaseRepositoryTest{
	@Autowired
	UserDao userDao;

	@Test
	@Rollback(false)
	public void testPersist() {
		SecUser secUser = new SecUser();
		secUser.setEmail("testemail");
		secUser.setEnabled(true);
		secUser.setUsername("fisrtuser");
		secUser.setPasswd("password");
		secUser.setLoginFailedCount(0);
		userDao.persist(secUser);
		logger.debug(ToStringBuilder.reflectionToString(secUser));
	}
	
	@Test
	@Rollback(false)
	public void testMerge() {
	    SecUser find = userDao.find(SecUser.class, 3);
	    Timestamp updateTime = find.getUpdateTime();
        SecUser secUser = new SecUser();
        secUser.setSecUserId(3);
        secUser.setEmail("testemail");
        secUser.setEnabled(true);
        secUser.setUsername("user");
        secUser.setPasswd("password");
        secUser.setLoginFailedCount(2);
        secUser.setUpdateTime(updateTime);
        userDao.merge(secUser);
        logger.debug(ToStringBuilder.reflectionToString(secUser));
    }

}
