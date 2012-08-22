package org.app.ws.rest;

import java.io.IOException;

import junit.framework.Assert;

import org.app.web.BaseWebTest;
import org.app.web.dto.grid.GridConfig;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

import com.fasterxml.jackson.core.JsonGenerationException;
import com.fasterxml.jackson.databind.JsonMappingException;

public class TableMetadataResourceTest extends BaseWebTest{

	@Autowired
	GridConfigResource tableMetadataResource;
	
	@Test
	public void testRead() throws JsonGenerationException, JsonMappingException, IOException {
		GridConfig gridConfig = tableMetadataResource.read("sys","sec_user");
		Assert.assertFalse(gridConfig.getColumns().isEmpty());
		logger.debug(objectMapper.writeValueAsString(gridConfig));
	}
}
