package org.app.repo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.sql.DataSource;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.reflect.FieldUtils;
import org.app.framework.paging.PagingParam;
import org.app.integration.spring.DbNameMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Service;

@Service
public class SimpleSqlBuilder {
	protected static Logger logger = LoggerFactory.getLogger(SimpleSqlBuilder.class);

	DbNameMapper dbNameMapper = DbNameMapper.getInstance();

	@Autowired
	SqlChecker sqlChecker;

	protected static String replaceTokens(String text, Object bean) {
		Pattern pattern = Pattern.compile(":(\\S+)");
		Matcher matcher = pattern.matcher(text);
		StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			try {
				Object replacement = FieldUtils.readDeclaredField(bean, matcher.group(1), true);
				if (replacement != null) {
					// in matcher.appendReplacement(buffer, replacement);
					// $1, $2 in replacement will be treated as capture group.
					matcher.appendReplacement(buffer, "");
					buffer.append(replacement);
				}

			} catch (IllegalArgumentException e) {
				logger.info("Unable to find field from bean to replace place holder in sql", e);
			} catch (IllegalAccessException e) {
				logger.info("Unable to find field from bean to replace place holder in sql", e);
			}
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}

	protected static String replaceTokens(String text, Map<String, String> replacements) {
		Pattern pattern = Pattern.compile(":(\\w+)");
		Matcher matcher = pattern.matcher(text);
		StringBuffer buffer = new StringBuffer();
		while (matcher.find()) {
			String replacement = replacements.get(matcher.group(1));
			if (replacement != null) {
				// matcher.appendReplacement(buffer, replacement);
				// use this in case there are '$1' in replacement 
				matcher.appendReplacement(buffer, "");
				buffer.append(replacement);
			}
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}

	public String count(DataSource dataSource, String tableName, PagingParam pagingParam) {
		sqlChecker.checkTableName(tableName);
		return "select count(1) from " + tableName + " " + buildFilter(pagingParam);
	}

	public String findAll(DataSource dataSource, String tableName, PagingParam pagingParam) {
		HashMap<String, String> parameters = new HashMap<String, String>();
		sqlChecker.checkTableName(tableName);
		parameters.put("table", tableName);
		parameters.put("start", String.valueOf(pagingParam.getStart()));
		parameters.put("limit", String.valueOf(pagingParam.getLimit()));
		String dbName = dbNameMapper.getDbName(dataSource);
		if (dbName == null) {
			logger.info("Unsupported database type for DataSource [ {} ], availabe types are: {}", dataSource,
					dbNameMapper.getErrorCodesMap().keySet());
		}
		String template = "";
		if (pagingParam.getStart() != null && pagingParam.getLimit() != null) {
			if (dbName.equals("MySQL") || dbName.equals("PostgreSQL") || dbName.equals("H2")) {
				template = "select t.* from :table as t :where_clause :order_clause limit :limit offset :start";
			} else if (dbName.equals("Oracle")) {
				template = "select t.* from (select rownum as _index, t1.* form :table as t1 :where_clause :order_clause) t  where _index >= :start and _index < (:start + :limit)";
			} else if (dbName.equals("DB2") || dbName.equals("MS-SQL") || dbName.equals("Derby")) {
				template = "select t.* from (select row_number() over() as _index, t1.* form :table as t1 :where_clause :order_clause) t  where _index >= :start and _index < (:start + :limit)";
			} else if (dbName.equals("HSQL")) {
				template = "select limit :start :limit t.* from :table as t :where_clause :order_clause)";
			} else if (dbName.equals("Sybase")) {
				template = "select t.* from (select rank() as _index, t1.* form :table as t1 :where_clause :order_clause) t  where _index >= :start and _index < (:start + :limit)";
			} else if (dbName.equals("Informix")) {
				template = "select skip :start first :limit t.* from :table as t :where_clause :order_clause";
			}
		} else {
			template = "select t.* from :table as t :where_clause :order_clause";
		}
		parameters.put("order_clause", buildOrder(pagingParam));
		parameters.put("where_clause", buildFilter(pagingParam));
		template = replaceTokens(template, parameters);
		return replaceTokens(template, parameters);
	}

	private String buildFilter(PagingParam pagingParam) {
	    LinkedHashMap<String, String> filters = pagingParam.getFilter();
	    List<String> tokens = new ArrayList<String>(2);
	    if(filters == null){
            return "";
        }
	    for (Entry<String, String> sort : filters.entrySet()) {
	        String key = sort.getKey();
	        String value = sort.getValue();
	        sqlChecker.checkColName(key);
	        sqlChecker.checkColValue(value);
	        tokens.add(key + " like '%" + value + "%'");
	    }
	    if(!tokens.isEmpty()){
	        return "where " + StringUtils.join(tokens, " and ");
	    }else{
	        return "";
	    }
    }

    private String buildOrder(PagingParam pagingParam) {
        LinkedHashMap<String, String> sorts = pagingParam.getSort();
        List<String> tokens = new ArrayList<String>(2);
        if(sorts == null){
            return "";
        }
        for (Entry<String, String> sort : sorts.entrySet()) {
            String key = sort.getKey();
            String value = sort.getValue();
            sqlChecker.checkColName(key);
            if (value.equalsIgnoreCase("asc")) {
                tokens.add(key + " asc");
            } else {
                tokens.add(key + " desc");
            }
        }
        if(!tokens.isEmpty()){
            return "order by " + StringUtils.join(tokens, ',');
        }else{
            return "";
        }
    }

    public UpdateStatement update(DataSource dataSource, String tableName, String pKeyCol,
			HashMap<String, SqlParameterValue> sqlParams) {
		StringBuffer buffer = new StringBuffer();
		HashMap<String, String> parameters = new HashMap<String, String>();
		Set<String> keySet = sqlParams.keySet();
		List<String> keys = new ArrayList<String>();
		List<Object> values = new ArrayList<Object>();
		for (String key : keySet) {
			// skip primary key, we will use primary key in where clause
			if (key.equals(pKeyCol)) {
				continue;
			}
			sqlChecker.checkColName(key);
			keys.add(key + " = ?");
			values.add(sqlParams.get(key));
		}
		sqlChecker.checkTableName(tableName);
		parameters.put("col_fragment", StringUtils.join(keys, ","));
		parameters.put("table", tableName);
		sqlChecker.checkColName(pKeyCol);
		parameters.put("pKeyCol", pKeyCol);
		String template = "update :table set :col_fragment where :pKeyCol = ?";
		values.add(sqlParams.get(pKeyCol));
		buffer.append(replaceTokens(template, parameters));
		Object[] args = values.toArray(new Object[values.size()]);
		return new UpdateStatement(buffer.toString(), args);
	}
    
    public UpdateStatement insert(DataSource dataSource, String tableName, String pKeyCol,
            HashMap<String, SqlParameterValue> sqlParams) {
        StringBuffer buffer = new StringBuffer();
        HashMap<String, String> parameters = new HashMap<String, String>();
        Set<String> keySet = sqlParams.keySet();
        List<String> keys = new ArrayList<String>();
        List<String> holders = new ArrayList<String>();
        List<Object> values = new ArrayList<Object>();
        for (String key : keySet) {
            if (key.equals(pKeyCol) ) {
                // primary key specified,allow this
            }
            sqlChecker.checkColName(key);
            keys.add(key);
            holders.add("?");
            values.add(sqlParams.get(key));
        }
        sqlChecker.checkTableName(tableName);
        parameters.put("key_fragment", StringUtils.join(keys, ","));
        parameters.put("val_fragment", StringUtils.join(holders, ","));
        parameters.put("table", tableName);
        sqlChecker.checkColName(pKeyCol);
        String template = "insert into :table (:col_fragment) values (:val_fragment)";
        buffer.append(replaceTokens(template, parameters));
        Object[] args = values.toArray(new Object[values.size()]);
        return new UpdateStatement(buffer.toString(), args);
    }
    
    public UpdateStatement delete(DataSource dataSource, String tableName, String pKeyCol, Integer id) {
        StringBuffer buffer = new StringBuffer();
        List<Object> values = new ArrayList<Object>();
        values.add(id);
        HashMap<String, String> parameters = new HashMap<String, String>();
        parameters.put("table", tableName);
        parameters.put("pKeyCol", pKeyCol);
        String template = "delete from :table  where :pKeyCol = ?";
        buffer.append(replaceTokens(template, parameters));
        Object[] args = values.toArray(new Object[values.size()]);
        return new UpdateStatement(buffer.toString(), args);
    }

	public static class UpdateStatement {
		public final String sql;
		public final Object[] args;

		public UpdateStatement(String sql, Object[] args) {
			this.sql = sql;
			this.args = args;
		}
	}

}
