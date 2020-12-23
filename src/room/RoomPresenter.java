package room;

import conferencemain.MainPresenter;

import java.util.ArrayList;
import java.util.List;

/**
 * The presenter of room.
 * Responses for presenting messages related to room to the users.
 * @author Group0065
 * @version 1.0.0
 */
class RoomPresenter extends MainPresenter {

    /**
     * Displays the description of the room info
     * @param roomInfo String representation of the room info of all existing room
     */
    void printRoomInfo(List<String> roomInfo) {
        if (roomInfo.size() == 0) {
            super.printErrorMessage("There is no room.");
            return;
        }
        for(String s : roomInfo) {
            System.out.println(s);
            super.printSeparateLine();
        }
    }

    /**
     * Informs the user if the room added to the room list successfully, if not, prints the possible reasons.
     *
     * @param addRoom is the result that get from the room manager, telling the presenter if it is added successfully.
     */

    void addANewRoom(boolean addRoom) {
        if (addRoom) {
            super.printActionMessage("The new room has been added.");
        } else {
            super.printErrorMessage("Unable to add the room, please check if the room already in the room list" +
                    "or if the room name is correct");
        }
    }

    /**
     * Informs the user that the room is already exist, no need to add it in the room list.
     */
    void roomExistMessage() { super.printErrorMessage("The room already exists");}

    /**
     * Asks the user to enter the room name, such that the system knows which room the user wants to
     * add to the room list.
     */
    void askNewRoomName(){
        System.out.println("Please enter the name of room you want to add, which will be the locations of events.");
        super.getInput();
    }

    /**
     * Asks the user to enter the number of capacity of the room, such that the system can knows
     * the capacity of the new room
     */
    void askNewRoomCapacity(){
        System.out.println("Please enter a number as the capacity of room you want to add");
        super.getInput();
    }

    /**
     * Asks the user to enter a valid number for room capacity, if the user enter an unacceptable
     * input previously.
     */
    void invalidNumber(){
        System.out.println("Please enter a valid number as the room capacity");
        super.getInput();
    }

    /**
     * Displays error message that room name cannot be empty
     */
    void roomNameEmpty() {
        printErrorMessage("Room name cannot be empty");
    }

    /**
     * Prints the time menu for user to select which time slots they would set to the new room
     */
    void printTimeMenu() {
        List<String> options = new ArrayList<>();
        options.add("select time slots for 24 hours");
        options.add("select time slots from 9am to 5pm");
        options.add("input your own time slots");
        super.printMenu(options, "return to previous page");
    }

    /**
     * Prints the instructions that user chose to input their own time slots
     */
    void printInputTimeSlots() {
        super.printSeparateLine();
        System.out.println("Choose your own time slots:");
        System.out.println("Please enter s to stop adding time slots");
        System.out.println("Please enter r to redo the previous time slots");
        System.out.println("Time should be in 24 hours format and [start time, end time)");
    }

    /**
     * Prints the error message that this time slot is empty
     */
    void printEmptyTimeSlots() {
        super.printErrorMessage("Time Slot is empty");
    }

    /**
     * Prints the action message that the given time slot is removed
     * @param timeSlot A given time slot with length 2, where timeSlot[0] < timeSlot[1]
     */
    void printRemoveTimeSlots(Integer[] timeSlot) {
        super.printActionMessage("Remove the time slot: " + timeSlot[0] + "-" + timeSlot[1]);
    }

    /**
     * Prints the instructions for user to input time
     * @param message A message that is either "start i" or "end i" where i is a natural number
     */
    void inputStartOrEndTime(String message) {
        System.out.println("Please input " + message + " time:");
        super.getInput();
    }

    /**
     * Prints the error message that the time is out of range
     * @param start true if for start time else end time
     */
    void invalidTimeRange(boolean start) {
        if (start) {
            super.printErrorMessage("Start time range must be in between 0 to 23");
        } else {
            super.printErrorMessage("End time range must be in between 1 to 24");
        }
    }

    /**
     * Prints the error message that time is overlap
     */
    void timeOverlap() {
        super.printErrorMessage("Time Overlap");
    }

    /**
     * Prints the error message that time slot is empty
     */
    void emptySlots() {
        super.printErrorMessage("Empty Time Slots");
    }

    /**
     * Prints the instructions for user to input room feature
     */
    void askRoomFeatures(){
        System.out.println("Please choose the features in this room from the menu below:\n" +
                "If the desired feature is not present, please select add new features.");
    }

    /**
     * Prints the instructions for user to input new room feature
     */
    void askNewFeature(){
        System.out.println("Please make sure the new feature is not in or similar to any existing feature.\n" +
                "Please enter the new feature for this room, or enter nothing to go back.");
        super.getInput();
    }
}