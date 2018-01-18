package sk.drakkar.oar.gui.swing;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.Article;
import sk.drakkar.oar.ArticleParser;
import sk.drakkar.oar.Configuration;
import sk.drakkar.oar.IssueFolderVisitor;
import sk.drakkar.oar.authors.Author;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.LinkedList;
import java.util.List;

import static sk.drakkar.oar.pages.PagePlugin.MARKDOWN_FILE_SUFFIX;

public class AuthorService {
    public final Logger logger = LoggerFactory.getLogger(getClass());

    private List<Author> cachedAuthors = new LinkedList<>();

    private final Configuration configuration;

    private final ArticleParser articleParser = new ArticleParser();

    public AuthorService(Configuration configuration) {
        this.configuration = configuration;
    }

    public void initialize() {
        try {
            IssueFolderVisitor visitor = new IssueFolderVisitor();
            Files.walkFileTree(configuration.getContentFolder().toPath(), visitor);

            List<File> issueFolders = visitor.getIssueFolders();
            for (File issueFolder : issueFolders) {
                processIssueFolder(issueFolder);
            }
        } catch (IOException e) {
            logger.error("Unable to load authors from the issue folder", e);
        }
    }

    private void processIssueFolder(File issueFolder) {
        for (File articleFile : issueFolder.listFiles(this::isArticleFile)) {
            Article article = articleParser.parse(articleFile);
            this.cachedAuthors.addAll(article.getMetadata().getAuthors());
        }
    }

    private boolean isArticleFile(File file) {
        return file.getName().endsWith(MARKDOWN_FILE_SUFFIX);
    }

    public List<Author> listAuthors() {
        return this.cachedAuthors;
    }
}
