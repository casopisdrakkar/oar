package sk.drakkar.oar.authors;

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
import java.util.List;

public class AuthorListBuilder extends DefaultPlugin {
	
	private static final Logger logger = LoggerFactory.getLogger(AuthorListBuilder.class);
	
	private Configuration configuration;

	private AuthorListTemplater authorListTemplater = new AuthorListTemplater();
	
	private Multimap<Author, Article> authorMap;

	private boolean removingNicknameNames = true;
	
	public AuthorListBuilder(Configuration configuration) {
		this.configuration = configuration;
		
		authorMap = ListMultimapBuilder
			.treeKeys(AuthorByNameComparator.INSTANCE)
			.treeSetValues(ArticleByIssueComparator.INSTANCE)
			.build();
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
	public void articleProcessed(Article article) {
		for(Author author : article.getMetadata().getAuthors()) {
			authorMap.put(author, article);
		}
	}

	@Override
	public void publicationComplete() {
		String html = authorListTemplater.convert(this.authorMap);
		write(html);
		
		logger.info("Written author list.");
	}

	public void setIgnoredMostProductiveAuthorNames(List<String> authorNames) {
		this.authorListTemplater.setIgnoredMostProductiveAuthorNames(authorNames);
	}

}
