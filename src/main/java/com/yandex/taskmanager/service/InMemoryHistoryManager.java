package com.yandex.taskmanager.service;

import com.yandex.taskmanager.HistoryManager;
import com.yandex.taskmanager.model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {

    private final Map<Integer, Node> HISTORY = new HashMap<>();
    private final Node HEAD;
    private final Node TAIL;

    public InMemoryHistoryManager() {
        HEAD = new Node();
        TAIL = new Node();
        HEAD.setNext(TAIL);
        TAIL.setPrev(HEAD);
    }

    @Override
    public void add(Task task) {
        Task taskCopy = new Task();
        taskCopy.setId(task.getId());
        taskCopy.createTitle(task.getTitle());
        taskCopy.setDescription(task.getDescription());
        taskCopy.setStatus(task.getStatus());
        remove(task.getId());
        Node newNode = new Node(taskCopy);
        linkLast(newNode);
        HISTORY.put(task.getId(), newNode);
    }

    private void linkLast(Node node) {
        node.setNext(TAIL);
        node.setPrev(TAIL.getPrev());
        TAIL.getPrev().setNext(node);
        TAIL.setPrev(node);
    }

    @Override
    public void remove(int id) {
        if (HISTORY.get(id) != null) {
            removeNode(HISTORY.get(id));
        }
    }

    private void removeNode(Node node) {
        HISTORY.remove(node.getTask().getId());
        node.getPrev().setNext(node.getNext());
        node.getNext().setPrev(node.getPrev());
    }

    private List<Task> getTasks() {
        List<Task> history = new ArrayList<>();
        Node current = HEAD.getNext();
        while (current != TAIL) {
            history.add(current.getTask());
            current = current.getNext();
        }
        return history;
    }


    @Override
    public List<Task> getHistory() {
        return getTasks();
    }
}
