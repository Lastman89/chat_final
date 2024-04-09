import org.example.Loger;
import org.junit.jupiter.api.Assertions;

import java.io.*;
import java.nio.file.Path;
import java.nio.file.Paths;


public class MainTest {

    @org.junit.jupiter.api.Test
    public void test_client_logs() {
        Loger testClientlog = new Loger();
        String nick = "Test";
        String msgTest = "Hello world";
        testClientlog.clientLogs(nick, msgTest);

        Path path = Paths.get("files");
        String checkFile = null;
        File dirTest = new File(path + "/" + "clientLogs");

        for (File items : dirTest.listFiles()) {
            if (items.getName().equals(nick + ".log")) {
                checkFile = items.getName();
            }
        }
        Assertions.assertEquals(checkFile, nick + ".log");
    }

    @org.junit.jupiter.api.Test
    public void test_server_logs() {
        Loger testServerLog = new Loger();
        String nick = "Test";
        String msgTest = "Hello world";
        testServerLog.logs(nick, msgTest);

        Path path = Paths.get("files");
        String checkFile = null;
        File dirTest = new File(path + "/" + "serverLogs");

        for (File items : dirTest.listFiles()) {
            if (items.getName().equals(nick + ".log")) {
                checkFile = items.getName();
            }
        }
        Assertions.assertEquals(checkFile, null);
    }

}
