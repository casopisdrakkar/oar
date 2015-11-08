package sk.drakkar.oar.plugin;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.Article;
import sk.drakkar.oar.Issue;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.pipeline.GlobalContextVariables;
import sk.drakkar.oar.pipeline.PluginExecutionException;

import java.io.File;

public abstract class ArticleAssemblyPlugin implements AssetAssemblyPlugin {
    protected final Logger logger = LoggerFactory.getLogger(getClass());

    /**
     * Handler method called whenever an article is processed in the Issue Asset Pipeline.
     *
     * @param article which has just been processed
     * @param context context of the Issue Asset Pipeline.
     * @throws PluginExecutionException whenever the plugin fails
     */
    public abstract void articleProcessed(Article article, Context context) throws PluginExecutionException;


    @Override
    public void onAsset(File assetFile, Issue issue, Context context) {
        Article article = context.get(GlobalContextVariables.article);
        if(article == null) {
            logger.debug("No article found in context {}", context);
            return;
        }

        articleProcessed(article, context);
    }


}
