package castle.view;

import shapes.Circle;
import shapes.Picture;

public class PlayerStyle {
    private final int radius;

    public PlayerStyle(int radius) {
        this.radius = radius;
    }

    public void render(Picture picture, int midX, int midY) {
        picture.add(new Circle(midX, midY, radius));
    }
}
