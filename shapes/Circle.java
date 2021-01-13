package shapes;

import java.awt.*;

public class Circle extends Shape {
    private final int x;
    private final int y;
    private final int radius;

    public Circle(int x, int y, int radius) {
        this.x = x;
        this.y = y;
        this.radius = radius;
    }

    @Override
    public void draw(Graphics g) {
        g.drawOval(x - radius, y - radius, radius * 2, radius * 2);
    }
}
