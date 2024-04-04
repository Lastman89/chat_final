package org.example;

import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.*;
import java.net.Socket;
import java.nio.file.Path;
import java.nio.file.Paths;

public class ReadMessage implements Runnable {

    private static BufferedReader in; // поток чтения из сокета


    static Socket clientSocket;

    public ReadMessage(Socket clientSocket) throws IOException {
        ReadMessage.clientSocket = clientSocket;
        //принять
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
    }
        @Override
        public void run() {

            String str;
            try {
                while (!clientSocket.isOutputShutdown()) {
                    str = in.readLine(); // ждем сообщения с сервера
                    System.out.println(str);
                    Thread.sleep(200);
                    if (str.equals("exit")) {
                        break; // выходим из цикла если пришло "stop"
                    }
                }
            } catch (IOException e) {

            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }

        }



}
