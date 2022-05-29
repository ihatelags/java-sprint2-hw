package managers;

import exceptions.ManagerSaveException;
import managers.interfaces.HistoryManager;
import managers.interfaces.Manager;
import tasks.*;

import java.io.*;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static java.nio.charset.StandardCharsets.UTF_8;

public class FileBackedTasksManager extends InMemoryTasksManager {
    private final File file;

    public FileBackedTasksManager() {
        this(new File("tasks.csv"));
    }

    public FileBackedTasksManager(File file) {
        this.file = file;
    }

    private void save() {
        try (final BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            //Заголовок
            writer.write("id,type,name,status,description,epic" + "\n");
            for (Map.Entry<Integer, Task> entry : tasks.entrySet()) {
                writer.append(toString(entry.getValue()));
            }
            for (Map.Entry<Integer, Epic> entry : epics.entrySet()) {
                writer.append(toString(entry.getValue()));
            }
            for (Map.Entry<Integer, Subtask> entry : subtasks.entrySet()) {
                writer.append(toString(entry.getValue()));
            }
            writer.newLine();
            writer.write(historyToString(super.historyManager));
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
    }

    //метод сохранения задачи в строку id,type,name,status,description,epic
    public String toString(Task task) {
        if (task.getType() == TaskType.SUBTASK) {
            Subtask subtask = (Subtask) task;
            return subtask.getId() + "," + subtask.getType() + "," + subtask.getName() + "," + subtask.getStatus() +
                    "," + subtask.getDesc() + "," + subtask.getEpicId() + "\n";
        }
        return task.getId() + "," + task.getType() + "," + task.getName() + "," + task.getStatus() + "," +
                task.getDesc() + ",\n";
    }

    //метод создания задачи из строки
    private Task fromString(String value) {
        Task task;
        String[] taskData = value.split(",");
        if (taskData.length == 0) {
            return null;
        }
        String id = taskData[0];
        String type = taskData[1];
        String name = taskData[2];
        String status = taskData[3];
        String description = taskData[4];
        switch (TaskType.valueOf(type)) {
            //id,type,name,status,description,epic
            case TASK:
                task = new Task(name, description, Status.valueOf(status));
                task.setId(Integer.parseInt(id));
                break;
            case EPIC:
                task = new Epic(name, description, Status.valueOf(status));
                task.setId(Integer.parseInt(id));
                break;
            case SUBTASK:
                int epicId = Integer.parseInt(taskData[5]);
                task = new Subtask(name, description, Status.valueOf(status), epicId);
                task.setId(Integer.parseInt(id));
                break;
            default:
                throw new ManagerSaveException("Task type error.");
        }
        return task;
    }


    //метод для сохранения менеджера истории из CSV
    private static String historyToString(HistoryManager manager) {
        StringBuilder sb = new StringBuilder();
        for (Task task : manager.getHistory()) {
            sb.append(task.getId()).append(",");
        }
        //убираем последнюю запятую
        if (sb.length() > 0) {
            sb.setLength(sb.length() - 1);
        }
        return sb.toString();
    }

    //метод для восстановления менеджера истории из CSV
    private static List<Integer> historyFromString(String value) {
        List<Integer> history = new ArrayList<>();
        if (value != null) {
            for (String id : value.split(",")) {
                history.add(Integer.parseInt(id));
            }
        }
        return history;
    }

    @Override
    public List<Task> getTasks() {
        final List<Task> tasks = super.getTasks();
        save();
        return tasks;
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void createTask(Task task) {
        super.createTask(task);
        save();
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void createEpic(Epic epic) {
        super.createEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteByID(int id) {
        super.deleteByID(id);
        save();
    }

    //   метод, который будет восстанавливать данные менеджера из файла при запуске программы
    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTasksManager fileBackedTasksManager = new FileBackedTasksManager(file);
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(Files.newInputStream(file.toPath()), UTF_8))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.equals("id,type,name,status,description,epic")) {
                    continue; // Пропускаем заголовок
                }
                if (line.isEmpty()) {
                    break;
                }
                Task task = fileBackedTasksManager.fromString(line);
                switch (task.getType()) {
                    case TASK:
                        fileBackedTasksManager.tasks.put(task.getId(), task);
                        break;
                    case EPIC:
                        fileBackedTasksManager.epics.put(task.getId(), (Epic) task);
                        break;
                    case SUBTASK:
                        fileBackedTasksManager.subtasks.put(task.getId(), (Subtask) task);
                        Integer epicID = ((Subtask) task).getEpicId();
                        Epic epic = fileBackedTasksManager.epics.get(epicID);
                        if (epic != null) {
                            epic.getSubtasks().add((Subtask) task);
                        }
                        break;
                }
            }
            line = reader.readLine();
            List<Integer> history = historyFromString(line);
            for (Integer id : history) {
                Task task = fileBackedTasksManager.getByID(id);
                fileBackedTasksManager.historyManager.add(task);
            }
        } catch (IOException e) {
            throw new ManagerSaveException(e.getMessage());
        }
        return fileBackedTasksManager;
    }

    public static void main(String[] args) {

        Manager taskManager = Managers.getDefault();

        //создайте две задачи, эпик с тремя подзадачами и эпик без подзадач;
        System.out.println("Создаем тестовые объекты...");
        Task task1 = new Task("task1 title", "task1 desc", Status.NEW);
        taskManager.createTask(task1);
        Task task2 = new Task("task2 title", "task2 desc", Status.NEW);
        taskManager.createTask(task2);
        Epic epic1 = new Epic("epic1 title",
                "epic1 desc",
                Status.NEW,
                new ArrayList<>());
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("subtask1 title",
                "subtask1 desc",
                Status.NEW,
                epic1.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("subtask2 title",
                "subtask2 title",
                Status.NEW,
                epic1.getId());
        taskManager.createSubtask(subtask2);
        Subtask subtask3 = new Subtask("subtask3 title",
                "subtask3 title",
                Status.NEW,
                epic1.getId());
        taskManager.createSubtask(subtask3);
        Epic epic2 = new Epic("epic2 title",
                "epic2 desc",
                Status.NEW,
                new ArrayList<>());
        taskManager.createEpic(epic2);

        //запросите созданные задачи несколько раз в разном порядке
        //после каждого запроса выведите историю и убедитесь, что в ней нет повторов
        System.out.println("Наполняем и выводим историю просмотров...");
        taskManager.getByID(1);
        taskManager.getByID(2);
        taskManager.getByID(1);
        taskManager.getByID(2);
        taskManager.getByID(5);
        taskManager.getByID(3);
        taskManager.getByID(7);
        taskManager.updateTask(task1);
        System.out.println(taskManager.getHistory());
        System.out.println();


        // Создайте новый FileBackedTasksManager менеджер из этого же файла
        // Проверьте, что история просмотра восстановилась верно и все задачи, эпики, подзадачи, которые были в старом,
        // есть в новом менеджере
        FileBackedTasksManager newFileBackedTasksManager = FileBackedTasksManager.loadFromFile(
                new File("tasks.csv"));
        System.out.println("Выводим историю после загрузки файла...");
        System.out.println(newFileBackedTasksManager.getHistory());
        System.out.println("Сравниваним таски: " + newFileBackedTasksManager.getTasks().equals(taskManager.getTasks()));
        System.out.println("Сравниваним эпики: " + newFileBackedTasksManager.getEpics().equals(taskManager.getEpics()));
        System.out.println("Сравниваним историю: " + newFileBackedTasksManager.getHistory().equals(taskManager.getHistory()));
    }

}
