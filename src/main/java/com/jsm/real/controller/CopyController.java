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
import com.jsm.real.entity.Copy;
import com.jsm.real.entity.Topic;
import com.jsm.real.service.CopyService;
import com.jsm.real.service.TopicService;
import com.jsm.real.util.StringUtils;

@Controller
public class CopyController extends BaseController {
	@Autowired
	CopyService copyService;
	@Autowired
	TopicService topicService;
	@Transactional
	@RequestMapping("/addCopy")
	public String addCopy(Model model, Copy copy, @RequestParam(name="volumn", required=false) Integer volumn) {
		// TODO sanitize input
		// validate input
		if(copy.getBid()==null||volumn==null||volumn<1) {
			model.addAttribute("submit_res", "Invalid. The copy cannot be added! Specify book id and volumn number.");
			model.addAttribute("bid", copy.getBid());
			return "addCopyPage";
		}
		Copy newCopy;
		for(int i = 0; i<volumn; i++) {
			newCopy = new Copy();
			newCopy.setBid(copy.getBid());
			copyService.addCopy(newCopy);
		}
		model.addAttribute("submit_res", "The copy entries were successfully added!");
		return "addCopyPage";
	}
		
	@RequestMapping("/copyList") 
	public String copyList(Model model, HttpSession session) {
/*		model.addAttribute("role", this.getRoleFromSession(session));
		// TODO modify the hard coded limit
		List<Copy> copyParts = new ArrayList<>();
		List<Book> bookParts = new ArrayList<>();
		List<Topic> topicParts = new ArrayList<>();
		
		copyService.getBookCopyList(null, null, null, 2000,copyParts,
				bookParts, topicParts);
		model.addAttribute("copyList", copyParts);
		model.addAttribute("bookList", bookParts);
		model.addAttribute("topicList", topicParts);
		return "copiesPage";
		*/
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		
		model.addAttribute("role", this.getRoleFromSession(session));
		// TODO modify the hard coded limit
		List<Copy> copyParts = new ArrayList<>();
		List<Book> bookParts = new ArrayList<>();
		List<Topic> topicParts = new ArrayList<>();
		List<AuthorBook> authBookParts = new ArrayList<>();
		List<Author> authParts = new ArrayList<>();
		copyService.getBookCopyListBetter(null, null, null, null, null, 0, copyParts, bookParts, topicParts, authBookParts, authParts);
		model.addAttribute("copyList", copyParts);
		model.addAttribute("bookList", bookParts);
		model.addAttribute("topicListLower", topicParts);
		model.addAttribute("authList", authParts);
		return "copiesPage";
		
		
	}
	
	// this request redirects to add copies page
	@RequestMapping("/toAddCopy")
	public String toAddCopy() {
		return "addCopyPage";
	}	
	
	@RequestMapping("/queryCopyListBy") 
	public String queryCopyListBy(Model model, Copy copy, @RequestParam(name="bookName", required=false) String bookName, 
			@RequestParam(name="topicId", required=false) Integer topicId, 
			@RequestParam(name="authId", required=false) Long authId, 
			@RequestParam(name="authPenName", required=false) String authPenName,
			HttpSession session) {
/*		model.addAttribute("role", this.getRoleFromSession(session));
		//sanitize input
		bookName = StringUtils.sqlSanitize(bookName);
		List<Copy> copyParts = new ArrayList<>();
		List<Book> bookParts = new ArrayList<>();
		List<Topic> topicParts = new ArrayList<>();
		copyService.getBookCopyList(copy, bookName, topicId, 2000, copyParts,
				bookParts, topicParts);
		model.addAttribute("copyList", copyParts);
		model.addAttribute("bookList", bookParts);
		model.addAttribute("topicList", topicParts);
		return "copiesPage";*/
		List<Topic> topicList = topicService.getTopicList();
		model.addAttribute("topicList", topicList);
		
		model.addAttribute("role", this.getRoleFromSession(session));
		//sanitize input
		bookName = StringUtils.sqlSanitize(bookName);
		authPenName = StringUtils.sqlSanitize(authPenName);
		List<Copy> copyParts = new ArrayList<>();
		List<Book> bookParts = new ArrayList<>();
		List<Topic> topicParts = new ArrayList<>();
		List<AuthorBook> authBookParts = new ArrayList<>();
		List<Author> authParts = new ArrayList<>();
		copyService.getBookCopyListBetter(copy, bookName, topicId, authPenName, authId, 2000, 
				copyParts, bookParts, topicParts, authBookParts, authParts);
		model.addAttribute("copyList", copyParts);
		model.addAttribute("bookList", bookParts);
		model.addAttribute("topicListLower", topicParts);
		model.addAttribute("authList", authParts);
		return "copiesPage";

	}
	
	@RequestMapping("/deleteCopy")
	public String deleteCopy(Model model, Copy copy) {
		// if the copy is borrowed, or the copy_id does not exist
		if(copy.getCopy_id()==null||copy.getStatus()=='N') {
			model.addAttribute("submit_res", "The copy entry cannot be deleted!");
		}
		// otherwise can delete the copy
		copyService.deleteCopy(copy);
		model.addAttribute("submit_res", "The copy entry was successfully deleted!");
		List<Copy> copyParts = new ArrayList<>();
		List<Book> bookParts = new ArrayList<>();
		List<Topic> topicParts = new ArrayList<>();
		copyService.getBookCopyList(null, null, null, 2000,copyParts,
				bookParts, topicParts);
		model.addAttribute("copyList", copyParts);
		model.addAttribute("bookList", bookParts);
		model.addAttribute("topicList", topicParts);
		return "copiesPage";
	}
	
}
