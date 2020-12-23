package userinterface;

import account.AccountEventSystem;
import account.AccountManager;
import account.AccountSystem;
import event.EventManager;
import event.EventSchedulerSystem;
import message.MessageSearchingSystem;
import message.MessageSendingSystem;
import message.MessagingManager;
import room.RoomManager;
import room.RoomSystem;
import java.util.Scanner;

/**
 * This class is an controller class of organizer.
 * This class stores the username of the logged in account.
 * The methods in OrganizerSystem interact with presenter, display the specific menu for the organizer to operate
 * his/her message, event, room, account systems.
 * @author Group0694
 * @version 2.0.0
 */
class OrganizerSystem extends UserSystem {
    protected OrganizerPresenter organizerPresenter;

    /**
     * Creates an organizer system with the specified username and use case classes
     * @param username The username of the account logged in
     * @param messagingManager A copy of the MessagingManager use case
     * @param eventManager A copy of the EventManager use case
     * @param roomManager A copy of the RoomManager use case
     * @param accountManager A copy of the AccountManager use case
     */
    OrganizerSystem(String username, MessagingManager messagingManager,
                    EventManager eventManager, RoomManager roomManager,
                    AccountManager accountManager) {
        super(username, messagingManager, eventManager, roomManager, accountManager);
        this.organizerPresenter = new OrganizerPresenter();
    }

    /**
     * Lets organizers choose the message viewing options
     * @param mss An instance of MessageSearchingSystem
     */
    @Override
    protected void messageInboxOption(MessageSearchingSystem mss) {
        Scanner c = new Scanner(System.in);
        String input;
        for(;;) {
            organizerPresenter.printMessageInboxMenu();
            input =  c.nextLine();
            if (!super.generalMessageInboxOption(input, mss)) {
                if ("r".equals(input)) {
                    return;
                } else {
                    organizerPresenter.printInvalidInput();
                }
            }
        }
    }

    /**
     * Lets organizers choose the message sending and status options
     */
    @Override
    protected void messageAccessOption() {
        Scanner c = new Scanner(System.in);
        MessageSendingSystem messageSendingSystem = new MessageSendingSystem(accountManager, messagingManager);
        String input;
        for (; ; ) {
            organizerPresenter.printMessageAccessMenu();
            input = c.nextLine();
            if (!super.generalMessageAccessOption(input)) {
                switch (input) {
                    case "6":
                        new AccountSystem(accountManager).getSpeakerList();
                        break;
                    case "7":
                        messageSendingSystem.sendMessageToSpeakers(this.username);
                        break;
                    case "8":
                        messageSendingSystem.sendMessageToAttendees(this.username);
                        break;
                    case "r":
                        return;
                    default:
                        organizerPresenter.printInvalidInput();
                }
            }
        }
    }

    /**
     * Lets organizers choose the event schedules options
     */
    protected void eventScheduleOption() {
        Scanner c = new Scanner(System.in);
        String input;
        for(;;) {
            organizerPresenter.printEventScheduleMenu();
            input =  c.nextLine();
            if (!super.generalEventScheduleOption(input)) {
                if ("r".equals(input)) {
                    return;
                } else {
                    organizerPresenter.printInvalidInput();
                }
            }
        }
    }

    /**
     * Lets organizers choose the event attending options
     */
    @Override
    protected void eventAccessOption() {
        Scanner c = new Scanner(System.in);
        String input;
        for(;;) {
            organizerPresenter.printEventAccessMenu();
            input = c.nextLine();
            if (!super.generalEventAccessOption(input)) {
                EventSchedulerSystem eventSchedulerSystem = new EventSchedulerSystem(roomManager);
                AccountEventSystem accountEventSystem = new AccountEventSystem(accountManager);
                RoomSystem roomSystem = new RoomSystem(roomManager);
                switch(input) {
                    case "3":
                        accountEventSystem.getSpecialListEvents(this.username);
                        break;
                    case "4":
                        eventSchedulerSystem.cancelEvent(eventManager, accountManager);
                        break;
                    case "5":
                        eventSchedulerSystem.modifyEvent(eventManager, accountManager);
                        break;
                    case "6":
                        eventSchedulerSystem.createEvent(this.username, accountManager, eventManager);
                        break;
                    case "7":
                        roomSystem.getRoomsInfo();
                        break;
                    case "8":
                        roomSystem.addRoom();
                        break;
                    case "r":
                        return;
                    default:
                        organizerPresenter.printInvalidInput();
                }
            }
        }
    }

    /**
     * Lets organizers choose the account options
     */
    @Override
    protected void accountOption() {
        Scanner c = new Scanner(System.in);
        String input;
        for(;;) {
            organizerPresenter.printAccountMenu();
            input = c.nextLine();
            if (!super.generalAccountOption(input)) {
                switch(input) {
                    case "5":
                        new AccountSystem(accountManager).addAccount();
                        break;
                    case "6":
                        new AccountSystem(accountManager).upgradeAttendee(this.getMessageStatus());
                        break;
                    case "7":
                        new AccountSystem(accountManager).degradeVIP(eventManager, this.getMessageStatus());
                        break;
                    case "r":
                        return;
                    default:
                        organizerPresenter.printInvalidInput();
                }

            }
        }
    }
}
