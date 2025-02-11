package com.yandex.taskmanager.model;

import com.yandex.taskmanager.Status;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

public class Epic extends Task {

    private final ArrayList<Subtask> subtasks;

    private LocalDateTime endTime;

    public Epic() {
        subtasks = new ArrayList<>();
        setTaskType(TaskTypes.EPIC);
    }

    public Epic(int id, String title, Status status, String description) {
        setId(id);
        createTitle(title);
        setStatus(status);
        setDescription(description);
        subtasks = new ArrayList<>();
        setTaskType(TaskTypes.EPIC);
    }

    public Epic(int id, String title, Status status, String description,
                Duration duration, LocalDateTime startTime) {
        setId(id);
        createTitle(title);
        setStatus(status);
        setDescription(description);
        subtasks = new ArrayList<>();
        setTaskType(TaskTypes.EPIC);
        setDuration(duration);
        setStartTime(startTime);
    }

    public void deleteSubtask(Subtask subtask) {
        subtasks.remove(subtask);
    }

    public void deleteAllSubtask() {
        subtasks.clear();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void addSubtask(Subtask subtask) {
        if (!subtasks.contains(subtask)) {
            subtasks.add(subtask);
            subtask.setEpic(this);
            setStatus(Status.NEW);
        } else System.out.println("Подзадача уже есть в данном эпике");
    }

    public boolean isEpicDone() {
        boolean result = true;
        for (Task subtask : subtasks) {
            if (!subtask.getStatus().equals(Status.DONE)) {
                result = false;
                break;
            }
        }
        if (result) setStatus(Status.DONE);
        else setStatus(Status.IN_PROGRESS);
        return result;
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return getId() + ", Epic, " + getTitle() + ", " + getStatus()
                + ", " + getDescription() + ", " + getDuration().toMinutes() + ", " + getStartTime();
    }
}
