package event;

import java.sql.Timestamp;
import java.util.SortedSet;

/**
 * An interface that extends the EventObserver, it provides additional access that also updates account's specialist.
 * @author Group0694
 * @version 3.0.0
 */
public interface EventWithSpecObserver extends EventObserver {
    /**
     * Updates by adding an event to its schedule. In addition, it also updates the special list.
     * @param eventId A String representation of the event id.
     * @param timeDuration A time duration of an event.
     */
    void updateAddWithSpec(String eventId, SortedSet<Timestamp[]> timeDuration);

    /**
     * Updates by removing an event from its schedule. In addition, it also updates the special list.
     * @param eventId A String representation of the event id.
     * @param timeDuration A time duration of an event.
     */
    void updateRemoveWithSpec(String eventId, SortedSet<Timestamp[]> timeDuration);

    /**
     * Checks if this observer is free during this duration
     * @param timeDuration A collection of Timestamp[] where index 0 represents the start time and index 1 represents
     *                     the end time.
     * @return true iff this observer is free during this duration
     */
    boolean freeAtThisTime(SortedSet<Timestamp[]> timeDuration);

    /**
     * Updates and adds an event id to this observer's specialist
     * @param eventId A String representation of this event id
     */
    void updateSpecAdd(String eventId);

    /**
     * Checks if this observer is VIP attendee
     * @return true iff this observer is VIP attendee
     */
    boolean isVip();

    /**
     * Updates and removes an event id from this observer's specialist
     * @param eventId A String representation of this event id
     */
    void updateSpecRemove(String eventId);
}
