package com.yandex.taskmanager.model;

import com.yandex.taskmanager.Status;

public class Subtask extends Task {

    private Epic epic;

    public void changeStatus(Status status) {
        this.setStatus(status);
        epic.isEpicDone();
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    public Epic getEpic() {
        return epic;
    }
}
