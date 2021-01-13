package castle;


import castle.view.MapView;
import castle.view.PlayerStyle;
import castle.view.RoomStyle;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Scanner;

public class Game {
    private final MapView mapView;
    private final RoomField roomField;
    private final HashMap<String, Handler> handlers = new HashMap<>();

    public Game() {
        Room currentRoom = createRooms();
        roomField = new RoomField(currentRoom);

        RoomStyle roomStyle = new RoomStyle(64);
        PlayerStyle playerStyle = new PlayerStyle(8);
        mapView = new MapView(240, 200, roomStyle, playerStyle);
        mapView.draw(roomField);

        handlers.put("go", new Handler() {
            @Override
            public void doCmd(String word) {
                if (moveCurrentRoom(word)) {
                    showPrompt();
                } else {
                    System.out.println("那里没有门！");
                }
            }
        });
        handlers.put("bye", new Handler() {
            @Override
            public boolean isBye() {
                return true;
            }
        });
        handlers.put("help", new Handler() {
            @Override
            public void doCmd(String word) {
                System.out.println("迷路了吗？你可以做的命令有：" + handlers.keySet());
                System.out.println("如：\tgo east");
            }
        });
    }

    private Room createRooms() {
        Room outside, lobby, pub, study, bedroom, tower, basement, cellar;

        //	制造房间
        outside = new Room("城堡外");
        lobby = new Room("大堂");
        pub = new Room("小酒吧");
        study = new Room("书房");
        bedroom = new Room("卧室");
        tower = new Room("哨塔");
        basement = new Room("地下室");
        cellar = new Room("酒窖");


        //	初始化房间的出口
        outside.setExit("east", lobby);
        outside.setExit("south", study);
        outside.setExit("west", pub);
        lobby.setExit("west", outside);
        pub.setExit("east", outside);
        study.setExit("north", outside);
        study.setExit("east", bedroom);
        bedroom.setExit("west", study);
        lobby.setExit("up", tower);
        tower.setExit("down", lobby);
        study.setExit("down", basement);
        basement.setExit("up", study);
        pub.setExit("down", cellar);
        cellar.setExit("up", pub);

        return outside;  //	从城堡门外开始
    }

    private void printWelcome() {
        System.out.println();
        System.out.println("欢迎来到城堡！");
        System.out.println("这是一个超级无聊的游戏。");
        System.out.println("如果需要帮助，请输入 'help' 。");
        System.out.println();
        showPrompt();
    }

    // 以下为用户命令

    public void showPrompt() {
        System.out.println("你在" + getCurrentRoom());
        System.out.print("出口有: ");
        System.out.println(getCurrentRoom().getExitDesc());
    }

    public void play() {
        Scanner in = new Scanner(System.in, StandardCharsets.UTF_8);
        while (true) {
            String line = in.nextLine();
            String[] words = line.split(" ");
            Handler handler = handlers.get(words[0]);
            String value = "";
            if (words.length > 1) {
                value = words[1];
            }
            if (handler != null) {
                handler.doCmd(value);
                if (handler.isBye()) {
                    break;
                }
            }
        }
        in.close();
    }

    public void exit() {
        mapView.close();
        System.out.println("感谢您的光临。再见！");
    }

    private Room getCurrentRoom() {
        return roomField.getCurrentRoom();
    }

    private boolean moveCurrentRoom(String direction) {
        boolean success = roomField.moveCurrentRoom(direction);
        mapView.draw(roomField);
        return success;
    }

    public static void main(String[] args) {
        Game game = new Game();
        game.printWelcome();
        game.play();
        game.exit();
    }
}
