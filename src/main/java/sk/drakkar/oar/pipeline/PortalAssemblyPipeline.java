package sk.drakkar.oar.pipeline;

import sk.drakkar.oar.plugin.PortalAssemblyPlugin;

import java.util.Collection;

public class PortalAssemblyPipeline extends Pipeline<PortalAssemblyPlugin> {
    public PortalAssemblyPipeline() {
        // empty constructor
    }

    public PortalAssemblyPipeline(Collection<PortalAssemblyPlugin> plugins) {
        super(plugins);
    }

    @Override
    protected void doWithPlugin(PortalAssemblyPlugin plugin, Context context) {
        plugin.publicationComplete(context);
    }
}
