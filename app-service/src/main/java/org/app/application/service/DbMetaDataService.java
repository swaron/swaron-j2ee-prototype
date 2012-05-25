package org.app.application.service;

import java.util.List;

import javax.sql.DataSource;

import org.app.application.assemble.DbInfoAssembler;
import org.app.domain.grid.service.ExternalDbService;
import org.app.domain.grid.vo.ColumnMetaData;
import org.app.domain.grid.vo.TableMetaData;
import org.app.repo.jpa.dao.DbInfoDao;
import org.app.repo.jpa.model.DbInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DbMetaDataService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DbInfoDao dbInfoDao;

    @Autowired
    DbInfoAssembler dbInfoAssembler;

    @Autowired
    ExternalDbService dbService;

    public List<TableMetaData> getTableMetaDatas(Integer dbInfoId) {
        DataSource dataSource = ensureDataSource(dbInfoId);
        return dbService.getTableResults(dataSource);
    }

    public TableMetaData getTableMetaData(Integer dbInfoId, String tableName) {
        DataSource dataSource = ensureDataSource(dbInfoId);
        return dbService.getTableMetaData(dataSource, tableName);
    }

    public List<ColumnMetaData> getColumnMetaDatas(Integer dbInfoId, String tableName) {
        DataSource dataSource = ensureDataSource(dbInfoId);
        return dbService.getColumnsResults(dataSource, tableName);
    }

    public DataSource ensureDataSource(Integer dbInfoId) {
        DbInfo dbInfo = dbInfoDao.find(DbInfo.class, dbInfoId);
        DataSource dataSource = dbService.getDataSource(dbInfoAssembler.createKey(dbInfo));
        if (dataSource == null) {
            dataSource = dbService.buildDataSource(dbInfoAssembler.createKey(dbInfo),
                    dbInfoAssembler.createProperties(dbInfo));
        }
        return dataSource;
    }

}
