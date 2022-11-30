package com.jsm.real.service;

import java.util.List;

import com.jsm.real.entity.Employee;

public interface EmployeeService {
	// register an employee
	void registerEmp(Employee employee);
	
	// add or update an employee
	void saveEmployee(Employee employee);
	
	// delete an employee
	void delEmployee(Employee employee);
	
	// query employee info based on condition
	List<Employee> queryEmployee(Employee employee);
	
	// check whether user already taken
	List<Employee> queryByUsername(String empUsername);
	
	void activateAccount(Employee emp);
	void deactivateAccount(Employee emp);
	List<Employee> findEmpById(Employee emp);
}
