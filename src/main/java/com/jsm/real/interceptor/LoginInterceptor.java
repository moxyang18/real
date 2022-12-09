package com.jsm.real.interceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class LoginInterceptor implements HandlerInterceptor {
	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
		if(request.getSession().getAttribute("uid") == null) {
			//response.sendRedirect("welcomePage");
			response.sendRedirect(request.getContextPath() + "login");
			//response.sendRedirect("/");
			return false;
		} 
		return true;
	}
}
