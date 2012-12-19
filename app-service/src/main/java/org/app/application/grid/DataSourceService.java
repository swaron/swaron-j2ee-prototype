package org.app.application.grid;

import java.sql.SQLException;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;
import java.util.Properties;

import javax.annotation.PreDestroy;
import javax.sql.DataSource;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.BasicDataSourceFactory;
import org.app.repo.jpa.dao.DbInfoDao;
import org.app.repo.jpa.po.DbInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DataSourceService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DbInfoDao dbInfoDao;
    @Autowired
    DbInfoAssembler dbInfoAssembler;
    @Autowired
    DatabaseMetaDataService dbService;

    private HashMap<String, DataSource> dataSources = new HashMap<String, DataSource>();

    public DataSource getDataSource(Integer dbInfoId) {
        DbInfo dbInfo = dbInfoDao.find(DbInfo.class, dbInfoId);
        String key = dbInfoAssembler.createKey(dbInfo);
        DataSource dataSource = dataSources.get(key);
        if (dataSource == null) {
            // close not used data source at the same time
            closeNotExistDatasource();
            dataSource = createDataSource(key, dbInfo);
            dataSources.put(key, dataSource);
        }
        return dataSource;
    }

    private void closeNotExistDatasource() {
        List<DbInfo> dbInfos = dbInfoDao.findAll(DbInfo.class);
        HashMap<String, Properties> infos = new HashMap<String, Properties>();
        for (DbInfo dbInfo : dbInfos) {
            infos.put(dbInfoAssembler.createKey(dbInfo), dbInfoAssembler.createProperties(dbInfo));
        }
        @SuppressWarnings("unchecked")
		Collection<String> toRemove = CollectionUtils.subtract(dataSources.keySet(), infos.keySet());
        for (String key : toRemove) {
            DataSource dataSource = dataSources.remove(key);
            if (dataSource instanceof BasicDataSource) {
                try {
                    ((BasicDataSource) dataSource).close();
                } catch (SQLException e) {
                    logger.warn("unable to close datasource", e);
                }
            }
        }
    }

    public synchronized DataSource createDataSource(String key, DbInfo dbInfo) {
        try {
            Properties prop = dbInfoAssembler.createProperties(dbInfo);
            DataSource dataSource = BasicDataSourceFactory.createDataSource(prop);
            return dataSource;
        } catch (Exception e) {
            logger.info("unable to create datasource with properties: {}", dbInfo);
            return null;
        }
    }

    @PreDestroy
    public void destroy() throws SQLException {
        for (Entry<String, DataSource> e : dataSources.entrySet()) {
            DataSource ds = e.getValue();
            if (ds instanceof BasicDataSource) {
                ((BasicDataSource) ds).close();
            }
        }
    }

}
