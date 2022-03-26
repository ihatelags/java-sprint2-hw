package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;

/**
 * Класс Manager содержит список CRUD методов для всех типов задач.
 */
public class Manager {
	private final HashMap<Integer, Task> tasks;
	private final HashMap<Integer, Epic> epics;
	private final HashMap<Integer, Subtask> subtasks;
	private int generatedID = 0;

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
	public Task getTask(int id) {
		return tasks.get(id);
	}

	public Task getEpic(int id) {
		return epics.get(id);
	}

	public Task getSubtask(int id) {
		return subtasks.get(id);
	}

	/**
	 * Создание. Сам объект должен передаваться в качестве параметра.
	 */
	public void createTask(Task task) {
		task.setId(++generatedID);
		tasks.put(task.getId(), task);
	}

	public void createEpic(Epic epic) {
		epic.setId(++generatedID);
		epics.put(epic.getId(), epic);
	}

	public void createSubtask(Subtask subtask) {
		subtask.setId(++generatedID);
		if (subtasks.containsKey(subtask.getId())) {
			System.out.println("Задача с таким ID уже существует = " + subtask.getId());
			return;
		}
		if (!epics.containsKey(subtask.getEpicId())) {
			System.out.println("Эпик с таким ID не найден: " + subtask.getEpicId());
			return;
		}
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
		Epic epic = epics.get(subtask.getEpicId());
		ArrayList<Subtask> temp = new ArrayList<>();
		for (Subtask temp_subtask : subtasks.values()) {
			if (temp_subtask.getEpicId() == epic.getId()) {
				temp.add(temp_subtask);
			}
		}
		epic.setSubtasks(temp);

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

	public void deleteSubtask(Subtask subtask) {
		if (epics.containsKey(subtask.getEpicId())) {
			Epic epic = epics.get(subtask.getEpicId());
			epic.getSubtasks().remove(subtask);
		}
		subtasks.remove(subtask.getId());
	}

	/**
	 * Обновление статуса Эпика
	 */
	public void updateEpicStatus(Epic epic) {
		HashSet<String> subtasksSet = new HashSet<>();

		int countSubtask = epic.getSubtasks().size();
		// если у эпика нет подзадач, то статус должен быть NEW.
		if (countSubtask == 0) {
			epic.setStatus("NEW");
		} else {
			for (Subtask subtask : epic.getSubtasks()) {
				if (subtask.getStatus().equals("NEW")) {
					subtasksSet.add("NEW");
				} else if (subtask.getStatus().equals("DONE")) {
					subtasksSet.add("DONE");
				} else {
					subtasksSet.add("IN_PROGRESS");
				}
			}
			if (subtasksSet.size() == 1 && subtasksSet.toArray()[0] == ("NEW")) {
				epic.setStatus("NEW");
			} else if (subtasksSet.size() == 1 && subtasksSet.toArray()[0] == ("DONE")) {
				epic.setStatus("DONE");
			} else {
				epic.setStatus("IN_PROGRESS");
			}

		}
	}
}
