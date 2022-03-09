package sk.emaa.rest.service.model;

import java.util.Date;
import java.util.List;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("training")
public class Training implements Comparable<Training> {

	@Id
	private String id;
	private String schoolId;
	private Date date;
	private List<String> presentStudentIds;
	private List<String> notPresentStudentIds;
	private String notes;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getSchoolId() {
		return schoolId;
	}
	
	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	public List<String> getPresentStudentIds() {
		return presentStudentIds;
	}

	public void setPresentStudentIds(List<String> presentStudentIds) {
		this.presentStudentIds = presentStudentIds;
	}

	public List<String> getNotPresentStudentIds() {
		return notPresentStudentIds;
	}

	public void setNotPresentStudentIds(List<String> notPresentStudentIds) {
		this.notPresentStudentIds = notPresentStudentIds;
	}

	public String getNotes() {
		return notes;
	}

	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	@Override
	public int compareTo(Training training) {
		return getDate().compareTo(training.getDate());
	}
	
	@Override
	public String toString() {
		// TODO Auto-generated method stub
		return "Training - " + date;
	}

}
