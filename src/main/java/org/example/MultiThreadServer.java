package org.example;


import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.LinkedList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class MultiThreadServer {
    static ExecutorService executeIt = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    public static final Integer LOCALHOST_PORT = 8082;
    public static final String LOCALHOST_NAME = "localhost";
    public static LinkedList<Server> serverList = new LinkedList<>(); // список всех нитей

    private static Configs configs = new Configs();

    public static void main(String[] args) throws IOException {


        //Пуск - Выполнить - telnet - ОК - ввести o localhost port
        try {
            ServerSocket socket = new ServerSocket(configs.writecConfig(LOCALHOST_PORT, LOCALHOST_NAME));

            //отправить
            //принять
            BufferedReader in = new BufferedReader(new InputStreamReader(System.in));
            // стартуем цикл при условии что серверный сокет не закрыт
            while (!socket.isClosed()) {

                // проверяем поступившие комманды из консоли сервера если такие
                // были
                if (in.ready()) {
                    System.out.println("Main Server found any messages in channel, let's look at them.");

                    // если команда - exit то инициализируем закрытие сервера и
                    // выход из цикла раздачии нитей монопоточных серверов
                    String serverCommand = in.readLine();
                    if (serverCommand.equalsIgnoreCase("exit")) {
                        System.out.println("Main Server initiate exiting...");
                        socket.close();
                        break;
                    }
                }

                // если комманд от сервера нет то становимся в ожидание
                // подключения к сокету общения под именем - "clientSocket" на
                // серверной стороне
                Socket clientSocket = socket.accept();
                serverList.add(new Server(clientSocket)); // добавить новое соединенние в список

                // после получения запроса на подключение сервер создаёт сокет
                // для общения с клиентом и отправляет его в отдельную нить
                // в Runnable
                // монопоточную нить = сервер - Server и тот
                // продолжает общение от лица сервера
                executeIt.execute(new Server(clientSocket));
                System.out.print("Connection accepted.");
            }

            // закрытие пула нитей после завершения работы всех нитей
            executeIt.shutdown();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
