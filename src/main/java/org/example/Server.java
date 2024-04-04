package org.example;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;


import static java.time.LocalTime.now;

public class Server implements Runnable {

    private static Socket clientDialog;
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток записи в сокет

    public Server(Socket client) throws IOException {
        Server.clientDialog = client;
        //отправить
        out = new BufferedWriter(new OutputStreamWriter(clientDialog.getOutputStream()));
        //принять
        in = new BufferedReader(new InputStreamReader(clientDialog.getInputStream()));
    }


    @Override
    public void run() {
        try {
            //отправить

            System.out.println("New connection accepted");


            out.write("Write your nick:" + "\n");
            out.flush(); // выталкиваем все из буфера
            //ждем сообщения от клиента
            String nickName = in.readLine();
            //logi
            logs(nickName, nickName);

            out.write("Wellcome to chat, " + nickName + "\n");
            out.flush(); // выталкиваем все из буфера
            // начинаем диалог с подключенным клиентом в цикле, пока сокет не закрыт
            while (!clientDialog.isClosed()) {

                String message = in.readLine();
                //отправим сообщение всем участникам чата
                for (Server vr : MultiThreadServer.serverList) {
                    vr.send(nickName, message);
                }
                //logi
                logs(nickName, message);

                if (message.equalsIgnoreCase("exit")) {
                    break;
                }

            }
            // если условие выхода - верно выключаем соединения
            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");

            // закрываем сначала каналы сокета !
            in.close();
            out.close();

            // потом закрываем сам сокет общения на стороне сервера!
            clientDialog.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void send(String nick, String msg) {
        try {
            out.write("[" + nick + "]: " + msg + "\n");
            out.flush(); // выталкиваем все из буфера
        } catch (IOException ignored) {}
    }


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

    @JsonAutoDetect
    static class serverConfig {
        public String nameHost;
        public int port;

        serverConfig() {
        }
    }
}
