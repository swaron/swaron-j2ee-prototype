package org.app.web.html.db;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
@RequestMapping("/db/internal")
public class SysDbController {

    @RequestMapping(value = "/manage", method = RequestMethod.GET)
    public void manage() {
    }
}
