package sk.drakkar.oar;

/**
 * Generates a CSS color for an arbitrary string.
 * <p>
 *     As of now, the string hash-code is used to generate
 *     a numerical index of the actual color.
 * </p>
 */
public class ColorGenerator {
    public enum Color {
        RED("#c13f35"),
        GREEN("#237238"),
        BLUE("#155985"),
        GRAY("#4b4b4b"),
        ORANGE("#F1610D"),
        VIOLET("#A51C52"),
        MAROON("#53171b");

        private String hexColor;

        Color(String hexColor) {
            this.hexColor = hexColor;
        }

        public String getHexColor() {
            return hexColor;
        }
    }

    public String getColor(String input) {
        int index = Math.abs(input.hashCode() % Color.values().length);
        return Color.values()[index].name().toLowerCase();
    }
}
