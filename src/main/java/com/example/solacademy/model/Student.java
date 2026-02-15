/**
 * 
 */
package com.example.solacademy.model;

import java.util.List;

/**
 * @author qdomi
 *
 */
public class Student {

private Long id;
private String nickname;
private String name;
private String email;
private String comments;
private int rate;
private List<Task> tasks;

public Long getId() {
	return id;
}
public void setId(Long id) {
	this.id = id;
}
public String getNickname() {
	return nickname;
}
public void setNickname(String nickname) {
	this.nickname = nickname;
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getEmail() {
	return email;
}
public void setEmail(String email) {
	this.email = email;
}
public String getComments() {
	return comments;
}
public void setComments(String comments) {
	this.comments = comments;
}
public int getRate() {
	return rate;
}
public void setRate(int rate) {
	this.rate = rate;
}
public List<Task> getTasks() {
	return tasks;
}
public void setTasks(List<Task> tasks) {
	this.tasks = tasks;
}

}
