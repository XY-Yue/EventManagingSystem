package userinterface;

import account.AccountEventSystem;
import account.AccountManager;
import account.AccountSystem;
import event.EventSchedulerSystem;
import message.MessageSearchingSystem;
import message.MessageSendingSystem;
import message.MessagingManager;
import room.RoomSystem;

import java.util.*;

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
     * @param accountManager A copy of the AccountManager use case
     */
    OrganizerSystem(String username, MessagingManager messagingManager,
                    AccountManager accountManager) {
        super(username, messagingManager, accountManager);
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
                        eventSchedulerSystem.cancelEvent(eventManager);
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

    /**
     * Runs the options for user to choose a conference with a additional choice of making a new conference
     */
    @Override
    public void run(){
        Scanner sc = new Scanner(System.in);

        while (true) {
            List<String> conferences = gateway.readTextFile("ConferenceDataBase.txt");
            if (conferences == null) conferences = new ArrayList<>();
            conferences.add("Create a new conference");
            organizerPresenter.printMenu(conferences, "log out");
            String input = sc.nextLine();
            if ("r".equalsIgnoreCase(input)) {
                // this will result in logout
                if (logOut(sc)) return;
            } else if (input.matches("[\\d]+") && Integer.parseInt(input) < conferences.size()) {
                int option = Integer.parseInt(input);
                String conference = conferences.get(option);
                if (option == conferences.size() - 1) { // organizer requested to make a new conference
                    String newConference = new LocalConferenceInitSystem<>().createConference(sc, conferences);

                    if (newConference == null) continue;
                    else {
                        conference = newConference;
                        gateway.writeToText("ConferenceDataBase.txt", Collections.singletonList(conference));
                    }
                }
                // Read the corresponding Eve/Room Manager
                initConference(conference);
            }else organizerPresenter.printInvalidInput();
        }
    }
}
