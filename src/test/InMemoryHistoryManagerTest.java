import manager.HistoryManager;
import manager.Managers;
import tasks.Status;
import tasks.Task;
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

   @Test
    void shouldRemoveTasks() {
        HistoryManager historyManager = Managers.getDefaultHistoryManager();
        Task task = new Task("Задача", "Описание", Status.NEW);
        historyManager.addHistory(task);
        assertEquals(1, historyManager.getHistory().size());
        historyManager.remove(task.getId());
        assertTrue(historyManager.getHistory().isEmpty());
    }
}