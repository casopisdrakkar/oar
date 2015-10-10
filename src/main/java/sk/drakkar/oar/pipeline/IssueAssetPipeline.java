package sk.drakkar.oar.pipeline;

import sk.drakkar.oar.Article;
import sk.drakkar.oar.plugin.Plugin;

import java.util.Collection;

public class IssueAssetPipeline extends Pipeline {
    public IssueAssetPipeline() {
        // empty constructor
    }

    public IssueAssetPipeline(Collection<Plugin> plugins) {
        super(plugins);
    }

    @Override
    protected void doWithPlugin(Plugin plugin, Context context) {
        Article article = context.get(GlobalContextVariables.article);
        plugin.articleProcessed(article, context);
    }
}
