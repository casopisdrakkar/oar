package sk.drakkar.oar.plugin;

import sk.drakkar.oar.Article;
import sk.drakkar.oar.Issue;

import java.util.EventListener;

public interface Plugin extends EventListener {
    public void articleProcessed(Article article);

    public void issueArticlesProcessed(Issue issue);

    public void publicationComplete();
}
