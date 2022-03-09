package sk.emaa.rest.service.model;

public class TrainingStudent {

	private String studentId;
	private String firstname;
	private String lastname;
	private Boolean present;
	
	public String getStudentId() {
		return studentId;
	}
	
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}
	
	public String getFirstname() {
		return firstname;
	}
	
	public void setFirstname(String firstname) {
		this.firstname = firstname;
	}

	public String getLastname() {
		return lastname;
	}

	public void setLastname(String lastname) {
		this.lastname = lastname;
	}

	public Boolean isPresent() {
		return present;
	}

	public void setPresent(Boolean present) {
		this.present = present;
	}
	
	@Override
	public String toString() {
		return "Student id: " + studentId + ", firstname: " + firstname + ", lastname: " + lastname+ ", present: " + present;
	}

}
