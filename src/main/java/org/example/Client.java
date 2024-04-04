package org.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

import static java.time.LocalTime.now;

public class Client {
    public static void main(String[] args) {


        //Считываем конфиги сервера
        JSONParser parser = new JSONParser();
        String host = null;
        long port = 0;
        try {
            Path path = Paths.get("files/configs.json");
            Object obj = parser.parse(new FileReader(path.toString()));
            JSONObject jsonObject = (JSONObject) obj;
            host = (String) jsonObject.get("nameHost");
            port = (long) jsonObject.get("port");

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }


        //подключаемся к серверу
        try (Socket clientSocket = new Socket(host, (int) port);
             PrintWriter writer = new PrintWriter(clientSocket.getOutputStream(), true);
             BufferedReader readerSecond = new BufferedReader(new InputStreamReader(System.in));
             BufferedReader reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()))
        ) {
            String serverAns = reader.readLine(); // ждём, что скажет сервер
            System.out.println(serverAns); // получив - выводим на экран
            String nick = readerSecond.readLine(); // ждём пока клиент что-нибудь
            // не напишет в консоль
            writer.write(nick + "\n"); // отправляем сообщение на сервер
            writer.flush();

            clientLogs(nick, nick);
            ReadMessage msg = new ReadMessage(clientSocket);

            // проверяем живой ли канал и работаем если живой
            while (!clientSocket.isOutputShutdown()) {
                /*serverAns = reader.readLine(); // ждём, что скажет сервер
                System.out.println(serverAns); // получив - выводим на экран*/
                msg.run();

                String Ans = readerSecond.readLine(); // ждём пока клиент что-нибудь
                // не напишет в консоль
                writer.write(Ans + "\n"); // отправляем сообщение на сервер
                writer.flush();

                clientLogs(nick, Ans);

                if (Ans.equalsIgnoreCase("exit")) {
                    break;
                }
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    private static void clientLogs(String nick, String message) {


        Path path = Paths.get("files");

        File dir = new File(path + "/" + "clientLogs");
        // пробуем создать каталог
        if (dir.mkdir())
            System.out.println("Каталог создан");

        path = Paths.get("files/clientLogs/" + nick + ".log");
        try (FileWriter file = new
                FileWriter(path.toString(), true)) {
            for (File items : dir.listFiles()) {
                if (items.equals(nick)) {
                    file.write(nick);
                    file.write("\n");
                } else {
                    file.write("[" + now() + "] " + message);
                    file.write("\n"); //чтобы сохранять каждый раз с новой строки
                    file.flush();
                }

            }
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}




