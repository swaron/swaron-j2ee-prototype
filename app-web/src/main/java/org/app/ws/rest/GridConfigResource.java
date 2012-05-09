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

import org.app.repo.jpa.dao.CodeDictionaryDao;
import org.app.repo.jpa.dao.GenericDao;
import org.app.repo.jpa.model.CodeDictionary;
import org.app.ws.rest.grid.ColumnConfig;
import org.app.ws.rest.grid.GridConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/grid-config", produces = { "application/json", "application/xml" })
public class GridConfigResource {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	GenericDao genericDao;
	
	@Autowired
	CodeDictionaryDao codeDictionaryDao;

	@RequestMapping(value = "/{tableName}", method = RequestMethod.GET)
	@ResponseBody
	public GridConfig read(@PathVariable String tableName) {
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
			if (attr.getName().equals(version.getName())) {
				columnConfig.setHide(true);
			}
			columnConfig.setMapping(attr.getName());
			try {
				Field field = clazz.getDeclaredField(attr.getName());
				if (field.isAnnotationPresent(Column.class)) {
					String name = field.getAnnotation(Column.class).name();
					columnConfig.setMapping(name);
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
