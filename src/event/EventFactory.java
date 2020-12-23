package event;

import java.sql.Timestamp;

/**
 * An Factory class that creates Event Entities
 * Creates a event object of given type with given information
 * @author Group0694
 * @version 2.0.0
 */
public class EventFactory {
    /**
     * Constructs the Event Entity
     * @param type The type of the event, used for determining which constructor to call
     * @param name name of the new event
     * @param startTime The start time of this event
     * @param endTime The end time of this event
     * @param location room name of the new event held in
     * @param description description of the new event
     * @param capacity the max number of people can participate in the new event
     * @param id The unique ID of this event
     * @return the event object created based on user input
     */
    Event makeEvent(String type, String name, Timestamp startTime, Timestamp endTime,
                              String location, String description, int capacity, String id){
        switch (type.toLowerCase()){
            case "talk":
                return new Talk(name, startTime, endTime, location, description, capacity, id);
            case "party":
                return new Party(name, startTime, endTime, location, description, capacity, id);
            case "panel discussion":
                return new PanelDiscussion(name, startTime, endTime, location, description, capacity, id);
            default:
                return null;
        }
    }
}
