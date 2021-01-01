package account;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * An entity class represents an organizer account, which is a child class of abstract class Account.
 * It stores a list of events that the organizer organized in addition, no matter the event is removed or not.
 * @author Group0694
 * @version 2.0.0
 */

public class Organizer extends Account {
    private final List<String> organizedEvents;
    private boolean isVIP = true;

    /** Creates an organizer account with specific username and his/her account password.
     * @param name A string represents the username of this organizer account.
     * @param password A string represents the password of this organizer account.
     */
    public Organizer(String name, String password) {
        super(name, password);
        organizedEvents = new ArrayList<>();
    }

    /** Gets a string represents the type of this organizer account.
     * @return A string that shows the type of this account is organizer.
     */
    @Override
    protected String getType() {
        return "Organizer";
    }

    /** Gets the Iterator of the list of events that the organizer organized.
     * @return A Iterator of the list of strings which represents the unique id of each event that the
     * organizer organized.
     */
    @Override
    protected Iterator<String> getSpecialList() {
        return organizedEvents.iterator();
    }

    /** Adds a new event to the list of events that the organizer organized.
     * @param id A String represents the unique ID of this event.
     * @return true iff the list does not already contain the ID
     */
    @Override
    protected boolean addToSpecialList(String id) {
        if (organizedEvents.contains(id)) {
            return false;
        }
        organizedEvents.add(id);
        return true;
    }

    /** Removes an event from the list of events that the organizer organized.
     * @param id A String represents the unique ID of this event.
     * @return true iff the list does contain the ID and it is removed
     */
    @Override
    protected boolean removeFromSpecialList(String id) {
        if (organizedEvents.contains(id)) {
            organizedEvents.remove(id);
            return true;
        }
        return false;
    }

    /** Displays a description for this organizer account about the number of events he/she is organizing.
     * @return A string representing the number of events organized by this organizer account in addition.
     */
    @Override
    public String toString(){
        return super.toString() + "\nYou have organized " + organizedEvents.size() + " events.";
    }

    /**
     * Checks if this account has VIP access
     * @return The value of isVIP which represents if this account has VIP
     */
    @Override
    protected boolean isVIP() {
        return isVIP;
    }

    /**
     * Checks if the given id is in this account's specialist.
     * @param eventId A String representation of event id.
     * @return True iff event id is in this account's specialist.
     */
    @Override
    protected boolean isInSpecialist(String eventId) {
        return organizedEvents.contains(eventId);
    }
}
