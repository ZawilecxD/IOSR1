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

        ServiceInstance serviceInstance = ServiceInstance.builder()
                .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                .address("127.0.0.1")
                .port(port)
                .name("worker")
                .id("worker_"+port)
                .build();

        ServiceDiscoveryBuilder.builder(Void.TYPE)
                .basePath("iosr1")
                .client(curatorFramework)
                .thisInstance(serviceInstance)
                .build()
                .start();

        NodeCache nodeCache = new NodeCache(curatorFramework, "/iosr1/worker");
        nodeCache.getListenable().addListener(() -> {
            System.out.println("NodeChanged");
            ChildData currentData = nodeCache.getCurrentData();
            byte[] bytes = curatorFramework.getData().forPath("/iosr1/worker");
            System.out.println(bytes);
            ContentDTO contentDTO = ContentDTO.fromByteArray(bytes);
            System.out.println("deserialized");
            System.out.println(contentDTO);
            System.out.println(contentDTO.toString());
            System.out.println("end of processing");
        });
        nodeCache.start();

        port(port);
        get("/get/:key", (request, response) -> {
            return fileOperations.readTextFromFile(request.params(":key"));
        });

        System.out.println("Client up and running!");
    }

}
