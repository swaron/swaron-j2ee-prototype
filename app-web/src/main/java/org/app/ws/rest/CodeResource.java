package org.app.ws.rest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.app.framework.paging.PagingResult;
import org.app.repo.jdbc.dao.GenericJdbcDao;
import org.app.repo.jpa.dao.CodeDictionaryDao;
import org.app.repo.jpa.dao.GenericDao;
import org.app.repo.jpa.po.CodeDictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/code", produces = { "application/json",
		"application/xml" })
public class CodeResource {

	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	CodeDictionaryDao codeDictionaryDao;
	
	@Autowired
	GenericDao genericDao;
	
	@Autowired
	GenericJdbcDao genericJdbcDao;

	/**
	 * this method will return all codes defined in code_directory table, the
	 * CodeDictionary is i18n capable, refer to name field in DbCode.js for detail.
	 * 
	 * @return CodeDictionary list in database
	 */
	@RequestMapping(value = "/dict", method = RequestMethod.GET)
	@ResponseBody
	@Cacheable(value="global.static",key="'CodeResource.dictCode'")
	public List<CodeDictionary> listEnumCode() {
		List<CodeDictionary> list = codeDictionaryDao.findAll(CodeDictionary.class);
		return list;
	}

	@RequestMapping(value = "/type", method = RequestMethod.GET)
	@ResponseBody
	@Cacheable(value="global.static",key="'CodeResource.typeCode'")
	public HashMap<String, PagingResult<Map<String, Object>>> listTypeCode() {
		HashMap<String, PagingResult<Map<String, Object>>> typeCodes = codeDictionaryDao.dumpTypeCodes();
		return typeCodes;
	}

}
