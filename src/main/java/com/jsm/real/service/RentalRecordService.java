package com.jsm.real.service;

import java.util.List;

import com.jsm.real.entity.Book;
import com.jsm.real.entity.Copy;
import com.jsm.real.entity.RentalRecord;

public interface RentalRecordService {
	// generate an rental record
	Long genRentalRec(RentalRecord rentalRec);
	// update the rental record with the returned date (today) and return state
	Long uptRentalRec(RentalRecord rentalRec);
	// fetch all rental record
	void fetchAllRentalRecs(List<RentalRecord> recParts, List<Copy> copyParts, List<Book> bookParts);
	// query rental record by filter, by
	void qryRentalRecordsBy(RentalRecord rec, Long bid, String bookName, List<RentalRecord> recParts, List<Copy> copyParts, List<Book> bookParts);
	// check whether the record is of status borrowed
	boolean isBorrowed(Long recId);
	// get rental record by Id
	RentalRecord getRecById(Long recId);
	
}
