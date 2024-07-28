package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class TaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public TaskHandler(TaskManager taskManager, Gson gson) {
        this.taskManager = taskManager;
        this.gson = gson;
    }

    @Override
    public void handle(HttpExchange httpExchange) throws IOException {
        String method = httpExchange.getRequestMethod();
        String path = httpExchange.getRequestURI().getPath();
        String query = httpExchange.getRequestURI().getQuery();

        switch (method) {
            case "GET":
                if (path.endsWith("/tasks")) {
                    List<Task> tasks = taskManager.getTasks();
                    sendText(httpExchange, gson.toJson(tasks), 200);
                } else if (query != null && query.startsWith("id=")) {
                    int id = Integer.parseInt(query.substring(3));
                    Task task = taskManager.getTaskById(id);
                    if (task != null) {
                        sendText(httpExchange, gson.toJson(task), 200);
                    } else {
                        sendNotFound(httpExchange);
                    }
                }
                break;
            case "POST":
                String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Task task = gson.fromJson(body, Task.class);
                if (task.getId() == 0) {
                    taskManager.createNewTask(task);
                } else {
                    taskManager.updateTask(task);
                }
                sendText(httpExchange, gson.toJson(task), 201);
                break;
            case "DELETE":
                if (query != null && query.startsWith("id=")) {
                    int id = Integer.parseInt(query.substring(3));
                    taskManager.removeTaskById(id);
                    sendText(httpExchange, "{}", 200);
                }
                break;
            default:
                sendNotFound(httpExchange);
        }
    }
}
