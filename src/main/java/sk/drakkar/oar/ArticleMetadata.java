package sk.drakkar.oar;

import java.util.LinkedList;
import java.util.List;

import com.google.common.base.Joiner;
import com.google.common.base.MoreObjects;

public class ArticleMetadata {
	private String title;
	
	private List<String> authors = new LinkedList<String>();
	
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

	public List<String> getAuthors() {
		return authors;
	}
	
	public String getAuthorList() {
		return Joiner.on(", ").join(this.authors).toString();
	}

	public void setAuthors(List<String> authors) {
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


}
