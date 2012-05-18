package org.app.web.dbconsole;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/dbconsole/customdb/")
public class CustomDbController {
	
	@RequestMapping(value="/dbinfo", method = RequestMethod.GET)
	public String dbinfo() {
		return "dbconsole/customdb/dbinfo";
	}
	@RequestMapping(value="/browse", method = RequestMethod.GET)
	public String browse() {
	    return "dbconsole/customdb/browse";
	}
}
