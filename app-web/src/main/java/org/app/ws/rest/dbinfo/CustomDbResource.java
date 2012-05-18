package org.app.ws.rest.dbinfo;

import java.util.Collection;

import org.app.repo.jpa.dao.DbInfoDao;
import org.app.repo.jpa.dao.GenericDao;
import org.app.repo.jpa.vo.DbInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/dbinfo/customdb", produces = { "application/json", "application/xml" })
public class CustomDbResource {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    GenericDao genericDao;

    @Autowired
    DbInfoDao dbInfoDao;

    
    @RequestMapping(value = "/db-infos", method = RequestMethod.GET)
    @ResponseBody
    public Collection<DbInfo> dbList() {
        Collection<DbInfo> dbinfos = dbInfoDao.listDataSource();
        return dbinfos;
    }
}
