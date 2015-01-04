package sk.drakkar.oar;

import java.io.File;
import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.base.Charsets;

public class IssueIndexBuilder implements IssueArticlesProcessedListener {
	private static final String ISSUE_INDEX_FILENAME = "index.html";

	private IssueTemplater issueTemplater = new IssueTemplater();
	
	private static final Logger logger = LoggerFactory.getLogger(IssueIndexBuilder.class);

	private Configuration configuration;
	
	public IssueIndexBuilder(Configuration configuration) {
		this.configuration = configuration;
	}

	public void build(Issue issue) {
		logger.info("Building " + ISSUE_INDEX_FILENAME + " for " + issue.getNumber());
		try {
			File outputFolder = this.configuration.getOutputFolder(issue);
			String issueHtmlFileName = ISSUE_INDEX_FILENAME;
			File issueOutputFile = new File(outputFolder, issueHtmlFileName);
			String resolvedMarkdownSource = issueTemplater.convert(issue);
			com.google.common.io.Files.write(resolvedMarkdownSource, issueOutputFile, Charsets.UTF_8);
			
			logger.info("Exported Issue Markdown to " + issueOutputFile);
		} catch (IOException e) {
			throw new ArticleExportException("Cannot read issue source from " + issue.getFolder(), e);
		}
		
	}

	@Override
	public void issueArticlesProcessed(Issue issue) {
		build(issue);
	}
}
