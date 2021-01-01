package event;
import java.util.SortedSet;
import java.sql.Timestamp;

/**
 * An interface provides the basic access to notify the observers and update based on previous action.
 * @author Group0694
 * @version 3.0.0
 */
public interface EventObserver {

    /**
     * Updates by adding an event to its schedule
     * @param eventId A String representation of the event id
     * @param timeDuration A time duration of an event
     */
    void updateAdd(String eventId, SortedSet<Timestamp[]> timeDuration);

    /**
     * Updates by removing an event from its schedule
     * @param eventId A String representation of the event id
     * @param timeDuration A time duration of an event
     */
    void updateRemove(String eventId, SortedSet<Timestamp[]> timeDuration);

    /**
     * Gets the String representation of the current observer
     * @return A String representation of the current observer
     */
    String getName();
}
