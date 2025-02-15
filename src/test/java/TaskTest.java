import com.yandex.taskmanager.*;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Subtask;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.service.FileBackedTaskManager;
import com.yandex.taskmanager.service.InMemoryHistoryManager;
import com.yandex.taskmanager.service.Managers;
import org.junit.Assert;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {

    @Test
    void checkEpicStatusWithSubtasksWithNEWStatus() throws Exception {
        File tempFile = File.createTempFile("testFile", ".txt");
        String content = Files.readString(tempFile.toPath());
        assertEquals("", content);
        TaskManager manager = Managers.getDefault(tempFile.toPath().toString());
        Epic epic = new Epic();
        epic.setStatus(Status.IN_PROGRESS);
        epic.setDuration(Duration.ofMinutes(15));
        epic.setStartTime(LocalDateTime.now());
        Subtask subtask = new Subtask();
        subtask.setDuration(Duration.ofMinutes(15));
        subtask.setStartTime(LocalDateTime.now());
        Subtask subtask2 = new Subtask();
        subtask2.setDuration(Duration.ofMinutes(15));
        subtask2.setStartTime(LocalDateTime.now());
        epic.addSubtask(subtask);
        epic.addSubtask(subtask2);
        manager.addEpicTask(epic);
        manager.addSubTask(subtask);
        manager.addSubTask(subtask2);
        Assert.assertEquals(Status.NEW, epic.getStatus());
    }

    @Test
    void checkEpicStatusWithSubtasksWithDONEStatus() throws Exception {
        File tempFile = File.createTempFile("testFile", ".txt");
        String content = Files.readString(tempFile.toPath());
        assertEquals("", content);
        TaskManager manager = Managers.getDefault(tempFile.toPath().toString());
        Epic epic = new Epic();
        epic.setStatus(Status.IN_PROGRESS);
        epic.setStartTime(LocalDateTime.now());
        epic.setDuration(Duration.ofMinutes(15));
        Subtask subtask = new Subtask();
        subtask.setDuration(Duration.ofMinutes(15));
        subtask.setStartTime(LocalDateTime.now());
        Subtask subtask2 = new Subtask();
        subtask2.setDuration(Duration.ofMinutes(15));
        subtask2.setStartTime(LocalDateTime.now());
        epic.addSubtask(subtask);
        epic.addSubtask(subtask2);
        subtask.changeStatus(Status.DONE);
        subtask2.changeStatus(Status.DONE);
        manager.addEpicTask(epic);
        manager.addSubTask(subtask);
        manager.addSubTask(subtask2);
        Assert.assertEquals(Status.DONE, epic.getStatus());
    }

    @Test
    void checkEpicStatusWithSubtasksWithNEWAndDONEStatuses() throws Exception {
        File tempFile = File.createTempFile("testFile", ".txt");
        String content = Files.readString(tempFile.toPath());
        assertEquals("", content);
        TaskManager manager = Managers.getDefault(tempFile.toPath().toString());
        Epic epic = new Epic();
        epic.setStatus(Status.IN_PROGRESS);
        epic.setDuration(Duration.ofMinutes(15));
        epic.setStartTime(LocalDateTime.now());
        Subtask subtask = new Subtask();
        subtask.setDuration(Duration.ofMinutes(15));
        subtask.setStartTime(LocalDateTime.now());
        Subtask subtask2 = new Subtask();
        subtask2.setDuration(Duration.ofMinutes(15));
        subtask2.setStartTime(LocalDateTime.now());
        epic.addSubtask(subtask);
        epic.addSubtask(subtask2);
        subtask2.changeStatus(Status.DONE);
        manager.addEpicTask(epic);
        manager.addSubTask(subtask);
        manager.addSubTask(subtask2);
        Assert.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

    @Test
    void checkEpicStatusWithSubtasksWithINPROGRESSStatus() throws Exception {
        File tempFile = File.createTempFile("testFile", ".txt");
        String content = Files.readString(tempFile.toPath());
        assertEquals("", content);
        TaskManager manager = Managers.getDefault(tempFile.toPath().toString());
        Epic epic = new Epic();
        epic.setStatus(Status.IN_PROGRESS);
        epic.setDuration(Duration.ofMinutes(15));
        epic.setStartTime(LocalDateTime.now());
        Subtask subtask = new Subtask();
        subtask.setDuration(Duration.ofMinutes(15));
        subtask.setStartTime(LocalDateTime.now());
        Subtask subtask2 = new Subtask();
        subtask2.setDuration(Duration.ofMinutes(15));
        subtask2.setStartTime(LocalDateTime.now());
        epic.addSubtask(subtask);
        epic.addSubtask(subtask2);
        subtask.changeStatus(Status.IN_PROGRESS);
        subtask2.changeStatus(Status.IN_PROGRESS);
        manager.addEpicTask(epic);
        manager.addSubTask(subtask);
        manager.addSubTask(subtask2);
        Assert.assertEquals(Status.IN_PROGRESS, epic.getStatus());
    }

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
    void ManagersClassTest() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        String content = Files.readString(tempFile.toPath());
        assertEquals("", content);
        TaskManager manager = Managers.getDefault(tempFile.toPath().toString());
        HistoryManager historyManager = Managers.getDefaultHistory();
        assertEquals(manager.getClass(), FileBackedTaskManager.class);
        assertEquals(historyManager.getClass(), InMemoryHistoryManager.class);
        Task task = new Task();
        task.setDuration(Duration.ofMinutes(15));
        manager.addTask(task);
        manager.getTaskById(task.getId());
        assertFalse(manager.getHistory().isEmpty());
    }

    @Test
    void InMemoryTaskManagerTest() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        String content = Files.readString(tempFile.toPath());
        assertEquals("", content);
        TaskManager manager = Managers.getDefault(tempFile.toPath().toString());
        Task task = new Task();
        Epic epic = new Epic();
        task.setDuration(Duration.ofMinutes(15));
        task.setStartTime(LocalDateTime.now());
        epic.setDuration(Duration.ofMinutes(15));
        epic.setStartTime(LocalDateTime.now());
        Subtask subtask = new Subtask();
        subtask.setDuration(Duration.ofMinutes(15));
        subtask.setStartTime(LocalDateTime.now());
        epic.addSubtask(subtask);
        manager.addTask(task);
        manager.addEpicTask(epic);
        manager.addSubTask(subtask);
        assertEquals(task, manager.getTaskById(task.getId()));
        assertEquals(epic, manager.getEpicTaskById(epic.getId()));
        assertEquals(subtask, manager.getSubTaskById(subtask.getId()));
    }

    @Test
    void CreatedAndGeneratedIdsTest() throws IOException {
        Task task = new Task();
        Task task2 = new Task();
        task.setDuration(Duration.ofMinutes(15));
        task2.setDuration(Duration.ofMinutes(15));
        task2.setId(task.getId());
        File tempFile = File.createTempFile("testFile", ".txt");
        String content = Files.readString(tempFile.toPath());
        assertEquals("", content);
        TaskManager manager = Managers.getDefault(tempFile.toPath().toString());
        manager.addTask(task);
        manager.addTask(task2);
        assertEquals(1, manager.getAllTasks().size());
    }

    @Test
    void TaskTestAfterAddingToManager() throws IOException {
        Task task = new Task();
        task.setTitle("Таска");
        task.setDescription("Описание");
        task.setStatus(Status.IN_PROGRESS);
        int id = task.getId();
        File tempFile = File.createTempFile("testFile", ".txt");
        String content = Files.readString(tempFile.toPath());
        assertEquals("", content);
        TaskManager manager = Managers.getDefault(tempFile.toPath().toString());
        task.setDuration(Duration.ofMinutes(15));
        manager.addTask(task);
        assertEquals("Таска", task.getTitle());
        assertEquals("Описание", task.getDescription());
        assertEquals(Status.IN_PROGRESS, task.getStatus());
        assertEquals(id, task.getId());
    }

    @Test
    void addNewTask() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        String content = Files.readString(tempFile.toPath());
        assertEquals("", content);
        TaskManager manager = Managers.getDefault(tempFile.toPath().toString());
        Task task = new Task();
        task.setTitle("Test addNewTask");
        task.setDescription("Test addNewTask description");
        task.setStatus(Status.NEW);
        task.setDuration(Duration.ofMinutes(15));
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
    void checkDeletedSubtasksId() throws IOException {
        Epic epic = new Epic();
        Subtask subtask = new Subtask();
        epic.setDuration(Duration.ofMinutes(15));
        epic.setStartTime(LocalDateTime.now());
        subtask.setDuration(Duration.ofMinutes(15));
        subtask.setStartTime(LocalDateTime.now());
        File tempFile = File.createTempFile("testFile", ".txt");
        String content = Files.readString(tempFile.toPath());
        assertEquals("", content);
        epic.addSubtask(subtask);
        TaskManager taskManager = Managers.getDefault(tempFile.toPath().toString());
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subtask);
        epic.addSubtask(subtask);

        assertEquals(subtask.getId(), taskManager.getAllSubTasks().get(0).getId());
        taskManager.deleteSubTask(subtask.getId());
        assertEquals(0, taskManager.getAllSubTasks().size());
    }

    @Test
    void checkDeletedSubtasksIdInEpic() throws IOException {
        Epic epic = new Epic();
        Subtask subtask = new Subtask();
        epic.setDuration(Duration.ofMinutes(15));
        epic.setStartTime(LocalDateTime.now());
        subtask.setDuration(Duration.ofMinutes(15));
        subtask.setStartTime(LocalDateTime.now());
        File tempFile = File.createTempFile("testFile", ".txt");
        String content = Files.readString(tempFile.toPath());
        assertEquals("", content);
        epic.addSubtask(subtask);
        TaskManager taskManager = Managers.getDefault(tempFile.toPath().toString());
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subtask);
        epic.addSubtask(subtask);
        assertEquals(subtask, epic.getSubtasks().get(0));
        taskManager.deleteSubTask(subtask.getId());
        assertEquals(0, epic.getSubtasks().size());
    }

    @Test
    void objectSettersTest() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        String content = Files.readString(tempFile.toPath());
        assertEquals("", content);
        TaskManager taskManager = Managers.getDefault(tempFile.toPath().toString());
        Task task = new Task();
        Task task2 = new Task();
        task.setDuration(Duration.ofMinutes(15));
        task2.setDuration(Duration.ofMinutes(15));
        taskManager.addTask(task);
        taskManager.addTask(task2);
        taskManager.getTaskById(task.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task.getId());
        int taskId = task.getId();
        task.setId(task2.getId() + 1);
        taskManager.getTaskById(task.getId());
        List<Task> history = taskManager.getHistory();
        assertEquals(taskId, history.get(history.size() - 1).getId());
    }

    @Test
    void HistoryManagerTest() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        String content = Files.readString(tempFile.toPath());
        assertEquals("", content);
        TaskManager manager = Managers.getDefault(tempFile.toPath().toString());
        Task task = new Task();
        task.setDuration(Duration.ofMinutes(15));
        manager.addTask(task);
        task.setTitle("Таска_версия_1");
        manager.getTaskById(task.getId());
        assertEquals("Таска_версия_1", manager.getHistory().get(0).getTitle());
        task.setTitle("Таска_версия_2");
        manager.getTaskById(task.getId());
        task.setTitle("Таска_версия_3");
        assertEquals("Таска_версия_2", manager.getHistory().get(0).getTitle());
    }

    @Test
    void orderTest() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        String content = Files.readString(tempFile.toPath());
        assertEquals("", content);
        TaskManager taskManager = Managers.getDefault(tempFile.toPath().toString());
        Task task = new Task();
        Task task2 = new Task();
        Epic epic = new Epic();
        Epic epic2 = new Epic();
        task.setDuration(Duration.ofMinutes(15));
        task.setStartTime(LocalDateTime.now());
        task2.setDuration(Duration.ofMinutes(15));
        task2.setStartTime(LocalDateTime.now());
        epic.setDuration(Duration.ofMinutes(15));
        epic.setStartTime(LocalDateTime.now());
        epic2.setDuration(Duration.ofMinutes(15));
        epic2.setStartTime(LocalDateTime.now());
        Subtask subtask = new Subtask();
        Subtask subtask2 = new Subtask();
        subtask.setDuration(Duration.ofMinutes(15));
        subtask.setStartTime(LocalDateTime.now());
        subtask2.setDuration(Duration.ofMinutes(15));
        subtask2.setStartTime(LocalDateTime.now());
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
        assertEquals(task.getId(), history.get(history.size() - 1).getId());
    }

    @Test
    void deleteTaskFromHistoryTest() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        String content = Files.readString(tempFile.toPath());
        assertEquals("", content);
        TaskManager taskManager = Managers.getDefault(tempFile.toPath().toString());
        Task task = new Task();
        task.setDuration(Duration.ofMinutes(15));
        taskManager.addTask(task);
        taskManager.getTaskById(task.getId());
        assertEquals(1, taskManager.getHistory().size());
        taskManager.deleteTask(task.getId());
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    void deleteEpicFromHistoryTest() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        String content = Files.readString(tempFile.toPath());
        assertEquals("", content);
        TaskManager taskManager = Managers.getDefault(tempFile.toPath().toString());
        Epic epic = new Epic();
        epic.setDuration(Duration.ofMinutes(30));
        epic.setStartTime(LocalDateTime.now());
        Subtask subtask = new Subtask();
        Subtask subtask2 = new Subtask();
        Subtask subtask3 = new Subtask();
        subtask.setDuration(Duration.ofMinutes(15));
        subtask2.setDuration(Duration.ofMinutes(15));
        subtask3.setDuration(Duration.ofMinutes(15));
        subtask.setStartTime(LocalDateTime.now());
        subtask2.setStartTime(LocalDateTime.now());
        subtask3.setStartTime(LocalDateTime.now());
        epic.addSubtask(subtask);
        epic.addSubtask(subtask2);
        epic.addSubtask(subtask3);
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subtask);
        taskManager.addSubTask(subtask2);
        taskManager.addSubTask(subtask3);
        taskManager.getEpicTaskById(epic.getId());
        taskManager.getSubTaskById(subtask.getId());
        taskManager.getSubTaskById(subtask2.getId());
        taskManager.getSubTaskById(subtask3.getId());
        assertEquals(4, taskManager.getHistory().size());
        taskManager.deleteEpicTask(epic.getId());
        assertEquals(0, taskManager.getHistory().size());
    }

    @Test
    void whenEmptyHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        assertTrue(historyManager.getHistory().isEmpty());
    }
}