package com.jsm.real.service;

import java.util.List;

import com.jsm.real.entity.Author;
import com.jsm.real.entity.AuthorBook;
import com.jsm.real.entity.Book;
import com.jsm.real.entity.Copy;
import com.jsm.real.entity.Topic;

public interface CopyService {
	// stock certain number of copies of the book
	void addCopy(Copy copy);
	
	// delete a copy
	void deleteCopy(Copy copy);
	
	// query copy list based on the conditions specified in input Copy
	List<Copy> getCopyList(Copy copy, int limit);	
	
	// query all book copies by filter
	void getBookCopyList(Copy copy, String bookName, Integer topicId, int limit, List<Copy> copyParts,
			 List<Book> bookParts, List<Topic> topicParts);
	
	// query all book copies with related information, copy, book, topic, author
	void getBookCopyListBetter(Copy copy, String bookName, Integer topicId, String authPenName, Long authId, 
			int limit, List<Copy> copyParts,List<Book> bookParts, List<Topic> topicParts, 
			List<AuthorBook> authBookParts, List<Author> authParts);
	
	// check whether the copy with the copy id exists and is available
	boolean isAvailable(Long copyId);
	
	// save a copy
	Long saveCopy(Copy copy);
	
	// query a copy by id
	Copy getCopyById(Long copyId);
}
