package shapes;

import java.awt.*;

public class Label extends Shape {
    private final String text;
    private final int midX;
    private final int midY;

    public Label(String text, int midX, int midY) {
        this.text = text;
        this.midX = midX;
        this.midY = midY;
    }

    public void draw(Graphics g) {
        FontMetrics metrics = g.getFontMetrics();
        int x = midX - metrics.stringWidth(text) / 2;
        int y = midY - metrics.getHeight() / 2 + metrics.getAscent();
        g.drawString(text, x, y);
    }
}
