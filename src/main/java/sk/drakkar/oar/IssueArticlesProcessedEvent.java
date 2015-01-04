package sk.drakkar.oar;

import java.util.EventObject;

public class IssueArticlesProcessedEvent extends EventObject {

	public IssueArticlesProcessedEvent(Issue issue) {
		super(issue);
	}
	
}
