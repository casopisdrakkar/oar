package sk.drakkar.oar;

import sk.drakkar.oar.plugin.DefaultPlugin;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class IssueColorBuilder extends DefaultPlugin {
	
	@Override
	public void issueArticlesProcessed(Issue issue) {
		updateIssueColor(issue);
	}

	/**
	 * Updates Issue color. Issue color is defined as color of majority of Issue articles.
	 */
	public void updateIssueColor(Issue issue) {

		String issueColor = issue.getArticles().stream()
				.flatMap(x -> Stream.of(x.getMetadata().getColor()))
				.collect(Collectors.toMap(y -> y, y -> 1, Integer::sum))
				.entrySet().stream().max(Map.Entry.comparingByValue()).get().getKey();

		issue.setColor(issueColor);
	}
	
}
