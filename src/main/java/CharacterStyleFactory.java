import java.util.HashMap;
import java.util.Map;

public class CharacterStyleFactory {

    private final Map<String, CharacterStyle> styles = new HashMap<>();

    public CharacterStyle getStyle(String font, int size, String color) {
        String key = font + "|" + size + "|" + color;

        return styles.computeIfAbsent(key,
                k -> new CharacterStyle(font, size, color));
    }

    public int getStyleCount() {
        return styles.size();
    }
}