package sk.emaa.rest.service.model;

import java.util.List;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity(value = "schoolFee", noClassnameStored = true)
public class SchoolFee {

	@Id
	private String id;
	private String schoolId;
	private int month;
	private int year;
	private List<String> paid;
	
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

	public int getMonth() {
		return month;
	}

	public void setMonth(int month) {
		this.month = month;
	}

	public int getYear() {
		return year;
	}

	public void setYear(int year) {
		this.year = year;
	}

	public List<String> isPaid() {
		return paid;
	}

	public void setPaid(List<String> paid) {
		this.paid = paid;
	}
	
}
