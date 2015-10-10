package sk.drakkar.oar.pipeline;

public class AbortPipelineException extends PluginExecutionException {

    public AbortPipelineException() {
        super();
    }

    public AbortPipelineException(String msg) {
        super(msg);
    }

    public AbortPipelineException(String message, Throwable cause) {
        super(message, cause);
    }

    public AbortPipelineException(Throwable cause) {
        super(cause);
    }
}
