package event;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

/**
 * A panel discussion, which is a sub class of event, and it has multiple speakers
 * @author Group0694
 * @version 2.0.0
 */
class PanelDiscussion extends Event  {

    private final List<EventWithSpecObserver> host = new ArrayList<>();

    /**
     * Constructs a PanelDiscussion object
     * @param name name of the PanelDiscussion
     * @param organizer An instance of EventWithSpecObserver, represents the organizer of this event
     * @param duration A sorted collection of time interval where start time is at index 0 and end time is index 1
     * @param location An instance of EventObserver, represents the room name of the PanelDiscussion held in
     * @param description description of the PanelDiscussion
     * @param capacity the max number of people can participate in the PanelDiscussion
     * @param id The unique ID of the PanelDiscussion
     */
    PanelDiscussion(String name, EventWithSpecObserver organizer, SortedSet<Timestamp[]> duration,
                    EventObserver location, String description, int capacity, String id) {
        super(name, organizer, duration, location, description, capacity, id);
    }

    /**
     * Sets the Speakers of this PanelDiscussion. In addition, it also notifies the observers.
     * @param speakers A collection of EventWithSpecObserver instances, represents the new speakers of PanelDiscussion
     * @return True iff the speakers are modified
     */
    @Override
    protected boolean changeHost(List<EventWithSpecObserver> speakers) {
        this.notifyHostRemove();
        host.clear();
        host.addAll(speakers);
        this.notifyHostAdd();
        return true;
    }

    private List<String> getHostNames() {
        List<String> o = new ArrayList<>();
        for (EventObserver item : host) {
            o.add(item.getName());
        }
        return o;
    }

    /**
     * Gets all the speakers of this PanelDiscussion.
     * @return An iterator of hosts for this PanelDiscussion
     */
    @Override
    protected Iterator<String> getHosts() {
        return this.getHostNames().iterator();
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
        for (String speaker : this.getHostNames()){
            builder.append(speaker).append(", ");
        }
        if (builder.length() != 0) builder.delete(builder.length() - 2, builder.length());
        return builder.toString();
    }

    /**
     * Notifies hosts by adding current event to their schedule.
     */
    @Override
    protected void notifyHostAdd() {
        for(EventWithSpecObserver observer : host) {
            observer.updateAddWithSpec(this.getId(), this.duration);
        }
    }

    /**
     * Notifies hosts by removing current event from their schedule.
     */
    @Override
    protected void notifyHostRemove() {
        for(EventWithSpecObserver observer : host) {
            observer.updateRemoveWithSpec(this.getId(), this.duration);
        }
    }

}
