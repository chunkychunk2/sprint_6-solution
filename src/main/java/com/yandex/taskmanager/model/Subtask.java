package com.yandex.taskmanager.model;

import com.google.gson.annotations.Expose;
import com.yandex.taskmanager.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {

    private transient Epic epic;

    private Integer epicId;

    public Subtask() {
        setTaskType(TaskTypes.SUBTASK);
    }

    public Subtask(int id, String title, Status status, String description, int epicId) {
        setId(id);
        setTitle(title);
        setStatus(status);
        setDescription(description);
        setTaskType(TaskTypes.SUBTASK);
        this.epicId = epicId;
    }

    public Subtask(int id, String title, Status status, String description,
                   Duration duration, LocalDateTime startTime, int epicId) {
        setId(id);
        setTitle(title);
        setStatus(status);
        setDescription(description);
        setTaskType(TaskTypes.SUBTASK);
        this.epicId = epicId;
        setDuration(duration);
        setStartTime(startTime);
    }

    public Subtask(int id, String title, Status status, String description,
                   Duration duration, LocalDateTime startTime) {
        setId(id);
        setTitle(title);
        setStatus(status);
        setDescription(description);
        setTaskType(TaskTypes.SUBTASK);
        setDuration(duration);
        setStartTime(startTime);
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(Integer epicId) {
        this.epicId = epicId;
    }

    public void changeStatus(Status status) {
        this.setStatus(status);
        epic.isEpicDone();
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
        this.epicId = epic.getId();
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return getId() + ", Subtask, " + getTitle() + ", " + getStatus()
                + ", " + getDescription() + ", " + getDuration().toMinutes() + ", " + getStartTime() + ", " + epicId;
    }
}
