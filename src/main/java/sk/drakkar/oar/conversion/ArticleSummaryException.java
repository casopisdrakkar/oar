package sk.drakkar.oar.conversion;

public class ArticleSummaryException extends RuntimeException {

    public ArticleSummaryException() {
        super();
    }

    public ArticleSummaryException(String msg) {
        super(msg);
    }

    public ArticleSummaryException(String message, Throwable cause) {
        super(message, cause);
    }

    public ArticleSummaryException(Throwable cause) {
        super(cause);
    }
}