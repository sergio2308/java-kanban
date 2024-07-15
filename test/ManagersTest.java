import manager.Managers;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ManagersTest {

    @Test
    void shouldReturnNotNullInGetDefault() {
        assertNotNull(Managers.getDefault());
    }

    @Test
    void shouldReturnNotNullIngetDefaultHistoryManager() {
        assertNotNull(Managers.getDefaultHistoryManager());
    }
}