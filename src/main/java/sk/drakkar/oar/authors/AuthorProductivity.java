package sk.drakkar.oar.authors;

public class AuthorProductivity {
    private Author author;

    private int productivity;

    public AuthorProductivity() {
        // empty constructor
    }

    public AuthorProductivity(Author author, int productivity) {
        this.author = author;
        this.productivity = productivity;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public int getProductivity() {
        return productivity;
    }

    public void setProductivity(int productivity) {
        this.productivity = productivity;
    }
}
