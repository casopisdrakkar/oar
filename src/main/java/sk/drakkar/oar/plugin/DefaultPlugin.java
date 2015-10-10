package sk.drakkar.oar.plugin;

import sk.drakkar.oar.Article;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.Issue;

public class DefaultPlugin implements Plugin {
    @Override
    public void articleProcessed(Article article, Context context) {

    }

    @Override
    public void issueArticlesProcessed(Issue issue) {
        // NO-OP
    }

    @Override
    public void publicationComplete(Context context) {
        // NO-OP
    }
}
