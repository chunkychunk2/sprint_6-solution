import com.yandex.taskmanager.*;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Subtask;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.service.FileBackedTaskManager;
import com.yandex.taskmanager.service.Managers;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {


        TaskManager manager = Managers.getDefault("src/main/resources/tasks_info.csv");
        Task someTask =new Task();
        someTask.createTitle("Обычная таска");
        someTask.setDescription("Описание таски");
        someTask.setStartTime(LocalDateTime.now());
        someTask.setDuration(Duration.ofMinutes(30));
        Epic someAnotherEpicTask = new Epic();
        someAnotherEpicTask.createTitle("Эпическая задача_1");
        someAnotherEpicTask.setDescription("Это очень важная задача");
        someAnotherEpicTask.setStatus(Status.IN_PROGRESS);
        someAnotherEpicTask.setDuration(Duration.ofMinutes(15));
        someAnotherEpicTask.setStartTime(LocalDateTime.now().plusMinutes(40));
        System.out.println(someAnotherEpicTask);
        Subtask someAnotherSubtask = new Subtask();
        someAnotherSubtask.setEpic(someAnotherEpicTask);
        someAnotherSubtask.createTitle("Сабтаска эпика 1");
        someAnotherSubtask.setDescription("Описание сабтаски");
        someAnotherSubtask.setStartTime(LocalDateTime.now());
        someAnotherSubtask.setDuration(Duration.ofMinutes(15));
        someAnotherSubtask.setStatus(Status.DONE);
        manager.addEpicTask(someAnotherEpicTask);
        manager.addSubTask(someAnotherSubtask);
        manager.addTask(someTask);
        System.out.println(someAnotherSubtask);
        System.out.println(someAnotherEpicTask);
    }


    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        System.out.println(manager.getAllTasks());
        System.out.println("Эпики:");
        System.out.println(manager.getAllEpicTasks());
        System.out.println("Подзадачи:");
        System.out.println(manager.getAllSubTasks());

        System.out.println("История:");
        System.out.println(manager.getHistory());
    }
}
