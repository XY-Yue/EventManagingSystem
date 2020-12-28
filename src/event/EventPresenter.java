package event;

import conferencemain.MainPresenter;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;

/**
 * Presenter class of the event package.
 * Contains the text message to print out to user.
 * @author Group0694
 * @version 2.0.0
 */
class EventPresenter extends MainPresenter {


    /**
     * Prints out the list of toString description of events to user.
     * If the list is empty, prints out "None."
     * @param expired The list of expired event's information
     * @param curr The list of currently available event's information
     */
    void printEventList(List<String[]> expired, List<String[]> curr) {
        System.out.println("Expired Events: ");
        for(String[] s : expired) {
            System.out.println("Event ID: " + s[1] + ", Event Name: " + s[2]);
            System.out.println("Event Duration: ");
            System.out.println(s[0]);
        }
        if (expired.size() == 0) {
            System.out.println("None");
        }
        System.out.println();
        System.out.println("Upcoming Events: ");
        for(String[] s : curr) {
            System.out.println("Event ID: " + s[1] + ", Event Name: " + s[2]);
            System.out.println("Event Duration: ");
            System.out.println(s[0]);
        }
        if (curr.size() == 0) {
            System.out.println("None");
        }
    }

    /**
     * Prints out the result of the event is successfully created or not.
     * @param create a boolean, true if the event is successfully created, else false
     */
    void eventCreateMessage(boolean create) {
        if (create){super.printActionMessage("Event create successfully.");
        }else{super.printErrorMessage("Fail to create events.");}
    }

    /**
     * Prints out the list of toString description of all events starts in given time period.
     * If there are no event in given map, tells user that no event found
     * @param events a map which stores map time to a list of event id and name of events start at the time
     */
    void getEventByTimeMessage(Map<Timestamp, List<String[]>> events) {
        boolean key = false;
        for(Timestamp time: events.keySet()) {
            for (String[] s : events.get(time)) {
                if (!key) {
                    key = true;
                }
                System.out.println("Event Id: " + s[1] + ", Event Name: " + s[2]);
                System.out.println("Event Duration: ");
                System.out.println(s[0]);
            }
        }
        if (!key) {
            super.printErrorMessage("no events found");
        }
    }

    /**
     * Prints out the list of toString description of all events held in room with given name.
     * If there is no event in given map, tells user that no event found.
     * @param events a map which has id map to the toString description of corresponding event
     * @param location The location of the events
     */
    void getEventByLocationMessage(Map<String, String> events, String location) {
        if (!events.isEmpty()) {
            System.out.println("Here are all events at the location " + location + ":");
            for (String key : events.keySet()) {
                System.out.println("Event Id: " + key + ", Event Name: " + events.get(key));
            }
        } else {
            super.printErrorMessage("no events found");
        }
    }

    /**
     * Prints out the result of the speaker is successfully scheduled or not.
     * If the speaker is not successfully scheduled, prints out the event doesn't exist(which is the only reason for failing)
     * @param schedule a boolean which is true if scheduled successfully, else false
     */
    void scheduleSpeakerMessage(boolean schedule) {
        if (schedule){
            super.printActionMessage("scheduled the speaker to this event");
        }else{
            super.printErrorMessage("Rescheduling speaker failed.");
        }
    }

    /**
     * Prints out the result of the event time is successfully rescheduled or not.
     * If the reschedule fails, print out feedback for time conflict to user.
     * @param reschedule a boolean which is true if rescheduled successfully, else false
     */
    void rescheduleMessage(boolean reschedule) {
        if (reschedule){super.printActionMessage("The event has been modified");
        }else{super.printErrorMessage("Fail to reschedule this event.");}
    }

    /**
     * Prints out the toString description parameter.
     * @param description toString to print
     */
    void descriptionMessage(String description) {
        System.out.println(description);
    }

    /**
     * Prints out result of entering event capacity.
     * @param roomCapacity the capacity of room user want to add event to.
     */
    void capacityInvalid(Integer roomCapacity) {super.printErrorMessage("The capacity is too large for the room, " +
            "it has capacity of:" + roomCapacity.toString());}

    /**
     * Prints out "Enter event id:".
     */
    void askEventId() {
        System.out.println("Enter event id:");
        super.getInput();
    }

    /**
     * Prints out "Enter event name:".
     */
    void askEventName() {
        System.out.println("Enter event name:");
        super.getInput();
    }

    /**
     * Prints out "Enter event location:".
     */
    void askEventLocation() {
        System.out.println("Enter event location (i.e., the room name):");
        super.getInput();
    }

    /**
     * Prints out "Enter event description:".
     */
    void askEventDescription() {
        System.out.println("Enter event description:");
        super.getInput();
    }

    /**
     * Prints out "Enter event capacity:".
     */
    void askEventCapacity() {
        System.out.println("Enter event capacity:");
        super.getInput();
    }

    /**
     * Prints out "Enter talk speaker:".
     */
    void askTalkSpeaker(int numSpeaker) {
        if (numSpeaker == 1) {
            System.out.println("Choose one talk speaker that is available:");
        } else if (numSpeaker == -1) { // == -1 for multiple speakers
            System.out.println("Choose talk speakers that are available.\n " +
                    "Enter s to stop adding:");
        }
    }

    /**
     * Prints out "Enter start time, format 'yyyy-mm-dd hh':".
     */
    void askStartTime(String s) {
        System.out.println("Enter " + s + " time, format 'yyyy-mm-dd hh:mm':");
        super.getInput();
    }

    /**
     * Prints out "Enter end time, format 'yyyy-mm-dd hh':".
     */
    // Difference between askStartTime, where this method can only input hh but askStartTime hh:mm
    void askEndTime(String s){
        System.out.println("Enter " + s + " time, format 'yyyy-mm-dd hh':");
        super.getInput();
    }

    /**
     * Prints out "[ERROR] Time format invalid or timeslot invalid, please enter again, format 'yyyy-mm-dd hh':".
     */
    void wrongTimeFormat() {
        super.printErrorMessage("Time format invalid, please enter again, format 'yyyy-mm-dd hh':");
        super.getInput();
    }

    /**
     * Prints out "[ERROR] Time format invalid or timeslot invalid, please enter again, format 'yyyy-mm-dd hh':".
     */
    void wrongTimeFormatWithHours() {
        super.printErrorMessage("Time format invalid, please enter again, format 'yyyy-mm-dd hh:mm':");
        super.getInput();
    }

    /**
     * Prints out "[ERROR] Event not exist.".
     */
    void eventNotExist() {
        super.printErrorMessage("Event does not exist.");
    }

    /**
     * Prints out "[ERROR] This is not a number.".
     */
    void notInteger() {
        super.printErrorMessage("This is not a number.");
    }

    /**
     * Prints out "[ACTION] Event has been canceled."
     */
    void cancelEvent() {
        super.printActionMessage("Event has been canceled.");
    }

    /**
     * Prints out the error message that end time is before start time
     */
    void endTimeBeforeStartTime() {
        super.printErrorMessage("End time is before start time.");
    }

    /**
     * Prints error message about invalid time input
     * @param startTime The time that the input must be after
     */
    void getInvalidTimeMessage(Timestamp startTime) {
        super.printErrorMessage("Invalid time, time must come after " + super.getTime(startTime));
        super.getInput();
    }

    /**
     * Prints a menu of all types of events
     * @param allEventTypes All types of events
     */
    void printEventTypeMenu(List<String> allEventTypes) {
        System.out.println("Event Type:");
        super.printMenu(allEventTypes, "return to previous page");
    }

    /**
     * Prints the error message that there is no available room for a event
     */
    void printNoAvailableRoom() {
        super.printErrorMessage("No Available Rooms during this period satisfies the event's requirements");
    }

    /**
     * Prints the message that asks if this is a VIP only event
     */
    void askVIP(){
        System.out.println("Is this a VIP only Event? Enter 'y' for yes, anything else for no.");
        super.getInput();
    }

    /**
     * Prints a message that asks for the required features for a event
     */
    void askEventFeatures(){
        System.out.println("Choose features required for this event from the below list.\n" +
                "If you do not see the desired feature, please add a room that has the desired feature.");
    }

    /**
     * Asks for event duration
     * @param num The length of event duration
     */
    void askEventDuration(int num) {
        System.out.println("Please enter y for add event duration: " + num + ", else r to stop");
        super.getInput();
    }

    /**
     * Prints the error message that time overlap
     */
    void printTimeOverlap() {
        super.printErrorMessage("Time overlap");
    }

    /**
     * Prints the error message that event time duration is empty
     */
    void printTimeDurationEmpty() {
        super.printErrorMessage("Time duration cannot be empty");
    }
}
