package com.jsm.real.controller;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jsm.real.entity.Author;
import com.jsm.real.entity.AuthorBook;
import com.jsm.real.entity.Book;
import com.jsm.real.entity.Topic;
import com.jsm.real.service.BookService;
import com.jsm.real.service.TopicService;
import com.jsm.real.service.UserService;
import com.jsm.real.util.StringUtils;

@Controller
public class BookController extends BaseController {
	@Autowired
	BookService bookService;
	@Autowired
	TopicService topicService;	
	@Autowired
	UserService userService;
	@Transactional
	@RequestMapping("/addBook")
	public String addBook(Model model, Book book, HttpSession session) {
		// TODO sanitize input
		// validate input
		if(book.getBook_name()==null||book.getTopic_id()==null) {
			model.addAttribute("submit_res", "Invalid. The book cannot be added! Specify book name, topic id.");
			model.addAttribute("book_name", book.getBook_name());
			model.addAttribute("topic_id", book.getTopic_id());
			return "addBookPage";
		}
		bookService.saveBook(book);
		model.addAttribute("submit_res", "The book entry was successfully added!");
		model.addAttribute("role", this.getRoleFromSession(session));
		return "addBookPage";
	}
	
	
	@RequestMapping("/bookList") 
	public String bookList(Model model, HttpSession session) {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		// add role to view
		model.addAttribute("role", this.getRoleFromSession(session));
		Book book = new Book();
		List<Book> bookParts = new ArrayList<>();
		List<Topic> topicParts = new ArrayList<>();
		bookService.getBookList(book, null, 2000, bookParts, topicParts);
		model.addAttribute("bookList", bookParts);
		model.addAttribute("topicLowerList", topicParts);
		// if the role stored in session is not STAFF, can only perform query operations
		if(!this.getRoleFromSession(session).equals("STAFF")) {
			model.addAttribute("queryOnly", "true");
		} else {
			model.addAttribute("queryOnly", "false");
		}
		return "booksPage";
	}
	// this request redirects to add a book page
	@RequestMapping("/toAddBook")
	public String toAddBook(Model model, HttpSession session) {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		model.addAttribute("role", this.getRoleFromSession(session));
		return "addBookPage";
	}	
	
	@RequestMapping("/queryBook") 
	public String queryBook(Model model, Book book, @RequestParam(name="topicName", required=false) String topicName, HttpSession session) {
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		// add role to view
		model.addAttribute("role", this.getRoleFromSession(session));
		// TODO modify the hard coded limit
		if((book.getBook_name()==null||book.getBook_name().trim()=="")&&book.getTopic_id()==null&&book.getBid()==null&&(topicName==null||topicName.trim()=="")) {
			model.addAttribute("submit_res", "query all");
		} else {
			model.addAttribute("submit_res", "query based on your input");
		}
		List<Book> bookParts = new ArrayList<>();
		List<Topic> topicParts = new ArrayList<>();
		bookService.getBookList(book, topicName, 2000, bookParts, topicParts);
		model.addAttribute("bookList", bookParts);
		model.addAttribute("topicLowerList", topicParts);
		return "booksPage";
	}
	
	@RequestMapping("/editBook") 
	public String editBook(Model model, Book book, HttpSession session) {
		// add role to view
		model.addAttribute("role", this.getRoleFromSession(session));
		// TODO modify the hard coded limit
		if(book.getBid()==null) {
			model.addAttribute("submit_res", "please specify book id");
			return "booksPage";
		} else if(book.getBook_name()==null||book.getBook_name().trim()=="") {
			model.addAttribute("submit_res", "please specify book name");
			return "booksPage";			
		} else if(book.getTopic_id()==null) {
			model.addAttribute("submit_res", "please specify topic id");
			return "booksPage";				
		}
		// if the book id is invalid, cannot edit
		Book idCheck = new Book();
		idCheck.setBid(book.getBid());
		List<Book> bookParts = new ArrayList<>();
		List<Topic> topicParts = new ArrayList<>();
		bookService.getBookList(idCheck, null, 2000, bookParts, topicParts);
		if(bookParts.size()<1) {
			model.addAttribute("submit_res", "please select valid book id");
			return "booksPage";				
		}
		// if the topic id is invalid, cannot edit
		Topic topic = new Topic();
		topic.setTopic_id(book.getTopic_id());
		List<Topic> topicList = topicService.getTopicList(topic, 2000);
		if(topicList==null||topicList.size()<1) {
			model.addAttribute("submit_res", "please select valid topic id");
			return "booksPage";				
		}
		book.setBook_name(book.getBook_name().trim());
		bookService.saveBook(book);
		model.addAttribute("submit_res", "edit success!");
		bookParts = new ArrayList<>();
		topicParts = new ArrayList<>();
		bookService.getBookList(book, null, 2000, bookParts, topicParts);
		model.addAttribute("bookList", bookParts);
		model.addAttribute("topicListLower", topicParts);
		return "booksPage";
	}	
	
	
	@Transactional
	@RequestMapping("/authBookList")
	public String authBookList(Model model, HttpSession session) {
		// add role to view
		model.addAttribute("role", this.getRoleFromSession(session));
		List<AuthorBook> authBookParts = new ArrayList<>();
		List<Book> bookParts = new ArrayList<>();
		List<Author> authParts = new ArrayList<>();
		bookService.getAuthBookList(null, null, null, 2000,
				authBookParts, bookParts, authParts);
		model.addAttribute("authBookList", authBookParts);
		model.addAttribute("bookList", bookParts);
		model.addAttribute("authList", authParts);
		return "authBooksPage";
	}
	
	
	@Transactional
	@RequestMapping("/queryAuthBook")
	public String queryAuthBook(Model model, AuthorBook authBook, @RequestParam(name="bookName", required=false) String bookName, @RequestParam(name="penName", required=false) String penName, HttpSession session) {	
		// add role to view
		model.addAttribute("role", this.getRoleFromSession(session));
		// sanitize the input
		bookName = StringUtils.sqlSanitize(bookName);
		penName = StringUtils.sqlSanitize(penName);
		List<AuthorBook> authBookParts = new ArrayList<>();
		List<Book> bookParts = new ArrayList<>();
		List<Author> authParts = new ArrayList<>();
		bookService.getAuthBookList(authBook, bookName, penName, 2000, authBookParts, bookParts, authParts);
		model.addAttribute("authBookList", authBookParts);
		model.addAttribute("bookList", bookParts);
		model.addAttribute("authList", authParts);	
		return "authBooksPage";
	}
	
	@Transactional
	@RequestMapping("/addAuthBook")
	public String addAuthBook(Model model, AuthorBook authBook, HttpSession session) {
		// add role to view
		model.addAttribute("role", this.getRoleFromSession(session));
		List<AuthorBook> authBookParts = new ArrayList<>();
		List<Book> bookParts = new ArrayList<>();
		List<Author> authParts = new ArrayList<>();
		bookService.getAuthBookList(null, null, null, 2000,
				authBookParts, bookParts, authParts);
		model.addAttribute("authBookList", authBookParts);
		model.addAttribute("bookList", bookParts);
		model.addAttribute("authList", authParts);
		// only care about the mapping relationship between author and book
		// both needs to be specified
		if(authBook.getBid()==null||authBook.getUID()==null) {
			model.addAttribute("submit_res", "Cannot add the author book relationship! Please specify both author id and bid");
			return "authBooksPage";
		}
		// both author and book needs to exist
		if(!userService.existsAuthor(authBook.getUID())||!bookService.existsBook(authBook.getBid())) {
			model.addAttribute("submit_res", "Cannot add the author book relationship! Please provide valid author id and bid");
			return "authBooksPage";
		}
		// the mapping needs to not exist already
		if(bookService.existsAuthBook(authBook.getUID(), authBook.getBid())) {
			model.addAttribute("submit_res", "Duplicate! Cannot add the author book relationship!");
			return "authBooksPage";			
		}
		// after input validation, can now insert the mapping
		bookService.addAuthBook(authBook.getBid(),authBook.getUID());
		model.addAttribute("submit_res", "Successfully added an author/book entry!");
		authBookParts = new ArrayList<>();
		bookParts = new ArrayList<>();
		authParts = new ArrayList<>();
		bookService.getAuthBookList(null, null, null, 2000,
				authBookParts, bookParts, authParts);
		model.addAttribute("authBookList", authBookParts);
		model.addAttribute("bookList", bookParts);
		model.addAttribute("authList", authParts);
		return "authBooksPage";
	}
	
	
	@Transactional
	@RequestMapping("/deleteAuthBook")
	public String deleteAuthBook(Model model, AuthorBook authBook, HttpSession session) {
		// add role to view
		model.addAttribute("role", this.getRoleFromSession(session));
		List<AuthorBook> authBookParts = new ArrayList<>();
		List<Book> bookParts = new ArrayList<>();
		List<Author> authParts = new ArrayList<>();
		bookService.getAuthBookList(null, null, null, 2000,
				authBookParts, bookParts, authParts);
		model.addAttribute("authBookList", authBookParts);
		model.addAttribute("bookList", bookParts);
		model.addAttribute("authList", authParts);		
		// only care about the mapping relationship between author and book
		// both needs to be specified
		if(authBook.getBid()==null||authBook.getUID()==null) {
			model.addAttribute("submit_res", "Cannot add the author book relationship! Please specify both author id and bid");
			return "authBooksPage";
		}		
		// the mapping needs to exist already
		if(!bookService.existsAuthBook(authBook.getUID(), authBook.getBid())) {
			model.addAttribute("submit_res", "The authorBook relationship does not exist!");
			return "authBooksPage";			
		}
		// after input validation, can now delete the mapping
		bookService.delAuthBook(authBook.getUID(), authBook.getBid());
		model.addAttribute("submit_res", "Successfully deleted an author/book entry!");
		authBookParts = new ArrayList<>();
		bookParts = new ArrayList<>();
		authParts = new ArrayList<>();
		bookService.getAuthBookList(null, null, null, 2000,
				authBookParts, bookParts, authParts);
		model.addAttribute("authBookList", authBookParts);
		model.addAttribute("bookList", bookParts);
		model.addAttribute("authList", authParts);
		return "authBooksPage";
	}
	
	@Transactional
	@RequestMapping("/updateBook")
	public String updateBook(Model model, Book book) {
		// TODO sanitize input
		// validate input
		if(book.getBid()==null||book.getBook_name()==null||book.getTopic_id()==null) {
			model.addAttribute("submit_res", "Invalid. The book cannot be modified! Specify bid, book name, topic id.");
			model.addAttribute("bid", book.getBid());
			model.addAttribute("book_name", book.getBook_name());
			model.addAttribute("topic_id", book.getTopic_id());
			return "uptBookPage";
		}
		bookService.saveBook(book);
		model.addAttribute("submit_res", "The book entry was successfully updated!");
		return "uptBookPage";
	}	
	
	// this request redirects to update a book page
	@RequestMapping("/toUptBook")
	public String toUptBook(Model model, @RequestParam(name="bid", required=true) Long bid) {
		model.addAttribute("bid", bid);
		return "uptBookPage";
	}		
	
}
