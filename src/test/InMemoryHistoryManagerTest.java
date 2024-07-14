import Manager.HistoryManager;
import Manager.Managers;
import Tasks.Status;
import Tasks.Task;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    @Test
    void shouldSaveAddedTasks() {
        HistoryManager historyManager = Managers.getDefaultHistoryManager();
        Task task = new Task("Задача", "Описание", Status.NEW);
        historyManager.addHistory(task);
        historyManager.addHistory(task);
        for (Task task1 : historyManager.getHistory()) {
            assertEquals(task1, task, "Задачи не сохраняются в историю!");
        }
    }

    void shouldRemoveTasks() {
        HistoryManager historyManager = Managers.getDefaultHistoryManager();
        Task task1 = new Task("Задача1", "Описание1", Status.NEW);
        Task task2 = new Task("Задача2", "Описание2", Status.NEW);
        Task task3 = new Task("Задача3", "Описание3", Status.NEW);
        historyManager.addHistory(task1);
        historyManager.addHistory(task2);
        historyManager.addHistory(task3);
        assertEquals(3, historyManager.getHistory().size());
        historyManager.remove(task1.getId());
        assertEquals(2, historyManager.getHistory().size());
        historyManager.remove(task2.getId());
        assertEquals(1, historyManager.getHistory().size());
        historyManager.remove(task3.getId());
        assertTrue(historyManager.getHistory().isEmpty());
    }
}