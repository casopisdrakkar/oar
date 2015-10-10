package sk.drakkar.oar;


import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.pipeline.AbortPipelineException;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.pipeline.GlobalContextVariables;
import sk.drakkar.oar.plugin.ConfigurablePlugin;

import java.io.File;
import java.io.IOException;

public class IssueAssetPlugin extends ConfigurablePlugin {

    private static final Logger logger = LoggerFactory.getLogger(IssueAssetPlugin.class);

    private final ArticleTemplater articleTemplater = new ArticleTemplater();

    private final ArticleParser articleParser = new ArticleParser();

    private final ToHtmlConverter toHtmlConverter = new PegdownConverter();

    public IssueAssetPlugin(Configuration configuration) {
        super(configuration);
    }

    @Override
    public void articleProcessed(Article article, Context context) {
        Issue issue = context.get(GlobalContextVariables.issue);
        File issueAsset = context.get(GlobalContextVariables.issueAsset);

        process(issue, issueAsset, context);
    }

    public void process(Issue issue, File issueAsset, Context context) {
        if(isArticle(issueAsset)) {
            processArticle(issue, issueAsset, context);
        } else if(isAllowedResource(issueAsset)) {
            processAllowedResource(issueAsset, issue, context);
            throw new AbortPipelineException("Pipe line aborted on published asset " + issueAsset);
        } else {
            throw new AbortPipelineException("Pipe line aborted on non-published asset " + issueAsset);
        }
    }


    private boolean isArticle(File file) {
        return file.getName().endsWith(".md");
    }

    private void processArticle(Issue issue, File articleFile, Context context) {
        Article article = articleParser.parse(articleFile);
        String htmlSource = toHtmlConverter.convert(article.getSource());
        article.setHtmlSource(htmlSource);
        article.setIssue(issue);

        publish(article);
        issue.addArticle(article);
        context.put(GlobalContextVariables.article, article);
    }

    private void publish(Article article) {
        try {
            File outputFolder = getConfiguration().getOutputFolder(article.getIssue());
            String articleHtmlFileName = com.google.common.io.Files.getNameWithoutExtension(article.getSourceFile().getName()) + ".html";
            File articleOutputFile = new File(outputFolder, articleHtmlFileName);
            String articleHtml = articleTemplater.convert(article);
            com.google.common.io.Files.write(articleHtml, articleOutputFile, Charsets.UTF_8);

            logger.debug("Exported Markdown to " + articleOutputFile);
        } catch (IOException e) {
            throw new ArticleExportException("Cannot read article source from " + article.getSourceFile(), e);
        }
    }



    private void processAllowedResource(File file, Issue issue, Context context) {
        File outputFolder = getConfiguration().getOutputFolder(issue);
        try {
            FileUtils.copyAndOverwrite(file, outputFolder);
        } catch (IOException e) {
            throw new ResourceException("Cannot copy resource " + file + " to target folder " + outputFolder, e);
        }
    }


    private boolean isAllowedResource(File file) {
        return file.getName().endsWith(".png")
                || file.getName().endsWith(".jpg")
                || file.getName().endsWith(".pdf");
    }

}
