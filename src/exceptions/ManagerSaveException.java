package exceptions;

import java.io.IOException;

public class ManagerSaveException extends IOException {

    public ManagerSaveException(String message) {
        super(message);
    }

    @Override
    public String getMessage() {
        return super.getMessage();
    }
}
