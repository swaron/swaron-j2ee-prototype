package org.app.repo.service;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.sql.DataSource;

import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.ColumnMapRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.jdbc.core.SingleColumnRowMapper;
import org.springframework.jdbc.core.metadata.CallMetaDataContext;
import org.springframework.jdbc.core.metadata.CallMetaDataProvider;
import org.springframework.jdbc.core.metadata.CallMetaDataProviderFactory;
import org.springframework.test.annotation.Rollback;

public class CustomDbManagerTest extends BaseServiceTest {

    private JdbcTemplate jdbcTemplate;
    
    @Autowired
    private CustomDbManager customDbManager;

    @Autowired
    @Qualifier("dataSource_app")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Test
    @Rollback(false)
    public void dbInfo() throws Exception {
        Connection connection = jdbcTemplate.getDataSource().getConnection();
        String catalog = connection.getCatalog();
        DatabaseMetaData metaData = connection.getMetaData();
        ResultSet catalogs = metaData.getCatalogs();
        RowMapper<String> rowMapper = new SingleColumnRowMapper<String>(String.class);
        RowMapperResultSetExtractor<String> resultSetExtractor = new RowMapperResultSetExtractor<String>(rowMapper);
        List<String> list = resultSetExtractor.extractData(catalogs);
        logger.info("catalogs: \n" + list);
        catalogs.close();

        ResultSet schemas = metaData.getSchemas();
        RowMapper<Map<String, Object>> schemaRowMapper = new ColumnMapRowMapper();
        RowMapperResultSetExtractor<Map<String, Object>> resultSetExtractor2 = new RowMapperResultSetExtractor<Map<String, Object>>(
                schemaRowMapper);
        resultSetExtractor2.extractData(schemas);
        logger.info("schemas: \n" + list);
        schemas.close();

        List<String> tableNames = new ArrayList<String>();
        ResultSet tables = null;
        try {
            tables = metaData.getTables(null, null, "", new String[] { "TABLE" });
            while (tables != null && tables.next()) {
                tableNames.add(tables.getString("TABLE_NAME"));
            }
        } catch (SQLException se) {
            logger.warn("Error while accessing table meta data results" + se.getMessage());
        } finally {
            if (tables != null) {
                try {
                    tables.close();
                } catch (SQLException e) {
                    logger.warn("Error while closing table meta data reults" + e.getMessage());
                }
            }
        }
        logger.info("tables: \n" + tableNames);
    }
}
