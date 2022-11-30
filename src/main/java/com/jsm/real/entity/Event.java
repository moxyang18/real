package com.jsm.real.entity;

import java.sql.Date;

import java.sql.Timestamp;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="jsm_event")
//@DiscriminatorColumn(discriminatorType = DiscriminatorType.STRING,
//    name = "event_type",
//    columnDefinition = "CHAR(1)")
public class Event {
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY) private Long event_id;
	private String event_name;
	private Character event_type;
	private Timestamp start_datetime;
	private Timestamp stop_datetime;
	private Integer topic_id;
	public Long getEvent_id() {
		return event_id;
	}
	public void setEvent_id(Long event_id) {
		this.event_id = event_id;
	}
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
	public Timestamp getStart_datetime() {
		return start_datetime;
	}
	public void setStart_datetime(Timestamp start_datetime) {
		this.start_datetime = start_datetime;
	}
	public Timestamp getStop_datetime() {
		return stop_datetime;
	}
	public void setStop_datetime(Timestamp stop_datetime) {
		this.stop_datetime = stop_datetime;
	}
	public Integer getTopic_id() {
		return topic_id;
	}
	public void setTopic_id(Integer topic_id) {
		this.topic_id = topic_id;
	}
	

}
