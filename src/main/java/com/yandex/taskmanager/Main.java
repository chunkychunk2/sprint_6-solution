package com.yandex.taskmanager;

import com.yandex.taskmanager.service.HttpTaskServer;
import com.yandex.taskmanager.service.Managers;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException {
        TaskManager taskManager = Managers.getDefault("src/main/resources/tasks_info.csv");
        HttpTaskServer httpTaskServer = new HttpTaskServer(taskManager);
        httpTaskServer.start();
    }
}
