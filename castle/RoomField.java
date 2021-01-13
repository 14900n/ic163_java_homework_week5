package castle;

import java.util.*;

public class RoomField {
    private static final int DIMENSION = 3;
    private static final HashMap<String, int[]> directions = new HashMap<>(); // 键：方向，值：位移

    private final int[] shape = new int[DIMENSION];
    private final Room[] rooms;

    private int[] currentRoomLocation;

    static {
        directions.put("up", new int[]{1, 0, 0});
        directions.put("down", new int[]{-1, 0, 0});
        directions.put("south", new int[]{0, 1, 0});
        directions.put("north", new int[]{0, -1, 0});
        directions.put("east", new int[]{0, 0, 1});
        directions.put("west", new int[]{0, 0, -1});
    }

    public RoomField(Room currentRoom) {
        HashMap<Room, int[]> locations = new HashMap<>();
        locations.put(currentRoom, new int[DIMENSION]);
        Queue<Room> queue = new LinkedList<>();
        queue.add(currentRoom);

        // 遍历所有联通的房间，计算出它们相对当前房间的位置
        while (!queue.isEmpty()) {
            Room room0 = queue.remove();
            int[] location0 = locations.get(room0);

            for (Map.Entry<String, int[]> entry : directions.entrySet()) {
                Room room1 = room0.getExit(entry.getKey());
                if (room1 != null && !locations.containsKey(room1)) {
                    int[] location1 = sumOf(location0, entry.getValue());
                    locations.put(room1, location1);
                    queue.add(room1);
                }
            }
        }

        // 计算相对位置在各维度的最值
        int[] rangeMin = minOf(locations.values());
        int[] rangeMax = maxOf(locations.values());

        // flat()和fold()要用到shape，因此shape要先初始化
        for (int i = 0; i < DIMENSION; i++) {
            shape[i] = rangeMax[i] - rangeMin[i] + 1;
        }

        // 计算当前房间的绝对位置
        currentRoomLocation = minusOf(rangeMin);

        // 创建并填充房间数组
        rooms = new Room[prodOf(shape, 0)];
        for (Map.Entry<Room, int[]> entry : locations.entrySet()) {
            int[] location = sumOf(currentRoomLocation, entry.getValue()); // 绝对位置
            rooms[flat(location)] = entry.getKey();
        }
    }

    public Room getRoom(int[] location) {
        Room room = null;
        if (isLegal(location)) {
            room = rooms[flat(location)];
        }
        return room;
    }

    public boolean moveCurrentRoom(String direction) {
        boolean success = false;
        Room currentRoom = getCurrentRoom();
        if (currentRoom.getExit(direction) != null) {
            int[] diff = directions.get(direction);
            if (diff != null) {
                int[] location = sumOf(currentRoomLocation, diff);
                Room room = getRoom(location);
                if (room != null) {
                    currentRoomLocation = location;
                    success = true;
                }
            }
        }
        return success;
    }

    public Room getCurrentRoom() {
        return rooms[flat(currentRoomLocation)];
    }

    public int[] getCurrentRoomLocation() {
        return currentRoomLocation.clone();
    }

    // 获取“子空间”上所有房间的位置
    public ArrayList<int[]> listValidLocation(int... axes) {
        ArrayList<int[]> list = new ArrayList<>();
        int begin = flat(Arrays.copyOf(axes, DIMENSION));
        int end = begin + prodOf(shape, axes.length);
        for (int i = begin; i < end; i++) {
            if (rooms[i] != null) {
                list.add(fold(i));
            }
        }
        return list;
    }

    public static int[] minOf(Collection<int[]> locations) {
        int[] min = null;
        for (int[] location : locations) {
            if (min == null) {
                min = location.clone();
            } else {
                for (int i = 0; i < DIMENSION; i++) {
                    min[i] = Math.min(min[i], location[i]);
                }
            }
        }
        return min;
    }

    public static int[] maxOf(Collection<int[]> locations) {
        int[] max = null;
        for (int[] location : locations) {
            if (max == null) {
                max = location.clone();
            } else {
                for (int i = 0; i < DIMENSION; i++) {
                    max[i] = Math.max(max[i], location[i]);
                }
            }
        }
        return max;
    }

    private boolean isLegal(int[] location) {
        for (int i = 0; i < location.length; i++) {
            if (location[i] < 0 || location[i] >= shape[i]) {
                return false;
            }
        }
        return true;
    }

    // 位置 -> 一维数组下标
    private int flat(int[] location) {
        int index = location[0];
        for (int i = 1; i < DIMENSION; i++) {
            index *= shape[i];
            index += location[i];
        }
        return index;
    }

    // 一维数组下标 -> 位置
    private int[] fold(int index) {
        int[] location = new int[DIMENSION];
        for (int i = DIMENSION - 1; i >= 0; i--) {
            location[i] = index % shape[i];
            index /= shape[i];
        }
        return location;
    }

    private static int[] sumOf(int[] location1, int[] location2) {
        int[] sum = new int[DIMENSION];
        for (int i = 0; i < DIMENSION; i++) {
            sum[i] = location1[i] + location2[i];
        }
        return sum;
    }

    private static int[] minusOf(int[] location) {
        int[] minus = new int[DIMENSION];
        for (int i = 0; i < DIMENSION; i++) {
            minus[i] = -location[i];
        }
        return minus;
    }

    private static int prodOf(int[] array, int index0) {
        int prod = 1;
        for (int i = index0; i < array.length; i++) {
            prod *= array[i];
        }
        return prod;
    }
}
