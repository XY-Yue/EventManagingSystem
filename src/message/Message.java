package message;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.sql.Timestamp;

/**
 * This class is an entity class of messaging feature.
 * This is an abstract class of methods, all different kinds of message class will extend this class.
 * All the constructed messages are already sent by its sender.
 * For most of the attributes in this class there is no setter method because the sent message should not be changed.
 * @author Group0694
 * @version 2.0.0
 */
abstract class Message implements Serializable {
    private final String senderUsername;
    private final List<String> receiverUsername;
    private final Timestamp time;
    private final String id;

    /**
     * Constructs a Message object
     * @param sender A String representation of sender username
     * @param receiver A String representation of receiver username
     * @param id A String representation of message id
     * @param time Time of this message constructed
     */
    Message(String sender, List<String> receiver, String id, Timestamp time) {
        senderUsername = sender;
        receiverUsername = receiver;
        this.time = time;
        this.id = id;
    }

    /**
     * Gets sender's username of this message.
     * @return sender's username of the message
     */
    protected String getSenderUsername() {
        return senderUsername;
    }

    /**
     * Gets the message constructed time
     * @return Time of this message constructed
     */
    protected Timestamp getTime() {
        return this.time;
    }

    /**
     * Gets receivers' usernames of this message.
     * @return An iterator of receiver usernames
     */
    protected Iterator<String> getReceiverUsername() {
        return receiverUsername.iterator();
    }

    /**
     * Gets the id of this message
     * @return Id of this message
     */
    protected String getId() {
        return id;
    }

    /**
     * Represents toString information of this message.
     * This is an abstract function which gives a String version of the message which is for the receiver of the message.
     * It is abstract because different kinds of messages may have different format of content and subject.
     * @param username the username of the checking user, which is one of the receivers of the message
     * @return the string information of message without receiver.(Receiver view of message)
     */
    protected abstract String toStringReceived(String username);
}
