package sk.drakkar.oar.conversion;
public class Summary {
	public static final String DEFAULT_COLOR = "gray";

	private String title;
	
	private String authors;
	
	private String summary;
	
	private String color = DEFAULT_COLOR;
	
	private String tags = "èlánek";

	private String shortSummary;
	
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
		if(color == null || "-".equals(color) || color.trim().isEmpty()) {
			this.color = DEFAULT_COLOR;
		} else {
			this.color = color;
		}
	}
	
	public String getTags() {
		return tags;
	}
	
	public void setTags(String tags) {
		this.tags = tags;
	}

	public String getShortSummary() {
		return shortSummary;
	}

	public void setShortSummary(String shortSummary) {
		this.shortSummary = shortSummary;
	}
	
	public boolean hasShortSummary() {
		return this.shortSummary != null;
	}
}
