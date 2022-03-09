package sk.emaa.rest.service.model;

import java.util.Date;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("grade1")
public class Grade1 {
	
	@Id
	private String id;
	private String studentId;
	private Date siuNimTao;
	private Date selfdefense;
	private Date basicTechniques;
	private Date latSao;
	private Date theory;
	
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

	public Date getSiuNimTao() {
		return siuNimTao;
	}

	public void setSiuNimTao(Date siuNimTao) {
		this.siuNimTao = siuNimTao;
	}

	public Date getSelfdefense() {
		return selfdefense;
	}

	public void setSelfdefense(Date selfdefense) {
		this.selfdefense = selfdefense;
	}

	public Date getBasicTechniques() {
		return basicTechniques;
	}

	public void setBasicTechniques(Date basicTechniques) {
		this.basicTechniques = basicTechniques;
	}

	public Date getLatSao() {
		return latSao;
	}

	public void setLatSao(Date latSao) {
		this.latSao = latSao;
	}

	public Date getTheory() {
		return theory;
	}

	public void setTheory(Date theory) {
		this.theory = theory;
	}
	
	@Override
	public String toString() {
		return "studentId: " + studentId + ", siuNimTao: " + siuNimTao + ", selfdefense: " + selfdefense + ", basicTechniques: " + basicTechniques + ", latSao: " + latSao + ", theory: " + theory;
	}

}
