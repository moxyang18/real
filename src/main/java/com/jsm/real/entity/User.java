package com.jsm.real.entity;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;import javax.persistence.Id;import javax.persistence.MappedSuperclass;
import javax.persistence.Table;

@Entity
@Table(name="jsm_user")
public class User {
	@Id@GeneratedValue(strategy = GenerationType.IDENTITY) private Long uid;
	private String fname;
	private String lname;
	private Character user_type;
	private Long phone_no;
	private String email;
	private String username;
	private String pwd;
	private String salt;
	// ACCOUNT STATUS: 'ACTIVE', 'FROZEN' IF DEACTIVATED, 'HOLDER' IF IS A NOT REGISTERED AUTHROR
	private String accnt_status;
	public String getAccnt_status() {
		return accnt_status;
	}
	public void setAccnt_status(String accnt_status) {
		this.accnt_status = accnt_status;
	}
	public String getSalt() {
		return salt;
	}
	public void setSalt(String salt) {
		this.salt = salt;
	}
	public Long getUid() {
		return uid;
	}
	public void setUid(Long uid) {
		this.uid = uid;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPwd() {
		return pwd;
	}
	public void setPwd(String pwd) {
		this.pwd = pwd;
	}
	public String getFname() {
		return fname;
	}
	public void setFname(String fname) {
		this.fname = fname;
	}
	public String getLname() {
		return lname;
	}
	public void setLname(String lname) {
		this.lname = lname;
	}
	public Character getUser_type() {
		return user_type;
	}
	public void setUser_type(Character user_type) {
		this.user_type = user_type;
	}
	public Long getPhone_no() {
		return phone_no;
	}
	public void setPhone_no(Long phone_no) {
		this.phone_no = phone_no;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}	
}
