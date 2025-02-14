package com.yandex.taskmanager.handler;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.yandex.taskmanager.TaskManager;
import com.yandex.taskmanager.model.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;

public class TaskHandler implements HttpHandler {

    private TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {

        String path = httpExchange.getRequestURI().getPath();
        System.out.println(path);
        // а из path — профессию и имя
        String[] pathParams = path.split("/");
        int nestingLevel = pathParams.length;

        String method = httpExchange.getRequestMethod();
        System.out.println("Началась обработка " + method + " /tasks запроса от клиента.");

        String response;

        int statusCode;

        switch (method) {
            case "POST":
                response = handlePostTaskRequest(taskManager, httpExchange);
                statusCode = 201;
                break;
            case "GET":
                if (nestingLevel == 3) {
                    response = handleGetTaskRequest(taskManager, pathParams[2]);
                    statusCode = 200;
                } else if (nestingLevel == 2) {
                    response = handleGetAllTasksRequest(taskManager);
                    statusCode = 200;
                } else {
                    response = null;
                    statusCode = 404;
                }
                break;
            case "DELETE":
                if (nestingLevel == 3) {
                    response = handleDeleteTaskRequest(taskManager, pathParams[2]);
                    statusCode = 200;
                } else if (nestingLevel == 2) {
                    response = handleDeleteAllTasksRequest(taskManager);
                    statusCode = 200;
                } else {
                    response = null;
                    statusCode = 404;
                }
                break;
            default:
                response = "Вы использовали какой-то другой метод!";
                statusCode = 404;
        }

        httpExchange.sendResponseHeaders(statusCode, 0);

        try (OutputStream os = httpExchange.getResponseBody()) {
            os.write(response.getBytes());
        }
    }

    private static String handleGetTaskRequest(TaskManager taskManager, String pathParam) {
        int taskId = Integer.parseInt(pathParam);
        return String.valueOf(taskManager.getTaskById(taskId));
    }

    private static String handleGetAllTasksRequest(TaskManager taskManager) {
        return String.valueOf(taskManager.getAllTasks());
    }

    private static String handleDeleteTaskRequest(TaskManager taskManager, String pathParam) {
        int taskId = Integer.parseInt(pathParam);
        taskManager.deleteTask(taskId);
        return "Задача id:" + taskId + " удалена";
    }

    private static String handleDeleteAllTasksRequest(TaskManager taskManager) {
        taskManager.deleteAllTasks();
        return "Все задачи удалены";
    }

    private static String handlePostTaskRequest(TaskManager taskManager, HttpExchange httpExchange) throws IOException {
        InputStream inputStream = httpExchange.getRequestBody();
        String name = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);
        Task someTask = new Task();
        someTask.setTitle(name);
        someTask.setDescription("Таска создалась методом POST");
        someTask.setStartTime(LocalDateTime.now());
        someTask.setDuration(Duration.ofMinutes(30));
        taskManager.addTask(someTask);
        return "Задача создана успешно";
    }
}
