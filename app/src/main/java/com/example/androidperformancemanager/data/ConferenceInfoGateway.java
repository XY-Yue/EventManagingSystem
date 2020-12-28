package com.example.androidperformancemanager.data;

import com.example.androidperformancemanager.conferencemain.MainPresenter;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

/**
 * Reads and writes to text files
 */
public class ConferenceInfoGateway extends MainGateway{
    @SuppressWarnings("FieldMayBeFinal")
    private MainPresenter presenter = new MainPresenter();

    /**
     * Reads from the target text file and output its content line by line
     * @param filePath the file path of the file
     * @return a list of the content of each line from top to bottom, if the reading operation is successful
     */
    public List<String> readTextFile(String filePath){
        try{
            BufferedReader reader = new BufferedReader(new FileReader(getSrcPath(filePath)));
            String nextLine = reader.readLine();
            List<String> result = new ArrayList<>();

            while (nextLine != null){
                result.add(nextLine);
                nextLine = reader.readLine();
            }
            reader.close();
            return result;
        }catch (IOException ioException){
            presenter.printErrorMessage("fail to read from " + filePath);
            return null;
        }
    }

    /**
     * Writes the given data into the target text file
     * @param filePath The path of the text file
     * @param data The data needed to be written
     * @return true iff the writing operation is successful
     */
    public boolean writeToText(String filePath, List<String> data){
        try {
            BufferedWriter writer = new BufferedWriter(new FileWriter(getSrcPath(filePath), true));

            for (String str : data) {
                writer.write(str);
                writer.newLine();
            }
            writer.close();
            return true;
        } catch (IOException ioException){
            presenter.printErrorMessage("fail to write to " + filePath);
            return false;
        }
    }
}
