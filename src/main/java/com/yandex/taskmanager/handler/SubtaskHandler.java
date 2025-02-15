package com.yandex.taskmanager.handler;

import com.google.gson.Gson;
import com.google.gson.internal.bind.util.ISO8601Utils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanager.TaskManager;
import com.yandex.taskmanager.model.Subtask;

import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler implements HttpHandler {

    private final TaskManager taskManager;
    private final Gson gson;

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange exchange) {
        String method = exchange.getRequestMethod();
        String path = exchange.getRequestURI().getPath();
        String[] pathParts = path.split("/");
        try {
            switch (method) {
                case "GET" -> {
                    if (pathParts.length == 3) {
                        handleGetSubtaskById(exchange, pathParts[2]);
                    } else if (pathParts.length == 2) {
                        handleGetAllSubtasks(exchange);
                    } else {
                        sendNotFound(exchange);
                    }
                }
                case "POST" -> handlePostSubtask(exchange);
                case "DELETE" -> {
                    if (pathParts.length == 3) {
                        handleDeleteSubtaskById(exchange, pathParts[2]);
                    } else {
                        sendNotFound(exchange);
                    }
                }
                default -> sendText(exchange, "Метод не поддерживается", 400);
            }
        } catch (Exception e) {
            sendServerError(exchange, e.getMessage());
        }
    }

    private void handleGetAllSubtasks(HttpExchange exchange) {
        try {
            String response = gson.toJson(taskManager.getAllSubTasks());
            sendText(exchange, response, 200);
        } catch (Exception e) {
            e.printStackTrace();
            sendNotFound(exchange);
        }
    }

    private void handleGetSubtaskById(HttpExchange exchange, String taskIdStr) {
        try {
            int subTaskId = Integer.parseInt(taskIdStr);
            Subtask subTask = taskManager.getSubTaskById(subTaskId);
            if (subTask == null) {
                sendNotFound(exchange);
            } else {
                String response = gson.toJson(subTask);
                sendText(exchange, response, 200);
            }
        } catch (Exception e) {
            e.printStackTrace();
            sendNotFound(exchange);
        }
    }

    private void handlePostSubtask(HttpExchange exchange) {
        try {
            InputStreamReader inputReader = new InputStreamReader(exchange.getRequestBody(), StandardCharsets.UTF_8);
            Subtask task = gson.fromJson(inputReader, Subtask.class);
            taskManager.addSubTask(task);
            sendText(exchange, "Задача добавлена", 201);
        } catch (Exception e) {
            e.printStackTrace();
            sendHasInteractions(exchange);
        }
    }

    private void handleDeleteSubtaskById(HttpExchange exchange, String taskIdStr) {
        try {
            int subTaskId = Integer.parseInt(taskIdStr);
            taskManager.deleteSubTask(subTaskId);
            sendText(exchange, "Задача удалена", 204);
        } catch (Exception e) {
            e.printStackTrace();
            sendNotFound(exchange);
        }
    }
}