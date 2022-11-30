package com.jsm.real.controller;

import javax.servlet.http.HttpSession;

public class BaseController {
	// Get the currently logged-in user's id
	protected final Long getUidFromSession(HttpSession session) {
		return Long.valueOf(session.getAttribute("uid").toString());
	}
	// Get the user's username from the session
	protected final String getUnameFromSession(HttpSession session) {
		return session.getAttribute("username").toString();
	}
	// Get the user's role_type from the session
	protected final String getRoleFromSession(HttpSession session) {
		// CUSTOMER, AUTHOR, LIBRARIAN, ADMIN
		return session.getAttribute("role").toString();
	}
	
}
