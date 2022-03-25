package tests;

import manager.Manager;
import tasks.Status;
import tasks.Task;

public class Tests {

    public void doTest()
    {
        final IdGenerator idGenerator = new IdGenerator();
        Manager manager = new Manager(new HashMap<>(), new HashMap<>(), new HashMap<>());
        Task task1 = new Task("tidy up", "just do it!", idGenerator.getNewId());
        manager.createTask(task1);
        Task task2 = new Task("walk the dog", "woof-woof", idGenerator.getNewId());
        manager.createTask(task2);
        Epic epic1 = new Epic("prepare for OCA",
                "life is hard",
                Status.NEW,
                new HashSet<>(),
                idGenerator.getNewId());
        manager.createEpic(epic1);
        Subtask subtask1 = new Subtask("read documentation",
                "it's gonna be so much fun",
                Status.NEW,
                epic1.getId(),
                idGenerator.getNewId());
        manager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("do practice tests",
                "work work work",
                Status.NEW,
                epic1.getId(),
                idGenerator.getNewId());
        manager.createSubtask(subtask2);
        Epic epic2 = new Epic("get ready for IELTS",
                "let me speak from my heart",
                Status.NEW,
                new HashSet<>(),
                idGenerator.getNewId());
        manager.createEpic(epic2);

        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());
        System.out.println();

        Task updatedTask = new Task(task1.getTitle(), task1.getDescription(), Status.IN_PROGRESS, task1.getId());
        manager.updateTask(updatedTask);
        Subtask updatedSubtask = new Subtask(subtask1.getTitle(),
                subtask1.getDescription(),
                subtask1.getId(),
                Status.IN_PROGRESS,
                subtask1.getEpicId());
        manager.updateSubtask(updatedSubtask);

        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());
        System.out.println();

        manager.deleteEpicById(epic2.getId());
        manager.deleteTaskById(task2.getId());
        manager.deleteSubtaskById(subtask2.getId());

        System.out.println(manager.getEpics());
        System.out.println(manager.getTasks());
        System.out.println(manager.getSubtasks());
        System.out.println();
    }

}
