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
import sk.drakkar.oar.CzechCollatorUtils;
import sk.drakkar.oar.Slugger;
import sk.drakkar.oar.authors.AuthorListBuildingException;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.plugin.ConfigurablePlugin;

import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.Collection;
import java.util.Map;

public class TagDetailPageBuilder extends ConfigurablePlugin {
    private static final Logger logger = LoggerFactory.getLogger(TagDetailPageBuilder.class);

    private TagDetailPageTemplater tagDetailPageTemplater = new TagDetailPageTemplater();

    private Collator tagCollator = CzechCollatorUtils.getCaseInsensitiveCzechCollator();

    private Slugger slugger = new Slugger();

    private Multimap<String, Article> tagMap;

    private File tagPagesFolder;

    public TagDetailPageBuilder(Configuration configuration) {
        super(configuration);

        this.tagMap = MultimapBuilder.ListMultimapBuilder
                .treeKeys(tagCollator)
                .arrayListValues()
                .build();

        this.tagPagesFolder = new File(getConfiguration().getOutputFolder(), "tags");
        if(!this.tagPagesFolder.exists()) {
            this.tagPagesFolder.mkdirs();
        }
    }

    @Override
    public void articleProcessed(Article article, Context context) {
        for(String tag : article.getMetadata().getTags()) {
            tagMap.put(tag, article);
        }
    }

    @Override
    public void publicationComplete(Context context) {
        for (Map.Entry<String, Collection<Article>> entry : tagMap.asMap().entrySet()) {
            String tag = entry.getKey();
            Collection<Article> articles = sortByIssue(entry.getValue());

            writeTagDetailPage(tag, articles);
        }
        logger.info("Written tag detail pages.");
    }

    private Collection<Article> sortByIssue(Collection<Article> articles) {
        return ArticleByIssueComparator.sortByIssue(articles);
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
