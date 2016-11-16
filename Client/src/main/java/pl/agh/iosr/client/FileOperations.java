package pl.agh.iosr.client;

import pl.agh.iosr.client.config.ClientRunner;

import java.io.*;

/**
 * Created by Murzynas on 2016-11-05.
 */
public class FileOperations {

    public String readTextFromFile(String fileName) {
        String pathToFile = getPathToFile(fileName);
        String returnText = "";
        try {
            FileReader fr = new FileReader(pathToFile);
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

        return returnText;
    }

    public void writeToFile(String fileName, String text) {
        String pathToFile = getPathToFile(fileName);
        File f = new File(pathToFile);
        try {
            FileWriter writer = new FileWriter(f);
            writer.write(text);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
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

