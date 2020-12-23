package event;

import java.io.Serializable;
import java.text.DateFormat;
import java.util.*;
import java.sql.Timestamp;

/**
 * An abstract entity class of events, stores name, ID, start time, end time, location, description, capacity,
 * and a list of attendees of the event.
 * Abstract because there are different kind of events.
 * Methods in this class are some getter and setter for attributes in this class.
 * @author Group0694
 * @version 2.0.0
 */
abstract class Event implements Serializable {
    private final String name;
    private final String ID;
    private Timestamp startTime;
    private Timestamp endTime;
    private String location;
    private final String description;
    private int capacity;
    private final List<String> attendee;
    private boolean isVIP = false;
    private final List<String> requiredFeatures;

    /**
     * Constructs a event object
     * @param name name of the new event
     * @param startTime The start time of this event
     * @param endTime The end time of this event
     * @param location room name of the new event held in
     * @param description description of the new event
     * @param capacity the max number of people can participate in the new event
     * @param id The unique ID of the event
     */
    protected Event(String name, Timestamp startTime, Timestamp endTime, String location,
                    String description, int capacity, String id){
        this.name = name;
        this.ID = id;
        this.startTime = startTime;
        this.endTime = endTime;
        this.location = location;
        this.description = description;
        this.capacity = capacity;
        this.attendee = new ArrayList<>();
        this.requiredFeatures = new ArrayList<>();
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
     * Gets start time of this event.
     * @return start time of event
     */
    protected Timestamp getStartTime() {
        return startTime;
    }

    /**
     * Gets end time of this event.
     * @return end time of event
     */
    protected Timestamp getEndTime() {
        return endTime;
    }

    /**
     * Gets location of this event, which represents as the name of room holds this event
     * @return name of room which holds the event
     */
    protected String getLocation() {
        return location;
    }

    /**
     * Checks if the user of given username is one of the attendees of this event.
     * @param username the username of user we want to check
     * @return true if given username in attendee list, else false
     */
    protected boolean isInEvent(String username) {
        return attendee.contains(username);
    }

    /**
     * Sets start time of this event. Gives a new start time to event.
     * @param newStart new start time we want to set for event
     * @param endTime the new end time of this event
     */
    protected void setTime(Timestamp newStart, Timestamp endTime) {
        startTime = newStart;
        this.endTime = endTime;
    }

    /**
     * Adds a new attendee to the attendee list of this event.
     * @param attendeeName the username of user we want to add into attendee list
     * @return true if added successfully, else false
     */
    protected boolean addAttendee(String attendeeName) {
        if (attendee.size() > capacity) return false;
        return attendee.add(attendeeName);
    }

    /**
     * Removes an attendee from the attendee list of this event.
     * @param attendeeName the username of user we want to remove from attendee list
     * @return true if removed successfully, else false
     */
    protected boolean removeAttendee(String attendeeName) {
        return attendee.remove(attendeeName);
    }

    /**
     * Gives the toString information of this event in a string.
     * The information contains name, start time, location, capacity and description.
     * @return the String of information of the event, contains name, start time, location, capacity and description
     */
    public String toString(){
        return this.getType() + ": " + name + "\n" +
                ((isVIP) ? "VIP ONLY\n" : "") +
                "Time of event: " + getTime(startTime) + " to " + getTime(endTime) + "\n" +
                "Location of event: " + location + "\n" +
                "Capacity of event: " + capacity + "\n" +
                "Description of event :" + description + "\n";
    }

    /**
     * Sets the Speaker of this event.
     * This is an abstract method which will be working on changing the speaker of event.
     * @param speakers new speaker of event
     * @return true if changed speaker successfully, else false
     */
    protected abstract boolean changeHost(List<String> speakers);

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
    protected Iterator<String> getAttendees(){
        return attendee.iterator();
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
     * Sets a new location for this event
     * @param roomName The new location for a event, assumes it is valid
     */
    protected void setLocation(String roomName){ this.location = roomName;}

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

}
