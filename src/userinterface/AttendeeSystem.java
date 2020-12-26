package userinterface;

import account.AccountEventSystem;
import account.AccountManager;
import event.EventManager;
import message.MessageSearchingSystem;
import message.MessagingManager;
import room.RoomManager;

import java.util.Scanner;

/**
 * AttendeeSystem is an controller class of attendees.
 * AttendeeSystem stores the username of the logged in account.
 * The methods in AttendeeSystem interact with presenter, display the menu for the attendee to operate his/her
 * message, event, room, account systems.
 * @author Group0694
 * @version 2.0.0
 */
class AttendeeSystem extends UserSystem {
    @SuppressWarnings("FieldMayBeFinal")
    private AttendeePresenter attendeePresenter;

    /**
     * Creates an attendee system with the specified username and use case classes
     * @param username The username of the account logged in
     * @param messagingManager A copy of the MessagingManager use case
     * @param accountManager A copy of the AccountManager use case
     */
    public AttendeeSystem(String username, MessagingManager messagingManager,
                          AccountManager accountManager){
        super(username, messagingManager, accountManager);
        attendeePresenter = new AttendeePresenter();
    }

    /**
     * Lets attendees choose the message viewing options
     * @param mss An instance of MessageSearchingSystem
     */
    @Override
    protected void messageInboxOption(MessageSearchingSystem mss) {
        Scanner c = new Scanner(System.in);
        String input;
        for(;;) {
            attendeePresenter.printMessageInboxMenu();
            input =  c.nextLine();
            if (!super.generalMessageInboxOption(input, mss)) {
                if ("r".equals(input)) {
                    return;
                } else {
                    attendeePresenter.printInvalidInput();
                }
            }
        }
    }

    /**
     * Lets attendees choose the message sending and status options
     */
    @Override
    protected void messageAccessOption() {
        Scanner c = new Scanner(System.in);
        String input;
        for(;;) {
            attendeePresenter.printMessageAccessMenu();
            input = c.nextLine();
            if (!super.generalMessageAccessOption(input)) {
                switch(input) {
                    case "6":
                        new AccountEventSystem(accountManager).getSpeakerOfSignedEvents(this.username, eventManager);
                        break;
                    case "r":
                        return;
                    default:
                        attendeePresenter.printInvalidInput();
                }
            }
        }
    }

    /**
     * Lets attendees choose the event schedule options
     */
    protected void eventScheduleOption() {
        Scanner c = new Scanner(System.in);
        String input;
        for(;;) {
            attendeePresenter.printEventScheduleMenu();
            input =  c.nextLine();
            if (!super.generalEventScheduleOption(input)) {
                if ("r".equals(input)) {
                    return;
                } else {
                    attendeePresenter.printInvalidInput();
                }
            }
        }
    }
    /**
     * Lets attendees choose the event attending options
     */
    @Override
    protected void eventAccessOption() {
        Scanner c = new Scanner(System.in);
        String input;
        for(;;) {
            attendeePresenter.printEventAccessMenu();
            input = c.nextLine();
            if (!super.generalEventAccessOption(input)) {
                if ("r".equals(input)) {
                    return;
                } else {
                    attendeePresenter.printInvalidInput();
                }
            }
        }
    }

    /**
     * Lets attendees choose the account options
     */
    @Override
    protected void accountOption() {
        Scanner c = new Scanner(System.in);
        String input;
        for(;;) {
            attendeePresenter.printAccountMenu();
            input = c.nextLine();
            if (!super.generalAccountOption(input)) {
                switch (input) {
                    case "r":
                        return;
                    default:
                        attendeePresenter.printInvalidInput();
                }
            }
        }
    }
}