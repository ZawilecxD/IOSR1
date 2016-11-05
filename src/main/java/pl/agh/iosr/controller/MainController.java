package pl.agh.iosr.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import java.io.*;

/**
 * Created by Murzynas on 2016-11-05.
 */
@RestController
@RequestMapping("/")
public class MainController {

    @Value("${files.main.directory}")
    private String filesDirectory;

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
        String filePath = getPathToFile(fileName);
        String returnText = "";
        try {
            FileReader fr = new FileReader(filePath);
            BufferedReader br = new BufferedReader(fr);
            String temp = "";
            while((temp = br.readLine()) != null) {
                returnText += temp;
            }
            fr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

        System.out.println("READING FILE "+filePath+", read text = "+returnText);
        return returnText;
    }

    @RequestMapping(value = "write", method = RequestMethod.POST)
    public String getUsersView(@RequestBody String fileName, @RequestBody String text) {
        System.out.println("WRITING TO FILE "+getPathToFile(fileName) + ", text = "+text);
        return "POST OK";
    }


    private String getPathToFile(String fileName) {
        return filesDirectory + "\\" + fileName + ".txt";
    }
}
