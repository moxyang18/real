package com.jsm.real.service.impl;

import java.util.List;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.jsm.real.dao.EmployeeDao;
import com.jsm.real.entity.Employee;
import com.jsm.real.entity.User;
import com.jsm.real.service.EmployeeService;

@Service
public class EmployeeServiceImpl implements EmployeeService {
	@Autowired
	EmployeeDao employeeDao;
	private SessionFactory sf;
	Configuration cfg = new Configuration()
			.addAnnotatedClass(Employee.class)
			.setProperty("hibernate.dialect", "org.hibernate.dialect.MySQL5InnoDBDialect")
			.setProperty("hibernate.connection.url", "jdbc:mysql://localhost:3306/real")
			.setProperty("hibernate.connection.username", "root")   
	        .setProperty("hibernate.connection.password", "1234")
	        .setProperty("hibernate.hbm2ddl.auto", "update")
	        .setProperty("hibernate.show_sql", "true")
	        .setProperty("hibernate.connection.pool_size", "10"); 
	@Override
	public void saveEmployee(Employee employee) {
		employeeDao.save(employee);
	}

	@Override
	public void delEmployee(Employee employee) {
		employeeDao.delete(employee);
	}

	@Override
	public List<Employee> queryEmployee(Employee employee) {
		sf = cfg.buildSessionFactory();	
		
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Employee where ");
		if(employee.getEmp_id()!=null) {
			sb.append("emp_id = " + employee.getEmp_id() + " and ");
		}
		if(employee.getEmp_uname() !=null&&employee.getEmp_uname().trim()!="") {
			sb.append("emp_uname LIKE '" + employee.getEmp_uname().trim() + "%' and ");
		}
		if(employee.getEmp_stree() !=null&&employee.getEmp_stree().trim()!="") {
			sb.append("emp_stree LIKE '" + employee.getEmp_stree().trim() + "%' and ");
		}
		if(employee.getEmp_city() !=null&&employee.getEmp_city().trim()!="") {
			sb.append("emp_city LIKE '" + employee.getEmp_city().trim() + "%' and ");
		}
		
		if(employee.getEmp_state() !=null&&employee.getEmp_state().trim()!="") {
			sb.append("emp_state LIKE '" + employee.getEmp_state().trim() + "%' and ");
		}
		
		if(employee.getEmp_zipcode() !=null&&employee.getEmp_zipcode().trim()!="") {
			sb.append("emp_zipcode LIKE '" + employee.getEmp_zipcode().trim() + "%' and ");
		}		

		if(employee.getEmp_type() !=null&&employee.getEmp_type().trim()!="") {
			sb.append("emp_type LIKE '" + employee.getEmp_type().trim() + "%' and ");
		}	
		
		if(employee.getEmp_fname() !=null&&employee.getEmp_fname().trim()!="") {
			sb.append("emp_fname LIKE '" + employee.getEmp_fname().trim() + "%' and ");
		}		
		
		if(employee.getEmp_Lname() !=null&&employee.getEmp_Lname().trim()!="") {
			sb.append("emp_Lname LIKE '" + employee.getEmp_Lname().trim() + "%' and ");
		}	
		
		if(employee.getEmp_dob() !=null&&employee.getEmp_dob().trim()!="") {
			sb.append("emp_dob = '" + employee.getEmp_dob() + "' and ");
		}	

		if(employee.getEmp_join_date()!=null&&employee.getEmp_join_date()!="") {
			sb.append("emp_join_date = '" + employee.getEmp_join_date() + "' and ");
		}
		
		if(employee.getEmp_status()!=null&&employee.getEmp_status().trim()!="") {
			sb.append("emp_status LIKE '" + employee.getEmp_status() + "%' and ");
		}
		if(employee.getEmp_phone_no()!=null) {
			sb.append("emp_phone_no =" + employee.getEmp_phone_no()+" and ");
		}
		if(employee.getEmp_email()!=null&&employee.getEmp_email().trim()!="") {
			sb.append("emp_status LIKE '" + employee.getEmp_email().trim() + "%' and ");
		}
		sb.append("1 = 1");
		List<Employee> res = session.createQuery(sb.toString(), Employee.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}

	@Override
	public List<Employee> queryByUsername(String empUsername) {
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		sb.append("from Employee where emp_uname = '" + empUsername +"' ");
		List<Employee> res = session.createQuery(sb.toString(), Employee.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}

	
	@Override
	public void activateAccount(Employee emp) {
		List<Employee> res = null;
		if(emp.getEmp_id()!=null) {
			res = this.findEmpById(emp);
		} else if(emp.getEmp_uname()!=null) {
			res = this.queryByUsername(emp.getEmp_uname());			
		}
		emp = res.get(0);
		emp.setEmp_status("ACTIVE");
		employeeDao.save(emp);
	}

	@Override
	public void deactivateAccount(Employee emp) {
		List<Employee> res = null;
		if(emp.getEmp_id()!=null) {
			res = this.findEmpById(emp);
		} else if(emp.getEmp_uname()!=null) {
			res = this.queryByUsername(emp.getEmp_uname());			
		}
		emp = res.get(0);
		emp.setEmp_status("FROZEN");
		employeeDao.save(emp);		
	}

	@Override
	public List<Employee> findEmpById(Employee emp) {
		sf = cfg.buildSessionFactory();	
		Session session = sf.openSession();
		Transaction tx = session.beginTransaction();
		StringBuilder sb = new StringBuilder();
		Long uid = (long) -1;
		if(emp.getEmp_id()!=null) {
			uid = emp.getEmp_id();
		}
		sb.append("from Employee where emp_id = " + uid +" ");
		List<Employee> res = session.createQuery(sb.toString(), Employee.class).getResultList();
		tx.commit();
		session.close();
		return res;
	}	
	
	
	
	@Override
	public void registerEmp(Employee employee) {
		// TODO Auto-generated method stub
		
	}
}
