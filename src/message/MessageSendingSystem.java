package message;

import account.AccountManager;
import event.EventManager;
import userinterface.UserSystem.MessageStatus;

import java.util.*;

/**
 * An upper controller of message, mostly in charge of sending message
 * Stores MessagingManager, MessagePresenter, AccountManager class, also uses EventManager
 * Gets input from user and gives corresponding moves by calling methods from MessagingManager and AccountManager
 * @author Group0694
 * @version 2.0.0
 */
public class MessageSendingSystem {

    private final MessagePresenter presenter;
    private final AccountManager accounts;
    private final MessagingManager messages;

    /**
     * Constructs a MessageSendingSystem object
     * @param accounts An instance of AccountManager
     * @param messages An instance of MessagingManager
     */
    public MessageSendingSystem(AccountManager accounts, MessagingManager messages){
        presenter = new MessagePresenter();
        this.accounts = accounts;
        this.messages = messages;
    }

    //send message to given receivers

    /**
     * Sends a WordMessage from a user with username given as a parameter to receivers given by user input.
     * The subject and content of WordMessage are both given by user input.
     * Gives prompt to user to enter a list of receivers, subject and content step by step, and check if
     * receivers are valid, if not, will print out feedback to user.
     * After this message is sent successfully, there will be feedback printed out to user.
     * @param sender username of the user who send the message
     */
    public void sendMessage (String sender) {
        if (!messagable(sender)) return;
        Scanner sc = new Scanner(System.in);
        List<String> realReceivers = getReceivers(sc, sender);
        if (realReceivers == null) return;
        sendMessage(sender, sc, realReceivers);
    }

    private List<String> getReceivers(Scanner sc, String sender) {
        presenter.askReceivers();
        String receiver = sc.nextLine();
        List<String> realReceivers = new ArrayList<>();
        while (!(receiver.equalsIgnoreCase("s"))) {
            if (accounts.checkUser(receiver)) {
                if (!receiver.equals(sender)) {
                    if (!realReceivers.contains(receiver)) {
                        realReceivers.add(receiver);
                        presenter.printActionMessage("Add " + receiver + " to the receiver list, " +
                                "please enter next receiver's username or enter 's' to stop");
                    } else {
                        presenter.alreadyInLIst(receiver);
                    }
                } else {
                    presenter.sendToSelf();
                }
            } else {
                presenter.accountNotExist();
            }
            presenter.getInput();
            receiver = sc.nextLine();
        }
        if (realReceivers.size() == 0) {
            presenter.printErrorMessage("You need at least one receiver to send message");
            presenter.printSendMessage(false);
            return null;
        }
        return realReceivers;
    }

    private void sendMessage(String sender, Scanner sc, List<String> realReceivers) {
        presenter.askSubject();
        String subject = sc.nextLine();
        presenter.askContent();
        String content = sc.nextLine();
        String messageId = messages.sendWordMessage(sender, realReceivers, subject, content);
        updateAccountMessageList(messageId, sender, realReceivers);
        presenter.printSendMessage(true);
    }

    @SuppressWarnings("BooleanMethodIsAlwaysInverted")
    private boolean messagable(String sender) {
        if (!accounts.checkMessagable(sender)) {
            presenter.notMessagable();
            return false;
        }
        return true;
    }

    private void updateAccountMessageList(String messageId, String senderUsername, List<String> receiverUsername) {
        // Sent status
        accounts.updateMessageMap(senderUsername, MessageStatus.SENT.toString(), messageId);
        // Unread status
        final String key = MessageStatus.UNREAD.toString();
        for (String receiver : receiverUsername) {
            accounts.updateMessageMap(receiver, key, messageId);
        }
    }

    //send message to all speakers, valid for organizer

    /**
     * Sends same message for all speakers only from organizer whose user name given as parameter.
     * The receiver doesn't need to be given by user input or parameter, can only send to all registered speakers.
     * The subject and content of WordMessage are both given by user input.
     * Gives prompt to user to enter subject and content step by step, and check if receivers are valid,
     * if not, will print out feedback to user.
     * After this message is sent successfully, there will be feedback printed out to user.
     * @param organizerName username of the organizer who send the message
     */
    public void sendMessageToSpeakers (String organizerName) {
        if (!messagable(organizerName)) return;
        Scanner sc = new Scanner(System.in);
        List<String> speakers = accounts.getUsernameForType("Speaker");
        if(speakers.isEmpty()){
            presenter.noSpeakers();
            return;
        }
        sendMessage(organizerName, sc, speakers);
    }

    //send message to all attendees, valid for organizer

    /**
     * Send the same message to all attendees only from organizer whose username is given as parameter.
     * The receiver doesn't need to be given by user input or parameter, can only send to all registered attendees.
     * The subject and content of WordMessage are both given by user input.
     * This method gives prompt to user to enter subject and content step by step, and check if receivers are valid,
     * if not, will print out feedback to user.
     * After the message is sent successfully, there will be feedback printed out to user.
     * @param organizerName username of the organizer who send the message
     */
    public void sendMessageToAttendees (String organizerName) {
        if (!messagable(organizerName)) return;
        Scanner sc = new Scanner(System.in);
        List<String> attendees = accounts.getUsernameForType("Attendee");
        if(attendees.isEmpty()){
            presenter.noAttendees();
            return;
        }
        sendMessage(organizerName, sc, attendees);
    }

    //send message to all attendees in some events, valid for speaker

    /**
     * Sends messages to all attendees in the events
     * @param speakerName A String representation of the speaker username
     * @param events An instance of EventManager
     */
    public void sendMessageToAttendeesInEvent(String speakerName, EventManager events) {
        if (!messagable(speakerName)) return;
        Scanner sc = new Scanner(System.in);
        List<String> attendees = getAttendeeInEvents(speakerName, events, sc);
        if (attendees == null) return;
        sendMessage(speakerName, sc, attendees);
    }

    private List<String> getAttendeeInEvents(String speakerName, EventManager events, Scanner sc) {
        Set<String> attendees = new HashSet<>();
        presenter.askEventID();
        String eventID = sc.nextLine();
        List<String> hostList;
        while (!(eventID.equalsIgnoreCase("s"))) {
            if (!(events.checkEvent(eventID))) {
                presenter.noEvent();
                presenter.getInput();
                eventID = sc.nextLine();
                continue;
            }
            hostList = new ArrayList<>();
            events.getHosts(eventID).forEachRemaining(hostList::add);
            if (!hostList.contains(speakerName)) {
                presenter.notSpeaker();
            }
            else {
                Iterator<String> lst = events.getAttendees(eventID);
                while (lst.hasNext()) {
                    String att = lst.next();
                    attendees.add(att);
                }
                presenter.printActionMessage("Added all attendees of " + eventID +
                        " to the receiver list.");
            }
            presenter.getInput();
            eventID = sc.nextLine();
        }
        if (attendees.isEmpty()) {
            presenter.noReceivers();
            return null;
        }
        return new ArrayList<>(attendees);
    }

}
