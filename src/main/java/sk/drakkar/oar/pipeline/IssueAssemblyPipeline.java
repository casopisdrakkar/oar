package sk.drakkar.oar.pipeline;

import sk.drakkar.oar.Issue;
import sk.drakkar.oar.plugin.IssueAssemblyPlugin;

import java.util.Collection;

public class IssueAssemblyPipeline extends Pipeline<IssueAssemblyPlugin> {
    public IssueAssemblyPipeline() {
        // empty constructor
    }

    public IssueAssemblyPipeline(Collection<IssueAssemblyPlugin> plugins) {
        super(plugins);
    }

    @Override
    protected void doWithPlugin(IssueAssemblyPlugin plugin, Context context) throws PluginExecutionException {
        Issue issue = context.get(GlobalContextVariables.issue);
        plugin.issueArticlesProcessed(issue);
    }
}
