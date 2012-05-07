package org.app.web.dbconsole;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/dbconsole")
public class DataBaseController {
	
	@RequestMapping(method = RequestMethod.GET)
	public String dbIndex() {
		return "dbconsole/dbIndex";
	}
}
