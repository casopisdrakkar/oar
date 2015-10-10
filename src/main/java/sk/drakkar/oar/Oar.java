package sk.drakkar.oar;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.authors.AuthorArticlesCollector;
import sk.drakkar.oar.authors.AuthorListBuilder;
import sk.drakkar.oar.authors.AuthorProfilePageBuilder;
import sk.drakkar.oar.authors.MostProductiveAuthorsCollector;
import sk.drakkar.oar.css.CopyCssPlugin;
import sk.drakkar.oar.homepage.HomePageBuilder;
import sk.drakkar.oar.pages.PagePlugin;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.pipeline.GlobalContextVariables;
import sk.drakkar.oar.pipeline.IssueAssetPipeline;
import sk.drakkar.oar.pipeline.IssuePipeline;
import sk.drakkar.oar.pipeline.PortalAssemblyPipeline;
import sk.drakkar.oar.plugin.Plugin;
import sk.drakkar.oar.search.TipueSearchPlugin;
import sk.drakkar.oar.tags.TagCloudBuilder;
import sk.drakkar.oar.tags.TagDetailPageBuilder;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.List;

public class Oar {
	private static final Logger logger = LoggerFactory.getLogger(Oar.class);

	private final IssueParser issueParser = new IssueParser();
	
	private final Configuration configuration;

	private final IssueAssetPipeline issueAssetPipeline = new IssueAssetPipeline();

	private final IssuePipeline issuePipeline = new IssuePipeline();

	private final PortalAssemblyPipeline portalAssemblyPipeline = new PortalAssemblyPipeline();

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
			
			executePortalAssemblyPipeline();

			logger.info("Done.");
		} catch (IOException e) {
			throw new ProjectHierarchyException(e);
		}
	}


	private void processIssueFolder(File issueFolder) {
		Issue issue = issueParser.toIssue(issueFolder);

		for (File issueAssetFile : issueFolder.listFiles()) {
			Context issueAssetContext = Context
					.of(GlobalContextVariables.issue, issue)
					.andOf(GlobalContextVariables.issueAsset, issueAssetFile);
			executeIssueAssetPipeline(issueAssetContext);
		}
		executeIssuePipeline(issue);
	}

	private void executeIssueAssetPipeline(Context context) {
		issueAssetPipeline.execute(context);
	}

	private void executeIssuePipeline(Issue issue) {
		issuePipeline.execute(Context.of(GlobalContextVariables.issue, issue));
	}

	private void executePortalAssemblyPipeline() {
		portalAssemblyPipeline.execute(new Context());
	}
	

	public void addPlugin(Plugin plugin) {
		this.issueAssetPipeline.add(plugin);
		this.issuePipeline.add(plugin);
		this.portalAssemblyPipeline.add(plugin);
	}


	public static void main(String[] args) {
		CommandLineConfiguration commandLineConfiguration = new CommandLineConfiguration();
		CmdLineParser cmdLineParser = new CmdLineParser(commandLineConfiguration);
		try {
			cmdLineParser.parseArgument(args);
			
			File projectFolder = commandLineConfiguration.getProjectFolder();
			Configuration configuration = new Configuration(projectFolder);

			File outputFolder = commandLineConfiguration.getTargetFolder();
			if(outputFolder == null) {
				outputFolder = Configuration.OUTPUT_FOLDER;
			}
			configuration.setOutputFolder(outputFolder);

			Oar oar = new Oar(configuration);

			oar.addPlugin(new IssueAssetPlugin(configuration));

			oar.addPlugin(new IssueIndexBuilder(configuration));

			PagePlugin pagePlugin = new PagePlugin(configuration);
			oar.addPlugin(pagePlugin);

			IssueColorBuilder issueColorBuilder = new IssueColorBuilder();
			oar.addPlugin(issueColorBuilder);
			
			TagCloudBuilder tagCloudBuilder = new TagCloudBuilder(configuration);
			oar.addPlugin(tagCloudBuilder);

			TagDetailPageBuilder tagDetailPageBuilder = new TagDetailPageBuilder(configuration);
			oar.addPlugin(tagDetailPageBuilder);

			oar.addPlugin(new AuthorArticlesCollector());

			MostProductiveAuthorsCollector mostProductiveAuthorsPlugin = new MostProductiveAuthorsCollector();
			mostProductiveAuthorsPlugin.setIgnoredMostProductiveAuthorNames(Arrays.asList("redakce"));
			oar.addPlugin(mostProductiveAuthorsPlugin);

			AuthorListBuilder authorListWriter = new AuthorListBuilder(configuration);
			oar.addPlugin(authorListWriter);

			AuthorProfilePageBuilder authorProfilePageBuilder = new AuthorProfilePageBuilder(configuration);
			oar.addPlugin(authorProfilePageBuilder);

			HomePageBuilder homePageBuilder = new HomePageBuilder(configuration);
			oar.addPlugin(homePageBuilder);

			CopyCssPlugin copyCssPlugin = new CopyCssPlugin(configuration);
			oar.addPlugin(copyCssPlugin);

			TipueSearchPlugin tipueSearchPlugin = new TipueSearchPlugin(configuration);
			oar.addPlugin(tipueSearchPlugin);

			oar.publish();
		} catch (CmdLineException e) {
			System.err.println("Usage:");
			System.err.println("\tjava Oar FILE [OUTPUT_FOLDER]");
			cmdLineParser.printUsage(System.err);
		}
		
	}	
}
