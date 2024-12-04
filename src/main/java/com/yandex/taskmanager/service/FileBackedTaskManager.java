package com.yandex.taskmanager.service;

import com.yandex.taskmanager.Status;
import com.yandex.taskmanager.exceptions.ManagerSaveException;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Subtask;
import com.yandex.taskmanager.model.Task;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTaskManager(String path) {
        file = new File(path);
    }

    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("id,type,name,status,description,epicId");
            bw.newLine();
            for (Task task : getAllTasks()) {
                bw.write(toString(task));
                bw.newLine();
            }
            for (Epic epic : getAllEpicTasks()) {
                bw.write(toString(epic));
                bw.newLine();
            }
            for (Subtask subTask : getAllSubTasks()) {
                bw.write(toString(subTask));
                bw.newLine();
            }
            bw.newLine();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка записи в файл: " + e.getMessage());
        }
    }

    public String toString(Task task) {
        return task.toString();
    }

    public Task fromString(String value) {
        String[] taskInfo = value.split(",");
        if (taskInfo[1].trim().equals("Task"))
            return new Task(Integer.parseInt(taskInfo[0]), taskInfo[2], Status.valueOf(taskInfo[3].trim()), taskInfo[4]);
        else if (taskInfo[1].trim().equals("Epic")) return new Epic(Integer.parseInt(taskInfo[0]), taskInfo[2],
                Status.valueOf(taskInfo[3].trim()), taskInfo[4]);
        else return new Subtask(Integer.parseInt(taskInfo[0]), taskInfo[2],
                    Status.valueOf(taskInfo[3].trim()), taskInfo[4], Integer.parseInt(taskInfo[5].trim()));
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager fromFile = new FileBackedTaskManager(file.toString());
        StringBuilder stringFile = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                stringFile.append(reader.readLine());
                stringFile.append(System.lineSeparator());
            }
            String[] lines = stringFile.toString().split(System.lineSeparator());
            List<Task> tasks = new ArrayList<>();
            for (int i = 1; i < lines.length; i++) {
                tasks.add(fromFile.fromString(lines[i]));
                switch (tasks.get(i-1).getTaskType()) {
                    case TASK: {
                        fromFile.addTask(tasks.get(i-1));
                        break;
                    }
                    case EPIC: {
                        fromFile.addEpicTask((Epic) tasks.get(i-1));
                        break;
                    }
                    case SUBTASK: {
                        fromFile.addSubTask((Subtask) tasks.get(i-1));
                        break;
                    }
                }
            }
            return fromFile;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void addTask(Task task) {
        super.addTask(task);
        save();
    }

    @Override
    public void addSubTask(Subtask task) {
        super.addSubTask(task);
        save();
    }

    @Override
    public void addEpicTask(Epic task) {
        super.addEpicTask(task);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public void deleteAllEpicTasks() {
        super.deleteAllEpicTasks();
        save();
    }

    @Override
    public void deleteAllSubTasks() {
        super.deleteAllSubTasks();
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteEpicTask(int id) {
        super.deleteEpicTask(id);
        save();
    }

    @Override
    public void deleteSubTask(int id) {
        super.deleteSubTask(id);
        save();
    }

    @Override
    public void updateTask(int id) {
        super.updateTask(id);
        save();
    }

    @Override
    public void updateEpicTask(int id) {
        super.updateEpicTask(id);
        save();
    }

    @Override
    public void updateSubTask(int id) {
        super.updateSubTask(id);
        save();
    }
}
