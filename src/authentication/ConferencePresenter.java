package authentication;
import conferencemain.MainPresenter;

/**
 * A presenter class for conference login and register system.
 * Methods of this class prints out text information to user.
 * @author Group0694
 * @version 2.0.0
 */
class ConferencePresenter extends MainPresenter {

    /**
     * Prints out a welcome message according to system name.
     * @param systemName a string represents the name of this system
     */
    void printWelcomeMessage(String systemName) {
        super.printWelcomeMessage(systemName, "Please enter 'r' to redo the previous action");
    }

    /**
     * Prints to remind this account already exists
     */
    void printAccountExist() {
        super.printErrorMessage("Account already exist");
    }

    /**
     * Prints out the restriction of username when user try to create account.
     * @param restrictionMessage A string represents the restriction of the message
     */
    void enterUsernameWithRestriction(String restrictionMessage) {
        printSeparateLine();
        System.out.println("Please enter username");
        System.out.println("[Restriction] " + restrictionMessage);
        getInput();
    }

    /**
     * Prints out the restriction of password when user try to create account.
     * @param restrictionMessage A string represents the restriction of the message
     */
    void enterPasswordWithRestriction(String restrictionMessage) {
        printSeparateLine();
        System.out.println("Please enter password");
        System.out.println("[Restriction] " + restrictionMessage);
        getInput();
    }

    /**
     * Prints message that ask user to enter username.
     */
    void enterUsernameWithoutRestriction() {
        printSeparateLine();
        System.out.println("Please enter username");
        getInput();
    }

    /**
     * Prints message that ask user to enter password.
     */
    void enterPasswordWithoutRestriction() {
        System.out.println("Please enter password");
        getInput();
    }

    /**
     * Prints message that check if user confirm its operation.
     */
    void printConfirm() {
        printSeparateLine();
        System.out.println("Please enter 'y' to confirm account registration");
        System.out.println("Please enter 'r' to redo previous action");
        getInput();
    }

    /**
     * Prints the info of this account
     * @param accountType a string represents the type of user's account.
     * @param username a string represents username of user's account.
     * @param password a string represents the password of user's account.
     */
    void printUsernameAndPassword(String accountType, String username, String password) {
        printSeparateLine();
        System.out.println("Account Type: " + accountType);
        System.out.println("Username: " + username);
        System.out.println("Password: " + password);
    }


    /**
     * Prints an error message that remind the username or password is incorrect
     */
    void printIncorrectUsernameOrPassword() {
        super.printErrorMessage("Incorrect Username or Password");
    }

    /**
     * Prints message to tell the user has logged in successfully
     */
    void printLoginSuccessful() {
        super.printActionMessage("Login Successful");
    }
}
