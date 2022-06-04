package managers;

import org.junit.jupiter.api.BeforeEach;
import tasks.Epic;
import tasks.Status;
import tasks.Subtask;
import tasks.Task;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.TreeSet;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {
    protected T taskManager;
    Task task;
    Epic epic;
    Subtask subtask;

    @BeforeEach
    void init() {
        taskManager.deleteTasks();
        task = new Task("task title", "task1 desc", Status.NEW, LocalDateTime.now(), 15);
        taskManager.createTask(task);
        epic = new Epic("epic1 title","epic1 desc",Status.NEW,new ArrayList<>());
        taskManager.createEpic(epic);
        subtask = new Subtask("subtask1 title","subtask1 desc",Status.NEW,
                LocalDateTime.now().plusMinutes(16), 15,epic.getId());
        taskManager.createSubtask(subtask);
    }

    @Test
    void shouldGetAllTasks() {
        assertEquals(1, taskManager.getTasks().size());
    }

    @Test
    void shouldDeleteAllTasks() {
        taskManager.deleteTasks();
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void shouldGetTask() {
        assertEquals(task, taskManager.getTask(task.getId()));
    }

    @Test
    void shouldCreateTask() {
        assertEquals(task, taskManager.getTask(task.getId()));
    }

    @Test
    void shouldUpdateTask() {
        Task taskNew = new Task("updated task", "task1 desc", task.getId(), Status.NEW,
                LocalDateTime.now(), 15);
        taskManager.updateTask(taskNew);
        assertEquals(taskNew, taskManager.getTask(task.getId()));
    }

    @Test
    void shouldDeleteTask() {
        taskManager.deleteTask(task.getId());
        assertNull(taskManager.getTask(task.getId()), "Задача не удалена");
    }

    @Test
    void shouldGetSubtask() {
        assertEquals(subtask, taskManager.getSubtask(subtask.getId()));
    }

    @Test
    void shouldCreateSubtask() {
        assertNotNull(subtask, "Подзадача не найдена");
        assertEquals(subtask, taskManager.getSubtask(subtask.getId()), "Подзадачи не совпадают");
    }

    @Test
    void shouldUpdateSubtask() {
        Subtask subtaskNew = new Subtask("updated subtask", "desc", subtask.getId(), Status.NEW,
                LocalDateTime.now(), 15, epic.getId());
        taskManager.updateSubtask(subtaskNew);
        assertEquals(subtaskNew, taskManager.getSubtask(subtask.getId()));
    }

    @Test
    void shouldDeleteSubtask() {
        taskManager.deleteSubtask(subtask.getId());
        assertNull(taskManager.getSubtask(subtask.getId()), "Подзадача не удалена");
    }

    @Test
    void shouldGetEpics() {
        assertEquals(1, taskManager.getEpics().size());
        assertEquals(epic, taskManager.getEpics().get(0));
    }

    @Test
    void shouldDeleteEpics() {
        taskManager.deleteEpics();
        assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    void shouldGetEpic() {
        assertEquals(epic, taskManager.getEpic(epic.getId()));
    }

    @Test
    void shouldCreateEpic() {
        assertNotNull(epic, "Эпик не найден");
        assertEquals(epic, taskManager.getEpic(epic.getId()), "Подзадачи не совпадают");
    }

    @Test
    void shouldUpdateEpic() {
        Epic epicNew = new Epic("updated epic", "desc", Status.NEW, epic.getSubtasks());
        epicNew.setId(epic.getId());
        taskManager.updateEpic(epicNew);
        assertEquals(epicNew, taskManager.getEpic(epic.getId()));
    }

    @Test
    void shouldDeleteEpic() {
        taskManager.deleteEpic(epic.getId());
        assertNull(taskManager.getEpic(epic.getId()), "Эпик не удален");
    }

    @Test
    void shouldGetByID() {
        assertNotNull(taskManager.getByID(task.getId()), "Задача не найдена");
        assertNotNull(taskManager.getByID(subtask.getId()), "Подзадача не найдена");
        assertNotNull(taskManager.getByID(epic.getId()), "Эпик не найден");
        assertEquals(task, taskManager.getTask(task.getId()), "Задача не найдена");
        assertEquals(subtask, taskManager.getSubtask(subtask.getId()), "Подзадача не найдена");
        assertEquals(epic, taskManager.getEpic(epic.getId()), "Эпик не найден");
    }

    @Test
    void shouldDeleteByID() {
        taskManager.deleteByID(task.getId());
        taskManager.deleteByID(subtask.getId());
        taskManager.deleteByID(epic.getId());
        assertNull(taskManager.getByID(task.getId()), "Задача не удалена");
        assertNull(taskManager.getByID(subtask.getId()), "Подзадача не удалена");
        assertNull(taskManager.getByID(epic.getId()), "Эпик не удален");
    }

    @Test
    void shouldGetHistory() {
        taskManager.getByID(task.getId());
        taskManager.getByID(subtask.getId());
        assertEquals(2, taskManager.getHistory().size());
    }

    @Test
    void shouldGetPrioritizedTasks(){
        Task task2 = new Task("task2 title", "desc", Status.NEW, LocalDateTime.now().minusHours(1), 15);
        taskManager.createTask(task2);
        Task task3 = new Task("task3 title", "desc", Status.NEW);
        taskManager.createTask(task3);
        List<Task> list = new ArrayList<>(Arrays.asList(task2, task, subtask, task3));
        List<Task> sortedList = new ArrayList<>(taskManager.getPrioritizedTasks());
        assertEquals(list, sortedList);
        assertEquals(4, sortedList.size());
    }

}