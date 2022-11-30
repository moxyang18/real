package com.jsm.real.controller;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpSession;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.jsm.real.entity.Book;
import com.jsm.real.entity.Copy;
import com.jsm.real.entity.Invoice;
import com.jsm.real.entity.InvoiceMute;
import com.jsm.real.entity.RentalRecord;
import com.jsm.real.entity.RentalRecordMute;
import com.jsm.real.service.InvoiceService;
import com.jsm.real.util.StringUtils;

@Controller
public class InvoiceController extends BaseController {
	@Autowired
	InvoiceService invoiceService;
	
	// display all invoices
	@RequestMapping("/invoices")
	public String invoices(Model model) {
		List<Invoice> invoiceParts  = new ArrayList<>();
		List<RentalRecord> recParts  = new ArrayList<>();
		List<Copy> copyParts  = new ArrayList<>();
		List<Book> bookParts  = new ArrayList<>();
		invoiceService.fetchAllInvoices(invoiceParts, recParts, copyParts, bookParts); 
		model.addAttribute("invoiceList", invoiceParts);
		model.addAttribute("recList", recParts);
		model.addAttribute("copyList", copyParts);
		model.addAttribute("bookList", bookParts);
		return "employee/invoicesPage";
	}
	
	// query invoices by filter
	@RequestMapping("/qryInvoices")	
	public String qryInvoices(Model model, InvoiceMute invoiceMute, @RequestParam(name="copyId", required=false) Long copyId,
			@RequestParam(name="custId", required=false) Long custId) {
		// sanitize input
		invoiceMute.setInvoice_date(StringUtils.sqlSanitize(invoiceMute.getInvoice_date()));
		invoiceMute.setPaid_status(StringUtils.sqlSanitize(invoiceMute.getPaid_status()));
		// set content in InvoiceMute to Invoice
		Invoice rec = new Invoice();
		if(!invoiceMute.getInvoice_date().equals(""))
			rec.setInvoice_date(StringUtils.toSqlDate(invoiceMute.getInvoice_date())); 
		rec.setInvoice_amt(invoiceMute.getInvoice_amt());
		rec.setInvoice_id(invoiceMute.getInvoice_id());
		rec.setRental_id(invoiceMute.getRental_id());
		rec.setPaid_status(invoiceMute.getPaid_status());
		rec.setAmt_to_pay(invoiceMute.getAmt_to_pay());
		List<Invoice> invoiceParts  = new ArrayList<>();
		List<RentalRecord> recParts  = new ArrayList<>();
		List<Copy> copyParts  = new ArrayList<>();
		List<Book> bookParts  = new ArrayList<>();
		invoiceService.queryInvoicesBy(rec, copyId, custId, invoiceParts, recParts, copyParts, bookParts);
		model.addAttribute("invoiceList", invoiceParts);
		model.addAttribute("recList", recParts);
		model.addAttribute("copyList", copyParts);
		model.addAttribute("bookList", bookParts);
		model.addAttribute("submit_res", "The invoice records queried!");
		return "employee/invoicesPage";
	}
	
	// display the customer's own invoices
	@RequestMapping("/ownInvoices")
	public String ownInvoices(Model model, HttpSession session) {
		List<Invoice> invoiceParts  = new ArrayList<>();
		List<RentalRecord> recParts  = new ArrayList<>();
		List<Copy> copyParts  = new ArrayList<>();
		List<Book> bookParts  = new ArrayList<>();
		invoiceService.queryInvoicesBy(null, null, this.getUidFromSession(session), invoiceParts, recParts, copyParts, bookParts);
		model.addAttribute("invoiceList", invoiceParts);
		model.addAttribute("recList", recParts);
		model.addAttribute("copyList", copyParts);
		model.addAttribute("bookList", bookParts);
		return "customer/invoicesPage";
	}
	
	// display the customer's own invoices by filter
	@RequestMapping("/qryOwnInvoices")
	public String qryOwnInvoices(Model model, HttpSession session, InvoiceMute invoiceMute, @RequestParam(name="copyId", required=false) Long copyId) {
		// sanitize input
		invoiceMute.setInvoice_date(StringUtils.sqlSanitize(invoiceMute.getInvoice_date()));
		invoiceMute.setPaid_status(StringUtils.sqlSanitize(invoiceMute.getPaid_status()));
		// set content in InvoiceMute to Invoice
		Invoice rec = new Invoice();
		if(!invoiceMute.getInvoice_date().equals(""))
			rec.setInvoice_date(StringUtils.toSqlDate(invoiceMute.getInvoice_date())); 
		rec.setInvoice_amt(invoiceMute.getInvoice_amt());
		rec.setInvoice_id(invoiceMute.getInvoice_id());
		rec.setRental_id(invoiceMute.getRental_id());
		rec.setPaid_status(invoiceMute.getPaid_status());
		rec.setAmt_to_pay(invoiceMute.getAmt_to_pay());
		List<Invoice> invoiceParts  = new ArrayList<>();
		List<RentalRecord> recParts  = new ArrayList<>();
		List<Copy> copyParts  = new ArrayList<>();
		List<Book> bookParts  = new ArrayList<>();
		invoiceService.queryInvoicesBy(rec, copyId, this.getUidFromSession(session), invoiceParts, recParts, copyParts, bookParts);
		model.addAttribute("invoiceList", invoiceParts);
		model.addAttribute("recList", recParts);
		model.addAttribute("copyList", copyParts);
		model.addAttribute("bookList", bookParts);
		model.addAttribute("submit_res", "The invoice records queried!");
		return "customer/invoicesPage";
	}
}
