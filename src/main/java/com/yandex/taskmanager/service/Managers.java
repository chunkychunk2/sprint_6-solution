package com.yandex.taskmanager.service;

import com.yandex.taskmanager.HistoryManager;
import com.yandex.taskmanager.TaskManager;

public class Managers {

    public static TaskManager getDefault(String path) {
        return new FileBackedTaskManager(path);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

}
