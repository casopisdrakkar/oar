package sk.drakkar.oar;

public class IllegalIssueFileNameException extends RuntimeException {

    public IllegalIssueFileNameException() {
        super();
    }

    public IllegalIssueFileNameException(String msg) {
        super(msg);
    }

    public IllegalIssueFileNameException(String message, Throwable cause) {
        super(message, cause);
    }

    public IllegalIssueFileNameException(Throwable cause) {
        super(cause);
    }
}

