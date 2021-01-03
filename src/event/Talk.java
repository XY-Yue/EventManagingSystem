package event;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.SortedSet;

/**
 * An entity class which extends abstract class event, representing a Event with only one speaker
 * Inherits all existing attributes of event class and with new attribute speaker.
 * @author Group0694
 * @version 2.0.0
 */
class Talk extends Event {
    private EventWithSpecObserver speaker;

    /**
     * Constructs a Talk object
     * @param name name of the Talk
     * @param organizer An instance of EventWithSpecObserver, represents the organizer of this event
     * @param duration A sorted collection of time interval where start time is at index 0 and end time is index 1
     * @param location An instance of EventObserver, represents the room name of the Talk held in
     * @param description description of the Talk
     * @param capacity the max number of people can participate in the Talk
     * @param id The unique ID of the Talk
     */
    Talk(String name, EventWithSpecObserver organizer, SortedSet<Timestamp[]> duration , EventObserver location,
         String description, int capacity, String id) {
        super(name, organizer, duration, location, description, capacity, id);
    }

    /**
     * Changes the speaker of this talk by given new speaker.
     * @param speakers A collection of EventWithSpecObserver, represents the new speaker of this talk
     * @return true if there is exactly one speaker given, and changed speaker successfully, else false
     */
    @Override
    public boolean changeHost(List<EventWithSpecObserver> speakers) {
        if (speakers == null || speakers.size() != 1) return false;
        this.notifyHostRemove();
        this.speaker = speakers.get(0);
        this.notifyHostAdd();
        return true;
    }

    /**
     * Shows the speaker of this talk.
     * @return An iterator that only contains the one speaker for this talk
     */
    @Override
    protected Iterator<String> getHosts() {
        List<String> speaker = new ArrayList<>();
        speaker.add(this.speaker.getName());
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
        return super.toString() + "Host: " + speaker.getName();
    }

    /**
     * Notifies the host by adding current event to its schedule.
     */
    @Override
    protected void notifyHostAdd() {
        this.speaker.updateAddWithSpec(getId(), this.duration);
    }

    /**
     * Notifies the host by removing current event from its schedule.
     */
    @Override
    protected void notifyHostRemove() {
        if (this.speaker != null) {
            this.speaker.updateRemoveWithSpec(getId(), this.duration);
        }
    }
}
