package com.yandex.taskmanager.model;

import com.yandex.taskmanager.Status;

public class Subtask extends Task {

    private Epic epic;

    private Integer epicId;

    public Subtask() {
        setTaskType(TaskTypes.SUBTASK);
    }

    public Subtask(int id, String title, Status status, String description, int epicId) {
        setId(id);
        createTitle(title);
        setStatus(status);
        setDescription(description);
        setTaskType(TaskTypes.SUBTASK);
        this.epicId = epicId;
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
        return getId() + ", Subtask, " + getTitle() + ", " + getStatus() + ", " + getDescription() + ", " + epicId;
    }
}
