package sk.drakkar.oar.pipeline;

import sk.drakkar.oar.plugin.Plugin;

/**
 * Indicates that the pipeline was willingly explicitely stopped
 * by plugin. This usually means that the further pipeline
 * execution is no longer necessary or desired. This
 * leads to <i>STOPPED</i> result of pipeline execution.
 * <p>
 *     When the pipeline is terminated abruptly,
 *     possibly due to exception that cannot be handled,
 *     use the {@link PipelineAbortedException}.
 * </p>
 * @see sk.drakkar.oar.pipeline.PipelineAbortedException
 *
 */
public class PipelineStoppedException extends PluginExecutionException {

    private final String reason;

    private final Object asset;

    private final Class<? extends Plugin> plugin;

    public PipelineStoppedException(String reason, Object asset, Class<? extends Plugin> plugin) {
        super(reason + ", stopping pipeline on " + asset + " in plugin " + plugin);
        this.reason = reason;
        this.asset = asset;
        this.plugin = plugin;

    }

    public Object getAsset() {
        return asset;
    }

    public Class<? extends Plugin> getPlugin() {
        return plugin;
    }

    public String getReason() {
        return reason;
    }
}

