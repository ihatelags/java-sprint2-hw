package tasks;

import java.util.ArrayList;

public class Epic extends Task {
	private ArrayList<Subtask> subtasks;

	public Epic(String title, String desc, int id, String status) {
		super(title, desc, id, status);
	}

	public Epic(String title, String desc, int id, String status, ArrayList<Subtask> subtasks) {
		super(title, desc, id, status);
		this.subtasks = subtasks;
	}

	public ArrayList<Subtask> getSubtasks() {
		return subtasks;
	}

	public void setSubtasks(ArrayList<Subtask> subtasks) {
		this.subtasks = subtasks;
	}
}
