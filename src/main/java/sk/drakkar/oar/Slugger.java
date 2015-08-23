package sk.drakkar.oar;

public class Slugger {
    private static final String DIACRITICS = " ľščťžýáíéúäňřěôĺŕůabcdefghijklmnopqrstuvwxyz0123456789";

    private static final String REMOVED_DIACRITICS = "-lsctzyaieuanreolruabcdefghijklmnopqrstuvwxyz0123456789";

    private static final int NOT_FOUND = -1;

    public String toSlug(String value) {
        StringBuilder slug = new StringBuilder(value.length());
        for (Character c : value.toLowerCase().toCharArray()) {
            int position = DIACRITICS.indexOf(c);
            if (position == NOT_FOUND) {
                slug.append("");
            } else {
                slug.append(REMOVED_DIACRITICS.charAt(position));
            }
        }
        return slug.toString();
    }
}
