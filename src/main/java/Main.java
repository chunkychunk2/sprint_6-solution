import com.yandex.taskmanager.*;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Subtask;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.service.FileBackedTaskManager;
import com.yandex.taskmanager.service.Managers;

import java.io.File;

public class Main {

    public static void main(String[] args) {

       // TaskManager manager = Managers.getDefault();

//        Task task = new Task();
//        manager.addTask(task);
//        Task task2 = new Task();
//        manager.addTask(task2);
//        Epic epic = new Epic();
//        manager.addEpicTask(epic);
//        Subtask subtask = new Subtask();
//        Subtask subtask2 = new Subtask();
//        manager.addSubTask(subtask);
//        manager.addSubTask(subtask2);
//        epic.addSubtask(subtask);
//        epic.addSubtask(subtask2);
//
//        Epic epic2 = new Epic();
//        manager.addEpicTask(epic2);
//        Subtask subtask3 = new Subtask();
//        manager.addSubTask(subtask3);
//        epic2.addSubtask(subtask3);
//
//
//        System.out.println(manager.getAllEpicTasks());
//        System.out.println(manager.getAllTasks());
//        System.out.println(manager.getAllSubTasks());
//
//        manager.getTaskInfo(epic);
//        subtask.changeStatus(Status.DONE);
//        manager.getTaskInfo(epic);
//        manager.getTaskInfo(subtask2);
//        subtask2.changeStatus(Status.DONE);
//        manager.getTaskInfo(epic);
//        manager.getTaskInfo(subtask2);
//
//        System.out.println(manager.getAllTasks());
//
//        System.out.println(manager.getAllTasks());
//
//        System.out.println(manager.getAllEpicTasks());
//
//        System.out.println(manager.getAllEpicTasks());
//        System.out.println(epic.getStatus());
//        System.out.println(epic2.getStatus());
//        System.out.println(manager.getAllSubTasks());
//
//        System.out.println(epic.getStatus());
//        System.out.println(epic2.getStatus());
//        System.out.println(manager.getAllSubTasks());
//
//        Epic epic3 = new Epic();
//        manager.addEpicTask(epic3);
//        Subtask subtask4 = new Subtask();
//        manager.addSubTask(subtask4);
//        epic3.addSubtask(subtask4);
//        System.out.println(epic3.getStatus());
//        subtask4.changeStatus(Status.DONE);
//
//        System.out.println(epic3.getStatus());
//
//        System.out.println(epic3.getStatus());
//
//        Epic epic4 = new Epic();
//        Subtask subtask5 = new Subtask();
//        epic4.addSubtask(subtask5);
//        manager.addSubTask(subtask5);
//        manager.addEpicTask(epic4);
//
//        System.out.println(epic.getId());
//        System.out.println(epic2.getId());
//
//        Epic epic5 = new Epic();
//        Subtask subtask6 = new Subtask();
//        epic5.addSubtask(subtask6);
//        manager.addSubTask(subtask6);
//        manager.addEpicTask(epic5);
//
//
//        manager.getTaskById(task.getId());
//        manager.getTaskById(task2.getId());
//        manager.getSubTaskById(subtask3.getId());
//        manager.getSubTaskById(subtask2.getId());
//        manager.getSubTaskById(subtask.getId());
//        manager.getEpicTaskById(epic.getId());
//        manager.getEpicTaskById(epic2.getId());
//        manager.getEpicTaskById(epic3.getId());
//        manager.getSubTaskById(subtask3.getId());
//        manager.getSubTaskById(subtask2.getId());
//        manager.getSubTaskById(subtask.getId());
//
//        printAllTasks(manager);
//        HistoryManager historyManager = Managers.getDefaultHistory();
//        historyManager.add(task);
//        System.out.println(historyManager.getHistory());
//
//
//        Epic epic1 = new Epic();
//        System.out.println(epic1.getId());
//       Subtask subtask1 = new Subtask();
//        manager.addEpicTask(epic1);
//        manager.addSubTask(subtask1);
//        epic1.addSubtask(subtask1);
//        System.out.println(epic1.getId());
//        System.out.println(epic1.getId());
//        System.out.println(manager.getEpicTaskById(epic1.getId()).getSubtasks());
//        manager.deleteSubTask(subtask1.getId());
//        System.out.println(manager.getEpicTaskById(epic1.getId()).getSubtasks());
//
//
//        Epic epic6 = new Epic();
//        Subtask subtask7 = new Subtask();
//        Subtask subtask8 = new Subtask();
//        Subtask subtask9 = new Subtask();
//        epic6.addSubtask(subtask7);
//        epic6.addSubtask(subtask8);
//        epic6.addSubtask(subtask9);
//        manager.addEpicTask(epic6);
//        manager.addSubTask(subtask7);
//        manager.addSubTask(subtask8);
//        manager.addSubTask(subtask9);
//        manager.getEpicTaskById(epic6.getId());
//        manager.getSubTaskById(subtask7.getId());
//        manager.getSubTaskById(subtask8.getId());
//        manager.getSubTaskById(subtask9.getId());
//        System.out.println(manager.getHistory());
//        manager.deleteEpicTask(epic6.getId());
//        System.out.println(manager.getHistory());
//
//        Task task12 = new Task();
//        manager.addTask(task12);
//        manager.getTaskById(task.getId());
//        System.out.println(manager.getHistory());
//        manager.deleteTask(task12.getId());
//        System.out.println(manager.getHistory());

        TaskManager manager = Managers.getDefaultFileBacked("src/main/resources/tasks_info.csv");
        Task someTask =new Task();
        someTask.createTitle("Обычная таска");
        someTask.setDescription("Описание таски");
        Epic someAnotherEpicTask = new Epic();
        someAnotherEpicTask.createTitle("Эпическая задача_1");
        someAnotherEpicTask.setDescription("Это очень важная задача");
        someAnotherEpicTask.setStatus(Status.IN_PROGRESS);
        System.out.println(someAnotherEpicTask);
        Subtask someAnotherSubtask = new Subtask();
        someAnotherSubtask.setEpic(someAnotherEpicTask);
        someAnotherSubtask.createTitle("Сабтаска эпика 1");
        someAnotherSubtask.setDescription("Описание сабтаски");
        someAnotherSubtask.setStatus(Status.DONE);
        manager.addEpicTask(someAnotherEpicTask);
        manager.addSubTask(someAnotherSubtask);
        manager.addTask(someTask);
        System.out.println(someAnotherSubtask);
        System.out.println(someAnotherEpicTask);

//        File file = new File("src/main/resources/tasks_info.csv");
//        TaskManager taskManager = FileBackedTaskManager.loadFromFile(file);
//        System.out.println(taskManager.getAllTasks());
//        System.out.println(taskManager.getAllEpicTasks());
//        System.out.println(taskManager.getAllSubTasks());
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
