package userinterface;

import conferencemain.MainPresenter;
import data.DataSaver;

import java.util.List;
import java.util.Scanner;

public class LocalConferenceInitSystem<T> {

    /**
     * Gets the required use case from gateway
     * @param conference The conference the use case belongs to
     * @param filePath the path of the .ser file
     * @return the instance of the required use case class
     */
    T getManager(String conference, String filePath){
        DataSaver<T> managerDataSaver = new DataSaver<>();
        try {
            return managerDataSaver.readFromFile(
                    managerDataSaver.getSrcPath(conference + filePath));
        } catch (ClassNotFoundException exception){
            return null;
        }
    }

    /**
     * Asks input for a new conference, checks if it is valid
     * @param sc A instance of scanner
     * @param curConferences all current existing conferences
     * @return the name of the new conference
     */
    String createConference(Scanner sc, List<String> curConferences){
        MainPresenter presenter = new MainPresenter();
        while (true){
            presenter.printActionMessage("Enter the name of the new Conference, contains letters and spaces.\n" +
                    "Enter /r to cancel.");
            presenter.getInput();
            String newConferenceName = sc.nextLine().trim();

            if ("/r".equalsIgnoreCase(newConferenceName)) return null;

            if (newConferenceName.matches(" *"))
                presenter.printErrorMessage("Cannot be all space or empty");
            else if (!newConferenceName.matches("[\\w ]+"))
                presenter.printErrorMessage("Can only contain letters and spaces");
            else if (curConferences.contains(newConferenceName))
                presenter.printErrorMessage("Conference already exist.");
            else {
                presenter.printActionMessage("Created the new conference" + newConferenceName);
                return newConferenceName;
            }
        }
    }

    /**
     * Saves the given manager into .ser file through gateway
     * @param conference the name of the conference this use case belonging to
     * @param filePath the path of the target file
     * @param manager the use case class requiring to be stored
     */
    void saveManager(String conference, String filePath, T manager){
        DataSaver<T> managerDataSaver = new DataSaver<>();
        managerDataSaver.saveToFile(managerDataSaver.getSrcPath(conference + filePath), manager);
    }
}
