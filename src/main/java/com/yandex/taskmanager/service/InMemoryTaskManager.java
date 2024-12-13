package com.yandex.taskmanager.service;

import com.yandex.taskmanager.HistoryManager;
import com.yandex.taskmanager.Status;
import com.yandex.taskmanager.TaskManager;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Subtask;
import com.yandex.taskmanager.model.Task;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final Map<Integer, Task> allTasks = new HashMap<>();
    private final Map<Integer, Subtask> allSubTasks = new HashMap<>();
    private final Map<Integer, Epic> allEpicTasks = new HashMap<>();

    private final HistoryManager historyManager;

    private static int taskId = 0;

    private static void setTaskId() {
        taskId++;
    }

    public static int getTaskId() {
        setTaskId();
        return taskId;
    }

    InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public void addTask(Task task) {
        if (!allTasks.containsKey(task.getId())) allTasks.put(task.getId(), task);
        else System.out.println("Задача с данным id уже существует!");
    }

    @Override
    public void addSubTask(Subtask task) {
        if (!allSubTasks.containsKey(task.getId())) allSubTasks.put(task.getId(), task);
        else System.out.println("Подзадача с данным id уже существует!");
    }

    @Override
    public void addEpicTask(Epic task) {
        if (!allEpicTasks.containsKey(task.getId())) allEpicTasks.put(task.getId(), task);
        else System.out.println("Эпик с данным id уже существует!");
    }

    @Override
    public void deleteAllTasks() {
        allTasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        for (Map.Entry<Integer, Epic> entry : allEpicTasks.entrySet()) entry.getValue().deleteAllSubtask();
        allSubTasks.clear();
        changeStatus();
    }

    @Override
    public void deleteAllEpicTasks() {
        allEpicTasks.clear();
        allSubTasks.clear();
    }

    @Override
    public ArrayList<Task> getAllTasks() {
        ArrayList<Task> taskList = new ArrayList<>();
        for (Map.Entry<Integer, Task> entry : allTasks.entrySet()) taskList.add(entry.getValue());
        return taskList;
    }

    @Override
    public ArrayList<Epic> getAllEpicTasks() {
        ArrayList<Epic> taskList = new ArrayList<>();
        for (Map.Entry<Integer, Epic> entry : allEpicTasks.entrySet()) taskList.add(entry.getValue());
        return taskList;
    }

    @Override
    public ArrayList<Subtask> getAllSubTasks() {
        ArrayList<Subtask> taskList = new ArrayList<>();
        for (Map.Entry<Integer, Subtask> entry : allSubTasks.entrySet()) taskList.add(entry.getValue());

        return taskList;
    }

    @Override
    public Task getTaskById(int id) {
        if (allTasks.get(id) != null) historyManager.add(allTasks.get(id));
        return allTasks.get(id);
    }

    @Override
    public Subtask getSubTaskById(int id) {
        if (allSubTasks.get(id) != null) historyManager.add(allSubTasks.get(id));
        return allSubTasks.get(id);
    }

    @Override
    public Epic getEpicTaskById(int id) {
        if (allEpicTasks.get(id) != null) historyManager.add(allEpicTasks.get(id));
        return allEpicTasks.get(id);
    }

    @Override
    public void updateTask(int id) {
        for (Map.Entry<Integer, Task> entry : allTasks.entrySet()) {
            if (entry.getKey().equals(id)) allTasks.put(id, entry.getValue());
        }
    }

    @Override
    public void updateSubTask(int id) {
        for (Map.Entry<Integer, Subtask> entry : allSubTasks.entrySet()) {
            if (entry.getKey().equals(id)) allSubTasks.put(id, entry.getValue());
        }
    }

    @Override
    public void updateEpicTask(int id) {
        for (Map.Entry<Integer, Epic> entry : allEpicTasks.entrySet()) {
            if (entry.getKey().equals(id)) allEpicTasks.put(id, entry.getValue());
        }
    }

    @Override
    public void deleteTask(int id) {
        allTasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteSubTask(int id) {
        allSubTasks.get(id).getEpic().deleteSubtask(getSubTaskById(id));
        allSubTasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public void deleteEpicTask(int id) {
        for (Subtask subtask : allEpicTasks.get(id).getSubtasks()) {
            historyManager.remove(subtask.getId());
        }
        allEpicTasks.get(id).getSubtasks().clear();
        allEpicTasks.remove(id);
        historyManager.remove(id);
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        return epic.getSubtasks();
    }

    @Override
    public void getTaskInfo(Task task) {
        System.out.println("Название: " + task.getTitle());
        System.out.println("Описание: " + task.getDescription());
        System.out.println("Идентификатор: " + task.getId());
        System.out.println("Статус: " + task.getStatus());
    }

    @Override
    public void changeStatus() {
        for (Map.Entry<Integer, Epic> entry : allEpicTasks.entrySet()) entry.getValue().setStatus(Status.IN_PROGRESS);
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }
}
