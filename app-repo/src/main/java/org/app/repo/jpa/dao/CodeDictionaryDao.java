package org.app.repo.jpa.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.app.framework.paging.PagingParam;
import org.app.framework.paging.PagingResult;
import org.app.repo.jdbc.dao.GenericJdbcDao;
import org.app.repo.jpa.po.CodeDictionary;
import org.app.repo.jpa.po.SystemConfig;
import org.app.repo.jpa.po.SystemConfig_;
import org.hibernate.Session;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.orm.hibernate4.SessionFactoryUtils;
import org.springframework.stereotype.Repository;

/**
 * Code Dictionary contians two type of code. <br>
 * "enum code" are defined in table code_dictionary. <br>
 * "type code" are defined in many type table, these tables are specified in
 * "system_config" table under key "CodeDictionaryDao.TypeCode", separated by
 * comma. for example, here is a sample value in param_value column of system_config table:"OrganizationType, AddressType"
 * 
 * @author aaron
 * 
 */
@Repository
public class CodeDictionaryDao extends AbstractDaoSupport {
	
	public static final String TYPECODE_SYSTEM_CONFIG_KEY = "CodeDictionaryDao.TypeCode";
	private static final int MAX_TYPETABLE_SIZE = 1000;

	protected final Logger logger = LoggerFactory.getLogger(getClass());
	@Autowired
	GenericJdbcDao genericJdbcDao;
	

	public long findAliasCount(String table, String column) {
		PagingParam pagingParam = new PagingParam();
		pagingParam.addFilter("tableName", table);
		pagingParam.addFilter("columnName", column);
		return countAll(CodeDictionary.class, pagingParam);
	}

	public boolean isTypeTable(String table) {
		List<SystemConfig> list = findAllByCol(SystemConfig.class, SystemConfig_.paramKey.getName(), TYPECODE_SYSTEM_CONFIG_KEY);
//		List<String> tables = new ArrayList<String>();
		for (SystemConfig systemConfig : list) {
			String paramValue = systemConfig.getParamValue();
			String[] split = StringUtils.split(paramValue, ", ");
			if(split == null){
				continue;
			}
			for (String tableToken : split) {
				if(StringUtils.isNotBlank(tableToken) && tableToken.equals(table)){
					return true;
//					tables.add(tableToken);
				}
			}
		}
		return false;
	}
	public HashMap<String, PagingResult<Map<String, Object>>> dumpTypeCodes() {
		List<SystemConfig> list = findAllByCol(SystemConfig.class, SystemConfig_.paramKey.getName(), TYPECODE_SYSTEM_CONFIG_KEY);
		List<String> tables = new ArrayList<String>();
		for (SystemConfig systemConfig : list) {
			String paramValue = systemConfig.getParamValue();
			String[] split = StringUtils.split(paramValue, ", ");
			if(split == null){
				continue;
			}
			for (String tableToken : split) {
				if(StringUtils.isNotBlank(tableToken)){
					tables.add(tableToken);
				}
			}
		}
		Session session = entityManager.unwrap(Session.class);
		DataSource dataSource = SessionFactoryUtils.getDataSource(session.getSessionFactory());
		//if too many types are returned and transfer to client. the performance will be impact.
		PagingParam pagingParam = new PagingParam();
		pagingParam.setStart(0);
		pagingParam.setLimit(MAX_TYPETABLE_SIZE);
		HashMap<String, PagingResult<Map<String, Object>>> resultMap = new HashMap<String, PagingResult<Map<String, Object>>>();
		for (String table : tables) {
			PagingResult<Map<String, Object>> result = genericJdbcDao.findPaing(dataSource, table, pagingParam);
			if(result.getTotal() > result.getRecords().size()){
				logger.warn("the type table is too large, and not all type are return to client side.");
			}
			resultMap.put(table, result);
		}
		return resultMap;
	}
}
