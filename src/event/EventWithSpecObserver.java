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
}
