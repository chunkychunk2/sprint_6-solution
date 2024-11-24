package com.yandex.taskmanager;

import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Subtask;
import com.yandex.taskmanager.model.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    void addTask(Task task);

    void addSubTask(Subtask task);

    void addEpicTask(Epic task);

    void deleteAllTasks();

    void deleteAllSubTasks();

    void deleteAllEpicTasks();

    ArrayList<Task> getAllTasks();

    ArrayList<Epic> getAllEpicTasks();

    ArrayList<Subtask> getAllSubTasks();

    Task getTaskById(int id);

    Subtask getSubTaskById(int id);

    Epic getEpicTaskById(int id);

    void updateTask(int task);

    void updateSubTask(int task);

    void updateEpicTask(int task);

    void deleteTask(int id);

    void deleteSubTask(int id);

    void deleteEpicTask(int id);

    ArrayList<Subtask> getEpicSubtasks(Epic epic);

    void getTaskInfo(Task task);

    void changeStatus();

    List<Task> getHistory();

}
