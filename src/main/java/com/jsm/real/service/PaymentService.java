package com.jsm.real.service;

import java.util.List;

import com.jsm.real.entity.Invoice;
import com.jsm.real.entity.Payment;
import com.jsm.real.entity.RentalRecord;

public interface PaymentService {
	// make a payment
	Long savePayment(Payment pmt);
	// query all payments
	void fetchAllPayments(List<Payment> pmtParts, List<Invoice> invoiceParts, List<RentalRecord> recParts); 
	// query payments by filters
	void queryPaymentsBy(Payment rec, Long custId, List<Payment> pmtParts, List<Invoice> invoiceParts, List<RentalRecord> recParts);
}
