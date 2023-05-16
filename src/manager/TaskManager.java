package manager;
import task.*;

import java.util.List;

public interface TaskManager {

    void removeTasks();

    void removeEpics(int id);

    void removeSubTasks(int id);

    List<Task> getHistory();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    SubTask getSubTaskById(int id);

    void addTask(Task task);

    void addSubTask(SubTask subTask);

    void addEpic(Epic epic);

    void updateTask(Task task);

    void updateSubTask(SubTask subTask);

    void removeTaskById(int id);

    void removeEpicById(int id);

    void removeSubTaskById(int id);

    void getEpicSubs(int id);

    List<Task> getTasks();
}