package castle.view;

import castle.Room;
import shapes.Label;
import shapes.Line;
import shapes.Picture;
import shapes.Triangle;

public class RoomStyle {
    private final int size;
    private final int margin;
    private final int doorSize;
    private final int stairSize;
    private final int upStairX;
    private final int upStairY;
    private final int downStairX;
    private final int downStairY;

    public RoomStyle(int size) {
        this.size = size;
        margin = size / 16;
        doorSize = size / 3;
        stairSize = size / 8;
        upStairX = (int) (0.7 * size);
        upStairY = (int) (0.4 * size) - stairSize;
        downStairX = upStairX;
        downStairY = (int) (0.5 * size);
    }

    public int getSize() {
        return size;
    }

    public void render(Picture picture, Room room, int x, int y) {
        int x0 = x + margin;
        int y0 = y + margin;
        int x1 = x + size - margin;
        int y1 = y + size - margin;
        int midX = (x0 + x1) / 2;
        int midY = (y0 + y1) / 2;
        int doorX0 = midX - doorSize / 2;
        int doorX1 = midX + doorSize / 2;
        int doorY0 = midY - doorSize / 2;
        int doorY1 = midY + doorSize / 2;

        if (room.getExit("north") == null) {
            picture.add(new Line(x0, y0, x1, y0));
        } else {
            picture.add(new Line(x0, y0, doorX0, y0));
            picture.add(new Line(doorX1, y0, x1, y0));
            picture.add(new Line(doorX0, y0, doorX0, y0 - margin));
            picture.add(new Line(doorX1, y0, doorX1, y0 - margin));
        }

        if (room.getExit("south") == null) {
            picture.add(new Line(x0, y1, x1, y1));
        } else {
            picture.add(new Line(x0, y1, doorX0, y1));
            picture.add(new Line(doorX1, y1, x1, y1));
            picture.add(new Line(doorX0, y1, doorX0, y1 + margin));
            picture.add(new Line(doorX1, y1, doorX1, y1 + margin));
        }

        if (room.getExit("west") == null) {
            picture.add(new Line(x0, y0, x0, y1));
        } else {
            picture.add(new Line(x0, y0, x0, doorY0));
            picture.add(new Line(x0, doorY1, x0, y1));
            picture.add(new Line(x0, doorY0, x0 - margin, doorY0));
            picture.add(new Line(x0, doorY1, x0 - margin, doorY1));
        }

        if (room.getExit("east") == null) {
            picture.add(new Line(x1, y0, x1, y1));
        } else {
            picture.add(new Line(x1, y0, x1, doorY0));
            picture.add(new Line(x1, doorY1, x1, y1));
            picture.add(new Line(x1, doorY0, x1 + margin, doorY0));
            picture.add(new Line(x1, doorY1, x1 + margin, doorY1));
        }

        if (room.getExit("up") != null) {
            picture.add(makeRegularTriangle(x0 + upStairX, y0 + upStairY, stairSize, false));
        }

        if (room.getExit("down") != null) {
            picture.add(makeRegularTriangle(x0 + downStairX, y0 + downStairY, stairSize, true));
        }

        picture.add(new Label(room.toString(), midX, midY - 20));
    }

    private static Triangle makeRegularTriangle(int x, int y, int side, boolean flipped) {
        int h = (int) (0.866 * side);
        if (flipped) {
            return new Triangle(x, y, x + side, y, x + side / 2, y + h);
        } else {
            return new Triangle(x, y + h, x + side, y + h, x + side / 2, y);
        }
    }
}
