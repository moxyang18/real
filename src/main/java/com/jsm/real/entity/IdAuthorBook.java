package com.jsm.real.entity;

import java.io.Serializable;

public class IdAuthorBook implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 3345318939668700536L;
	private Long bid;
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
