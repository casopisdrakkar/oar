package sk.drakkar.oar.authors;

import com.google.common.base.Charsets;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.Article;
import sk.drakkar.oar.Configuration;
import sk.drakkar.oar.Issue;
import sk.drakkar.oar.Slugger;
import sk.drakkar.oar.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public class AuthorProfilePageBuilder implements Plugin {
    private static final Logger logger = LoggerFactory.getLogger(AuthorProfilePageBuilder.class);

    private Configuration configuration;

    private AuthorProfilePageTemplater authorProfilePageTemplater = new AuthorProfilePageTemplater();

    private Slugger slugger = new Slugger();

    private Multimap<String, Article> authorMap;

    private File authorProfilesFolder;

    public AuthorProfilePageBuilder(Configuration configuration) {
        this.configuration = configuration;

        this.authorMap = MultimapBuilder.ListMultimapBuilder
                .treeKeys(getCaseInsensitiveCzechCollator())
                .arrayListValues()
                .build();

        this.authorProfilesFolder = new File(this.configuration.getOutputFolder(), "authors");
        if(!this.authorProfilesFolder.exists()) {
            this.authorProfilesFolder.mkdirs();
        }
    }

    private Collator getCaseInsensitiveCzechCollator() {
        Collator collator = Collator.getInstance(new Locale("cz"));
        collator.setStrength(Collator.SECONDARY);
        collator.setDecomposition(Collator.CANONICAL_DECOMPOSITION);

        return collator;
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

    @Override
    public void issueArticlesProcessed(Issue issue) {
        logger.info("Building author profile page list " + issue.getNumber());

        for (Article article : issue.getArticles()) {
            for(String author : article.getMetadata().getAuthors()) {
                authorMap.put(author, article);
            }
        }
    }

    @Override
    public void publicationComplete() {
        for (Map.Entry<String, Collection<Article>> entry : authorMap.asMap().entrySet()) {
            String author = entry.getKey();
            Collection<Article> articles = entry.getValue();

            String html = this.authorProfilePageTemplater.convert(author, articles);
            String authorSlug = slugger.toSlug(author);

            write(authorSlug, html);
        }

        logger.info("Written author profile pages.");
    }

}
