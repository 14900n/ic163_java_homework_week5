package castle;

import java.util.HashMap;

public class Room {
    private final String description;
    private final HashMap<String, Room> exits = new HashMap<>();

    public Room(String description) {
        this.description = description;
    }

    public void setExit(String dir, Room room) {
        exits.put(dir, room);
    }

    @Override
    public String toString() {
        return description;
    }

    public String getExitDesc() {
        StringBuilder sb = new StringBuilder();
        for (String dir : exits.keySet()) {
            sb.append(dir);
            sb.append(' ');
        }
        return sb.toString();
    }

    public Room getExit(String direction) {
        return exits.get(direction);
    }
}
