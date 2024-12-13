package com.yandex.taskmanager.model;

import com.yandex.taskmanager.Status;
import com.yandex.taskmanager.service.InMemoryTaskManager;

import java.util.Objects;

public class Task {

    private String title;

    private String description;

    private Integer id;

    private Status status;

    private TaskTypes taskType;

    public Task() {
        status = Status.NEW;
        id = InMemoryTaskManager.getTaskId();
        taskType = TaskTypes.TASK;
    }

    public Task(int id, String title, Status status, String description) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.description = description;
        taskType = TaskTypes.TASK;
    }

    public TaskTypes getTaskType() {
        return taskType;
    }

    public void setTaskType(TaskTypes taskType) {
        this.taskType = taskType;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }


    public Integer getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void createTitle(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return Objects.equals(id, task.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return id + ", Task, " + title + ", " + status + ", " + description;
    }
}
