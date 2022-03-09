package sk.emaa.rest.service.model;

import java.util.Date;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("student")
public class Student implements Comparable<Student> {

	@Id
	private String id;
	private String schoolId;
	private String gender;
	private int actualGrade;
	private String firstname;
	private String lastname;
	private Date birthdate;
	private String parentContact;
	private String streetAndNumber;
	private String city;
	private String zipCode;
	private String phone;
	private String email;
	private String notes;
	private boolean deactivated;
	private String examNotes;
	private boolean instructor;
	
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

	public String getGender() {
		return gender;
	}
	
	public void setGender(String gender) {
		this.gender = gender;
	}
	
	public int getActualGrade() {
		return actualGrade;
	}

	public void setActualGrade(int actualGrade) {
		this.actualGrade = actualGrade;
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
	
	public Date getBirthdate() {
		return birthdate;
	}
	
	public void setBirthdate(Date birthdate) {
		this.birthdate = birthdate;
	}
	
	public String getParentContact() {
		return parentContact;
	}

	public void setParentContact(String parentContact) {
		this.parentContact = parentContact;
	}

	public String getStreetAndNumber() {
		return streetAndNumber;
	}
	
	public void setStreetAndNumber(String streetAndNumber) {
		this.streetAndNumber = streetAndNumber;
	}
	
	public String getCity() {
		return city;
	}
	
	public void setCity(String city) {
		this.city = city;
	}
	
	public String getZipCode() {
		return zipCode;
	}

	public void setZipCode(String zipCode) {
		this.zipCode = zipCode;
	}

	public String getPhone() {
		return phone;
	}
	
	public void setPhone(String phone) {
		this.phone = phone;
	}
	
	public String getEmail() {
		return email;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public String getNotes() {
		return notes;
	}
	
	public void setNotes(String notes) {
		this.notes = notes;
	}
	
	public boolean isDeactivated() {
		return deactivated;
	}

	public void setDeactivated(boolean deactivated) {
		this.deactivated = deactivated;
	}

	public String getExamNotes() {
		return examNotes;
	}

	public void setExamNotes(String examNotes) {
		this.examNotes = examNotes;
	}

	public boolean isInstructor() {
		return instructor;
	}

	public void setInstructor(boolean instructor) {
		this.instructor = instructor;
	}

	@Override
	public String toString() {
		return "Student id: " + id + ", schoolId: " + schoolId + ", firstname: " + firstname + ", lastname: " + lastname+ ", birthdate: " + birthdate;
	}
	
	@Override
	public int compareTo(Student student) {
		return lastname.compareTo(student.getLastname());
	}
	
}
