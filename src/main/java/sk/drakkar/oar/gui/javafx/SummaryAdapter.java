package sk.drakkar.oar.gui.javafx;

import javafx.beans.property.StringProperty;
import javafx.beans.property.adapter.JavaBeanStringPropertyBuilder;
import sk.drakkar.oar.conversion.Summary;

public class SummaryAdapter {
    private Summary delegate = new Summary();

    private StringProperty title;

    private StringProperty authors;

    private StringProperty summary;

    private StringProperty color;

    private StringProperty tags;

    private StringProperty shortSummary;

    public SummaryAdapter(Summary delegate) {
        this.delegate = delegate;

        try {
            JavaBeanStringPropertyBuilder propertyBuilder = JavaBeanStringPropertyBuilder.create().bean(this.delegate);
            this.title = propertyBuilder.name("title").build();
            this.authors = propertyBuilder.name("authors").build();
            this.summary = propertyBuilder.name("summary").build();
            this.color = propertyBuilder.name("color").build();
            this.tags = propertyBuilder.name("tags").build();
            this.shortSummary = propertyBuilder.name("shortSummary").build();
        } catch (NoSuchMethodException e) {
            throw new IllegalStateException(e);
        }
    }

    public Summary getWrappedSummary() {
        return this.delegate;
    }

    public String getTitle() {
        return title.get();
    }

    public StringProperty titleProperty() {
        return title;
    }

    public void setTitle(String title) {
        this.title.set(title);
    }

    public String getAuthors() {
        return authors.get();
    }

    public StringProperty authorsProperty() {
        return authors;
    }

    public void setAuthors(String authors) {
        this.authors.set(authors);
    }

    public String getSummary() {
        return summary.get();
    }

    public StringProperty summaryProperty() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary.set(summary);
    }

    public String getColor() {
        return color.get();
    }

    public StringProperty colorProperty() {
        return color;
    }

    public void setColor(String color) {
        this.color.set(color);
    }

    public String getTags() {
        return tags.get();
    }

    public StringProperty tagsProperty() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags.set(tags);
    }

    public String getShortSummary() {
        return shortSummary.get();
    }

    public StringProperty shortSummaryProperty() {
        return shortSummary;
    }

    public void setShortSummary(String shortSummary) {
        this.shortSummary.set(shortSummary);
    }
}
