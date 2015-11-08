package sk.drakkar.oar;


import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.pipeline.GlobalContextVariables;
import sk.drakkar.oar.plugin.AssetAssemblyPlugin;
import sk.drakkar.oar.plugin.ConfigurationSupport;

import java.io.File;
import java.io.IOException;

public class ArticlePlugin extends ConfigurationSupport implements AssetAssemblyPlugin {

    private static final Logger logger = LoggerFactory.getLogger(ArticlePlugin.class);

    private final ArticleTemplater articleTemplater = new ArticleTemplater();

    private final ArticleParser articleParser = new ArticleParser();

    private final ToHtmlConverter toHtmlConverter = new PegdownConverter();

    public ArticlePlugin(Configuration configuration) {
        super(configuration);
    }

    @Override
    public void onAsset(File assetFile, Issue issue, Context context) {
        if(!isArticle(assetFile)) {
            logger.debug("Non-article resource {} will not be handled", assetFile);
            return;
        }
        processArticle(issue, assetFile, context);
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



}
