package sk.emaa.rest.service.model;

import java.util.Date;

public class SeminarDay {
	
	private Date date;
	private Boolean breakfastDisabled;
	private Boolean lunchDisabled;
	private Boolean dinnerDisabled;
	
	public Date getDate() {
		return date;
	}
	
	public void setDate(Date date) {
		this.date = date;
	}

	public Boolean getBreakfastDisabled() {
		return breakfastDisabled;
	}

	public void setBreakfastDisabled(Boolean breakfastDisabled) {
		this.breakfastDisabled = breakfastDisabled;
	}

	public Boolean getLunchDisabled() {
		return lunchDisabled;
	}

	public void setLunchDisabled(Boolean lunchDisabled) {
		this.lunchDisabled = lunchDisabled;
	}

	public Boolean getDinnerDisabled() {
		return dinnerDisabled;
	}

	public void setDinnerDisabled(Boolean dinnerDisabled) {
		this.dinnerDisabled = dinnerDisabled;
	}

}
