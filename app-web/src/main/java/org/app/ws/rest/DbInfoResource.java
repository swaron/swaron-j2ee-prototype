package org.app.ws.rest;

import org.app.framework.paging.PagingAssembler;
import org.app.framework.paging.PagingForm;
import org.app.framework.paging.PagingParam;
import org.app.framework.paging.PagingResult;
import org.app.repo.jpa.dao.DbInfoDao;
import org.app.repo.jpa.model.DbInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/db-info", produces = { "application/json", "application/xml" })
public class DbInfoResource {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    DbInfoDao dbInfoDao;

    @Autowired
    PagingAssembler pagingAssembler;

    @RequestMapping(value = "", method = RequestMethod.POST)
    @ResponseBody
    public PagingResult<DbInfo> create(@RequestBody DbInfo dbInfo) {
        dbInfoDao.persist(dbInfo);
        return PagingResult.fromSingleResult(dbInfo);
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    @ResponseBody
    public PagingResult<DbInfo> read(PagingForm form) {
        PagingParam pagingParam = pagingAssembler.toPagingParam(form);
        return dbInfoDao.findPaging(DbInfo.class, pagingParam);
    }

    @RequestMapping(value = "/{id}", method = RequestMethod.PUT)
    @ResponseBody
    public PagingResult<DbInfo> update(@PathVariable String id, @RequestBody DbInfo dbInfo) {
        DbInfo merge = dbInfoDao.merge(dbInfo);
        return PagingResult.fromSingleResult(merge);

    }

    @RequestMapping(value = "/{id}", method = RequestMethod.DELETE)
    @ResponseBody
    public void delete(@PathVariable Integer id) {
        dbInfoDao.remove(DbInfo.class, id);
    }

}
