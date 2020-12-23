package authentication;

import account.*;

import java.util.Scanner;

/**
 * A controller class that can ask user input for username and password to log in and stores accountManager.
 * @author Group0694
 * @version 2.0.0
 */

public class ConferenceLoginSystem {

    /**
     * Asks user to give input of user name and password to log in.
     * @param accountManager A copy of the instance of AccountManager class
     * @return an array of string of username and its type, null if user request to quit.
     */
    public String[] logIn(AccountManager accountManager){
        ConferencePresenter cp = new ConferencePresenter();
        cp.printWelcomeMessage("Login System");
        Scanner sc = new Scanner(System.in);

        while (true) {
            cp.enterUsernameWithoutRestriction();
            String input = sc.nextLine();
            String username, password, type;
            if ("r".equals(input)) {
                cp.printRedoMessage("Quit Login System");
                return null;
            } else {
                username = input;
            }
            cp.enterPasswordWithoutRestriction();
            password = sc.nextLine();
            type = validateAccount(username, password, accountManager);
            if (type != null){
                cp.printLoginSuccessful();
                return new String[]{username, type};
            }
            cp.printIncorrectUsernameOrPassword();
        }
    }

    /* helper method */
    // This one line method exists because we can encapsulate how the password is checked and account for future changes
    private String validateAccount(String username, String password, AccountManager accountManager){
        return accountManager.checkPassword(username, password);
    }

}
