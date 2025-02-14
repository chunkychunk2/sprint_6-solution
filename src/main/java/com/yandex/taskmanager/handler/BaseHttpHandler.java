package com.yandex.taskmanager.handler;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

public abstract class BaseHttpHandler {

    protected void sendText(HttpExchange exchange, String text, int statusCode) {
        try (exchange) {
            try {
                if (exchange.getRequestMethod().equals("DELETE")) {
                    exchange.sendResponseHeaders(statusCode, -1);
                } else {
                    exchange.sendResponseHeaders(statusCode, 0);
                    byte[] response = text.getBytes(StandardCharsets.UTF_8);
                    exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                    exchange.getResponseBody().write(response);
                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    protected void sendNotFound(HttpExchange exchange) {
        sendText(exchange, "Не найдено", 404);
    }

    protected void sendHasInteractions(HttpExchange exchange) {
        sendText(exchange, "Нельяза добавить или обновить", 406);
    }

    protected void sendServerError(HttpExchange exchange, String errorMessage) {
        sendText(exchange, errorMessage, 500);
    }
}