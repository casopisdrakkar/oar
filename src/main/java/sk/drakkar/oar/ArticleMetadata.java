package sk.drakkar.oar;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import sk.drakkar.oar.authors.Author;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;

public class ArticleMetadata {
	private String title;
	
	private List<Author> authors = new LinkedList<Author>();
	
	private List<String> tags = new LinkedList<String>();

	private String summary;
	
	private String color;
	
	private boolean fulltext;
	
	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<Author> getAuthors() {
		return authors;
	}
	
	public String getAuthorList() {
		return Joiner.on(", ").join(this.authors).toString();
	}

	public void setAuthors(List<Author> authors) {
		this.authors = authors;
	}
	
	public String getSummary() {
		return summary;
	}
	
	public void setSummary(String summary) {
		this.summary = summary;
	}
	
	public List<String> getTags() {
		return tags;
	}

	/**
	 * Return comma separated tag list.
	 */
	public String getTagList() {
		return Joiner.on(", ").join(this.tags).toString();
	}

	public void setTags(List<String> tags) {
		this.tags = tags;
	}
	
	public String getColor() {
		return color;
	}
	
	public void setColor(String color) {
		this.color = color;
	}
	
	public void setFulltext(boolean hasFulltext) {
		this.fulltext = hasFulltext;
	}	
	
	public boolean isFulltext() {
		return fulltext;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("title", this.title)
			.add("authors", this.authors)
			.toString();
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) return true;
		if (o == null || getClass() != o.getClass()) return false;
		ArticleMetadata that = (ArticleMetadata) o;
		return Objects.equals(isFulltext(), that.isFulltext()) &&
				Objects.equals(getTitle(), that.getTitle()) &&
				Objects.equals(getAuthors(), that.getAuthors()) &&
				Objects.equals(getTags(), that.getTags()) &&
				Objects.equals(getSummary(), that.getSummary()) &&
				Objects.equals(getColor(), that.getColor());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getTitle(), getAuthors(), getTags(), getSummary(), getColor(), isFulltext());
	}
}
