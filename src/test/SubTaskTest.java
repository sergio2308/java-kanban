import tasks.*;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SubTaskTest {
    class TaskTest {

        @Test
        public void shouldReturnEquals() {
            Epic epic1 = new Epic("Эпик 1","Описание эпика 1");
            int epicId = 0;
            int id = 1;
            SubTask subTask1 = new SubTask("Подзадача 1", "Описание подзадачи 1", 0, Status.NEW);
            subTask1.setId(id);
            SubTask subTask2 = new SubTask("Подзадача 2", "Описание подзадачи 2", 0, Status.NEW);
            subTask2.setId(id);
            assertEquals(subTask1, subTask2, "Подзадачи не равны!");
        }
    }
}