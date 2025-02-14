package com.yandex.taskmanager.handler;

import com.sun.net.httpserver.Headers;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;

public class HelloHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        // получаем путь, на который пришел запрос
        String path = httpExchange.getRequestURI().getPath();
        // разбиваем путь на компоненты и берём последний
        String name = path.split("/")[2];
        // выводим полученное имя в консоль и в качестве ответа
        System.out.println("Имя: " + name);
        httpExchange.sendResponseHeaders(200, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            String response = "Привет, " + name + "!";
            os.write(response.getBytes());
        }
    }
}
