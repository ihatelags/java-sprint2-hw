package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>{

    @BeforeEach
    @Override
    void init() {
        try {
            taskManager = (FileBackedTaskManager) Managers.getDefault();
            super.init();
        }
        catch(IOException e) {
            e.printStackTrace();
        }
    }

    @Test
    void shouldLoadHistoryFromFile() {
        FileBackedTaskManager fb = FileBackedTaskManager.loadFromFile(
                new File("tasks.csv"));
        assertEquals(taskManager.getHistory(), fb.getHistory(), "История не совпадает");
        assertEquals(taskManager.getEpics(), fb.getEpics(), "История не совпадает");
        assertEquals(taskManager.getTasks(), fb.getTasks(), "История не совпадает");
    }

    @Test
    void shouldSaveAndLoadTasks() {
        FileBackedTaskManager fb = FileBackedTaskManager.loadFromFile(
                new File("tasks.csv"));
        assertEquals(task, fb.getByID(1));
        assertEquals(epic, fb.getByID(2));
        assertEquals(subtask, fb.getByID(3));
    }

    @Test
    public void shouldSaveEmptyTasksAndEmptyHistoryAndLoadEmpty() {
        taskManager.deleteTasks();
        taskManager.deleteEpics();
        FileBackedTaskManager fb = FileBackedTaskManager.loadFromFile(
                new File("tasks.csv"));
        assertNull(fb.getTask(0));
        assertEquals(new ArrayList<>(), fb.getHistory());
    }
}