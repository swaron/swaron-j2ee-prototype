package org.app.ws.rest;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

import org.apache.commons.lang.StringUtils;
import org.app.application.service.DbMetaDataService;
import org.app.domain.grid.entity.ColumnConfig;
import org.app.domain.grid.entity.GridConfig;
import org.app.domain.grid.service.ExternalDbService;
import org.app.domain.grid.vo.ColumnMetaData;
import org.app.domain.grid.vo.TableMetaData;
import org.app.repo.jpa.dao.CodeDictionaryDao;
import org.app.repo.jpa.dao.GenericDao;
import org.app.repo.service.JdbcUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
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
	ExternalDbService customDbManager;
	
	@Autowired
	DbMetaDataService metaDataService;
	
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
        GridConfig gridConfig = new GridConfig();
        gridConfig.setEntityName(tableName);        
        gridConfig.setTableName(tableName);
        
        TableMetaData tableMetaData = metaDataService.getTableMetaData(dbInfoId, tableName);
        
        gridConfig.setIdProperty(tableMetaData.getSelfReferencingColName());
        logger.debug("found identity column: " + gridConfig.getIdProperty());
        List<ColumnMetaData> columnsResults = metaDataService.getColumnMetaDatas(dbInfoId, tableName);
        List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
        for (ColumnMetaData col : columnsResults) {
            ColumnConfig config = new ColumnConfig();
            if("YES".equalsIgnoreCase(col.getIsAutoincrement()) ){
                gridConfig.setIdProperty(col.getColumnName());
            }
            config.setHasAlias(false);
            config.setHide(false);
            config.setMapping(col.getColumnName());
            config.setName(col.getColumnName());
            config.setType(JdbcUtils.toClass(col.getDataType()).getSimpleName());
            columns.add(config);
        }
        gridConfig.setColumns(columns);
        return gridConfig;
    }

    private GridConfig getSysDbGridConfig(String tableName) {
        @SuppressWarnings("unchecked")
		EntityType<Object> entity = (EntityType<Object>) genericDao.getGridConfig("internal.table", tableName);
		if (entity == null) {
			return null;
		}
		GridConfig gridConfig = new GridConfig();
		gridConfig.setEntityName(entity.getName());
		Table annotation = AnnotationUtils.findAnnotation(entity.getJavaType(), Table.class);
		if(annotation != null){
			gridConfig.setTableName(annotation.name());
		}else{
			gridConfig.setTableName(entity.getName());
		}
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		gridConfig.setColumns(columns);
		Set<SingularAttribute<? super Object, ?>> attributes = entity.getSingularAttributes();
		SingularAttribute<? super Object, Timestamp> version = entity.getVersion(Timestamp.class);
		SingularAttribute<? super Object, ?> id = entity.getId(entity.getIdType().getJavaType());
		gridConfig.setIdProperty(id.getName());
		Class<Object> clazz = entity.getJavaType();
		for (Attribute<?, ?> attr : attributes) {
			ColumnConfig columnConfig = new ColumnConfig();
			columnConfig.setName(attr.getName());
			columnConfig.setType(attr.getJavaType().getSimpleName());
			if (version != null && attr.getName().equals(version.getName())) {
				columnConfig.setHide(true);
			}
			columnConfig.setMapping(attr.getName());
			try {
				Field field = clazz.getDeclaredField(attr.getName());
				if (field.isAnnotationPresent(Column.class)) {
					String name = field.getAnnotation(Column.class).name();
					if(StringUtils.isNotEmpty(name)){
					    columnConfig.setMapping(name);
					}
				}
				if(field.isAnnotationPresent(OneToMany.class)){
				    continue;
				}
				if(field.isAnnotationPresent(OneToOne.class)){
				    continue;
				}
				if(field.isAnnotationPresent(ManyToOne.class)){
				    continue;
				}
			} catch (NoSuchFieldException e) {
				columnConfig.setHide(true);
				logger.warn("field " + attr.getName() + " of class " + clazz.getName() + " not found.");
			}
			if(codeDictionaryDao.findAliasCount(gridConfig.getTableName(), columnConfig.getMapping()) > 0){
				columnConfig.setHasAlias(true);
			}
			columns.add(columnConfig);
		}
		return gridConfig;
    }
}
