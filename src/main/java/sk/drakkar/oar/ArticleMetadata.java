package sk.drakkar.oar;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;
import sk.drakkar.oar.authors.Author;

import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

public class ArticleMetadata {
	private String title;
	
	private List<Author> authors = new LinkedList<Author>();
	
	private List<Tag> tags = new LinkedList<Tag>();

	private String summary;
	
	private String color;
	
	private boolean fulltext;

	public String discussionUrl;
	
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
	
	public List<Tag> getTags() {
		return tags;
	}

	/**
	 * Return comma separated tag list.
	 */
	public String getTagList() {
		return Joiner.on(", ").join(this.tags).toString();
	}

	public void setTagValues(List<String> tagValues) {
		List<Tag> tags = tagValues.stream()
				.map(Tag::new)
				.collect(Collectors.toList());
		setTags(tags);
	}

	public void setTags(List<Tag> tags) {
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

	public String getDiscussionUrl() {
		return discussionUrl;
	}

	public void setDiscussionUrl(String discussionUrl) {
		this.discussionUrl = discussionUrl;
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
				Objects.equals(getColor(), that.getColor()) &&
				Objects.equals(getDiscussionUrl(), that.getDiscussionUrl());
	}

	@Override
	public int hashCode() {
		return Objects.hash(getTitle(), getAuthors(), getTags(), getSummary(), getColor(), isFulltext(),
				getDiscussionUrl());
	}
}
