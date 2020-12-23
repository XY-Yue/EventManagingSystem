package conferencemain;

import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.Date;
import java.util.List;

/**
 * A main presenter class
 * Methods of this class prints out welcome message, separate lines, get input signal, invalid input signal, menu,
 * error message, redo message and action message
 * @author Group0694
 * @version 2.0.0
 */
public class MainPresenter {

    /**
     * Prints welcome message
     * @param systemName a string represents the system name
     * @param additionMessage a string represents additional messages
     */
    public void printWelcomeMessage(String systemName, String additionMessage) {
        printSeparateLine();
        System.out.println("Welcome to " + systemName);
        if (additionMessage != null) {
            System.out.println(additionMessage);
        }
    }

    /**
     * Prints a separate line
     */
    public void printSeparateLine() {
        System.out.println("------------------------------------------");
    }

    /**
     * Prints a signal to get user input
     */
    public void getInput() {
        System.out.print(">>> ");
    }

    /**
     * Provides messages to tell the user his/her input is invalid
     */
    public void printInvalidInput() {
        printErrorMessage("Invalid Input");
    }

    /**
     * Prints the menu onto the screen
     */
    public void printMenu(List<String> options, String redoKey) {
        printSeparateLine();
        System.out.println("Please enter: ");
        for (int i = 0; i < options.size(); i++) {
            System.out.println(i + ": " + options.get(i));
        }
        if (redoKey != null) {
            System.out.println("r: " + redoKey);
        }
        getInput();
    }

    /**
     * Prints error message
     * @param errorMessage a string represents error message
     */
    public void printErrorMessage(String errorMessage) {
        System.out.println("[ERROR] " + errorMessage);
    }

    /**
     * Prints redo message
     * @param redoMessage a string represents redo message
     */
    public void printRedoMessage(String redoMessage) {
        System.out.println("[REDO] " + redoMessage);
    }

    /**
     * Prints action message
     * @param actionMessage a string represents action message
     */
    public void printActionMessage(String actionMessage) {
        System.out.println("[ACTION] " + actionMessage);
    }

    /**
     * Displays the time properly (without the seconds)
     * @param time The time
     */
    public String getTime(Timestamp time) {
        Date date = new Date(time.getTime());
        return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(date);
    }
}
