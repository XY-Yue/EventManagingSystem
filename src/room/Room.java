package room;

import event.EventObserver;
import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.*;

/**
 * An entity class of Room. Implements Serializable, Available, EventObserver.
 * Stores capacity, available time, unique room name, schedule of a room, and a collection of features
 * Schedule is a map with start time map to event name.
 * availableTime is a map that maps the start hour to end hour of the room open time, 0 <= start time <= 23 and
 * 1 <= end time <= 24, and no overlap.
 * @author Group0694
 * @version 2.0.0
 */
@SuppressWarnings("FieldMayBeFinal")
class Room implements Serializable, Available, EventObserver {
    private final Integer capacity;
    private NavigableMap<Integer, Integer> availableTime;
    private final String roomName; //Check for uniqueness
    // No getters for this room name as it is stored directly in the use case as keys in the Map
    private NavigableMap<Timestamp[], String> schedule;

    private final List<String> features;

    /**
     * Constructs a Room object
     * @param capacity Maximum capacity of this room
     * @param availableTimeSlots Time slots that this room is available
     * @param roomName An unique String representation of the room name
     * @param features A List of additional features that this room can provide
     */
    protected Room(int capacity, Integer[][] availableTimeSlots, String roomName, List<String> features) {
        // Assume availableTimeSlops do not overlap
        // A list of [[start time 1, end time 1], [start time 2, end time 2]]
        this.capacity = capacity;
        this.availableTime = new TreeMap<>();

        for (Integer[] lst: availableTimeSlots) {
            availableTime.put(lst[0], lst[1]);
        }

        this.features = new ArrayList<>(features);
        this.roomName = roomName;
        schedule = new TreeMap<> ((Comparator<Timestamp[]> & Serializable)
                (Timestamp[] t1, Timestamp[]t2) -> {
                    // t1 == [startTime1, endTime1]
                    // t2 == [startTime2, endTime2]
                    // They won't overlap
                    if (!t1[1].after(t2[0])) {
                        return -1;
                    }
                    if (!t2[1].after(t1[0])) {
                        return 1;
                    }
                    return 0;
                }
        );
    }

    /**
     * Gets the capacity of room.
     * @return capacity of room
     */
    protected int getCapacity() {
        return capacity;
    }

    /**
     * Checks if the room is available during the given interval
     * @param startTime Start time of the interval
     * @param endTime End time of the interval
     * @return true iff the room is available during this interval
     */
    protected boolean isAvailable(Timestamp startTime, Timestamp endTime) {
        return this.isAvailable(this.schedule, startTime, endTime);
    }

    /**
     * Checks the room is open during the given interval hours
     * @param startHour1 Start Hour of the given interval
     * @param endHour2 End Hour of the given interval
     * @return true iff this room is open during this interval
     */
    protected boolean isValidTimeSlots(int startHour1, int endHour2) {
        // Case if startHour 1 == endHour2
        if (startHour1 == endHour2) {
            if (availableTime.get(startHour1) != null) {
                return true;
            }
            Map.Entry<Integer, Integer> timeslot1 = availableTime.lowerEntry(startHour1);
            return timeslot1.getValue() >= startHour1;
        } else if (startHour1 < endHour2) {
            // Case if start hour < end hour
            Integer endTime = availableTime.get(startHour1);
            if (endTime != null && endTime >= endHour2) {
                return true;
            } else {
                Map.Entry<Integer, Integer> timeslot1 = availableTime.lowerEntry(startHour1);
                Map.Entry<Integer, Integer> timeslot2 = availableTime.lowerEntry(endHour2);
                if (timeslot1 != null && timeslot2 != null) {
                    if (timeslot1.equals(timeslot2)) {
                        return true;
                    }
                    return timeslot1.getValue().equals(timeslot2.getKey());
                }
            }
        } else {
            // Case iff end hour < start hour
            return isValidTimeSlots(0, startHour1) && isValidTimeSlots(endHour2, 24);
        }
        return false;
    }

    /**
     * Adds the event to the schedule
     * @param timeDuration A sorted collection of time interval where start time is at index 0 and end time is index 1
     * @param eventID A String representation of the event id
     * @return true iff event is added successfully
     */
    protected boolean addEventToSchedule(SortedSet<Timestamp[]> timeDuration, String eventID) {
        // return schedule.putIfAbsent(new Timestamp[]{startTime, endTime}, eventID) == null
        for (Timestamp[] t : timeDuration) {
            this.schedule.put(t, eventID);
        }
        return true; // Since we already check for available room
    }

    /**
     * Removes the event from the schedule
     * @param timeDuration A sorted collection of time interval where start time is at index 0 and end time is index 1
     * @param eventName A String representation of the event id
     * @return true iff the event is removed successfully
     */
    protected boolean removeEventFromSchedule(SortedSet<Timestamp[]> timeDuration, String eventName) {
        for (Timestamp[] t : timeDuration) {
            schedule.remove(t, eventName);
        }
        return true; // Since we already check it
    }

    // This is a helper method for toString
    private String printSchedule(){
        StringBuilder sb = new StringBuilder();
        for(Timestamp[] time: schedule.keySet()){
            sb.append("\n\t");
            sb.append("Event ");
            sb.append(schedule.get(time));
            sb.append(" is hold in this room from ");
            sb.append(this.getTime(time[0]));
            sb.append(" to ");
            sb.append(this.getTime(time[1]));
            sb.append(".");
        }
        return sb.toString();
    }

    private String getTime(Timestamp time) {
        Date date = new Date(time.getTime());
        return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(date);
    }

    /**
     * Generates a String representation of the room available time slots in ascending order.
     * @return a String representation of the room available time slots.
     */
    protected String printAvailableTime() {
        List<String> lst = new ArrayList<>();
        for(Integer startTime: availableTime.keySet()) {
            lst.add(startTime.toString() + "-" + availableTime.get(startTime).toString());
        }
        return String.join(", ", lst);
    }

    /**
     * Represents the string contains all information of room.
     * @return toString description of room
     */
    @Override
    public String toString(){
        StringBuilder featuresString = new StringBuilder();

        for (String feature : features) featuresString.append(feature).append("; ");
        if (featuresString.length() != 0) featuresString.replace(
                featuresString.length() - 2, featuresString.length(), ".");

        return "The name of this room is: " + roomName + "\n" +
                "The capacity of this room is: " + capacity + "\n" +
                "The room is available during: " + printAvailableTime() + "\n" +
                "The schedule of this room is: " + this.printSchedule() + "\n" +
                "This room has:" + ((features.size() == 0) ? "No features" : featuresString.toString());
    }

    /**
     * Checks if the room has the list of additional features
     * @param checkedFeatures A list of String representation of the additional features
     * @return true iff the room has all the features given by
     */
    protected boolean hasFeatures(List<String> checkedFeatures){
        return features.containsAll(checkedFeatures);
    }

    /**
     * Updates an event by adding to its schedule.
     * @param eventId A String representation of the event id
     * @param timeDuration A time duration of an event
     */
    @Override
    public void updateAdd(String eventId, SortedSet<Timestamp[]> timeDuration) {
        for (Timestamp[] t : timeDuration) {
            this.schedule.put(t, eventId);
        }
    }

    /**
     * Updates an event by adding to its schedule.
     * @param eventId A String representation of the event id
     * @param timeDuration A time duration of an event
     */
    @Override
    public void updateRemove(String eventId, SortedSet<Timestamp[]> timeDuration) {
        for (Timestamp[] t : timeDuration) {
            schedule.remove(t, eventId);
        }
    }

    /**
     * Gets A String representation of current instance.
     * @return A String representation of the current room name.
     */
    @Override
    public String getName() {
        return this.roomName;
    }
}
