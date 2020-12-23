package authentication;

import account.AccountFactory;
import account.AccountManager;
import java.util.Scanner;

/**
 * A controller class that can ask user input for username and password to register an account.
 * Stores accountManager, usernameValidator and passwordValidator.
 * @author Group0694
 * @version 2.0.0
 */
@SuppressWarnings("FieldMayBeFinal")
public class ConferenceRegisterSystem {

    private Validator usernameValidator = new SimpleValidationUsername();
    private Validator passwordValidator = new SimpleValidationPassword();

    /**
     * Requests to create an account.
     * @param option a string represents type of account needs to create.
     * @param am A copy of the instance of AccountManager class
     * @return result of call enterUsername method.
     */
    public String createAccount(String option, AccountManager am) {
        Scanner c = new Scanner(System.in);
        ConferencePresenter crp = new ConferencePresenter();

        crp.printActionMessage("Request to create a " + option + " account.");
        return enterUsername(c, crp, option, am);
    }

    /**
     * Creates an account with given data, check if data are valid to create an account.
     * @param accountType A string represents the type of account.
     * @param username A string represents the username of the account.
     * @param password A string represents the password of the account.
     * @param am A copy of the instance of AccountManager class
     */
    public void createAccountWithData(String accountType, String username, String password, AccountManager am) {
        ConferencePresenter crp = new ConferencePresenter();
        crp.printSeparateLine();
        AccountFactory af = new AccountFactory();
        if (usernameValidator.validate(username)) {
            if (am.getAccountInfo(username) == null) {
                if (passwordValidator.validate(password)) {
                    if (af.getAccount(accountType, username, password) != null) {
                        crp.printActionMessage("Account Created: " + username + ", Type: " + accountType);
                        am.addAccount(accountType, username, password);
                    } else {
                        crp.printErrorMessage("Invalid Account Type: " + accountType);
                    }
                } else {
                    crp.printErrorMessage("Invalid Password: " + password);
                }
            } else {
                crp.printErrorMessage("Account Already Exist: " + username);
            }
        } else {
            crp.printErrorMessage("Invalid Username: " + username);
        }
    }

    private String enterUsername(Scanner c, ConferencePresenter crp, String accountType, AccountManager am) {
        crp.enterUsernameWithRestriction(usernameValidator.getDescription());
        String input = c.nextLine();
        for (;;) {
            if (input.equals("r")) {
                // Exit register system
                crp.printRedoMessage("Create account");
                return null;
            } else if (!usernameValidator.validate(input)) {
                crp.printInvalidInput();
                crp.getInput();
                input = c.nextLine();
            } else if (am.getAccountInfo(input) != null) {
                crp.printAccountExist();
                crp.getInput();
                input = c.nextLine();
            } else {
                crp.printActionMessage("Valid Username");
                break;
            }
        }
        String password = enterPassword(c, crp);
        if (password.equals("r")) {
            enterUsername(c, crp, accountType, am);
        } else {
            crp.printUsernameAndPassword(accountType, input, password);
            // input is correct username, password is correct password, and user has confirmed
            am.addAccount(accountType, input, password);
            return input;
        }
        return null;
    }

    private String enterPassword(Scanner c, ConferencePresenter crp) {
        crp.enterPasswordWithRestriction(passwordValidator.getDescription());
        String input = c.nextLine();
        for (;;) {
            if (input.equals("r")) {
                crp.printRedoMessage("Enter Username");
                break;
            } else if (!passwordValidator.validate(input)) {
                crp.printInvalidInput();
                crp.getInput();
                input = c.nextLine();
            } else {
                crp.printActionMessage("Valid Password");
                String confirmation = enterConfirmation(c, crp);
                if (confirmation.equals("r")) {
                    input = enterPassword(c, crp);
                }
                break;
            }
        }
        return input;
    }

    private String enterConfirmation(Scanner c, ConferencePresenter crp) {
        crp.printConfirm();
        String input = c.nextLine();
        for (;;) {
            if (input.equals("r")) {
                crp.printRedoMessage("Enter Password");
                break;
            } else if (!input.equals("y")) {
                crp.printInvalidInput();
                crp.getInput();
                input = c.nextLine();
            } else {
                crp.printActionMessage("Account Created");
                break;
            }
        }
        return input;
    }

}
