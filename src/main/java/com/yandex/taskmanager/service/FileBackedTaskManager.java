package com.yandex.taskmanager.service;

import com.yandex.taskmanager.Status;
import com.yandex.taskmanager.exceptions.ManagerLoadException;
import com.yandex.taskmanager.exceptions.ManagerSaveException;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Subtask;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.model.TaskTypes;

import java.io.*;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public class FileBackedTaskManager extends InMemoryTaskManager {

    private File file;

    public FileBackedTaskManager(String path) {
        file = new File(path);
    }

    public void save() {
        try (BufferedWriter bw = new BufferedWriter(new FileWriter(file))) {
            bw.write("id,type,name,status,description,duration,startTime,epicId");
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
        TaskTypes taskType = TaskTypes.valueOf(taskInfo[1].trim().toUpperCase(Locale.ROOT));
        String taskStatus = taskInfo[3].trim();
        switch (taskType) {
            case TASK:
                return new Task(Integer.parseInt(taskInfo[0]), taskInfo[2], Status.valueOf(taskStatus),
                        taskInfo[4], Duration.ofMinutes(Long.parseLong(taskInfo[5].trim())), LocalDateTime.parse(taskInfo[6].trim()));
            case EPIC:
                System.out.println(taskInfo[0] + " " + taskInfo[1] + " " + taskInfo[2] + " " + taskInfo[3] + " " + taskInfo[4] + " " + taskInfo[5] + " " + taskInfo[6]);
                return new Epic(Integer.parseInt(taskInfo[0]), taskInfo[2],
                        Status.valueOf(taskStatus), taskInfo[4],Duration.ofMinutes(Long.parseLong(taskInfo[5].trim())), LocalDateTime.parse(taskInfo[6].trim()));
            case SUBTASK:
                return new Subtask(Integer.parseInt(taskInfo[0]), taskInfo[2],
                        Status.valueOf(taskStatus), taskInfo[4], Duration.ofMinutes(Long.parseLong(taskInfo[5].trim())),
                        LocalDateTime.parse(taskInfo[6].trim()), Integer.parseInt(taskInfo[7].trim()));
            default:
                throw new ManagerLoadException("Ошибка чтения записи: " + Arrays.toString(taskInfo));
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerLoadException {
        FileBackedTaskManager fromFile = new FileBackedTaskManager(file.toString());
        StringBuilder stringFile = new StringBuilder();
        try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
            while (reader.ready()) {
                stringFile.append(reader.readLine());
                stringFile.append(System.lineSeparator());
            }
            String[] lines = stringFile.toString().split(System.lineSeparator());
            for (int i = 1; i < lines.length; i++) {
                Task task = fromFile.fromString(lines[i]);
                switch (task.getTaskType()) {
                    case TASK: {
                        fromFile.addTask(task);
                        break;
                    }
                    case EPIC: {
                        fromFile.addEpicTask((Epic) task);
                        break;
                    }
                    case SUBTASK: {
                        fromFile.addSubTask((Subtask) task);
                        break;
                    }
                }
            }
            return fromFile;

        } catch (IOException e) {
            throw new ManagerLoadException(e.toString());
        } catch (Exception e) {
            throw e;
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
