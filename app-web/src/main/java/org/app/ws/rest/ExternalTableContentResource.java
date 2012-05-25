package org.app.ws.rest;

import java.io.IOException;

import javax.persistence.Entity;
import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.app.application.assemble.DbInfoAssembler;
import org.app.application.service.DbMetaDataService;
import org.app.domain.grid.service.ExternalDbService;
import org.app.framework.paging.PagingAssembler;
import org.app.framework.paging.PagingForm;
import org.app.framework.paging.PagingParam;
import org.app.framework.paging.PagingResult;
import org.app.repo.jdbc.dao.GenericJdbcDao;
import org.codehaus.jackson.JsonProcessingException;
import org.codehaus.jackson.map.ObjectMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.TypeMismatchException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/external-table-content", produces = { "application/json", "application/xml" })
public class ExternalTableContentResource {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	GenericJdbcDao genericDao;

	@Autowired
	PagingAssembler pagingAssembler;

	@Autowired
	ObjectMapper objectMapper;

    @Autowired
    DbInfoAssembler dbInfoAssembler;
    
    @Autowired
    ExternalDbService dbService;
    
    @Autowired
    DbMetaDataService metaDataService;
    
	@RequestMapping(value = "/{dbInfoId}/{tableName}", method = RequestMethod.POST)
	@ResponseBody
	public PagingResult<Object> create(@PathVariable Integer dbInfoId, @PathVariable String tableName, HttpServletRequest request) throws IOException {
        return null;
	}

	@RequestMapping(value = "/{dbInfoId}/{tableName}", method = RequestMethod.GET)
	@ResponseBody
	public PagingResult<?> read(@PathVariable Integer dbInfoId, @PathVariable String tableName, PagingForm form) {
	    DataSource dataSource = metaDataService.ensureDataSource(dbInfoId);
	    JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
	    PagingParam pagingParam = pagingAssembler.toPagingParam(form);
	    return genericDao.findPaing(dataSource, tableName, pagingParam);
	}

	@RequestMapping(value = "/{dbInfoId}/{tableName}/{id}", method = RequestMethod.PUT)
	@ResponseBody
	public PagingResult<Object> update(@PathVariable Integer dbInfoId, @PathVariable String tableName, @PathVariable String id,
			HttpServletRequest request) throws IOException {
                return null;

	}

	@RequestMapping(value = "/{dbInfoId}/{tableName}/{id}", method = RequestMethod.DELETE)
	@ResponseBody
	public void delete(@PathVariable Integer dbInfoId, @PathVariable String tableName, @PathVariable Integer id) {
	}

}
