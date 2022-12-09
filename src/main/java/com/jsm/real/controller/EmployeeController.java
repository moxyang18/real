package com.jsm.real.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jsm.real.util.*;
import com.jsm.real.entity.Author;
import com.jsm.real.entity.Customer;
import com.jsm.real.entity.Employee;
import com.jsm.real.entity.User;
import com.jsm.real.service.EmployeeService;

@Controller
public class EmployeeController extends BaseController {
	
	@Autowired 
	EmployeeService employeeService;
	
	// register for employee
	@RequestMapping("/regEmployee")
	public String register(Model model, Employee employee) {
		// sanitize all info 
		employee.setEmp_id(null); // auto-increment field
		employee.setEmp_uname(StringUtils.sqlSanitize(employee.getEmp_uname()));
		employee.setEmp_city(StringUtils.sqlSanitize(employee.getEmp_city()));
		employee.setEmp_country(StringUtils.sqlSanitize(employee.getEmp_country()));
		employee.setEmp_email(StringUtils.sqlSanitize(employee.getEmp_email()));
		employee.setEmp_fname(StringUtils.sqlSanitize(employee.getEmp_fname()));
		employee.setEmp_Lname(StringUtils.sqlSanitize(employee.getEmp_Lname()));
		// check whether legal 
		// employee.setEmp_pwd(null);
		employee.setEmp_state(StringUtils.sqlSanitize(employee.getEmp_state()));
		employee.setEmp_status("ACTIVE");
		employee.setEmp_stree(StringUtils.sqlSanitize(employee.getEmp_stree()));
		employee.setEmp_type(StringUtils.sqlSanitize(employee.getEmp_type()));
		employee.setEmp_zipcode(StringUtils.sqlSanitize(employee.getEmp_zipcode()));
		// and re-populate to view
		model.addAttribute("emp_city", employee.getEmp_city());
		model.addAttribute("emp_country", employee.getEmp_country());
		model.addAttribute("emp_dob", employee.getEmp_dob());
		model.addAttribute("emp_email", employee.getEmp_email());
		model.addAttribute("emp_fname", employee.getEmp_fname());
		model.addAttribute("emp_Lname", employee.getEmp_Lname());
		model.addAttribute("emp_phone_no", employee.getEmp_phone_no());
		model.addAttribute("emp_pwd", employee.getEmp_pwd());
		model.addAttribute("emp_state", employee.getEmp_state());
		model.addAttribute("emp_status", employee.getEmp_status());
		model.addAttribute("emp_stree", employee.getEmp_stree());
		model.addAttribute("emp_type", employee.getEmp_type());
		model.addAttribute("emp_uname", employee.getEmp_uname());
		model.addAttribute("emp_zipcode", employee.getEmp_zipcode());
		model.addAttribute("empList", employeeService.queryEmployee(new Employee()));
		// register needs username, password, and email address, checked validity in controller
		// generate salt, and encrypt password to store
		
		// prompt the users to modify illegal user name and passwords for SQL injection
		if(StringUtils.containsUnsafe(employee.getEmp_uname())||StringUtils.containsUnsafe(employee.getEmp_pwd())) {
			model.addAttribute("submit_res", "Username or password contains illegal characters! ");
			return "admin/manageEmployee";	
		}
		
		// if username is short,
		if(employee.getEmp_uname()==null||employee.getEmp_uname().trim().length()<5) {
			model.addAttribute("submit_res", "Username must be at least 5 characters! ");
			return "admin/manageEmployee";				
		}
		// if username is taken,
		List<Employee> queried = employeeService.queryByUsername(employee.getEmp_uname());
		if(queried!=null&&queried.size()>0) {
			model.addAttribute("submit_res", "Username already taken! ");
			return "admin/manageEmployee";
		}
		// otherwise username valid
		// check password is at least 8 digits
		if(employee.getEmp_pwd()==null||employee.getEmp_pwd().trim().length()<8) {
			model.addAttribute("submit_res", "Password must be at least 8 characters! ");
			return "admin/manageEmployee";			
		}
		// now generate the salt
		String salt = UUID.randomUUID().toString().toUpperCase();
		// generate the Md5 encrypted password
		String md5Pwd = StringUtils.getMd5String(employee.getEmp_pwd().trim(),salt);
		employee.setEmp_salt(salt);
		employee.setEmp_pwd(md5Pwd);
		employeeService.saveEmployee(employee);
		model.addAttribute("submit_res", "Employee account successfully created! ");
		model.addAttribute("empList", employeeService.queryEmployee(new Employee()));
		return "admin/manageEmployee";
	}
	
	// employee list
	@RequestMapping("/employeesList")	
	public String employeesList(Model model) {
		model.addAttribute("empList", employeeService.queryEmployee(new Employee()));
		return "admin/manageEmployee";
	}
	
	@RequestMapping("/toAdminHome")	
	public String toAdminHome() {
		return "admin/home";
	}	
	
	// query employee list based on conditions
	@RequestMapping("/queryEmployees")	
	public String queryEmployees(Model model, Employee employee) {
		// sanitize all info 
		employee.setEmp_uname(StringUtils.sqlSanitize(employee.getEmp_uname()));
		employee.setEmp_city(StringUtils.sqlSanitize(employee.getEmp_city()));
		employee.setEmp_country(StringUtils.sqlSanitize(employee.getEmp_country()));
		employee.setEmp_email(StringUtils.sqlSanitize(employee.getEmp_email()));
		employee.setEmp_fname(StringUtils.sqlSanitize(employee.getEmp_fname()));
		employee.setEmp_Lname(StringUtils.sqlSanitize(employee.getEmp_Lname()));
		// check whether legal 
		// employee.setEmp_pwd(null);
		employee.setEmp_state(StringUtils.sqlSanitize(employee.getEmp_state()));
		employee.setEmp_stree(StringUtils.sqlSanitize(employee.getEmp_stree()));
		employee.setEmp_type(StringUtils.sqlSanitize(employee.getEmp_type()));
		employee.setEmp_zipcode(StringUtils.sqlSanitize(employee.getEmp_zipcode()));
		model.addAttribute("empList", employeeService.queryEmployee(employee));
		return "admin/manageEmployee";
	}	
	
	@RequestMapping("/loginEmployee")
	public String loginEmployee(Model model, @RequestParam(name="username", required=true) String username,  @RequestParam(name="pwd", required=true) String pwd,  HttpSession session) {	
		Employee emp = new Employee();
		emp.setEmp_pwd((pwd));
		emp.setEmp_uname((username));
		// sanitize input 
		emp.setEmp_uname(StringUtils.sqlSanitize(emp.getEmp_uname()));
		emp.setEmp_pwd(StringUtils.sqlSanitize(emp.getEmp_pwd()));
		// re-populate the fields back to model
		// user fields
		model.addAttribute("username", emp.getEmp_uname());		
		model.addAttribute("pwd", emp.getEmp_pwd());
		// query the record by user user name
		// if exists
		List<Employee> loginInfo = employeeService.queryByUsername(emp.getEmp_uname());
		if(loginInfo==null||loginInfo.size()==0) {
			model.addAttribute("submit_res", "The Employee Username or Password Incorrect! ");
			return "employee/empLoginPage";		
		}
		Employee storedData = loginInfo.get(0);
		//  check the password matches the entered password encrypted by Md5. authentication passed!
		if(!storedData.getEmp_pwd().equals(StringUtils.getMd5String(emp.getEmp_pwd(), storedData.getEmp_salt()))) {
			model.addAttribute("submit_res", "The Employee Username or Password Incorrect! ");
			return "employee/empLoginPage";		
		}
		// if not active account status, cannot login
		if(storedData.getEmp_status()==null||!storedData.getEmp_status().equals("ACTIVE")) {
			model.addAttribute("submit_res", "Emp Account is not active! Unable to login. ");
			return "employee/empLoginPage";				
		}
		// upon successful login, add uid/username/role to session
		session.setAttribute("uid", storedData.getEmp_id() );
		session.setAttribute("username", storedData.getEmp_uname());
		
		// if the account is for administrator
		if(storedData.getEmp_type().equals("ADMIN")) {
			session.setAttribute("role", "ADMIN");
			return "admin/home";
		}
		// otherwise the account is for other employees
		if(storedData.getEmp_type().equals("STAFF")) {
			session.setAttribute("role", "STAFF");
			return "employee/empHome";
		}
		session.invalidate();
		// otherwise invalid
		model.addAttribute("submit_res", "Employee type void. Cannot login! ");
		return "login";
	}
	
	@RequestMapping("/toEmpLoginPage")
	public String toEmpLoginPage() {
		return "employee/empLoginPage";
	}
	
	@RequestMapping("/adminHome") 
	public String adminHome() {
		return "admin/home"; 
	}
	
	@RequestMapping("/empHome") 
	public String empHome() {
		return "employee/empHome"; 
	}
	
	@RequestMapping("/activateEmpAccnt")
	public String activateEmpAccnt(Model model, Employee employee) {
		// activate the employee account thru the employee id or username
		// sanitize only these two fields
		employee.setEmp_uname(StringUtils.sqlSanitize(employee.getEmp_uname()));
		
		employeeService.activateAccount(employee);
		model.addAttribute("empList", employeeService.queryEmployee(new Employee()));
		return "admin/manageEmployee";
	}
	
	@RequestMapping("/freezeEmpAccnt")
	public String freezeEmpAccnt(Model model, Employee employee) {
		// deactivate the employee account thru the employee id or username
		// sanitize only these two fields
		employee.setEmp_uname(StringUtils.sqlSanitize(employee.getEmp_uname()));
		
		employeeService.deactivateAccount(employee);
		model.addAttribute("empList", employeeService.queryEmployee(new Employee()));
		return "admin/manageEmployee";
	}
	
	@RequestMapping("/empProfile")
	public String empProfile(Model model, HttpSession session) {
		Employee emp = new Employee();
		emp.setEmp_id(this.getUidFromSession(session));
		List<Employee> qryList = employeeService.queryEmployee(emp);
		if(qryList.size()==0) {
			return "employee/profilePage";
		}
		model.addAttribute("employee", qryList.get(0));
		return "employee/profilePage";
	}
	
	@RequestMapping("/uptEmpProfile")
	public String uptEmpProfile(Model model, Employee emp, HttpSession session) {
		// sanitize input first
		emp.setEmp_city(StringUtils.sqlSanitize(emp.getEmp_city()));
		emp.setEmp_country(StringUtils.sqlSanitize(emp.getEmp_country()));
		emp.setEmp_dob(StringUtils.sqlSanitize(emp.getEmp_dob()));
		emp.setEmp_email(StringUtils.sqlSanitize(emp.getEmp_email()));
		emp.setEmp_fname(StringUtils.sqlSanitize(emp.getEmp_fname()));
		emp.setEmp_join_date(StringUtils.sqlSanitize(emp.getEmp_join_date()));
		emp.setEmp_Lname(StringUtils.sqlSanitize(emp.getEmp_Lname()));
		emp.setEmp_state(StringUtils.sqlSanitize(emp.getEmp_state()));
		emp.setEmp_stree(StringUtils.sqlSanitize(emp.getEmp_stree()));
		emp.setEmp_uname(StringUtils.sqlSanitize(emp.getEmp_uname()));
		emp.setEmp_zipcode(StringUtils.sqlSanitize(emp.getEmp_zipcode()));
		// re-populate the fields back to model
		// user fields
		model.addAttribute("emp_city", emp.getEmp_city());
		model.addAttribute("emp_country", emp.getEmp_country());
		model.addAttribute("emp_dob", emp.getEmp_dob()  );
		model.addAttribute("emp_email", emp.getEmp_email());
		model.addAttribute("emp_fname", emp.getEmp_fname());
		model.addAttribute("emp_id", emp.getEmp_id());
		model.addAttribute("emp_join_date", emp.getEmp_join_date());
		model.addAttribute("emp_Lname", emp.getEmp_Lname());
		model.addAttribute("emp_phone_no", emp.getEmp_phone_no());
		model.addAttribute("emp_pwd", emp.getEmp_pwd());
		model.addAttribute("emp_salt", emp.getEmp_salt());
		model.addAttribute("emp_state", emp.getEmp_state());
		model.addAttribute("emp_status", emp.getEmp_status());
		model.addAttribute("emp_stree", emp.getEmp_stree());
		model.addAttribute("emp_type", emp.getEmp_type());
		model.addAttribute("emp_uname", emp.getEmp_uname());
		model.addAttribute("emp_zipcode", emp.getEmp_zipcode());

		emp.setEmp_id(this.getUidFromSession(session));

		employeeService.saveEmployee(emp);
		model.addAttribute("submit_res", "Update Successful! ");
		
		List<Employee> qryList = employeeService.queryEmployee(emp);
		if(qryList.size()==0) {
			return "employee/profilePage";
		}
		model.addAttribute("employee", qryList.get(0));
		return "employee/profilePage";
	}	
	
	
	
	@RequestMapping("/uptEmpUsername")
	public String uptEmpUsername(Model model, @RequestParam(name="USERNAME", required=true) String username, HttpSession session) {
		String outPage = "employee/profilePage";
		Employee empA = new Employee();
		empA.setEmp_id(this.getUidFromSession(session));
		List<Employee> qryList = employeeService.queryEmployee(empA);
		if(qryList.size()==0) {
			return "employee/profilePage";
		}
		model.addAttribute("employee", qryList.get(0));
		// if the new user name is less than 5 characters, unable to change
		if(username.length()<5) {
			model.addAttribute("submit_res_upper", "Username must be at least 5 characters! ");
			return outPage;				
		}
		// if the new user name contains illegal characters 
		if(StringUtils.containsUnsafe(username)) {
			model.addAttribute("submit_res_upper", "Username contains illegal characters! ");
			return outPage;	
		}
		// if the new user name exists, unable to change
		Employee emp = new Employee();
		emp.setEmp_uname(username);
		List<Employee> queried = employeeService.queryByUsername(username);
		if(queried!=null&&queried.size()>0) {
			model.addAttribute("submit_res_upper", "Username already taken! ");
			return outPage;	
		}		
		// otherwise can change user name
		// fetch original user info, and set the user name field only
		Employee oldEmp = employeeService.queryByUsername(this.getUnameFromSession(session)).get(0);
		oldEmp.setEmp_uname(username);
		employeeService.saveEmployee(oldEmp);
		model.addAttribute("submit_res_upper", "Username Successfully Updated!");
		// set new username back to session
		session.setAttribute("username", username);
		empA = new Employee();
		empA.setEmp_id(this.getUidFromSession(session));
		qryList = employeeService.queryEmployee(empA);
		if(qryList.size()==0) {
			return "employee/profilePage";
		}
		model.addAttribute("employee", qryList.get(0));
		return outPage;		
	}
	
	@RequestMapping("/employeeBenefits")
	public String employeeBenefits() {
		return "empBenefitPage";
	}
	
	@RequestMapping("/uptEmpUserpwd")
	public String uptEmpUserpwd(Model model, @RequestParam(name="OLDPWD", required=true) String oldPwd, @RequestParam(name="PWD", required=true) String pwd, HttpSession session) {
		String outPage = "employee/profilePage";
		Employee empA = new Employee();
		empA.setEmp_id(this.getUidFromSession(session));
		List<Employee> qryList = employeeService.queryEmployee(empA);
		if(qryList.size()==0) {
			return "employee/profilePage";
		}
		model.addAttribute("employee", qryList.get(0));
		
		
		// if the new password is less than 8 characters, unable to change
		if(pwd.length()<8) {
			model.addAttribute("submit_res_upper", "Password must be at least 8 characters! ");
			return outPage;				
		}
		// if the new password contains illegal characters 
		if(StringUtils.containsUnsafe(pwd)) {
			model.addAttribute("submit_res_upper", "Password contains illegal characters! ");
			return outPage;	
		}
		// if the oldPwd does not match the user info, unable to change
		Employee storedData = employeeService.queryByUsername(this.getUnameFromSession(session)).get(0);
		if(!storedData.getEmp_pwd().equals(StringUtils.getMd5String(oldPwd, storedData.getEmp_salt()))) {
			model.addAttribute("submit_res_upper", "Old Password was entered incorrectly! ");
			return outPage;	
		}	
		// otherwise can change user password
		// fetch original user info, and set the user name field only
		storedData.setEmp_pwd(StringUtils.getMd5String(pwd, storedData.getEmp_salt()));
		employeeService.saveEmployee(storedData);
		model.addAttribute("submit_res_upper", "Password Successfully Updated!");
		
		empA = new Employee();
		empA.setEmp_id(this.getUidFromSession(session));
		qryList = employeeService.queryEmployee(empA);
		if(qryList.size()==0) {
			return "employee/profilePage";
		}
		model.addAttribute("employee", qryList.get(0));
		return "employee/profilePage";
	}
}
