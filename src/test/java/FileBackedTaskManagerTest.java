import com.yandex.taskmanager.Status;
import com.yandex.taskmanager.TaskManager;
import com.yandex.taskmanager.exceptions.ManagerLoadException;
import com.yandex.taskmanager.exceptions.ManagerSaveException;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Subtask;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.service.FileBackedTaskManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {

    @BeforeEach
    void setUp() throws IOException {
        File tempFile = File.createTempFile("temp", "csv");
        taskManager = new FileBackedTaskManager(tempFile.getPath());
    }

    @Test
    void whenLoadEmptyFileThenEmptyTasks() throws Exception {
        File tempFile = File.createTempFile("temp", "csv");
        TaskManager fileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        List<Task> tasks = fileBackedTaskManager.getAllTasks();
        List<Subtask> subTasks = fileBackedTaskManager.getAllSubTasks();
        List<Epic> epicTasks = fileBackedTaskManager.getAllEpicTasks();
        assertEquals(0, tasks.size());
        assertEquals(0, subTasks.size());
        assertEquals(0, epicTasks.size());
    }

    @Test
    void whenSaveAndLoadTasksThenNotEmptyTasks() throws Exception {
        File tempFile = File.createTempFile("temp", ".csv");
        TaskManager fileBackedTaskManager = new FileBackedTaskManager(tempFile.getPath());
        fileBackedTaskManager.addTask(new Task(1, "name1", Status.NEW, "description", Duration.ofMinutes(15),
                LocalDateTime.now()));
        fileBackedTaskManager.addTask(new Task(2, "name2", Status.NEW, "description2", Duration.ofMinutes(15),
                LocalDateTime.now()));
        fileBackedTaskManager.addTask(new Task(3, "name3", Status.NEW, "description3", Duration.ofMinutes(15),
                LocalDateTime.now()));
        fileBackedTaskManager.addTask(new Task(4, "name4", Status.NEW, "description4", Duration.ofMinutes(15),
                LocalDateTime.now()));
        Subtask task1 = new Subtask(7, "name7", Status.NEW, "description7", Duration.ofMinutes(15),
                LocalDateTime.now());
        Subtask task2 = new Subtask(8, "name8", Status.NEW, "description8", Duration.ofMinutes(15),
                LocalDateTime.now());
        Subtask task3 = new Subtask(9, "name9", Status.NEW, "description8", Duration.ofMinutes(15),
                LocalDateTime.now());
        Epic epic1 = new Epic(55, "name1", Status.NEW, "description", Duration.ofMinutes(15),
                LocalDateTime.now());
        epic1.addSubtask(task1);
        fileBackedTaskManager.addEpicTask(epic1);
        Epic epic2 = new Epic(66, "name2", Status.NEW, "description2", Duration.ofMinutes(15),
                LocalDateTime.now());
        epic2.addSubtask(task2);
        epic2.addSubtask(task3);
        fileBackedTaskManager.addEpicTask(epic2);
        fileBackedTaskManager.addSubTask(task1);
        fileBackedTaskManager.addSubTask(task2);
        fileBackedTaskManager.addSubTask(task3);
        TaskManager newFileBackedTaskManager = FileBackedTaskManager.loadFromFile(tempFile);
        List<Task> tasks = newFileBackedTaskManager.getAllTasks();
        List<Subtask> subTasks = newFileBackedTaskManager.getAllSubTasks();
        List<Epic> epicTasks = newFileBackedTaskManager.getAllEpicTasks();

        assertEquals(4, tasks.size());
        assertEquals(3, subTasks.size());
        assertEquals(2, epicTasks.size());
    }

    @Test
    void whenLoadTasksAndFileNotExists() throws ManagerLoadException{
        assertThrows(ManagerLoadException.class, () -> {
            FileBackedTaskManager.loadFromFile(new File("Path/notExistPath"));
        }, "Не удалось загрузить данные");
    }

    @Test
    void whenSaveTasksAndFileNotExists() {
        FileBackedTaskManager fileBackedTaskManager = new FileBackedTaskManager("test/test");
        assertThrows(ManagerSaveException.class, () -> {
            fileBackedTaskManager.addTask(new Task(1, "name1", Status.NEW, "description", Duration.ofMinutes(15),
                    LocalDateTime.now()));
        }, "Не удалось сохранить данные");
    }
}