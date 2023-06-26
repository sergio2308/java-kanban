package manager;

import java.io.File;

public abstract class Managers {
    static final String URL = "http://localhost:";
    static final File FILE = new File("src" + File.separator + "resources", "tasks.csv");
    static final File PATH_FILE = new File("src" + File.separator + "resources", "tasks.csv");

    public TaskManager getDefault() {
        InMemoryHistoryManager hm = new InMemoryHistoryManager();
        return new InMemoryTaskManager(hm);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}