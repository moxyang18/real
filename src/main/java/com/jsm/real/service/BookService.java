package com.jsm.real.service;

import java.util.List;

import com.jsm.real.entity.Author;
import com.jsm.real.entity.AuthorBook;
import com.jsm.real.entity.Book;
import com.jsm.real.entity.Topic;

public interface BookService {
	// add or update a book
	void saveBook(Book book);
	
	// add the book and corresponding author
	void addAuthBook(Long bid, Long authId);
	
	void delAuthBook(Long bid, Long authId);
	
	// delete a book, does not support the operation for now
	void deleteBook(Book book, Long authId);
	
	// query book list based on the conditions specified in input Book
	void getBookList(Book book, String topicName, int limit, List<Book> bookParts, List<Topic> topicParts);

	// query the book's author list
	List<Author> getAuthorList(Book book);
	
	// get all author/book information
	void getAuthBookList(AuthorBook authBook, String bookName, String penName, int limit, List<AuthorBook> authBookParts,
			 List<Book> bookParts, List<Author> authParts);
	
	// check whether the book with the bid exists
	boolean existsBook(Long bid);
	
	// check wheteher the author book mapping exists
	boolean existsAuthBook(Long bid, Long authid);
	
}
