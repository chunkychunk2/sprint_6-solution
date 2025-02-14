package com.yandex.taskmanager.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpServer;
import com.yandex.taskmanager.Status;
import com.yandex.taskmanager.TaskManager;
import com.yandex.taskmanager.adapter.DurationAdapter;
import com.yandex.taskmanager.adapter.LocalDateTimeAdapter;
import com.yandex.taskmanager.handler.EpicHandler;
import com.yandex.taskmanager.handler.HelloHandler;
import com.yandex.taskmanager.handler.SubtaskHandler;
import com.yandex.taskmanager.handler.TaskHandler;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Subtask;
import com.yandex.taskmanager.model.Task;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.time.Duration;
import java.time.LocalDateTime;

public class HttpTaskServer {

    private static final int PORT = 8080;

    public static void main(String[] args) throws IOException {
        HttpServer httpServer = HttpServer.create(new InetSocketAddress(8080), 0);

        TaskManager manager = Managers.getDefault("src/main/resources/tasks_info.csv");
        Task someTask = new Task();
        someTask.setTitle("Обычная таска");
        someTask.setDescription("Описание таски");
        someTask.setStartTime(LocalDateTime.now());
        someTask.setDuration(Duration.ofMinutes(30));
        Task someTask2 = new Task();
        someTask2.setTitle("Обычная таска");
        someTask2.setDescription("Описание таски");
        someTask2.setStartTime(LocalDateTime.now());
        someTask2.setDuration(Duration.ofMinutes(30));
        Epic someAnotherEpicTask = new Epic();
        someAnotherEpicTask.setTitle("Эпическая задача_1");
        someAnotherEpicTask.setDescription("Это очень важная задача");
        someAnotherEpicTask.setStatus(Status.IN_PROGRESS);
        someAnotherEpicTask.setDuration(Duration.ofMinutes(15));
        someAnotherEpicTask.setStartTime(LocalDateTime.now().plusMinutes(40));
        Subtask someAnotherSubtask = new Subtask();
        someAnotherSubtask.setEpic(someAnotherEpicTask);
        someAnotherSubtask.setTitle("Сабтаска эпика 1");
        someAnotherSubtask.setDescription("Описание сабтаски");
        someAnotherSubtask.setStartTime(LocalDateTime.now());
        someAnotherSubtask.setDuration(Duration.ofMinutes(15));
        someAnotherSubtask.setStatus(Status.DONE);
        manager.addEpicTask(someAnotherEpicTask);
        manager.addSubTask(someAnotherSubtask);
        manager.addTask(someTask);
        manager.addTask(someTask2);

        GsonBuilder gsonBuilder = new GsonBuilder();
        gsonBuilder.setPrettyPrinting();
        gsonBuilder.serializeNulls();
        gsonBuilder.registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter());
        gsonBuilder.registerTypeAdapter(Duration.class, new DurationAdapter());
        Gson gson = gsonBuilder.create();

        String taskSerialized = gson.toJson(someTask);
        System.out.println(taskSerialized);
        String epicSerialized = gson.toJson(someAnotherEpicTask);
        System.out.println(epicSerialized);

        String subtaskSerialized = gson.toJson(someAnotherSubtask);
        System.out.println(subtaskSerialized);

        // связываем конкретный путь и его обработчик
        httpServer.createContext("/hello", new HelloHandler());
        httpServer.createContext("/tasks", new TaskHandler(manager));
        httpServer.createContext("/epics", new EpicHandler());
        httpServer.createContext("/subtasks", new SubtaskHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }
}
