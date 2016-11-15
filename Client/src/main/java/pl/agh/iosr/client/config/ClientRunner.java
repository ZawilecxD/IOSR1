package pl.agh.iosr.client.config;

import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.RetryNTimes;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.UriSpec;

import java.io.InputStream;

/**
 * Created by Murzynas on 2016-11-05.
 */
public class ClientRunner {
    private InputStream inputStream;
    public static String mainDirectoryPath;
    private int port = 9001; //TODO: cmd

    public static void main(String[] args) throws Exception {

        CuratorFramework curatorFramework = CuratorFrameworkFactory.newClient("0.0.0.0:2181", new RetryNTimes(5, 1000));
        curatorFramework.start();

        ServiceInstance serviceInstance = ServiceInstance.builder()
                .uriSpec(new UriSpec("{scheme}://{address}:{port}"))
                .address("0.0.0.0")
                .port(9001)
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
        int i = 0;
        while(true){
            i++;
        }
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
