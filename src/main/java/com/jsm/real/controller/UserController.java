package com.jsm.real.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jsm.real.entity.Author;
import com.jsm.real.entity.Customer;
import com.jsm.real.entity.User;
import com.jsm.real.service.UserService;
import com.jsm.real.util.StringUtils;

@Controller
public class UserController extends BaseController {
	@Autowired
	private UserService userService;
	@Transactional
	@RequestMapping("/regCustomer")
	public String registerCustomer(Model model, User user, Customer cust) {
		// sanitize input first
		user.setEmail(StringUtils.sqlSanitize(user.getEmail()));
		user.setFname(StringUtils.sqlSanitize(user.getFname()));
		user.setLname(StringUtils.sqlSanitize(user.getLname()));
		user.setUsername(StringUtils.sqlSanitize(user.getUsername().trim()));
		cust.setId_no(StringUtils.sqlSanitize(cust.getId_no()));
		// re-populate the fields back to model
		// user fields
		model.addAttribute("username", user.getUsername());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("fname", user.getFname());
		model.addAttribute("lname", user.getLname());
		model.addAttribute("accnt_status", user.getAccnt_status());
		model.addAttribute("phone_no", user.getPhone_no());
		model.addAttribute("pwd", user.getPwd());
		// customer fields
		model.addAttribute("id_type", cust.getId_type());
		model.addAttribute("id_no", cust.getId_no());	
			
		// register needs username, password, and email address, checked validity in controller
		// generate salt, and encrypt password to store
		// if username is short,
		if(user.getUsername().length()<5) {
			model.addAttribute("submit_res", "Username must be at least 5 characters! ");
			return "customer/registerPage";				
		}
		// prompt the users to modify illegal username and passwords for sql injection
		if(StringUtils.containsUnsafe(user.getUsername())||StringUtils.containsUnsafe(user.getPwd())) {
			model.addAttribute("submit_res", "Username or password contains illegal characters! ");
			return "customer/registerPage";	
		}
		// if username is taken, prompt error
		List<User> queried = userService.findUserByUsername(user);
		if(queried!=null&&queried.size()>0) {
			model.addAttribute("submit_res", "Username already taken! ");
			return "customer/registerPage";	
		}
		// otherwise username valid
		// check password is at least 8 digits
		if(user.getPwd()==null||user.getPwd().length()<8) {
			model.addAttribute("submit_res", "Password must be at least 8 characters! ");
			return "customer/registerPage";			
		}
		// now generate the salt
		String salt = UUID.randomUUID().toString().toUpperCase();
		// generate the Md5 encrypted password
		String md5Pwd = StringUtils.getMd5String(user.getPwd().trim(),salt);
		user.setSalt(salt);
		user.setPwd(md5Pwd);
		// set account status to be ACTIVE
		user.setAccnt_status("ACTIVE");
		user.setUser_type('C');
		// generate the Md5 encrypted id number
		cust.setId_no(StringUtils.getMd5String(cust.getId_no().trim(),salt));
		Long uid = userService.saveUser(user);
		cust.setUid(uid);
		userService.saveCustomer(cust);	
		model.addAttribute("submit_res", "Registration Successful! ");
		model.addAttribute("username", "");
		model.addAttribute("pwd", "");
		return "customer/loginPage";	
	}
	
	@RequestMapping("/loginCustomer")
	public String loginCustomer(Model model,  @RequestParam(name="username", required=true) String username,  @RequestParam(name="pwd", required=true) String pwd, HttpSession session) {
		User user = new User();
		user.setPwd(pwd);
		user.setUsername(username);
		// sanitize input 
		user.setUsername(StringUtils.sqlSanitize(user.getUsername()));
		user.setPwd(StringUtils.sqlSanitize(user.getPwd()));
		// re-populate the fields back to model
		// user fields
		model.addAttribute("username", user.getUsername());		
		model.addAttribute("pwd", user.getPwd());
		// query the record by user user name
		// if exists, and the password matches the entered password encrypted by Md5. authentication passed!
		List<User> loginInfo = userService.findUserByUsername(user);
		if(loginInfo==null||loginInfo.size()==0) {
			model.addAttribute("submit_res", "The Username or Password Incorrect! ");
			return "customer/loginPage";		
		}
		User storedData = loginInfo.get(0);
		
		// if the account is not active, cannot login
		if(storedData.getAccnt_status()==null||!storedData.getAccnt_status().equals("ACTIVE")) {
			model.addAttribute("submit_res", "The Account is not Active! Unable to Login!");
			return "customer/loginPage";
		}
		
		// if the account type is not a customer, cannot login either
		if(storedData.getUser_type()!='C'||!storedData.getPwd().equals(StringUtils.getMd5String(user.getPwd(), storedData.getSalt()))) {
			model.addAttribute("submit_res", "The Username or Password Incorrect! ");
			return "customer/loginPage";		
		}
		// upon successful login, add uid/username/role to session
		session.setAttribute("uid", storedData.getUid());
		session.setAttribute("username", storedData.getUsername());
		session.setAttribute("role", "CUSTOMER");
		// otherwise logged in	
		return "customer/home";
	}	
	
	// this author registered in REAL, and can view the seminar he's invited in
	@RequestMapping("/regAuthor")
	public String registerAuth(Model model, User user, Author auth) {
		// sanitize input first
		user.setEmail(StringUtils.sqlSanitize(user.getEmail()));
		user.setFname(StringUtils.sqlSanitize(user.getFname()));
		user.setLname(StringUtils.sqlSanitize(user.getLname()));
		user.setUsername(StringUtils.sqlSanitize(user.getUsername().trim()));
		auth.setCity(StringUtils.sqlSanitize(auth.getCity()));
		auth.setCountry(StringUtils.sqlSanitize(auth.getCountry()));
		auth.setPen_name(StringUtils.sqlSanitize(auth.getPen_name()));
		auth.setState(StringUtils.sqlSanitize(auth.getState()));
		auth.setStreet(StringUtils.sqlSanitize(auth.getStreet()));
		auth.setZipcode(StringUtils.sqlSanitize(auth.getZipcode()));
		// re-populate the fields back to model
		// user fields
		model.addAttribute("username", user.getUsername());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("fname", user.getFname());
		model.addAttribute("lname", user.getLname());
		model.addAttribute("accnt_status", user.getAccnt_status());
		model.addAttribute("phone_no", user.getPhone_no());
		model.addAttribute("pwd", user.getPwd());
		// author fields
		model.addAttribute("city", auth.getCity());
		model.addAttribute("country", auth.getCountry());
		model.addAttribute("pen_name", auth.getPen_name());
		model.addAttribute("state", auth.getState());		
		model.addAttribute("street", auth.getStreet());
		model.addAttribute("zipcode", auth.getZipcode());			
		// register needs username, password, and email address, checked validity in controller
		// generate salt, and encrypt password to store
		// if username is short,
		if(user.getUsername().length()<5) {
			model.addAttribute("submit_res", "Username must be at least 5 characters! ");
			return "author/registerPage";				
		}
		// prompt the users to modify illegal username and passwords for sql injection
		if(StringUtils.containsUnsafe(user.getUsername())||StringUtils.containsUnsafe(user.getPwd())) {
			model.addAttribute("submit_res", "Username or password contains illegal characters! ");
			return "author/registerPage";	
		}
		// if username is taken, prompt error
		List<User> queried = userService.findUserByUsername(user);
		if(queried!=null&&queried.size()>0) {
			model.addAttribute("submit_res", "Username already taken! ");
			return "author/registerPage";	
		}
		// otherwise username valid
		// check password is at least 8 digits
		if(user.getPwd()==null||user.getPwd().length()<8) {
			model.addAttribute("submit_res", "Password must be at least 8 characters! ");
			return "author/registerPage";			
		}
		// now generate the salt
		String salt = UUID.randomUUID().toString().toUpperCase();
		// generate the Md5 encrypted password
		String md5Pwd = StringUtils.getMd5String(user.getPwd().trim(),salt);
		user.setSalt(salt);
		user.setPwd(md5Pwd);
		// set account status to be ACTIVE
		user.setAccnt_status("ACTIVE");
		user.setUser_type('A');
		Long uid = userService.saveUser(user);
		auth.setUid(uid);
		userService.saveAuthor(auth);
		model.addAttribute("submit_res", "Registration Successful! ");
		model.addAttribute("username", "");
		model.addAttribute("pwd", "");
		return "author/loginPage";		
	}
	
	@RequestMapping("/loginAuthor")
	public String loginAuthor(Model model, @RequestParam(name="username", required=true) String username,  @RequestParam(name="pwd", required=true) String pwd,  HttpSession session) {
		User user = new User();
		user.setPwd(pwd);
		user.setUsername(username);
		// sanitize input 
		user.setUsername(StringUtils.sqlSanitize(user.getUsername()));
		user.setPwd(StringUtils.sqlSanitize(user.getPwd()));
		// re-populate the fields back to model
		// user fields
		model.addAttribute("username", user.getUsername());		
		model.addAttribute("pwd", user.getPwd());
		// query the record by user user name
		// if exists
		List<User> loginInfo = userService.findUserByUsername(user);
		if(loginInfo==null||loginInfo.size()==0) {
			model.addAttribute("submit_res", "The Username or Password Incorrect! ");
			return "author/loginPage";		
		}
		User storedData = loginInfo.get(0);
		// check the user type must be author, and the password matches the entered password encrypted by Md5. authentication passed!
		if(storedData.getUser_type()!='A'||!storedData.getPwd().equals(StringUtils.getMd5String(user.getPwd(), storedData.getSalt()))) {
			model.addAttribute("submit_res", "The Username or Password Incorrect! ");
			return "author/loginPage";		
		}
		// if not active account status, cannot login
		if(storedData.getAccnt_status()==null||!storedData.getAccnt_status().equals("ACTIVE")) {
			model.addAttribute("submit_res", "Account is not active! Unable to login. ");
			return "author/loginPage";				
		}
		
		// upon successful login, add uid/username/role to session
		session.setAttribute("uid", storedData.getUid());
		session.setAttribute("username", storedData.getUsername());
		session.setAttribute("role", "AUTHOR");
		// otherwise logged in	
		return "author/home";
	}

	@RequestMapping("/toCustLoginPage")
	public String toCustLoginPage() {
		return "customer/loginPage";
	}	
	
	@RequestMapping("/toAuthLoginPage")
	public String toAuthLoginPage() {
		return "author/loginPage";
	}	
	
	@RequestMapping("/toCustRegPage")
	public String toCustRegPage() {
		return "customer/registerPage";
	}	
	
	@RequestMapping("/toAuthRegPage")
	public String toAuthRegPage() {
		return "author/registerPage";
	}	
	
	
	@RequestMapping("/authorHome") 
	public String authorHome() {
		return "author/home"; 
	}
	
	@RequestMapping("/customerHome") 
	public String customerHome() {
		return "customer/home"; 
	}
	
	@RequestMapping("/authList") 
	public String authList(Model model, HttpSession session) {
		model.addAttribute("role", this.getRoleFromSession(session));
		List<Author> authParts = new ArrayList<>();
		List<User> userParts = new ArrayList<>();
		userService.queryAuthorList(null, null, null, userParts, authParts); 
		model.addAttribute("userList", userParts);
		model.addAttribute("authList", authParts);
		return "authorsPage";
	}
	
	@RequestMapping("/queryAuthor") 
	public String queryAuthor(Model model, Author auth, User user, HttpSession session) {
		model.addAttribute("role", this.getRoleFromSession(session));
		// sanitize input
		auth.setPen_name(StringUtils.sqlSanitize(auth.getPen_name()));
		user.setFname(StringUtils.sqlSanitize(user.getFname()));
		user.setLname(StringUtils.sqlSanitize(user.getLname()));
		model.addAttribute("role", this.getRoleFromSession(session));
		List<Author> authParts = new ArrayList<>();
		List<User> userParts = new ArrayList<>();
		userService.queryAuthorList(auth, user.getFname(), user.getLname(), userParts, authParts); 
		model.addAttribute("userList", userParts);
		model.addAttribute("authList", authParts);
		return "authorsPage";
	}
	
	// this author is only for book/author info data entry, not a system user
	@RequestMapping("/enterAuthor")
	public String enterAuthor(Model model, User user, Author auth, HttpSession session) {
		List<Author> authParts = new ArrayList<>();
		List<User> userParts = new ArrayList<>();
		userService.queryAuthorList(auth, user.getFname(), user.getLname(), userParts, authParts); 
		model.addAttribute("userList", userParts);
		model.addAttribute("authList", authParts);
		model.addAttribute("role", this.getRoleFromSession(session));
		// many fields are not necessary
		if(user.getFname()==null||user.getFname().trim().equals("")||user.getLname()==null||
				user.getLname().trim().equals("")||auth.getPen_name()==null||auth.getPen_name().trim().equals("")) {
			model.addAttribute("submit_res", "Error! Please specify the author's name and pen name");
			return "authorsPage";
		}
		
		// necessary fields are the author names, must not set username and password
		// sanitize input first
		user.setEmail(StringUtils.sqlSanitize(user.getEmail()));
		user.setFname(StringUtils.sqlSanitize(user.getFname()));
		user.setLname(StringUtils.sqlSanitize(user.getLname()));
		// placeholder for the long value
		user.setPhone_no(Long.valueOf("0000000000"));
		auth.setPen_name(StringUtils.sqlSanitize(auth.getPen_name()));
		auth.setCity(StringUtils.sqlSanitize(auth.getCity()));
		auth.setCountry(StringUtils.sqlSanitize(auth.getCountry()));
		auth.setState(StringUtils.sqlSanitize(auth.getState()));
		auth.setStreet(StringUtils.sqlSanitize(auth.getStreet()));
		auth.setZipcode(StringUtils.sqlSanitize(auth.getZipcode()));
		// set account status to be HOLDER
		user.setUser_type('A');
		user.setAccnt_status("HOLDER");
		Long uid = userService.saveUser(user);
		auth.setUid(uid);
		userService.saveAuthor(auth);
		model.addAttribute("submit_res", "Successfully added an author. ");
		return "authorsPage";
	}
	
	@RequestMapping("/usersList")
	public String usersList(Model model) {
		// sanitize input first
		model.addAttribute("usrList", userService.fetchAllSystemUsers());
		return "admin/manageUser";
	}

	@RequestMapping("/queryUsers")
	public String queryUsers(Model model, User user) {
		// sanitize input first
		model.addAttribute("usrList", userService.queryUserList(user));
		return "admin/manageUser";
	}
	
	
	@RequestMapping("/activateUsrAccnt")
	public String activateUsrAccnt(Model model, User user) {
		// activate the employee account thru the u id or username
		// sanitize only these two fields
		user.setUsername(StringUtils.sqlSanitize(user.getUsername()));
		userService.activateAccount(user);
		model.addAttribute("usrList", userService.fetchAllUsers());
		return "admin/manageUser";
	}
	
	@RequestMapping("/freezeUsrAccnt")
	public String freezeUsrAccnt(Model model, User user) {
		// deactivate the employee account thru the uid or username
		// sanitize only these two fields
		user.setUsername(StringUtils.sqlSanitize(user.getUsername()));
		userService.deactivateAccount(user);
		model.addAttribute("usrList", userService.fetchAllUsers());
		return "admin/manageUser";
	}
	
	@RequestMapping("/editAuthor") 
	public String editAuthor(Model model, User user, Author auth, HttpSession session) {
		model.addAttribute("role", this.getRoleFromSession(session));
		// sanitize input and 
		// many fields are not necessary
		// necessary fields are the author names, must not set username and password
		// sanitize input first
		user.setEmail(StringUtils.sqlSanitize(user.getEmail()));
		user.setFname(StringUtils.sqlSanitize(user.getFname()));
		user.setLname(StringUtils.sqlSanitize(user.getLname()));
		user.setUsername(StringUtils.sqlSanitize(user.getUsername().trim()));
		user.setPwd(null);	
		user.setUsername(null);
		auth.setPen_name(StringUtils.sqlSanitize(auth.getPen_name()));
		return "authorsPage";
	}
	
	@RequestMapping("/authProfile")
	public String authProfile(Model model, HttpSession session) {
		List<Author> authParts = new ArrayList<>();
		List<User> userParts = new ArrayList<>();
		Author author = new Author();
		author.setUid(this.getUidFromSession(session));
		userService.queryAuthorList(author, null, null, userParts, authParts);
		if(authParts.size()==0) {
			return "author/profilePage";
		}
		model.addAttribute("authPart", authParts.get(0));
		model.addAttribute("userPart", userParts.get(0));
		return "author/profilePage";
	}
	@RequestMapping("/uptAuthProfile")
	public String uptAuthProfile(Model model, Author auth, User user, HttpSession session) {
		// sanitize input first
		user.setEmail(StringUtils.sqlSanitize(user.getEmail()));
		user.setFname(StringUtils.sqlSanitize(user.getFname()));
		user.setLname(StringUtils.sqlSanitize(user.getLname()));
		user.setUsername(StringUtils.sqlSanitize(user.getUsername().trim()));
		auth.setCity(StringUtils.sqlSanitize(auth.getCity()));
		auth.setCountry(StringUtils.sqlSanitize(auth.getCountry()));
		auth.setPen_name(StringUtils.sqlSanitize(auth.getPen_name()));
		auth.setState(StringUtils.sqlSanitize(auth.getState()));
		auth.setStreet(StringUtils.sqlSanitize(auth.getStreet()));
		auth.setZipcode(StringUtils.sqlSanitize(auth.getZipcode()));
		// re-populate the fields back to model
		// user fields
		model.addAttribute("username", user.getUsername());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("fname", user.getFname());
		model.addAttribute("lname", user.getLname());
		model.addAttribute("accnt_status", user.getAccnt_status());
		model.addAttribute("phone_no", user.getPhone_no());
		// author fields
		model.addAttribute("city", auth.getCity());
		model.addAttribute("country", auth.getCountry());
		model.addAttribute("pen_name", auth.getPen_name());
		model.addAttribute("state", auth.getState());		
		model.addAttribute("street", auth.getStreet());
		model.addAttribute("zipcode", auth.getZipcode());			

		// set account status to be ACTIVE
		user.setAccnt_status("ACTIVE");
		user.setUser_type('A');
		user.setUid(this.getUidFromSession(session));
		Long uid = userService.saveUser(user);
		auth.setUid(uid);
		userService.saveAuthor(auth);
		model.addAttribute("submit_res", "Profile Update Successful! ");
		
		
		List<Author> authParts = new ArrayList<>();
		List<User> userParts = new ArrayList<>();
		Author author = new Author();
		author.setUid(this.getUidFromSession(session));
		userService.queryAuthorList(author, null, null, userParts, authParts);
		if(authParts.size()==0) {
			return "author/profilePage";
		}
		model.addAttribute("authPart", authParts.get(0));
		model.addAttribute("userPart", userParts.get(0));
		
		
		return "author/profilePage";
	}	
	
	
	@RequestMapping("/custProfile")
	public String custProfile(Model model, HttpSession session) {
		List<Customer> custParts = new ArrayList<>();
		List<User> userParts = new ArrayList<>();
		Customer customer = new Customer();
		customer.setUid(this.getUidFromSession(session));
		userService.queryCustomerList(customer, null, null, userParts, custParts);
		if(custParts.size()==0) {
			return "customer/profilePage";
		}
		model.addAttribute("custPart", custParts.get(0));
		model.addAttribute("userPart", userParts.get(0));
		return "customer/profilePage";
	}
	@RequestMapping("/uptCustProfile")
	public String uptCustProfile(Model model, Customer cust, User user, HttpSession session) {
		// sanitize input first
		user.setEmail(StringUtils.sqlSanitize(user.getEmail()));
		user.setFname(StringUtils.sqlSanitize(user.getFname()));
		user.setLname(StringUtils.sqlSanitize(user.getLname()));
		cust.setId_no(StringUtils.sqlSanitize(cust.getId_no()));
		// re-populate the fields back to model
		// user fields
		model.addAttribute("username", user.getUsername());
		model.addAttribute("email", user.getEmail());
		model.addAttribute("fname", user.getFname());
		model.addAttribute("lname", user.getLname());
		model.addAttribute("accnt_status", user.getAccnt_status());
		model.addAttribute("phone_no", user.getPhone_no());
		// customer fields
		model.addAttribute("id_type", cust.getId_type());
		model.addAttribute("id_no", cust.getId_no());	
		// set account status to be ACTIVE
		user.setAccnt_status("ACTIVE");
		user.setUser_type('C');
		user.setUid(this.getUidFromSession(session));
		Long uid = userService.saveUser(user);
		cust.setUid(uid);
		userService.saveCustomer(cust);	
		model.addAttribute("submit_res", "Update Successful! ");
		
		List<Customer> custParts = new ArrayList<>();
		List<User> userParts = new ArrayList<>();
		Customer customer = new Customer();
		customer.setUid(this.getUidFromSession(session));
		userService.queryCustomerList(customer, null, null, userParts, custParts);
		if(custParts.size()==0) {
			return "customer/profilePage";
		}
		model.addAttribute("custPart", custParts.get(0));
		model.addAttribute("userPart", userParts.get(0));
		
		return "customer/profilePage";
	}	
	
	//change user name
	@RequestMapping("/uptUsername")
	public String uptUsername(Model model, @RequestParam(name="USERNAME", required=true) String username, HttpSession session) {	
		String outPage = "login";
		if(this.getRoleFromSession(session).equals("AUTHOR")) {	
			List<Author> authParts = new ArrayList<>();
			List<User> userParts = new ArrayList<>();
			Author author = new Author();
			author.setUid(this.getUidFromSession(session));
			userService.queryAuthorList(author, null, null, userParts, authParts);
			if(authParts.size()==0) {
				return "author/profilePage";
			}
			model.addAttribute("authPart", authParts.get(0));
			model.addAttribute("userPart", userParts.get(0));
			outPage = "author/profilePage";
		} else if(this.getRoleFromSession(session)=="CUSTOMER") {
			List<Customer> custParts = new ArrayList<>();
			List<User> userParts = new ArrayList<>();
			Customer customer = new Customer();
			customer.setUid(this.getUidFromSession(session));
			userService.queryCustomerList(customer, null, null, userParts, custParts);
			if(custParts.size()==0) {
				return "customer/profilePage";
			}
			model.addAttribute("custPart", custParts.get(0));
			model.addAttribute("userPart", userParts.get(0));
			outPage = "customer/profilePage";
		}		
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
		User user = new User();
		user.setUsername(username);
		List<User> queried = userService.findUserByUsername(user);
		if(queried!=null&&queried.size()>0) {
			model.addAttribute("submit_res_upper", "Username already taken! ");
			return outPage;	
		}		
		// otherwise can change user name
		// fetch original user info, and set the user name field only
		User oldUser = new User();
		oldUser.setUsername(this.getUnameFromSession(session));
		oldUser = userService.findUserByUsername(oldUser).get(0);
		oldUser.setUsername(username);
		userService.saveUser(oldUser);
		model.addAttribute("submit_res_upper", "Username Successfully Updated!");
		// after updating username, write it back to session
		session.setAttribute("username", username);
		if(this.getRoleFromSession(session).equals("AUTHOR")) {	
			List<Author> authParts = new ArrayList<>();
			List<User> userParts = new ArrayList<>();
			Author author = new Author();
			author.setUid(this.getUidFromSession(session));
			userService.queryAuthorList(author, null, null, userParts, authParts);
			if(authParts.size()==0) {
				return "author/profilePage";
			}
			model.addAttribute("authPart", authParts.get(0));
			model.addAttribute("userPart", userParts.get(0));
		} else if(this.getRoleFromSession(session)=="CUSTOMER") {
			List<Customer> custParts = new ArrayList<>();
			List<User> userParts = new ArrayList<>();
			Customer customer = new Customer();
			customer.setUid(this.getUidFromSession(session));
			userService.queryCustomerList(customer, null, null, userParts, custParts);
			if(custParts.size()==0) {
				return "customer/profilePage";
			}
			model.addAttribute("custPart", custParts.get(0));
			model.addAttribute("userPart", userParts.get(0));
			outPage = "customer/profilePage";
		}	
		return outPage;
	}
	
	//change user password
	@RequestMapping("/uptUserpwd")
	public String uptUserpwd(Model model,  @RequestParam(name="OLDPWD", required=true) String oldPwd, @RequestParam(name="PWD", required=true) String pwd, HttpSession session) {
		String outPage = "login";
		if(this.getRoleFromSession(session).equals("AUTHOR")) {	
			List<Author> authParts = new ArrayList<>();
			List<User> userParts = new ArrayList<>();
			Author author = new Author();
			author.setUid(this.getUidFromSession(session));
			userService.queryAuthorList(author, null, null, userParts, authParts);
			if(authParts.size()==0) {
				return "author/profilePage";
			}
			model.addAttribute("authPart", authParts.get(0));
			model.addAttribute("userPart", userParts.get(0));
			outPage = "author/profilePage";
		} else if(this.getRoleFromSession(session)=="CUSTOMER") {
			List<Customer> custParts = new ArrayList<>();
			List<User> userParts = new ArrayList<>();
			Customer customer = new Customer();
			customer.setUid(this.getUidFromSession(session));
			userService.queryCustomerList(customer, null, null, userParts, custParts);
			if(custParts.size()==0) {
				return "customer/profilePage";
			}
			model.addAttribute("custPart", custParts.get(0));
			model.addAttribute("userPart", userParts.get(0));
			outPage = "customer/profilePage";
		}		
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
		User oldUser = new User();
		oldUser.setUsername(this.getUnameFromSession(session));
		User storedData = userService.findUserByUsername(oldUser).get(0);
		if(!storedData.getPwd().equals(StringUtils.getMd5String(oldPwd, storedData.getSalt()))) {
			model.addAttribute("submit_res_upper", "Old Password was entered incorrectly! ");
			return outPage;	
		}		
		// otherwise can change user password
		// fetch original user info, and set the user name field only
		storedData.setPwd(StringUtils.getMd5String(pwd, storedData.getSalt()));
		userService.saveUser(storedData);
		model.addAttribute("submit_res_upper", "Password Successfully Updated!");
		if(this.getRoleFromSession(session).equals("AUTHOR")) {	
			List<Author> authParts = new ArrayList<>();
			List<User> userParts = new ArrayList<>();
			Author author = new Author();
			author.setUid(this.getUidFromSession(session));
			userService.queryAuthorList(author, null, null, userParts, authParts);
			if(authParts.size()==0) {
				return "login";
			}
			model.addAttribute("authPart", authParts.get(0));
			model.addAttribute("userPart", userParts.get(0));
		} else if(this.getRoleFromSession(session)=="CUSTOMER") {
			List<Customer> custParts = new ArrayList<>();
			List<User> userParts = new ArrayList<>();
			Customer customer = new Customer();
			customer.setUid(this.getUidFromSession(session));
			userService.queryCustomerList(customer, null, null, userParts, custParts);
			if(custParts.size()==0) {
				return "login";
			}
			model.addAttribute("custPart", custParts.get(0));
			model.addAttribute("userPart", userParts.get(0));
			outPage = "customer/profilePage";
		}	
		return outPage;	
	}
	

}
