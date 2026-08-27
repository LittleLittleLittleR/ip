package littler.exception;

/**
 * LittleRException is a custom exception class for the LittleR application.
 * It extends the built-in Exception class and is used to handle specific error cases 
 */
public class LittleRException extends Exception {
    
    /**
     * Constructs a new LittleRException with the specified detail message.
     * @param message the detail message
     */
    public LittleRException(String message) {
        super(message);
    }
    
}
