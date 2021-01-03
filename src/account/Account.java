package account;

import event.EventWithSpecObserver;
import room.Available;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/** An abstract class represents all type of accounts. Implements Serializable, Available, EventWithSpecObserver.
 * An account can store this user account's username, password, friends list, associated events，and whether
 * this user account can do messaging operations.
 * @author Group0694
 * @version 2.0.0
 */
public abstract class Account implements Serializable, Available, EventWithSpecObserver {
    protected NavigableMap<Timestamp[], String> schedule;
    private final String username; // No getters as the username are already stored in use case
    private String password;
    private List<String> friends;
    private final Map<String, List<String>> messageMap;

    /**
     * Creates an account with the specified username and password.
     * @param name The username of this account.
     * @param password The password to log in this account.
     */
    public Account(String name, String password) {
        this.username = name;
        this.password = password;
        this.friends = new ArrayList<>();
        this.schedule = new TreeMap<> ((Comparator<Timestamp[]> & Serializable)
                (Timestamp[] t1, Timestamp[]t2) -> {
                    // t1 == [startTime1, endTime1]
                    // t2 == [startTime2, endTime2]
                    // They won't overlap
                    if (!t1[1].after(t2[0])) {
                        return -1;
                    }
                    if (!t2[1].after(t1[0])) {
                        return 1;
                    }
                    return 0;
                }

        );
        this.messageMap = new HashMap<>();
    }

    private class ScheduleIterator implements Iterator<String[]>{
        private final Iterator<Map.Entry<Timestamp[], String>> set = schedule.entrySet().iterator();

        /**
         * Checks if the iterator has a next element
         * @return true iff there is a next element
         */
        @Override
        public boolean hasNext() {
            return set.hasNext();
        }

        /**
         * Gets the next element in the schedule iterator, assumes it exists
         * @return a array of string in the format of [start time, end time, event ID], all in string representation
         */
        @Override
        public String[] next() {
            Map.Entry<Timestamp[], String> map = set.next();
            return new String[]{map.getKey()[0].toString(), map.getKey()[1].toString(), map.getValue()};
        }
    }

    /**
     * Gets the password of this account.
     * @return A String representing the account’s password.
     */
    protected String getPassword(){
        return password;
    }

    /**
     * Sets the password of this account.
     * @param password A String containing the new password for this account.
     */
    protected void setPassword(String password){
        this.password = password;
    }

    /**
     * Checks if if the account is free at this time interval.
     * @param startTime Represents the start time of the time interval
     * @param endTime Represents the end time of the time interval
     * @return true iff the account is available at the given time interval, false if this account cannot.
     */
    protected boolean isAvailable(Timestamp startTime, Timestamp endTime){
        return this.isAvailable(this.schedule, startTime, endTime);
    }

    /**
     * Adds an event with given duration into the collection of all signed events of this account.
     * @param startTime Represents the start time of the event
     * @param endTime Represents the end time of the event
     * @param id A String representing the id of the new event.
     */
    protected void addEvent(Timestamp startTime, Timestamp endTime, String id){
        // Assume everything is valid
        schedule.put(new Timestamp[]{startTime, endTime}, id);
    }

    /**
     * Cancels the given event at the given time interval.
     * @param startTime Represents the start time of the event
     * @param endTime Represents the end time of the event
     * @param id A String representing the id of event want to be canceled.
     */
    protected void removeEvent(Timestamp startTime, Timestamp endTime, String id) {
        for (Timestamp[] k : schedule.keySet()) {
            if(k[0].equals(startTime) && k[1].equals(endTime)) {
                if (schedule.get(k).equals(id)) {
                    schedule.remove(k);
                    return;
                }
            }
        }
    }

    /**
     * Gets all the signed events of this account.
     * @return A Iterator of the Map of event id representing all the signed events along with their time
     * in ascending order.
     */
    protected Iterator<String[]> getSchedule(){
        return new ScheduleIterator();
    }

    /**
     * Gets all the friends of this account.
     * @return A iterator of the List of usernames representing all the friends of this account.
     */
    protected Iterator<String> getFriends(){
        return friends.iterator();
    }

    /**
     * Gets the type of the account.
     * @return A String representing the type of this account.
     */
    protected abstract String getType();

    /**
     * Gets the collection of events that are specialized regarding to the type of the account.
     * @return A List of event id representing all the specialized events operating by this account.
     */
    protected abstract Iterator<String> getSpecialList();

    /**
     * Adds a specialized event into the collection of all specialized events operating by this account.
     * @param id A String representing the unique id of that new event.
     */
    protected abstract boolean addToSpecialList(String id);

    /**
     * Removes a specialized event from the collection of all specialized events operating by this account.
     * @param id A String representing the unique id of that new event.
     */
    protected abstract boolean removeFromSpecialList(String id);

    /**
     * Displays a description for this account including general information in type, username, signed events
     * and friends.
     * @return A String representing the information including type, username, signed events list
     * and friends list.
     */
    public String toString(){
        return "Type: " + this.getType() + "\nUsername: " + username + "\nYou have signed up for " +
                schedule.size() + " events" + "\nYou have " + friends.size() + " user(s) in your user contact list.";
    }

    /**
     * Checks if this account has a friend with username given
     * @param username A String representing the username of the friend account going to be checked.
     * @return true iff this account has a friend of the given username, false otherwise.
     */
    protected boolean hasFriend(String username) { return this.friends.contains(username); }

    /**
     * Adds this account has a friend with username given
     * @param username A String representing the username of the friend account going to be added.
     */
    protected void addFriend(String username) { friends.add(username); }


    /**
     * Removes the given friend from this account's friend list
     * @param username A String representing the username of the friend account going to be removed.
     */
    protected void removeFriend(String username) {
        friends.remove(username);
    }

    /**
     * Gets the message list with a given key
     * @param key A String representation of the message status
     * @return Am iterator of a list of message ids with given key
     */
    protected Iterator<String> getMessageMap(String key) {
        if (messageMap.containsKey(key)) {
            return this.messageMap.get(key).iterator();
        }
        return Collections.emptyIterator();
    }

    /**
     * Adds a message into a message list with a given key
     * @param key A String representation of the message status
     * @param messageId A String representation of the message id
     */
    protected void updateMessageMap(String key, String messageId) {
        if (!messageMap.containsKey(key)) {
            messageMap.put(key, new ArrayList<>());
        }
        messageMap.get(key).add(messageId);
    }

    /**
     * Removes a message from a message list with a given key
     * @param key A String representation of the message status
     * @param messageId A String representation of the message id
     */
    protected void removeMessageMap(String key, String messageId) {
        // Assuming every thing is checked
        if (messageMap.containsKey(key)) {
            messageMap.get(key).remove(messageId);
        }
    }

    /**
     * Checks if the user is a VIP user
     * @return true iff the user is a VIP user.
     */
    protected abstract boolean isVIP();

    /**
     * Sets the message map
     * @param key The String representation of the message status
     * @param lst The iterator of message id list
     */
    protected void setMessageMap(String key, Iterator<String> lst) {
        String value;
        while (lst.hasNext()) {
            value = lst.next();
            if (messageMap.containsKey(key)) {
                messageMap.get(key).add(value);
            } else {
                List<String> temp = new ArrayList<>();
                temp.add(value);
                messageMap.put(key, temp);
            }
        }
    }

    /**
     * Checks if event id is in current account's specialist
     * @param eventId A String representation of event id
     * @return true iff event id is in current account's specialist
     */
    protected abstract boolean isInSpecialist(String eventId);

    /**
     * Adds and updates an event id to current account's schedule
     * @param eventId A String representation of event id
     * @param timeDuration Event's duration
     */
    @Override
    public void updateAdd(String eventId, SortedSet<Timestamp[]> timeDuration) {
        for (Timestamp[] t : timeDuration) {
            this.schedule.put(t, eventId);
        }
    }

    /**
     * Removes and updates an event id from current account's schedule
     * @param eventId A String representation of event id
     * @param timeDuration Event's duration
     */
    @Override
    public void updateRemove(String eventId, SortedSet<Timestamp[]> timeDuration) {
        for (Timestamp[] t : timeDuration) {
            schedule.remove(t, eventId);
        }
    }

    /**
     * Gets the current account's username
     * @return A String representation of current account's username
     */
    @Override
    public String getName() {
        return this.username;
    }

    /**
     * Adds and updates an event id to current account's schedule. In addition, also updates to its specialist.
     * @param eventId A String representation of event's id
     * @param timeDuration Event's duration
     */
    @Override
    public void updateAddWithSpec(String eventId, SortedSet<Timestamp[]> timeDuration) {
        this.updateAdd(eventId, timeDuration);
        this.addToSpecialList(eventId);
    }

    /**
     * Removes and updates an event id from current account's schedule. In addition, also updates to its specialist.
     * @param eventId A String representation of event's id
     * @param timeDuration Event's duration
     */
    @Override
    public void updateRemoveWithSpec(String eventId, SortedSet<Timestamp[]> timeDuration) {
        this.updateRemove(eventId, timeDuration);
        this.removeFromSpecialList(eventId);
    }

    /**
     * Check if account is free at this time duration.
     * @param timeDuration A sorted set of Timestamp[], where each element has length 2, index 0 is start time, and
     *                     index 1 is end time.
     * @return True iff account is free during the given duration time.
     */
    @Override
    public boolean freeAtThisTime(SortedSet<Timestamp[]> timeDuration) {
        for (Timestamp[] t : timeDuration) {
            if (!this.isAvailable(t[0], t[1])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Updates and adds the event id to current account's specialist
     * @param eventId A String representation of the event id
     */
    @Override
    public void updateSpecAdd(String eventId) {
        this.addToSpecialList(eventId);
    }

    /**
     * Updates and removes the event if from current account's specialist
     * @param eventId A String representation of the event id
     */
    @Override
    public void updateSpecRemove(String eventId) {
        this.removeFromSpecialList(eventId);
    }

    /**
     * Check if current account is VIP account
     * @return True iff current account is VIP account
     */
    @Override
    public boolean isVip() {
        return getType().equals("vip");
    }
}
