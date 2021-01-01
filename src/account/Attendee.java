package account;

import java.util.Iterator;

/**
 * An entity class represents an attendee account, which is a child class of abstract class Account.
 * @author Group0694
 * @version 2.0.0
 */

class Attendee extends Account {
    /**
     * Creates an attendee account with specific username and his/her account password.
     * @param name A string represents the username of this attendee account.
     * @param password  A string represents the password of this attendee account.
     */
    public Attendee(String name, String password) {
        super(name, password);
    }

    /** Gets the type of this attendee account.
     * @return A string that shows the type of this account is attendee.
     */
    @Override
    protected String getType() {
        return "Attendee";
    }

    /** Gets the Iterator of the list of events specialized for this account (unavailable for attendee)
     * @return A Iterator of the list of string of the events specialized to Attendee, which is always null.
     */
    @Override
    protected Iterator<String> getSpecialList() {
        return null;
    }

    /** Adds a event into the specialized list for this account
     * @param id A String represents the unique ID of this event.
     * @return true iff the list does not already contain the ID
     */
    @Override
    protected boolean addToSpecialList(String id) {
        return false;
    }

    /** Removes a event from the specialized list for this account
     * @param id A String represents the unique ID of this event.
     * @return true iff the list does contain the ID and it is removed
     */
    @Override
    protected boolean removeFromSpecialList(String id) {
        return false;
    }

    /**
     * Checks if this account has VIP access
     * @return false only, because normal attendees cannot be VIP
     */
    @Override
    protected boolean isVIP() {
        return false;
    }

    /**
     * Checks if the given id is in this account's specialist
     * @param id A String representation of event id
     * @return True iff id is in this account's specialist
     */
    @Override
    protected boolean isInSpecialist(String id) {
        return false;
    }
}
