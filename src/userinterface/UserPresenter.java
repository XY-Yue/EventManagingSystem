package userinterface;

import conferencemain.MainPresenter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * This class is an general presenter class of all userSystem.
 * This class displays the message to the user about what selections can be performed.
 * @author Group0694
 * @version 2.0.0
 */
public class UserPresenter extends MainPresenter {

    /**
     * Provides an optionMenu for the user so that the user can understand the options based
     * on the information given by this method
     * @param username A string represents the username of this account
     */
    void optionMenu(String username){
        System.out.println("Welcome: " + username);
        List<String> lst = Arrays.asList("manage my account", "check my inbox", "access or send messages",
                "view event schedule", "access my conference system");
        super.printMenu(lst, "return to previous page");
    }

    /**
     * Provides messages to confirm whether the user wants to logout
     */
    void logOut(){
        System.out.println("Do you wish to log out?");
        System.out.println("y: Yes");
        System.out.println("n: No");
        getInput();
    }

    /**
     * Provides messages to tell user he/she has successfully log out
     */
    void logOutSuccess() {
        super.printActionMessage("You've successfully log out.");
    }

    /**
     * Returns a collections of of possible options of general message viewing option menu
     * @return a collection of options
     */
    List<String> getGeneralMessageInboxOptionMenu() {
        System.out.println("Welcome to my inbox:");
        return new ArrayList<> (Arrays.asList(
                "set message displaying order",
                "view message received",
                "view message received from specific account",
                "view message sent",
                "view message sent to a specific account",
                "view unread message",
                "view unread message from a specific account",
                "view archived message"));
    }

    /**
     * Returns a collections of of possible options of general message status and sending option menu
     * @return a collection of options
     */
    List<String> getGeneralMessageAccessOptionMenu() {
        System.out.println("Welcome to my message system:");
        return new ArrayList<> (Arrays.asList(
                "mark message as unread",
                "archive message",
                "un-archive message",
                "delete message",
                "view favourite user list contact information",
                "send message to other accounts"));
    }

    /**
     * Returns a message of returning to System option page
     * @return a String representation of message
     */
    String getReturnToMessage() {
         return "return to System option page";
     }

    /**
     * Returns a collections of of possible options of general event schedule option menu
     * @return a collection of options
     */
    List<String> getGeneralEventScheduleOptionMenu() {
        System.out.println("Welcome to my event");
        return new ArrayList<>(Arrays.asList(
                "view a schedule of events",
                "view signed up events",
                "view a schedule of events that available to you",
                "view schedule of events on a given time",
                "view schedule of events with a given room",
                "view schedule of events by a specific Speaker"
        ));
    }

    /**
     * Returns a collections of of possible options of general event attending option menu
     * @return a collection of options
     */
    List<String> getGeneralEventAccessOptionMenu() {
        System.out.println("Welcome to my event");
        return new ArrayList<>(Arrays.asList(
                "sign up for an event",
                "cancel an enrollment of event",
                "view the description of a event"
        ));
    }

    /**
     * Returns a collections of of possible options of general account option menu
     * @return a collection of options
     */
    List<String> getGeneralAccountOptionMenu() {
        System.out.println("Welcome to my account:");
        return new ArrayList<> (Arrays.asList(
                "view account info",
                "change password",
                "view favourite user list",
                "add account to favourite user list",
                "delete account from favourite user list"
        ));
    }

    /**
     * Prints the general message viewing menu
     */
    void printMessageAccessMenu() {
        super.printMenu(getGeneralMessageAccessOptionMenu(), getReturnToMessage());
    }

    /**
     * Prints the general message status and sending menu
     */
    void printMessageInboxMenu() {
        super.printMenu(getGeneralMessageInboxOptionMenu(), getReturnToMessage());
    }

    /**
     * Prints the general event schedule menu
     */
    void printEventScheduleMenu() {
        super.printMenu(getGeneralEventScheduleOptionMenu(), getReturnToMessage());
    }

    /**
     * Prints the general event attending menu
     */
    void printEventAccessMenu() {
        super.printMenu(getGeneralEventAccessOptionMenu(), getReturnToMessage());
    }

    /**
     * Prints the general account menu
     */
    void printAccountMenu() {
        super.printMenu(getGeneralAccountOptionMenu(), getReturnToMessage());
    }
}