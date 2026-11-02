import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

public class TextDocument {

    private final List<TextCharacter> characters = new ArrayList<>();
    private final CharacterStyleFactory factory;

    public TextDocument(CharacterStyleFactory factory) {
        this.factory = factory;
    }

    public void addCharacter(char value, String font, int size, String color) {
        CharacterStyle style = factory.getStyle(font, size, color);
        characters.add(new TextCharacter(value, style));
    }

    public void editCharacter(int index, char value) {
        characters.get(index).setValue(value);
    }

    public void changeStyle(int index, String font, int size, String color) {
        CharacterStyle style = factory.getStyle(font, size, color);
        characters.get(index).setStyle(style);
    }

    public String getText() {
        StringBuilder text = new StringBuilder();

        for (TextCharacter character : characters) {
            text.append(character.getValue());
        }

        return text.toString();
    }

    public int getCharacterCount() {
        return characters.size();
    }

    public TextCharacter getCharacter(int index) {
        return characters.get(index);
    }

    public void save(String filename) throws IOException {
        try (BufferedWriter writer = Files.newBufferedWriter(Path.of(filename))) {
            for (TextCharacter character : characters) {
                CharacterStyle style = character.getStyle();

                writer.write((int) character.getValue() + "\t"
                        + style.getFont() + "\t"
                        + style.getSize() + "\t"
                        + style.getColor());

                writer.newLine();
            }
        }
    }

    public void load(String filename) throws IOException {
        List<TextCharacter> loaded = new ArrayList<>();

        try (BufferedReader reader = Files.newBufferedReader(Path.of(filename))) {
            String line;

            while ((line = reader.readLine()) != null) {
                String[] parts = line.split("\t", -1);

                if (parts.length != 4) {
                    throw new IOException("Invalid document format");
                }

                try {
                    int codePoint = Integer.parseInt(parts[0]);

                    if (codePoint < Character.MIN_VALUE
                            || codePoint > Character.MAX_VALUE) {
                        throw new IOException("Invalid character value");
                    }

                    char value = (char) codePoint;
                    String font = parts[1];
                    int size = Integer.parseInt(parts[2]);
                    String color = parts[3];

                    CharacterStyle style = factory.getStyle(font, size, color);
                    loaded.add(new TextCharacter(value, style));

                } catch (NumberFormatException e) {
                    throw new IOException("Invalid document data", e);
                }
            }
        }

        characters.clear();
        characters.addAll(loaded);
    }
}