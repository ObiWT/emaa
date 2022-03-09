package sk.emaa.rest.service.model;

import java.util.Date;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("grade3")
public class Grade3 {

	@Id
	private String id;
	private String studentId;
	private Date defenseAgainstMultipleEnemies;
	private Date defenseAgainstWeapons;
	private Date softTechniques;
	
	public String getId() {
		return id;
	}
	
	public void setId(String id) {
		this.id = id;
	}
	
	public String getStudentId() {
		return studentId;
	}
	
	public void setStudentId(String studentId) {
		this.studentId = studentId;
	}

	public Date getDefenseAgainstMultipleEnemies() {
		return defenseAgainstMultipleEnemies;
	}

	public void setDefenseAgainstMultipleEnemies(Date defenseAgainstMultipleEnemies) {
		this.defenseAgainstMultipleEnemies = defenseAgainstMultipleEnemies;
	}

	public Date getDefenseAgainstWeapons() {
		return defenseAgainstWeapons;
	}

	public void setDefenseAgainstWeapons(Date defenseAgainstWeapons) {
		this.defenseAgainstWeapons = defenseAgainstWeapons;
	}

	public Date getSoftTechniques() {
		return softTechniques;
	}

	public void setSoftTechniques(Date softTechniques) {
		this.softTechniques = softTechniques;
	}
	
}
