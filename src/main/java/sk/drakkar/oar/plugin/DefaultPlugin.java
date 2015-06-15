package sk.drakkar.oar.plugin;

import sk.drakkar.oar.Issue;

public class DefaultPlugin implements Plugin {
    @Override
    public void issueArticlesProcessed(Issue issue) {
        // NO-OP
    }

    @Override
    public void publicationComplete() {
        // NO-OP
    }
}
