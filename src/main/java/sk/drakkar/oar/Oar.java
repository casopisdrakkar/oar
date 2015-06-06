package sk.drakkar.oar;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.drakkar.oar.authors.AuthorListBuilder;
import sk.drakkar.oar.homepage.HomePageBuilder;
import sk.drakkar.oar.tags.TagCloudBuilder;

import com.google.common.base.Charsets;

public class Oar {

	private static final Logger logger = LoggerFactory.getLogger(Oar.class);
	
	private final ArticleParser articleParser = new ArticleParser();
	
	private final ArticleTemplater articleTemplater = new ArticleTemplater();
	
	private final ToHtmlConverter toHtmlConverter = new PegdownConverter();
	
	private final IssueParser issueParser = new IssueParser();
	
	private List<IssueArticlesProcessedListener> issueArticlesProcessedListeners = new LinkedList<IssueArticlesProcessedListener>();
	
	private List<PublicationCompleteListener> publicationCompleteListeners = new LinkedList<PublicationCompleteListener>();

	private final Configuration configuration;
	
	public Oar(Configuration configuration) {
		this.configuration = configuration;
	}

	public void publish() {
		try {
			IssueFolderVisitor visitor = new IssueFolderVisitor();
			Files.walkFileTree(configuration.getContentFolder().toPath(), visitor);
			
			List<File> issueFolders = visitor.getIssueFolders();
			for (File issueFolder : issueFolders) {
				processIssueFolder(issueFolder);
			}
			
			fireOnPublicationComplete();

			logger.info("Done.");
		} catch (IOException e) {
			throw new ProjectHierarchyException(e);
		}
		
	}


	private void processIssueFolder(File issueFolder) {
		Issue issue = issueParser.toIssue(issueFolder);
		for (File file : issueFolder.listFiles()) {
			if(isArticle(file)) {
				processArticle(issue, file);
			} else if(isAllowedResource(file)) {
				processAllowedResource(file, issue);
			}
		}
		fireOnIssueArticlesProcessed(issue);
	}

	private void fireOnIssueArticlesProcessed(Issue issue) {
		for (IssueArticlesProcessedListener issueArticlesProcessedListener : this.issueArticlesProcessedListeners) {
			issueArticlesProcessedListener.issueArticlesProcessed(issue);
		}
	}

	private void fireOnPublicationComplete() {
		for (PublicationCompleteListener listener : publicationCompleteListeners) {
			listener.publicationComplete();
		}
	}
	
	private void processArticle(Issue issue, File articleFile) {
		Article article = articleParser.parse(articleFile);
		String htmlSource = toHtmlConverter.convert(article.getSource());
		article.setHtmlSource(htmlSource);
		article.setIssue(issue);
		
		saveMarkdown(article);
		
		issue.addArticle(article);
	}

	private void saveMarkdown(Article article) {
		try {
			File outputFolder = this.configuration.getOutputFolder(article.getIssue());
			String articleHtmlFileName = com.google.common.io.Files.getNameWithoutExtension(article.getSourceFile().getName()) + ".html";
			File articleOutputFile = new File(outputFolder, articleHtmlFileName);
			String articleHtml = articleTemplater.convert(article);
			com.google.common.io.Files.write(articleHtml, articleOutputFile, Charsets.UTF_8);
			
			logger.info("Exported Markdown to " + articleOutputFile);
		} catch (IOException e) {
			throw new ArticleExportException("Cannot read article source from " + article.getSourceFile(), e);
		}
	}


	private boolean isArticle(File file) {
		return file.getName().endsWith(".md");
	}


	private boolean isAllowedResource(File file) {
		return file.getName().endsWith(".png")
				|| file.getName().endsWith(".jpg")
				|| file.getName().endsWith(".pdf");
	}
	
	private void processAllowedResource(File file, Issue issue) {
		File outputFolder = this.configuration.getOutputFolder(issue);
		try {
			FileUtils.copyAndOverwrite(file, outputFolder);
		} catch (IOException e) {
			throw new ResourceException("Cannot copy resource " + file + " to target folder " + outputFolder, e);
		}		
	}
	
	public void addIssueArticlesProcessedListener(IssueArticlesProcessedListener listener) {
		this.issueArticlesProcessedListeners.add(listener);
	}

	public void addPublicationCompleteListener(PublicationCompleteListener listener) {
		this.publicationCompleteListeners.add(listener);
	}
	
	public static void main(String[] args) {
		CommandLineConfiguration commandLineConfiguration = new CommandLineConfiguration();
		CmdLineParser cmdLineParser = new CmdLineParser(commandLineConfiguration);
		try {
			cmdLineParser.parseArgument(args);
			
			File projectFolder = commandLineConfiguration.getProjectFolder();
			Configuration configuration = new Configuration(projectFolder);
			Oar oar = new Oar(configuration);
			
			oar.addIssueArticlesProcessedListener(new IssueIndexBuilder(configuration));

			TagCloudBuilder tagCloudBuilder = new TagCloudBuilder(configuration);
			oar.addIssueArticlesProcessedListener(tagCloudBuilder);
			oar.addPublicationCompleteListener(tagCloudBuilder);

			AuthorListBuilder authorListBuilder = new AuthorListBuilder(configuration);
			oar.addIssueArticlesProcessedListener(authorListBuilder);
			oar.addPublicationCompleteListener(authorListBuilder);

			HomePageBuilder homePageBuilder = new HomePageBuilder(configuration);
			oar.addIssueArticlesProcessedListener(homePageBuilder);
			oar.addPublicationCompleteListener(homePageBuilder);
			
			oar.publish();
		} catch (CmdLineException e) {
			System.err.println("Usage:");
			System.err.println("\tjava Oar FILE");
			cmdLineParser.printUsage(System.err);
		}
		
	}	
}
