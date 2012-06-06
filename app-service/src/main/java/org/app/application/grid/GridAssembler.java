package org.app.application.grid;

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
import org.app.domain.grid.service.SqlTypeUtils;
import org.app.domain.grid.vo.ColumnMetaData;
import org.app.domain.grid.vo.TableMetaData;
import org.app.repo.jpa.dao.CodeDictionaryDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Service;

@Service
public class GridAssembler {
	protected Logger logger = LoggerFactory.getLogger(getClass());
	
	@Autowired
	CodeDictionaryDao codeDictionaryDao;
	
	public GridConfig createGridConfig(EntityType<Object> entity) {
		if(entity == null){
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
			columnConfig.setEntityName(attr.getName());
			columnConfig.setType(attr.getJavaType().getSimpleName());
			if (version != null && attr.getName().equals(version.getName())) {
				columnConfig.setHide(true);
			}
			columnConfig.setColumnName(attr.getName());
			try {
				Field field = clazz.getDeclaredField(attr.getName());
				if (field.isAnnotationPresent(Column.class)) {
					String name = field.getAnnotation(Column.class).name();
					if(StringUtils.isNotEmpty(name)){
					    columnConfig.setColumnName(name);
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
			if(codeDictionaryDao.findAliasCount(gridConfig.getTableName(), columnConfig.getColumnName()) > 0){
				columnConfig.setHasAlias(true);
			}
			columns.add(columnConfig);
		}
		return gridConfig;
	}

	public GridConfig createGridConfig(TableMetaData tableMetaData, List<ColumnMetaData> columnMetaDatas) {
		GridConfig gridConfig = new GridConfig();
	    gridConfig.setIdProperty(tableMetaData.getPrimaryKeyCol());
	    gridConfig.setEntityName(tableMetaData.getTableName());        
	    gridConfig.setTableName(tableMetaData.getTableName());
	    if(gridConfig.getIdProperty() == null){
	    	logger.warn("no identity column for table :" + tableMetaData.getTableName());
	    }
	    List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
	    for (ColumnMetaData col : columnMetaDatas) {
	        ColumnConfig config = new ColumnConfig();
	        config.setHasAlias(false);
	        config.setHide(false);
	        config.setColumnName(col.getColumnName());
	        config.setEntityName(col.getColumnName());
	        config.setType(SqlTypeUtils.toClass(col.getDataType()).getSimpleName());
	        columns.add(config);
	    }
	    gridConfig.setColumns(columns);
	    return gridConfig;
	}
}
