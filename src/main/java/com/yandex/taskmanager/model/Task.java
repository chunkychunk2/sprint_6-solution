package com.yandex.taskmanager.model;

import com.yandex.taskmanager.Status;
import com.yandex.taskmanager.service.InMemoryTaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task implements Comparable<Task> {

    private String title;

    private String description;

    private Integer id;

    private Status status;

    private TaskTypes taskType;

    private Duration duration;

    private LocalDateTime startTime;

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

    public Task(int id, String title, Status status, String description,
                Duration duration, LocalDateTime startTime) {
        this.id = id;
        this.title = title;
        this.status = status;
        this.description = description;
        taskType = TaskTypes.TASK;
        this.startTime = startTime;
        this.duration = duration;
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

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getEndTime() {
        if (duration == null) {
            return startTime;
        }
        return startTime.plus(duration);
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
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
        return id + ", Task, " + title + ", " + status + ", "
                + description + ", " + duration.toMinutes() + ", " + startTime;
    }

    @Override
    public int compareTo(Task o) {
        return this.getStartTime().compareTo(o.getStartTime());
    }
}
