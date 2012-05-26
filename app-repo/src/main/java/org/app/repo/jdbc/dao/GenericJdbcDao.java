package org.app.repo.jdbc.dao;

import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;

import org.app.framework.paging.PagingParam;
import org.app.framework.paging.PagingResult;
import org.app.integration.spring.DbNameMapper;
import org.app.repo.service.SimpleSqlBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.SQLErrorCodes;
import org.springframework.jdbc.support.SQLErrorCodesFactory;
import org.springframework.stereotype.Repository;

@Repository
public class GenericJdbcDao {
	@Autowired
	SimpleSqlBuilder sqlBuilder;
	
    public PagingResult<?> findPaing(DataSource dataSource, String tableName, PagingParam pagingParam) {
    	String countSql = sqlBuilder.count(dataSource, tableName, pagingParam);
    	String findAllSql = sqlBuilder.findAll(dataSource, tableName, pagingParam);
    	
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        RowMapper<Map<String, Object>> rowMapper = new ColumnMapRowMapper();
        List<Map<String, Object>> resultList = jdbcTemplate.query(findAllSql, rowMapper);
        long total = jdbcTemplate.queryForLong(countSql);
        PagingResult<Map<String, Object>> result = new PagingResult<Map<String, Object>>(resultList, (int) total);
        return result;
    }
}
