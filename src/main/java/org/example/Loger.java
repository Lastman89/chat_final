package org.example;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.time.LocalTime.now;

public class Loger {

    public static void logs(String nickName, String message) {
        Path path = Paths.get("files");

        File dir = new File(path + "/" + "serverLogs");
        // пробуем создать каталог
        if (dir.mkdir())
            System.out.println("Каталог создан");

        path = Paths.get("files/serverLogs/serverLogs.log");

        try (FileWriter file = new FileWriter(path.toString(), true)) {
            if (message.equalsIgnoreCase("exit")) {
                message = "User " + nickName + " left chat....";
                file.write("[" + now() + "] " + " " + message);
                file.write("\n"); //чтобы сохранять каждый раз с новой строки
                file.flush();
            } else {
                file.write("[" + now() + "] " + nickName + ": " + message);
                file.write("\n"); //чтобы сохранять каждый раз с новой строки
                file.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void clientLogs(String nick, String message) {


        Path path = Paths.get("files");

        File dir = new File(path + "/" + "clientLogs");
        // пробуем создать каталог
        if (dir.mkdir())
            System.out.println("Каталог создан");

        path = Paths.get("files/clientLogs/" + nick + ".log");
        try (FileWriter file = new
                FileWriter(path.toString(), true)) {
            file.write("[" + now() + "] " + message);
            file.write("\n"); //чтобы сохранять каждый раз с новой строки
            file.flush();

            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
