package sk.drakkar.oar;

import sk.drakkar.oar.plugin.DefaultPlugin;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

public class IssueColorBuilder extends DefaultPlugin {
	
	public static final String DEFAULT_ISSUE_COLOR = "missingColor";

	@Override
	public void issueArticlesProcessed(Issue issue) {
		updateIssueColor(issue);
	}

	/**
	 * Updates Issue color. Issue color is defined as color of majority of Issue articles.
	 */
	public void updateIssueColor(Issue issue) {
		Map<String,Integer> colors = new HashMap<>();

		for(Article a : issue.getArticles()) {
			String color = a.getMetadata().getColor();
			if (colors.containsKey(color)) {
				colors.put(color, colors.get(color) + 1);
			} else {
				colors.put(color, 1);
			}
		}

		String issueColor = Collections.max(
				colors.entrySet(), (entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).getKey();

		String color = (issueColor != null) ? issueColor : DEFAULT_ISSUE_COLOR;
		issue.setColor(color);
	}
	
}
