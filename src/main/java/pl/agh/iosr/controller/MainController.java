package pl.agh.iosr.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;

/**
 * Created by Murzynas on 2016-11-05.
 */
@RestController
@RequestMapping("/")
public class MainController {

    @PostConstruct
    public void init() {
        //TODO: initialize zookeeper
    }

    @RequestMapping(value = "", method = RequestMethod.GET)
    public String getIndexPage() {
        return "index";
    }

    @RequestMapping(value = "main", method = RequestMethod.GET)
    public String getUsersView() {
        return "main";
    }

    @RequestMapping(value = "read/{fileName}", method = RequestMethod.GET)
    public @ResponseBody  String getUsersView(@PathVariable String fileName) {
        System.out.println("READING FILE "+fileName);
        return "GET OK";
    }

    @RequestMapping(value = "write", method = RequestMethod.POST)
    public String getUsersView(@RequestBody String fileName, @RequestBody String text) {
        System.out.println("WRITING TO FILE "+fileName + ", text = "+text);
        return "POST OK";
    }

}
