package sk.drakkar.oar.homepage;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.drakkar.oar.Configuration;
import sk.drakkar.oar.Issue;
import sk.drakkar.oar.IssueArticlesProcessedListener;
import sk.drakkar.oar.IssueByReversedNumberComparator;
import sk.drakkar.oar.PublicationCompleteListener;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class HomePageBuilder implements PublicationCompleteListener, IssueArticlesProcessedListener {
	
	private static final Logger logger = LoggerFactory.getLogger(HomePageBuilder.class);
	
	private Configuration configuration;

	private List<Issue> issues = new LinkedList<Issue>(); 
	
	private HomePageTemplater homePageTemplater = new HomePageTemplater(); 
	
	public HomePageBuilder(Configuration configuration) {
		this.configuration = configuration;
	}
	
	@Override
	public void publicationComplete() {
		logger.info("Building home page");
		Collections.sort(issues, IssueByReversedNumberComparator.INSTANCE);
		
		String html = homePageTemplater.convert(this.issues);
		write(html);
	}
	
	private void write(String html) {
		try {
			File outputFile = new File(this.configuration.getOutputFolder(), "index.html");
			Files.write(html, outputFile, Charsets.UTF_8);
		} catch (IOException e) {
			throw new HomePageBuildingException("Unable to write home page", e);
		}
	}	

	@Override
	public void issueArticlesProcessed(Issue issue) {
		issues.add(issue);
	}
}
