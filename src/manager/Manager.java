package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Класс Manager содержит список CRUD методов для всех типов задач.
 */
public class Manager {
	private HashMap<Integer, Task> tasks = new HashMap<>();
	private HashMap<Integer, Epic> epics = new HashMap<>();
	private HashMap<Integer, Subtask> subtasks = new HashMap<>();
	private int generateID = 0;

	public Manager(HashMap<Integer, Epic> epics, HashMap<Integer, Task> tasks, HashMap<Integer, Subtask> subtasks) {
		this.epics = epics;
		this.tasks = tasks;
		this.subtasks = subtasks;
	}

	/**
	 * Список всех задач
	 */
	public ArrayList<Task> getTasks() {
		return new ArrayList<>(tasks.values());
	}

	public ArrayList<Task> getEpics() {
		return new ArrayList<>(epics.values());
	}

	public ArrayList<Task> getSubtasks() {
		return new ArrayList<>(subtasks.values());
	}

	/**
	 * Удаление всех задач
	 */
	public void clearTasks() {
		tasks.clear();
	}

	public void clearEpics() {
		epics.clear();
	}

	public void clearSubtasks() {
		subtasks.clear();
	}

	/**
	 * Получение по идентификатору
	 */
	public Task getTaskById(int id) {
		return tasks.get(id);
	}

	public Task getEpicById(int id) {
		return epics.get(id);
	}

	public Task getSubtaskById(int id) {
		return subtasks.get(id);
	}

	/**
	 * Создание. Сам объект должен передаваться в качестве параметра.
	 */
	public void createTask(Task task) {
		task.setId(++generateID);
		tasks.put(task.getId(), task);
	}

	public void createEpic(Epic epic) {
		epic.setId(++generateID);
		epics.put(epic.getId(), epic);
	}

	public void createSubtask(Subtask subtask) {
		subtask.setId(++generateID);
		subtasks.put(subtask.getId(), subtask);
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

	public void updateEpic(Epic epic) {
		if (!epics.containsKey(epic.getId())) {
			return;
		}
		epics.put(epic.getId(), epic);
	}

	public void updateSubtask(Subtask subtask) {
		if (!subtasks.containsKey(subtask.getId())) {
			return;
		}
		subtasks.put(subtask.getId(), subtask);
	}

	/**
	 * Удаление по идентификатору
	 */
	public void deleteTask(int id) {
		tasks.remove(id);
	}

	public void deleteEpic(int id) {
		epics.remove(id);
	}

	public void deleteSubtask(int id) {
		subtasks.remove(id);
	}

	/**
	 * Обновление статуса Эпика
	 */
	private void updateStatus(Epic epic) {
		int countDone = 0;
		int countNew = 0;
		for (Subtask subtask : epic.getSubtasks()) {
			if (subtask.getStatus() == "NEW") {
				countNew++;
			} else if (subtask.getStatus() == "DONE") {
				countDone++;
			}
			if (countNew == Subtasks.size()) {
				epics.get(subtask.getEpicId()).setStatus("NEW");
			} else if (countDone == Subtasks.size()) {
				epics.get(subtask.getEpicId()).setStatus("DONE");
			} else {
				epics.get(subtask.getEpicId()).setStatus("IN_PROGRESS");
			}
		}
	}
}
