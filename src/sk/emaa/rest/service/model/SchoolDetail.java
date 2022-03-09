package sk.emaa.rest.service.model;

import java.util.Map;

public class SchoolDetail {

	private long activeStudentsCount;
	
	private long inactiveStudentsCount;
	
	private String avarageOnTraining;
	
	private Map<String, Integer> data;


	public long getActiveStudentsCount() {
		return activeStudentsCount;
	}

	public void setActiveStudentsCount(long activeStudentsCount) {
		this.activeStudentsCount = activeStudentsCount;
	}

	public long getInactiveStudentsCount() {
		return inactiveStudentsCount;
	}

	public void setInactiveStudentsCount(long inactiveStudentsCount) {
		this.inactiveStudentsCount = inactiveStudentsCount;
	}

	public String getAvarageOnTraining() {
		return avarageOnTraining;
	}

	public void setAvarageOnTraining(String avarageOnTraining) {
		this.avarageOnTraining = avarageOnTraining;
	}

	public Map<String, Integer> getData() {
		return data;
	}

	public void setData(Map<String, Integer> data) {
		this.data = data;
	}
	
}
