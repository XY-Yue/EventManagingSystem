package room;


import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * An use case class of room.
 * Stores all room in a map which has room name map to the corresponding Room object.
 * Contains constructor of room, which is able to construct a new room.
 * Methods in this class contains check room status, operating on specific room, and get room with given information.
 * All the parameter should be given by controller of room.
 * @author Group0694
 * @version 2.0.0
 */
@SuppressWarnings("deprecation")
public class RoomManager implements Serializable {
    private final Map<String, Room> roomList;
    private final List<String> roomFeatureList;

    /**
     * Constructs the RoomManager object
     */
    public RoomManager(){
        roomList = new HashMap<>();
        roomFeatureList =
                new ArrayList<>(Arrays.asList("Projector", "Row of chairs", "Table", "Computers"));
    }

    /**
     * Gives the toString description of room with given name.
     * @param roomName the name of room ask for toString description
     * @return the toString description of room
     */
    String printRoom(String roomName) {
        return (roomList.get(roomName) == null) ? null : roomList.get(roomName).toString();
    }

    /**
     * Finds a list of room that satisfies all the given conditions
     * @param start Required start time of the interval
     * @param end Required end time of the interval
     * @param features Required a list of String representation of additional features
     * @param capacity Required capacity
     * @return A list of room name that satisfies all of the given conditions
     */
    public List<String> availableRooms(Timestamp start, Timestamp end, List<String> features, int capacity){
        List<String> result = new ArrayList<>();
        for (String room : roomList.keySet()){
            if (roomList.get(room) != null && roomList.get(room).hasFeatures(features)) {
                if (this.checkRoomAvailability(start, end, room) && checkRoomTimeSlots(start, end, room))
                    if (roomList.get(room).getCapacity() >= capacity) {
                        result.add(room);
                    }
            }
        }
        return result;
    }

    /**
     * Adds a room
     * @param capacity Maximum capacity of the given room
     * @param availableTime Available time slots of the room
     * @param roomName A String representation of the room name
     * @param features A list of String representation of additional features
     * @return true iff the room is added successfully
     */
    boolean addRoom(int capacity, Integer[][] availableTime, String roomName, List<String> features) {
        // Return true if and only roomName is unique
        // Assume availableTime do not overlap, should check when user input
        if (roomList.get(roomName) == null) {
            Room room = new Room(capacity, availableTime, roomName, features);
            roomList.put(roomName, room);
            return true;
        }
        return false;
    }

    /**
     * Checks the given room name exists or not
     * @param roomName A String representation of the room name
     * @return true iff the room exists
     */
    boolean hasRoom(String roomName) {
        // Return true iff roomName is in the list
        // For controller to use this method check and then generates different information to the presenter
        // Anytime when asking user to enter room name, this method should be called
        // All other method that needs roomName should assume valid roomName
        return (roomList.get(roomName) != null);
    }

    /**
     * Checks the room is available during the given time interval
     * @param startTime Start time of the given interval
     * @param endTime End time of the given interval
     * @param roomName A String representation of the room name
     * @return true iff the room is available during this interval
     */
    public boolean checkRoomAvailability(Timestamp startTime, Timestamp endTime, String roomName){
        // Assume roomName is in the list
        return roomList.get(roomName) != null && (roomList.get(roomName).isAvailable(startTime, endTime));
    }

    private boolean checkRoomTimeSlots(Timestamp startTime, Timestamp endTime, String roomName) {
        Room room = roomList.get(roomName);
        if (room == null) return false;
        Timestamp copy = new Timestamp(startTime.getTime());
        copy.setDate(startTime.getDate() + 1);
        if (!endTime.before(copy)) {
            return room.isValidTimeSlots(0, 24);
        }
        int minutes1 = startTime.getMinutes();
        int hour1 = (minutes1 == 0) ? startTime.getHours() : startTime.getHours() - 1;
        int minutes2 = endTime.getMinutes();
        int hour2 = (minutes2 == 0) ? endTime.getHours() : endTime.getHours() + 1;
        return room.isValidTimeSlots(hour1, hour2);
    }

    /**
     * Adds an event to a room
     * @param roomName A String representation of the room name
     * @param eventID A String representation of the event id
     * @param time Start time of this event
     * @param endTime End time of this event
     * @return true iff the event is added successfully
     */
    public boolean addEventToRoom(String roomName, String eventID, Timestamp time, Timestamp endTime) {
        // Return boolean not String so controller knows what needs to send to presenter
        return roomList.get(roomName) != null && roomList.get(roomName).addEventToSchedule(time, endTime, eventID);
    }

    /**
     * Removes an event from a room
     * @param roomName A String representation of the room name
     * @param eventName A String representation of the event id
     * @param time Start time of this event
     * @param endTime End time of this event
     * @return true iff the event is removed successfully
     */
    public boolean removeEventFromRoom(String roomName, String eventName, Timestamp time, Timestamp endTime) {
        return roomList.get(roomName) != null && roomList.get(roomName).
                removeEventFromSchedule(time, endTime, eventName);
    }

    /**
     * Gets the room capacity
     * @param roomName A String representation of given room
     * @return Maximum capacity of the given room, -1 if the room does not exist
     */
    public int getRoomCapacity(String roomName) {
        if (roomList.get(roomName) == null) return -1;
        return roomList.get(roomName).getCapacity();
    }

    /**
     * Generates a String representation of the room info
     * @return room info for all rooms
     */
    List<String> getRoomsInfo() {
        List<String> roomInfo = new ArrayList<>();
        for(Room r : roomList.values()) {
            roomInfo.add(r.toString());
        }
        return roomInfo;
    }

    /**
     * Gets a list of String representation of the additional features
     * @return An iterator of additional features
     */
    public Iterator<String> getAllFeatures() {
        return roomFeatureList.iterator();
    }

    /**
     * Checks if the feature is in the feature list
     * @param feature A String representation of the additional feature
     * @return true iff the additional feature is in the list
     */
    boolean hasFeature(String feature){
        return roomFeatureList.contains(feature);
    }

    /**
     * Adds a new feature to the feature list
     * @param feature A String representation of the additional feature
     * @return true iff added successfully
     */
    boolean addFeature(String feature){
        return roomFeatureList.add(feature);
    }
}
