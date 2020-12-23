package userinterface;

import java.util.List;

/**
 * AttendeePresenter is an presenter class of attendee.
 * AttendeePresenter displays the message to the attendee users about what selections they can perform.
 * @author Group0694
 * @version 2.0.0
 */
class AttendeePresenter extends UserPresenter {

    /**
     * Displays the description of additional message status and sending options basing on attendee's authority.
     */
    @Override
    void printMessageAccessMenu() {
        List<String> options = super.getGeneralMessageAccessOptionMenu();
        options.add("view Speaker contact information of signed up events");
        super.printMenu(options, super.getReturnToMessage());
    }
}
