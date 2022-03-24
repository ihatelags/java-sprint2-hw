package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;


public class Manager {
	private final HashMap<Integer, Task> tasks = new HashMap<>();
	private int generateID = 0;

	/**
	 * Список всех задач
	 *
	 * @return
	 */
	public ArrayList<Task> getTasks() {
		return new ArrayList<>(tasks.values());
	}

	/**
	 * Удаление всех задач
	 */
	public void clearTasks() {
		tasks.clear();
	}

	/**
	 * Получение по идентификатору
	 */
	public Task getTask(int id) {
		return tasks.get(id);
	}

	/**
	 * Создание. Сам объект должен передаваться в качестве параметра.
	 */
	public void createTask(Task task) {
		task.setId(++generateID);
		tasks.put(task.getId(), task);
	}

	/**
	 * Обновление. Новая версия объекта с верным идентификатором передаются в видемпараметра.
	 */
	public void updateTask(Task task) {
		if (!tasks.containsKey(task.getId())) {
			return;
		}
		tasks.put(task.getId(), task);
	}

	/**
	 * Удаление по идентификатору
	 */
	public void deleteTask(int id) {
		tasks.remove(id);
	}

	private void updateStatus(Epic epic) {
		for (Subtask subtask : epic.getSubtasks()) {
			System.out.println("hi");
		}
	}
}
