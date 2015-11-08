package sk.drakkar.oar.pipeline;

import sk.drakkar.oar.Issue;
import sk.drakkar.oar.plugin.AssetAssemblyPlugin;

import java.io.File;
import java.util.Collection;

public class AssetAssemblyPipeline extends Pipeline<AssetAssemblyPlugin>  {
    public AssetAssemblyPipeline() {
        super();
    }

    public AssetAssemblyPipeline(Collection<AssetAssemblyPlugin> plugins) {
        super(plugins);
    }

    @Override
    protected void beforeExecute(Context context) {
        logger.debug("Processing asset file {}", getAssetFile(context));
    }

    @Override
    protected void doWithPlugin(AssetAssemblyPlugin plugin, Context context) throws PluginExecutionException {
        File assetFile = getAssetFile(context);
        Issue issue = context.get(GlobalContextVariables.issue);

        plugin.onAsset(assetFile, issue, context);
    }

    private File getAssetFile(Context context) {
        return context.get(GlobalContextVariables.issueAsset);
    }
}
