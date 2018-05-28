package sk.drakkar.oar;

import java.text.Collator;
import java.util.Comparator;

public class TagCollator implements Comparator<Tag> {
    public static final TagCollator INSTANCE = new TagCollator();

    private Collator valueCollator = CzechCollatorUtils.getCaseInsensitiveCzechCollator();

    @Override
    public int compare(Tag tag1, Tag tag2) {
        return valueCollator.compare(tag1.getValue(), tag2.getValue());
    }
}
