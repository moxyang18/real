package com.jsm.real.entity;

import java.sql.Timestamp;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
@Entity
public class EventSemExhibiMute {
	@Id private Long event_id;	
	private String event_name;
	private Character event_type;
	private String start_datetime;
	private String stop_datetime;
	private Integer topic_id;
	private Double expense;
	private Integer SEM_CAPACITY;
	public String getEvent_name() {
		return event_name;
	}
	public void setEvent_name(String event_name) {
		this.event_name = event_name;
	}
	public Character getEvent_type() {
		return event_type;
	}
	public void setEvent_type(Character event_type) {
		this.event_type = event_type;
	}
	public Long getEvent_id() {
		return event_id;
	}
	public void setEvent_id(Long event_id) {
		this.event_id = event_id;
	}
	public String getStart_datetime() {
		return start_datetime;
	}
	public void setStart_datetime(String start_datetime) {
		this.start_datetime = start_datetime;
	}
	public String getStop_datetime() {
		return stop_datetime;
	}
	public void setStop_datetime(String stop_datetime) {
		this.stop_datetime = stop_datetime;
	}
	public Integer getTopic_id() {
		return topic_id;
	}
	public void setTopic_id(Integer topic_id) {
		this.topic_id = topic_id;
	}
	public Double getExpense() {
		return expense;
	}
	public void setExpense(Double expense) {
		this.expense = expense;
	}
	public Integer getSEM_CAPACITY() {
		return SEM_CAPACITY;
	}
	public void setSEM_CAPACITY(Integer sEM_CAPACITY) {
		SEM_CAPACITY = sEM_CAPACITY;
	}
}
