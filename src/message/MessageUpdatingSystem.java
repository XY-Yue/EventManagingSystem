package message;

import account.AccountManager;
import userinterface.UserSystem.MessageStatus;
import java.util.Scanner;

/**
 * An upper controller of message, mostly in charge of updating message status
 * Stores MessagePresenter, AccountManager class
 * Gets input from user and gives corresponding moves by calling methods from AccountManager
 * @author Group0694
 * @version 2.0.0
 */
public class MessageUpdatingSystem {
    private final AccountManager accounts;
    private final MessagePresenter presenter;

    /**
     * Constructs a MessageUpdatingSystem object
     * @param accounts An instance of AccountManager
     */
    public MessageUpdatingSystem(AccountManager accounts) {
        this.accounts = accounts;
        this.presenter = new MessagePresenter();
    }

    /**
     * Archive the message
     * @param currUsername A String representation of current user's username
     */
    public void archiveMessage(String currUsername) {
        Scanner c = new Scanner(System.in);
        presenter.askMessageID();
        String messageId = c.nextLine();
        // Is unread message (however when putting back it should be in received list
        if (accounts.isValidMessageId(currUsername, messageId, MessageStatus.UNREAD.toString())) {
            String newMessageId = "0" + messageId; // 0 means it's in received list
            // Remove from unread list and update to archive list
            updateMessageStatus(currUsername, messageId, newMessageId, MessageStatus.ARCHIVE.toString(),
                    MessageStatus.UNREAD.toString());
            presenter.printActionMessageId(messageId, "Archive");
        } else if (accounts.isValidMessageId(currUsername, messageId, MessageStatus.RECEIVED.toString())) {
            // Received Status
            String newMessageId = "0" + messageId;
            updateMessageStatus(currUsername, messageId, newMessageId, MessageStatus.ARCHIVE.toString(),
                    MessageStatus.RECEIVED.toString());
            presenter.printActionMessageId(messageId, "Archive");
        } else if (accounts.isValidMessageId(currUsername, messageId, MessageStatus.SENT.toString())) {
            // Sent Status
            String newMessageId = "1" + messageId;
            updateMessageStatus(currUsername, messageId, newMessageId, MessageStatus.ARCHIVE.toString(),
                    MessageStatus.SENT.toString());
            presenter.printActionMessageId(messageId, "Archive");
        } else {
            presenter.printInvalidMessageId(messageId);
        }
    }

    /**
     * Unarchive the message
     * @param currUsername A String representation of the current user's username
     */
    public void unarchiveMessage(String currUsername) {
        Scanner c = new Scanner(System.in);
        presenter.askMessageID();
        String messageId = c.nextLine();
        // Put in received message Id
        String newMessageId = "0" + messageId;
        if (accounts.isValidMessageId(currUsername, newMessageId, MessageStatus.ARCHIVE.toString())) {
            updateMessageStatus(currUsername, newMessageId, messageId, MessageStatus.RECEIVED.toString(),
                    MessageStatus.ARCHIVE.toString());
            presenter.printActionMessageId(messageId, "Unarchive");
            return;
        }
        newMessageId = "1" + messageId;
        if (accounts.isValidMessageId(currUsername, newMessageId, MessageStatus.ARCHIVE.toString())) {
            updateMessageStatus(currUsername, newMessageId, messageId, MessageStatus.SENT.toString(),
                    MessageStatus.ARCHIVE.toString());
            presenter.printActionMessageId(messageId, "Unarchive");
            return;
        }
        presenter.printInvalidMessageId(messageId);
    }

    /**
     * Mark a received message as unread
     * @param currUsername A String representation of current user's username
     */
    public void markAsUnread(String currUsername) {
        Scanner c = new Scanner(System.in);
        presenter.askMessageID();
        String messageId = c.nextLine();
        if (accounts.isValidMessageId(currUsername, messageId, MessageStatus.RECEIVED.toString())) {
            updateMessageStatus(currUsername, messageId, messageId, MessageStatus.UNREAD.toString(),
                    MessageStatus.RECEIVED.toString());
            presenter.printActionMessageId(messageId, "Unread");
        } else {
            presenter.printInvalidMessageId(messageId);
        }
    }

    /**
     * Delete the message
     * @param currUsername A String representation of the current user's username
     */
    public void deleteMessage(String currUsername) {
        Scanner c = new Scanner(System.in);
        presenter.askMessageID();
        String messageId = c.nextLine();
        if (messageId.charAt(0) == 'M') {
            for (MessageStatus ms : MessageStatus.values()) {
                if (accounts.isValidMessageId(currUsername, messageId, ms.toString())) {
                    accounts.removeMessageMap(currUsername, ms.toString(), messageId);
                    presenter.printActionMessageId(messageId, "Delete (" + ms.toString() + ")");
                    return;
                }
            }
        }
        presenter.printInvalidMessageId(messageId);
    }

    private void updateMessageStatus(String username, String messageId, String newMessageId, String updateKey, String removeKey) {
        accounts.removeMessageMap(username, removeKey, messageId);
        accounts.updateMessageMap(username, updateKey, newMessageId);
    }
}
