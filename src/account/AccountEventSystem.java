package account;

import event.EventManager;

import java.sql.Timestamp;
import java.util.*;

/** 
 * A controller class that responsible for joint Account Event operations.
 * Responsibilities includes Signing up for events, canceling events, etc
 * @author Group0694
 * @version 2.0.0
 */
public class AccountEventSystem {

    private final AccountManager accountManager;
    private final AccountPresenter presenter;

    /**
     * Creates a AccountEventSystem that stores a copy of AccountManager
     * @param accountManager A alias of the AccountManager object made outside of this class
     */
    public AccountEventSystem(AccountManager accountManager){
        this.accountManager = accountManager;
        presenter = new AccountPresenter();
    }

    /**
     * Prints a string of speakers of eventManager the user signed up.
     * @param username The username of current user.
     * @param eventManager A alias of EventManager use case.
     */
    public void getSpeakerOfSignedEvents(String username, EventManager eventManager) {
        List<String> speakers = new ArrayList<>();
        Iterator<String[]> events = accountManager.viewSignedUpEvents(username);
        while (events.hasNext()) {
            String id;
            for (Iterator<String> i = eventManager.getHosts(events.next()[2]); i.hasNext();) {
                id = i.next();
                if (!speakers.contains(id)) {
                    speakers.add(id);
                }
            }
        }
        presenter.getSpeakerOfSignedEvents(speakers);
    }

    /**
     * Prints a string of the eventManager the user signed up.
     * @param username The username of current user.
     */
    public void viewSignEvents(String username) {
        presenter.getSignedEvents(accountManager.viewSignedUpEvents(username));
    }

    private String getValidEvent(EventManager eventManager){
        Scanner sc = new Scanner(System.in);
        presenter.askEventId();
        String eventId = sc.nextLine();
        if (!eventManager.checkEvent(eventId)) {
            presenter.eventNotExist();
            return null;
        }
        return eventId;
    }

    /**
     * Takes in user input for signing up a event, prints the result of the event registration.
     * @param username The username of current user.
     * @param eventManager A alias of EventManager use case.
     */
    public void signUpEvent(String username, EventManager eventManager) {
        String eventId = getValidEvent(eventManager);
        if (eventId == null) return;
        if (eventManager.isVIP(eventId) && !accountManager.checkVIP(username)){
            presenter.printErrorMessage("This event is only available for VIPs");
            return;
        }
        SortedSet<Timestamp[]> timeDuration = eventManager.getEventDuration(eventId);
        if(!eventManager.isValidEvent(eventId)) {
            presenter.printPassedEvent(eventId);
        }
        else if (!eventManager.canSignup(eventId)) presenter.signupFailEventFull();

        else if (!accountManager.freeAtTime(timeDuration, username)) presenter.signupFailNotFree();
        else {
            // Event will notify attendee observer
            presenter.signUpEventResult(eventManager.addAttendee(accountManager.getEventObserver(username), eventId));
        }
    }

    /**
     * Prints a string reflecting the result of cancel the enrollment of an event.
     * @param username The username of current user.
     * @param eventManager An alias of EventManager use case.
     */
    public void cancelEvent(String username, EventManager eventManager) {
        String eventId = getValidEvent(eventManager);
        if (eventId == null) return;
        if (eventManager.hasAttendee(eventId, accountManager.getEventObserver(username))) {
            SortedSet<Timestamp[]> timeDuration = eventManager.getEventDuration(eventId);
            Timestamp currTime = new Timestamp(new Date().getTime());
            Timestamp firstStartTime = timeDuration.first()[0];
            if (currTime.after(firstStartTime)) {
                presenter.printEventExpired(eventId);
                return;
            }
            presenter.cancelEventResult(eventManager.removeAttendee(accountManager.getEventObserver(username),
                    eventId));
        } else {
            presenter.userNotSignUpEvent(username, eventId);
        }
    }

    /**
     * Requests input of speaker name from user and print a string reflecting all eventManager that are given
     * by a same speaker.
     */
    public void getEventsBySpeaker() {
        Scanner sc = new Scanner(System.in);
        presenter.askSpeakerName();
        String speakerName = sc.nextLine();
        if (accountManager.getUsernameForType("Speaker").contains(speakerName)) {
            getSpecialListEvents(speakerName);
        }
        else {
            presenter.speakerNotExist();
        }
    }

    /**
     * Gets the specialist of the given account
     * @param username A String representation of the account's username
     */
    public void getSpecialListEvents(String username) {
        presenter.printSpecialListEvents(accountManager.getSpecialList(username));
    }

    /**
     * Prints out all events that the user with given username is attendable.
     * @param username the username of user we want to find attendable event
     * @param eventManager A alias of EventManager use case.
     * @param isVIP indicates if we want VIP events only
     */
    public void viewEventsAttendable(String username, EventManager eventManager, boolean isVIP) {
        boolean accountIsVip = accountManager.checkVIP(username);
        Map<String, SortedSet<Timestamp[]>> m = eventManager.getAllEventTime(accountIsVip, isVIP);
        List<String> allEvents = accountManager.getAvailableEvent(m, username);
        if (allEvents.size() == 0) {
            presenter.printNotAvailable();
        } else {
            presenter.printEventList(allEvents, m, "Here are all events you are able to attend:");
        }
    }

}
