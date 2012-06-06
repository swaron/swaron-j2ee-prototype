package org.app.application.grid;

import java.util.HashMap;
import java.util.List;
import java.util.Properties;

import javax.sql.DataSource;

import org.app.application.assemble.DbInfoAssembler;
import org.app.domain.grid.service.DbMetaDataService;
import org.app.domain.grid.vo.ColumnMetaData;
import org.app.domain.grid.vo.TableMetaData;
import org.app.repo.jpa.dao.DbInfoDao;
import org.app.repo.jpa.model.DbInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class GridService {
    protected Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DbInfoDao dbInfoDao;

    @Autowired
    DbInfoAssembler dbInfoAssembler;

    @Autowired
    DbMetaDataService dbService;

    public List<TableMetaData> getTableMetaDatas(Integer dbInfoId) {
        DataSource dataSource = ensureDataSource(dbInfoId);
        return dbService.getTableMetaDatas(dataSource);
    }

    public TableMetaData getTableMetaData(Integer dbInfoId, String tableName) {
        DataSource dataSource = ensureDataSource(dbInfoId);
        return dbService.getTableMetaData(dataSource, tableName);
    }

    public List<ColumnMetaData> getColumnMetaDatas(Integer dbInfoId, String tableName) {
        DataSource dataSource = ensureDataSource(dbInfoId);
        return dbService.getColumnMetaDatas(dataSource, tableName);
    }

    public DataSource ensureDataSource(Integer dbInfoId) {
        DbInfo dbInfo = dbInfoDao.find(DbInfo.class, dbInfoId);
        String key = dbInfoAssembler.createKey(dbInfo);
		DataSource dataSource = dbService.getDataSource(key);
        if (dataSource == null) {
            syncDatasource();
            dataSource = dbService.getDataSource(key);
        }
        return dataSource;
    }

    private void syncDatasource() {
		List<DbInfo> dbInfos = dbInfoDao.findAll(DbInfo.class);
		HashMap<String, Properties> infos = new HashMap<String, Properties>();
		for (DbInfo dbInfo : dbInfos) {
			infos.put(dbInfoAssembler.createKey(dbInfo), dbInfoAssembler.createProperties(dbInfo));
			dbService.syncDataSource(infos);
		}
	}

}
