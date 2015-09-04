package sk.drakkar.oar;

import java.text.Collator;
import java.util.Locale;

public abstract class CzechCollatorUtils {
    public static Collator getCaseInsensitiveCzechCollator() {
        Collator collator = Collator.getInstance(new Locale("cz"));
        collator.setStrength(Collator.SECONDARY);
        collator.setDecomposition(Collator.CANONICAL_DECOMPOSITION);

        return collator;
    }

}
