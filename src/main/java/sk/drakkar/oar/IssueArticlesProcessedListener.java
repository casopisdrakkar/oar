package sk.drakkar.oar;

import java.util.EventListener;

public interface IssueArticlesProcessedListener extends EventListener {
	public void issueArticlesProcessed(Issue issue);
}
