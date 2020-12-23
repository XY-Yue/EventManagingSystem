package userinterface;

import account.AccountEventSystem;
import account.AccountManager;
import account.AccountSystem;
import event.EventManager;
import event.EventSystem;
import message.MessageSearchingSystem;
import message.MessageSendingSystem;
import message.MessageUpdatingSystem;
import message.MessagingManager;
import room.RoomManager;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * This controller class represents an abstract user system
 * @author Group0694
 * @version 2.0.0
 */

@SuppressWarnings("BooleanMethodIsAlwaysInverted")
public abstract class UserSystem {
    protected String username;
    protected final AccountManager accountManager;
    protected final EventManager eventManager;
    protected final RoomManager roomManager;
    protected final MessagingManager messagingManager;

    /**
     * Stores available status for messages
     */
    //Stores here as it is needed in multiple classes, even if this seems to belong to 'message' package, there
    //are 3 controllers in message and none is a suitable place to place this enum
    public enum MessageStatus {
        SENT {
            public String toString() {
                return "sent";
            }
        },
        RECEIVED {
            public String toString() {
                return "received";
            }
        },
        UNREAD {
            public String toString() {
                return "unread";
            }
        },
        ARCHIVE {
            public String toString() {
                return "archive";
            }
        }
    }

    /**
     * Gets the valid message status
     * @return A list of valid message status
     */
    List<String> getMessageStatus() {
        List<String> lst = new ArrayList<>();
        for(MessageStatus ms : MessageStatus.values()) {
            lst.add(ms.toString());
        }
        return lst;
    }

    /**
     * Creates a UserSystem with specific speaker account's username, messageSearchingSystem, eventSystem, roomSystem,
     * and accountSystem
     * @param username A string represents the username of this user account
     * @param messagingManager A copy of the MessagingManager use case
     * @param eventManager A copy of the EventManager use case
     * @param roomManager A copy of the RoomManager use case
     * @param accountManager A copy of the AccountManager use case
     */
    UserSystem(String username, MessagingManager messagingManager,
               EventManager eventManager, RoomManager roomManager,
               AccountManager accountManager){
        this.username = username;
        this.messagingManager = messagingManager;
        this.eventManager = eventManager;
        this.roomManager = roomManager;
        this.accountManager = accountManager;

    }

    /**
     * Provides five options and a logout option for a user to choose
     * Allows the user to decide message, event or account sub-menus
     */
    public void selectOption(){
        Scanner sc = new Scanner(System.in);
        UserPresenter up = new UserPresenter();
        MessageSearchingSystem messageSearchingSystem = new MessageSearchingSystem(messagingManager, accountManager);
        while (true) {
            up.optionMenu(this.username);
            switch (sc.nextLine()){
                case "0":
                    this.accountOption();
                    break;
                case "1":
                    this.messageInboxOption(messageSearchingSystem);
                    break;
                case "2":
                    this.messageAccessOption();
                    break;
                case "3":
                    this.eventScheduleOption();
                    break;
                case "4":
                    this.eventAccessOption();
                    break;
                case "r":
                    // this will result in logout
                    up.logOut();
                    switch (sc.nextLine()) {
                        case "y":
                            up.logOutSuccess();
                            return;
                        case "n":
                            break;
                        default:
                            up.printInvalidInput();
                    }
                    break;
                default:
                    up.printInvalidInput();
            }
        }
    }

    /**
     * Provides options for searching messages such as get all message from a specific account
     * @param mss An instance of MessageSearchingSystem
     */
    abstract protected void messageInboxOption(MessageSearchingSystem mss);

    /**
     * Provides options for managing messages such as setting a message as read/unread
     */
    abstract protected void messageAccessOption();

    /**
     * Provides options for event viewing related operations
     */
    abstract protected void eventScheduleOption();

    /**
     * Provides options for event signing up or canceling related operations
     */
    abstract protected void eventAccessOption();

    /**
     * Provides options for account related operations such as viewing account info
     */
    abstract protected void accountOption();

    /**
     * Provides general message viewing options that all accounts have
     * @param input The input entered in subclasses
     * @param messageSearchingSystem An instance of MessageSearchingSystem
     * @return true iff a valid option is selected here
     */
    protected boolean generalMessageInboxOption(String input, MessageSearchingSystem messageSearchingSystem) {
        switch(input) {
            case "0":
                messageSearchingSystem.setDisplayingOrder();
                return true;
            case "1":
                messageSearchingSystem.getMessageReceive(this.username, MessageStatus.RECEIVED.toString(),
                        MessageStatus.UNREAD.toString());
                return true;
            case "2":
                messageSearchingSystem.getReceivedMessagesFromSpecificAccount(this.username,
                        MessageStatus.RECEIVED.toString(),
                        MessageStatus.UNREAD.toString());
                return true;
            case "3":
                messageSearchingSystem.getMessageSent(this.username, MessageStatus.SENT.toString());
                return true;
            case "4":
                messageSearchingSystem.getSentMessageToSpecificAccount(this.username,
                        MessageStatus.SENT.toString());
                return true;
            case "5":
                // view message unread
                messageSearchingSystem.getUnreadMessage(this.username, MessageStatus.RECEIVED.toString(),
                        MessageStatus.UNREAD.toString());
                return true;
            case "6":
                // view message unread from a specific account
                messageSearchingSystem.getUnreadMessageFromSpecificAccount(this.username,
                        MessageStatus.RECEIVED.toString(), MessageStatus.UNREAD.toString());
                return true;
            case "7":
                messageSearchingSystem.getArchivedMessages(this.username, MessageStatus.ARCHIVE.toString());
                return true;
            default:
                return false;
        }
    }

    /**
     * Provides general message status options that all accounts have
     * @param input The input entered in subclasses
     * @return true iff a valid option is selected here
     */
    protected boolean generalMessageAccessOption(String input) {
        MessageUpdatingSystem messageUpdatingSystem = new MessageUpdatingSystem(accountManager);
        switch(input) {
            case "0":
                messageUpdatingSystem.markAsUnread(this.username);
                return true;
            case "1":
                messageUpdatingSystem.archiveMessage(this.username);
                return true;
            case "2":
                messageUpdatingSystem.unarchiveMessage(this.username);
                return true;
            case "3":
                messageUpdatingSystem.deleteMessage(this.username);
                return true;
            case "4":
                new AccountSystem(accountManager).viewFriendList(this.username);
                return true;
            case "5":
                new MessageSendingSystem(accountManager, messagingManager).sendMessage(this.username);
                return true;
            default:
                return false;
        }
    }

    /**
     * Provides general event schedule viewing options that all accounts have
     * @param input The input entered in subclasses
     * @return true iff a valid option is selected here
     */
    protected boolean generalEventScheduleOption(String input) {
        EventSystem eventSystem = new EventSystem(eventManager);
        AccountEventSystem accountEventSystem = new AccountEventSystem(accountManager);
        switch(input) {
            case "0":
                eventSystem.printEventList(false);
                return true;
            case "1":
                accountEventSystem.viewSignEvents(this.username);
                return true;
            case "2":
                accountEventSystem.viewEventsAttendable(this.username, eventManager, false);
                return true;
            case "3":
                eventSystem.getEventByTime();
                return true;
            case "4":
                eventSystem.getEventByLocation();
                return true;
            case "5":
                accountEventSystem.getEventsBySpeaker();
                return true;
            default:
                return false;
        }
    }

    /**
     * Provides general account accessing viewing options that all accounts have (such as sign up or cancel a event)
     * @param input The input entered in subclasses
     * @return true iff a valid option is selected here
     */
    protected boolean generalEventAccessOption(String input) {
        EventSystem eventSystem = new EventSystem(eventManager);
        AccountEventSystem accountEventSystem = new AccountEventSystem(accountManager);
        switch(input) {
            case "0":
                accountEventSystem.signUpEvent(this.username, eventManager);
                return true;
            case "1":
                accountEventSystem.cancelEvent(this.username, eventManager);
                return true;
            case "2":
                eventSystem.checkDescription();
                return true;
            default:
                return false;
        }
    }

    /**
     * Provides general account options that all accounts have
     * @param input The input entered in subclasses
     * @return true iff a valid option is selected here
     */
    protected boolean generalAccountOption(String input) {
        AccountSystem accountSystem = new AccountSystem(accountManager);
        switch(input) {
            case "0":
                accountSystem.displayCurrentAccount(this.username);
                return true;
            case "1":
                accountSystem.changePassword(this.username);
               return true;
            case "2":
                accountSystem.viewFriendList(this.username);
                return true;
            case "3":
                accountSystem.addFriend(this.username);
                return true;
            case "4":
                accountSystem.removeFriend(this.username);
                return true;
            default:
                return false;
        }
    }
}
