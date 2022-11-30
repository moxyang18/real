package com.jsm.real.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.jsm.real.interceptor.LoginInterceptor;

@Configuration
public class LoginInterceptorConfigurer implements WebMvcConfigurer {
	@Autowired
	LoginInterceptor interceptor;
	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		// whitelist to exclude from interception
		List<String> whiteList = new ArrayList<>();
		whiteList.add("/regCustomer");
		whiteList.add("/loginCustomer");
		whiteList.add("/loginAdmin");
		whiteList.add("/regAuthor");
		whiteList.add("/loginAuthor");
		whiteList.add("/loginEmployee");
		whiteList.add("/welcomePage");
//		whiteList.add("/");
		whiteList.add("/index");
		whiteList.add("/toCustLoginPage");
		whiteList.add("/toAuthLoginPage");
		whiteList.add("/author/loginPage");
		whiteList.add("/login");
		whiteList.add("/toCustRegPage");
		whiteList.add("/toCustRegPage");
		whiteList.add("/toAuthRegPage");
		whiteList.add("/regAuthor");
		whiteList.add("/regCustomer");
		whiteList.add("author/registerPage");
		whiteList.add("customer/registerPage");
		whiteList.add("customer/loginPage");
		whiteList.add("author/loginPage");
		whiteList.add("/toEmpLoginPage");
		
		// add interceptor that intercepts all subpaths with no login state
		registry.addInterceptor(interceptor).addPathPatterns("/**").excludePathPatterns(whiteList);
	}
}
