package sk.drakkar.oar.tags;

import com.google.common.base.Charsets;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder.ListMultimapBuilder;
import com.google.common.io.Files;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import sk.drakkar.oar.Article;
import sk.drakkar.oar.ArticleByIssueComparator;
import sk.drakkar.oar.Configuration;
import sk.drakkar.oar.CzechCollatorUtils;
import sk.drakkar.oar.pipeline.Context;
import sk.drakkar.oar.plugin.ConfigurableArticleAssemblyPlugin;
import sk.drakkar.oar.plugin.PortalAssemblyPlugin;

import java.io.File;
import java.io.IOException;
import java.text.Collator;

public class TagCloudBuilder extends ConfigurableArticleAssemblyPlugin implements PortalAssemblyPlugin {
	
	private static final Logger logger = LoggerFactory.getLogger(TagCloudBuilder.class);
	
	private TagCloudTemplater tagCloudTemplater = new TagCloudTemplater();

	public static final Collator tagCollator = CzechCollatorUtils.getCaseInsensitiveCzechCollator();

	private Multimap<String, Article> tagMap;

	public TagCloudBuilder(Configuration configuration) {
		super(configuration);

		tagMap = ListMultimapBuilder
			.treeKeys(tagCollator)
			.treeSetValues(ArticleByIssueComparator.INSTANCE)
			.build();		
	}

	private void write(String html) {
		try {
			File outputFile = new File(getConfiguration().getOutputFolder(), "tags.html");
			Files.write(html, outputFile, Charsets.UTF_8);
		} catch (IOException e) {
			throw new TagCloudException("Unable to write tag cloud", e);
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
		String html = tagCloudTemplater.convert(tagMap);
		write(html);
		
		logger.info("Written tag cloud.");
	}
	
}
