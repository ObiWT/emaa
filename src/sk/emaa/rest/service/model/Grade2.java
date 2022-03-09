package sk.emaa.rest.service.model;

import java.util.Date;

import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity("grade2")
public class Grade2 {

	@Id
	private String id;
	private String studentId;
	private Date chumKiu;
	private Date history;
	private Date danChi;
	private Date chiSaoSection1;
	
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

	public Date getChumKiu() {
		return chumKiu;
	}

	public void setChumKiu(Date chumKiu) {
		this.chumKiu = chumKiu;
	}

	public Date getHistory() {
		return history;
	}

	public void setHistory(Date history) {
		this.history = history;
	}

	public Date getDanChi() {
		return danChi;
	}

	public void setDanChi(Date danChi) {
		this.danChi = danChi;
	}

	public Date getChiSaoSection1() {
		return chiSaoSection1;
	}

	public void setChiSaoSection1(Date chiSaoSection1) {
		this.chiSaoSection1 = chiSaoSection1;
	}

}
