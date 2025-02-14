import com.yandex.taskmanager.*;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Subtask;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.service.Managers;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {


        TaskManager manager = Managers.getDefault("src/main/resources/tasks_info.csv");
        Task someTask =new Task();
        someTask.setTitle("Обычная таска");
        someTask.setDescription("Описание таски");
        someTask.setStartTime(LocalDateTime.now());
        someTask.setDuration(Duration.ofMinutes(30));
        Epic someAnotherEpicTask = new Epic();
        someAnotherEpicTask.setTitle("Эпическая задача_1");
        someAnotherEpicTask.setDescription("Это очень важная задача");
        someAnotherEpicTask.setStatus(Status.IN_PROGRESS);
        someAnotherEpicTask.setDuration(Duration.ofMinutes(15));
        someAnotherEpicTask.setStartTime(LocalDateTime.now().plusMinutes(40));
        Subtask someAnotherSubtask = new Subtask();
        someAnotherSubtask.setEpic(someAnotherEpicTask);
        someAnotherSubtask.setTitle("Сабтаска эпика 1");
        someAnotherSubtask.setDescription("Описание сабтаски");
        someAnotherSubtask.setStartTime(LocalDateTime.now());
        someAnotherSubtask.setDuration(Duration.ofMinutes(15));
        someAnotherSubtask.setStatus(Status.DONE);
        manager.addEpicTask(someAnotherEpicTask);
        manager.addSubTask(someAnotherSubtask);
        manager.addTask(someTask);
    }
}
