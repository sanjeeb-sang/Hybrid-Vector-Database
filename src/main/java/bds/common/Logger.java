package bds.common;

/**
 * Logger that logs to Console.
 */
public class Logger {

    private boolean isDebugEnabled;
    private boolean isErrorEnabled;

    private static Logger instance;

    /**
     * Method to create a new Logger instance.
     */
    public static Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }

        return instance;
    }

    /**
     * Private constructor to not allow other object to use the Constructor.
     */
    private Logger() {}

    /**
     * Method to log a debug message.
     * @param message the debug message.
     * @param callingClass the class sending the message.
     */
    public void debug(String message, String callingClass) {
        if (isDebugEnabled) {
            System.out.println(message);
        }
    }

    /**
     * Method to enable debug logging.
     * @param enable true to enable, false to disable debug logging.
     */
    public void setDebugEnabled(boolean enable) {
        this.isDebugEnabled = enable;
    }

    /**
     * Method to enable error logging.
     * @param enable true to enable, false to disable error logging.
     */
    public void setErrorEnabled(boolean enable) {
        this.isErrorEnabled = enable;
    }

    /**
     * Method to just print a message to console.
     * @param message the message to print.
     */
    public void output(String message) {
        System.out.println(message);
    }

    /**
     * Method to log an error message.
     * @param message the error message.
     * @param callingClass the class sending the message.
     */
    public void error(String message, String callingClass) {
        if (isErrorEnabled) {
            System.out.println(message);
        }
    }
}
