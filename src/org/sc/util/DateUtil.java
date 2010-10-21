package org.sc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.lang.time.DateFormatUtils;;


public class DateUtil {
	public static String getCurrentDate(){
		return DateFormatUtils.format(new Date(), "yyyy-MM-dd");
	}
	public static String getCurrentTime(){
		return DateFormatUtils.format(new Date(), "yyyy-MM-dd HH:mm:ss");
	}
}