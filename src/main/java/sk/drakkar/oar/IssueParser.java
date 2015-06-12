package sk.drakkar.oar;

import java.io.File;
import java.io.IOException;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.yaml.snakeyaml.Yaml;

import com.google.common.base.Charsets;
import com.google.common.io.Files;

public class IssueParser {
	
	private static final Logger logger = LoggerFactory.getLogger(IssueParser.class);
	
	private static final String ISSUE_METADATA_KEY_PDF_FILE_NAME = "pdf";
	private static final String ISSUE_METADATA_KEY_ARTICLES = "articles";
	
	public static final String ISSUE_METADATA_FILENAME = "metadata.yaml";

	public Issue toIssue(File issueFolder) {
		Issue issue = new Issue();
		issue.setFolder(issueFolder);
		issue.setNumber(Integer.parseInt(issueFolder.getName()));
		
		loadIssueMetadata(issue);
		
		return issue;
	}

	@SuppressWarnings("unchecked")
	protected void loadIssueMetadata(Issue issue) {
		try {
			Yaml yaml = new Yaml();
			
			File metadataFile = new File(issue.getFolder(), ISSUE_METADATA_FILENAME);
			Map<String, Object> yamlIssueMetadata = (Map<String, Object>) yaml.load(Files.toString(metadataFile, Charsets.UTF_8));
			
			logger.debug(yamlIssueMetadata.toString());
			
			String pdfFileName = (String) yamlIssueMetadata.get(ISSUE_METADATA_KEY_PDF_FILE_NAME);
			if(pdfFileName == null) {
				throw new IssueMetadataException("No '" + ISSUE_METADATA_KEY_PDF_FILE_NAME + "' filename is stated in the issue metadata file");
			}
			issue.setPdfFileName(pdfFileName);
			
			List<String> articleFileNames = (List<String>) yamlIssueMetadata.get(ISSUE_METADATA_KEY_ARTICLES);
			if(articleFileNames == null) {
				articleFileNames = Collections.emptyList();
			}
			issue.setArticleOrder(articleFileNames);
			
		} catch (IOException e) {
			throw new IssueMetadataException("Cannot load issue metadata! Is the file " + ISSUE_METADATA_FILENAME + " present?", e);
		}
	}

	/**
	 * Updates Issue color. Issue color is defined as color of majority of Issue articles.
	 */
	public void updateIssueColor(Issue issue) {
		Map<String,Integer> colors = new HashMap<>();

		for(Article a : issue.getArticles()) {
			String color = a.getMetadata().getColor();
			if (colors.containsKey(color)) {
				colors.put(color, colors.get(color) + 1);
			} else {
				colors.put(color, 1);
			}
		}

		String issueColor = Collections.max(
				colors.entrySet(), (entry1, entry2) -> entry1.getValue() > entry2.getValue() ? 1 : -1).getKey();

		String color = (issueColor != null) ? issueColor : Issue.DEFAULT_ISSUE_COLOR;
		issue.setColor(color);
	}
}
