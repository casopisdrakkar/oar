package sk.drakkar.oar;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.google.common.base.Charsets;
import com.google.common.base.Joiner;
import com.google.common.base.Splitter;

public class ArticleParser {
	
	private static final Logger logger = LoggerFactory.getLogger(ArticleParser.class);
	
	private static final String YAML_DOCUMENT_SEPARATOR = "---";

	private SummaryExtractor summaryExtractor = new SummaryExtractor();
	
	public Article parse(File articleFile) {
		try {
			Article article = new Article();
			article.setSourceFile(articleFile);
			
			List<String> articleMarkdownSourceLines = com.google.common.io.Files.readLines(articleFile, Charsets.UTF_8);
			
			int lineNo = 0;
			for(String line : articleMarkdownSourceLines) {
				if(lineNo == 0 && !line.startsWith(YAML_DOCUMENT_SEPARATOR)) {
					throw new MissingArticleMetadataException("Missing YAML document separator line " + lineNo + " in " + articleFile);
				}
				if(lineNo > 0 && line.startsWith(YAML_DOCUMENT_SEPARATOR)) {
					break;
				}
				lineNo++;
			}
			List<String> articleMetadataSourceLines = articleMarkdownSourceLines.subList(0, lineNo);
			
			List<String> articleContentSourceLines = articleMarkdownSourceLines.subList(lineNo + 1, articleMarkdownSourceLines.size());
			article.setSource(Joiner.on("\n").join(articleContentSourceLines));
			
			Yaml yaml = new Yaml();
			
			@SuppressWarnings("unchecked") Map<String, Object> metadata 
				= yaml.loadAs(toString(articleMetadataSourceLines), Map.class);
			ArticleMetadata articleMetadata = new ArticleMetadata();
			articleMetadata.setTitle((String) metadata.get("Title"));
			articleMetadata.setAuthors(parseAuthors(metadata));
			articleMetadata.setTags(parseTags(metadata));
			articleMetadata.setSummary(summaryExtractor.getSummary(article, metadata));
			articleMetadata.setColor((String) metadata.get("Color"));
			setHasFulltext(articleMetadata, metadata);

			article.setMetadata(articleMetadata);
			
			
			logger.debug("Parsed an article from " + articleFile);
			
			return article;
		} catch (IOException e) {
			throw new ArticleProcessException("Cannot read article from " + articleFile, e);
		} 	
	}

	private void setHasFulltext(ArticleMetadata articleMetadata, Map<String, Object> rawMetadata) {
		boolean hasFulltext = false;
		if(rawMetadata.get("Fulltext") != null) {
			hasFulltext = (boolean) rawMetadata.get("Fulltext");
		}
		articleMetadata.setFulltext(hasFulltext);
	}

	private List<String> parseAuthors(Map<String, Object> metadata) {
		String authorsString = (String) metadata.get("Authors");
		List<String> authors = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(authorsString);
		return authors;
	}

	private List<String> parseTags(Map<String, Object> metadata) {
		String tagsString = (String) metadata.get("Tags");
		List<String> tags = Splitter.on(",").omitEmptyStrings().trimResults().splitToList(tagsString);
		return tags;
	}
	private String toString(List<String> articleMarkdownSourceLines) {
		return Joiner.on("\n").join(articleMarkdownSourceLines);
	}
}
