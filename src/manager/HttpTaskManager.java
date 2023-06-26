package manager;

import client.KVTaskClient;
import com.google.gson.*;
import server.*;
import task.*;

import java.io.File;
import java.net.URL;
import java.time.LocalDateTime;

public class HttpTaskManager extends FileBackedTasksManager {

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
        client.put(String.valueOf(Key.KEY_TASKS), json);
        json = gson.toJson(getSubTasks());
        client.put(String.valueOf(Key.KEY_SUBTASKS), json);
        json = gson.toJson(getEpics());
        client.put(String.valueOf(Key.KEY_EPICS), json);
        json = gson.toJson(Managers.getDefaultHistory().getHistory());
        client.put(String.valueOf(Key.KEY_HISTORY), json);
    }

    public void load() {
        HistoryManager history = Managers.getDefaultHistory();
        removeTasks();
        removeSubTasks();
        removeEpics();
        JsonArray jsonArray = parserJson(String.valueOf(Key.KEY_TASKS));
        if (jsonArray != null) {
            for (JsonElement element : jsonArray) {
                updateTask(gson.fromJson(element, Task.class));
            }
        }
        jsonArray = parserJson(String.valueOf(Key.KEY_SUBTASKS));
        if (jsonArray != null) {
            for (JsonElement element : jsonArray) {
                updateSubTask(gson.fromJson(element, SubTask.class));
            }
        }
        jsonArray = parserJson(String.valueOf(Key.KEY_EPICS));
        if (jsonArray != null) {
            for (JsonElement element : jsonArray) {
                updateEpicStatus(gson.fromJson(element, Epic.class));
            }
        }
        jsonArray = parserJson(String.valueOf(Key.KEY_HISTORY));
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

