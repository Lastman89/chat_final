package org.example;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.FileWriter;
import java.io.IOException;
import java.io.StringWriter;
import java.nio.file.Path;
import java.nio.file.Paths;

public class Configs {
    //запишем конфиги для подключения в файл
    public static int writecConfig(int port, String host) throws IOException {
        serverConfig config = new serverConfig();
        config.nameHost = host;
        config.port = port;

        //писать результат сериализации будем во Writer(StringWriter)
        StringWriter writer = new StringWriter();

        //это объект Jackson, который выполняет сериализацию
        ObjectMapper mapper = new ObjectMapper();

        // сама сериализация: 1-куда, 2-что
        mapper.writeValue(writer, config);

        //преобразовываем все записанное во StringWriter в строку
        String result = writer.toString();

        Path path = Paths.get("files/configs.json");
        //пишем в файл конфиги
        try (FileWriter file = new
                FileWriter(path.toString(), false)) {
            file.write(result);
            file.flush();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return port;
    }
    @JsonAutoDetect
    static class serverConfig {
        public String nameHost;
        public int port;

        serverConfig() {
        }
    }
}
