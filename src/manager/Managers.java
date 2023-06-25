package manager;

import java.io.File;

public abstract class Managers {
    public static final String url = "http://localhost:";
    public static final File file = new File("src" + File.separator + "resources", "tasks.csv");
    public static final File PATH_FILE = new File("src" + File.separator + "resources", "tasks.csv");

    public TaskManager getDefault() {
        InMemoryHistoryManager hm = new InMemoryHistoryManager();
        return new InMemoryTaskManager(hm);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}