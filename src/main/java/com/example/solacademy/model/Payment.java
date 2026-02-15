package com.example.solacademy.model;

import java.time.LocalDate;

public class Payment {
	
    private Long schedule_id;
    private Long student_id;
    private String title;
    private String inicio;
    private String fin;
    private int duration;
    private String serie_id;
    private int paid;
    private int rate;

	public Long getSchedule_id() {
		return schedule_id;
	}
	public void setSchedule_id(Long schedule_id) {
		this.schedule_id = schedule_id;
	}
	public Long getStudent_id() {
		return student_id;
	}
	public void setStudent_id(Long student_id) {
		this.student_id = student_id;
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
	public int getDuration() {
		return duration;
	}
	public void setDuration(int duration) {
		this.duration = duration;
	}
	public String getSerie_id() {
		return serie_id;
	}
	public void setSerie_id(String serie_id) {
		this.serie_id = serie_id;
	}
	public int getPaid() {
		return paid;
	}
	public void setPaid(int paid) {
		this.paid = paid;
	}
	public int getRate() {
		return rate;
	}
	public void setRate(int rate) {
		this.rate = rate;
	}    
	
}
