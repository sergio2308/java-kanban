package manager;

public abstract class Managers {
    public TaskManager getDefault() {
        InMemoryHistoryManager hm = new InMemoryHistoryManager();
        return new InMemoryTaskManager(hm);
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}