package org.app.ws.rest;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.sql.DataSource;

import org.app.application.grid.DataSourceService;
import org.app.application.grid.DatabaseMetaDataService;
import org.app.application.grid.DbInfoAssembler;
import org.app.domain.vo.grid.ColumnMetaData;
import org.app.domain.vo.grid.TableMetaData;
import org.app.framework.paging.PagingAssembler;
import org.app.framework.paging.PagingForm;
import org.app.framework.paging.PagingParam;
import org.app.framework.paging.PagingResult;
import org.app.repo.jdbc.dao.GenericJdbcDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.SqlParameterValue;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping(value = "/rest/external-table-content", produces = { "application/json", "application/xml" })
public class ExternalTableContentResource {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    GenericJdbcDao genericDao;

    @Autowired
    PagingAssembler pagingAssembler;

    @Autowired
    ObjectMapper objectMapper;

    @Autowired
    DbInfoAssembler dbInfoAssembler;

    @Autowired
    DatabaseMetaDataService metaDataService;

    @Autowired
    DataSourceService dataSourceService;

    @RequestMapping(value = "/{dbInfoId}/{tableName}", method = RequestMethod.POST)
    @ResponseBody
    public PagingResult<Map<String, Object>> create(@PathVariable Integer dbInfoId, @PathVariable String tableName,
            HttpServletRequest request, @RequestBody Map<String, Object> body) throws IOException {
        DataSource dataSource = dataSourceService.getDataSource(dbInfoId);
        TableMetaData tableMetaData = metaDataService.getTableMetaData(dataSource, tableName);
        List<ColumnMetaData> columnMetaDatas = metaDataService.getColumnMetaDatas(dataSource, tableName);
        HashMap<String, SqlParameterValue> sqlParams = metaDataService.buildSqlParameter(columnMetaDatas, body);
        genericDao.insertRecord(dataSource, tableName, tableMetaData.getPrimaryKeyCol(), sqlParams);
        return PagingResult.fromSingleResult(body);
    }

    @RequestMapping(value = "/{dbInfoId}/{tableName}", method = RequestMethod.GET)
    @ResponseBody
    public PagingResult<Map<String, Object>> read(@PathVariable Integer dbInfoId, @PathVariable String tableName,
            PagingForm form) {
        DataSource dataSource = dataSourceService.getDataSource(dbInfoId);
        PagingParam pagingParam = pagingAssembler.toPagingParam(form);
        return genericDao.findPaing(dataSource, tableName, pagingParam);
    }

    @RequestMapping(value = "/{dbInfoId}/{tableName}/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public PagingResult<Map<String, Object>> update(@PathVariable Integer dbInfoId, @PathVariable String tableName,
            @PathVariable String id, @RequestBody Map<String, Object> body) throws IOException {
        DataSource dataSource = dataSourceService.getDataSource(dbInfoId);
        TableMetaData tableMetaData = metaDataService.getTableMetaData(dataSource, tableName);
        List<ColumnMetaData> columnMetaDatas = metaDataService.getColumnMetaDatas(dataSource, tableName);
        HashMap<String, SqlParameterValue> sqlParams = metaDataService.buildSqlParameter(columnMetaDatas, body);
        genericDao.updateTable(dataSource, tableName, tableMetaData.getPrimaryKeyCol(), sqlParams);
        return PagingResult.fromSingleResult(body);
    }

    @RequestMapping(value = "/{dbInfoId}/{tableName}/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable Integer dbInfoId, @PathVariable String tableName, @PathVariable Integer id) {
        DataSource dataSource = dataSourceService.getDataSource(dbInfoId);
        TableMetaData tableMetaData = metaDataService.getTableMetaData(dataSource, tableName);
        genericDao.deleteRecord(dataSource, tableName, tableMetaData.getPrimaryKeyCol(), id);
    }

}
