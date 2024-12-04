package com.yandex.taskmanager.service;

import com.yandex.taskmanager.HistoryManager;
import com.yandex.taskmanager.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
    public static TaskManager getDefaultFileBacked(String path) {
        return new FileBackedTaskManager(path);
    }
}
