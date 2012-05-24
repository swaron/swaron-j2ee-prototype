package org.app.web.db;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/db/external/")
public class ExternalDbController {
	
	@RequestMapping(value="/db-info", method = RequestMethod.GET)
	public void dbInfo() {
	}
	@RequestMapping(value="/browse", method = RequestMethod.GET)
	public void browse() {
	}
}
