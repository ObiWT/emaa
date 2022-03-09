package sk.emaa.rest.service.model;

import java.util.Date;

import dev.morphia.annotations.Embedded;
import dev.morphia.annotations.Entity;
import dev.morphia.annotations.Id;

@Entity(value = "examStudent", noClassnameStored = true)
public class ExamStudent {

	@Id
	private String id;
	private String studentId;

	private String notesToExam1;
	private String notesToExam2;
	private String notesToExam3;

	private Date grade1Achieved;
	private Date grade2Achieved;
	private Date grade3Achieved;

	@Embedded
	private Exam4 exam4;
	@Embedded
	private Exam5 exam5;
	@Embedded
	private Exam6 exam6;
	
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
	
	public String getNotesToExam1() {
		return notesToExam1;
	}
	
	public void setNotesToExam1(String notesToExam1) {
		this.notesToExam1 = notesToExam1;
	}

	public String getNotesToExam2() {
		return notesToExam2;
	}

	public void setNotesToExam2(String notesToExam2) {
		this.notesToExam2 = notesToExam2;
	}

	public String getNotesToExam3() {
		return notesToExam3;
	}

	public void setNotesToExam3(String notesToExam3) {
		this.notesToExam3 = notesToExam3;
	}

	public Date getGrade1Achieved() {
		return grade1Achieved;
	}

	public void setGrade1Achieved(Date grade1Achieved) {
		this.grade1Achieved = grade1Achieved;
	}

	public Date getGrade2Achieved() {
		return grade2Achieved;
	}

	public void setGrade2Achieved(Date grade2Achieved) {
		this.grade2Achieved = grade2Achieved;
	}

	public Date getGrade3Achieved() {
		return grade3Achieved;
	}

	public void setGrade3Achieved(Date grade3Achieved) {
		this.grade3Achieved = grade3Achieved;
	}

	public Exam4 getExam4() {
		return exam4;
	}

	public void setExam4(Exam4 exam4) {
		this.exam4 = exam4;
	}

	public Exam5 getExam5() {
		return exam5;
	}

	public void setExam5(Exam5 exam5) {
		this.exam5 = exam5;
	}

	public Exam6 getExam6() {
		return exam6;
	}

	public void setExam6(Exam6 exam6) {
		this.exam6 = exam6;
	}
	
	@Override
	public String toString() {
		return "ExamStudent - studentId: " + studentId;
	}
	
}
