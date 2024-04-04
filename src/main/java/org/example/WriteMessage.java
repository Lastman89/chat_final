package org.example;

import java.io.*;
import java.net.Socket;

public class WriteMessage implements Runnable {
    PrintWriter writer;
    BufferedReader readerSecond;
    BufferedReader reader;


    String nick;

    private static Socket clientSocket;

    public WriteMessage(Socket clientSocket, String nick) throws IOException {
        WriteMessage.clientSocket = clientSocket;
        this.nick = nick;
        //отправить
        writer = new PrintWriter(clientSocket.getOutputStream(), true);
        readerSecond = new BufferedReader(new InputStreamReader(System.in));
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
    @Override
    public void run() {
        String userWord;
        while (!clientSocket.isOutputShutdown()) {
            try {
                userWord = readerSecond.readLine();  // сообщения с консоли
                writer.write(userWord + "\n"); // отправляем на сервер
                Client.clientLogs(nick, userWord);
                if (userWord.equals("exit")) {
                    break; // выходим из цикла если пришло "exit"
                } else {
                    writer.write(userWord + "\n"); // отправляем на сервер


                }
                writer.flush(); // чистим
            } catch (IOException e) {

            }


        }
    }
}
