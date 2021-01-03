package event;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.*;
import java.sql.Timestamp;

/**
 * An abstract entity class of events, stores name, ID, durationw, location, description, capacity,
 * and a list of attendees of the event. Implements Serializable.
 * Abstract because there are different kind of events.
 * Methods in this class are some getter and setter for attributes in this class.
 * @author Group0694
 * @version 2.0.0
 */
abstract class Event implements Serializable {
    private final String name;
    private final String ID;
    private final EventWithSpecObserver organizer;
    protected SortedSet<Timestamp[]> duration;
    private EventObserver location;
    private final String description;
    private int capacity;
    private final List<EventWithSpecObserver> attendee;
    private boolean isVIP = false;
    private final List<String> requiredFeatures;
    /**
     * Constructs a event object. Also notifies Room and Organizer of this event.
     * @param name name of the new event
     * @param organizer An instance of EventWithSpecObserver, represents the organizer who created this event
     * @param duration A sorted collection of time interval where start time is at index 0 and end time is index 1
     * @param location An instance of EventObserver that Room observes the Event
     * @param description description of the new event
     * @param capacity the max number of people can participate in the new event
     * @param id The unique ID of the event
     */
    protected Event(String name, EventWithSpecObserver organizer, SortedSet<Timestamp[]> duration,
                    EventObserver location, String description, int capacity, String id){
        this.name = name;
        this.ID = id;
        this.organizer = organizer;
        this.duration = duration;
        this.location = location;
        this.description = description;
        this.capacity = capacity;
        this.attendee = new ArrayList<>();
        this.requiredFeatures = new ArrayList<>();
        this.notifyRoomAdd();
        this.organizer.updateSpecAdd(this.ID);
    }


    /**
     * Gets name of this event.
     * @return name of event
     */
    protected String getName() {
        return name;
    }

    /**
     * Gets id of this event.
     * @return id of event
     */
    protected String getId() {
        return ID;
    }

    /**
     *  Gets event duration
     * @return An iterator of event duration
     */
    protected Iterator<Timestamp[]> getDuration() {
        return this.duration.iterator();
    }

    /**
     * Gets location of this event, which represents as the name of room holds this event
     * @return name of room which holds the event
     */
    protected String getLocation() {
        return location.getName();
    }

    /**
     * Checks if the user of given username is one of the attendees of this event.
     * @param observer An instance of EventWithSpecObserver, represents the attendee of this event
     * @return true if given username in attendee list, else false
     */
    protected boolean isInEvent(EventWithSpecObserver observer) {
        return attendee.contains(observer);
    }

    /**
     * Sets start time of this event. Gives a new start time to event. In addition, it also notifies all the observers.
     * @param duration A sorted collection of time interval where start time is at index 0 and end time is index 1
     */
    protected void setTime(SortedSet<Timestamp[]> duration) {
        this.notifyRoomRemove();
        this.notifyHostRemove();
        this.notifyAttendeesRemove();
        this.duration = new TreeSet<>(duration);
        this.notifyRoomAdd();
        this.notifyHostAdd();
        for(EventWithSpecObserver observer : this.attendee) {
            // Since we already remove all the attendee from old time without update its specialist for vip,
            // if attendee is not free and is vip and event is vip, we remove the specialist
            if(observer.freeAtThisTime(this.duration)) {
                observer.updateAdd(this.ID, this.duration);
            } else {
                if (observer.isVip() && isVIP) {
                    observer.updateSpecRemove(this.ID);
                }
            }
        }
    }

    /**
     * Adds a new attendee to the attendee list of this event.
     * @param attendeeObserver An instance of EventWithSpecObserver, represents the attendee of this event
     * @return true if added successfully, else false
     */
    protected boolean addAttendee(EventWithSpecObserver attendeeObserver) {
        if (attendee.size() >= capacity) {
            return false;
        }
        attendee.add(attendeeObserver);
        if (attendeeObserver.isVip() && this.isVIP) {
            attendeeObserver.updateAddWithSpec(this.ID, this.duration);
        } else {
            attendeeObserver.updateAdd(this.ID, this.duration);
        }
        return true;
    }

    /**
     * Removes an attendee from the attendee list of this event.
     * @param attendeeObserver An instance of EventWithSpecObserver, represents the attendee of this event
     * @return true if removed successfully, else false
     */
    protected boolean removeAttendee(EventWithSpecObserver attendeeObserver) {
        if (attendee.contains(attendeeObserver)) {
            attendee.remove(attendeeObserver);
            if (attendeeObserver.isVip() && this.isVIP) {
                attendeeObserver.updateRemoveWithSpec(this.ID, this.duration);
            } else {
                attendeeObserver.updateRemove(this.ID, this.duration);
            }
            return true;
        }
        return false;
    }

    /**
     * Prints event duration
     * @return A String representation of this event duration
     */
    protected String printEventDuration() {
        StringBuilder sb = new StringBuilder();
        for (Timestamp[] item : this.duration) {
            sb.append("\t");
            sb.append(getTime(item[0]));
            sb.append(" to ");
            sb.append(getTime(item[1]));
            sb.append("\n");
        }
        return sb.toString();
    }

    /**
     * Gives the toString information of this event in a string.
     * The information contains name, start time, location, capacity and description.
     * @return the String of information of the event, contains name, start time, location, capacity and description
     */
    public String toString(){
        return this.getType() + ": " + name + "\n" +
                ((isVIP) ? "VIP ONLY\n" : "") +
                "Time of event: \n" + // + getTime(startTime) + " to " + getTime(endTime) + "\n" +
                this.printEventDuration() +
                "Location of event: " + location.getName() + "\n" +
                "Capacity of event: " + capacity + "\n" +
                "Description of event :" + description + "\n";
    }

    /**
     * Sets the Speaker of this event.
     * This is an abstract method which will be working on changing the speaker of event.
     * @param speakers An instance of EventWithSpecObserver, represents the new speaker of event
     * @return true if changed speaker successfully, else false
     */
    protected abstract boolean changeHost(List<EventWithSpecObserver> speakers);

    /**
     * Gets the speaker of this event.
     * This is an abstract method which will show the hosts of the event.
     * @return An iterator of hosts for this event
     */
    protected abstract Iterator<String> getHosts();

    /**
     * Gets the attendees of this event in a list.
     * This is an abstract method which will show attendees of the event.
     * @return An iterator of the attendee list
    */
    protected Iterator<String> getAttendees() {
        List<String> lst = new ArrayList<>();
        for (EventObserver observer : attendee) {
            lst.add(observer.getName());
        }
        return lst.iterator();
    }

    /**
     * Checks of there are still space in the event
     * @return true if and only if the event is not full
     */
    protected boolean canSignup(){ return capacity > attendee.size(); }

    /**
     * Gets the capacity of the event
     * @return the capacity of the event
     */
    protected int getCapacity(){ return capacity; }

    /**
     * Modifies the capacity of the event
     * @param capacity the new capacity of the event
     */
    protected void setCapacity(int capacity){ this.capacity = capacity; }

    /**
     * Sets a new location for this event. In addition, it also notifies all the observers.
     * @param room The new location for a event, assumes it is valid
     */
    protected void setLocation(EventObserver room) {
        this.notifyRoomRemove();
        this.location = room;
        this.notifyRoomAdd();
    }

    /**
     * Checks if the event is VIP only
     * @return true iff the event is VIP only
     */
    protected boolean isVIP(){
        return isVIP;
    }

    /**
     * Updates the VIP status of this event
     * @param isVIP The new VIP status of this event
     */
    protected void setVIP(boolean isVIP){ this.isVIP = isVIP; }

    /**
     * Gets the type of this event
     * @return a string representation of the event type
     */
    abstract protected String getType();

    /**
     * Gets the number of speakers required for this event.
     * @return the number of speakers this event requires
     */
    abstract protected int getNumSpeaker();

    /**
     * Gets a string representation of the date and time this event happens
     * @return A string representing the data of the event
     */
    public String getTime(Timestamp time) {
        Date date = new Date(time.getTime());
        return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(date);
    }

    /**
     * Adds all the given features to the required features list
     * @param features a list of new required features for this event
     * @return true iff not all new required features already exist in this event
     */
    protected boolean addRequiredFeatures(List<String> features){ return this.requiredFeatures.addAll(features); }
    /**
     * Gets all required features of this event
     * @return An iterator of requiredFeature list
     */
    protected Iterator<String> getRequiredFeatures(){
        return requiredFeatures.iterator();
    }

    /**
     * Gets the first start time of event duration
     * @return First start time of event duration, null if duration length == 0
     */
    protected Timestamp getFirstTime() {
        try {
            return this.duration.first()[0];
        } catch (IndexOutOfBoundsException ie) {
            return null;
        }
    }

    /**
     * Notifies all the attendee observers to remove and update an event
     */
    protected void notifyAttendeesRemove() {
        for (EventWithSpecObserver observer : attendee) {
            observer.updateRemove(this.ID, this.duration);
        }
    }

    /**
     * Notifies the room observer to add and update an event
     */
    protected void notifyRoomAdd() {
        location.updateAdd(this.ID, this.duration);
    }

    /**
     * Notifies the room observer to remove and update an event
     */
    protected void notifyRoomRemove() {
        location.updateRemove(this.ID, this.duration);
    }

    /**
     * Notifies the host observer to add and update an event
     */
    abstract protected void notifyHostAdd();

    /**
     * Notifies the host observer to remove and update an event
     */
    abstract protected void notifyHostRemove();
}
