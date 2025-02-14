package com.yandex.taskmanager.handler;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanager.TaskManager;

public class PrioritizedHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public PrioritizedHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        try {
            if (method.equals("GET")) {
                handleGetPrioritized(exchange);
            } else {
                sendText(exchange, "Метод " + method + " некорректный, необходимо передать GET запрос",
                        404);
            }
        } catch (Exception e) {
            sendServerError(exchange, e.getMessage());
        }
    }

    private void handleGetPrioritized(HttpExchange exchange) {
        try {
            String response = gson.toJson(taskManager.getPrioritizedTasks());
            sendText(exchange, response, 200);
        } catch (Exception e) {
            e.printStackTrace();
            sendNotFound(exchange);
        }
    }
}