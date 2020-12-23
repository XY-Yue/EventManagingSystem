package userinterface;

import java.util.List;

/**
 * OrganizerPresenter is an presenter class of organizer.
 * OrganizerPresenter displays the message to the organizer users about what selections they can perform.
 * @author Group0694
 * @version 2.0.0
 */
class OrganizerPresenter extends UserPresenter {

    /**
     * Displays the description of additional message status and sending options basing on organizer's authority.
     */
    @Override
    void printMessageAccessMenu() {
        List<String> options = super.getGeneralMessageAccessOptionMenu();
        options.add("view all Speaker contact information");
        options.add("send messages to all Speakers");
        options.add("send messages to all Attendees");
        super.printMenu(options, super.getReturnToMessage());
    }

    /**
     * Displays the description of additional event attending options basing on organizer's authority.
     */
    @Override
    void printEventAccessMenu() {
        List<String> options = super.getGeneralEventAccessOptionMenu();
        options.add("view all events created by you");
        options.add("cancel an event");
        options.add("modify an event");
        options.add("create an event");
        options.add("view all rooms");
        options.add("add a room");
        super.printMenu(options, super.getReturnToMessage());
    }

    /**
     * Displays the description of additional account option basing on organizer's authority.
     */
    @Override
    void printAccountMenu() {
        List<String> options = super.getGeneralAccountOptionMenu();
        options.add("create a new account for someone else");
        options.add("upgrade an attendee account to a VIP");
        options.add("degrade a VIP to an attendee account");
        super.printMenu(options, super.getReturnToMessage());
    }
}
