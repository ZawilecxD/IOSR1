package pl.agh.iosr.client.config;

import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.Properties;

/**
 * Created by Murzynas on 2016-11-05.
 */
public class ClientRunner {
    private InputStream inputStream;
    public static String mainDirectoryPath;

    public static void main(String[] args) {
        //TODO: rejestracja clienta w ZK
        System.out.println(args[0]);
        mainDirectoryPath = args[0];
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
