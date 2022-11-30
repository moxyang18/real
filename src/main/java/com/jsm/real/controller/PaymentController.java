package com.jsm.real.controller;

import java.util.ArrayList;
import java.util.Arrays;
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
import com.jsm.real.entity.Payment;
import com.jsm.real.entity.PaymentMute;
import com.jsm.real.entity.RentalRecord;
import com.jsm.real.service.InvoiceService;
import com.jsm.real.service.PaymentService;
import com.jsm.real.service.RentalRecordService;
import com.jsm.real.util.StringUtils;

@Controller
public class PaymentController extends BaseController {
	@Autowired
	PaymentService pmtService;
	@Autowired
	InvoiceService invoiceService;
	@Autowired
	RentalRecordService recService;
	private static final List<Character> pmtSymbols = new ArrayList<>(Arrays.asList('1', '2', '3', '4'));
	private static final List<String> pmtTexts = new ArrayList<>(Arrays.asList("cash", "credit card", "debit card", "PayPal"));
	
	// display all payments
	@RequestMapping("/payments")
	public String payments(Model model) {
		List<Payment> pmtParts  = new ArrayList<>();
		List<Invoice> invoiceParts  = new ArrayList<>();
		List<RentalRecord> recParts  = new ArrayList<>();
		pmtService.fetchAllPayments(pmtParts, invoiceParts, recParts); 
		model.addAttribute("invoiceList", invoiceParts);
		model.addAttribute("pmtList", pmtParts);
		model.addAttribute("recList", recParts);
		return "employee/paymentsPage";
	}
	
	// query payments by filter
	@RequestMapping("/qryPayments")	
	public String qryPayments(Model model, @RequestParam(name="custId", required=false) Long custId, PaymentMute pmtMute) {
		// sanitize input
		pmtMute.setCard_hdr_fname(StringUtils.sqlSanitize(pmtMute.getCard_hdr_fname()));
		pmtMute.setCard_hdr_lname(StringUtils.sqlSanitize(pmtMute.getCard_hdr_lname()));
		pmtMute.setPaid_date(StringUtils.sqlSanitize(pmtMute.getPaid_date()));
		// set content in InvoiceMute to Invoice
		Payment rec = new Payment();
		if(!pmtMute.getPaid_date().equals(""))
			rec.setPaid_date(StringUtils.toSqlDate(pmtMute.getPaid_date()));
		rec.setCard_hdr_fname(pmtMute.getCard_hdr_fname());
		rec.setCard_hdr_lname(pmtMute.getCard_hdr_lname());
		rec.setInvoice_id(pmtMute.getInvoice_id());
		rec.setPaid_amt(pmtMute.getPaid_amt());
		rec.setPAID_METHOD(pmtMute.getPAID_METHOD());
		rec.setPid(pmtMute.getPid());
		List<Payment> pmtParts  = new ArrayList<>();
		List<Invoice> invoiceParts  = new ArrayList<>();
		List<RentalRecord> recParts  = new ArrayList<>();
		pmtService.queryPaymentsBy(rec, custId, pmtParts, invoiceParts, recParts);
		model.addAttribute("invoiceList", invoiceParts);
		model.addAttribute("pmtList", pmtParts);
		model.addAttribute("recList", recParts);
		model.addAttribute("submit_res", "The Payments records queried!");
		return "employee/paymentsPage";
	}
	
	// display the customer's own payments
	@RequestMapping("/ownPayments")
	public String ownPayments(Model model, HttpSession session) {
		List<Payment> pmtParts  = new ArrayList<>();
		List<Invoice> invoiceParts  = new ArrayList<>();
		List<RentalRecord> recParts  = new ArrayList<>();
		pmtService.queryPaymentsBy(null, this.getUidFromSession(session), pmtParts, invoiceParts, recParts);
		model.addAttribute("invoiceList", invoiceParts);
		model.addAttribute("pmtList", pmtParts);
		model.addAttribute("recList", recParts);
		model.addAttribute("pmtSymbols", pmtSymbols);
		model.addAttribute("pmtTexts", pmtTexts);
		return "customer/paymentsPage"; 
	}
	
	// display the customer's own payments by filter
	@RequestMapping("/qryOwnPayments")
	public String qryOwnInvoices(Model model, HttpSession session, PaymentMute pmtMute) {
		// sanitize input
		pmtMute.setCard_hdr_fname(StringUtils.sqlSanitize(pmtMute.getCard_hdr_fname()));
		pmtMute.setCard_hdr_lname(StringUtils.sqlSanitize(pmtMute.getCard_hdr_lname()));
		pmtMute.setPaid_date(StringUtils.sqlSanitize(pmtMute.getPaid_date()));
		// set content in InvoiceMute to Invoice
		Payment rec = new Payment();
		if(!pmtMute.getPaid_date().equals(""))
			rec.setPaid_date(StringUtils.toSqlDate(pmtMute.getPaid_date()));
		rec.setCard_hdr_fname(pmtMute.getCard_hdr_fname());
		rec.setCard_hdr_lname(pmtMute.getCard_hdr_lname());
		rec.setInvoice_id(pmtMute.getInvoice_id());
		rec.setPaid_amt(pmtMute.getPaid_amt());
		rec.setPAID_METHOD(pmtMute.getPAID_METHOD());
		rec.setPid(pmtMute.getPid());
		List<Payment> pmtParts  = new ArrayList<>();
		List<Invoice> invoiceParts  = new ArrayList<>();
		List<RentalRecord> recParts  = new ArrayList<>();
		pmtService.queryPaymentsBy(rec, this.getUidFromSession(session), pmtParts, invoiceParts, recParts);
		model.addAttribute("invoiceList", invoiceParts);
		model.addAttribute("pmtList", pmtParts);
		model.addAttribute("recList", recParts);
		model.addAttribute("pmtSymbols", pmtSymbols);
		model.addAttribute("pmtTexts", pmtTexts);
		model.addAttribute("submit_res", "The Payments records queried!");
		return "customer/paymentsPage";
	}
	
	
	// make a payment
	@RequestMapping("/makePayment")
	public String makePayment(Model model, HttpSession session, Payment pmt) {
		// sanitize the input
		pmt.setCard_hdr_fname(StringUtils.sqlSanitize(pmt.getCard_hdr_fname()).toUpperCase());
		pmt.setCard_hdr_lname(StringUtils.sqlSanitize(pmt.getCard_hdr_lname()).toUpperCase());	
		// repopulate to model
		model.addAttribute("invoice_id", pmt.getInvoice_id());
		model.addAttribute("paid_amt", pmt.getPaid_amt());
		model.addAttribute("PAID_METHOD", pmt.getPAID_METHOD());
		model.addAttribute("card_hdr_fname", pmt.getCard_hdr_fname());
		model.addAttribute("card_hdr_lname", pmt.getCard_hdr_lname());
		
		// validate the input
		if(pmt.getCard_hdr_fname().equals("")||pmt.getCard_hdr_lname().equals("")||
				pmt.getInvoice_id()==null||pmt.getPaid_amt()==null||
				pmt.getPAID_METHOD()==null||pmt.getPAID_METHOD()=='0') {
			model.addAttribute("submit_res", "Please Specify are required payment information!");
			return "customer/invoicesPage";
		}	
		// fetch the invoice information
		Invoice storedData = invoiceService.getInvoiceById(pmt.getInvoice_id());
		// if the invoice is complete, or the payment amount is invalid, 
		if(storedData.getPaid_status().equals("COMPLETE")) {
			model.addAttribute("submit_res", "No further payment needed for this invoice!");
			return "customer/invoicesPage";
		}
			
		if(storedData.getAmt_to_pay()<pmt.getPaid_amt()||
				pmt.getPaid_amt()<=0) {
			model.addAttribute("submit_res", "Please Select a valid payment amount!");
			return "customer/invoicesPage";
		}
		// if the invoice does not belong to the customer
		if(!recService.getRecById(storedData.getRental_id()).getUID().equals(this.getUidFromSession(session))) {
			model.addAttribute("submit_res", "Please Select a valid Invoice that Belongs to you!");
		}		
		// otherwise can make the payment
		// add the payment entry,
		// get today's value for payment date
		java.sql.Date sqlToday = new java.sql.Date(new java.util.Date().getTime());
		pmt.setPaid_date(sqlToday);
		pmt.setPid(null);
		pmtService.savePayment(pmt);
		// then update the state in invoice
		Double remainingAmt = storedData.getAmt_to_pay()-pmt.getPaid_amt();
		if(remainingAmt==0.0) {
			storedData.setPaid_status("COMPLETE");
		}
		storedData.setAmt_to_pay(remainingAmt);
		invoiceService.saveInvoice(storedData);
		model.addAttribute("submit_res", "Successful Made an Payment!");
		return "customer/invoicesPage";
	}
}
