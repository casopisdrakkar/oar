package sk.drakkar.oar.plugin;

import sk.drakkar.oar.Article;
import sk.drakkar.oar.Issue;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.pipeline.PluginExecutionException;

import java.util.EventListener;

public interface Plugin extends EventListener {
    public void articleProcessed(Article article, Context context) throws PluginExecutionException;

    public void issueArticlesProcessed(Issue issue);

    public void publicationComplete(Context context) throws PluginExecutionException;
}
