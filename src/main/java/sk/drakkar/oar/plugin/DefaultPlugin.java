package sk.drakkar.oar.plugin;

import sk.drakkar.oar.Article;
import sk.drakkar.oar.Issue;

public class DefaultPlugin implements Plugin {
    @Override
    public void articleProcessed(Article article) {

    }

    @Override
    public void issueArticlesProcessed(Issue issue) {
        // NO-OP
    }

    @Override
    public void publicationComplete() {
        // NO-OP
    }
}
