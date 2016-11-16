package pl.agh.iosr.controller;

import com.fasterxml.jackson.databind.ObjectWriter;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import com.mashape.unirest.http.exceptions.UnirestException;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import pl.agh.iosr.utils.ContentDTO;

import javax.annotation.PostConstruct;
import java.io.*;
import java.net.MalformedURLException;

/**
 * Created by Murzynas on 2016-11-05.
 */
@RestController
@RequestMapping("/")
public class ManagerController {

    private ServiceProvider<Void> serviceProvider;

    @Value("${files.main.directory}")
    private String filesDirectory;
    private CuratorFramework curatorFramework;

    @PostConstruct
    public void init() {
        curatorFramework = CuratorFrameworkFactory.newClient("0.0.0.0:2181", new RetryNTimes(5, 1000));
        curatorFramework.start();

        ServiceDiscovery<Void> serviceDiscovery = ServiceDiscoveryBuilder.builder(Void.TYPE).basePath("iosr1").client(curatorFramework).build();
        try {
            serviceDiscovery.start();
        } catch (Exception e) {
            System.out.println("ERROR establishing Service Discovery. Aborting");
            e.printStackTrace();
            return;
        }

        serviceProvider = serviceDiscovery.serviceProviderBuilder().serviceName("worker").build();
        try {
            serviceProvider.start();
        } catch (Exception e) {
            System.out.println("ERROR establishing Service Provider. Aborting");
            e.printStackTrace();
            return;
        }

    }

    @RequestMapping(value = "read/{fileName}", method = RequestMethod.GET)
    public @ResponseBody  String readFile(@PathVariable String fileName) throws MalformedURLException, UnirestException {
        System.out.println("received read request for "+fileName);
        ServiceInstance<Void> instance;
        try {
            instance = serviceProvider.getInstance();
        } catch (Exception e) {
            System.out.println("ERROR fetching instance. Aborting operation");
            e.printStackTrace();
            return null;
        }
        String returnText = "";
        String address = instance.buildUriSpec() +"/get/" + fileName;
        System.out.println(address);
        HttpResponse<String> response = Unirest.get(address).asString();
        returnText = response.getBody();
        System.out.println("RESPONSE: "+returnText);


/*        String filePath = getPathToFile(fileName);

        //TODO: ten kod do klienta, a tutaj podbicie ZK o tresc do klienta
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

        System.out.println("READING FILE "+filePath+", read text = "+returnText);*/
        return returnText;
    }

    @RequestMapping(value = "write", method = RequestMethod.POST)
    public @ResponseBody String writeToFile(@RequestParam String fileName, @RequestParam String text) {
        ContentDTO contentDTO = new ContentDTO().withType(ContentDTO.OperationType.ADD).withKey(fileName).withValue(text);
        byte[] data = contentDTO.toByteArray();
        System.out.println("ContentDTO bytearray:"+data);

        try {
            curatorFramework.setData().forPath("/iosr1/worker", data);
            System.out.println("Data set for /iosr1/worker");
        } catch (Exception e) {
            System.out.println("Failure setting data");
            e.printStackTrace();
        }

        try {
            byte[] bytes = curatorFramework.getData().forPath("/iosr1/worker");
            ContentDTO contentDTO1 = ContentDTO.fromByteArray(bytes);
            System.out.println(contentDTO1);
        } catch (Exception e) {
            e.printStackTrace();
        }
/*        String pathToFile = getPathToFile(fileName);

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

        System.out.println("WRITING TO FILE "+pathToFile + ", text = "+text);*/
        return "POST OK";
    }


    //TODO: idzie do klienta? kazdy bedzie mial swoja sciezke chyba
    private String getPathToFile(String fileName) {
        return filesDirectory + "\\" + fileName + ".txt";
    }
}
