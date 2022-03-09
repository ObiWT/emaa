package sk.emaa.rest.service.model;

import java.util.Date;

public class SeminarDayStudent {
	
	private boolean accomodation;
	private Date date;
	private boolean breakfast;
	private boolean lunch;
	private boolean dinner;

	public boolean getAccomodation() {
		return accomodation;
	}

	public void setAccomodation(boolean accomodation) {
		this.accomodation = accomodation;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public boolean isBreakfast() {
		return breakfast;
	}
	
	public void setBreakfast(boolean breakfast) {
		this.breakfast = breakfast;
	}

	public boolean isLunch() {
		return lunch;
	}

	public void setLunch(boolean lunch) {
		this.lunch = lunch;
	}

	public boolean isDinner() {
		return dinner;
	}

	public void setDinner(boolean dinner) {
		this.dinner = dinner;
	}
	
}
