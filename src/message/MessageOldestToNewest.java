package message;

import java.util.List;
import java.util.Comparator;

/**
 * A class implements MessageSorter interface, will sort message from oldest to newest
 * @author Group0694
 * @version 2.0.0
 */
public class MessageOldestToNewest implements MessageSorter {
    /**
     * Sorts the message from Oldest to Newest
     * @param lst A List of Message
     */
    @Override
    public void sortMessage(List<Message> lst) {
        lst.sort((Comparator.comparing(Message::getTime)));
    }

    /**
     * Gets the String representation describing its behaviour
     * @return A String representation of its behaviour
     */
    @Override
    public String getOrderMessage() {
        return "Oldest to Newest";
    }
}
