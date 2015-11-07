package sk.drakkar.oar.plugin;

import sk.drakkar.oar.Article;
import sk.drakkar.oar.Issue;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.pipeline.PluginExecutionException;

import java.util.EventListener;

public interface Plugin extends EventListener {
    /**
     * Handler method called whenever an article is processed in the Issue Asset Pipeline.
     *
     * @param article which has just been processed
     * @param context context of the Issue Asset Pipeline.
     * @throws PluginExecutionException whenever the plugin fails
     */
    public void articleProcessed(Article article, Context context) throws PluginExecutionException;

    /**
     * Handler method called whenever all articles in an issue have been processed.
     *
     * @param issue the completed and parsed issue with all articles, resources and metadata
     */
    public void issueArticlesProcessed(Issue issue);

    /**
     * Issued whenever all issues have been processed and the site building process is about to be
     * completed.
     * @param context Portal Assembly Pipeline context containing shared data between all
     *                plugins that support this method.
     * @throws PluginExecutionException
     */
    public void publicationComplete(Context context) throws PluginExecutionException;
}
