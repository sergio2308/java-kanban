package manager;
import task.*;

import java.util.HashMap;
import java.util.Map;

public interface TaskManager {

    void removeTasks();

    void removeEpics(int id);

    void removeSubTasks(int id);

    Task getTaskById(int id);

    HistoryManager getHistoryManager();

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    Task addTask(Task task);

    SubTask addSubTask(SubTask subTask);

    Epic addEpic(Epic epic);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void updateEpicStatus(Epic epic);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubTaskById(int id);

    void getEpicSubs(int id);

    Map<Integer, Task> getTasks();

    Map<Integer, Epic> getEpics();

    Map<Integer, SubTask> getSubTasks();
}