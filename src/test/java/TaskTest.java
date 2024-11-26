import com.yandex.taskmanager.*;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Subtask;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.service.InMemoryHistoryManager;
import com.yandex.taskmanager.service.InMemoryTaskManager;
import com.yandex.taskmanager.service.Managers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void isTasksEquals() {
        Task task = new Task();
        Task task2 = new Task();
        task.setId(12);
        task2.setId(12);
        Assertions.assertEquals(task, task2, "Задачи не равны! id первой задачи: " + task.getId()
                + ", id второй задачи: " + task2.getId());
    }

    @Test
    void isSubtasksEquals() {
        Subtask subtask = new Subtask();
        Subtask subtask2 = new Subtask();
        subtask.setId(10);
        subtask2.setId(10);
        Assertions.assertEquals(subtask, subtask2, "Задачи не равны! id первой задачи: " + subtask.getId()
                + ", id второй подзадачи: " + subtask2.getId());
    }

    @Test
    void isEpictasksEquals() {
        Epic epic = new Epic();
        Epic epic2 = new Epic();
        epic.setId(135);
        epic2.setId(135);
        assertEquals(epic, epic2, "Задачи не равны! id первого эпика: " + epic.getId()
                + ", id второго эпика: " + epic2.getId());
    }

    @Test
    void ManagersClassTest() {
        TaskManager manager = Managers.getDefault();
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertEquals(manager.getClass(), InMemoryTaskManager.class);
        assertEquals(historyManager.getClass(), InMemoryHistoryManager.class);
        Task task = new Task();
        manager.addTask(task);
        manager.getTaskById(task.getId());
        assertFalse(manager.getHistory().isEmpty());
    }

    @Test
    void InMemoryTaskManagerTest() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task();
        Epic epic = new Epic();
        Subtask subtask = new Subtask();
        manager.addTask(task);
        manager.addEpicTask(epic);
        manager.addSubTask(subtask);
        assertEquals(task, manager.getTaskById(task.getId()));
        assertEquals(epic, manager.getEpicTaskById(epic.getId()));
        assertEquals(subtask, manager.getSubTaskById(subtask.getId()));
    }

    @Test
    void CreatedAndGeneratedIdsTest() {
        Task task = new Task();
        Task task2 = new Task();
        task2.setId(task.getId());
        TaskManager manager = Managers.getDefault();
        manager.addTask(task);
        manager.addTask(task2);
        assertEquals(1, manager.getAllTasks().size());
    }

    @Test
    void TaskTestAfterAddingToManager() {
        Task task = new Task();
        task.createTitle("Таска");
        task.setDescription("Описание");
        task.setStatus(Status.IN_PROGRESS);
        int id = task.getId();
        TaskManager manager = Managers.getDefault();
        manager.addTask(task);
        assertEquals("Таска", task.getTitle());
        assertEquals("Описание", task.getDescription());
        assertEquals(Status.IN_PROGRESS, task.getStatus());
        assertEquals(id, task.getId());
    }

    @Test
    void addNewTask() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task();
        task.createTitle("Test addNewTask");
        task.setDescription("Test addNewTask description");
        task.setStatus(Status.NEW);
        manager.addTask(task);

        int taskId = task.getId();

        Task savedTask = manager.getTaskById(taskId);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        final ArrayList<Task> tasks = manager.getAllTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void add() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task task = new Task();
        Task task2 = new Task();
        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertNotNull(history, "История не пустая.");
    }

    @Test
    void checkDeletedSubtasksId() {
        Epic epic = new Epic();
        Subtask subtask = new Subtask();
        TaskManager taskManager = Managers.getDefault();
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subtask);
        epic.addSubtask(subtask);

        assertEquals(subtask.getId(), taskManager.getAllSubTasks().get(0).getId());
        taskManager.deleteSubTask(subtask.getId());
        assertEquals(0, taskManager.getAllSubTasks().size());
    }

    @Test
    void checkDeletedSubtasksIdInEpic() {
        Epic epic = new Epic();
        Subtask subtask = new Subtask();
        TaskManager taskManager = Managers.getDefault();
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subtask);
        epic.addSubtask(subtask);
        assertEquals(subtask, epic.getSubtasks().get(0));
        taskManager.deleteSubTask(subtask.getId());
        assertEquals(0, epic.getSubtasks().size());
    }

    @Test
    void objectSettersTest() {
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task();
        Task task2 = new Task();
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task.getId());
        int taskId = task.getId();
        task.setId(task2.getId() + 1);
        taskManager.getTaskById(task.getId());
        List<Task> history = taskManager.getHistory();
        assertEquals(taskId, history.get(history.size()-1).getId());
    }

    @Test
    void HistoryManagerTest() {
        TaskManager manager = Managers.getDefault();
        Task task = new Task();
        manager.addTask(task);
        task.createTitle("Таска_версия_1");
        manager.getTaskById(task.getId());
        assertEquals("Таска_версия_1", manager.getHistory().get(0).getTitle());
        task.createTitle("Таска_версия_2");
        manager.getTaskById(task.getId());
        task.createTitle("Таска_версия_3");
        assertEquals("Таска_версия_2", manager.getHistory().get(0).getTitle());
    }

    @Test
    void orderTest() {
        TaskManager taskManager = Managers.getDefault();
        Task task = new Task();
        Task task2 = new Task();
        Epic epic = new Epic();
        Epic epic2 = new Epic();
        Subtask subtask = new Subtask();
        Subtask subtask2 = new Subtask();
        epic.addSubtask(subtask);
        epic2.addSubtask(subtask2);
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.addEpicTask(epic);
        taskManager.addEpicTask(epic2);
        taskManager.addSubTask(subtask);
        taskManager.addSubTask(subtask2);
        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getEpicTaskById(epic.getId());
        taskManager.getEpicTaskById(epic2.getId());
        taskManager.getSubTaskById(subtask.getId());
        taskManager.getSubTaskById(subtask2.getId());
        taskManager.getTaskById(task.getId());
        List<Task> history = taskManager.getHistory();

        System.out.println(history);
        System.out.println(task);
        assertEquals(task.getId(), history.get(history.size()-1).getId());
    }
}