package sk.drakkar.oar;

import com.google.common.io.Files;

import java.io.File;
import java.util.Objects;

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

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		Article article = (Article) o;
		return Objects.equals(getSource(), article.getSource()) &&
				Objects.equals(getSourceFile(), article.getSourceFile()) &&
				Objects.equals(getMetadata(), article.getMetadata()) &&
				Objects.equals(getHtmlSource(), article.getHtmlSource());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getSource(), getSourceFile(), getMetadata(), getHtmlSource());
	}

	@Override
	public String toString() {
		return new StringBuilder()
				.append(getMetadata().getTitle())
				.append(" (#")
				.append(getIssue().getNumber())
				.append(")")
				.toString();
	}
}
