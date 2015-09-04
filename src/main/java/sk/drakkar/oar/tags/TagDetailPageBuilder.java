package sk.drakkar.oar.tags;

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
import sk.drakkar.oar.authors.AuthorListBuildingException;
import sk.drakkar.oar.plugin.DefaultPlugin;

import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.Collection;
import java.util.Locale;
import java.util.Map;

public class TagDetailPageBuilder extends DefaultPlugin {
    private static final Logger logger = LoggerFactory.getLogger(TagDetailPageBuilder.class);

    private Configuration configuration;

    private TagDetailPageTemplater tagDetailPageTemplater = new TagDetailPageTemplater();

    private Slugger slugger = new Slugger();

    private Multimap<String, Article> tagMap;

    private File tagPagesFolder;

    public TagDetailPageBuilder(Configuration configuration) {
        this.configuration = configuration;

        this.tagMap = MultimapBuilder.ListMultimapBuilder
                .treeKeys(getCaseInsensitiveCzechCollator())
                .treeSetValues(ArticleByIssueComparator.INSTANCE)
                .build();

        this.tagPagesFolder = new File(this.configuration.getOutputFolder(), "tags");
        if(!this.tagPagesFolder.exists()) {
            this.tagPagesFolder.mkdirs();
        }
    }

    private Collator getCaseInsensitiveCzechCollator() {
        Collator collator = Collator.getInstance(new Locale("cz"));
        collator.setStrength(Collator.SECONDARY);
        collator.setDecomposition(Collator.CANONICAL_DECOMPOSITION);

        return collator;
    }

    @Override
    public void articleProcessed(Article article) {
        for(String tag : article.getMetadata().getTags()) {
            tagMap.put(tag, article);
        }
    }

    @Override
    public void publicationComplete() {
        for (Map.Entry<String, Collection<Article>> entry : tagMap.asMap().entrySet()) {
            String tag = entry.getKey();
            Collection<Article> articles = entry.getValue();

            writeTagDetailPage(tag, articles);
        }
        logger.info("Written tag detail pages.");
    }

    private void writeTagDetailPage(String tag, Collection<Article> articles) {
        String html = this.tagDetailPageTemplater.convert(tag, articles);
        String tagSlug = this.slugger.toSlug(tag);

        write(tagSlug, html);
    }


    private void write(String authorSlug, String html) {
        try {
            File outputFile = new File(this.tagPagesFolder, authorSlug + ".html");
            Files.write(html, outputFile, Charsets.UTF_8);

            logger.debug("Written profile page for '" + authorSlug + "'");
        } catch (IOException e) {
            throw new AuthorListBuildingException("Unable to write author list", e);
        }
    }

}
