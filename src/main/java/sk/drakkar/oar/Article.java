package sk.drakkar.oar;

import java.io.File;

import com.google.common.io.Files;

public class Article {
	private String source;

	private Issue issue;
	
	private File sourceFile;
	
	private ArticleMetadata metadata;
	
	private String htmlSource;
	
	public String getSlug() {
		return Files.getNameWithoutExtension(sourceFile.getName());
	}
	
	public String getSource() {
		return source;
	}

	public void setSource(String source) {
		this.source = source;
	}
	
	public Issue getIssue() {
		return issue;
	}
	
	public void setIssue(Issue issue) {
		this.issue = issue;
	}
	
	public void setSourceFile(File sourceFile) {
		this.sourceFile = sourceFile;
	}
	
	public File getSourceFile() {
		return sourceFile;
	}
	
	public void setMetadata(ArticleMetadata metadata) {
		this.metadata = metadata;
	}
	
	public ArticleMetadata getMetadata() {
		return metadata;
	}
	
	public String getHtmlSource() {
		return htmlSource;
	}
	
	public void setHtmlSource(String htmlSource) {
		this.htmlSource = htmlSource;
	}
}
