import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.io.IOException;
import java.nio.file.Path;

import static org.junit.jupiter.api.Assertions.*;

public class FlyweightTest {

    @TempDir
    Path tempDir;

    @Test
    public void testCharacterStyleProperties() {
        CharacterStyle style = new CharacterStyle("Arial", 12, "Red");

        assertEquals("Arial", style.getFont());
        assertEquals(12, style.getSize());
        assertEquals("Red", style.getColor());
    }

    @Test
    public void testStyleFactorySharesObjects() {
        CharacterStyleFactory factory = new CharacterStyleFactory();

        CharacterStyle first = factory.getStyle("Arial", 12, "Red");
        CharacterStyle second = factory.getStyle("Arial", 12, "Red");

        assertSame(first, second);
        assertEquals(1, factory.getStyleCount());
    }

    @Test
    public void testTextCharacter() {
        CharacterStyle first = new CharacterStyle("Arial", 12, "Red");
        CharacterStyle second = new CharacterStyle("Calibri", 14, "Blue");

        TextCharacter character = new TextCharacter('A', first);

        assertEquals('A', character.getValue());
        assertSame(first, character.getStyle());

        character.setValue('B');
        character.setStyle(second);

        assertEquals('B', character.getValue());
        assertSame(second, character.getStyle());
    }

    @Test
    public void testDocumentAddAndGet() {
        TextDocument document = new TextDocument(new CharacterStyleFactory());

        document.addCharacter('H', "Arial", 12, "Red");
        document.addCharacter('i', "Arial", 12, "Red");

        assertEquals("Hi", document.getText());
        assertEquals(2, document.getCharacterCount());
        assertEquals('H', document.getCharacter(0).getValue());
    }

    @Test
    public void testDocumentEditing() {
        TextDocument document = new TextDocument(new CharacterStyleFactory());

        document.addCharacter('A', "Arial", 12, "Red");
        document.editCharacter(0, 'B');
        document.changeStyle(0, "Verdana", 16, "Black");

        assertEquals("B", document.getText());
        assertEquals("Verdana", document.getCharacter(0).getStyle().getFont());
    }

    @Test
    public void testSaveAndLoad() throws IOException {
        CharacterStyleFactory factory = new CharacterStyleFactory();
        TextDocument document = new TextDocument(factory);

        document.addCharacter('H', "Arial", 12, "Red");
        document.addCharacter('i', "Calibri", 14, "Blue");

        String filename = tempDir.resolve("test-document.txt").toString();

        document.save(filename);

        TextDocument loaded = new TextDocument(factory);
        loaded.load(filename);

        assertEquals("Hi", loaded.getText());
        assertEquals(2, loaded.getCharacterCount());
        assertEquals("Blue", loaded.getCharacter(1).getStyle().getColor());
        assertSame(document.getCharacter(0).getStyle(),
                loaded.getCharacter(0).getStyle());
    }

    @Test
    public void testInvalidDocumentFormat() throws IOException {
        Path file = tempDir.resolve("invalid.txt");
        java.nio.file.Files.writeString(file, "invalid data");

        TextDocument document = new TextDocument(new CharacterStyleFactory());

        assertThrows(IOException.class,
                () -> document.load(file.toString()));
    }

    @Test
    public void testFlyweightDemo() {
        assertDoesNotThrow(() -> FlyweightDemo.main(new String[0]));
    }
}