package sk.drakkar.oar.authors;

import java.io.File;
import java.io.IOException;
import java.text.Collator;
import java.util.Locale;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import sk.drakkar.oar.*;

import com.google.common.base.Charsets;
import com.google.common.collect.Multimap;
import com.google.common.collect.MultimapBuilder.ListMultimapBuilder;
import com.google.common.io.Files;
import sk.drakkar.oar.plugin.Plugin;

public class AuthorListBuilder implements Plugin {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthorListBuilder.class);
	
	private Configuration configuration;

	private AuthorTemplater authorTemplater = new AuthorTemplater();
	
	private Multimap<String, Article> authorMap;
	
	public AuthorListBuilder(Configuration configuration) {
		this.configuration = configuration;
		
		authorMap = ListMultimapBuilder
			.treeKeys(getCaseInsensitiveCzechCollator())
			.arrayListValues()
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
			File outputFile = new File(this.configuration.getOutputFolder(), "authors.html");
			Files.write(html, outputFile, Charsets.UTF_8);
		} catch (IOException e) {
			throw new AuthorListBuildingException("Unable to write author list", e);
		}
	}

	@Override
	public void issueArticlesProcessed(Issue issue) {
		logger.info("Building author list " + issue.getNumber());
		
		for (Article article : issue.getArticles()) {
			for(String author : article.getMetadata().getAuthors()) {
				authorMap.put(author, article);
			}
		}
	}

	@Override
	public void publicationComplete() {
		String html = authorTemplater.convert(this.authorMap);
		write(html);
		
		logger.info("Written author list.");
	}	
}
