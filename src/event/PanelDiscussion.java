package event;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * A panel discussion, which is a sub class of event, and it has multiple speakers
 * @author Group0694
 * @version 2.0.0
 */
class PanelDiscussion extends Event  {

    private final List<String> host = new ArrayList<>();

    /**
     * Constructs a PanelDiscussion object
     * @param name name of the PanelDiscussion
     * @param startTime The start time of this PanelDiscussion
     * @param endTime The end time of this PanelDiscussion
     * @param location room name of the PanelDiscussion held in
     * @param description description of the PanelDiscussion
     * @param capacity the max number of people can participate in the PanelDiscussion
     * @param id The unique ID of the PanelDiscussion
     */
    PanelDiscussion(String name, Timestamp startTime, Timestamp endTime,
                              String location, String description, int capacity, String id) {
        super(name, startTime, endTime, location, description, capacity, id);
    }

    /**
     * Sets the Speakers of this PanelDiscussion.
     * @param speakers new speakers of PanelDiscussion
     * @return true meaning that the speakers are modified
     */
    @Override
    protected boolean changeHost(List<String> speakers) {
        host.clear();
        host.addAll(speakers);
        return true;
    }

    /**
     * Gets all the speakers of this PanelDiscussion.
     * @return An iterator of hosts for this PanelDiscussion
     */
    @Override
    protected Iterator<String> getHosts() {
        return host.iterator();
    }

    /**
     * Gets the type of this PanelDiscussion which is just PanelDiscussion
     * @return "PanelDiscussion" which represents the type of the event
     */
    @Override
    protected String getType() {
        return "Panel Discussion";
    }

    /**
     * Gets the number of speakers required for this PanelDiscussion, which is multiple
     * @return -1, representing multiple speakers are allowed
     */
    @Override
    protected int getNumSpeaker() {
        return -1;
    }

    /**
     * Gives the toString information of this talk in a string.
     * The information contains all information from toString method in event class, also speaker for the talk.
     * @return the String of information of this Panel Discussion, contains name, start time, location, capacity, description and
     * speaker
     */
    @Override
    public String toString(){
        return super.toString() + "Host: " + hostToString();
    }

    private String hostToString(){
        StringBuilder builder = new StringBuilder();
        for (String speaker : host){
            builder.append(speaker).append(", ");
        }
        if (builder.length() != 0) builder.delete(builder.length() - 2, builder.length());
        return builder.toString();
    }
}
