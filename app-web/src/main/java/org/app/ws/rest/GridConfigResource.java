package org.app.ws.rest;

import java.util.List;

import javax.persistence.metamodel.EntityType;
import javax.sql.DataSource;

import org.app.application.grid.DataSourceService;
import org.app.application.grid.DatabaseMetaDataService;
import org.app.domain.vo.grid.ColumnMetaData;
import org.app.domain.vo.grid.TableMetaData;
import org.app.repo.jpa.dao.CodeDictionaryDao;
import org.app.repo.jpa.dao.GenericDao;
import org.app.web.assembler.grid.GridAssembler;
import org.app.web.dto.grid.GridConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/grid-config", produces = { "application/json", "application/xml" })
public class GridConfigResource {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	GenericDao genericDao;
	
	@Autowired
	CodeDictionaryDao codeDictionaryDao;
	
	@Autowired
	DatabaseMetaDataService metaDataService;
	
	@Autowired
	DataSourceService dataSourceService;
	
	@Autowired
	GridAssembler gridAssembler;
	
	@RequestMapping(method = RequestMethod.GET)
	@ResponseBody
	public GridConfig read(@RequestParam String dbInfoId, @RequestParam String tableName) {
	    if("sys".equals(dbInfoId)){
	        return getSysDbGridConfig(tableName);
	    }else{
	        Integer id = Integer.parseInt(dbInfoId);
	        return getCustomDbGridConfig(id,tableName);
	    }
	}

    private GridConfig getCustomDbGridConfig(Integer dbInfoId, String tableName) {
        DataSource dataSource = dataSourceService.getDataSource(dbInfoId);
        TableMetaData tableMetaData = metaDataService.getTableMetaData(dataSource, tableName);
        List<ColumnMetaData> columnMetaDatas = metaDataService.getColumnMetaDatas(dataSource, tableName);
        return gridAssembler.createGridConfig(tableMetaData, columnMetaDatas);
    }

	private GridConfig getSysDbGridConfig(String tableName) {
        @SuppressWarnings("unchecked")
		EntityType<Object> entity = (EntityType<Object>) genericDao.getEntityByName(tableName);
		GridConfig gridConfig = gridAssembler.createGridConfig(entity);
		return gridConfig;
    }
}
