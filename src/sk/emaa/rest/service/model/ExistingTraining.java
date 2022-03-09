package sk.emaa.rest.service.model;

import java.util.List;

public class ExistingTraining {

	private String trainingId;
	private List<TrainingStudent> students;
	private String notes;
	
	public String getTrainingId() {
		return trainingId;
	}

	public void setTrainingId(String trainingId) {
		this.trainingId = trainingId;
	}
	
	public List<TrainingStudent> getStudents() {
		return students;
	}
	
	public void setStudents(List<TrainingStudent> students) {
		this.students = students;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
}
