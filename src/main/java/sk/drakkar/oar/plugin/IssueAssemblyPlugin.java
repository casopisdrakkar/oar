package sk.drakkar.oar.plugin;

import sk.drakkar.oar.Issue;

public interface IssueAssemblyPlugin extends Plugin  {

    /**
     * Handler method called whenever all articles in an issue have been processed.
     *
     * @param issue the completed and parsed issue with all articles, resources and metadata
     */
    public void issueArticlesProcessed(Issue issue);

}
