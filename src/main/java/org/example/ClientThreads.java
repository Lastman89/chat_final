package org.example;

import java.io.*;
import java.net.Socket;


public class ClientThreads implements Runnable {

    static Socket clientSocket;
    PrintWriter writer;
    BufferedReader readerSecond;
    BufferedReader reader;

    public ClientThreads(Socket clientSocket) throws IOException {
        ClientThreads.clientSocket = clientSocket;
        //ClientThreads.nick = nick;
        //отправить
        writer = new PrintWriter(clientSocket.getOutputStream(), true);
        readerSecond = new BufferedReader(new InputStreamReader(System.in));
        reader = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }


    @Override
    public void run() {


        while (!clientSocket.isOutputShutdown()) {

            String str;
            if (clientSocket.isClosed()) {
                break;
            }

            try {
                str = reader.readLine(); // ждем сообщения с сервер

                System.out.println(str);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


        }

    }
}
