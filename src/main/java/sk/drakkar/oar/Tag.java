package sk.drakkar.oar;

public class Tag {
    private final Slugger slugger = new Slugger();

    private final String value;

    public Tag(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public String getSlug() {
        return this.slugger.toSlug(value);
    }

    @Override
    public String toString() {
        return this.value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Tag)) return false;

        Tag tag = (Tag) o;

        return getValue().equals(tag.getValue());
    }

    @Override
    public int hashCode() {
        return getValue().hashCode();
    }
}
