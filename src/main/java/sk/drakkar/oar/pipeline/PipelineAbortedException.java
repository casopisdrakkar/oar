package sk.drakkar.oar.pipeline;

import sk.drakkar.oar.plugin.Plugin;

public class PipelineAbortedException extends PluginExecutionException {
    private final String reason;

    private final Object asset;

    private final Class<? extends Plugin> plugin;

    public PipelineAbortedException(String reason, Object asset, Class<? extends Plugin> plugin) {
        super(reason + ", abruptly aborting pipeline on " + asset + " in plugin " + plugin);
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
