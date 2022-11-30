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

import com.jsm.real.entity.Book;
import com.jsm.real.entity.Copy;
import com.jsm.real.entity.Invoice;
import com.jsm.real.entity.RentalRecord;
import com.jsm.real.entity.RentalRecordMute;
import com.jsm.real.service.CopyService;
import com.jsm.real.service.InvoiceService;
import com.jsm.real.service.RentalRecordService;
import com.jsm.real.util.DateCustUtils;
import com.jsm.real.util.StringUtils;

@Controller
public class RentalController extends BaseController {
	@Autowired
	RentalRecordService recService;
	@Autowired
	CopyService copyService;
	@Autowired
	InvoiceService invoiceService;
	// display all copies
	@RequestMapping("/displayAllCopies")
	public String displayAllCopies() {
		return null;
	}
	// display copies info, can be filtered by copy id, book id, book name, topic, available status, author pen name
	@RequestMapping("/displayCopiesInfo")	
	public String displayCopiesInfo() {
		return null;
	}
	// display all rental records
	@RequestMapping("/rentalRecords")
	public String rentalRecords(Model model) {
		List<RentalRecord> recParts = new ArrayList<>();
		List<Copy> copyParts = new ArrayList<>();
		List<Book> bookParts = new ArrayList<>();
		recService.fetchAllRentalRecs(recParts, copyParts, bookParts);
		model.addAttribute("recList", recParts);
		model.addAttribute("copyList", copyParts);
		model.addAttribute("bookList", bookParts);
		return "employee/rentalRecsPage";
	}
	
	// query rental records by filter
	@RequestMapping("/qryRentalRecords")	
	public String qryRentalRecords(Model model, RentalRecordMute recMute, @RequestParam(name="bookId", required=false) Long bookId,
			@RequestParam(name="bookName", required=false) String bookName) {
		// sanitize input
		bookName = StringUtils.sqlSanitize(bookName);
		recMute.setAct_ret_date(StringUtils.sqlSanitize(recMute.getAct_ret_date()));
		recMute.setBorrow_date(StringUtils.sqlSanitize(recMute.getBorrow_date()));
		recMute.setExp_ret_date(StringUtils.sqlSanitize(recMute.getExp_ret_date()));
		// set content in RentalRecordMute to RentalRecord
		RentalRecord rec = new RentalRecord();
		if(!recMute.getAct_ret_date().equals(""))
			rec.setAct_ret_date(StringUtils.toSqlDate(recMute.getAct_ret_date()));
		if(!recMute.getBorrow_date().equals(""))
			rec.setBorrow_date(StringUtils.toSqlDate(recMute.getBorrow_date()));
		if(!recMute.getExp_ret_date().equals(""))
			rec.setExp_ret_date(StringUtils.toSqlDate(recMute.getExp_ret_date()));
		rec.setCopy_id(recMute.getCopy_id());
		rec.setRental_id(recMute.getRental_id());
		rec.setRental_status(recMute.getRental_status());
		rec.setUID(recMute.getUID());
		

		List<RentalRecord> recParts = new ArrayList<>();
		List<Copy> copyParts = new ArrayList<>();
		List<Book> bookParts = new ArrayList<>();
		recService.qryRentalRecordsBy(rec, bookId, bookName, recParts, copyParts, bookParts);
		model.addAttribute("recList", recParts);
		model.addAttribute("copyList", copyParts);
		model.addAttribute("bookList", bookParts);
		model.addAttribute("submit_res", "The rental records queried!");
		return "employee/rentalRecsPage";
	}
	
	// display the customer's own rental history
	@RequestMapping("/ownRentalRecords")
	public String ownRentalRecords(Model model, HttpSession session) {
		List<RentalRecord> recParts = new ArrayList<>();
		List<Copy> copyParts = new ArrayList<>();
		List<Book> bookParts = new ArrayList<>();
		RentalRecord rec = new RentalRecord();
		rec.setUID(this.getUidFromSession(session));
		recService.qryRentalRecordsBy(rec, null, null, recParts, copyParts, bookParts);
		model.addAttribute("recList", recParts);
		model.addAttribute("copyList", copyParts);
		model.addAttribute("bookList", bookParts);
		return "customer/ownRentalPage";
	}
	
	// display the customer's own rental history by filter
	@RequestMapping("/qryOwnRentalRecords")
	public String qryOwnRentalRecords(Model model, HttpSession session, RentalRecordMute recMute, @RequestParam(name="bookId", required=false) Long bookId,
			@RequestParam(name="bookName", required=false) String bookName) {
		// sanitize input
		bookName = StringUtils.sqlSanitize(bookName);
		// sanitize input
		bookName = StringUtils.sqlSanitize(bookName);
		recMute.setAct_ret_date(StringUtils.sqlSanitize(recMute.getAct_ret_date()));
		recMute.setBorrow_date(StringUtils.sqlSanitize(recMute.getBorrow_date()));
		recMute.setExp_ret_date(StringUtils.sqlSanitize(recMute.getExp_ret_date()));
		// set content in RentalRecordMute to RentalRecord
		RentalRecord rec = new RentalRecord();
		if(!recMute.getAct_ret_date().equals(""))
			rec.setAct_ret_date(StringUtils.toSqlDate(recMute.getAct_ret_date()));
		if(!recMute.getBorrow_date().equals(""))
			rec.setBorrow_date(StringUtils.toSqlDate(recMute.getBorrow_date()));
		if(!recMute.getExp_ret_date().equals(""))
			rec.setExp_ret_date(StringUtils.toSqlDate(recMute.getExp_ret_date()));
		rec.setCopy_id(recMute.getCopy_id());
		rec.setRental_id(recMute.getRental_id());
		rec.setRental_status(recMute.getRental_status());
		rec.setUID(recMute.getUID());
		
		List<RentalRecord> recParts = new ArrayList<>();
		List<Copy> copyParts = new ArrayList<>();
		List<Book> bookParts = new ArrayList<>();
		rec.setUID(this.getUidFromSession(session));
		recService.qryRentalRecordsBy(rec, bookId, bookName, recParts, copyParts, bookParts);
		model.addAttribute("recList", recParts);
		model.addAttribute("copyList", copyParts);
		model.addAttribute("bookList", bookParts);
		model.addAttribute("submit_res", "The rental records queried!");
		return "customer/ownRentalPage";
	}
	
	// customer rents a copy (customer side)
	@Transactional
	@RequestMapping("/rentCopy")
	public String rentCopy(Model model, HttpSession session, RentalRecord rec) {
		model.addAttribute("role", this.getRoleFromSession(session));
		// need to specify copyId
		if(rec.getCopy_id()==null) {
			model.addAttribute("submit_res", "Specify the copy you want to check out!");
			return "copiesPage";
		}
		// the copy needs to exist and is available
		if(!copyService.isAvailable(rec.getCopy_id())){ 
			model.addAttribute("submit_res", "Specify a valid, available copy you want to check out!");
			return "copiesPage";
		}
		// otherwise can perform the check out
		Copy storedCopy = copyService.getCopyById(rec.getCopy_id());
		storedCopy.setStatus('N');
		copyService.saveCopy(storedCopy);
		
		// get today's value for borrow date
		java.sql.Date sqlToday = new java.sql.Date(new java.util.Date().getTime());
		rec.setBorrow_date(sqlToday);
		// expect to return the book after seven days
		java.sql.Date sqlExpRetDate = DateCustUtils.addDays(sqlToday, 7);
		rec.setExp_ret_date(sqlExpRetDate);
		// set customer id
		rec.setUID(this.getUidFromSession(session));
		// reset the rental id
		rec.setRental_id(null);
		// set the borrow status to borrowed
		rec.setRental_status('B');
		recService.genRentalRec(rec);
		model.addAttribute("submit_res", "The copy was successfully checked out!");
		return "customer/ownRentalPage";
	}
	
	// customer returns a copy (employee side)
	@Transactional
	@RequestMapping("/returnCopy")
	public String returnCopy(Model model, HttpSession session, RentalRecordMute rec) {
		model.addAttribute("role", this.getRoleFromSession(session));
		// need to specify rental_id
		if(rec.getRental_id()==null) {
			model.addAttribute("submit_res", "Specify the rental id if you want to check in!");
			return "employee/rentalRecsPage";
		}
		RentalRecord storedDataR = recService.getRecById(rec.getRental_id());
		
		// the rental record needs to exist and status is borrowed, if not, prompt employee
		if(storedDataR==null||storedDataR.getRental_status()!='B') {
			model.addAttribute("submit_res", "Specify the valid rental record you want to check in a copy for!");
			return "employee/rentalRecsPage";
		}		
		// otherwise can return
		// get the stored rental record and copy data
		Copy storedDataC = copyService.getCopyById(storedDataR.getCopy_id());
		// set the copy's status to be available
		storedDataC.setStatus('Y');
		copyService.saveCopy(storedDataC);
		// set the rental record's actual return date to be today
		// get today's value for actual return date
		java.sql.Date sqlToday = new java.sql.Date(new java.util.Date().getTime());
		storedDataR.setAct_ret_date(sqlToday);
		//if returned after expected return date, late
		Double calcAmt = null;
		if(sqlToday.after(storedDataR.getExp_ret_date())) {
			storedDataR.setRental_status('L');
			//DATEDIFF(NEW.EXP_RET_DATE, NEW.BORROW_DATE)*0.2+DATEDIFF(NEW.ACT_RET_DATE, NEW.EXP_RET_DATE)*0.4
			calcAmt = DateCustUtils.daysDiff(storedDataR.getBorrow_date(), storedDataR.getExp_ret_date())*0.2+
					DateCustUtils.daysDiff(storedDataR.getExp_ret_date(), storedDataR.getAct_ret_date())*0.4;
		} else {
			storedDataR.setRental_status('R');
			//DATEDIFF(NEW.ACT_RET_DATE,NEW.BORROW_DATE)*0.2
			calcAmt = DateCustUtils.daysDiff(storedDataR.getBorrow_date(), storedDataR.getAct_ret_date())*0.2;
		}	
/*		String s = "";
		if(storedDataR.getAct_ret_date()!=null) {
			s+=" storedDataR.getAct_ret_date(): "+storedDataR.getAct_ret_date();
		}
		if(storedDataR.getBorrow_date()!=null) {
			s+=" storedDataR.getBorrow_date(): "+storedDataR.getBorrow_date();
		}
		if(storedDataR.getCopy_id()!=null) {
			s+=" storedDataR.getCopy_id(): "+storedDataR.getCopy_id();
		}
		if(storedDataR.getExp_ret_date()!=null) {
			s+= " storedDataR.getExp_ret_date()"+storedDataR.getExp_ret_date();
		}
		if(storedDataR.getRental_id()!=null) {
			s+= " storedDataR.getRental_id():" + storedDataR.getRental_id();
		}
		if(storedDataR.getRental_status()!=null) {
			s+= " storedDataR.getRental_status():" + storedDataR.getRental_status();
		}
		if(storedDataR.getUID()!=null) {
			s+= " storedDataR.getUID():" + storedDataR.getUID();
		} 
		RentalRecord newRent = new RentalRecord();
		newRent.setAct_ret_date(storedDataR.getAct_ret_date());
		newRent.setBorrow_date(storedDataR.getBorrow_date());
		newRent.setCopy_id(storedDataR.getCopy_id());
		newRent.setExp_ret_date(storedDataR.getExp_ret_date());
		newRent.setRental_id(storedDataR.getRental_id());
		newRent.setRental_status(storedDataR.getRental_status());
		newRent.setUID(storedDataR.getUID());
		model.addAttribute("submit_res", "The rental info is: " + s);
		recService.genRentalRec(newRent);*/
		
		Long newRecId = recService.genRentalRec(storedDataR);
		Invoice newInvoice = new Invoice();
		newInvoice.setInvoice_amt(calcAmt);
		newInvoice.setAmt_to_pay(calcAmt);
		newInvoice.setPaid_status("INCOMPLETE");
		newInvoice.setInvoice_date(sqlToday);
		newInvoice.setInvoice_id(null);
		newInvoice.setRental_id(newRecId);
		invoiceService.saveInvoice(newInvoice);
		model.addAttribute("submit_res", "The copy was successfully returned!");
		return "customer/ownRentalPage";
	}
	
	
	
}
