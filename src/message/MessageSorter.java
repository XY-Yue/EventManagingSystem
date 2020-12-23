package message;

import java.util.List;
/**
 * An interface that sorts the message
 * @author Group0694
 * @version 2.0.0
 */
interface MessageSorter {
    /**
     * Sorts the message
     * @param lst A List of Message
     */
    void sortMessage(List<Message> lst);

    /**
     * Gets the String representation describing its behaviour
     * @return A String representation of its behaviour
     */
    String getOrderMessage();
}
