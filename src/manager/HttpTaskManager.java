package manager;

import client.KVTaskClient;
import com.google.gson.*;
import server.*;
import task.*;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;

public class HttpTaskManager extends FileBackedTasksManager {

    public static final String KEY_TASKS = "tasks";
    public static final String KEY_SUBTASKS = "subtasks";
    public static final String KEY_EPICS = "epics";
    public static final String KEY_HISTORY = "history";

    private KVTaskClient client;
    private Gson gson;
    private String url = "http://localhost:";

    public static final String PATH_SERVER = "http://localhost:";
    public HttpTaskManager(String url) {
        super(Managers.PATH_FILE);
        this.client = new KVTaskClient(url);
        gson = new GsonBuilder()
                .setPrettyPrinting()
                .registerTypeAdapter(LocalDateTime.class, new TypeAdapterForLocalDateTime())
                .create();
    }

    public void save() {
        save();
        String json = gson.toJson(getTasks());
        client.put(KEY_TASKS, json);
        json = gson.toJson(getSubTasks());
        client.put(KEY_SUBTASKS, json);
        json = gson.toJson(getEpics());
        client.put(KEY_EPICS, json);
        json = gson.toJson(Managers.getDefaultHistory().getHistory());
        client.put(KEY_HISTORY, json);
    }

    public void load() {
        HistoryManager history = Managers.getDefaultHistory();
        removeTasks();
        removeSubTasks();
        removeEpics();
        JsonArray jsonArray = parserJson(KEY_TASKS);
        if (jsonArray != null) {
            for (JsonElement element : jsonArray) {
                updateTask(gson.fromJson(element, Task.class));
            }
        }
        jsonArray = parserJson(KEY_SUBTASKS);
        if (jsonArray != null) {
            for (JsonElement element : jsonArray) {
                updateSubTask(gson.fromJson(element, SubTask.class));
            }
        }
        jsonArray = parserJson(KEY_EPICS);
        if (jsonArray != null) {
            for (JsonElement element : jsonArray) {
                updateEpicStatus(gson.fromJson(element, Epic.class));
            }
        }
        jsonArray = parserJson(KEY_HISTORY);
        if (jsonArray != null) {
            for (JsonElement element : jsonArray) {
                history.add(gson.fromJson(element, Task.class));
            }
        }
    }

    private JsonArray parserJson(String key) {
        String json = client.load(key);
        JsonElement jsonElement = JsonParser.parseString(json);
        if (jsonElement.isJsonArray()) {
            JsonArray jsonArray = jsonElement.getAsJsonArray();
            return jsonArray;
        }
        System.out.println("Список не получен");
        return null;
    }
}

