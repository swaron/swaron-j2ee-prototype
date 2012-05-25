package org.app.repo.jdbc.dao;

import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.app.framework.paging.PagingParam;
import org.app.framework.paging.PagingResult;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Repository;

@Repository
public class GenericJdbcDao {
    public PagingResult<?> findPaing(DataSource dataSource, String tableName, PagingParam pagingParam) {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource);
        RowMapper<Map<String, Object>> rowMapper = new ColumnMapRowMapper();
        List<Map<String, Object>> resultList = jdbcTemplate.query("select * from " + tableName, rowMapper);
        long total = jdbcTemplate.queryForLong("select count(*) from " + tableName);
        PagingResult<Map<String, Object>> result = new PagingResult<Map<String, Object>>(resultList, (int) total);
        return result;
    }
}
