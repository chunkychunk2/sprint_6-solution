import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.yandex.taskmanager.Status;
import com.yandex.taskmanager.TaskManager;
import com.yandex.taskmanager.model.Epic;
import com.yandex.taskmanager.model.Subtask;
import com.yandex.taskmanager.model.Task;
import com.yandex.taskmanager.model.adapter.DurationAdapter;
import com.yandex.taskmanager.model.adapter.LocalDateTimeAdapter;
import com.yandex.taskmanager.service.HttpTaskServer;
import com.yandex.taskmanager.service.InMemoryTaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {

    public final static int PORT = 8080;
    private HttpTaskServer server;
    private TaskManager taskManager;
    private final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .create();
    private Task task;
    private Subtask subTask;
    private Epic epicTask;

    @BeforeEach
    void init() throws IOException {
        taskManager = new InMemoryTaskManager();
        server = new HttpTaskServer(taskManager);
        task = new Task(17, "name1", Status.NEW, "description", Duration.ofMinutes(15),
                LocalDateTime.now());
        taskManager.addTask(task);
        epicTask = new Epic(543, "name1", Status.NEW, "description", Duration.ofMinutes(15),
                LocalDateTime.now());
        taskManager.addEpicTask(epicTask);
        subTask = new Subtask(19, "name2", Status.NEW, "description2", Duration.ofMinutes(15),
                LocalDateTime.now(), epicTask.getId());
      //  epicTask.addSubtask(subTask);
        taskManager.addSubTask(subTask);
    }

    @BeforeEach
    void start() {
        System.out.println("Starting server on port " + PORT);
        server.start();
    }

    @AfterEach
    void stop() {
        server.stop();
        System.out.println("Server stopped");
    }

    @Test
    void whenGetAllTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uriTasks = URI.create("http://localhost:8080/tasks");

        HttpResponse<String> responseTasks = sendTaskGet(client, uriTasks);
        List<Task> actual = gson.fromJson(responseTasks.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());
        List<Task> expected = taskManager.getAllTasks();

        assertEquals(200, responseTasks.statusCode());
        assertEquals(expected.size(), 1);
        assertEquals(expected, actual);
    }

    @Test
    void whenGetAllSubTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uriTasks = URI.create("http://localhost:8080/subtasks");

        HttpResponse<String> responseTasks = sendTaskGet(client, uriTasks);
        System.out.println(responseTasks.body().toString());
        List<Subtask> actual = gson.fromJson(responseTasks.body(), new TypeToken<ArrayList<Subtask>>() {}.getType());
        List<Subtask> expected = taskManager.getAllSubTasks();

        assertEquals(200, responseTasks.statusCode());
        assertEquals(expected.size(), 1);
        assertEquals(expected, actual);
    }

    @Test
    void whenGetAllEpicTasks() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uriTasks = URI.create("http://localhost:8080/epics");

        HttpResponse<String> responseTasks = sendTaskGet(client, uriTasks);
        List<Epic> actual = gson.fromJson(responseTasks.body(), new TypeToken<ArrayList<Epic>>() {
        }.getType());
        List<Epic> expected = taskManager.getAllEpicTasks();

        assertEquals(200, responseTasks.statusCode());
        assertEquals(expected.size(), 1);
        assertEquals(expected, actual);
    }

    @Test
    void whenGetTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uriTasks = URI.create("http://localhost:8080/tasks/" + task.getId());

        HttpResponse<String> responseTasks = sendTaskGet(client, uriTasks);
        Task actual = gson.fromJson(responseTasks.body(), new TypeToken<Task>() {
        }.getType());

        assertEquals(200, responseTasks.statusCode());
        assertEquals(task, actual);
    }

    @Test
    void whenGetSubTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uriTasks = URI.create("http://localhost:8080/subtasks/" + subTask.getId());

        HttpResponse<String> responseTasks = sendTaskGet(client, uriTasks);
        Subtask actual = gson.fromJson(responseTasks.body(), new TypeToken<Subtask>() {}.getType());

        assertEquals(200, responseTasks.statusCode());
        assertEquals(subTask, actual);
    }

    @Test
    void whenGetEpicTaskById() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uriTasks = URI.create("http://localhost:8080/epics/" + epicTask.getId());

        HttpResponse<String> responseTasks = sendTaskGet(client, uriTasks);
        Epic actual = gson.fromJson(responseTasks.body(), new TypeToken<Epic>() {}.getType());

        assertEquals(200, responseTasks.statusCode());
        assertEquals(epicTask, actual);
    }

    @Test
    void whenAddNewTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uriTasks = URI.create("http://localhost:8080/tasks");
        Task task1 = new Task(1, "name1", Status.NEW, "description", Duration.ofMinutes(15),
                LocalDateTime.now());

        HttpResponse<String> responsePostTask = sendPostTask(client, uriTasks, task1);
        List<Task> actual = taskManager.getAllTasks();

        assertEquals(201, responsePostTask.statusCode());
        assertEquals(2, actual.size());
    }

    @Test
    void whenAddNewSubTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uriTasks = URI.create("http://localhost:8080/subtasks");
        Subtask task = new Subtask(876, "name2", Status.NEW, "description2", Duration.ofMinutes(15),
                LocalDateTime.now(),epicTask.getId());

        HttpResponse<String> responsePostTask = sendPostTask(client, uriTasks, task);
        List<Subtask> actual = taskManager.getAllSubTasks();

        assertEquals(201, responsePostTask.statusCode());
        assertEquals(2, actual.size());
    }

    @Test
    void whenAddNewEpicTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uriTasks = URI.create("http://localhost:8080/epics");
        Epic epic = new Epic(1, "name1", Status.NEW, "description", Duration.ofMinutes(15),
                LocalDateTime.now());
epic.setEndTime(LocalDateTime.now());
        HttpResponse<String> responsePostTask = sendPostTask(client, uriTasks, epic);
        List<Epic> actual = taskManager.getAllEpicTasks();

        assertEquals(201, responsePostTask.statusCode());
        assertEquals(2, actual.size());
    }

    @Test
    void whenDeleteTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uriTasks = URI.create("http://localhost:8080/tasks/" + task.getId());

        HttpResponse<String> responsePostTask = sendDeleteTask(client, uriTasks);
        List<Task> expected = taskManager.getAllTasks();

        assertEquals(204, responsePostTask.statusCode());
        assertEquals(expected.size(), 0);
    }

    @Test
    void whenDeleteSubTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uriTasks = URI.create("http://localhost:8080/subtasks/" + subTask.getId());

        HttpResponse<String> responsePostTask = sendDeleteTask(client, uriTasks);
        List<Subtask> expected = taskManager.getAllSubTasks();

        assertEquals(204, responsePostTask.statusCode());
        assertEquals(expected.size(), 0);
    }

    @Test
    void whenDeleteEpicTask() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        Epic epic = new Epic(23, "name1", Status.NEW, "description", Duration.ofMinutes(15),
                LocalDateTime.now());
        Subtask subtask = new Subtask(19, "name2", Status.NEW, "description2", Duration.ofMinutes(15),
                LocalDateTime.now(), epic.getId());
        epic.addSubtask(subtask);
        taskManager.addEpicTask(epic);
        taskManager.addSubTask(subtask);
        URI uriTasks = URI.create("http://localhost:8080/epics/" + epic.getId());

        HttpResponse<String> responsePostTask = sendDeleteTask(client, uriTasks);
        List<Epic> expected = taskManager.getAllEpicTasks();

        assertEquals(204, responsePostTask.statusCode());
        assertEquals(expected.size(), 1);
    }

    @Test
    void whenGetHistory() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uriTasks = URI.create("http://localhost:8080/history/");
        taskManager.getTaskById(task.getId());
        taskManager.getSubTaskById(subTask.getId());

        HttpResponse<String> responseTasks = sendTaskGet(client, uriTasks);
        List<Task> actual = gson.fromJson(responseTasks.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertEquals(200, responseTasks.statusCode());
        assertEquals(2, actual.size());
    }

    @Test
    void whenGetPrioritized() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI uriTasks = URI.create("http://localhost:8080/prioritized/");

        HttpResponse<String> responseTasks = sendTaskGet(client, uriTasks);
        List<Task> actual = gson.fromJson(responseTasks.body(), new TypeToken<ArrayList<Task>>() {
        }.getType());

        assertEquals(200, responseTasks.statusCode());
        assertEquals(1, actual.size());
    }

    private HttpResponse<String> sendTaskGet(HttpClient client, URI uriTasks) throws IOException, InterruptedException {
        HttpRequest requestTasks = HttpRequest.newBuilder().uri(uriTasks).GET().build();
        return client.send(requestTasks, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> sendPostTask(HttpClient client, URI uriTasks, Task taskToPost) throws IOException,
            InterruptedException {
        String taskToPostGson = gson.toJson(taskToPost);
        HttpRequest postTask = HttpRequest.newBuilder()
                .uri(uriTasks)
                .header("Content-Type", "application/json")
                .POST(HttpRequest.BodyPublishers.ofString(taskToPostGson))
                .build();
        return client.send(postTask, HttpResponse.BodyHandlers.ofString());
    }

    private HttpResponse<String> sendDeleteTask(HttpClient client, URI uriTasks) throws IOException,
            InterruptedException {
        HttpRequest deleteTask = HttpRequest.newBuilder()
                .uri(uriTasks)
                .DELETE()
                .build();
        return client.send(deleteTask, HttpResponse.BodyHandlers.ofString());
    }

}