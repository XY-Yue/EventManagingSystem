package event;

import java.sql.Timestamp;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * A party, which is a sub class of event, and it has no speaker
 * @author Group0694
 * @version 2.0.0
 */
class Party extends Event  {

    /**
     * Constructs a Party object
     * @param name name of the Party
     * @param startTime The start time of this Party
     * @param endTime The end time of this Party
     * @param location room name of the Party held in
     * @param description description of the Party
     * @param capacity the max number of people can participate in the Party
     * @param id The unique ID of the Party
     */
    Party(String name, Timestamp startTime, Timestamp endTime, String location,
                    String description, int capacity, String id) {
        super(name, startTime, endTime, location, description, capacity, id);
    }

    /**
     * Changes the host of the event, which is not allowed for party
     * @param speakers new speaker of event
     * @return false, as Party has no speakers
     */
    @Override
    protected boolean changeHost(List<String> speakers) {
        return false;
    }

    /**
     * Gets the speakers of this event, none in this case
     * @return an empty Iterator, as Party has no speakers
     */
    @Override
    protected Iterator<String> getHosts() {
        return Collections.emptyIterator();
    }

    /**
     * Gets the type of this Party which is just Party
     * @return "Party" which represents the type of the event
     */
    @Override
    protected String getType() {
        return "Party";
    }

    /**
     * Gets the number of speakers required for this Party, which is None
     * @return 0, representing no speaker is needed
     */
    @Override
    protected int getNumSpeaker() {
        return 0;
    }

    /**
     * Gives the toString information of this talk in a string.
     * The information contains all information from toString method in event class, add a Have Fun message
     * @return the String of information of this talk, contains name, start time, location, capacity and description
     */
    @Override
    public String toString(){
        return super.toString() + "Have fun!";
    }
}
