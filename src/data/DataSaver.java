package data;

import conferencemain.MainPresenter;
import java.io.*;

/**
 * Gateway class. Contains method that read from and write to ser files.
 * @author Group0694
 * @version 2.0.0
 */
public class DataSaver<T> {

    @SuppressWarnings("FieldMayBeFinal")
    private MainPresenter wp = new MainPresenter();

    /** Reads from a file that contains the information of a use case class
     * @param path The location and name of the file reading from
     * @return A object of the use case class that extracted from a file
     * @throws ClassNotFoundException if the ser file contains the wrong class
     */
    public T readFromFile(String path) throws ClassNotFoundException {

        // Use the example from class
        try {
            InputStream file = new FileInputStream(path); // String path should be "fileName.ser"
            InputStream buffer = new BufferedInputStream(file);
            ObjectInput input = new ObjectInputStream(buffer);

            // deserialize the EventManager
            @SuppressWarnings("unchecked")
            T em = (T)input.readObject();
            input.close();
            return em;
        } catch (IOException ex) {
            wp.printErrorMessage("fail to read from " + path);
            return null;
        }
    }

    /** Writes to a ser file that contains the use case class' information
     * @param filePath The location and name of the file reading from
     * @param var A object of the use case class that need to be written to file
     */
    public void saveToFile(String filePath, T var) {

        try {
            OutputStream file = new FileOutputStream(filePath);
            OutputStream buffer = new BufferedOutputStream(file);
            ObjectOutput output = new ObjectOutputStream(buffer);

            // serialize the EventManager
            output.writeObject(var);
            output.close();
        } catch (IOException ex) {
            wp.printErrorMessage("fail to save to " + filePath);
        }
    }

    /** Gives the given file name a proper file path that the file should be stored
     * @param path The name of the required file
     * @return A String representing the correct location of the data
     */
    public String getSrcPath(String path) {
        // Read and save everything outside project path, if change latter than we just need to change this method
        return "src" + File.separator + "data" + File.separator + path;
    }
}