package sk.drakkar.oar.authors;

import sk.drakkar.oar.Slugger;

public class Author {
    private Slugger slugger = new Slugger();

    private String fullName;

    public String getFullName() {
        return this.fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getSlug() {
        return slugger.toSlug(this.getFullName());
    }

    public void setSlugger(Slugger slugger) {
        this.slugger = slugger;
    }

    public static Author parse(String fullName) {
        return Author.parse(fullName, true);
    }

    /**
     * Parses the author from the full name, optionally removing quotes from
     * nicknames.
     * <p>
     *     Authors with just the nick names (no first name or last name)
     *     may have the surrounding quotes removed.
     *     Thus  <code>„Alhmar”</code> becomes <code>Alhmar</code>.
     * </p>
     *
     *
     * @param fullName
     * @param removeNickNameQuotes 	Remove introductory quotes from authors with a nickname
     */
    public static Author parse(String fullName, boolean removeNickNameQuotes) {
        String sanitizedFullName = removeNickNameQuotes(fullName);

        Author author = new Author();
        author.setFullName(sanitizedFullName);

        return author;
    }

    private static String removeNickNameQuotes(String author) {
        if(author.startsWith("„") && author.endsWith("“")) {
            return author.substring(1, author.length() - 1);
        }
        return author;
    }

    @Override
    public String toString() {
        return getFullName();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        Author author = (Author) o;

        return getFullName().equals(author.getFullName());

    }

    @Override
    public int hashCode() {
        return getFullName().hashCode();
    }
}
