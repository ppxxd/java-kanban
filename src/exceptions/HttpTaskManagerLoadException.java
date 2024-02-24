package exceptions;

public class HttpTaskManagerLoadException extends ManagerSaveException {

    public HttpTaskManagerLoadException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
