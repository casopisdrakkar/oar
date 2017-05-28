package sk.drakkar.oar.conversion;

import java.beans.PropertyChangeListener;
import java.beans.PropertyChangeSupport;

public class Summary {
	public static final String DEFAULT_COLOR = "gray";

	private String title;
	
	private String authors;
	
	private String summary;
	
	private String color = DEFAULT_COLOR;
	
	private String tags = "článek";

	private String shortSummary = "článek";

	private PropertyChangeSupport propertyChangeSupport = new PropertyChangeSupport(this);

	public Summary() {
		// empty constructor
	}

	/**
	 * A copy constructor.
	 * @param summary an existing summary
	 */
	public Summary(Summary summary) {
		this.title = summary.getTitle();
		this.authors = summary.getAuthors();
		this.summary = summary.getSummary();
		this.color = summary.getColor();
		this.tags = summary.getTags();
		this.shortSummary = summary.getShortSummary();
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		String oldTitle = this.title;
		this.title = title;
		propertyChangeSupport.firePropertyChange("title", oldTitle, title);
	}

	public String getAuthors() {
		return authors;
	}

	public void setAuthors(String authors) {
		String oldAuthors = this.authors;
		this.authors = authors;
		propertyChangeSupport.firePropertyChange("authors", oldAuthors, authors);
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
		String oldSummary = this.summary;
		this.summary = summary;
		propertyChangeSupport.firePropertyChange("summary", oldSummary, summary);
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		String oldColor = this.color;

		if(color == null || "-".equals(color) || color.trim().isEmpty()) {
			this.color = DEFAULT_COLOR;
		} else {
			this.color = color;
		}
		propertyChangeSupport.firePropertyChange("color", oldColor, color);
	}
	
	public String getTags() {
		return tags;
	}
	
	public void setTags(String tags) {
		String oldTags = this.tags;
		this.tags = tags;
		propertyChangeSupport.firePropertyChange("tags", oldTags, tags);
	}

	public String getShortSummary() {
		return shortSummary;
	}

	public void setShortSummary(String shortSummary) {
		String oldShortSummary = this.shortSummary;
		this.shortSummary = shortSummary;
		propertyChangeSupport.firePropertyChange("shortSummary", oldShortSummary, shortSummary);
	}
	
	public boolean hasShortSummary() {
		return this.shortSummary != null;
	}

	public void addPropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.addPropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(listener);
	}

	public void removePropertyChangeListener(String propertyName, PropertyChangeListener listener) {
		this.propertyChangeSupport.removePropertyChangeListener(propertyName, listener);
	}
}
