package manager;

import task.*;

import java.util.List;

public interface HistoryManager {

    List<Task> getViewedTasks();

    void addViewedTask(Task task);

    void remove(int id);
}