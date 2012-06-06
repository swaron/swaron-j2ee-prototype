package org.app.repo.jdbc.dao;

import static org.junit.Assert.fail;

import org.app.repo.jpa.BaseRepositoryTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class GenericJdbcDaoTest extends BaseRepositoryTest{

    @Autowired
    GenericJdbcDao genericJdbcDao;
    
    @Test
    public void test() {
//        simpleJdbcTemplate.queryForInt("select * from code_dictionary where sort_order like ?", "2");
        simpleJdbcTemplate.queryForInt("select * from code_lookup where column_id like ?", "2");
    }

}
