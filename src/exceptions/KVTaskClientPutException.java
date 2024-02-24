package exceptions;

public class KVTaskClientPutException extends RuntimeException {

    public KVTaskClientPutException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}