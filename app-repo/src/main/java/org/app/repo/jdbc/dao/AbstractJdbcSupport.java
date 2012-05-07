package org.app.repo.jdbc.dao;

import javax.sql.DataSource;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.JdbcTemplate;

public abstract class AbstractJdbcSupport {
	protected JdbcTemplate jdbcTemplate;
//    protected SimpleJdbcInsert jdbcInsert;

    @Autowired
    @Qualifier("dataSource_app")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
//        this.jdbcInsert = new SimpleJdbcInsert(dataSource).withTableName("user").usingGeneratedKeyColumns("user_id");
    }

}
