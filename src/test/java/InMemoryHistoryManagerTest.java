import com.yandex.taskmanager.HistoryManager;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.service.InMemoryHistoryManager;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class InMemoryHistoryManagerTest {

    @Test
    void whenEmptyHistory() {
        HistoryManager historyManager = new InMemoryHistoryManager();
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void whenAddTask() {
        Task task = new Task();
        task.createTitle("Задача-1");
        HistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertEquals(task, history.get(0));
    }

    @Test
    void whenAddCopyTask() {
        Task task = new Task();
        task.createTitle("Задача-1");
        HistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(task);
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
    }

    @Test
    void whenAddUpdatedTask() {
        Task task = new Task();
        HistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(task);
        task.createTitle("Задача-новая");
        historyManager.add(task);
        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task, history.get(0));
        assertEquals(task.getTitle(), history.get(0).getTitle());
    }

    @Test
    void whenRemove() {
        Task task1 = new Task();
        Task task2 = new Task();
        Task task3 = new Task();
        task1.createTitle("Задача-1");
        task2.createTitle("Задача-2");
        task3.createTitle("Задача-3");
        HistoryManager historyManager = new InMemoryHistoryManager();
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.remove(task2.getId());
        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(2, historyManager.getHistory().size());
        assertEquals(task1, history.get(0));
        assertEquals(task3, history.get(1));
    }


}