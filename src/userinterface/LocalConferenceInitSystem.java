package userinterface;

import conferencemain.MainPresenter;
import data.DataSaver;

import java.util.List;
import java.util.Scanner;

public class LocalConferenceInitSystem<T> {

    T getManager(String conference, String filePath){
        DataSaver<T> managerDataSaver = new DataSaver<>();
        try {
            return managerDataSaver.readFromFile(
                    managerDataSaver.getSrcPath(conference + filePath));
        } catch (ClassNotFoundException exception){
            return null;
        }
    }

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

    void saveManager(String conference, String filePath, T manager){
        DataSaver<T> managerDataSaver = new DataSaver<>();
        managerDataSaver.saveToFile(managerDataSaver.getSrcPath(conference + filePath), manager);
    }
}
