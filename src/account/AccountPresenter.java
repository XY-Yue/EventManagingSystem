package account;

import conferencemain.MainPresenter;

import java.sql.Timestamp;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.SortedSet;

/**
 * A presenter class for account.
 * It contains methods to print out text information for users, which guides the account associated operations.
 * @author Group0694
 * @version 2.0.0
 */
class AccountPresenter extends MainPresenter {

    /**
     * Prints out "Enter new password:".
     */
    void askNewPassword() {
        System.out.println("Enter new password:");
        super.getInput();
    }

    /**
     * Prints out "Enter username:".
     */
    void askUsername() {
        System.out.println("Enter username:");
        super.getInput();
    }

    /**
     * Prints out "password updated successfully".
     */
    void changePasswordResult() {
       super.printActionMessage("password updated successfully");
    }

    /**
     * Prints out the account info given as parameter.
     * @param info account info to print
     */
    void accountInfo(String info) {
        System.out.println(info);
    }

    /**
     * Prints out all info of speakers which are given in a list as parameter.
     * If there is no speakers in the list, prints "There are no speaker."
     * @param speakers list of speakers info to print
     */
    void getAllSpeakers(List<String> speakers) {
        if (!speakers.isEmpty()) {
            System.out.println(speakers.toString());
        } else {
            super.printErrorMessage("There are no speaker.");
        }
    }

    /**
     * Prints out all info of friends which are given in a iterator as parameter.
     * @param friends iterator of friend info to print
     */
    void getFriendList(Iterator<String> friends) {
        StringBuilder builder = new StringBuilder();
        while (friends.hasNext()){
            builder.append(friends.next());
            builder.append(", ");
        }
        if (builder.length() == 0) {
            super.printErrorMessage("Your contact list is empty.");
        } else {
            System.out.println("Favourite User List: " +
                    builder.delete(builder.length() - 2, builder.length()).toString());
        }
    }

    /**
     * Prints out info of all signed events of user given in a list as parameter.
     * If the list is empty, prints "you are not signed in any event."
     * @param events Iterator of event info to print
     */
    void getSignedEvents(Iterator<String[]> events) {
        if (events.hasNext()) {
            while (events.hasNext()) {
                String[] event = events.next();
                System.out.println("Time: " + super.getTime(Timestamp.valueOf(event[0])) + " to " +
                        super.getTime(Timestamp.valueOf(event[1])) + ", Event Id: " + event[2]);
            }
        } else {
            super.printErrorMessage("you are not signed in any event.");
        }
    }

    /**
     * Prints out info of all speaker of signed events given in a list as parameter.
     * If the list is empty, prints "there are no speakers".
     * @param speakers the list of speakers that need to be printed
     */
    void getSpeakerOfSignedEvents(List<String> speakers) {
        if (!speakers.isEmpty()) {
            System.out.println(speakers.toString());
        } else {
            super.printErrorMessage("there are no speakers");
        }
    }

    /**
     * Prints the specialist event
     * @param events A list of events
     */
    void printSpecialListEvents(Iterator<String> events) {
        StringBuilder stringBuilder = new StringBuilder();
        while (events.hasNext()){
            stringBuilder.append(events.next());
            stringBuilder.append(", ");
        }
        if (stringBuilder.length() != 0) {
            System.out.println(stringBuilder.delete(
                    stringBuilder.length() - 2, stringBuilder.length()).toString());
        } else {
            super.printErrorMessage("No event found.");
        }
    }

    /**
     * Prints out feedback of if successfully add an user to friend list or not.
     * @param r a boolean true if added successfully, else false
     */
    void addFriendResult (boolean r){
        if (r) {
            super.printActionMessage("account successfully added into favourites.");
        } else {
            super.printErrorMessage("user does not exist or is already in your favourites.");
        }
    }

    /**
     * Prints out feedback of if successfully removed an user from friend list or not.
     * @param r a boolean true if removed successfully, else false
     */
    void deleteFriendResult (boolean r){
        if (r) {
            super.printActionMessage("account successfully removed from your favourites.");
        } else {
            super.printErrorMessage("user does not exist or is not in your favourites yet.");
        }
    }

    /**
     * Prints out feedback of if successfully signed up an event.
     * @param r a boolean true if signed up successfully, else false
     */
    void signUpEventResult ( boolean r){
        if (r) {
            super.printActionMessage("event signed successfully.");
        } else {
            super.printErrorMessage("fail to sign event.");
        }
    }

    /**
     * Prints out feedback of if successfully cancelled an event.
     * @param r a boolean true if cancelled successfully, else false
     */
    void cancelEventResult ( boolean r){
        if (r) {
            super.printActionMessage("event canceled successfully.");
        } else {
            super.printErrorMessage("event and time not match.");
        }
    }

    /**
     * Prints out a message to ask user for an input of event id.
     */
    void askEventId () {
        System.out.println("Enter event id:");
        super.getInput();
    }

    /**
     * Prints out an error message regarding the user's input event doesn't exist.
     */
    void eventNotExist () {
        super.printErrorMessage("Event does not exist.");
    }

    /**
     * Prints out an error message regarding the user's input event time does not fit in user's schedule.
     */
    void signupFailNotFree () {
        super.printErrorMessage("User is not free at this time.");
    }

    /**
     * Prints out an error message regarding the user's input event is already full.
     */
    void signupFailEventFull () {
        super.printErrorMessage("The event is already full.");
    }

    /**
     * Prints out an error message regarding the user's input Speaker doesn't exist.
     */
    void speakerNotExist () {
        super.printErrorMessage("Speaker does not exist.");
    }

    /**
     * Prints out a message to ask user for an input of speaker username.
     */
    void askSpeakerName () {
        System.out.println("Enter speaker username:");
        super.getInput();
    }

    /**
     * Prints out an error message the user's input of new password is same with the current one.
     */
    void samePassword () {
        super.printErrorMessage("The new password cannot be same with the latest one.");
    }

    /**
     * Prints out the error message that user did not sign up for this event
     * @param username username of this user
     * @param eventId event id of this event
     */
    void userNotSignUpEvent(String username, String eventId) {
        super.printErrorMessage("User " + username + " did not sign up for the event " + eventId);
    }

    /**
     * Prints out the list header of all speakers
     */
    void printSpeakerListMessage() {
        System.out.println("Speaker List: ");
    }

    /**
     * Prints the error message that no event available
     */
    void printNotAvailable() {
        super.printErrorMessage("No Event Available");
    }

    /**
     * Prints out the list of toString description of events to user.
     * @param events A list of event id
     * @param m A map that maps event id to its duration
     * @param message additional message
     */
    void printEventList(List<String> events, Map<String, SortedSet<Timestamp[]>> m, String message) {
        System.out.println(message);
//        for(String[] s : events) {
//            System.out.println("Starting Time: " + s[0] + ", End Time: " + s[1] + ", Event Id: " + s[2] + ", Event Name: " + s[3]);
//        }
        for (String eventId : events) {
            System.out.println("Event Id: " + eventId);
            System.out.println("Event Duration: ");
            for (Timestamp[] t : m.get(eventId)) {
                System.out.println("\tStarting Time: " + t[0] + ", End Time: " + t[1]);
            }
            super.printSeparateLine();
        }
    }

    /**
     * Prints out the message that a event is already expired
     * @param eventId The ID of the event that's expired
     */
    void printEventExpired(String eventId) {
        super.printErrorMessage("Event: " + eventId + " was expired or in process.");
    }

    /**
     * Prints out the message that a event is already passed
     * @param eventId The ID of the event that's passed
     */
    void printPassedEvent(String eventId) {
        super.printErrorMessage("Event: " + eventId + " not exist or is already passed");
    }

    /**
     * Prints the message of upgrade account
     * @param key true iff upgraded successfully
     * @param username The input username
     */
    void printUpgradeAccount(boolean key, String username) {
        if (key) {
            printActionMessage("Upgraded: " + username + " to a VIP account");
        } else {
            printErrorMessage("Invalid username or user: " + username + " is not an attendee");
        }
    }

    /**
     * prints the message of degrade account
     * @param key true iff degraded successfully
     * @param username The input username
     */
    void printDegradeAccount(boolean key, String username) {
        if (key) {
            printActionMessage("Degraded: " + username + " to an Attendee account");
        } else {
            printErrorMessage("Invalid username or user: " + username + " is not a VIP");
        }
    }
}
