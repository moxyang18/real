package com.jsm.real.util;

import java.sql.Date;

public class DateCustUtils {
    public final static long SECOND_MILLIS = 1000;
    public final static long MINUTE_MILLIS = SECOND_MILLIS*60;
    public final static long HOUR_MILLIS = MINUTE_MILLIS*60;
    public final static long DAY_MILLIS = HOUR_MILLIS*24;
    public static Date addDays(Date date, int days) {
        return new Date(date.getTime() + 1000 * 60 * 60 * 24 * days);
    }
	
    public static int daysDiff( Date earlierDate, Date laterDate )
    {
        if( earlierDate == null || laterDate == null ) return 0;
        
        return (int)((laterDate.getTime()/DAY_MILLIS) - (earlierDate.getTime()/DAY_MILLIS));
    }
    
}
