package org.app.ws.rest;

import org.app.framework.paging.PagingForm;
import org.app.framework.paging.PagingResult;
import org.app.web.BaseWebTest;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TableResourceTest extends BaseWebTest{

	@Autowired
	InternalTableContentResource tableResource;
	
	@Test
	public void testRead() {
		PagingResult<?> result = tableResource.read("sec_user", new PagingForm());
		logger.debug("record count: " + result.getTotal());
	}

}
