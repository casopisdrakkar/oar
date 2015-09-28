package sk.drakkar.oar.authors;

import com.google.common.base.Charsets;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.Article;
import sk.drakkar.oar.ArticleByIssueComparator;
import sk.drakkar.oar.Configuration;
import sk.drakkar.oar.Slugger;
import sk.drakkar.oar.plugin.DefaultPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class AuthorProfilePageBuilder extends DefaultPlugin {
    private static final Logger logger = LoggerFactory.getLogger(AuthorProfilePageBuilder.class);

    private Configuration configuration;

    private AuthorProfilePageTemplater authorProfilePageTemplater = new AuthorProfilePageTemplater();

    private Slugger slugger = new Slugger();

    private Multimap<Author, Article> authorMap;

    private File authorProfilesFolder;

    public AuthorProfilePageBuilder(Configuration configuration) {
        this.configuration = configuration;

        this.authorMap = MultimapBuilder
                .treeKeys(AuthorByNameComparator.INSTANCE)
                .arrayListValues()
                .build();

        this.authorProfilesFolder = new File(this.configuration.getOutputFolder(), "authors");
        if(!this.authorProfilesFolder.exists()) {
            this.authorProfilesFolder.mkdirs();
        }
    }

    @Override
    public void articleProcessed(Article article) {
        for(Author author : article.getMetadata().getAuthors()) {
            this.authorMap.put(author, article);
        }
    }

    @Override
    public void publicationComplete() {
        for (Map.Entry<Author, Collection<Article>> entry : this.authorMap.asMap().entrySet()) {
            Author author = entry.getKey();
            Collection<Article> articles = sortByIssue(entry.getValue());
            writeProfilePage(author, articles);
        }
        logger.info("Written author profile pages.");
    }

    private Collection<Article> sortByIssue(Collection<Article> articles) {
        return ArticleByIssueComparator.sortByIssue(articles);
    }

    private void writeProfilePage(Author author, Collection<Article> articles) {
        String html = this.authorProfilePageTemplater.convert(author, articles);
        String authorSlug = this.slugger.toSlug(author.getFullName());

        write(authorSlug, html);
    }

    private void write(String authorSlug, String html) {
        try {
            File outputFile = new File(this.authorProfilesFolder, authorSlug + ".html");
            Files.write(html, outputFile, Charsets.UTF_8);

            logger.debug("Written profile page for '" + authorSlug + "'");
        } catch (IOException e) {
            throw new AuthorListBuildingException("Unable to write author list", e);
        }
    }
}
