package sk.drakkar.oar.plugin;

import sk.drakkar.oar.Configuration;

public abstract class ConfigurableArticleAssemblyPlugin extends ArticleAssemblyPlugin {
    private Configuration configuration;

    public ConfigurableArticleAssemblyPlugin(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }

}
