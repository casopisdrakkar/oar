package sk.drakkar.oar;

import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class ReleaseDate {
    private int year;

    private int month;

    private static final List<String> rawCzechMonthNames = Arrays.asList(
            "leden", "unor", "brezen",
            "duben", "kveten", "cerven",
            "cervenec", "srpen", "zari",
            "rijen", "listopad", "prosinec"
    );

    public ReleaseDate() {
        // empty constructor
    }

    /**
     * Create a new release date from numeric year and 1-based month.
     */
    public ReleaseDate(int year, int month) {
        this.year = year;
        if(month < 1 || month > 12) {
            throw new IllegalArgumentException("Month must be between 1--12");
        }
        this.month = month;
    }

    public static ReleaseDate parseFromRawCzechMonthName(int year, String rawCzechMonthName) {
        int zeroBasedMonth = rawCzechMonthNames.indexOf(rawCzechMonthName);
        if(zeroBasedMonth < 0) {
            throw new IllegalArgumentException("Illegal month name (should be without diacritics, e. g. 'zari'): "
                    + rawCzechMonthName);
        }
        return new ReleaseDate(year, zeroBasedMonth + 1);
    }

    public int getYear() {
        return year;
    }

    public int getMonth() {
        return month;
    }

    public String getDescriptiveMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.MONTH, month - 1);
        Locale czechLocale = new Locale("cs", "CZ");
        return calendar.getDisplayName(Calendar.MONTH, Calendar.LONG_STANDALONE, czechLocale);
    }

    @Override
    public String toString() {
        return getDescriptiveMonth() + " " + getYear();
    }
}
