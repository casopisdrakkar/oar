package sk.drakkar.oar.plugin;

import sk.drakkar.oar.Configuration;

public class ConfigurationSupport {
    private Configuration configuration;

    public ConfigurationSupport(Configuration configuration) {
        this.configuration = configuration;
    }

    public Configuration getConfiguration() {
        return configuration;
    }
}
