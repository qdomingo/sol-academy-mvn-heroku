package com.example.solacademy.model;

public class Task {
	
	private Long id;
	private String title;
	private String description;
	private Long student_id;
	private int confirmed;
	private Long schedule_id;
	
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
	public String getDescription() {
		return description;
	}
	public void setDescription(String description) {
		this.description = description;
	}

	public Long getStudent_id() {
		return student_id;
	}
	public void setStudent_id(Long student_id) {
		this.student_id = student_id;
	}
	public int getConfirmed() {
		return confirmed;
	}
	public void setConfirmed(int confirmed) {
		this.confirmed = confirmed;
	}
	public Long getSchedule_id() {
		return schedule_id;
	}
	public void setSchedule_id(Long schedule_id) {
		this.schedule_id = schedule_id;
	}
	
}
