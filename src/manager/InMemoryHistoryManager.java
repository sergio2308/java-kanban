package manager;

import task.*;
import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final List<Task> viewedTasks = new ArrayList<>();
    private static final int SIZE_OF_VIEWEDTASKS = 10;

    @Override
    public void addViewedTask(Task task) {
        if (task != null) {
            viewedTasks.add(task);
            if (viewedTasks.size() > SIZE_OF_VIEWEDTASKS) {
                viewedTasks.remove(0);
            }
        }
    }

    @Override
    public List<Task> getViewedTasks() {
        return viewedTasks;
    }

    @Override
    public void remove(int id) {
        viewedTasks.remove(id);
    }
}