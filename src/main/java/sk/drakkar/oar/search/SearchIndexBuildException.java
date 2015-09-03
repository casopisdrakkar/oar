package sk.drakkar.oar.search;

public class SearchIndexBuildException extends RuntimeException {

    public SearchIndexBuildException() {
        super();
    }

    public SearchIndexBuildException(String msg) {
        super(msg);
    }

    public SearchIndexBuildException(String message, Throwable cause) {
        super(message, cause);
    }

    public SearchIndexBuildException(Throwable cause) {
        super(cause);
    }
}
