package tasks;


public class Subtask extends Task {
	int epicID;

	public Subtask(String title, String desc, int id, String status, int epicId) {
		super(title, desc, id, status);
		this.epicId = epicId;
	}

	public int getEpicId() {return epicId;}

	public void setEpicId(int epicId) {
		this.epicId = epicId;
	}
}
