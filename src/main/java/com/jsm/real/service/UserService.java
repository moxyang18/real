package com.jsm.real.service;

import java.util.List;

import com.jsm.real.entity.Author;
import com.jsm.real.entity.Customer;
import com.jsm.real.entity.User;

public interface UserService {
	Long saveUser(User user);
	// add or update a user that is an author
	void saveAuthor(Author author);
	// add or update a user that is a customer
	void saveCustomer(Customer cust);
	
	// check whether the account is active
	boolean isActiveAccount(User user);
	
	// find user by username
	List<User> findUserByUsername(User user);
	// find user by id
	public List<User> findUserByUid(User user);
	
	// fetch all users
	List<User> fetchAllUsers();
	// query all system users
	List<User> fetchAllSystemUsers();
	// query users based on input
	List<User> queryUserList(User user);
	// query customers based on input
	List<Customer> queryCustList(Customer cust);
	// query authors based on input
	List<Author> queryAuthList(Author author);
	
	// query authors based on input, joined with users
	void queryAuthorList(Author author, String fName, String lName, List<User> userParts, List<Author> authParts);
	
	// activate or deactivate a user account (for admin use only)
	void activateAccount(User user);
	void deactivateAccount(User user);
	
	// exists such user
	boolean existsAuthor(Long uid);
	
	// query customers based on input, joined with users
	void queryCustomerList(Customer customer, String fName, String lName, List<User> userParts, List<Customer> custParts);
}
