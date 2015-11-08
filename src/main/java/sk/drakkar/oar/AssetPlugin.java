package sk.drakkar.oar;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.plugin.AssetAssemblyPlugin;
import sk.drakkar.oar.plugin.ConfigurationSupport;

import java.io.File;
import java.io.IOException;

public class AssetPlugin extends ConfigurationSupport implements AssetAssemblyPlugin {
    protected static final Logger logger = LoggerFactory.getLogger(AssetPlugin.class);

    public AssetPlugin(Configuration configuration) {
        super(configuration);
    }

    @Override
    public void onAsset(File assetFile, Issue issue, Context context) {
        if (!isAllowedAsset(assetFile)) {
            logger.debug("Asset {} will not be handled", assetFile);
            return;
        }
        processAllowedAsset(assetFile, issue, context);
    }

    private boolean isAllowedAsset(File file) {
        return file.getName().endsWith(".png")
                || file.getName().endsWith(".jpg")
                || file.getName().endsWith(".pdf");
    }

    private void processAllowedAsset(File file, Issue issue, Context context) {
        File outputFolder = getConfiguration().getOutputFolder(issue);
        try {
            FileUtils.copyAndOverwrite(file, outputFolder);
            logger.debug("Copied asset {} to {}", file, outputFolder);
        } catch (IOException e) {
            throw new ResourceException("Cannot copy resource " + file + " to target folder " + outputFolder, e);
        }
    }
}
