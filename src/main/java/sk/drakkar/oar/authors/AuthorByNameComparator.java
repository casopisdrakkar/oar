package sk.drakkar.oar.authors;

import sk.drakkar.oar.CzechCollatorUtils;

import java.text.Collator;
import java.util.Comparator;

public class AuthorByNameComparator implements Comparator<Author> {
    public static final Comparator<Author> INSTANCE = new AuthorByNameComparator();

    public static final Collator collator = CzechCollatorUtils.getCaseInsensitiveCzechCollator();

    @Override
    public int compare(Author author, Author anotherAuthor) {
        return collator.compare(author.getFullName(), anotherAuthor.getFullName());
    }
}
