package conferencemain;

import account.AccountManager;
import event.EventManager;
import message.MessagingManager;
import room.RoomManager;
import userinterface.UserSystemFactory;
import authentication.ConferenceLoginSystem;
import authentication.ConferenceRegisterSystem;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * An controller class represents the welcome system for users.
 * It stores MessageSearchingSystem, EventSystem, RoomSystem, AccountSystem and AccountManager.
 * It gets input from user and give corresponding moves by calling methods from MessageSearchingSystem,
 * EventSystem, RoomSystem, AccountSystem and AccountManager.
 * @author Group0694
 * @version 2.0.0
 */
class WelcomeSystem {
    // Storing all 4 use cases to avoid duplicate long parameters for all methods in this class
    private final AccountManager accountManager;
    private final MessagingManager messagingManager;

    /**
     * Creates an welcome system with specific account manager and message, event, room, and account systems.
     * @param accountManager A copy of the AccountManager use case to be stored
     * @param messagingManager A copy of the MessagingManager use case to be stored
     */
    WelcomeSystem(AccountManager accountManager, MessagingManager messagingManager){
        this.accountManager = accountManager;
        this.messagingManager = messagingManager;
    }

    /**
     * Print strings reflecting the current system and directs the related options for users.
     * Check if the data are saved successfully with ending the program.
     * If user choose "0", the login option would be selected and enters related user system.
     * If user choose "1", the create new account option would be selected and enters related user system.
     * If user choose "q", the program will end. Under this case, checks if the data inputted by users are saved.
     * @return true iff the data were saved and quited the program successfully, false if the data were not saved
     * but quited the program.
     */
    boolean start(){
        Scanner sc = new Scanner(System.in);
        String wMessage = "CSC207 Phase 2 Conference System";
        WelcomePresenter wp = new WelcomePresenter();
        wp.printWelcomeMessage(wMessage);
        wp.printMenu();
        while (true){
            String input = sc.nextLine();
            switch (input){
                case "0":
                    wp.requestTo("login");
                    this.login();
                    break;
                case  "1":
                    wp.requestTo("create an account");
                    this.register();
                    break;
                case "q":
                    wp.requestTo("end program");
                    wp.printExistMenu();
                    input = sc.nextLine();
                    aa:
                    for (;;) {
                        switch(input) {
                            case "0":
                                wp.printActionMessage("Save the account and message data");
                                wp.printActionMessage("End Program");
                                return true;
                            case "1":
                                wp.printActionMessage("Un-save the account and message data");
                                wp.printActionMessage("End Program");
                                return false;
                            case "r":
                                wp.printRedoMessage("End Program");
                                break aa;
                            default:
                                wp.printInvalidInput();
                                wp.getInput();
                                input = sc.nextLine();
                        }
                    }
                    break;
                default:
                    wp.printInvalidInput();
                    wp.getInput();
                    continue;
            }
            wp.printWelcomeMessage(wMessage);
            wp.printMenu();
        }
    }

    // helper for login() and register()
    private void createUserSys(String username, String type){
        new UserSystemFactory().makeUserSys(type, username,
                accountManager, messagingManager).run();
    }

    // helper for start()
    private void login(){
        ConferenceLoginSystem start = new ConferenceLoginSystem();
        String[] accInfo = start.logIn(accountManager);
        if (accInfo != null) {
            this.createUserSys(accInfo[0], accInfo[1]);
        }
    }

    // helper for start()
    private void register(){
        ConferenceRegisterSystem register = new ConferenceRegisterSystem();
        WelcomePresenter wp = new WelcomePresenter();
        List<String> allAccounts = accountManager.getAllTypes();
        // The parameter parameter of this menu can be replaced by accountManager.getAllTypes() or other choices
        // of accounts that are allowed to register
        List<String> lst = new ArrayList<>();
        lst.add(allAccounts.get(0));
        lst.add(allAccounts.get(3));
        wp.printMenu(lst, "Cancel Registration");
        Scanner sc = new Scanner(System.in);
        String choice;
        String username;
        while (true) {
            choice = sc.nextLine();
            switch(choice) {
                case "0":
                    username = register.createAccount(allAccounts.get(0), accountManager);
                    if (username != null) {
                        this.createUserSys(username, allAccounts.get(0));
                    }
                    return;
                case "1":
                    username = register.createAccount(allAccounts.get(3), accountManager);
                    if (username != null) {
                        this.createUserSys(username, allAccounts.get(3));
                    }
                    return;
                case "r":
                    wp.printRedoMessage("Cancel Registration");
                    return;
                default:
                    wp.printInvalidInput();
                    wp.getInput();
            }
        }
    }
}
