package sk.drakkar.oar;

import org.kohsuke.args4j.CmdLineException;
import org.kohsuke.args4j.CmdLineParser;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.authors.AuthorArticlesCollector;
import sk.drakkar.oar.authors.AuthorListBuilder;
import sk.drakkar.oar.authors.AuthorProfilePageBuilder;
import sk.drakkar.oar.authors.MostProductiveAuthorsCollector;
import sk.drakkar.oar.pipeline.*;
import sk.drakkar.oar.plugin.*;
import sk.drakkar.oar.resources.CopyCssPlugin;
import sk.drakkar.oar.homepage.HomePageBuilder;
import sk.drakkar.oar.pages.PagePlugin;
import sk.drakkar.oar.resources.CopyFontsPlugin;
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

	private final AssetAssemblyPipeline assetAssemblyPipeline = new AssetAssemblyPipeline();

	private final IssueAssemblyPipeline issueAssemblyPipeline = new IssueAssemblyPipeline();

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
			Context issueAssetContext = newIssueAssetContext(issueAssetFile, issue);
			executeAssetAssemblyPipeline(issueAssetContext);
		}
		executeIssuePipeline(issue);
	}

	private Context newIssueAssetContext(File issueAssetFile, Issue issue) {
		return Context
                .of(GlobalContextVariables.issue, issue)
                .andOf(GlobalContextVariables.issueAsset, issueAssetFile);
	}

	private PipelineResult executeAssetAssemblyPipeline(Context context) {
		return assetAssemblyPipeline.execute(context);
	}

	private PipelineResult executeIssuePipeline(Issue issue) {
		return issueAssemblyPipeline.execute(Context.of(GlobalContextVariables.issue, issue));
	}

	private PipelineResult executePortalAssemblyPipeline() {
		return portalAssemblyPipeline.execute(new Context());
	}
	

	public Oar addPlugin(Plugin plugin) {
		if(plugin instanceof AssetAssemblyPlugin) {
			withAssetAssemblyPlugin((AssetAssemblyPlugin) plugin);
		}
		if(plugin instanceof IssueAssemblyPlugin) {
			withIssueCompletedListener((IssueAssemblyPlugin) plugin);
		}
		if(plugin instanceof PortalAssemblyPlugin) {
			withPortalCompletedListener((PortalAssemblyPlugin) plugin);
		}

		return this;
	}

	public Oar withAssetAssemblyPlugin(AssetAssemblyPlugin plugin) {
		logger.debug("Adding asset assembly plugin {}", plugin.getClass());
		this.assetAssemblyPipeline.add(plugin);
		return this;
	}

	public Oar withIssueCompletedListener(IssueAssemblyPlugin plugin) {
		logger.debug("Adding issue assembly plugin {}", plugin.getClass());
		this.issueAssemblyPipeline.add(plugin);
		return this;
	}

	public Oar withPortalCompletedListener(PortalAssemblyPlugin plugin) {
		logger.debug("Adding portal assembly plugin {}", plugin.getClass());
		this.portalAssemblyPipeline.add(plugin);
		return this;
	}

	private void logPipelines() {
		if(logger.isInfoEnabled()) {
			logger.info(this.assetAssemblyPipeline.toString());
			logger.info(this.issueAssemblyPipeline.toString());
			logger.info(this.portalAssemblyPipeline.toString());
		}
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


			oar.addPlugin(new AssetPlugin(configuration));
			oar.addPlugin(new ArticlePlugin(configuration));

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

			CopyFontsPlugin copyFontsPlugin = new CopyFontsPlugin(configuration);
			oar.addPlugin(copyFontsPlugin);

			TipueSearchPlugin tipueSearchPlugin = new TipueSearchPlugin(configuration);
			oar.addPlugin(tipueSearchPlugin);

			oar.logPipelines();

			oar.publish();
		} catch (CmdLineException e) {
			System.err.println("Usage:");
			System.err.println("\tjava Oar FILE [OUTPUT_FOLDER]");
			cmdLineParser.printUsage(System.err);
		}
		
	}	
}
