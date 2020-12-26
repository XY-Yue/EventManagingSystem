package data;

import java.io.File;

public class MainGateway {
    /**
     * Gives the given file name a proper file path that the file should be stored
     *
     * @param path The name of the required file
     * @return A String representing the correct location of the data
     */
    public String getSrcPath(String path) {
        // Read and save everything outside project path, if change latter than we just need to change this method
        return "src" + File.separator + "data" + File.separator + path;
    }
}
