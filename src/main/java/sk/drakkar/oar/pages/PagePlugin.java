package sk.drakkar.oar.pages;

import com.google.common.base.Charsets;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.*;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.plugin.DefaultPlugin;

import java.io.File;
import java.io.IOException;

public class PagePlugin extends DefaultPlugin {
    private static final Logger logger = LoggerFactory.getLogger(PagePlugin.class);

    public static final String MARKDOWN_FILE_SUFFIX = ".md";

    private final ArticleParser articleParser = new ArticleParser();

    private final PageTemplater pageTemplater = new PageTemplater();

    private final ToHtmlConverter toHtmlConverter = new PegdownConverter();

    private Configuration configuration;

    public PagePlugin(Configuration configuration) {
        this.configuration = configuration;
    }

    @Override
    public void publicationComplete(Context context) {
        File contentFolder = configuration.getContentFolder();
        for (File file : contentFolder.listFiles()) {
            if(isPageFile(file)) {
                processPage(file);
            }
        }
        logger.info("Written static page HTMLs.");
    }

    private void processPage(File pageFile) {
        Article page = articleParser.parse(pageFile);
        String htmlSource = toHtmlConverter.convert(page.getSource());
        page.setHtmlSource(htmlSource);

        saveMarkdown(page);
    }

    private boolean isPageFile(File file) {
        return file.isFile() && file.getName().endsWith(MARKDOWN_FILE_SUFFIX);
    }

    private void saveMarkdown(Article article) {
        try {
            File outputFolder = this.configuration.getOutputFolder();
            String articleHtmlFileName = com.google.common.io.Files.getNameWithoutExtension(article.getSourceFile().getName()) + ".html";
            File articleOutputFile = new File(outputFolder, articleHtmlFileName);
            String pageHtml = pageTemplater.convert(article);
            com.google.common.io.Files.write(pageHtml, articleOutputFile, Charsets.UTF_8);

            logger.info("Exported Markdown to " + articleOutputFile);
        } catch (IOException e) {
            throw new ArticleExportException("Cannot read article source from " + article.getSourceFile(), e);
        }
    }
}
