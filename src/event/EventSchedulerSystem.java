package event;

import account.AccountManager;
import room.RoomManager;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * An upper controller of event, mostly in charge of creating and modifying events.
 * Stores RoomManager and EventPresenter, also uses AccountManager and EventManager.
 * Gets input from user and gives corresponding moves by calling methods from EventManager, AccountManager or RoomManager.
 * @author Group0694
 * @version 2.0.0
 */
public class EventSchedulerSystem {

    private final EventPresenter presenter;
    private final RoomManager rooms;

    /**
     * Constructs a EventSchedulerSystem
     * @param roomManager A copy of the RoomManager use case
     */
    public EventSchedulerSystem(RoomManager roomManager){
        this.rooms = roomManager;
        presenter = new EventPresenter();
    }

    private String inputEventType(Scanner c, EventManager eventManager) {
        List<String> allEventTypes = eventManager.allEventTypes();
        presenter.printEventTypeMenu(allEventTypes);
        String input;
        int len = allEventTypes.size();
        do {
            input = c.nextLine();
            if (input.matches("[\\d]")) {
                int num = Integer.parseInt(input);
                if (num < len) {
                    return allEventTypes.get(num);
                }
            } else {
                if (input.equals("r")) {
                    presenter.printRedoMessage("Return to the previous page");
                    return null;
                }
            }
            presenter.printInvalidInput();
            presenter.getInput();
        } while(true);
    }

    private String inputNonemptyString(Scanner c){
        String name;
        while (true) {
            name = c.nextLine();
            if (name.trim().isEmpty()) {
                presenter.printErrorMessage("Input must be empty or all space");
                presenter.getInput();
            }
            else break;
        }
        return name;
    }

    private boolean isWrongFormat(String myTime) {
        try {
            Timestamp.valueOf(myTime);
            return false;
        } catch (IllegalArgumentException e) {
            return true;
        }
    }

    private String inputRoom(Scanner c, SortedSet<Timestamp[]> timeDuration, List<String> requiredFeatures, int capacity) {
        List<String> roomList = rooms.availableRooms(timeDuration, requiredFeatures, capacity);
        if (roomList.size() == 0) {
            presenter.printNoAvailableRoom();
            return null;
        }
        presenter.askEventLocation();
        presenter.printMenu(roomList, "cancel event operation");
        return askChoice(c, roomList);
    }

    private String askChoice(Scanner sc, List<String> optionList){
        while (true){
            String input = sc.nextLine();
            if (input.equalsIgnoreCase("r")) return null;
            else if (input.matches("[\\d]+") && Integer.parseInt(input) < optionList.size()){
                return optionList.get(Integer.parseInt(input));
            }else presenter.printInvalidInput();
        }
    }

    private int inputCapacity(Scanner c) {
        presenter.askEventCapacity();
        String capacityString = c.nextLine();
        int capacity;
        try {capacity = Integer.parseInt(capacityString);
            if (capacity <= 0) {
                presenter.printErrorMessage("Capacity has to be greater than or equal to 1");
                return -1;
            }
            return capacity;
        } catch (NumberFormatException e) {
            presenter.notInteger();
            return -1;
        }
    }

    private Timestamp inputTime(Scanner c, Timestamp timeBefore, String message) {
        // message == "start" or "end"
        Timestamp o;
        String time;
        presenter.askStartTime(message);
        do {
            time = c.nextLine();
            if (isWrongFormat(time + ":00.0")) {
                presenter.wrongTimeFormatWithHours();
                continue;
            }
            o = Timestamp.valueOf(time + ":00.0");

            // Check the time is after current time or start time
            if (o.after(timeBefore)) {
                return o;
            } else {
                presenter.getInvalidTimeMessage(timeBefore);
            }

        } while(true);
    }

    private SortedSet<Timestamp[]> inputDurationTime(Scanner c) {
        NavigableSet<Timestamp[]> sortedSet = new TreeSet<>((Comparator<Timestamp[]> & Serializable)
                (Timestamp[] t1, Timestamp[] t2) -> {
                    // t1 == [startTime1, endTime1]
                    // t2 == [startTime2, endTime2]
                    // Check for start time
                    if (!t1[1].after(t2[0])) {
                        return -1;
                    }
                    if (!t2[1].after(t1[0])) {
                        return 1;
                    }
                    return 0;
                }
        );
        String input;
        do {
            presenter.askEventDuration(sortedSet.size());
            input = c.nextLine();
            switch (input) {
                case "y":
                    Timestamp currTime = new Timestamp(new Date().getTime());
                    Timestamp myTime = inputTime(c, currTime, "start");
                    Timestamp endTime = inputTime(c, myTime, "end");
                    Timestamp[] cTime = {myTime, endTime};
                    if (checkValidDuration(sortedSet, cTime)) {
                        sortedSet.add(new Timestamp[]{myTime, endTime});
                    } else {
                        presenter.printTimeOverlap();
                        presenter.getInput();
                    }
                    break;
                case "r":
                    return sortedSet;
                default:
                    presenter.printInvalidInput();
                    presenter.getInput();
            }
        } while (true);
    }

    private boolean checkValidDuration(NavigableSet<Timestamp[]> s, Timestamp[] currTime) {
        Timestamp[] lower = s.lower(currTime);
        Timestamp[] higher;
        if (lower == null) {
            higher = s.higher(currTime);
            if (higher == null) {
                return s.size() == 0;
            }
        } else {
            higher = s.higher(lower);
            if (higher == null) {
                return true;
            }
        }
        return !currTime[1].after(higher[0]);

    }

    private List<EventWithSpecObserver> inputHost(Scanner sc, AccountManager accounts,
                                   SortedSet<Timestamp[]> eventDuration, int speakerNum, String eventId) {
        List<String> speakers = accounts.getAvailableSpeakers(eventDuration, eventId);
        if (speakers.size() < speakerNum || (speakerNum == -1 && speakers.size() < 2)){
            presenter.printErrorMessage("Not enough speakers free at this time.");
            return null;
        }
        List<EventWithSpecObserver> host = new ArrayList<>();
        while (speakers.size() > 0 && (speakerNum == -1 || host.size() < speakerNum)) {
            presenter.askTalkSpeaker(speakerNum);
            presenter.printMenu(speakers, "cancel scheduling speakers for this event");
            String input = sc.nextLine();
            if (speakerNum == -1 && input.equalsIgnoreCase("s")) {
                if (host.size() > 1) break;
                else presenter.printErrorMessage("Please enter more speakers");
            } else if (input.equalsIgnoreCase("r")) {
                return null;
            } else if (input.matches("^[0-9]*$") && Integer.parseInt(input) < speakers.size()){
                host.add(accounts.getEventObserver(speakers.get(Integer.parseInt(input))));
                speakers.remove(Integer.parseInt(input));
                presenter.scheduleSpeakerMessage(true);
            }else {
                presenter.printInvalidInput();
            }
        }
        return host;
    }

    private boolean inputVIP(Scanner sc){
        presenter.askVIP();
        String input = sc.nextLine();
        return input.equalsIgnoreCase("y");
    }

    /**
     * Receives the input from user, and create an event by given information.
     * Ask for information step by step, requires user to give information following text prompt.
     * Gives feedback to user by print out strings, gives feedback when this event is successfully created,
     * or this event is not able to be created.
     * @param organizerName username of the organizer that create event.
     * @param accounts A copy of the AccountManager, used for assigning speakers
     * @param events A copy of the EventManager, used for adding events
     */
    public void createEvent(String organizerName, AccountManager accounts, EventManager events) {
        Scanner sc = new Scanner(System.in);
        String type = inputEventType(sc, events);
        if (type == null) {
            presenter.eventCreateMessage(false);
            return;
        }
        presenter.askEventName();
        String name = inputNonemptyString(sc);

        SortedSet<Timestamp[]> timeDuration = inputDurationTime(sc);
        if (timeDuration.size() == 0) {
            presenter.printTimeDurationEmpty();
            presenter.eventCreateMessage(false);
            return;
        }

        List<String> features = getEventFeatures(sc);

        int capacity = inputCapacity(sc);
        if (capacity == -1) {
            presenter.eventCreateMessage(false);
            return;
        }

        String location = inputRoom(sc, timeDuration, features, capacity);
        if (location == null) {
            presenter.eventCreateMessage(false);
            return;
        }

        presenter.askEventDescription();
        String description = inputNonemptyString(sc);

        // Notify room observer and all empty observer list
        String eventID = events.createEvent(type, name, accounts.getEventObserver(organizerName),
                timeDuration, rooms.getEventObserver(location),
                description, capacity);
        int numHosts = events.numSpeakers(eventID);

        events.setVIP(eventID, inputVIP(sc));

        events.setRequiredFeatures(eventID, features);

        List<EventWithSpecObserver> host = inputHost(sc, accounts, timeDuration, numHosts, eventID);
        if (host == null) {
            // Notify room observer and all empty observer to remove event
            events.cancelEvent(eventID);
            presenter.eventCreateMessage(false);
        }
        else {
            // rooms.addEventToRoom(location, eventID, timeDuration);
            events.scheduleSpeaker(eventID, host);
            events.updateHostAdd(eventID);
            // accounts.addToSpecialList(eventID, organizerName);
            presenter.eventCreateMessage(true);
            presenter.descriptionMessage(events.provideDescription(eventID));
        }
    }

    private List<String> getEventFeatures(Scanner sc){
        presenter.askEventFeatures();
        List<String> availableFeatures = new ArrayList<>();
        rooms.getAllFeatures().forEachRemaining(availableFeatures::add);
        List<String> featuresForEvent = new ArrayList<>();

        while (true){
            presenter.printMenu(availableFeatures, "Stop adding new features to Event");
            String feature = askChoice(sc, availableFeatures);
            if (feature == null) return featuresForEvent;
            else {
                featuresForEvent.add(feature);
                availableFeatures.remove(feature);
                presenter.printActionMessage("This event now require " + feature + " as a feature.");
            }
        }
    }

    private String inputEventID(EventManager events, Scanner sc){
        presenter.askEventId();
        String eventId = sc.nextLine();
        if (!events.isValidEvent(eventId)){
            presenter.printErrorMessage("Event not exist or is already passed");
            return null;
        }else return eventId;
    }

    /**
     * Modifies a event, allows changing the time, location or speakers of the event.
     * Event id and new start time are given by user input with prompt to enter event id and new schedule.
     * Asks for event id first, if id is invalid, give user feedback that id is invalid.
     * Then asks for other information needed to modify this event
     * If rescheduling a time, then all attendees that are not free at that time will be removed from the event
     * @param accounts A copy of the AccountManager, used for reassigning speakers
     * @param events A copy of the EventManager, used for modifying events
     */
    public void modifyEvent(EventManager events, AccountManager accounts) {
        Scanner sc = new Scanner(System.in);
        String eventId = inputEventID(events, sc);
        if (eventId == null) return;

        presenter.printMenu(Arrays.asList("Reschedule Time",
                "Reschedule Speaker",
                "Reschedule Room",
                "Change Event Capacity"),
                "to cancel reschedule the event");

        while (true){
            String input = sc.nextLine();
            // Not using strategy pattern as there are good helpers in this class that can be used
            // This avoids a large number of duplicate code
            if (input.equals("0")) {
                rescheduleTime(sc, eventId, events, accounts);
                return;
            }
            else if (input.equals("1")) {
                rescheduleSpeaker(sc, eventId, events, accounts);
                return;
            }
            else if (input.equals("2")) {
                rescheduleRoom(sc, eventId, events);
                return;
            }
            else if (input.equals("3")){
                changeCapacity(sc, eventId, events);
                return;
            }
            else if (input.equalsIgnoreCase("r")) return;
            else presenter.printInvalidInput();
        }
    }

    private void rescheduleRoom(Scanner c, String eventID, EventManager eventManager) {
        // Timestamp start = eventManager.getTime(eventID), end = eventManager.getEndTime(eventID);
        Iterator<String> lst = eventManager.getRequiredFeatures(eventID);
        List<String> requiredFeaturesList = new ArrayList<>();
        while (lst.hasNext()) {
            requiredFeaturesList.add(lst.next());
        }
        SortedSet<Timestamp[]> timeDuration = eventManager.getEventDuration(eventID);
        String room = this.inputRoom(c, timeDuration, requiredFeaturesList,
                eventManager.getCapacity(eventID));
        if (room == null) {
            presenter.rescheduleMessage(false);
            return;
        }
        if (rooms.getRoomCapacity(room) < eventManager.getCapacity(eventID)) {
            presenter.printErrorMessage("Room does not have enough capacity for this event.");
            presenter.rescheduleMessage(false);
            return;
        }
        // Notify room add and remove
        eventManager.changeRoom(eventID, rooms.getEventObserver(room));
        presenter.rescheduleMessage(true);
    }

    private void rescheduleSpeaker(Scanner c, String eventID,
                                   EventManager eventManager, AccountManager accountManager){
        SortedSet<Timestamp[]> timeDuration = eventManager.getEventDuration(eventID);
        List<EventWithSpecObserver> speakers = inputHost(c, accountManager, timeDuration,
                eventManager.numSpeakers(eventID), eventID);
        if (speakers == null) {
            presenter.scheduleSpeakerMessage(false);
        } else {
            eventManager.scheduleSpeaker(eventID, speakers);
            presenter.rescheduleMessage(true);
        }
    }

    private List<Timestamp[]> checkTime(SortedSet<Timestamp[]> previousTimeSlot, SortedSet<Timestamp[]> currTimeSlot) {
        List<Timestamp[]> lst = new ArrayList<>();
        for (Timestamp[] previousTime : previousTimeSlot) {
            Timestamp startTime = previousTime[0];
            Timestamp endTime = previousTime[1];
            for (Timestamp[] currTime : currTimeSlot) {
                Timestamp newStartTime = currTime[0];
                Timestamp newEndTime = currTime[1];
                if (!newEndTime.after(startTime) || !newStartTime.before(endTime)) {
                    // No overlapping during this time, so good
                    return lst;
                } else if (!startTime.before(newStartTime) && !newEndTime.after(endTime)) {
                    // In between the original time, so we don't need to check
                    return lst;
                }
                if (newStartTime.before(startTime)) {
                    lst.add(new Timestamp[]{newStartTime, startTime});
                }
                if (newEndTime.after(endTime)) {
                    lst.add(new Timestamp[]{endTime, newEndTime});
                }
            }
        }
        return lst;
    }

    private void rescheduleTime(Scanner sc, String eventID,
                                   EventManager eventManager, AccountManager accountManager){
        SortedSet<Timestamp[]> newTimeDuration = inputDurationTime(sc);
        if (newTimeDuration.size() == 0) {
            presenter.printTimeDurationEmpty();
            presenter.rescheduleMessage(false);
            return;
        }
        SortedSet<Timestamp[]> previousTimeDuration = eventManager.getEventDuration(eventID);
        List<String> speakers = new ArrayList<>();
        eventManager.getHosts(eventID).forEachRemaining(speakers::add);
        for (Timestamp[] lst : this.checkTime(previousTimeDuration, newTimeDuration)) {
            Timestamp t1 = lst[0];
            Timestamp t2 = lst[1];
            if (!rooms.checkRoomAvailability(t1, t2, eventManager.getRoom(eventID))) {
                presenter.printErrorMessage("Room not available at this time");
                presenter.rescheduleMessage(false);
                // Add back
                return;
            }
            for (String speaker : speakers) {
                if (!accountManager.freeAtTime(t1, t2, speaker)) {
                    presenter.printErrorMessage("At least one speaker is not free during this time");
                    return;
                }
            }
        }
        eventManager.rescheduleEvent(eventID, newTimeDuration);
        presenter.rescheduleMessage(true);
    }

    private void changeCapacity(Scanner sc, String eventID, EventManager eventManager){
        int newCapacity = inputCapacity(sc);
        int roomCapacity = rooms.getRoomCapacity(eventManager.getRoom(eventID));
        if (newCapacity > roomCapacity) {
            presenter.capacityInvalid(roomCapacity);
            return;
        }
        if (newCapacity == -1) {
            // Already output error message
            presenter.rescheduleMessage(false);
            return;
        }
        eventManager.setCapacity(eventID, newCapacity);
        presenter.rescheduleMessage(true);
    }

    // cancel event valid for organizer
    /**
     * Cancelling an event only by Organizer. The event id is given by user input.
     * If the event id is invalid, print out feedback to user.
     * If the event id is valid, then cancel this event and print out the feedback that the event is removed successfully.
     * @param events A copy of the EventManager, used for removing events
     */
    public void cancelEvent(EventManager events) {
        Scanner sc = new Scanner(System.in);
        String eventId = inputEventID(events, sc);
        if (eventId == null) return;
        events.cancelEvent(eventId);
        presenter.cancelEvent();
    }
}
