import java.awt.Color;

public enum Team {
    LEFT(Color.WHITE),
    RIGHT(Color.BLACK),
    NEUTRAL(Color.CYAN);

    private Color color;

    Team(Color c) {
        color = c;
    }
    public Color getColor() {
        return color;
    }
}
