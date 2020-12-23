package authentication;

/**
 * A class implements Validator interface, can check if the username from user input is valid.
 * @author Group0694
 * @version 2.0.0
 */
class SimpleValidationUsername implements Validator {

    /**
     * Checks if the given username contains only legal characters, and username not null.
     * @param username the String username given by user input
     * @return true if the username is valid for characters, else false
     */
    @Override
    public boolean validate(String username) {
        return username != null && username.matches("^[a-zA-Z0-9]{3,15}");
    }

    /**
     * Returns a text message which needs to be printed as a prompt to user when entering username
     * @return text message describes the restriction of the username setting
     */
    @Override
    public String getDescription() {
        return "Username can only be alphanumeric and 3 <= length <= 15";
    }
}
