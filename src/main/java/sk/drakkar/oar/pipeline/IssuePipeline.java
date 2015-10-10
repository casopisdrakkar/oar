package sk.drakkar.oar.pipeline;

import sk.drakkar.oar.Issue;
import sk.drakkar.oar.plugin.Plugin;

import java.util.Collection;

public class IssuePipeline extends Pipeline {
    public IssuePipeline() {
        // empty constructor
    }

    public IssuePipeline(Collection<Plugin> plugins) {
        super(plugins);
    }

    @Override
    protected void doWithPlugin(Plugin plugin, Context context) throws PluginExecutionException {
        Issue issue = context.get(GlobalContextVariables.issue);
        plugin.issueArticlesProcessed(issue);
    }
}
