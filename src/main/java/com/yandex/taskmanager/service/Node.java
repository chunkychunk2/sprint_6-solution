package com.yandex.taskmanager.service;

import com.yandex.taskmanager.model.Task;

public class Node {
    private Task task;
    private Node prev;
    private Node next;

    public Node() {
    }

    public Node(Task task) {
        this.task = task;
    }

    public Task getTask() {
        return task;
    }

    public Node getPrev() {
        return prev;
    }

    public void setPrev(Node prev) {
        this.prev = prev;
    }

    public Node getNext() {
        return next;
    }

    public void setNext(Node next) {
        this.next = next;
    }
}