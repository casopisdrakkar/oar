package sk.drakkar.oar.search;

import java.io.IOException;

public class SearchException extends RuntimeException {

    public SearchException() {
        super();
    }

    public SearchException(String msg) {
        super(msg);
    }

    public SearchException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchException(Throwable cause) {
        super(cause);
    }
}

