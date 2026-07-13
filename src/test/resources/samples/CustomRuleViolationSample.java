package samples;

/**
 * Sample violating project-specific custom rule (ForbidSystemOutCheck).
 */
public class CustomRuleViolationSample {

    public void logMessage(String message) {
        System.out.println(message);
    }
}
