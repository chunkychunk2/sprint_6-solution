package com.yandex.taskmanager.service;

import com.yandex.taskmanager.HistoryManager;
import com.yandex.taskmanager.Status;
import com.yandex.taskmanager.TaskManager;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Subtask;
import com.yandex.taskmanager.model.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    private final Set<Task> prioritizedTasks = new TreeSet<>();
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

    public InMemoryTaskManager() {
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public void addTask(Task task) {
        if (!allTasks.containsKey(task.getId())) {
            allTasks.put(task.getId(), task);
            addTaskByPriority(task);
        } else {
            System.out.println("Задача с данным id уже существует!");
        }
    }

    private void updateEpicStatus(int epicId) {
        if (epicId != 0) {
            int doneCount = 0;
            int newCount = 0;
            for (int i = 0; i < allEpicTasks.get(epicId).getSubtasks().size(); i++) {
                if (allEpicTasks.get(epicId).getSubtasks().get(i).getStatus() == Status.DONE) {
                    doneCount++;
                }
                if (allEpicTasks.get(epicId).getSubtasks().get(i).getStatus() == Status.NEW) {
                    newCount++;
                }
            }
            if (allEpicTasks.get(epicId).getSubtasks().isEmpty()) {
                allEpicTasks.get(epicId).setStatus(Status.NEW);
            } else if (doneCount == allEpicTasks.get(epicId).getSubtasks().size()) {
                allEpicTasks.get(epicId).setStatus(Status.DONE);
            } else if (newCount == allEpicTasks.get(epicId).getSubtasks().size()) {
                allEpicTasks.get(epicId).setStatus(Status.NEW);
            } else {
                allEpicTasks.get(epicId).setStatus(Status.IN_PROGRESS);
            }
        }
    }

    @Override
    public void addSubTask(Subtask task) {
        if (!allSubTasks.containsKey(task.getId())) {
            allSubTasks.put(task.getId(), task);
            Epic epicTask = allEpicTasks.get(task.getEpicId());
            epicTask.addSubtask(task);
            updateEpicStartTime(task.getEpicId());
            updateEpicEndTime(task.getEpicId());
            updateEpicDuration(task.getEpicId());
            addTaskByPriority(task);
            updateEpicStatus(task.getEpicId());
        } else {
            System.out.println("Подзадача с данным id уже существует!");
        }
    }

    @Override
    public void addEpicTask(Epic task) {
        if (!allEpicTasks.containsKey(task.getId())) {
            allEpicTasks.put(task.getId(), task);
            int id = task.getId();
            updateEpicStartTime(id);
            updateEpicEndTime(id);
            updateEpicDuration(id);
            addTaskByPriority(task);
        } else {
            System.out.println("Эпик с данным id уже существует!");
        }
    }

    @Override
    public void deleteAllTasks() {
        allTasks.keySet().forEach(subtask -> prioritizedTasks.remove(allTasks.get(subtask)));
        allTasks.clear();
    }

    @Override
    public void deleteAllSubTasks() {
        allSubTasks.keySet().forEach(subtask -> prioritizedTasks.remove(allSubTasks.get(subtask)));
        for (Map.Entry<Integer, Epic> entry : allEpicTasks.entrySet()) {
            entry.getValue().deleteAllSubtask();
            int id = entry.getValue().getId();
            updateEpicStartTime(id);
            updateEpicEndTime(id);
            updateEpicDuration(id);
            updateEpicStatus(id);
        }
        allSubTasks.clear();
        changeStatus();
    }

    @Override
    public void deleteAllEpicTasks() {
        allEpicTasks.values().forEach(prioritizedTasks::remove);
        allSubTasks.values().forEach(prioritizedTasks::remove);
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
        if (allTasks.get(id) != null) {
            historyManager.add(allTasks.get(id));
        }
        return allTasks.get(id);
    }

    @Override
    public Subtask getSubTaskById(int id) {
        if (allSubTasks.get(id) != null) {
            historyManager.add(allSubTasks.get(id));
        }
        return allSubTasks.get(id);
    }

    @Override
    public Epic getEpicTaskById(int id) {
        if (allEpicTasks.get(id) != null) {
            historyManager.add(allEpicTasks.get(id));
        }
        return allEpicTasks.get(id);
    }

    @Override
    public void updateTask(Task task) {
        allTasks.put(task.getId(), task);
        prioritizedTasks.remove(task);
        addTaskByPriority(task);
    }


    @Override
    public void updateSubTask(Subtask subtask) {
        int id = subtask.getId();
        allSubTasks.put(id, subtask);
        Epic epicTask = allEpicTasks.get(subtask.getEpicId());
        epicTask.deleteSubtask(subtask);
        epicTask.addSubtask(subtask);
        updateEpicStatus(id);
        updateEpicStartTime(id);
        updateEpicEndTime(id);
        updateEpicDuration(id);
        prioritizedTasks.remove(subtask);
        addTaskByPriority(subtask);
    }

    @Override
    public void updateEpicTask(Epic epicTask) {
        int id = epicTask.getId();
        allEpicTasks.put(id, epicTask);
        updateEpicStatus(id);
        updateEpicStartTime(id);
        updateEpicEndTime(id);
        updateEpicDuration(id);
        prioritizedTasks.remove(epicTask);
        addTaskByPriority(epicTask);
    }

    @Override
    public void deleteTask(int id) {
        prioritizedTasks.remove(allTasks.get(id));
        allTasks.remove(id);
        historyManager.remove(id);

    }

    @Override
    public void deleteSubTask(int id) {
        prioritizedTasks.remove(allSubTasks.get(id));
        allSubTasks.get(id).getEpic().deleteSubtask(getSubTaskById(id));
        allSubTasks.remove(id);
        historyManager.remove(id);
        try {
            updateEpicStatus(allSubTasks.get(id).getEpic().getId());
        } catch (NullPointerException e) {
            System.out.println("Такого эпика не существует!");
        }
    }

    @Override
    public void deleteEpicTask(int id) {
        for (Subtask subtask : allEpicTasks.get(id).getSubtasks()) {
            prioritizedTasks.remove(subtask);
            historyManager.remove(subtask.getId());
        }
        prioritizedTasks.remove(allEpicTasks.get(id));

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

    @Override
    public boolean isTaskOverlap(Task task) {
        LocalDateTime startTime = task.getStartTime();
        LocalDateTime endTime = task.getEndTime();
        return prioritizedTasks.stream()
                .filter(prioritizedTask -> prioritizedTask.getStartTime() != null)
                .anyMatch(prioritizedTask -> !endTime.isBefore(prioritizedTask.getStartTime())
                        && !prioritizedTask.getEndTime().isBefore(startTime));
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    private void addTaskByPriority(Task task) {
        if (task.getStartTime() != null && !isTaskOverlap(task)) {
            prioritizedTasks.add(task);
        }
    }

    protected void updateEpicStartTime(int epicId) {
        Epic epicTask = allEpicTasks.get(epicId);
        List<Subtask> subTasks = epicTask.getSubtasks();
        epicTask.setStartTime(calculateEpicStartTime(subTasks));
    }

    private LocalDateTime calculateEpicStartTime(List<Subtask> subTasks) {
        return subTasks.stream()
                .map(Task::getStartTime)
                .min(Comparator.naturalOrder()).orElse(null);
    }

    protected void updateEpicEndTime(int epicId) {
        Epic epicTask = allEpicTasks.get(epicId);
        List<Subtask> subTasks = epicTask.getSubtasks();
        epicTask.setEndTime(calculateEpicEndTime(subTasks));
    }

    private LocalDateTime calculateEpicEndTime(List<Subtask> subTasks) {
        return subTasks.stream()
                .map(Subtask::getEndTime)
                .max(Comparator.naturalOrder()).orElse(null);
    }

    protected void updateEpicDuration(int epicId) {
        Epic epicTask = allEpicTasks.get(epicId);
        List<Subtask> subTasks = epicTask.getSubtasks();
        epicTask.setDuration(calculateEpicDuration(subTasks));
    }

    private Duration calculateEpicDuration(List<Subtask> subTasks) {
        return subTasks.stream()
                .map(Task::getDuration)
                .reduce(Duration.ZERO, Duration::plus);
    }

}
