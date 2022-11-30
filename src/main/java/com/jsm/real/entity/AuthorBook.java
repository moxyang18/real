package com.jsm.real.entity;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;
import javax.persistence.Table;

@Entity
@IdClass(IdAuthorBook.class)
@Table(name="jsm_author_book")
public class AuthorBook {
	@Id 
	private Long bid;
	@Id 
	private Long UID;
	public Long getBid() {
		return bid;
	}
	public void setBid(Long bid) {
		this.bid = bid;
	}
	public Long getUID() {
		return UID;
	}
	public void setUID(Long uID) {
		UID = uID;
	}
}
