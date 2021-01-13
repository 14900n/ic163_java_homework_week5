package shapes;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;

public class Picture extends JFrame {
    private static final long serialVersionUID = 1L;
    private final int width;
    private final int height;

    private final ArrayList<Shape> listShape = new ArrayList<>();

    private class ShapesPanel extends JPanel {
        private static final long serialVersionUID = 1L;

        @Override
        protected void paintComponent(Graphics g) {
            super.paintComponent(g);
            for (Shape s : listShape) {
                if (s != null) {
                    s.draw(g);
                }
            }
        }

    }

    public void add(Shape s) {
        listShape.add(s);
    }

    public Picture(int width, int height) {
        add(new ShapesPanel());
        this.setDefaultCloseOperation(EXIT_ON_CLOSE);
        this.width = width;
        this.height = height;
    }

    public void draw() {
        setLocationRelativeTo(null);
        setSize(width, height);
        setVisible(true);
    }

    public void clear() {
        listShape.clear();
    }
}
