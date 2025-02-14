import com.yandex.taskmanager.Status;
import com.yandex.taskmanager.TaskManager;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Subtask;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.service.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class TaskManagerTest <T extends TaskManager> {
    protected TaskManager taskManager;

    @BeforeEach
    void setUp() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        taskManager = Managers.getDefault(tempFile.toPath().toString());
    }

    @Test
    void getAllTasks() {
        Task task1 = new Task(1, "name1", Status.NEW, "description", Duration.ofMinutes(15),
                LocalDateTime.now());
        Task task2 = new Task(2, "name2", Status.NEW, "description2", Duration.ofMinutes(15),
                LocalDateTime.now());
        List<Task> expected = new ArrayList<>();
        expected.add(task1);
        expected.add(task2);
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        List<Task> actual = taskManager.getAllTasks();
        assertEquals(expected, actual);
    }

    @Test
    void getAllEpic() {
        Epic task1 = new Epic(1, "name1", Status.NEW, "description");
        Epic task2 = new Epic(2, "name2", Status.NEW, "description2");
        List<Epic> expected = new ArrayList<>();
        expected.add(task1);
        expected.add(task2);
        taskManager.addEpicTask(task1);
        taskManager.addEpicTask(task2);
        List<Epic> actual = taskManager.getAllEpicTasks();
        assertEquals(expected,actual );
    }

    @Test
    void deleteAllTasks() {
        Task task1 = new Task(1, "name1", Status.NEW, "description", Duration.ofMinutes(15),
                LocalDateTime.now());
        Task task2 = new Task(2, "name2", Status.NEW, "description2", Duration.ofMinutes(15),
                LocalDateTime.now());
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.deleteAllTasks();
        List<Task> actual = taskManager.getAllTasks();
        assertTrue(actual.isEmpty());
    }

    @Test
    void deleteAllEpic() {
        Epic task1 = new Epic(1, "name1", Status.NEW, "description", Duration.ofMinutes(15),
                LocalDateTime.now());
        Epic task2 = new Epic(2, "name2", Status.NEW, "description2", Duration.ofMinutes(15),
                LocalDateTime.now());
        taskManager.addEpicTask(task1);
        taskManager.addEpicTask(task2);
        taskManager.deleteAllEpicTasks();
        List<Epic> actual = taskManager.getAllEpicTasks();
        assertTrue(actual.isEmpty());
    }

    @Test
    void deleteAllSubTask() {
        Epic epic = new Epic(1, "name1", Status.NEW, "description", Duration.ofMinutes(15),
                LocalDateTime.now());
        Subtask task1 = new Subtask(2, "name2", Status.NEW, "description2", Duration.ofMinutes(15),
                LocalDateTime.now(), epic.getId());
        Subtask task2 = new Subtask(3, "name3", Status.NEW, "description3", Duration.ofMinutes(15),
                LocalDateTime.now(), epic.getId());
        epic.addSubtask(task1);
        epic.addSubtask(task2);
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(task1);
        taskManager.addSubTask(task2);
        taskManager.deleteAllSubTasks();
        List<Subtask> actual = taskManager.getAllSubTasks();
        assertTrue(actual.isEmpty());
    }

    @Test
    void updateTask() {
        Task task1 = new Task(1, "name1", Status.NEW, "description", Duration.ofMinutes(15),
                LocalDateTime.now());
        taskManager.addTask(task1);
        Task oldTask = taskManager.getTaskById(1);
        Task update = new Task(task1.getId(), "name2", Status.NEW, "description2", Duration.ofMinutes(15),
                LocalDateTime.now());
        taskManager.addTask(task1);
        taskManager.updateTask(update);
        Task updateTask = taskManager.getTaskById(oldTask.getId());
        assertEquals(update, updateTask);
    }

    @Test
    void updateEpic() {
        Epic task1 = new Epic(1, "name1", Status.NEW, "description", Duration.ofMinutes(15),
                LocalDateTime.now());
        taskManager.addEpicTask(task1);
        Epic update = new Epic(task1.getId(), "name2", Status.NEW, "description2", Duration.ofMinutes(15),
                LocalDateTime.now());
        taskManager.updateEpicTask(update);
        Task updateTask = taskManager.getEpicTaskById(task1.getId());
        assertEquals(update, updateTask);
    }

    @Test
    void subTaskStatusIsNewThenEpicStatusIsNew() {
        Epic epic = new Epic(1, "name1", Status.IN_PROGRESS, "description", Duration.ofMinutes(15),
                LocalDateTime.now());
        Subtask task1 = new Subtask(2, "name1", Status.NEW, "description2", Duration.ofMinutes(15),
                LocalDateTime.now(), epic.getId());
        Subtask task2 = new Subtask(3, "name1", Status.NEW, "description3", Duration.ofMinutes(15),
                LocalDateTime.now(), epic.getId());
        epic.addSubtask(task1);
        epic.addSubtask(task2);
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(task1);
        taskManager.addSubTask(task2);
        assertEquals(Status.NEW, taskManager.getEpicTaskById(1).getStatus());
    }

    @Test
    void subTaskStatusIsDoneThenEpicStatusIsDone() {
        Epic epic = new Epic(1, "name1", Status.IN_PROGRESS, "description", Duration.ofMinutes(15),
                LocalDateTime.now());
        Subtask task1 = new Subtask(2, "name2", Status.DONE, "description2", Duration.ofMinutes(15),
                LocalDateTime.now(), epic.getId());
        Subtask task2 = new Subtask(3, "name3", Status.DONE, "description3", Duration.ofMinutes(15),
                LocalDateTime.now(), epic.getId());
        epic.addSubtask(task1);
        epic.addSubtask(task2);
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(task1);
        taskManager.addSubTask(task2);
        assertEquals(Status.DONE, taskManager.getEpicTaskById(1).getStatus());
    }

    @Test
    void subTaskStatusIsDoneAndNewThenEpicStatusIsInProgress() {
        Epic epic = new Epic(1, "name1", Status.NEW, "description", Duration.ofMinutes(15),
                LocalDateTime.now());
        Subtask task1 = new Subtask(2, "name2", Status.DONE, "description2", Duration.ofMinutes(15),
                LocalDateTime.now(), epic.getId());
        Subtask task2 = new Subtask(3, "name3", Status.NEW, "description3", Duration.ofMinutes(15),
                LocalDateTime.now(), epic.getId());
        epic.addSubtask(task1);
        epic.addSubtask(task2);
        System.out.println(epic);
        System.out.println(task1);
        System.out.println(task2);
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(task1);
        taskManager.addSubTask(task2);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpicTaskById(epic.getId()).getStatus());
    }

    @Test
    void subTaskStatusIsInProgressThenEpicStatusIsInProgress() {
        Epic epic = new Epic(1, "name1", Status.NEW, "description", Duration.ofMinutes(15),
                LocalDateTime.now());
        Subtask task1 = new Subtask(2, "name2", Status.IN_PROGRESS, "description2", Duration.ofMinutes(15),
                LocalDateTime.now(), epic.getId());
        Subtask task2 = new Subtask(3, "name3", Status.IN_PROGRESS, "description3", Duration.ofMinutes(15),
                LocalDateTime.now(), epic.getId());
        epic.addSubtask(task1);
        epic.addSubtask(task2);
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(task1);
        taskManager.addSubTask(task2);
        assertEquals(Status.IN_PROGRESS, taskManager.getEpicTaskById(1).getStatus());
    }

    @Test
    void addSubTaskToEpicTask() {
        Epic epic = new Epic(1, "name1", Status.NEW, "description", Duration.ofMinutes(15),
                LocalDateTime.now());
        Subtask subTask = new Subtask(2, "name2", Status.IN_PROGRESS, "description2", Duration.ofMinutes(15),
                LocalDateTime.now(), epic.getId());
        epic.addSubtask(subTask);
        assertEquals(epic.getId(), subTask.getEpicId());
    }

    @Test
    void addTaskIsOverLap() {
        LocalDateTime startTime = LocalDateTime.now();
        Task task1 = new Task(1, "name1", Status.NEW, "description", Duration.ofMinutes(15),
                startTime);
        Task task2 = new Task(2, "name2", Status.NEW, "description2", Duration.ofMinutes(100),
                startTime.minusMinutes(90));
        Task task3 = new Task(3, "name3", Status.NEW, "description3", Duration.ofMinutes(100),
                startTime.minusMinutes(200));
        taskManager.addTask(task1);
        taskManager.addTask(task2);
        taskManager.addTask(task3);
        assertEquals(taskManager.getPrioritizedTasks().size(), 2);

    }


}
