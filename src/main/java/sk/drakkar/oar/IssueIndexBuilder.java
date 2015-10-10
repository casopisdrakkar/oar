package sk.drakkar.oar;

import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.plugin.ConfigurablePlugin;

import java.io.File;
import java.io.IOException;

public class IssueIndexBuilder extends ConfigurablePlugin {
	private static final String ISSUE_INDEX_FILENAME = "index.html";

	private IssueTemplater issueTemplater = new IssueTemplater();
	
	private static final Logger logger = LoggerFactory.getLogger(IssueIndexBuilder.class);

	public IssueIndexBuilder(Configuration configuration) {
		super(configuration);
	}

	public void build(Issue issue) {
		logger.info("Building " + ISSUE_INDEX_FILENAME + " for " + issue.getNumber());
		try {
			File outputFolder = getConfiguration().getOutputFolder(issue);
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
