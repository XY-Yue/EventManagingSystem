package account;

import authentication.ConferenceRegisterSystem;
import authentication.SimpleValidationPassword;
import event.EventManager;
import java.util.List;
import java.util.Scanner;

/**
 * An upper controller of account.
 * It stores AccountManager, EventManager, SimpleValidationPassword, AccountPresenter and ConferenceRegisterSystem.
 * It gets input from user and give corresponding moves by calling methods from AccountManager, EventManager,
 * SimpleValidationPassword, AccountPresenter and ConferenceRegisterSystem. To do kinds of operation to user's account.
 * @author Group0694
 * @version 2.0.0
 */

public class AccountSystem {
    private final AccountManager accounts;
    @SuppressWarnings("FieldMayBeFinal")
    private AccountPresenter presenter;

    /**
     * Creates an account system with specific account manager and event manager.
     * @param accounts An AccountManager represents the user's account manager.
     *
     */
    public AccountSystem(AccountManager accounts) {
        this.accounts = accounts;
        this.presenter = new AccountPresenter();
    }

    /**
     * Gets info of current account.
     * Prints type, username, number of event it has singed up and number of friends it has.
     */
    public void displayCurrentAccount(String username) {
        presenter.accountInfo(accounts.getAccountInfo(username));
    }

    /**
     * Prints a string that contains all speakers.
     */
    public void getSpeakerList() {
        presenter.printSpeakerListMessage();
        presenter.getAllSpeakers(accounts.getUsernameForType("Speaker"));
    }

    /**
     * Prints strings to guide the user change password if the new password is valid and the result about if the
     * operation is successful.
     * @param username The username of current user.
     */
    public void changePassword(String username) {
        SimpleValidationPassword validator = new SimpleValidationPassword();
        Scanner sc = new Scanner(System.in);
        presenter.askNewPassword();
        String newPassword = sc.nextLine();
        if (accounts.getPassword(username).equals(newPassword)) {
            presenter.samePassword();
            return;
        }
        boolean create = validator.validate(newPassword);
        if (create){
            accounts.setPassword(newPassword, username);
            presenter.changePasswordResult();
        }else{
            presenter.printErrorMessage(validator.getDescription());
        }
    }

    /**
     * Prints a string of the user has.
     * @param username The username of current user.
     */
    public void viewFriendList(String username) {
        presenter.getFriendList(accounts.getFriendList(username));
    }

    /**
     * Prints a string reflecting the result of adding a friend to friendList.
     * @param username The username of current user.
     */
    public void addFriend(String username) {
        String userToAdd = inputUsername();
        if (userToAdd == null) return;
        presenter.addFriendResult(accounts.addFriend(username, userToAdd));
    }

    private String inputUsername() {
        Scanner sc = new Scanner(System.in);
        presenter.askUsername();
        String username = sc.nextLine();
        if (!accounts.checkUser(username)) {
            presenter.printErrorMessage("User does not exist.");
            return null;
        }
        return username;
    }

    /**
     * Prints a string reflecting the result of removing a friend from friendList.
     * @param username The username of current user.
     */
    public void removeFriend(String username) {
        String userToDelete = inputUsername();
        if (userToDelete == null) return;
        presenter.deleteFriendResult(accounts.removeFriend(username, userToDelete));
    }

    /**
     * Sets username, password to create a new account of selected type and prints a string reflecting
     * the result of this operation.
     */
    public void addAccount(){
        List<String> types = accounts.getAllTypes();
        presenter.printMenu(types, "Cancel New Account Creation");
        Scanner sc = new Scanner(System.in);
        while (true){
            String chooseType = sc.nextLine();
            if (chooseType.matches("^[0-9]*$")){
                int option = Integer.parseInt(chooseType);
                if (option >= types.size()) presenter.printInvalidInput();
                else {
                    new ConferenceRegisterSystem().createAccount(types.get(option), accounts);
                    return;
                }
            } else {
                presenter.printInvalidInput();
                presenter.getInput();
            }
        }
    }

    /**
     * Upgrade attendee to VIP
     * @param key A list of valid message status
     */
    public void upgradeAttendee(List<String> key) {
        Scanner c = new Scanner(System.in);
        presenter.askUsername();
        String username = c.nextLine();
        if (accounts.isAccountType(username, "attendee")) {
            accounts.upgradeAttendee(username, key);
            presenter.printUpgradeAccount(true, username);
            return;
        }
        presenter.printUpgradeAccount(false, username);
    }

    /**
     * Degrade VIP to attendee
     * @param em An instance of EventManager
     * @param key A list of valid message status
     */
    public void degradeVIP(EventManager em, List<String> key) {
        Scanner c = new Scanner(System.in);
        presenter.askUsername();
        String username = c.nextLine();
        if (accounts.isAccountType(username, "vip")) {
            List<String> lst = accounts.degradeVIP(username, key);
            presenter.printDegradeAccount(true, username);
            em.updateVIPEvent(lst, accounts.getEventObserver(username));
            return;
        }
        presenter.printDegradeAccount(false, username);
    }
}
