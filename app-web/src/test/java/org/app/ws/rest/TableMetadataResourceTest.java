package org.app.ws.rest;

import static org.junit.Assert.*;

import java.io.IOException;

import junit.framework.Assert;

import org.apache.commons.lang.builder.ReflectionToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.app.web.BaseWebTest;
import org.app.ws.rest.grid.GridConfig;
import org.codehaus.jackson.JsonGenerationException;
import org.codehaus.jackson.map.JsonMappingException;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class TableMetadataResourceTest extends BaseWebTest{

	@Autowired
	GridConfigResource tableMetadataResource;
	
	@Test
	public void testRead() throws JsonGenerationException, JsonMappingException, IOException {
		GridConfig gridConfig = tableMetadataResource.read("sec_user");
		Assert.assertFalse(gridConfig.getColumns().isEmpty());
		logger.debug(objectMapper.writeValueAsString(gridConfig));
	}
}
