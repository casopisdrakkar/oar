package sk.drakkar.oar.pipeline;

import sk.drakkar.oar.plugin.Plugin;

import java.util.Collection;

public class PortalAssemblyPipeline extends Pipeline {
    public PortalAssemblyPipeline() {
        // empty constructor
    }

    public PortalAssemblyPipeline(Collection<Plugin> plugins) {
        super(plugins);
    }

    @Override
    protected void doWithPlugin(Plugin plugin, Context context) {
        plugin.publicationComplete(context);
    }
}
