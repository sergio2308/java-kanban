package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class SubtaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public SubtaskHandler(TaskManager taskManager, Gson gson) {
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
                if (path.endsWith("/subtasks")) {
                    List<SubTask> subtasks = taskManager.getSubTasks();
                    sendText(httpExchange, gson.toJson(subtasks), 200);
                } else if (query != null && query.startsWith("id=")) {
                    int id = Integer.parseInt(query.substring(3));
                    SubTask subtask = taskManager.getSubTaskById(id);
                    if (subtask != null) {
                        sendText(httpExchange, gson.toJson(subtask), 200);
                    } else {
                        sendNotFound(httpExchange);
                    }
                }
                break;
            case "POST":
                String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                SubTask subtask = gson.fromJson(body, SubTask.class);
                if (subtask.getId() == 0) {
                    taskManager.createNewSubTask(subtask, subtask.getEpicId());
                } else {
                    taskManager.updateSubTask(subtask, subtask.getEpicId());
                }
                sendText(httpExchange, gson.toJson(subtask), 201);
                break;
            case "DELETE":
                if (query != null && query.startsWith("id=")) {
                    int id = Integer.parseInt(query.substring(3));
                    taskManager.removeSubTaskById(id);
                    sendText(httpExchange, "{}", 200);
                }
                break;
            default:
                sendNotFound(httpExchange);
        }
    }
}

