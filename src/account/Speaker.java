package account;

import java.util.*;

/**
 * An entity class represents an speaker account, which is a child class of abstract class Account.
 * It stores a list of events that the speaker given in addition.
 * @author Group0694
 * @version 2.0.0
 */

public class Speaker extends Account {
    private final Set<String> hostingEvents;
    private boolean isVIP = true;

    /** Creates a speaker account with specific username and his/her account password.
     * @param name A string represents the username of this speaker account.
     * @param password A string represents the password of this speaker account.
     */
    public Speaker(String name, String password) {
        super(name, password);
        hostingEvents = new HashSet<>();
    }

    /** Gets the type of this speaker account.
     * @return A string that shows the type of this account is speaker.
     */
    @Override
    protected String getType() {
        return "Speaker";
    }

    /** Gets the Iterator of the list of events that the speaker given.
     * @return A Iterator of the list of Strings which represents the unique IDs of events that the speaker given.
     */
    @Override
    protected Iterator<String> getSpecialList() {
        return hostingEvents.iterator();
    }

    /** Adds new event to the list of events that the speaker given.
     * @param id A String represents the unique ID of this event.
     * @return true iff the list does not already contain the ID
     */
    @Override
    protected boolean addToSpecialList(String id) {
        if (hostingEvents.contains(id)) {
            return false;
        }
        hostingEvents.add(id);
        return true;
    }

    /** Removes am event from the list of events that the speaker given.
     * @param id A String represents the unique ID of this event.
     * @return true iff the list does contain the ID and it is removed
     */
    @Override
    protected boolean removeFromSpecialList(String id) {
        if (hostingEvents.contains(id)) {
            hostingEvents.remove(id);
            return true;
        }
        return false;
    }

    /** Displays a description for this speaker account about the number of events he/she gives.
     * @return A string representing the number of events given by this speaker account.
     */
    @Override
    public String toString(){
        return super.toString() + "\nYou have hosted " + hostingEvents.size() + " events.";
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
     * Checks if the event id is in this account's specialist
     * @param eventId A String representation of event id
     * @return True iff the event id is in this account's specialist.
     */
    @Override
    protected boolean isInSpecialist(String eventId) {
        return hostingEvents.contains(eventId);
    }
}
