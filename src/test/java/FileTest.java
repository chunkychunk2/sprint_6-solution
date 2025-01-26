import com.yandex.taskmanager.Status;
import com.yandex.taskmanager.TaskManager;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Subtask;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.service.FileBackedTaskManager;
import com.yandex.taskmanager.service.Managers;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileTest {

    @Test
    void loadFromFileTest() throws Exception {
        File tempFile = File.createTempFile("testFile", ".txt");
        try (FileWriter writer = new FileWriter(tempFile)) {
            writer.write("id,type,name,status,description,epicId\n" +
                    "1, Task, Обычная таска, NEW, Описание таски\n" +
                    "2, Epic, Эпическая задача_1, IN_PROGRESS, Это очень важная задача\n" +
                    "3, Subtask, Сабтаска эпика 1, DONE, Описание сабтаски, 2\n");
        }
        TaskManager taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        Task task = new Task();
        task.setId(1);
        task.createTitle("Обычная таска");
        task.setStatus(Status.NEW);
        task.setDescription("Описание таски");
        assertEquals(task, taskManager.getAllTasks().get(0));
        Epic epic = new Epic();
        epic.setId(2);
        epic.createTitle("Эпическая задача_1");
        epic.setStatus(Status.IN_PROGRESS);
        epic.setDescription("Это очень важная задача");
        assertEquals(epic, taskManager.getAllEpicTasks().get(0));
        Subtask subtask = new Subtask();
        subtask.setId(3);
        subtask.createTitle("Сабтаска эпика 1");
        subtask.setStatus(Status.DONE);
        subtask.setDescription("Описание сабтаски");
        subtask.setEpicId(2);
        assertEquals(subtask, taskManager.getAllSubTasks().get(0));

        if (tempFile.exists()) {
            Files.delete(tempFile.toPath());
        }
    }

    @Test
    public void saveTasksToFileTest() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        String content = Files.readString(tempFile.toPath());
        assertEquals("", content);
        TaskManager manager = Managers.getDefault(tempFile.toPath().toString());
        Task someTask = new Task();
        someTask.createTitle("Обычная таска");
        someTask.setDescription("Описание таски");
        Epic someAnotherEpicTask = new Epic();
        someAnotherEpicTask.createTitle("Эпическая задача_1");
        someAnotherEpicTask.setDescription("Это очень важная задача");
        someAnotherEpicTask.setStatus(Status.IN_PROGRESS);
        Subtask someAnotherSubtask = new Subtask();
        someAnotherSubtask.setEpic(someAnotherEpicTask);
        someAnotherSubtask.createTitle("Сабтаска эпика 1");
        someAnotherSubtask.setDescription("Описание сабтаски");
        someAnotherSubtask.setStatus(Status.DONE);
        manager.addEpicTask(someAnotherEpicTask);
        manager.addSubTask(someAnotherSubtask);
        manager.addTask(someTask);

        content = Files.readString(tempFile.toPath());
        assertEquals("id,type,name,status,description,epicId" +
                "1, Task, Обычная таска, NEW, Описание таски" +
                "2, Epic, Эпическая задача_1, IN_PROGRESS, Это очень важная задача" +
                "3, Subtask, Сабтаска эпика 1, DONE, Описание сабтаски, 2", content.replaceAll("\n", "").replaceAll("\r", ""));
        if (tempFile.exists()) {
            Files.delete(tempFile.toPath());
        }
    }

    @Test
    public void saveToEmptyFileTest() throws IOException {
        File tempFile = File.createTempFile("testFile", ".txt");
        String content = Files.readString(tempFile.toPath());
        assertEquals("", content);
        TaskManager manager = Managers.getDefault(tempFile.toPath().toString());
        manager.deleteAllTasks();
        manager.deleteAllEpicTasks();
        manager.deleteAllSubTasks();
        content = Files.readString(tempFile.toPath());
        assertEquals("id,type,name,status,description,epicId", content.replaceAll("\n", "").replaceAll("\r", ""));
        if (tempFile.exists()) {
            Files.delete(tempFile.toPath());
        }
    }

    @Test
    void loadFromEmptyFileTest() throws Exception {
        File tempFile = File.createTempFile("testFile", ".txt");
        TaskManager taskManager = FileBackedTaskManager.loadFromFile(tempFile);
        ArrayList<Task> emptyTasks = new ArrayList<>();
        assertEquals(emptyTasks, taskManager.getAllTasks());
        ArrayList<Epic> emptyEpics = new ArrayList<>();
        assertEquals(emptyEpics, taskManager.getAllEpicTasks());
        ArrayList<Subtask> emptySubtasks = new ArrayList<>();
        assertEquals(emptySubtasks, taskManager.getAllSubTasks());

        if (tempFile.exists()) {
            Files.delete(tempFile.toPath());
        }
    }
}
