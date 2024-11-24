package com.yandex.taskmanager.service;

import com.yandex.taskmanager.HistoryManager;
import com.yandex.taskmanager.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final static Map<Integer, Task> HISTORY = new LinkedHashMap<>();

    @Override
    public void add(Task task) {
        Task newTask = new Task();
        newTask.setId(task.getId());
        newTask.createTitle(task.getTitle());
        newTask.setDescription(task.getDescription());
        newTask.setStatus(task.getStatus());
        HISTORY.remove(newTask.getId());
        HISTORY.put(newTask.getId(),newTask);
    }

    @Override
    public List<Task> getHistory() {
        return new LinkedList<>(HISTORY.values());
    }
}
