package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @BeforeEach
    @Override
    void init() {
        taskManager = new InMemoryTaskManager();
        super.init();
    }

    @Test
    void shouldReturnZeroTasksForNewManager() {
        taskManager = new InMemoryTaskManager();
        assertEquals(0, taskManager.getTasks().size(), "Задач нет");
        assertEquals(0, taskManager.getEpics().size(), "Задач нет");
        assertEquals(0, taskManager.getHistory().size(), "Задач нет");
    }
}