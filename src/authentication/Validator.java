package authentication;

/**
 * An interface provides the validator for username and password
 * @author Group0694
 * @version 2.0.0
 */
interface Validator {
    /**
     * Validates the given String
     * @param str a given String
     * @return True iff it satisfies the condition
     */
    boolean validate(String str);

    /**
     * Gets the description
     * @return a string represents the description
     */
    String getDescription();
}
