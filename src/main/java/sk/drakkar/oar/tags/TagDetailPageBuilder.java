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
import sk.drakkar.oar.Tag;
import sk.drakkar.oar.TagCollator;
import sk.drakkar.oar.authors.AuthorListBuildingException;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.plugin.ConfigurableArticleAssemblyPlugin;
import sk.drakkar.oar.plugin.PortalAssemblyPlugin;

import java.io.File;
import java.io.IOException;
import java.util.Collection;
import java.util.Map;

public class TagDetailPageBuilder extends ConfigurableArticleAssemblyPlugin implements PortalAssemblyPlugin {
    private static final Logger logger = LoggerFactory.getLogger(TagDetailPageBuilder.class);

    public static final String TAGS_OUTPUT_FOLDER = "tags";

    private TagDetailPageTemplater tagDetailPageTemplater = new TagDetailPageTemplater();

    private Multimap<Tag, Article> tagMap;

    private File tagPagesFolder;

    public TagDetailPageBuilder(Configuration configuration) {
        super(configuration);

        this.tagMap = MultimapBuilder.ListMultimapBuilder
                .treeKeys(TagCollator.INSTANCE)
                .arrayListValues()
                .build();

        this.tagPagesFolder = getConfiguration().getOrCreateOutputSubfolder(TAGS_OUTPUT_FOLDER);
    }

    @Override
    public void articleProcessed(Article article, Context context) {
        for(Tag tag : article.getMetadata().getTags()) {
            tagMap.put(tag, article);
        }
    }

    @Override
    public void publicationComplete(Context context) {
        for (Map.Entry<Tag, Collection<Article>> entry : tagMap.asMap().entrySet()) {
            Tag tag = entry.getKey();
            Collection<Article> articles = sortByIssue(entry.getValue());

            writeTagDetailPage(tag, articles);
        }
        logger.info("Written tag detail pages.");
    }

    private Collection<Article> sortByIssue(Collection<Article> articles) {
        return ArticleByIssueComparator.sortByIssue(articles);
    }

    private void writeTagDetailPage(Tag tag, Collection<Article> articles) {
        String html = this.tagDetailPageTemplater.convert(tag, articles);
        write(tag, html);
    }


    private void write(Tag tag, String html) {
        try {
            File outputFile = new File(this.tagPagesFolder, tag.getSlug() + ".html");
            Files.write(html, outputFile, Charsets.UTF_8);

            logger.debug("Written tag page for '" + tag + "'");
        } catch (IOException e) {
            throw new AuthorListBuildingException("Unable to write author list", e);
        }
    }

}
