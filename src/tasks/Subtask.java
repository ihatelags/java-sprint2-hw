package tasks;


public class Subtask extends Task {
	int epicID;

	public Subtask(String title, String desc, int id, String status, int epicID) {
		super(title, desc, id, status);
		this.epicID = epicID;
	}

	public int getepicID() {
		return epicID;
	}

	public void setepicID(int epicID) {
		this.epicID = epicID;
	}
}
