package sk.drakkar.oar.plugin;

import sk.drakkar.oar.Configuration;

public abstract class ConfigurablePlugin extends DefaultPlugin {
    private Configuration configuration;

    public ConfigurablePlugin(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
