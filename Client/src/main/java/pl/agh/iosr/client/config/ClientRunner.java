package pl.agh.iosr.client.config;

import org.apache.commons.cli.*;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.ChildData;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import pl.agh.iosr.client.FileOperations;
import pl.agh.iosr.client.utils.ContentDTO;

import java.io.File;
import java.io.IOException;

import static spark.Spark.get;
import static spark.Spark.port;

/**
 * Created by Murzynas on 2016-11-05.
 */
public class ClientRunner{
    public static String mainDirectoryPath;
    private static FileOperations fileOperations = new FileOperations();

    public static void main(String[] args) throws Exception {

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("0.0.0.0:2181", new RetryNTimes(5, 1000));
        curatorFramework.start();

        Options options = new Options();

        Option portOption = new Option("p", "port", true, "adres portu");
        portOption.isRequired();
        options.addOption(portOption);

        CommandLineParser parser = new BasicParser();
        HelpFormatter formatter = new HelpFormatter();
        CommandLine cmd;
        try {
            cmd = parser.parse(options, args);
        } catch (ParseException e) {
            formatter.printHelp("client",options);
            System.exit(1);
            return;
        }
        int port = Integer.parseInt(cmd.getOptionValue("port"));
        String id = "worker_" + port;

        boolean mkdir = new File(id).mkdir();
        mainDirectoryPath = id;

        ServiceInstance serviceInstance = ServiceInstance.builder()
                .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                .address("127.0.0.1")
                .port(port)
                .name("worker")
                .id(id)
                .build();

        ServiceDiscoveryBuilder.builder(Void.TYPE)
                .basePath("iosr1")
                .client(curatorFramework)
                .thisInstance(serviceInstance)
                .build()
                .start();

        NodeCache nodeCache = new NodeCache(curatorFramework, "/iosr1/worker");
        nodeCache.start();
        nodeCache.getListenable().addListener(() -> processNodeChange(nodeCache.getCurrentData()));

        port(port);
        get("/get/:key", (request, response) -> {
            String fileName = request.params(":key");
            System.out.println("Received GET request for "+fileName);
            return fileOperations.readTextFromFile(fileName);
        });

        System.out.println("Client up and running!");
    }

    private static void processNodeChange(ChildData childData) throws IOException {
        System.out.println("processing NodeChange");
        byte[] data = childData.getData();
        if(data != null && data.length!=0){
            System.out.println("data not empty");
            ContentDTO contentDTO = null;
            try {
                contentDTO = ContentDTO.fromByteArray(data);
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            if(contentDTO!=null){
                ContentDTO.OperationType type = contentDTO.getType();
                switch (type) {
                    case ADD:
                        fileOperations.writeToFile(contentDTO.getKey(), contentDTO.getValue());
                        break;
                    case DELETE:
                        fileOperations.deleteFile(contentDTO.getKey());
                        break;
                }
                System.out.println(contentDTO);
            }
        } else {
            System.out.println("NodeChange data empty");
        }
    }

}
