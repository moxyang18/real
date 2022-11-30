package com.jsm.real.util;

import java.sql.Date;
import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.UUID;

import org.springframework.util.DigestUtils;

public class StringUtils {

	/* Convert 02/11/2022 format to  2022-02-11 for DateConversion
	 * 
	 */
	public static String ToSqlDateStr(String pre) {
	    //format the string, and then convert to sql Date type
	    String datePostStr = pre.replaceAll("/", "-");
		String post = "";
		String[] subStrs =  datePostStr.split("-");
		post += subStrs[2] + "-";
		post += subStrs[0] + "-";
		post += subStrs[1];
		return post;
	}
	
	/* Convert 02/11/2022 format to Sql.Date type
	 * 
	 */
	public static Date ToSqlDate(String pre) {
		return Date.valueOf(ToSqlDateStr(pre));
	}
	
	public static Date toSqlDate(String pre) {
		return Date.valueOf(pre);
	}
	
	/* Sanitize Input String to Prevent SQL injection
	 * 
	 */
	public static String sqlSanitize(String input) {
		if(input==null) return "";
		return input.trim();
	}
	
	// 2022-11-23T21:20 localdatetime to timestamp in MySql
	public static Timestamp ToSqlTimeStamp(String pre) throws ParseException {
	    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm");
	    pre = pre.replace("T", " ");
	    //return dateFormat.parse(pre).toString();
	    //return parsedDate.toString();
	    Timestamp timestamp = new java.sql.Timestamp(dateFormat.parse(pre).getTime());
	    return timestamp;
	}
	
	
	//md5 Encryption Algorithm: Encrypt the string with salt for three times
	public static String getMd5String(String pwd, String salt) {
		for(int i = 0; i<3; i++) {
			pwd = DigestUtils.md5DigestAsHex((salt+pwd+salt).getBytes()).toUpperCase();
		}
		return pwd;
	}
	
	public static boolean containsUnsafe(String str) {
		if(str.trim().length()!=str.length()) return true;
		if(str.indexOf("'")!=-1) return true;
		return false;
	}
	
	public static void main(String[] args) throws ParseException {
		String pre = "2022-11-23T21:20";
		System.out.println(ToSqlTimeStamp(pre).toString());
		
		String salt = UUID.randomUUID().toString().toUpperCase();
		System.out.println(salt);
		System.out.println(getMd5String("11111111",salt));
	}
	
}
