package userinterface;

import account.AccountEventSystem;
import account.AccountManager;
import event.EventManager;
import message.MessageSearchingSystem;
import message.MessageSendingSystem;
import message.MessagingManager;
import room.RoomManager;

import java.util.Scanner;

/**
 * This controller class represents a speaker system, which is a child class of abstract class UserSystem.
 * This class stores the username of the logged in account.
 * It also stores a SpeakerPresenter class which can print information for SpeakerSystem
 * @author Group0694
 * @version 2.0.0
 */
class SpeakerSystem extends UserSystem {
    protected SpeakerPresenter speakerPresenter;

    /**
     * Creates an speaker system with the specified username and use case classes
     * @param username The username of the account logged in
     * @param messagingManager A copy of the MessagingManager use case
     * @param accountManager A copy of the AccountManager use case
     */
    SpeakerSystem(String username, MessagingManager messagingManager,
                  AccountManager accountManager) {
        super(username, messagingManager, accountManager);
        speakerPresenter = new SpeakerPresenter();
    }

    /**
     * Lets speakers choose the message viewing options
     * @param mss An instance of MessageSearchingSystem
     */
    @Override
    protected void messageInboxOption(MessageSearchingSystem mss) {
        Scanner c = new Scanner(System.in);
        String input;
        for(;;) {
            speakerPresenter.printMessageInboxMenu();
            input =  c.nextLine();
            if (!super.generalMessageInboxOption(input, mss)) {
                if ("r".equals(input)) {
                    return;
                } else {
                    speakerPresenter.printInvalidInput();
                }
            }
        }
    }

    /**
     * Lets speakers choose the message status and sending options
     */
    @Override
    protected void messageAccessOption() {
        Scanner c = new Scanner(System.in);
        String input;
        for(;;) {
            speakerPresenter.printMessageAccessMenu();
            input = c.nextLine();
            if (!super.generalMessageAccessOption(input)) {
                switch(input) {
                    case "6":
                        new MessageSendingSystem(accountManager, messagingManager).
                                sendMessageToAttendeesInEvent(this.username, eventManager);
                        break;
                    case "r":
                        return;
                    default:
                        speakerPresenter.printInvalidInput();
                }
            }
        }
    }


    /**
     * Lets speakers choose the event schedule options
     */
    protected void eventScheduleOption() {
        Scanner c = new Scanner(System.in);
        String input;
        for(;;) {
            speakerPresenter.printEventScheduleMenu();
            input =  c.nextLine();
            if (!super.generalEventScheduleOption(input)) {
                if ("r".equals(input)) {
                    return;
                } else {
                    speakerPresenter.printInvalidInput();
                }
            }
        }
    }

    /**
     * Lets speakers choose the event attending options
     */
    @Override
    protected void eventAccessOption() {
        Scanner c = new Scanner(System.in);
        String input;
        for(;;) {
            speakerPresenter.printEventAccessMenu();
            input = c.nextLine();
            if (!super.generalEventAccessOption(input)) {
                switch(input) {
                    case "3":
                        new AccountEventSystem(accountManager).getSpecialListEvents(this.username);
                        break;
                    case "r":
                        return;
                    default:
                        speakerPresenter.printInvalidInput();
                }
            }
        }
    }


    /**
     * Lets speakers choose the account viewing options
     */
    @Override
    protected void accountOption() {
        Scanner c = new Scanner(System.in);
        String input;
        for (;;) {
            speakerPresenter.printAccountMenu();
            input = c.nextLine();
            if (!super.generalAccountOption(input)) {
                switch(input) {
                    case "r":
                        return;
                    default:
                        speakerPresenter.printInvalidInput();
                }
            }
        }

    }
}
