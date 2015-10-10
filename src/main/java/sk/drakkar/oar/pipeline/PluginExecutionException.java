package sk.drakkar.oar.pipeline;

public class PluginExecutionException extends RuntimeException {

    public PluginExecutionException() {
        super();
    }

    public PluginExecutionException(String msg) {
        super(msg);
    }

    public PluginExecutionException(String message, Throwable cause) {
        super(message, cause);
    }

    public PluginExecutionException(Throwable cause) {
        super(cause);
    }
}
