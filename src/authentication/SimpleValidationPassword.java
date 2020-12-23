package authentication;

/**
 * A class implements Validator interface, can check if the password from user input is valid.
 * @author Group0694
 * @version 2.0.0
 */
public class SimpleValidationPassword implements Validator {
    /**
     * Checks if the given password contains only legal characters and password not null
     * @param password the String password given by user input
     * @return true if the password is valid for characters, else false
     */
    @Override
    public boolean validate(String password) {
        return password != null && password.matches("^(?=.*[A-Z])(?=.*[a-z])(?=.*[0-9])\\w{6,15}$");
    }

    /**
     * Returns a text message which needs to be printed as a prompt to user when entering password
     * @return text message that describes the restriction of the password setting
     */
    @Override
    public String getDescription() {
        return "Password must contain at least one lower case letter, one upper case letter and one number, " +
                "it can only contain word characters [a-zA-Z_0-9] and 6 <= length <= 15";
    }
}
