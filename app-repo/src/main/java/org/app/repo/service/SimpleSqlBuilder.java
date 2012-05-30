package org.app.repo.service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
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
				// see comment
				matcher.appendReplacement(buffer, "");
				buffer.append(replacement);
			}
		}
		matcher.appendTail(buffer);
		return buffer.toString();
	}

	public String count(DataSource dataSource, String tableName, PagingParam pagingParam) {
		sqlChecker.checkTableName(tableName);
		return "select count(1) from " + tableName;
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
		StringBuilder buffer = new StringBuilder();
		if (pagingParam.getStart() != null && pagingParam.getLimit() != null) {
			if (dbName.equals("MySQL") || dbName.equals("PostgreSQL") || dbName.equals("H2")) {
				String template = "select t.* from :table as t limit :limit offset :start";
				buffer.append(replaceTokens(template, parameters));
			} else if (dbName.equals("Oracle")) {
				String template = "select t.* from (select rownum as _index, t1.* form :table as t1) t  where _index >= :start and _index < (:start + :limit)";
				buffer.append(replaceTokens(template, parameters));
			} else if (dbName.equals("DB2") || dbName.equals("MS-SQL") || dbName.equals("Derby")) {
				String template = "select t.* from (select row_number() over() as _index, t1.* form :table as t1) t  where _index >= :start and _index < (:start + :limit)";
				buffer.append(replaceTokens(template, parameters));
			} else if (dbName.equals("HSQL")) {
				String template = "select limit :start :limit t.* from :table as t)";
				buffer.append(replaceTokens(template, parameters));
			} else if (dbName.equals("Sybase")) {
				String template = "select t.* from (select rank() as _index, t1.* form :table as t1) t  where _index >= :start and _index < (:start + :limit)";
				buffer.append(replaceTokens(template, parameters));
			} else if (dbName.equals("Informix")) {
				String template = "select skip :start first :limit t.* from :table as t";
				buffer.append(replaceTokens(template, parameters));
			}
		} else {
			String template = "select t.* from :table as t";
			buffer.append(replaceTokens(template, parameters));
		}
		return buffer.toString();
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

	public static class UpdateStatement {
		public final String sql;
		public final Object[] args;

		public UpdateStatement(String sql, Object[] args) {
			this.sql = sql;
			this.args = args;
		}
	}

}
