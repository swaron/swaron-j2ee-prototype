package org.app.repo.service;

import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.app.repo.jpa.model.DbInfo;
import org.app.repo.jpa.vo.ColumnMetaData;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.RowMapperResultSetExtractor;
import org.springframework.stereotype.Service;

@Service
public class CustomDbManager {
    Logger logger = LoggerFactory.getLogger(getClass());

    private JdbcTemplate jdbcTemplate;

    private HashMap<Integer, DataSource> dataSources = new HashMap<Integer, DataSource>();

    @Autowired
    @Qualifier("dataSource_app")
    public void setDataSource(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    public JdbcTemplate getJdbcTemplate(DbInfo dbInfo) {
        return new JdbcTemplate(getDataSource(dbInfo));
    }

    public DataSource getDataSource(DbInfo dbInfo) {
        Integer dbInfoId = dbInfo.getDbInfoId();
        DataSource dataSource = dataSources.get(dbInfoId);
        if (dataSource == null) {
            dataSource = buildDataSource(dbInfo);
            dataSources.put(dbInfoId, dataSource);
        }
        return dataSource;
    }

    private DataSource buildDataSource(DbInfo dbInfo) {
        Properties prop = DbInfoUtils.createProperties(dbInfo);
        try {
            return BasicDataSourceFactory.createDataSource(prop);
        } catch (Exception e) {
            logger.info("unable to create datasource with properties: {}", prop);
            return null;
        }
    }
    public List<ColumnMetaData> getColumnsResults(Integer dbInfoId, String tableName) {
        DataSource dataSource = this.dataSources.get(dbInfoId);
        if(dataSource == null){
            return null;
        }
        try {
            DatabaseMetaData metaData = dataSource.getConnection().getMetaData();
            ResultSet columns = metaData.getColumns(null, null, tableName, "");
            RowMapper<ColumnMetaData> rowMapper = BeanPropertyRowMapper.newInstance(ColumnMetaData.class);
            RowMapperResultSetExtractor<ColumnMetaData> resultSetExtractor = new RowMapperResultSetExtractor<ColumnMetaData>(rowMapper);
            List<ColumnMetaData> list = resultSetExtractor.extractData(columns);
            columns.close();
            return list;
        } catch (SQLException e) {
            logger.warn("Error while accessing table meta data results",e);
            return null;
        }

    }
    @PreDestroy
    public void destroy() throws SQLException {
        for (Entry<Integer, DataSource> e : dataSources.entrySet()) {
            DataSource ds = e.getValue();
            if (ds instanceof BasicDataSource) {
                ((BasicDataSource) ds).close();
            }
        }
    }

}
