package exception;

public class UserNotFoundException extends RuntimeException { // Or extend Exception if you prefer checked exceptions
    public UserNotFoundException(String message) {
        super(message);
    }
}
