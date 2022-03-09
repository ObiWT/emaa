package sk.emaa.rest.service.model;

import java.util.List;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("seminarStudent")
public class SeminarStudent {

	@Id
	private String id;
	private String seminarId;
	private String studentId;
	private String firstname;
	private String lastname;
	private String schoolId;
	private String schoolCity;
	private int actualGrade;
	private boolean goingToExam;
	private boolean vegetarian;
	private boolean instructor;
	private double haveToPay;
	private List<SeminarDayStudent> days;
	private boolean paidForTrainings;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getSeminarId() {
		return seminarId;
	}
	
	public void setSeminarId(String seminarId) {
		this.seminarId = seminarId;
	}

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

	public String getSchoolId() {
		return schoolId;
	}

	public void setSchoolId(String schoolId) {
		this.schoolId = schoolId;
	}

	public String getSchoolCity() {
		return schoolCity;
	}

	public void setSchoolCity(String schoolCity) {
		this.schoolCity = schoolCity;
	}

	public int getActualGrade() {
		return actualGrade;
	}

	public void setActualGrade(int actualGrade) {
		this.actualGrade = actualGrade;
	}

	public boolean isGoingToExam() {
		return goingToExam;
	}

	public void setGoingToExam(boolean goingToExam) {
		this.goingToExam = goingToExam;
	}

	public boolean isVegetarian() {
		return vegetarian;
	}

	public void setVegetarian(boolean vegetarian) {
		this.vegetarian = vegetarian;
	}

	public boolean isInstructor() {
		return instructor;
	}

	public void setInstructor(boolean instructor) {
		this.instructor = instructor;
	}

	public double getHaveToPay() {
		return haveToPay;
	}

	public void setHaveToPay(double haveToPay) {
		this.haveToPay = haveToPay;
	}

	public List<SeminarDayStudent> getDays() {
		return days;
	}

	public void setDays(List<SeminarDayStudent> days) {
		this.days = days;
	}

	public boolean isPaidForTrainings() {
		return paidForTrainings;
	}

	public void setPaidForTrainings(boolean paidForTrainings) {
		this.paidForTrainings = paidForTrainings;
	}
	
}
