package pl.agh.iosr.client.config;

import org.apache.commons.cli.*;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;
import static spark.Spark.*;
import java.io.InputStream;

/**
 * Created by Murzynas on 2016-11-05.
 */
public class ClientRunner {
    private InputStream inputStream;
    public static String mainDirectoryPath;

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
                .address("0.0.0.0")
                .port(port)
                .name("worker")
                .build();

        ServiceDiscoveryBuilder.builder(Void.TYPE)
                .basePath("iosr1")
                .client(curatorFramework)
                .thisInstance(serviceInstance)
                .build()
                .start();

//        System.out.println(args[0]);
//        mainDirectoryPath = args[0];

        //kręcimy się w kółko


        get("/get/:key", (request, response) -> {
            return request.params(":key")+"->value";
        });

        delete("/delete/:key", (request, response) -> {
            return request.params(":key")+"removed";
        });

    }

    //TODO uzywamy properties czy arhumenty wywoalania progamu??
//    private void readProperties() {
//        try {
//            Properties prop = new Properties();
//            String propFileName = "config.properties";
//
//            inputStream = getClass().getClassLoader().getResourceAsStream(propFileName);
//
//            if (inputStream != null) {
//                prop.load(inputStream);
//            } else {
//                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
//            }
//
//
//           String mainPath  =prop.getProperty("files.main.directory");
//            System.out.println(mainPath);
//
//        } catch (Exception e) {
//            System.out.println("Exception: " + e);
//        } finally {
//            inputStream.close();
//        }
//    }

}
