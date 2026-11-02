public class TextCharacter {

    private char value;
    private CharacterStyle style;

    public TextCharacter(char value, CharacterStyle style) {
        this.value = value;
        this.style = style;
    }

    public char getValue() {
        return value;
    }

    public CharacterStyle getStyle() {
        return style;
    }

    public void setValue(char value) {
        this.value = value;
    }

    public void setStyle(CharacterStyle style) {
        this.style = style;
    }
}