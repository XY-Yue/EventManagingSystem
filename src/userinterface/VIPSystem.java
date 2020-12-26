package userinterface;

import account.AccountEventSystem;
import account.AccountManager;
import event.EventManager;
import event.EventSystem;
import message.MessageSearchingSystem;
import message.MessagingManager;
import room.RoomManager;

import java.util.Scanner;

/**
 * This controller class represents a VIP system, which is a child class of abstract class UserSystem.
 * This class stores the username of the logged in account.
 * It also stores a VIPPresenter class which can print information for VIPSystem
 * @author Group0694
 * @version 2.0.0
 */
class VIPSystem extends UserSystem {

    VIPPresenter presenter;

    /**
     * Creates an VIP system with the specified username and use case classes
     * @param username The username of the account logged in
     * @param messagingManager A copy of the MessagingManager use case
     * @param accountManager A copy of the AccountManager use case
     */
    VIPSystem(String username, MessagingManager messagingManager,
              AccountManager accountManager) {
        super(username, messagingManager, accountManager);
        presenter = new VIPPresenter();
    }

    /**
     * Lets VIP choose the message viewing options
     * @param mss An instance of MessageSearchingSystem
     */
    @Override
    protected void messageInboxOption(MessageSearchingSystem mss) {
        Scanner c = new Scanner(System.in);
        String input;
        for(;;) {
            presenter.printMessageInboxMenu();
            input =  c.nextLine();
            if (!super.generalMessageInboxOption(input, mss)) {
                if ("r".equals(input)) {
                    return;
                } else {
                    presenter.printInvalidInput();
                }
            }
        }
    }

    /**
     * Lets VIP choose the message status and sending options
     */
    @Override
    protected void messageAccessOption() {
        Scanner c = new Scanner(System.in);
        String input;
        for(;;) {
            presenter.printMessageAccessMenu();
            input = c.nextLine();
            if (!super.generalMessageAccessOption(input)) {
                switch(input) {
                    case "6":
                        new AccountEventSystem(accountManager).getSpeakerOfSignedEvents(this.username, eventManager);
                        break;
                    case "r":
                        return;
                    default:
                        presenter.printInvalidInput();
                }
            }
        }
    }

    /**
     * Lets VIP choose the event schedule viewing options
     */
    protected void eventScheduleOption() {
        Scanner c = new Scanner(System.in);
        String input;
        for(;;) {
            presenter.printEventScheduleMenu();
            input =  c.nextLine();
            if (!super.generalEventScheduleOption(input)) {
                AccountEventSystem accountEventSystem = new AccountEventSystem(accountManager);
                switch(input) {
                    case "6":
                        new EventSystem(eventManager).printEventList(true);
                        break;
                    case "7":
                        accountEventSystem.viewEventsAttendable(username, eventManager, true);
                        break;
                    case "8":
                        accountEventSystem.getSpecialListEvents(username);
                        break;
                    case "r":
                        return;
                    default:
                        presenter.printInvalidInput();
                }
            }
        }
    }

    /**
     * Lets VIP choose the event attending options
     */
    @Override
    protected void eventAccessOption() {
        Scanner c = new Scanner(System.in);
        String input;
        for(;;) {
            presenter.printEventAccessMenu();
            input = c.nextLine();
            if (!super.generalEventAccessOption(input)) {
                switch(input) {
                    case "r":
                        return;
                    default:
                        presenter.printInvalidInput();
                }
            }
        }
    }

    /**
     * Lets VIP choose the account viewing options
     */
    @Override
    protected void accountOption() {
        Scanner c = new Scanner(System.in);
        String input;
        for(;;) {
            presenter.printAccountMenu();
            input = c.nextLine();
            if (!super.generalAccountOption(input)) {
                switch (input) {
                    case "r":
                        return;
                    default:
                        presenter.printInvalidInput();
                }
            }
        }
    }
}
