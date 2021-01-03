package account;
import java.util.*;

/**
 * An entity class representing an VIP account, which is a child class of abstract class Account.
 * It stores a list of VIP only events that the account attended
 * @author Group0694
 * @version 2.0.0
 */

public class VIPAttendee extends Account {
    private final Set<String> vipEvents;

    /**
     * Creates an account with the specified username and password.
     *
     * @param name The username of this account.
     * @param password The password to log in this account.
     */
    public VIPAttendee(String name, String password) {
        super(name, password);
        vipEvents = new HashSet<>();
    }

    /**
     * Checks the type of account
     * @return The type's string representation "VIP"
     */
    @Override
    protected String getType() {
        return "VIP";
    }

    /**
     * Gets the VIP events the user attended
     * @return A Iterator of the list of VIP event IDs for this user
     */
    @Override
    protected Iterator<String> getSpecialList() {
        return vipEvents.iterator();
    }

    /** Adds new VIP event to the list of events attended.
     * @param id A String represents the unique ID of this event.
     * @return true iff the list does not already contain the ID
     */
    @Override
    protected boolean addToSpecialList(String id) {
        if (vipEvents.contains(id)) {
            return false;
        }
        vipEvents.add(id);
        return true;
    }

    /** Removes an event from the list of events attended.
     * @param id A String represents the unique ID of this event.
     * @return true iff the list does contain the ID and it is removed
     */
    @Override
    protected boolean removeFromSpecialList(String id) {
        if (vipEvents.contains(id)) {
            vipEvents.remove(id);
            return true;
        }
        return false;
    }

    /** Displays a description for this VIP account about the number of VIP events attended.
     * @return A string representing the number of VIP events this VIP account attended
     */
    public String toString(){
        return super.toString() + "\nYou have attended " + vipEvents.size() + " VIP only events.";
    }

    /**
     * Checks if this account has VIP access
     * @return true only, because VIP attendees are always VIP
     */
    @Override
    protected boolean isVIP() {
        return true;
    }

    /**
     * Checks if the given event id is in this account's specialist
     * @param eventId A String representation of event id
     * @return True iff event id is in this account's specialist
     */
    @Override
    protected boolean isInSpecialist(String eventId) {
        return vipEvents.contains(eventId);
    }
}
