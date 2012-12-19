package org.app.repo.service;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.app.repo.jpa.dao.GenericDao;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class DaoUtilsTest extends BaseServiceTest{

	@PersistenceContext(unitName = "app-repo")
	EntityManager entityManager;

	@Autowired
	GenericDao genericDao;
	
	@Test
	public void test() {
		genericDao.getPersistentEntities();
	}

}
