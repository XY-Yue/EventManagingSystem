package message;

import conferencemain.MainPresenter;

import java.util.ArrayList;
import java.util.List;
/**
 * Presenter class of the message package.
 * Contains the text message to print out to user.
 * @author Group0694
 * @version 2.0.0
 */
class MessagePresenter extends MainPresenter {

    /**
     * Prints all the message
     * @param message A List of String representation of message
     * @param welcomeMessage A String representation of welcome message if the message list is not empty
     * @param errorMessage A String representation of error message if message list is empty
     */
    void printAllMessages(List<String> message, String welcomeMessage, String errorMessage) {
        if (!message.isEmpty()) {
            System.out.println(welcomeMessage);
            super.printSeparateLine();
            for (String m : message) {
                System.out.println(m);
            }
        } else {
            super.printErrorMessage(errorMessage);
            System.out.println();
        }
    }


    /**
     * Prints the message sent
     * @param ableToMessage true iff message sent successfully
     */
    void printSendMessage(boolean ableToMessage){
        if(ableToMessage){
            super.printActionMessage("Message sent successfully.");
        }
        else{
            super.printErrorMessage("Message is unable to be sent.");
        }
    }

    /**
     * Asks the user to input the name from a specific user.
     */
    void askSenderName(boolean send){
        System.out.println("get messages sent " + ((send)? "to" : "by") + " user:");
        super.getInput();
    }

    /**
     * Prints the error message that account does not exist
     */
    void accountNotExist(){super.printErrorMessage("Account not exist, enter another account or 's' to stop adding receivers.");}

    /**
     * Prints the error message that user cannot send message to themselves
     */
    void sendToSelf() {
        super.printErrorMessage("You cannot send message to yourself");
    }

    /**
     * Asks the user to enter content
     */
    void askContent(){
        System.out.println("Enter message content:");
        super.getInput();
    }

    /**
     * Asks the user to enter message subject
     */
    void askSubject(){
        System.out.println("Enter message subject:");
        super.getInput();
    }

    /**
     * Asks to whom this message the user wants to send, and informs the user the proper format to enter information.
     */
    void askReceivers(){
        System.out.println("Enter receivers of the message");
        System.out.println("Directly enter the username of receivers. Enter 's' to stop adding receivers.");
        super.getInput();
    }

    /**
     * Informs the user there is no speaker
     */
    void noSpeakers(){super.printErrorMessage("There is no speaker currently.");}

    /**
     * Prints the error message that is no attendee currently
     */
    void noAttendees(){super.printErrorMessage("There is no attendee currently.");}

    /**
     * Tells the user to enter event id that the user wants to send messages.
     */
    void askEventID(){
        System.out.println("Keep adding event id you want to send message to, enter 's' for stop entering events:");
        super.getInput();
    }

    /**
     * Asks user to input message id
     */
    void askMessageID() {
        System.out.println("Please enter the message id:");
        super.getInput();
    }

    /**
     * Prints the error message that message id is invalid
     * @param input A String representation of message id
     */
    void printInvalidMessageId(String input) {
        super.printErrorMessage("Invalid Message Id: " + input);
    }

    /**
     * Prints the action done with this message
     * @param input A String representation of this message id
     * @param action A String representation of the action on this message
     */
    void printActionMessageId(String input, String action) {
        super.printActionMessage(action + " the Message: " + input);
    }

    /**
     * Informs the user that the user is not a current speaker
     */
    void notSpeaker(){super.printErrorMessage("You are not the speaker of event");}

    /**
     * Informs the user that the the event does not exist
     */
    void noEvent(){super.printErrorMessage("The event does not exist");}

    /**
     * informs the user that the user is not able to send message.
     */
    void notMessagable(){super.printErrorMessage("You are not able to send message currently.");}

    /**
     * informs there are no receivers.
     */
    void noReceivers(){super.printErrorMessage("There are no receivers.");}

    /**
     * Prints the error message that username is already in the receiver list
     */
    void alreadyInLIst(String username) {
        super.printErrorMessage("User " + username + " is already in the receiver list");
    }

    /**
     * Prints the displaying order menu for user to select which way to sort the message
     */
    void printDisplayOrderMenu() {
        System.out.println("Message Displaying Menu");
        List<String> options = new ArrayList<>();
        options.add("set order from newest to oldest");
        options.add("set order from oldest to newest");
        super.printMenu(options, "return to Message System");
    }

    /**
     * Prints the action message that message displaying order has been changed
     * @param order A String representation of the new order
     */
    void setMessageOrder(String order) {
        super.printActionMessage("Set message displaying order to " + order);
    }

    /**
     * Prints the current order of displaying message
     * @param order A String representation of current displaying order
     */
    void currentOrder(String order) {
        System.out.println("Current displaying order: " + order);
    }

    /**
     * Prints the error message that user does not exist
     */
    void accountDoesNotExist() {
        super.printErrorMessage("User does not exist");
    }
}