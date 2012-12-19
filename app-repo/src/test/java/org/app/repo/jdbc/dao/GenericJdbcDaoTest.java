package org.app.repo.jdbc.dao;

import org.app.repo.jpa.BaseRepositoryTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GenericJdbcDaoTest extends BaseRepositoryTest{

    @Autowired
    GenericJdbcDao genericJdbcDao;
    
    @Test
    public void test() {
//        simpleJdbcTemplate.queryForInt("select * from code_dictionary where sort_order like ?", "2");
        jdbcTemplate.queryForInt("select * from code_lookup where column_id like ?", "2");
    }

}
