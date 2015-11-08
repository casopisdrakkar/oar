package sk.drakkar.oar.pipeline;

public enum PipelineResult {
    /**
     * Pipeline was successfully completed
     */
    COMPLETED,
    /**
     * Pipeline was explicitely stopped by one
     * of the plugins
     */
    STOPPED,
    /**
     * Pipeline was unexpectingly terminated
     * due to an exception
     */
    THROWN_EXCEPTION
}
