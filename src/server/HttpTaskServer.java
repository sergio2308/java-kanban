package server;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpServer;
import task.*;
import manager.*;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class HttpTaskServer {
    public static final int PORT = 8080;
    private final String FILE = "/tasks";
    public static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final String[] RESOURCES = {"tasks", "task", "subtask", "epic", "history"};
    private HttpServer httpServer;
    private Gson gson;
    private FileBackedTasksManager fileBackedTasksManager;

    public HttpTaskServer(File file) throws IOException {
        fileBackedTasksManager = FileBackedTasksManager.loadFromFile(file);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress(PORT), 0);
        httpServer.createContext(String.valueOf(file), this::handleTask);
    }

    private void handleTask(HttpExchange httpExchange)  throws IOException {
        try {
            System.out.println("Началась обработка запроса от клиента: " + httpExchange.getRequestURI());
            String response = "";
            int rCode = 400;
            int lengthResponse = 0;
            String method = httpExchange.getRequestMethod();
            String body = new String(httpExchange.getRequestBody().readAllBytes(), DEFAULT_CHARSET);
            String resource = getResource(httpExchange.getRequestURI().getPath());
            int id = getId(httpExchange.getRequestURI().getQuery());
            String endPoint = method + FILE + (id > 0 ? "id" : id == 0 ? "" : "falseid");
            if ("POST".equals(method) && body.isBlank()) {
                endPoint += "falsebody";
            }
            Task task;
            SubTask subTask;
            Epic epic;
            switch(endPoint) {
                case "GETtasks":
                    List<Task> tasks = new ArrayList<>(fileBackedTasksManager.getTasks());
                    tasks.addAll(fileBackedTasksManager.getSubTasks());
                    tasks.addAll(fileBackedTasksManager.getEpics());
                    response = gson.toJson(tasks);
                    rCode = 200;
                    break;
                case "GETtask":
                    response = gson.toJson(fileBackedTasksManager.getTasks());
                    rCode = 200;
                    break;
                case "GETtaskid":
                    task = fileBackedTasksManager.getTaskById(id);
                    if (task != null) {
                        response = gson.toJson(task);
                        rCode = 200;
                    } else {
                        rCode = 404;
                    }
                    break;
                case "GETsubtask":
                    response = gson.toJson(fileBackedTasksManager.getSubTasks());
                    rCode = 200;
                    break;
                case "GETsubtaskid":
                    subTask = fileBackedTasksManager.getSubTaskById(id);
                    if (subTask != null) {
                        response = gson.toJson(subTask);
                        rCode = 200;
                    } else {
                        rCode = 404;
                    }
                    break;
                case "GETepic":
                    response = gson.toJson(fileBackedTasksManager.getEpics());
                    rCode = 200;
                    break;
                case "GETepicid":
                    epic = fileBackedTasksManager.getEpicById(id);
                    if (epic != null) {
                        response = gson.toJson(epic);
                        rCode = 200;
                    } else {
                        rCode = 404;
                    }
                    break;
                case "GEThistory":
                    response = gson.toJson(Managers.getDefaultHistory().getHistory());
                    rCode = 200;
                    break;
                case "POSTtask":
                    task = gson.fromJson(body, Task.class);
                    if (task.getId() == 0) {
                        fileBackedTasksManager.addTask(task);
                    } else {
                        fileBackedTasksManager.updateTask(task);
                    }
                    rCode = 201;
                    break;
                case "POSTsubtask":
                    subTask = gson.fromJson(body, SubTask.class);
                    fileBackedTasksManager.updateSubTask(subTask);
                    rCode = 201;
                    break;
                case "POSTsubtaskid":
                    subTask = gson.fromJson(body, SubTask.class);
                    fileBackedTasksManager.addSubTask(subTask);
                    rCode = 201;
                    break;
                case "POSTepic":
                    epic = gson.fromJson(body, Epic.class);
                    if (epic.getId() == 0) {
                        fileBackedTasksManager.addEpic(epic);
                    } else {
                        fileBackedTasksManager.updateEpicStatus(epic);
                    }
                    rCode = 201;
                    break;
                case "DELETEtask":
                    fileBackedTasksManager.removeTasks();
                    rCode = 204;
                    lengthResponse = -1;
                    break;
                case "DELETEtaskid":
                    fileBackedTasksManager.removeTaskById(id);
                    rCode = 204;
                    lengthResponse = -1;
                    break;
                case "DELETEsubtask":
                    fileBackedTasksManager.removeSubTasks();
                    rCode = 204;
                    lengthResponse = -1;
                    break;
                case "DELETEsubtaskid":
                    fileBackedTasksManager.removeSubTasks();
                    rCode = 204;
                    lengthResponse = -1;
                    break;
                case "DELETEepic":
                    fileBackedTasksManager.removeEpics();
                    rCode = 204;
                    lengthResponse = -1;
                    break;
                case "DELETEepicid":
                    fileBackedTasksManager.removeEpicById(id);
                    rCode = 204;
                    lengthResponse = -1;
                    break;
                default:
                    System.out.println("Сервер не распознал запрос: " + method + ":" + httpExchange.getRequestURI());
                    rCode = 400;
            }
            httpExchange.getResponseHeaders().add("Content-Type", "application/json");
            httpExchange.sendResponseHeaders(rCode, lengthResponse);
            if (lengthResponse != -1) {
                try (OutputStream os = httpExchange.getResponseBody()) {
                    os.write(response.getBytes(DEFAULT_CHARSET));
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        } finally {
            httpExchange.close();
        }
    }

    public void stop() {
        httpServer.stop(0);
        System.out.println("Остановка сервера на порту " + PORT);
    }

    public void start() {
        System.out.println("Запускаем сервер на порту " + PORT);
        System.out.println("Ссылка: http://localhost:" + PORT + FILE);
        httpServer.start();
    }

    private String getResource(String path) {
        String[] linePath = path.split("/");
        String resource = FILE.substring(1);
        if (linePath.length > 2) {
            resource = linePath[2];
        }
        for (String s : RESOURCES) {
            if (s.equals(resource)) {
                return s;
            }
        }
        return "";
    }

    private int getId(String idLine) {
        int id = 0;
        if (idLine != null) {
            try {
                String[] idLineSplit = idLine.split("=");
                if (idLineSplit.length > 1) {
                    id = Integer.parseInt(idLineSplit[1]);
                    if (id <= 0) {
                        id = -1;
                    }
                } else {
                    id = -1;
                }
            } catch (NumberFormatException e) {
                e.printStackTrace();
                return -1;
            }
        }
        return id;
    }
}