package castle.view;

import castle.Room;
import castle.RoomField;
import shapes.Picture;

import java.util.ArrayList;

public class MapView {
    private final Picture picture;
    private final RoomStyle roomStyle;
    private final PlayerStyle playerStyle;

    public MapView(int width, int height, RoomStyle roomStyle, PlayerStyle playerStyle) {
        picture = new Picture(width, height);
        picture.setResizable(false);
        picture.draw();

        this.roomStyle = roomStyle;
        this.playerStyle = playerStyle;
    }

    public void draw(RoomField roomField) {
        int width = picture.getContentPane().getWidth();
        int height = picture.getContentPane().getHeight();

        int[] currentRoomLocation = roomField.getCurrentRoomLocation();
        int currentLayer = currentRoomLocation[0];
        ArrayList<int[]> currentLayerLocations = roomField.listValidLocation(currentLayer);
        int[] min = RoomField.minOf(currentLayerLocations);
        int[] max = RoomField.maxOf(currentLayerLocations);
        int minRow = min[1], maxRow = max[1];
        int minCol = min[2], maxCol = max[2];

        int gridSize = roomStyle.getSize();
        int mapWidth = (maxCol - minCol + 1) * gridSize;
        int mapHeight = (maxRow - minRow + 1) * gridSize;
        int currentRoomCenterX = (currentRoomLocation[2] - minCol) * gridSize + gridSize / 2;
        int currentRoomCenterY = (currentRoomLocation[1] - minRow) * gridSize + gridSize / 2;
        int margin = Math.min(width, height) / 8;

        int left = follow(width, margin, mapWidth, currentRoomCenterX);
        int top = follow(height, margin, mapHeight, currentRoomCenterY);
        int right = left + width;
        int bottom = top + height;
        int row0 = minRow + Math.max(0, top / gridSize);
        int row1 = minRow + Math.min(maxRow - minRow, bottom / gridSize);
        int col0 = minCol + Math.max(0, left / gridSize);
        int col1 = minCol + Math.min(maxCol - minCol, right / gridSize);

        picture.clear();
        for (int row = row0; row <= row1; row++) {
            for (int col = col0; col <= col1; col++) {
                Room room = roomField.getRoom(new int[]{currentLayer, row, col});
                if (room != null) {
                    roomStyle.render(picture, room, (col - minCol) * gridSize - left, (row - minRow) * gridSize - top);
                }
            }
        }
        playerStyle.render(picture, currentRoomCenterX - left, currentRoomCenterY - top);
        picture.repaint();
    }

    public void close() {
        picture.dispose();
    }

    private static int follow(int cameraSize, int margin, int mapSize, int mapFocus) {
        int offset;
        if (mapSize < cameraSize) {
            offset = (mapSize - cameraSize) / 2;
        } else if (mapFocus < margin || mapFocus + margin < cameraSize) {
            offset = 0;
        } else if (mapSize - mapFocus < margin || mapSize - mapFocus + margin < cameraSize) {
            offset = mapSize - cameraSize;
        } else {
            offset = mapFocus - cameraSize / 2;
        }
        return offset;
    }
}
