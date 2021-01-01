package account;

import event.EventWithSpecObserver;
import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * A use case class that can manager Account entity. Implements Serializable.
 * It stores all accounts。
 * @author Group0694
 * @version 2.0.0
 */

public class AccountManager implements Serializable {

    // This maps the type of the account to another map which maps the username of a account to the account entity
    private final Map<String, Map<String, Account>> allAccounts = new HashMap<>();
    private final String[] accountTypes = {"Attendee", "Organizer", "Speaker", "VIP"};

    private Account findAccountByUsername(String username){
        for (String type : allAccounts.keySet()){
            Account account = (allAccounts.get(type) != null) ? allAccounts.get(type).get(username) : null;
            if (account != null) return account;
        }
        return null;
    }

    /**
     * Checks if this account is given type
     * @param username A string representation of the username
     * @param type A String representation of the account type
     * @return true iff the account of a given username is the type of given type
     */
    boolean isAccountType(String username, String type) {
        if (allAccounts.containsKey(type)) {
            return allAccounts.get(type).containsKey(username);
        }
        return false;
    }

    /**
     * Checks if this user exist.
     * @param username A string representing the username of the account for checking.
     * @return true iff the account with given username exists, false if the account does not exist.
     */
    public boolean checkUser(String username) {
        return getAccountInfo(username) != null;
    }

    /**
     * Gets all usernames of accounts basing on the type
     * @param type A string represents the type of accounts needed.
     * @return a List of Strings that represent all usernames of the given type of accounts.
     */
    public List<String> getUsernameForType(String type){
        List<String> result = new ArrayList<>();
        Map<String, Account> users = allAccounts.get(type.toLowerCase());
        if (users != null) {result.addAll(users.keySet());}
        return result;
    }

    /**
     * Check if the given password matches the account's password.
     * @param username A string represents the username of this account.
     * @param password A string represents the password that be checked.
     * @return A string represents the type of this account iff the account exists and with the password matching
     * with that account's password, null if the account does not exist or the password does not match.
     */
    public String checkPassword(String username, String password){
        Account account = findAccountByUsername(username);
        return (account == null || !account.getPassword().equals(password)) ? null : account.getType();
    }

    /**
     * Adds a new account to specific account lists basing on the type of this new account. If this account is a
     * speaker account, it is added to speakerList. ELse, it is added to allAccounts.
     * Assumes that this account does not exist
     * @param accountType A string represents the type of this account.
     * @param username A string represents the username of this account.
     * @param password A string represents the password of this account.
     */
    public void addAccount(String accountType, String username, String password) {
        AccountFactory af = new AccountFactory();
        allAccounts.computeIfAbsent(accountType.toLowerCase(), k -> new HashMap<>());
        allAccounts.get(accountType.toLowerCase()).put(username, af.getAccount(accountType, username, password));
    }

    /**
     * Checks if a given account is available at given time interval
     * @param startTime Represents the start of a time interval
     * @param endTime Represents the end of a time interval
     * @param user A string represents the username of the account.
     * @return true iff this account exists and is available at given time, false if the account does not exist
     * or is not available at that time.
     */
    public boolean freeAtTime(Timestamp startTime, Timestamp endTime, String user) {
        Account account = findAccountByUsername(user);
        return (account != null) && account.isAvailable(startTime, endTime);
    }

    /**
     * Checks if a given account is available at given time interval list
     * @param timeDuration A sorted collection of time interval where start time is at index 0 and end time is index 1
     * @param user A String representation of username
     * @return true iff this account exists and is available at given time list
     */
    public boolean freeAtTime(SortedSet<Timestamp[]> timeDuration, String user) {
        Account account = findAccountByUsername(user);
        if (account == null) {
            return false;
        }
        for (Timestamp[] t : timeDuration) {
            if (!account.isAvailable(t[0], t[1])) {
                return false;
            }
        }
        return true;
    }

    /**
     * Signs up a event for an account at a given time interval
     * @param timeDuration A sorted collection of time interval where start time is at index 0 and end time is index 1
     * @param event A string represents the unique id of this event.
     * @param username A string represents the username of an account.
     * @return true iff this account exists and is available at the given time list
     */
    public boolean signUpEvent(SortedSet<Timestamp[]> timeDuration, String event, String username){
        Account curAccount = findAccountByUsername(username);
        if (curAccount == null){
            // curAccount.addEvent(startTime, endTime, event);
            return false;
        }
        for (Timestamp[] t : timeDuration) {
            if (!curAccount.isAvailable(t[0], t[1])) {
                return false;
            }
        }
        for (Timestamp[] t : timeDuration) {
            curAccount.addEvent(t[0], t[1], event);
        }
        return true;
    }

    /**
     * Cancels a event for an account at a given time interval
     * @param timeDuration A sorted collection of time interval where start time is at index 0 and end time is index 1
     * @param event A string represents the unique id of this event.
     * @param username A string represents the username of this account.
     * @return true iff this account exists and the event can be removed from account's eventList successfully,
     * false if this account does not exist or the removing fails.
     */
    public boolean cancelEvent(SortedSet<Timestamp[]> timeDuration, String event, String username){
        Account curAccount = findAccountByUsername(username);
        if (curAccount == null) {
            return false;
        }
        for (Timestamp[] t : timeDuration) {
            curAccount.removeEvent(t[0], t[1], event);
        }
        return true;
    }

    /**
     * Gets a list of strings representing the events that the user signed up.
     * @param username A string represents the username of the account.
     * @return A Iterator of a map representing user's current signed up events in the format of
     * a string array with [start time, end time, event ID], if the target account exists.
     */
    Iterator<String[]> viewSignedUpEvents(String username) {
        Account curAccount = findAccountByUsername(username);
        if (curAccount == null) return null;
        return curAccount.getSchedule();
    }

    /**
     * Adds a new friend to the list of friends of the account.
     * @param username A string represents the username of the account.
     * @param friend A string represents the username of the friend account.
     * @return true iff the account exist, the friend account exists and does not exist in the account's friend list,
     * and add the friend successfully, false if the either accounts does not exist or the friend account already in
     * the friend list of the account.
     */
    boolean addFriend(String username, String friend){
        Account curAccount = findAccountByUsername(username);
        if (curAccount == null || findAccountByUsername(friend) == null) return false;
        if (!curAccount.hasFriend(friend)) {
            curAccount.addFriend(friend);
            return true;
        }
        else return false;
    }

    /**
     * Removes a friend from the list of friends of the account.
     * @param username A string represents the username of the account.
     * @param friend A string represents the username of the friend account.
     * @return true iff the account exist, the friend exists and exist in the account's friend list,
     * and removes friend successfully, false if the either accounts does not exist or the friend account is not in
     * the friend list of the account.
     */
    boolean removeFriend(String username, String friend){
        Account curAccount = findAccountByUsername(username);
        if (curAccount == null || findAccountByUsername(friend) == null) return false;
        if (curAccount.hasFriend(friend)) {
            curAccount.removeFriend(friend);
            return true;
        }
        else return false;
    }

    /**
     * Gets the account's friend list.
     * @param username A string represents the username of the account.
     * @return A Iterator of strings representing the usernames of the account's friends if this account exists,
     * null if the account does not exist.
     */
    Iterator<String> getFriendList(String username){
        Account curAccount = findAccountByUsername(username);
        if (curAccount == null) return null;
        return curAccount.getFriends();
    }

    /**
     * Gets the message list with given username and given key
     * @param username A String representation of the username
     * @param key A String representation of message status
     * @return An iterator of message ids
     */
    public Iterator<String> getMessageMap(String username, String key) {
        Account curr = findAccountByUsername(username);
        if (curr == null) return null;
        return curr.getMessageMap(key);
    }

    /**
     * Adds the message id to the message list with given username and given key
     * @param username A String representation of the username
     * @param key A String representation of the message status
     * @param messageId A String representation of the message id
     */
    public void updateMessageMap(String username, String key, String messageId) {
        Account curr = findAccountByUsername(username);
        if (curr == null) return;
        curr.updateMessageMap(key, messageId);
    }

    /**
     * Removes the message id from a message list with given username and given key
     * @param username A String representation of the username
     * @param key A String representation of the message status
     * @param messageId A String representation of the message id
     */
    public void removeMessageMap(String username, String key, String messageId) {
        // Assuming everything is checked
        Account curr = findAccountByUsername(username);
        if (curr == null) return;
        curr.removeMessageMap(key, messageId);
    }

    /**
     * Check if the message id is a valid id in the message list
     * @param username A String representation of the username
     * @param messageId A String representation of the message id
     * @param key A String representation of the message status
     * @return true iff the message id is a valid message id in the message list
     */
    public boolean isValidMessageId(String username, String messageId, String key) {
        Account curr = findAccountByUsername(username);
        if (curr == null) return false;
        List<String> lst = new ArrayList<>();
        curr.getMessageMap(key).forEachRemaining(lst::add);
        return lst.contains(messageId);
    }

    /**
     * Checks if an account can do messaging operations.
     * @param username A string represents the username of this account.
     * @return true iff this account exists
     */
    public boolean checkMessagable(String username){
        Account curAccount = findAccountByUsername(username);
        return curAccount != null;
    }

    /**
     * Gets the collection of events that are specialized regarding to the type of the account.
     * @param username A string represents the username of this account.
     * @return A list of event id represents all the specialized events operating by this account.
     */
    Iterator<String> getSpecialList(String username){
        Account curAccount = findAccountByUsername(username);
        if (curAccount == null) return null;
        return curAccount.getSpecialList();
    }

    /**
     * Adds a specialized event into the collection of all specialized events operating by this account.
     * @param id A string represents the id of this event.d
     * @param username A string represents the username of this account.
     */
    public void addToSpecialList(String id, String username){
        Account curAccount = findAccountByUsername(username);
        if (curAccount == null) {
            return;
        }
        curAccount.addToSpecialList(id);
    }

    /** Removes an event from the list of events that the organizer organized.
     * @param id A string represents the unique ID of this event.
     */
    public void removeFromSpecialList(String id, String username) {
        Account curAccount = findAccountByUsername(username);
        if (curAccount != null) curAccount.removeFromSpecialList(id);
    }

    /**
     * Gets specific information of an account.
     * @param username A string represents the username of this account.
     * @return A string represents the detailed information of the account if this account exists.
     */
    public String getAccountInfo(String username){
        Account curAccount = findAccountByUsername(username);
        if (curAccount == null) return null;
        return curAccount.toString();
    }

    /**
     * Sets a new password for an account.
     * @param newPassword A string represents the new password.
     * @param username A string represents the username of this account.
     */
    void setPassword(String newPassword, String username) {
        Account curAccount = findAccountByUsername(username);
        if (curAccount == null) return;
        curAccount.setPassword(newPassword);
    }

    /**
     * Gets password of an account.
     * @param username A string represents the username of this account.
     */
    String getPassword(String username) {
        Account account = findAccountByUsername(username);
        if (account != null) {
        return account.getPassword();}
        return null;
    }

    /**
     * Gets available event id that user is free and can join
     * @param m A map that maps the event id to its duration
     * @param username A String representation of account's username
     * @return A list of event id that user is free and can join
     */
    List<String> getAvailableEvent(Map<String, SortedSet<Timestamp[]>> m, String username) {
        List<String> o = new ArrayList<>();
        for (String eventId : m.keySet()) {
            if (freeAtTime(m.get(eventId), username)) {
                o.add(eventId);
            }
        }
        return o;
    }


    /**
     * Finds all speakers that are free at the given time interval
     * @param eventDuration A sorted collection of time interval where start time is at index 0 and end time is index 1
     * @param eventId A String representation of event id
     * @return A list of speaker IDs that corresponds to speakers that are free at this time interval
     */
    public List<String> getAvailableSpeakers(SortedSet<Timestamp[]> eventDuration, String eventId){
        List<String> result = new ArrayList<>();
        Map<String, Account> map = allAccounts.get("speaker");
        if (map != null) {
            outer_loop: for (String speaker : map.keySet()) {
                if (!this.checkInSpecialist(speaker, eventId)) {
                    for (Timestamp[] t : eventDuration) {
                        if (!this.freeAtTime(t[0], t[1], speaker)) {
                            continue outer_loop;
                        }
                    }
                }
                result.add(speaker);
            }
        }
        return result;
    }

    /**
     * Gets all account types
     * @return A list of all possible account types represented by Strings
     */
    public List<String> getAllTypes(){
        return Arrays.asList(accountTypes);
    }

    /**
     * Checks if a given existing user is VIP
     * @param username The username of the target user
     * @return True iff the user is VIP
     */
    boolean checkVIP(String username){
        Account account = findAccountByUsername(username);
        return account != null && account.isVIP();
    }

    /**
     * Upgrades the given username from attendee to VIP
     * @param username A String representation of the account that will be upgraded
     * @param key A list of valid message status
     */
    void upgradeAttendee(String username, List<String> key) {
        // assume the given username is valid
        Account acc = allAccounts.get(accountTypes[0].toLowerCase()).get(username);
        addAccount(accountTypes[3].toLowerCase(), username, acc.getPassword());
        Account currAccount = allAccounts.get(accountTypes[3].toLowerCase()).get(username);
        // Update friend list
        Iterator<String> friends = acc.getFriends();
        while (friends.hasNext()) currAccount.addFriend(friends.next());
        // Update schedule
        Iterator<String[]> currSchedule = acc.getSchedule();
        while (currSchedule.hasNext()) {
            String[] event = currSchedule.next();
            currAccount.addEvent(Timestamp.valueOf(event[0]), Timestamp.valueOf(event[1]), event[2]);
        }
        // Update message map
        for (String k : key) {
            currAccount.setMessageMap(k, acc.getMessageMap(k));
        }
        // Remove the account from attendee list
        allAccounts.get(accountTypes[0].toLowerCase()).remove(username);
    }

    /**
     * Degrades the given username from VIP to attendee
     * @param username A String representation of the account that will be degraded
     * @param messageKey A list of valid message status
     * @return A list of VIP events that user with given username is attending
     */
    List<String> degradeVIP(String username, List<String> messageKey) {
        // assume the given username is valid
        Account acc = allAccounts.get(accountTypes[3].toLowerCase()).get(username);
        addAccount(accountTypes[0].toLowerCase(), username, acc.getPassword());
        Account currAccount = allAccounts.get(accountTypes[0].toLowerCase()).get(username);
        // Update friend list
        Iterator<String> friends = acc.getFriends();
        while (friends.hasNext()) currAccount.addFriend(friends.next());
        // Update schedule
        Iterator<String[]> currSchedule = acc.getSchedule();
        List<String[]> currScheduleList = new ArrayList<>();
        currSchedule.forEachRemaining(currScheduleList::add);
        Iterator<String> currVIP = acc.getSpecialList();
        List<String> vipEvents = new ArrayList<>();
        currVIP.forEachRemaining(vipEvents::add);
        // Remove all the vip events signed up
        for(String[] event : currScheduleList) {
            if (vipEvents.contains(event[2])) {
                acc.removeEvent(Timestamp.valueOf(event[0]), Timestamp.valueOf(event[1]), event[2]);
            }else {
                currAccount.addEvent(Timestamp.valueOf(event[0]), Timestamp.valueOf(event[1]), event[2]);
            }
        }
        // Update message map
        for (String k : messageKey) {
            currAccount.setMessageMap(k, acc.getMessageMap(k));
        }
        // Remove the account from attendee list
        allAccounts.get(accountTypes[3].toLowerCase()).remove(username);
        return vipEvents; // It will be used to update the event manager
    }

    /**
     * Gets an instance of EventWithSpecObserver
     * @param username A String representation of username
     * @return An instance of EventWithSpecObserver. Return null if account does not exist.
     */
    public EventWithSpecObserver getEventObserver(String username) {
        return findAccountByUsername(username);
    }

    private boolean checkInSpecialist(String username, String eventId) {
        Account a = this.findAccountByUsername(username);
        if (a != null) {
            return a.isInSpecialist(eventId);
        }
        return false;
    }
}


