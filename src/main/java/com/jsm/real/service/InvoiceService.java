package com.jsm.real.service;

import java.util.List;

import com.jsm.real.entity.Book;
import com.jsm.real.entity.Copy;
import com.jsm.real.entity.Invoice;
import com.jsm.real.entity.RentalRecord;

public interface InvoiceService {
	// generate an invoice upon update of the rental record
	Long saveInvoice(Invoice invoice);
	// query all invoice info
	void fetchAllInvoices(List<Invoice> invoiceParts,
			List<RentalRecord> recParts, List<Copy> copyParts, List<Book> bookParts);
	
	// query invoice info based on condition
	void queryInvoicesBy(Invoice invoice, Long copyId, Long custId, List<Invoice> invoiceParts,
			List<RentalRecord> recParts, List<Copy> copyParts, List<Book> bookParts);
	
	// query invoice by invoice_id
	Invoice getInvoiceById(Long invoice_id);
}
