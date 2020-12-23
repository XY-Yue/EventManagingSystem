package message;

import message.Message;

import java.io.Serializable;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;

/**
 * An entity class for messaging feature.
 * A class extends abstract class Message.
 * Represents messages which have their subject and content in words. Hence this class is called WordMessage.
 * All the constructed messages are already sent by its sender.
 * For most of the attributes in this class there is no setter method because the sent message should not be changed.
 * @author Group0694
 * @version 2.0.0
 */
class WordMessage extends Message implements Serializable {
    private final String subject;
    private final String content;

    /**
     * Constructs a WorldMessage object
     * @param sender A String representation of the sender username
     * @param receiver A String representation of the receiver username
     * @param id A String representation of the message id
     * @param time Time of this message constructed
     * @param subject A String representation of the subject of this message
     * @param WordContent A String representation of the content of this message
     */
    public WordMessage(String sender, List<String> receiver, String id,
                       Timestamp time, String subject, String WordContent) {
        super(sender, receiver, id, time);
        content = WordContent;
        this.subject = subject;
    }

    /**
     * Represents toString information of this message. It contains the name of sender and receivers, the subject and content.
     * Should be used when the sender checks the message information.
     * @return the string of information of the message.(Sender view of message)
     */
    @Override
    public String toString(){
        List<String> lst = new ArrayList<>();
        Iterator<String> receiverIterator = getReceiverUsername();
        while (receiverIterator.hasNext()) {
            lst.add(receiverIterator.next());
        }
        String receiverName = String.join(", ", lst);
        String timeString = printTime(this.getTime());
        return "Message Id: " + getId() + "\n" +
                "Sender: " + this.getSenderUsername() + "\n" +
                "At the time: " + timeString.substring(0, timeString.length() - 4) + "\n" +
                "Receiver: " + receiverName + "\n" +
                "Subject: " + subject + "\n" +
                "Content: " + content + "\n";
    }

    // Method for received message, we don't need all of the receivers

    /**
     * Represents toString information of this message. It contains the name of sender only, the subject and content.
     * Should be used when the checking user is one of the receivers, so this user doesn't need to know
     * all the receivers.
     * @param username the username of the checking user, which is one of the receivers of the message
     * @return the string information of message without receiver.(Receiver view of message)
     */
    @Override
    public String toStringReceived(String username) {
        String timeString = printTime(this.getTime());
        return "Message Id: " + getId() + "\n" +
                "Received from: " + username + "\n" +
                "At the time: " + timeString.substring(0, timeString.length() - 5) + "\n" +
                "Subject: " + subject + "\n" +
                "Content: " + content + "\n";
    }

    private String printTime(Timestamp time) {
        Date date = new Date(time.getTime());
        return DateFormat.getDateTimeInstance(DateFormat.LONG, DateFormat.LONG).format(date);
    }
}