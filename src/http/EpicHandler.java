package http;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import manager.TaskManager;
import tasks.Epic;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

public class EpicHandler extends BaseHttpHandler {
    private final TaskManager taskManager;
    private final Gson gson;

    public EpicHandler(TaskManager taskManager, Gson gson) {
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
                if (path.endsWith("/epics")) {
                    List<Epic> epics = taskManager.getEpics();
                    sendText(httpExchange, gson.toJson(epics), 200);
                } else if (query != null && query.startsWith("id=")) {
                    int id = Integer.parseInt(query.substring(3));
                    Epic epic = taskManager.getEpicById(id);
                    if (epic != null) {
                        sendText(httpExchange, gson.toJson(epic), 200);
                    } else {
                        sendNotFound(httpExchange);
                    }
                }
                break;
            case "POST":
                String body = new String(httpExchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
                Epic epic = gson.fromJson(body, Epic.class);
                taskManager.createNewEpic(epic);
                sendText(httpExchange, gson.toJson(epic), 201);
                break;
            case "DELETE":
                if (query != null && query.startsWith("id=")) {
                    int id = Integer.parseInt(query.substring(3));
                    taskManager.removeEpicById(id);
                    sendText(httpExchange, "{}", 200);
                }
                break;
            default:
                sendNotFound(httpExchange);
        }
    }
}