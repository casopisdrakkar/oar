package sk.drakkar.oar.conversion;
public class Summary {
	private String title;
	
	private String authors;
	
	private String summary;
	
	private String color = "gray";
	
	private String tags = "èlánek";

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		this.authors = authors;
	}

	public void appendSummaryLine(String line) {
		if(this.summary == null) {
			this.summary = line;
		} else {
			this.summary = this.summary + "\n" + line;
		}
	}
	
	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	public String getTags() {
		return tags;
	}
	
	public void setTags(String tags) {
		this.tags = tags;
	}
}
