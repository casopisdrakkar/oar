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
import sk.drakkar.oar.plugin.DefaultPlugin;

import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.Locale;

public class TagCloudBuilder extends DefaultPlugin {
	
	private static final Logger logger = LoggerFactory.getLogger(TagCloudBuilder.class);
	
	private TagCloudTemplater tagCloudTemplater = new TagCloudTemplater();
	private Configuration configuration;

	private Multimap<String, Article> tagMap;

	public TagCloudBuilder(Configuration configuration) {
		this.configuration = configuration;
		
		tagMap = ListMultimapBuilder
			.treeKeys(getCaseInsensitiveCzechCollator())
			.treeSetValues(ArticleByIssueComparator.INSTANCE)
			.build();		
	}
	
	private Collator getCaseInsensitiveCzechCollator() {
		Collator collator = Collator.getInstance(new Locale("cz"));
		collator.setStrength(Collator.SECONDARY);
		collator.setDecomposition(Collator.CANONICAL_DECOMPOSITION);
		
		return collator;
	}
	
	private void write(String html) {
		try {
			File outputFile = new File(this.configuration.getOutputFolder(), "tags.html");
			Files.write(html, outputFile, Charsets.UTF_8);
		} catch (IOException e) {
			throw new TagCloudException("Unable to write tag cloud", e);
		}
	}

	@Override
	public void articleProcessed(Article article) {
		for(String tag : article.getMetadata().getTags()) {
			tagMap.put(tag, article);
		}
	}

	@Override
	public void publicationComplete() {
		String html = tagCloudTemplater.convert(tagMap);
		write(html);
		
		logger.info("Written tag cloud.");
	}
	
}
