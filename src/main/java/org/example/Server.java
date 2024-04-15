package org.example;

import java.io.*;
import java.net.Socket;

public class Server implements Runnable {

    private static Socket clientDialog;
    private BufferedReader in; // поток чтения из сокета
    private BufferedWriter out; // поток записи в сокет

    public static Loger logs  = new Loger();

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

            logs.logs(nickName, nickName);

            out.write("Wellcome to chat, " + nickName + "\n");
            out.flush();
            // начинаем диалог с подключенным клиентом в цикле, пока сокет не закрыт
            while (!clientDialog.isClosed()) {

                String message = in.readLine();
                //отправим сообщение всем участникам чата
                for (Server vr : MultiThreadServer.serverList) {
                    vr.send(nickName, message);
                }
                //logi
                logs.logs(nickName, message);

                if (message.equalsIgnoreCase("exit")) {
                    break;
                }

            }
            // если условие выхода - верно выключаем соединения
            System.out.println("Client disconnected");
            System.out.println("Closing connections & channels.");

            in.close();
            out.close();

            clientDialog.close();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }
    private void send(String nick, String msg) {
        try {
            out.write("[" + nick + "]: " + msg + "\n");
            out.flush();
        } catch (IOException ignored) {}
    }

}
