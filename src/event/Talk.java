package event;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An entity class which extends abstract class event, representing a Event with only one speaker
 * Inherits all existing attributes of event class and with new attribute speaker.
 * @author Group0694
 * @version 2.0.0
 */
class Talk extends Event {
    private String speaker;

    /**
     * Constructs a Talk object
     * @param name name of the Talk
     * @param startTime The start time of this Talk
     * @param endTime The end time of this Talk
     * @param location room name of the Talk held in
     * @param description description of the Talk
     * @param capacity the max number of people can participate in the Talk
     * @param id The unique ID of the Talk
     */
    Talk(String name, Timestamp startTime, Timestamp endTime, String location,
                   String description, int capacity, String id) {
        super(name, startTime, endTime, location, description, capacity, id);
    }

    /**
     * Changes the speaker of this talk by given new speaker.
     * @param speakers new speaker of this talk
     * @return true if there is exactly one speaker given, and changed speaker successfully, else false
     */
    @Override
    public boolean changeHost(List<String> speakers) {
        if (speakers == null || speakers.size() == 0) return false;
        this.speaker = speakers.get(0);
        return true;
    }

    /**
     * Shows the speaker of this talk.
     * @return An iterator that only contains the one speaker for this talk
     */
    @Override
    protected Iterator<String> getHosts() {
        List<String> speaker = new ArrayList<>();
        speaker.add(this.speaker);
        return speaker.iterator();
    }

    /**
     * Gets the type of this Talk which is just Talk
     * @return "Talk" which represents the type of the event
     */
    @Override
    protected String getType() {
        return "Talk";
    }

    /**
     * Gets the number of speakers required for this PanelDiscussion, which is 1
     * @return 1, representing exactly one speaker is allowed
     */
    @Override
    protected int getNumSpeaker() {
        return 1;
    }

    /**
     * Gives the toString information of this talk in a string.
     * The information contains all information from toString method in event class, also speaker for the talk.
     * @return the String of information of this talk, contains name, start time, location, capacity, description and
     * speaker
     */
    @Override
    public String toString(){
        return super.toString() + "Host: " + speaker;
    }
}
