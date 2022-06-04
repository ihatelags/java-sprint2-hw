package managers;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager>{

    @BeforeEach
    @Override
    void init() {
        taskManager = (FileBackedTaskManager) Managers.getDefault();
        super.init();
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