package org.app.repo.jpa.dao;

import java.io.Serializable;
import java.util.List;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Order;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.app.framework.paging.PagingParam;
import org.app.framework.paging.PagingResult;
import org.app.framework.repo.service.DaoUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;

public abstract class AbstractDaoSupport {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@PersistenceContext(unitName = "app-repo")
	protected EntityManager entityManager;

	protected DaoUtils daoUtils;

	@PostConstruct
	public void init() {
		daoUtils = new DaoUtils(entityManager);
	}

	public <T> List<T> findAll(final Class<T> entityClass) throws DataAccessException {
		CriteriaQuery<T> query = entityManager.getCriteriaBuilder().createQuery(entityClass);
		query.select(query.from(entityClass));
		return entityManager.createQuery(query).getResultList();
	}

	public <T> PagingResult<T> findPaging(Class<T> entityClass, PagingParam pagingParam) {
		List<T> resultList = findAll(entityClass, pagingParam);
		long total = countAll(entityClass, pagingParam);
		PagingResult<T> result = new PagingResult<T>(resultList, (int) total);
		return result;
	}

	public <T> List<T> findAll(Class<T> entityClass, PagingParam pagingParam) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(entityClass);
		Root<T> root = query.from(entityClass);
		List<Predicate> filter = daoUtils.buildFilters(root, pagingParam.getFilter());
		query.where(filter.toArray(new Predicate[filter.size()]));
		List<Order> orders = daoUtils.buildOrders(root, pagingParam.getSort());
		query.select(root).orderBy(orders);
		TypedQuery<T> exeQuery = entityManager.createQuery(query);
		if (pagingParam.getStart() != null && pagingParam.getLimit() != null) {
			exeQuery.setFirstResult(pagingParam.getStart()).setMaxResults(pagingParam.getLimit());
		}
		return exeQuery.getResultList();
	}

	public <T> long countAll(Class<T> entityClass, PagingParam pagingParam) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<Long> query = builder.createQuery(Long.class);
		Root<T> root = query.from(entityClass);
		query.select(builder.count(root));
		List<Predicate> filter = daoUtils.buildFilters(root, pagingParam.getFilter());
		query.where(filter.toArray(new Predicate[filter.size()]));
		TypedQuery<Long> exeQuery = entityManager.createQuery(query);
		Long count = exeQuery.getSingleResult();
		return count;
	}

	public <T> List<T> findAllByCol(Class<T> entityClass, String fieldName, Serializable fieldValue) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(entityClass);
		Root<T> root = query.from(entityClass);
		query.select(root).where(builder.equal(root.<String> get(fieldName), fieldValue));
		return entityManager.createQuery(query).getResultList();
	}


	public <T> T findByCol(Class<T> entityClass, String fieldName, Serializable fieldValue) {
		CriteriaBuilder builder = entityManager.getCriteriaBuilder();
		CriteriaQuery<T> query = builder.createQuery(entityClass);
		Root<T> root = query.from(entityClass);
		query.select(root).where(builder.equal(root.<String> get(fieldName), fieldValue));
		return entityManager.createQuery(query).getSingleResult();
	}

	public <T> T find(Class<T> entityClass, Serializable primaryKey) {
		return entityManager.find(entityClass, primaryKey);
	}

	public void persist(Object entity) {
		entityManager.persist(entity);
	}

	public <T> T merge(T entity) {
		return entityManager.merge(entity);
	}

	public <T> void remove(Class<T> entityClass, Serializable primaryKey) {
		T ref = entityManager.getReference(entityClass, primaryKey);
		entityManager.remove(ref);
	}

}
