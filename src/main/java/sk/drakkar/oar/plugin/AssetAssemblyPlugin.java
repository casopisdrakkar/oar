package sk.drakkar.oar.plugin;

import sk.drakkar.oar.Issue;
import sk.drakkar.oar.pipeline.Context;

import java.io.File;

public interface AssetAssemblyPlugin extends Plugin {
    void onAsset(File assetFile, Issue issue, Context context);
}
