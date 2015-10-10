package sk.drakkar.oar.authors;

import com.google.common.base.Charsets;
import com.google.common.collect.Multimap;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.Article;
import sk.drakkar.oar.Configuration;
import sk.drakkar.oar.Slugger;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.plugin.ConfigurablePlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class AuthorProfilePageBuilder extends ConfigurablePlugin {
    private static final Logger logger = LoggerFactory.getLogger(AuthorProfilePageBuilder.class);

    public static final String AUTHOR_PROFILES_FOLDER = "authors";

    private AuthorProfilePageTemplater authorProfilePageTemplater = new AuthorProfilePageTemplater();

    private Slugger slugger = new Slugger();

    private File authorProfilesFolder;

    public AuthorProfilePageBuilder(Configuration configuration) {
        super(configuration);

        this.authorProfilesFolder = configuration.getOrCreateOutputSubfolder(AUTHOR_PROFILES_FOLDER);
    }

    @Override
    public void publicationComplete(Context context) {
        Multimap<Author, Article> authorMap = context.get(AuthorArticlesCollector.ContextVariables.authorArticles);

        for (Map.Entry<Author, Collection<Article>> entry : authorMap.asMap().entrySet()) {
            Author author = entry.getKey();
            Collection<Article> articles = entry.getValue();
            writeProfilePage(author, articles);
        }
        logger.info("Written author profile pages.");
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
