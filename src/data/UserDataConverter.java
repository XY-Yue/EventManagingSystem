package data;

import account.AccountManager;
import authentication.ConferenceRegisterSystem;
import conferencemain.MainPresenter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.FileReader;

/**
 * Gateway class. Contains method that convert data from txt format to ser files.
 * @author Group0694
 * @version 2.0.0
 */
public class UserDataConverter {

    @SuppressWarnings("FieldMayBeFinal")
    private MainPresenter wp = new MainPresenter();

    private AccountManager readFromFile(String path) {
        try {
            FileReader fr = new FileReader(path);
            BufferedReader br = new BufferedReader(fr);
            String currLine = br.readLine();
            String accountType, username, password;
            AccountManager am = new AccountManager();
            ConferenceRegisterSystem crs = new ConferenceRegisterSystem();
            while (currLine != null) {
                if (!currLine.startsWith("#")) {
                    accountType = currLine;
                    username = br.readLine();
                    password = br.readLine();
                    br.readLine();
                    crs.createAccountWithData(accountType, username, password, am);
                }
                currLine = br.readLine();
            }
            fr.close();
            br.close();
            return am;
        } catch (IOException e) {
            wp.printErrorMessage("fail to read from " + path);
            return new AccountManager();
        }
    }

    /** Converts a txt file to a ser file and save the ser file to the data package
     * @param prePath The path of the txt file that need to be converted
     * @param currPath The path of the location that ser file will be stored
     * @param ds An object of the DataSaver class that will be used to write ser file
     */
    public void convertData(String prePath, String currPath, DataSaver<AccountManager> ds) {
        ds.saveToFile(currPath, readFromFile(prePath));
    }
}
