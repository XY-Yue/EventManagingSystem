package message;


import java.util.Iterator;
import java.util.Scanner;
import account.AccountManager;

/**
 * An upper controller of message, mostly in charge of getting different status of messages
 * Stores MessagingManager, MessagePresenter, AccountManager, and MessageSorter class
 * Gets input from user and gives corresponding moves by calling methods from MessagingManager and AccountManager
 * @author Group0694
 * @version 2.0.0
 */
public class MessageSearchingSystem {
    private final MessagingManager messages;
    private final MessagePresenter presenter;
    private final AccountManager accounts;
    private MessageSorter sorter;

    /**
     * Constructs a MessageSearchingSystem object
     * @param messages An instance of MessagingManager
     * @param accounts An instance of AccountManager
     */
    public MessageSearchingSystem(MessagingManager messages, AccountManager accounts) {
        this.messages = messages;
        this.accounts = accounts;
        presenter = new MessagePresenter();
        sorter =  new MessageNewestToOldest();
    }

    //get all messages send from a user
    private String inputUsername(boolean sendTo) {
        Scanner c = new Scanner(System.in);
        presenter.askSenderName(sendTo);
        String sender = c.nextLine();
        return (accounts.checkUser(sender)) ? sender : null;
    }

    /**
     * Displays the message sent
     * @param username A String representation of current user's username
     * @param key A String representation of sent message status
     */
    public void getMessageSent(String username, String key) {
        Iterator<String> messageId = accounts.getMessageMap(username, key);
        presenter.printAllMessages(messages.getSentMessages(messageId, sorter),
                "The messages you have sent are:",
                "You haven't sent any message yet.");
    }

    /**
     * Displays the message received
     * @param username A String representation of current user's username
     * @param receivedKey A String representation of received message status
     * @param unreadKey A String representation of unread message status
     */
    public void getMessageReceive(String username, String receivedKey, String unreadKey) {
        // Since read all messages, we should update all unread message into received message
        Iterator<String> messageReceivedIdLst = accounts.getMessageMap(username, receivedKey);
        this.getUnreadMessage(username, receivedKey, unreadKey);
        presenter.printAllMessages(messages.getReceivedMessages(messageReceivedIdLst, sorter),
                "The messages you have received are:",
                "You do not have any reviewed messages in inbox.");
    }

    /**
     * Displays the unread message
     * @param username A String representation of current user's username
     * @param receivedKey A String representation of the received message key
     * @param unreadKey A String representation of the unread message key
     */
    public void getUnreadMessage(String username, String receivedKey, String unreadKey) {
        Iterator<String> messageUnreadId = accounts.getMessageMap(username, unreadKey);
        presenter.printAllMessages(messages.getReceivedMessages(messageUnreadId, sorter),
                "The unread message:",
                "All messages have been reviewed");
        messageUnreadId = accounts.getMessageMap(username, unreadKey);

        updateMessageStatus(messageUnreadId, username, receivedKey, unreadKey);
    }

    /**
     * Displays the archived message
     * @param username A String representation of current user's username
     * @param archiveKey A String representation of archived message key
     */
    public void getArchivedMessages(String username, String archiveKey) {
        Iterator<String> messageArchived = accounts.getMessageMap(username, archiveKey);
        presenter.printAllMessages(messages.getArchivedMessages(messageArchived),
                "The archived message:",
                "There is no archived message");
    }

    //view a received message from a specific user

    /**
     * Displays the message received from specific account
     * @param currUsername A String representation of current user's username
     * @param receivedKey A String representation of received message key
     * @param unreadKey A String representation of unread message key
     */
    public void getReceivedMessagesFromSpecificAccount(String currUsername, String receivedKey, String unreadKey) {
        String sender = this.inputUsername(false);
        if (sender == null) {
            // Output error message
            presenter.accountDoesNotExist();
            return;
        }
        Iterator<String> messageReceivedIdLst = accounts.getMessageMap(currUsername, receivedKey);
        this.getUnreadMessageFromSpecificAccountWithUsername(currUsername, sender, receivedKey, unreadKey);
        presenter.printAllMessages(messages.getReceivedMessagesFromSpecificAccount(sender, messageReceivedIdLst, sorter),
                "The messages you have received from " + sender + " are:",
                "You haven't reviewed any message from " + sender + " yet.");
    }

    /**
     * displays the unread message from specific account
     * @param currUsername A String representation of current user's username
     * @param receivedKey A String representation of received message key
     * @param unreadKey A String representation of unread message key
     */
    public void getUnreadMessageFromSpecificAccount(String currUsername, String receivedKey, String unreadKey) {
        String sender = this.inputUsername(false);
        if (sender == null) {
            // Output error message
            presenter.accountDoesNotExist();
            return;
        }
        this.getUnreadMessageFromSpecificAccountWithUsername(currUsername, sender, receivedKey, unreadKey);
    }

    private void getUnreadMessageFromSpecificAccountWithUsername(String currUsername, String sender,
                                                     String receivedKey, String unreadKey) {
        Iterator<String> messageUnreadId = accounts.getMessageMap(currUsername, unreadKey);
        presenter.printAllMessages(messages.getReceivedMessagesFromSpecificAccount(sender, messageUnreadId, sorter),
                "The message unread from " + sender + " are:",
                "You have reviewed all messages from " + sender);
        messageUnreadId = accounts.getMessageMap(currUsername, unreadKey);
        updateMessageStatus(messageUnreadId, currUsername, receivedKey, unreadKey);
    }

    private void updateMessageStatus(Iterator<String> messageUnreadId, String username, String receivedKey,
                                     String unreadKey) {
        String id;
        while (messageUnreadId.hasNext()) {
            id = messageUnreadId.next();
            messageUnreadId.remove();
            accounts.removeMessageMap(username, unreadKey, id);
            accounts.updateMessageMap(username, receivedKey, id);
        }
    }

    //view a sent message from a specific user

    /**
     * Displays the message sent to a specific account
     * @param currUsername A String representation of current user's username
     * @param key A String representation of message status key
     */
    public void getSentMessageToSpecificAccount(String currUsername, String key) {
        String receiver = this.inputUsername(true);
        if (receiver == null) {
            // Output error message
            presenter.accountDoesNotExist();
            return;
        }
        Iterator<String> messageId = accounts.getMessageMap(currUsername, key);
        presenter.printAllMessages(messages.getSentMessageToSpecificAccount(receiver, messageId, sorter),
                "The messages you have sent to " + receiver + " are:",
                "You haven't sent any message to " + receiver + " yet.");
    }

    private void setSorter(MessageSorter newSorter) {
        this.sorter = newSorter;
    }

    /**
     * Asks user to input the displaying order
     */
    public void setDisplayingOrder() {
        Scanner c = new Scanner(System.in);
        presenter.currentOrder(this.sorter.getOrderMessage());
        presenter.printDisplayOrderMenu();
        String input = c.nextLine();
        switch(input) {
            case "0":
                MessageSorter msn = new MessageNewestToOldest();
                this.setSorter(msn);
                presenter.setMessageOrder(this.sorter.getOrderMessage());
                break;
            case "1":
                MessageSorter mso = new MessageOldestToNewest();
                this.setSorter(mso);
                presenter.setMessageOrder(this.sorter.getOrderMessage());
                break;
            case "r":
                presenter.printRedoMessage("Return to the Message System");
                break;
            default:
                presenter.printInvalidInput();
        }
    }
}
