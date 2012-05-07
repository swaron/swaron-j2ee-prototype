package org.app.ws.rest;

import java.lang.reflect.Field;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.metamodel.Attribute;
import javax.persistence.metamodel.EntityType;
import javax.persistence.metamodel.SingularAttribute;

import org.app.repo.jpa.dao.GenericDao;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/table_metadata", produces = { "application/json", "application/xml" })
public class TableMetadataResource {
	protected final Logger logger = LoggerFactory.getLogger(getClass());

	@Autowired
	GenericDao genericDao;

	@RequestMapping(value = "/{tableName}", method = RequestMethod.GET)
	@ResponseBody
	public GridConfig read(@PathVariable String tableName) {
		@SuppressWarnings("unchecked")
		EntityType<Object> entity = (EntityType<Object>) genericDao.getGridConfig("internal.table", tableName);
		if (entity == null) {
			return null;
		}
		GridConfig gridConfig = new GridConfig();
		gridConfig.setTableName(entity.getName());
		List<ColumnConfig> columns = new ArrayList<ColumnConfig>();
		gridConfig.setColumns(columns);
		Set<SingularAttribute<? super Object, ?>> attributes = entity.getSingularAttributes();
		SingularAttribute<? super Object, Timestamp> version = entity.getVersion(Timestamp.class);
		SingularAttribute<? super Object, ?> id = entity.getId(entity.getIdType().getJavaType());
		gridConfig.setIdProperty(id.getName());
		Class<Object> clazz = entity.getJavaType();
		for (Attribute<?, ?> attr : attributes) {
			ColumnConfig columnConfig = new ColumnConfig();
			columnConfig.setMapping(attr.getName());
			columnConfig.setType(attr.getJavaType().getSimpleName());
			if (attr.getName().equals(version.getName())) {
				columnConfig.setHide(true);
			}
			columnConfig.setName(attr.getName());
			try {
				Field field = clazz.getDeclaredField(attr.getName());
				if (field.isAnnotationPresent(Column.class)) {
					String name = field.getAnnotation(Column.class).name();
					columnConfig.setName(name);
				}
			} catch (NoSuchFieldException e) {
				columnConfig.setHide(true);
				logger.warn("field " + attr.getName() + " of class " + clazz.getName() + " not found.");
			}
			columns.add(columnConfig);
		}
		return gridConfig;
	}
}
