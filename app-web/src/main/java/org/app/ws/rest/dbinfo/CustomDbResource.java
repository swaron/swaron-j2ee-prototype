package org.app.ws.rest.dbinfo;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import javax.persistence.Table;
import javax.persistence.metamodel.EntityType;

import org.app.framework.paging.PagingAssembler;
import org.app.framework.paging.PagingForm;
import org.app.framework.paging.PagingParam;
import org.app.framework.paging.PagingResult;
import org.app.framework.web.tree.TreeNode;
import org.app.framework.web.tree.TreeResult;
import org.app.repo.jpa.dao.DbInfoDao;
import org.app.repo.jpa.model.DbInfo;
import org.app.repo.service.CustomDbManager;
import org.app.ws.rest.grid.GridConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/dbinfo/customdb", produces = { "application/json", "application/xml" })
public class CustomDbResource {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DbInfoDao dbInfoDao;
    
    @Autowired
    PagingAssembler pagingAssembler;
    
    @Autowired
    CustomDbManager customDbManager;

    @RequestMapping(value="/db-infos", method = RequestMethod.POST)
    @ResponseBody
    public PagingResult<DbInfo> create(@RequestBody DbInfo dbInfo) {
        dbInfoDao.persist(dbInfo);
        return PagingResult.fromSingleResult(dbInfo);
    }

    @RequestMapping(value="/db-infos", method = RequestMethod.GET)
    @ResponseBody
    public PagingResult<DbInfo> read(PagingForm form) {
        PagingParam pagingParam = pagingAssembler.toPagingParam(form);
        return dbInfoDao.findPaging(DbInfo.class, pagingParam);
    }

    @RequestMapping(value = "/db-infos/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public PagingResult<DbInfo> update(@PathVariable String id, @RequestBody DbInfo dbInfo) {
        DbInfo merge = dbInfoDao.merge(dbInfo);
        return PagingResult.fromSingleResult(merge);

    }

    @RequestMapping(value = "/db-infos/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable Integer id) {
        dbInfoDao.remove(DbInfo.class, id);
    }

    
    @RequestMapping(value = "/table-tree", method = RequestMethod.GET)
    @ResponseBody
    public TreeResult list(@RequestParam Integer database) {
        TreeResult treeResult = new TreeResult();
        List<DbInfo> findAll = dbInfoDao.findAll(DbInfo.class);
        List<TreeNode> children = new ArrayList<TreeNode>();
        for (DbInfo dbInfo : findAll) {
            TreeNode node = new TreeNode();
            node.setExpanded(false);
            node.setLeaf(false);
            node.setLoaded(false);
            node.setText(dbInfo.getName());
            node.setValue(dbInfo.getDbInfoId().toString());
            children.add(node);
        }
        treeResult.setChildren(children);
        return treeResult;
    }
}
