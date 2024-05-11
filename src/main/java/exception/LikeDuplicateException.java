package exception;

public class LikeDuplicateException extends RuntimeException { // Or extend Exception if you prefer checked exceptions
    public LikeDuplicateException(String message) {
        super(message);
    }
}
