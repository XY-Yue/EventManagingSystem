package account;

/**
 * A factory class that can construct a new Attendee, Speaker or Organizer account.
 * It gives account type, username and password for a new account as parameter.
 * @author Group0694
 * @version 2.0.0
 */
public class AccountFactory {

    /**
     * Creates a new account with given type, username and password.
     * @param accountType A string representing the type of this new account.
     * @param username A string representing the username of this new account.
     * @param password A string representing the password of this new account.
     * @return An Account with these given information.
     */
    public Account getAccount(String accountType, String username, String password) {
        if (accountType == null) {
            return null;
        } else if (accountType.equalsIgnoreCase("ATTENDEE")) {
            return new Attendee(username, password);
        } else if (accountType.equalsIgnoreCase("SPEAKER")) {
            return new Speaker(username, password);
        } else if (accountType.equalsIgnoreCase("ORGANIZER")) {
            return new Organizer(username, password);
        } else if (accountType.equalsIgnoreCase("VIP")){
            return new VIPAttendee(username, password);
        } else {
            return null;
        }
    }
}

