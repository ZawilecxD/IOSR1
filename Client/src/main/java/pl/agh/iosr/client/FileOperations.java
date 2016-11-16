package pl.agh.iosr.client;

import pl.agh.iosr.client.config.ClientRunner;

import java.io.*;

/**
 * Created by Murzynas on 2016-11-05.
 */
public class FileOperations {

    public String readTextFromFile(String fileName) throws IOException {
        String pathToFile = getPathToFile(fileName);
        String returnText = "";
        FileReader fr = new FileReader(pathToFile);
        BufferedReader br = new BufferedReader(fr);
        String temp = "";
        while((temp = br.readLine()) != null) {
            returnText += temp;
        }
        fr.close();


        return returnText;
    }

    public void writeToFile(String fileName, String text) throws IOException {
        String pathToFile = getPathToFile(fileName);
        File f = new File(pathToFile);
        FileWriter writer = new FileWriter(f);
        writer.write(text);
        writer.flush();
        writer.close();

    }

    public void deleteFile(String fileName) {
        String pathToFile = getPathToFile(fileName);
        File f = new File(pathToFile);
        f.delete();
    }

    private String getPathToFile(String fileName) {
        return ClientRunner.mainDirectoryPath + "\\" + fileName + ".txt";
    }
}

