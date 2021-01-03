package event;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;


/**
 * An use case class of event. Implements Serializable.
 * Stores all event in a map with event id of event map to the corresponding event. Also a sorted map with
 * time map to a list of toString of events start at that time.
 * Also stores total number of events.
 * Has methods to construct new events, get event with given information, and change the information of event.
 * @author Group0694
 * @version 2.0.0
 */
public class EventManager implements Serializable {
    // Type map to Hashmap <id, Event>
    private final Map<String, Map<String, Event>> eventList;
    // Key first star time, map a list of event happens during this time
    // Value is a list of events starts with this time, String[] == {event time list to string, event id, event name}
    private final SortedMap<Timestamp, List<String[]>> eventSchedule;
    private int numEvents;
    private final String[] eventType = {"talk", "party", "panel discussion"};

    /**
     * Constructs a EventManager, set everything to empty.
     */
    public EventManager() {
        eventList = new HashMap<>();
        eventSchedule = new TreeMap<>();
        numEvents = 0;
    }

    private Event findEvent(String id) {
        for (Map<String, Event> events: eventList.values()) {
            if (events != null && events.get(id) != null) {
                return events.get(id);
            }
        }
        return null;
    }

    private int totalNumberOfEvents(){
        int sum = 0;
        for (Map<String, Event> events: eventList.values()){
            if (events != null) sum += events.size();
        }
        return sum;
    }

    /**
     * Gets all existing event types
     * @return a list of string representations of all existing event types
     */
    public List<String> allEventTypes(){ return Arrays.asList(eventType); }

    /**
     * Gets Schedule of all events
     * @return A list of arrays where in the format [time, id, event name]
     * @param vip indicates if only VIP events are wanted
     */
    Map<String, List<String[]>> getEventSchedule(boolean vip) {
        List<String[]> curr = new ArrayList<>();
        List<String[]> expired = new ArrayList<>();
        Timestamp currTime = new Timestamp(new Date().getTime());
        for (Timestamp t : eventSchedule.keySet()) {
            List<String[]> value = eventSchedule.get(t);
            if (currTime.before(t)) {
                addEventAtTimeToList(vip, curr, value);
            } else {
                addEventAtTimeToList(vip, expired, value);
            }
        }
        Map<String, List<String[]>> m = new HashMap<>();
        m.put("curr", curr);
        m.put("expired", expired);
        return m;
    }

    private void addEventAtTimeToList(boolean vip, List<String[]> list, List<String[]> value) {
        for (String[] item : value) {
            Event event = this.findEvent(item[1]);
            if (!vip || event != null && event.isVIP())// (not VIP only) or (VIP only and check if event is VIP)
                list.add(item);
                // list.add(getEventScheduleValue(t, item));
        }
    }

    /**
     * Checks if an event exist.
     * @param eventID id of the target event
     * @return true iff event exists
     */
    public boolean checkEvent(String eventID) {
        for (Map<String, Event> events: eventList.values()) {
            if (events != null && events.get(eventID) != null) {
                return true;
            }
        }
        return false;
    }

    /**
     * Creates a new event with given information, and then add the new constructed event into
     * eventList and eventSchedule.
     * Assumes that it has been checked to make sure there is only one event in a given room.
     * Assumes that it has been checked to make sure the capacity does not exceed the room capacity.
     * @param name name of the new event
     * @param timeDuration A sorted collection of time interval where start time is at index 0 and end time is index 1
     * @param location An instance of EventObserver, represents the new event held in
     * @param description description of the new event
     * @param capacity the max number of people can participate in the new event
     * @param type the type of the event
     * @return return event id if create successfully, otherwise return null.
     */
    String createEvent(String type, String name, EventWithSpecObserver organizer, SortedSet<Timestamp[]> timeDuration,
                              EventObserver location, String description, int capacity) {
        numEvents = Math.max(numEvents, totalNumberOfEvents());
        String id = "E" + numEvents;

        Event newEvent = new EventFactory().makeEvent(type, name, organizer, timeDuration, location,
                description, capacity, id);
        if (newEvent == null) return null;

        this.eventList.computeIfAbsent(type.toUpperCase(), k -> new HashMap<>());
        this.eventList.get(type.toUpperCase()).put(id, newEvent);
        this.eventSchedule.computeIfAbsent(newEvent.getFirstTime(), k -> new ArrayList<>());
        this.eventSchedule.get(newEvent.getFirstTime()).add(new String[]{newEvent.printEventDuration(), id, name});
        numEvents++;
        return id;
    }

    /**
     * Gets all the events starts during the given period of time.
     * @param time start of the time period
     * @param endTime end of the time period
     * @return a map contains all events start during the given period of time with start time map to them
     */
    Map<Timestamp, List<String[]>> getEventsByTime(Timestamp time, Timestamp endTime){
        return this.eventSchedule.subMap(time, endTime);
    }

    /**
     * Gets all the events are held in a given room.
     * @param location the name of given room
     * @return a map contains all events are held in the given room by event id mapped to event name
     */
    Map<String, String> getEventByLocation(String location){
        Map<String, String> result = new HashMap<>();
        for(Map<String, Event> events: eventList.values()){
            if(events != null){
                for (Event event : events.values()){
                    if (event != null && event.getLocation().equalsIgnoreCase(location)) result.put(
                            event.getId(), event.getName());
                }
            }
        }
        return result;
    }

    /**
     * Adds a given user as attendee into the event with given id.
     * @param observer An instance of of EventWithSpecObserver, represents the attendee of this event
     * @param id id of the event we want to add the user in
     * @return true of added successfully, else false
     */
    public boolean addAttendee(EventWithSpecObserver observer, String id){
        Event event = findEvent(id);
        return !(event == null || event.isInEvent(observer)) && event.addAttendee(observer);
    }

    /**
     * Removes a given user from attendee of the event with given id.
     * @param observer An instance of EventWithSpecObserver, represents the attendee of this event
     * @param eventID id of the event we want to add the user in
     * @return true iff removed successfully
     */
    public boolean removeAttendee(EventWithSpecObserver observer, String eventID){
        Event event = findEvent(eventID);
        return event != null && event.removeAttendee(observer);
    }

    /**
     * Schedules a speaker into a given event.
     * @param eventID id of the event we want to schedule the speaker to
     * @param username A collection of EventWithSpecObserver, represents the user we want to schedule as the speaker
     *                 of the event
     * @return true iff scheduled successfully
     */
    boolean scheduleSpeaker(String eventID, List<EventWithSpecObserver> username) {
        Event event = findEvent(eventID);
        if (event != null) {
            return event.changeHost(username);
        }
        return false;
    }

    /**
     * Reschedules an event, change its start startTime to the given startTime.
     * @param eventID id of the event we want to change startTime
     * @param timeDuration A sorted collection of time interval where start time is at index 0 and end time is index 1
     */
    void rescheduleEvent(String eventID, SortedSet<Timestamp[]> timeDuration) {
        Event event = findEvent(eventID);
        if (event != null) {  //&& timeDuration.first()[0] != event.getFirstTime()
            String name = event.getName();
            Timestamp startTime = timeDuration.first()[0];
            for(String[] lst : eventSchedule.get(event.getFirstTime())) {
                // lst[0] is the time duration
                if (lst[1].equals(eventID) && lst[2].equals(name)) {
                    eventSchedule.get(event.getFirstTime()).remove(lst);
                    break;
                }
            }
            // Notify room, host, and attendee observers
            event.setTime(timeDuration);
            eventSchedule.computeIfAbsent(startTime, k -> new ArrayList<>());
            eventSchedule.get(startTime).add(new String[]{event.printEventDuration(), eventID, name});
        }
    }

    /**
     * Cancels an event.
     * @param eventID id of the event we want to cancel. It also notifies all the observers.
     * @return true iff rescheduled successfully
     */
    boolean cancelEvent(String eventID) {
        Event event = findEvent(eventID);
        if (event == null) return false;
        List<String[]> events = eventSchedule.get(event.getFirstTime());
        if (events == null) return false;
        // Notify all observers remove
        event.notifyRoomRemove();
        event.notifyHostRemove();
        event.notifyAttendeesRemove();
        for (Map<String, Event> eventMap : eventList.values()) {
            eventMap.remove(eventID);
        }
        int index = -1;
        for (int i=0;i<events.size();i++) {
            if (events.get(i) != null && eventID.equalsIgnoreCase(events.get(i)[1])) {
                index = i;
                break;
            }
        }
        if (index != -1) events.remove(index);
        return true;
    }

    /**
     * Provides the description of event with given id.
     * The description is toString of the event.
     * @param ID id of the event we want to get description
     * @return the toString of event with given id if it exists
     */
    String provideDescription(String ID){
        Event event = findEvent(ID);
        return (event == null) ? null : event.toString();
    }

    /**
     * Returns the hosts of a given event.
     * @param eventID id of the event we want to work on
     * @return An iterator of hosts for this event, or null if this event does not exist
     */
    public Iterator<String> getHosts(String eventID){
        Event event = findEvent(eventID);
        return (event == null) ? null : event.getHosts();
    }

    /**
     * Returns all attendees of a given event.
     * @param eventID id of the event we want to work on
     * @return An iterator of hosts for this event, or null if this event does not exist
     */
    public Iterator<String> getAttendees(String eventID){
        Event event = findEvent(eventID);
        return (event == null) ? null : event.getAttendees();
    }

    /**
     * Checks if the attendee has signed up with the given event id
     * @param eventID id of the event
     * @param observer An instance of EventWithSpecObserver, represents the attendee of this event
     * @return true iff this user has signed up for this event
     */
    public boolean hasAttendee(String eventID, EventWithSpecObserver observer) {
        Event event = findEvent(eventID);
        return event != null && event.isInEvent(observer);
    }

    /**
     * Gets event duration with given event Id
     * @param eventId A String representation of event id
     * @return An instance of SortedSet of this event duration
     */
    public SortedSet<Timestamp[]> getEventDuration(String eventId) {
        Event event = findEvent(eventId);
        if (event == null) {
            return null;
        }
        SortedSet<Timestamp[]> sortedSet = new TreeSet<>((Comparator<Timestamp[]> & Serializable)
                (Timestamp[] t1, Timestamp[] t2) -> {
                    if (!t1[1].after(t2[0]))
                        return -1;
                    if (!t2[1].after(t1[0]))
                        return 1;
                    return 0;
                }
        );
        Iterator<Timestamp[]> t = event.getDuration();
        t.forEachRemaining(sortedSet::add);
        return sortedSet;
    }

    /**
     * Returns room of a given event.
     * @param eventID id of the event we want to work on
     * @return room where the event located
     */
    String getRoom(String eventID){
        Event event = findEvent(eventID);
        return (event == null) ? null : event.getLocation();
    }

    /**
     * Checks of there are still space in the event
     * @return true if and only if the event is not full
     */
    public boolean canSignup(String eventID){
        Event event = findEvent(eventID);
        return event != null && event.canSignup();
    }

    /**
     * Gets the number of speakers required for a given event.
     * @param eventID id of the target event
     * @return the number of speakers this event requires, or -2 if the event does not exist
     */
    int numSpeakers(String eventID){
        Event event = findEvent(eventID);
        return (event == null) ? -2 : event.getNumSpeaker(); // -2 if event not exist, -1 if multiple speakers
    }

    /**
     * Gets the capacity for a given event.
     * @param eventID id of the target event
     * @return the capacity this event requires, or -1 if the event is not found
     */
    int getCapacity(String eventID){
        Event event = findEvent(eventID);
        return (event == null) ? -1 : event.getCapacity();
    }

    /**
     * Sets a new capacity for a event
     * @param eventID id of the target event
     * @param newCapacity The new capacity for a event
     * @return true iff the event exists
     */
    boolean setCapacity(String eventID, int newCapacity){
        Event event = findEvent(eventID);
        if (event == null) return false;
        else {
            event.setCapacity(newCapacity);
            return true;
        }
    }

    /**
     * Sets a new location for a event
     * @param eventID id of the target event
     * @param room An instance of EventObserver, represents the new location for a event, assumes it is valid
     * @return true iff the event exists
     */
    boolean changeRoom(String eventID, EventObserver room){
        Event event = findEvent(eventID);
        if (event == null) return false;
        else {
            event.setLocation(room);
            return true;
        }
    }

    /**
     * Checks if the event is still valid or expired
     * @param eventID id of the target event
     * @return true iff the event is not passed and is therefore valid
     */
    public boolean isValidEvent(String eventID) {
        Event event = findEvent(eventID);
        return event != null && event.getFirstTime().after(new Timestamp(new Date().getTime()));
    }

    /**
     * Updates the VIP status of a event
     * @param eventID id of the target event
     * @param isVIP The new VIP status of the target event
     * @return true iff the event exists
     */
    boolean setVIP(String eventID, boolean isVIP){
        Event event = findEvent(eventID);
        if (event == null) return false;
        else {
            event.setVIP(isVIP);
            return true;
        }
    }

    /**
     * Checks if a given event is VIP only
     * @param eventID id of the target event
     * @return true iff the event exists and is VIP only
     */
    public boolean isVIP(String eventID){
        Event event = findEvent(eventID);
        return event != null && event.isVIP();
    }

    /**
     * Gets all required features of a event
     * @param eventID id of the target event
     * @return An iterator of required features for the event
     */
    Iterator<String> getRequiredFeatures(String eventID){
        Event event = findEvent(eventID);
        return (event == null) ? null : event.getRequiredFeatures();
    }

    /**
     * Adds the given list of required features to the event's features
     * @param eventID id of the target event
     * @param requiredFeatures a list of new required features for a event
     * @return true iff the target event exists and not all new required features already exist in event
     */
    boolean setRequiredFeatures(String eventID, List<String> requiredFeatures){
        Event event = findEvent(eventID);
        return event != null && event.addRequiredFeatures(requiredFeatures);
    }

    /**
     * Removes the username from the vip events
     * @param vipEvents A collection of vip event ids
     * @param observer An instance of EventWithSpecObserver, represents the attendee of this event
     */
    public void updateVIPEvent(List<String> vipEvents, EventWithSpecObserver observer) {
        for (String eventId : vipEvents) {
            Event e = findEvent(eventId);
            if (e != null) {
                e.removeAttendee(observer);
            }
        }
    }

    /**
     * Gets all the valid event
     * @param accountIsVip True iff given account is vip
     * @param isVip True iff user what to access vip only event
     * @return A map that maps valid event id to its duration
     */
    public Map<String, SortedSet<Timestamp[]>> getAllEventTime(boolean accountIsVip, boolean isVip) {
        Timestamp currTime = new Timestamp(new Date().getTime());
        SortedMap<Timestamp, List<String[]>> validMap = eventSchedule.tailMap(currTime);
        Map<String, SortedSet<Timestamp[]>> o = new HashMap<>();
        String eventId;
        for (Timestamp key : validMap.keySet()) {
            for(String[] value : validMap.get(key)) { // value[1] event id
                eventId = value[1];
                if (!isValidEvent(eventId)) {
                    continue;
                }
                if (isVip) {
                    if (isVIP(eventId)) {
                        o.put(eventId, getEventDuration(eventId));
                    }
                }
                else if (accountIsVip || isVIP(eventId)) {
                    o.put(eventId, getEventDuration(eventId));
                }
            }
        }
        return o;
    }

    /**
     * Notifies the host observer when adding an event to its schedule.
     * @param eventId A String representation of the event id
     */
    protected void updateHostAdd(String eventId) {
        Event event = findEvent(eventId);
        if (event != null) {
            event.notifyHostAdd();
        }
    }
}

