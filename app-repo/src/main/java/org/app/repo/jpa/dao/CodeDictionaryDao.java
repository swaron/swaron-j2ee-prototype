package org.app.repo.jpa.dao;

import java.util.LinkedHashMap;

import org.app.framework.paging.PagingParam;
import org.app.repo.jpa.model.CodeDictionary;
import org.springframework.stereotype.Repository;

@Repository
public class CodeDictionaryDao extends AbstractDaoSupport {

	public long findAliasCount(String table, String column) {
		PagingParam pagingParam = new PagingParam();
		LinkedHashMap<String, String> filters = new LinkedHashMap<String, String>();
		filters.put("tableName", table);
		filters.put("columnName", column);
		pagingParam.setFilter(filters);
		return countAll(CodeDictionary.class, pagingParam);
	}
}
