package com.jsm.real.entity;

import java.sql.Date;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
@Entity
@Table(name="jsm_employee")
public class Employee {
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY) private Long emp_id;
	private String emp_uname;  
	private String emp_pwd;
	private String emp_stree;
	private String emp_city;
	private String emp_state;
	private String emp_zipcode;
	// "EMPLOYEE TYPE: WORKER, ADMIN"
	private String emp_type;
	private String emp_country;
	private Long emp_phone_no;
	private String emp_salt;
	private String emp_join_date;
	public String getEmp_salt() {
		return emp_salt;
	}
	public void setEmp_salt(String emp_salt) {
		this.emp_salt = emp_salt;
	}
	public Long getEmp_phone_no() {
		return emp_phone_no;
	}
	public void setEmp_phone_no(Long emp_phone_no) {
		this.emp_phone_no = emp_phone_no;
	}
	public String getEmp_email() {
		return emp_email;
	}
	public void setEmp_email(String emp_email) {
		this.emp_email = emp_email;
	}
	private String emp_email;
	public String getEmp_status() {
		return emp_status;
	}
	public void setEmp_status(String emp_status) {
		this.emp_status = emp_status;
	}
	private String emp_fname;
	private String emp_Lname;
	private String emp_dob;
	// "EMPLOYEE STATUS: 'current','left', 'frozen' "
	private String emp_status;
	public String getEmp_fname() {
		return emp_fname;
	}
	public void setEmp_fname(String emp_fname) {
		this.emp_fname = emp_fname;
	}
	public String getEmp_Lname() {
		return emp_Lname;
	}
	public void setEmp_Lname(String emp_Lname) {
		this.emp_Lname = emp_Lname;
	}
	public String getEmp_dob() {
		return emp_dob;
	}
	public void setEmp_dob(String emp_dob) {
		this.emp_dob = emp_dob;
	}
	public Long getEmp_id() {
		return emp_id;
	}
	public void setEmp_id(Long emp_id) {
		this.emp_id = emp_id;
	}
	public String getEmp_uname() {
		return emp_uname;
	}
	public void setEmp_uname(String emp_uname) {
		this.emp_uname = emp_uname;
	}
	public String getEmp_pwd() {
		return emp_pwd;
	}
	public void setEmp_pwd(String emp_pwd) {
		this.emp_pwd = emp_pwd;
	}
	public String getEmp_stree() {
		return emp_stree;
	}
	public void setEmp_stree(String emp_stree) {
		this.emp_stree = emp_stree;
	}
	public String getEmp_city() {
		return emp_city;
	}
	public void setEmp_city(String emp_city) {
		this.emp_city = emp_city;
	}
	public String getEmp_state() {
		return emp_state;
	}
	public void setEmp_state(String emp_state) {
		this.emp_state = emp_state;
	}
	public String getEmp_zipcode() {
		return emp_zipcode;
	}
	public void setEmp_zipcode(String emp_zipcode) {
		this.emp_zipcode = emp_zipcode;
	}
	public String getEmp_type() {
		return emp_type;
	}
	public void setEmp_type(String emp_type) {
		this.emp_type = emp_type;
	}
	public String getEmp_country() {
		return emp_country;
	}
	public void setEmp_country(String emp_country) {
		this.emp_country = emp_country;
	}
	public String getEmp_join_date() {
		return emp_join_date;
	}
	public void setEmp_join_date(String emp_join_date) {
		this.emp_join_date = emp_join_date;
	}
}
