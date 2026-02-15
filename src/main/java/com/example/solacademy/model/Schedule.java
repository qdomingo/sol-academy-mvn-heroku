/**
 * 
 */
package com.example.solacademy.model;

/**
 * @author qdomi
 *
 */
public class Schedule {

    private Long id;
    private String title;
    private String inicio;
    private String fin;
    private Long student_id;
    private int weekly;
    private String serie_id;
    private Long task_id;
    
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public String getTitle() {
		return title;
	}
	public void setTitle(String title) {
		this.title = title;
	}
	public String getInicio() {
		return inicio;
	}
	public void setInicio(String inicio) {
		this.inicio = inicio;
	}
	public String getFin() {
		return fin;
	}
	public void setFin(String fin) {
		this.fin = fin;
	}
	public Long getStudent_id() {
		return student_id;
	}
	public void setStudent_id(Long student_id) {
		this.student_id = student_id;
	}
	public int getWeekly() {
		return weekly;
	}
	public void setWeekly(int weekly) {
		this.weekly = weekly;
	}
	public String getSerie_id() {
		return serie_id;
	}
	public void setSerie_id(String serie_id) {
		this.serie_id = serie_id;
	}
	public Long getTask_id() {
		return task_id;
	}
	public void setTask_id(Long task_id) {
		this.task_id = task_id;
	}      
}
