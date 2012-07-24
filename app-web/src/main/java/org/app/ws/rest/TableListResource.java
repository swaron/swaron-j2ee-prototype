package org.app.ws.rest;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;

import org.app.repo.jpa.dao.CodeDictionaryDao;
import org.app.repo.jpa.dao.GenericDao;
import org.app.web.dto.grid.GridConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/table-list", produces = { "application/json", "application/xml" })
public class TableListResource {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    GenericDao genericDao;

    @Autowired
    CodeDictionaryDao codeDictionaryDao;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<GridConfig> list() {
        Set<EntityType<?>> entities = genericDao.getPersistentEntities();
        List<GridConfig> tableNames = new ArrayList<GridConfig>();
        for (EntityType<?> entity : entities) {
            GridConfig gridConfig = new GridConfig();
            gridConfig.setEntityName(entity.getName());
            Table annotation = AnnotationUtils.findAnnotation(entity.getJavaType(), Table.class);
            if (annotation != null) {
                gridConfig.setTableName(annotation.name());
            } else {
                gridConfig.setTableName(entity.getName());
            }
            tableNames.add(gridConfig);
        }

        return tableNames;
    }
}
