package message;

import java.util.Comparator;
import java.util.List;

/**
 * A class implements MessageSorter interface, will sort message from newest to oldest
 * @author Group0694
 * @version 2.0.0
 */
class MessageNewestToOldest implements MessageSorter {
    /**
     * Sorts the message from Newest to Oldest
     * @param lst A List of Message
     */
    @Override
    public void sortMessage(List<Message> lst) {
        lst.sort(Comparator.comparing(Message::getTime).reversed());
    }

    /**
     * Gets the String representation describing its behaviour
     * @return A String representation of its behaviour
     */
    @Override
    public String getOrderMessage() {
        return "Newest to Oldest";
    }
}
