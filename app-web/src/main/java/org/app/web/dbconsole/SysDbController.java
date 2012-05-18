package org.app.web.dbconsole;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/dbconsole/sysdb")
public class SysDbController {
	
	@RequestMapping(method = RequestMethod.GET)
	public String table() {
		return "dbconsole/table";
	}
}
