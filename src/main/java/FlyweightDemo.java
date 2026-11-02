import java.io.IOException;

public class FlyweightDemo {

    public static void main(String[] args) throws IOException {

        CharacterStyleFactory factory = new CharacterStyleFactory();
        TextDocument document = new TextDocument(factory);

        String text = "HelloWorldCS3560";

        for (int i = 0; i < text.length(); i++) {
            if (i < 5) {
                document.addCharacter(text.charAt(i), "Arial", 12, "Red");
            } else if (i < 10) {
                document.addCharacter(text.charAt(i), "Calibri", 14, "Blue");
            } else if (i < 12) {
                document.addCharacter(text.charAt(i), "Verdana", 16, "Black");
            } else {
                document.addCharacter(text.charAt(i), "Arial", 16, "Blue");
            }
        }

        System.out.println("Original: " + document.getText());
        System.out.println("Characters: " + document.getCharacterCount());
        System.out.println("Shared styles: " + factory.getStyleCount());

        document.editCharacter(0, 'h');
        System.out.println("Edited: " + document.getText());

        document.editCharacter(0, 'H');
        document.changeStyle(0, "Calibri", 14, "Blue");

        String filename = "Homework9_document.txt";
        document.save(filename);

        System.out.println("Saved to: " + filename);

        TextDocument loaded = new TextDocument(factory);
        loaded.load(filename);

        System.out.println("Loaded: " + loaded.getText());
        System.out.println("Loaded characters: " + loaded.getCharacterCount());
        System.out.println("Shared styles after loading: " + factory.getStyleCount());
    }
}