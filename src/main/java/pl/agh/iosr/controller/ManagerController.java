package pl.agh.iosr.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import pl.agh.iosr.utils.WriteContentDTO;

import javax.annotation.PostConstruct;
import java.io.*;

/**
 * Created by Murzynas on 2016-11-05.
 */
@RestController
@RequestMapping("/")
public class ManagerController {

    @Value("${files.main.directory}")
    private String filesDirectory;

    @PostConstruct
    public void init() {
        //TODO: initialize zookeeper for this manager
    }

    @RequestMapping(value = "read/{fileName}", method = RequestMethod.GET)
    public @ResponseBody  String readFile(@PathVariable String fileName) {
        String filePath = getPathToFile(fileName);

            //TODO: ten kod do klienta, a tutaj podbicie ZK o tresc do klienta
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
    public @ResponseBody String writeToFile(@RequestParam String fileName, @RequestParam String text) {
        String pathToFile = getPathToFile(fileName);

            //TODO: ten kod do klienta, a tutaj setData rozpropagowane do wszystkich
            File f = new File(pathToFile);
            try {
                FileWriter writer = new FileWriter(f);
                writer.write(text);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
                return "POST ERROR";
            }

        System.out.println("WRITING TO FILE "+pathToFile + ", text = "+text);
        return "POST OK";
    }


    //TODO: idzie do klienta? kazdy bedzie mial swoja sciezke chyba
    private String getPathToFile(String fileName) {
        return filesDirectory + "\\" + fileName + ".txt";
    }
}
