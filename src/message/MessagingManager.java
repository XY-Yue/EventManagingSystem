package message;

import java.io.Serializable;
import java.sql.Timestamp;
import java.util.*;

/**
 * An use case class of messaging feature.
 * Stores all sent messages in a HashMap, which has username of all users map to their sent messages.
 * Contains constructor of Message, this class should be used to construct new message.
 * Methods in this class contains all the operations to a message.
 * Input information is given by Message controller system.
 * @author Group0694
 * @version 2.0.0
 */
public class MessagingManager implements Serializable {
    private final Map<String, Message> messageMap = new HashMap<>();
    private int numMessage = 0;
    // username map a list of message sent

    /**
     * Returns the all the messages sent by the user with given username.
     * Get a list of messages by getting the list of messages the username mapped to in hashmap messageMap.
     * The result should be in view of user with given username, so the toString should be in sender's view.
     * @param messageSent An iterator message sent
     * @param sorter A MessageSorter object that sorts the message
     * @return a list of sorted toString of messages those are all sent by the user with given username in sender's view
     */
    List<String> getSentMessages(Iterator<String> messageSent, MessageSorter sorter) {
        // Assume messageSent is not null
        List<String> lst = new ArrayList<>();
        for (Message m:  getSortMessage(messageSent, sorter)) {
            if (m != null) {
                lst.add(m.toString());
            }
        }
        return lst;
    }

    /**
     * Gets the message sent to a specific user
     * @param receiverUsername A String representation of receiver username
     * @param messageSent An iterator of message id sent by the current user
     * @param sorter A MessageSorter object that sorts the message
     * @return A sorted String representation of the message sent to the given username
     */
    List<String> getSentMessageToSpecificAccount(String receiverUsername, Iterator<String> messageSent,
                                                           MessageSorter sorter) {
        List<String> lst = new ArrayList<>();
        for (Message m : getSortMessage(messageSent, sorter)) {
            Iterator<String> receiverLst = m.getReceiverUsername();
            String receiverName;
            while (receiverLst.hasNext()) {
                receiverName = receiverLst.next();
                if(receiverName.equals(receiverUsername))
                    lst.add(m.toString());
            }
        }
        return lst;
    }

    // Not a helper method

    /**
     * Gets the message received from a specific account
     * @param senderUsername A String representation of the sender username
     * @param messageReceived An iterator of message id received by current user
     * @param sorter A MessageSorter object that sorts the message
     * @return A sorted list of received messages from the sender
     */
    List<String> getReceivedMessagesFromSpecificAccount(String senderUsername,
                                                        Iterator<String> messageReceived, MessageSorter sorter) {
        List<String> lst = new ArrayList<>();
        for (Message m : getSortMessage(messageReceived, sorter)) {
            if (m.getSenderUsername().equals(senderUsername)) {
                lst.add(m.toStringReceived(senderUsername));
            }
        }
        return lst;
    }

    private List<Message> getSortMessage(Iterator<String> messageId, MessageSorter sorter) {
        List<Message> lst = new ArrayList<>();
        String id;
        while (messageId.hasNext()) {
            id = messageId.next();
            lst.add(messageMap.get(id));
        }
        sorter.sortMessage(lst);
        return lst;
    }

    /**
     * Gets the message received from all other accounts
     * @param messageReceived An iterator of message id received by current suer
     * @param sorter A MessageSorter object that sorts the message
     * @return A sorted list of received messages from all other accounts
     */
    List<String> getReceivedMessages(Iterator<String> messageReceived, MessageSorter sorter) {
        List<String> lst = new ArrayList<>();
        List<Message> messageList = getSortMessage(messageReceived, sorter);
        for (Message m : messageList) {
            lst.add(m.toStringReceived(m.getSenderUsername()));
        }
        return lst;
    }

    /**
     * Gets the archived message of current account
     * @param messageArchived An iterator of message id archived
     * @return A list of archived messages
     */
    List<String> getArchivedMessages(Iterator<String> messageArchived) {
        List<String> lst = new ArrayList<>();
        String id;
        while(messageArchived.hasNext()) {
            // Received list
            id = messageArchived.next();
            if (id.charAt(0) == '0') {
                Message m = messageMap.get(id.substring(1));
                lst.add(m.toStringReceived(m.getSenderUsername()));
            } else if (id.charAt(0) == '1') {
                Message m = messageMap.get(id.substring(1));
                lst.add(m.toString());
            }
        }
        return lst;
    }

    /**
     * Sends the message to other accounts
     * @param senderUsername A String representation of sender username
     * @param receivers A List of String representation of receiver username
     * @param subject Subject of this message
     * @param content Content of this message
     * @return A String representation of this unique id
     */
    // NOTE: this method looks like this because we currently only have one type of message and a Factory is not needed.
    // A factory like the one for Account will be (and can easily be) added if more types of messages are added.
    String sendWordMessage(String senderUsername, List<String> receivers, String subject, String content) {
        // List of receivers will require AccountManager, either check username exist or get a list of Speaker
        // In controller level, you SHOULD check all username when you ask user to input receiver username
        Timestamp sendTime = new Timestamp(new Date().getTime());
        String messageId = "M" + numMessage;
        Message newMessage = new WordMessage(senderUsername, receivers, messageId, sendTime, subject, content);

        // Update to the message map
        messageMap.put(messageId, newMessage);
        // Update message id
        numMessage ++;
        return messageId; // It will return message id
    }

}
