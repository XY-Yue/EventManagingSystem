package userinterface;

import java.util.List;

/**
 * This class is an presenter class of SpeakerSystem. It extends the abstract class UserPresenter.
 * This class displays the message to the speakerSystem about what selections can be performed.
 * @author Group0694
 * @version 2.0.0
 */
class SpeakerPresenter extends UserPresenter {

    /**
     * Displays the description of additional message status and sending options basing on speaker's authority.
     */
    @Override
    void printMessageAccessMenu() {
        List<String> options = super.getGeneralMessageAccessOptionMenu();
        options.add("send messages to all attendees for one or more events");
        super.printMenu(options, super.getReturnToMessage());
    }

    /**
     * Displays the description of additional event attending options basing on speaker's authority.
     */
    @Override
    void printEventAccessMenu() {
        List<String> options = super.getGeneralEventAccessOptionMenu();
        options.add("view a list of events giving by you");
        super.printMenu(options, super.getReturnToMessage());
    }
}
