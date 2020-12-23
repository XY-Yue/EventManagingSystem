package event;

import java.sql.Timestamp;
import java.util.*;

/**
 * An upper controller of event, mostly in charge of searching and displaying events.
 * Stores EventManager and EventPresenter, also uses AccountManager and RoomManager.
 * Gets input from user and gives corresponding moves by calling methods from EventManager, AccountManager or RoomManager.
 * @author Group0694
 * @version 2.0.0
 */
public class EventSystem {
    private final EventManager events;
    private final EventPresenter presenter;

    /**
     * Constructs a EventSystem
     * @param events a copy of the EventManager use case
     */
    public EventSystem(EventManager events) {
        this.events = events;
        this.presenter = new EventPresenter();
    }

    /**
     * Prints out a list of information of all events to user.
     * @param vip indicates if only VIP events are needed
     */
    public void printEventList(boolean vip){
        Map<String, List<String[]>> allEvents = events.getEventSchedule(vip);
        List<String[]> curr = allEvents.get("curr");
        List<String[]> expired = allEvents.get("expired");
        if (curr.size() + expired.size() == 0) {
            presenter.printErrorMessage("There is no event at this time.");
        } else {
            presenter.printEventList(expired, curr);
        }
    }

    /**
     * Shows the description of an event which the event id is given by user input.
     * Gives prompt to user to enter event id.
     * Prints out the toString description of given id, or gives feedback when this event doesn't exist.
     */
    public void checkDescription() {
        Scanner sc = new Scanner(System.in);
        presenter.askEventId();
        String eventId = sc.nextLine();
        if(events.checkEvent(eventId)) {
            presenter.descriptionMessage(events.provideDescription(eventId));
        } else {
            presenter.eventNotExist();
        }
    }

    /**
     * Gets all events in given location provided by user input.
     * Gives prompt to user to enter location, which is room name.
     * Prints out toString description of all events in the given room, or feedback if this room doesn't
     * exists.
     */
    public void getEventByLocation () {
        Scanner sc = new Scanner(System.in);
        presenter.askEventLocation();
        String location = sc.nextLine();
        presenter.getEventByLocationMessage(events.getEventByLocation(location), location);
    }

    /**
     * Gets all event with time given time period provided by user input.
     * Gives prompt to user to enter start time and end time, which has to be in a correct format.
     * If the input time is not in a correct format, then give feedback to user to let user correct the input format.
     * Ask for the start of the time period first, if get the correct format, goes to end of the time period.
     * Gets all event starts during the time period.
     * When get the correct format, return the description of all corresponding events to user.
     */
    public void getEventByTime () {
        Scanner sc = new Scanner(System.in);
        presenter.askEndTime("start");
        String time = sc.nextLine();
        while (isWrongFormat(time + ":00:00.0")){
            presenter.wrongTimeFormat();
            time = sc.nextLine();
        }
        Timestamp startTime = Timestamp.valueOf(time + ":00:00.0");
        presenter.askEndTime("end");
        String otherTime = sc.nextLine();
        while (isWrongFormat(otherTime + ":00:00.0")){
            presenter.wrongTimeFormat();
            otherTime = sc.nextLine();
        }
        Timestamp endTime = Timestamp.valueOf(otherTime + ":00:00.0");
        if (endTime.before(startTime)) {
            presenter.endTimeBeforeStartTime();
            return;
        }
        presenter.getEventByTimeMessage(events.getEventsByTime(startTime, endTime));
    }

    //private helper
    private boolean isWrongFormat(String myTime) {
        // Decide to use try catch to present corner case
        try {
            Timestamp.valueOf(myTime);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

}
