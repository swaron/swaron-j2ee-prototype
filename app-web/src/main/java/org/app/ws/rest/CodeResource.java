package org.app.ws.rest;

import java.util.List;

import org.app.repo.jpa.dao.GenericDao;
import org.app.repo.jpa.model.CodeDictionary;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/codes", produces = { "application/json", "application/xml" })
public class CodeResource {

    protected final Logger logger = LoggerFactory.getLogger(getClass());

    @Autowired
    GenericDao genericDao;

    @RequestMapping(method = RequestMethod.GET)
    @ResponseBody
    public List<CodeDictionary> read() {
        List<CodeDictionary> list = genericDao.findAll(CodeDictionary.class);
        return list;
    }

}
