package com.yandex.taskmanager;

import com.yandex.taskmanager.model.Task;

import java.util.List;

public interface HistoryManager {

    List<Task> getHistory();

    void add(Task task);


}
