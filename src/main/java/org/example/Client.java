package org.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


public class Client {
    static ExecutorService executeIt = Executors.newFixedThreadPool(1);
    //Runtime.getRuntime().availableProcessors()
    private static BufferedReader in; // поток чтения из сокета

    static PrintWriter writer;
    static BufferedReader readerSecond;
    static BufferedReader reader;
    public static Loger Logs = new Loger();
    static String ans = null; // ждём пока клиент что-нибудь

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
        try (Socket clientSocket = new Socket(host, (int) port)

        ) {

            writer = new PrintWriter(clientSocket.getOutputStream(), true);
            readerSecond = new BufferedReader(new InputStreamReader(System.in));
            reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
            String serverAns = reader.readLine(); // ждём, что скажет сервер
            System.out.println(serverAns); // получив - выводим на экран
            String nick = readerSecond.readLine(); // ждём пока клиент что-нибудь
            // не напишет в консоль
            writer.write(nick + "\n"); // отправляем сообщение на сервер
            writer.flush();

            //Logs.clientLogs(nick, nick);


            // проверяем живой ли канал и работаем если живой
            while (!clientSocket.isOutputShutdown()) {
                executeIt.execute(new ClientThreads(clientSocket));

                try {
                    ans = readerSecond.readLine();
                    Logs.clientLogs(nick, ans);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

                // не напишет в консоль
                writer.write(ans + "\n"); // отправляем сообщение на сервер
                writer.flush();
                if (ans.equalsIgnoreCase("exit")) {
                    break;
                }

            }
            // закрытие пула нитей после завершения работы всех нитей
            executeIt.shutdown();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


}




