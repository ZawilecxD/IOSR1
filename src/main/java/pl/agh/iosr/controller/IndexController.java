package pl.agh.iosr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

/**
 * Created by Murzynas on 2016-11-12.
 */

@Controller
public class IndexController {

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getIndexPage() {
        return "index";
    }

    @RequestMapping(value = "main", method = RequestMethod.GET)
    public String getUsersView() {
        return "main";
    }
}
