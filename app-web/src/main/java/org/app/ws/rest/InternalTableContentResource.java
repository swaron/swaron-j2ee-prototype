package org.app.ws.rest;

import java.io.IOException;

import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;

import org.app.framework.paging.PagingAssembler;
import org.app.framework.paging.PagingForm;
import org.app.framework.paging.PagingParam;
import org.app.framework.paging.PagingResult;
import org.app.repo.jpa.dao.GenericDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "/rest/internal-table-content", produces = { "application/json", "application/xml" })
public class InternalTableContentResource {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	GenericDao genericDao;

	@Autowired
	PagingAssembler pagingAssembler;

	@Autowired
	ObjectMapper objectMapper;

	@RequestMapping(value = "/{tableName}", method = RequestMethod.POST)
	@ResponseBody
	public PagingResult<Object> create(@RequestParam String tableName, HttpServletRequest request) throws IOException {
		Class<?> clazz = genericDao.getPersistentClassByName(tableName);
		if (clazz == null) {
			throw new TypeMismatchException(tableName, Entity.class);
		}
		try {
			ServerHttpRequest input = new ServletServerHttpRequest(request);
			Object entity = objectMapper.readValue(input.getBody(), clazz);
			genericDao.persist(entity);
			return PagingResult.fromSingleResult(entity);
		} catch (JsonProcessingException ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
		}
	}

	@RequestMapping(value = "/{tableName}", method = RequestMethod.GET)
	@ResponseBody
	public PagingResult<?> read(@PathVariable String tableName, PagingForm form) {
		Class<? extends Object> clazz = genericDao.getPersistentClassByName(tableName);
		if (clazz == null) {
			throw new TypeMismatchException(tableName, Entity.class);
		}
		PagingParam pagingParam = pagingAssembler.toPagingParam(form);
		return genericDao.findPaging(clazz, pagingParam);
	}

	@RequestMapping(value = "/{tableName}/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public PagingResult<Object> update(@PathVariable String tableName, @PathVariable String id,
			HttpServletRequest request) throws IOException {
		Class<?> clazz = genericDao.getPersistentClassByName(tableName);
		if (clazz == null) {
			throw new TypeMismatchException(tableName, Entity.class);
		}
		try {
			ServerHttpRequest input = new ServletServerHttpRequest(request);
			Object entity = objectMapper.readValue(input.getBody(), clazz);
			Object merge = genericDao.merge(entity);
			return PagingResult.fromSingleResult(merge);
		} catch (JsonProcessingException ex) {
			throw new HttpMessageNotReadableException("Could not read JSON: " + ex.getMessage(), ex);
		}

	}

	@RequestMapping(value = "/{tableName}/{d}", method = RequestMethod.DELETE)
	@ResponseBody
	public void delete(@PathVariable String tableName, @PathVariable Integer id) {
		Class<?> clazz = genericDao.getPersistentClassByName(tableName);
		if (clazz == null) {
			throw new TypeMismatchException(tableName, Entity.class);
		}
		genericDao.remove(clazz, id);
	}

}
