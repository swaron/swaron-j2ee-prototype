package org.app.ws.rest;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import javax.servlet.http.HttpServletRequest;

import org.app.framework.paging.PagingAssembler;
import org.app.framework.paging.PagingForm;
import org.app.framework.paging.PagingResult;
import org.app.web.application.UserSessionListener;
import org.app.web.dto.admin.SessionInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(value = "/rest/admin", produces = { "application/json", "application/xml" })
public class AdminResource {

    protected final Logger logger = LoggerFactory.getLogger(getClass());


    @Autowired
    PagingAssembler pagingAssembler;


    @Autowired
    UserSessionListener userSessionListener;
    
    @RequestMapping(value="/online-user", method = RequestMethod.GET)
    @ResponseBody
    public PagingResult<SessionInfo> onlineUser(PagingForm form,HttpServletRequest request) {
    	Collection<SessionInfo> sessions = userSessionListener.getAllSessions();
    	List<SessionInfo> list = new ArrayList<SessionInfo>(sessions);
		return new PagingResult<SessionInfo>(list, sessions.size());
    }


}
