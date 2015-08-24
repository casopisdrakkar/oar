package sk.drakkar.oar.authors;

import java.text.Collator;
import java.util.Comparator;
import java.util.Locale;

public class AuthorByNameComparator implements Comparator<Author> {
    public static final Comparator<Author> INSTANCE = new AuthorByNameComparator();

    @Override
    public int compare(Author author, Author anotherAuthor) {
        return getCaseInsensitiveCzechCollator()
                .compare(author.getFullName(), anotherAuthor.getFullName());
    }

    private Collator getCaseInsensitiveCzechCollator() {
        Collator collator = Collator.getInstance(new Locale("cz"));
        collator.setStrength(Collator.SECONDARY);
        collator.setDecomposition(Collator.CANONICAL_DECOMPOSITION);

        return collator;
    }
}
