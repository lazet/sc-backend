package org.sc.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;


public class DateUtil {
    private static Log log = LogFactory.getLog(DateUtil.class);
    private static String datePattern = "yyyy-MM-dd";
    private static String timePattern = datePattern + " HH:MM a";
    public final static int WeekSpan = 7;
    public static int month[] = {
        31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};

    /**
     * 判断是否闰年
     *
     * @param year
     * @return
     */
    private static boolean LeapYear(int year) {
        if ((year % 4 == 0 && year % 100 != 0) || (year % 400 == 0)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 对个位数的月份之前补零
     *
     * @param month
     * @return
     */
    private static String impleMonth(int month) {
        String monthStr = new Integer(month).toString();
        if (monthStr.length() == 1) {
            monthStr = "0" + monthStr;
        }
        return monthStr;
    }

    /**
     * 对个位数的日子之前补零
     *
     * @param day
     * @return
     */
    private static String impleDay(int day) {
        String dayStr = new Integer(day).toString();
        if (dayStr.length() == 1) {
            dayStr = "0" + dayStr;
        }
        return dayStr;
    }

    /**
     * 得到当前周的第一天（周一）的日期
     *
     * @return
     */
    public static String getWeekFirstDate() {
        Date da = new Date();
        int dayOfWeek = da.getDay(); // 当前日期是星期几
        if (dayOfWeek != 0) {
            long fromWeekFirstInMillis = (dayOfWeek - 1) * 24 * 60 * 60 * 1000; // 与该周第一天相隔的毫秒数
            da.setTime(da.getTime() - fromWeekFirstInMillis);

        } else {
            //当前日期为一周的最后一天（周日）
            long fromWeekFirstInMillis = 6 * 24 * 60 * 60 * 1000; // 与该周第一天相隔的毫秒数
            da.setTime(da.getTime() - fromWeekFirstInMillis);

        }
        String weekFirstDay = new Integer(da.getYear() + 1900).toString();
        weekFirstDay = weekFirstDay + "-" + impleMonth(da.getMonth() + 1);
        weekFirstDay = weekFirstDay + "-" + impleDay(da.getDate());
        return weekFirstDay;
    }

    /**
     * 得到当前周的最后一天（周日）的日期
     * @return
     */
    public static String getWeekLastDate() {
        Date da = new Date();
        int dayOfWeek = da.getDay(); // 当前日期是星期几
        //如果当前天是星期日，则当前天为最后一天
        if (dayOfWeek!=0) {
            long toWeekLastInMillis = (WeekSpan - dayOfWeek) * 24 * 60 * 60 * 1000; // 与该周最后一天相隔的毫秒数
            da.setTime(da.getTime() + toWeekLastInMillis);
        }
        String weekLastDay = new Integer(da.getYear() + 1900).toString();
        weekLastDay = weekLastDay + "-" + impleMonth(da.getMonth() + 1);
        weekLastDay = weekLastDay + "-" + impleDay(da.getDate());
        return weekLastDay;
    }

    /**
     * 得到当前月的第一天的日期
     * @return
     */
    public static String getMonthFirstDate() {
        Date da = new Date();
        long dayOfMonth = da.getDate(); // 当前日期是本月第几天
        long fromMonthFirstInMillis = (dayOfMonth - 1) * 24 * 60 * 60 * 1000; // 与该月第一天相隔的毫秒数
        da.setTime(da.getTime() - fromMonthFirstInMillis);
        String MonthFirstDay = new Integer(da.getYear() + 1900).toString();
        MonthFirstDay = MonthFirstDay + "-" + impleMonth(da.getMonth() + 1);
        MonthFirstDay = MonthFirstDay + "-" + impleDay(da.getDate());
        return MonthFirstDay;
    }

    /**
     * 得到当前月的最后一天的日期
     * @return
     */
    public static String getMonthLastDate() {
        Date da = new Date();
        long dayOfMonth = da.getDate(); // 当前日期是本月第几天
        int monthSpan = 0;
        if ((da.getMonth() + 1) == 2) {
            if (LeapYear(da.getYear() + 1900)) {
                monthSpan = 29;
            } else {
                monthSpan = month[da.getMonth()];
            }
        } else {
            monthSpan = month[da.getMonth()];

        }
        // System.out.println("the montheSpan: "+monthSpan);

        long toMonthLastInMillis = (monthSpan - dayOfMonth) * 24 * 60 * 60 * 1000; // 与该月最后一天相隔的毫秒数
        da.setTime(da.getTime() + toMonthLastInMillis);
        String MonthLastDay = new Integer(da.getYear() + 1900).toString();
        MonthLastDay = MonthLastDay + "-" + impleMonth(da.getMonth() + 1);
        MonthLastDay = MonthLastDay + "-" + impleDay(da.getDate());
        return MonthLastDay;
    }

    /**
     * 得到当前的日期字符串，日期格式为 YYYY-MM-DD
     *
     * @return
     */
    public static String getCurrentDate() {
        Date da = new Date();
        String currentDay = new Integer(da.getYear() + 1900).toString();
        currentDay = currentDay + "-" + impleMonth(da.getMonth() + 1);
        currentDay = currentDay + "-" + impleDay(da.getDate());
        return currentDay;
    }


    //按长度把字符串前补0
    private static String strLen1(String s, int len) {
        if (isNullStr(s)) {
            s = "";

        }
        int strLen = s.length();
        for (int i = 0; i < len - strLen; i++) {
            s = "0" + s;
        }
        return s;
    }

    private static String strLen(String s, int len) {
        if (isNullStr(s)) {
            s = "";
        }
        if (s.length() == 8) {
            return s;
        }
        for (int i = 0; i < len - s.length(); i++) {
            s = "0" + s;
            if (s.length() == 8) {
                break;
            }
        }
        return s;
    }

    /**
     * 判断字符串是否为空
     * @param s
     * @return
     */
    private static boolean isNullStr(String s) {
        if (s == null || s.trim().length() <= 0) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 判断字符串数组是否为空
     * @param s
     * @return
     */
    private static boolean isNullStr(String[] s) {
        if ((s == null) || (s.length <= 0)) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * 返回日历的年字符串
     * @param cal
     * @return
     */
    public static String getYear(Calendar cal) {
        return String.valueOf(cal.get(cal.YEAR));
    }

    /**
     * 返回日历的月字符串(两位)
     * @param cal
     * @return
     */
    public static String getMonth(Calendar cal) {
        return strLen(String.valueOf(cal.get(cal.MONTH) + 1), 2);
    }

    /**
     * 返回日历的日字符串(两位)
     * @param cal
     * @return
     */
    public static String getDay(Calendar cal) {
        return strLen(String.valueOf(cal.get(cal.DAY_OF_MONTH)), 2);
    }

    /**
     * 返回日历的时字符串(两位)
     * @param cal
     * @return
     */
    public static String getHour(Calendar cal) {
        return strLen(String.valueOf(cal.get(cal.HOUR_OF_DAY)), 2);
    }

    /**
     * 返回日历的分字符串(两位)
     * @param cal
     * @return
     */
    public static String getMinute(Calendar cal) {
        return strLen(String.valueOf(cal.get(cal.MINUTE)), 2);
    }

    /**
     * 返回日历的秒字符串(两位)
     * @param cal
     * @return
     */
    public static String getSecond(Calendar cal) {
        return strLen(String.valueOf(cal.get(cal.SECOND)), 2);
    }

    /**
     * 返回日历的日期字符串（格式："yyyy-mm-dd"）
     * @param cal
     * @return
     */
    public static String getDateStr(Calendar cal) {
        return getYear(cal) + "-" + getMonth(cal) + "-" + getDay(cal);
    }

    /**
     * 返回日历的时间字符串（格式："hh:ss"）
     * @param cal
     * @return
     */
    public static String getTimeStr(Calendar cal) {
        return getHour(cal) + ":" + getMinute(cal) + ":" + getSecond(cal);
    }

    /**
     * 返回日历的日期时间字符串（格式："yyyy-mm-dd hh:ss"）
     * @param cal
     * @return
     */
    public static String getDate(Calendar cal) {
        return getDateStr(cal) + " " + getTimeStr(cal);
    }

    /**
     * 返回日期字符串("yyyy-mm-dd hh:ss:mm")的年
     * @param s
     * @return
     */
    public static int getYear(String s) {
        if (s == null || s.length() < 10) {
            return 1970;
        }
        return Integer.parseInt(s.substring(0, 4));
    }

    /**
     * 返回日期字符串("yyyy-mm-dd hh:ss:mm")的月
     * @param s
     * @return
     */
    public static int getMonth(String s) {
        if (s == null || s.length() < 10) {
            return 1;
        }
        return Integer.parseInt(s.substring(5, 7));
    }

    /**
     * 返回日期字符串("yyyy-mm-dd hh:ss:mm")的日
     * @param s
     * @return
     */
    public static int getDay(String s) {
        if (s == null || s.length() < 10) {
            return 1;
        }
        return Integer.parseInt(s.substring(8, 10));
    }
    /**
     * 返回日期字符串("yyyy-mm-dd hh:ss:mm")的时
     * @param s
     * @return
     */
    public static int getHour(String s) {
        if (s == null || s.length() < 16) {
            return 0;
        }
        return Integer.parseInt(s.substring(11, 13));
    }

    /**
     * 返回日期字符串("yyyy-mm-dd hh:ss:mm")的分
     * @param s
     * @return
     */
    public static int getMinute(String s) {
        if (s == null || s.length() < 16) {
            return 0;
        }
        return Integer.parseInt(s.substring(14, 16));
    }

    /**
     * 返回日期字符串("yyyy-mm-dd hh:ss:mm")的秒
     * @param s
     * @return
     */
    public static int getSecond(String s) {
        if (s == null || s.length() < 18) {
            return 0;
        }
        return Integer.parseInt(s.substring(16, 18));
    }

    /**
     * 返回日期时间字符串对应的日历（格式："yyyy-mm-dd hh:ss"）
     * @param s
     * @return
     */
    public static Calendar getCal(String s) {
        Calendar cal = Calendar.getInstance();
        cal.set(getYear(s), getMonth(s), getDay(s), getHour(s), getMinute(s),
                getSecond(s));
        return cal;
    }

    /**
     * 返回日期时间字符串对应的SQL日期（格式："yyyy-mm-dd hh:mi:ss"）
     * @param s
     * @return
     */
    public static java.sql.Date getSqlDate(String s) {
        return java.sql.Date.valueOf(s);
    }

    /**
     * 返回当天日期对应的SQL日期（）
     * @return
     */
    public static java.sql.Date getSqlDate() {
        return java.sql.Date.valueOf(getNowDate());
    }

    /**
     * 取当前日期时间的字符串，格式为"yyyy-mm-dd hh:mi:ss"
     * @return
     */
    public static String getNow() {
        Calendar now = Calendar.getInstance();
        return getDateStr(now) + " " + getTimeStr(now);
    }

    /**
     * 取当前日期的字符串，格式为"yyyy-mm-dd"
     * @return
     */
    public static String getNowDate() {
        Calendar now = Calendar.getInstance();
        return getDateStr(now);
    }

    /**
     * 取当前时间的字符串，格式为"hh:ss"
     * @return
     */
    public static String getNowTime() {
        Calendar now = Calendar.getInstance();
        return getTimeStr(now);
    }
    /**
     * 取当前时间的字符串
     * @return
     */
    public static String getCurrentTimeMillisStr() {

        return (new Long(System.currentTimeMillis()).toString());
    }

    /**
     * 根据当前时间的毫秒数（相对于January 1, 1970 00:00:00），取当前时间的字符串
     * @param time
     * @return
     */
    public static String changTimeMillisToStr(String time) {
        long t = 0l;
        try {
            t = Long.parseLong(time);
        } catch (Exception e) {
            return "";
        }
        Date date = new Date();
        date.setTime(t);
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);

        return getDateStr(cal) + " " + getTimeStr(cal);
    }
    /**
     * 根据当前时间的毫秒数（相对于January 1, 1970 00:00:00），取当前时间的字符串
     * @param time
     * @return
     */
    public static String changTimeMillisToStr(long time) {
        String str = "";
        try {
            str = Long.toString(time);
        } catch (Exception e) {
        }
        return changTimeMillisToStr(str);
    }

    /**
     * 格式化字符串为日期的函数.
     *
     * @param strDate 字符串.
     * @param format  转换格式如:"yyyy-MM-dd mm:ss"
     * @return 字符串包含的日期.
     */
    public static Date parseDate(String strDate, String format) {
        try {
            if (strDate == null || strDate.equals(""))
                return null;
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
            return simpleDateFormat.parse(strDate);
        } catch (Exception e) {
        }
        return new Date();
    }

    /**
     * 格式化日期为字符串函数.
     *
     * @param date   日期.
     * @param format 转换格式."yyyy-MM-dd mm:ss"
     * @return 日期转化来的字符串.
     */
    public static String formatDate(Date date, String format) {
        if (date == null)
            return "";
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
        return simpleDateFormat.format(date);
    }

    /**
     * 返回缺省的时间模式 (yyyy-MM-dd)
     *
     * @return a string representing the date pattern on the UI
     */
    public static String getDatePattern() {
        return datePattern;
    }

    /**
     * 根据日期返回yyyy-MM-dd格式的日期
     *
     * @param aDate date from database as a string
     * @return formatted string for the ui
     */
    public static final String getDate(Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate != null) {
            df = new SimpleDateFormat(datePattern);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * 根据指定的格式，将一个日期字符串转换为 Date对象。
     *
     * @param aMask   the date pattern the string is in
     * @param strDate a string representation of a date
     * @return a converted Date object
     * @throws ParseException
     * @see java.text.SimpleDateFormat
     */
    public static final Date convertStringToDate(String aMask, String strDate)
            throws ParseException {
        SimpleDateFormat df = null;
        Date date = null;
        df = new SimpleDateFormat(aMask);

        if (log.isDebugEnabled()) {
            log.debug("converting '" + strDate + "' to date with mask '"
                    + aMask + "'");
        }

        try {
            date = df.parse(strDate);
        } catch (ParseException pe) {
            //log.error("ParseException: " + pe);
            throw new ParseException(pe.getMessage(), pe.getErrorOffset());
        }

        return (date);
    }

    /**
     * 用yyyy-MM-dd HH:MM a日期格式返回当前的时间
     *
     *
     * @param theTime the current time
     * @return the current date/time
     */
    public static String getTimeNow(Date theTime) {
        return getDateTime(timePattern, theTime);
    }

    /**
     * 返回当前的日期，格式yyyy-MM-dd
     * @return the current date
     * @throws ParseException
     */
    public static Calendar getToday() throws ParseException {
        Date today = new Date();
        SimpleDateFormat df = new SimpleDateFormat(datePattern);

        // This seems like quite a hack (date -> string -> date),
        // but it works ;-)
        String todayAsString = df.format(today);
        Calendar cal = new GregorianCalendar();
        cal.setTime(convertStringToDate(todayAsString));

        return cal;
    }

    /**
     * 根据指定的日前格式，返回日期对象对应的字符串。
     * @param aMask the date pattern the string is in
     * @param aDate a date object
     * @return a formatted string representation of the date
     * @see java.text.SimpleDateFormat
     */
    public static final String getDateTime(String aMask, Date aDate) {
        SimpleDateFormat df = null;
        String returnValue = "";

        if (aDate == null) {
            log.error("aDate is null!");
        } else {
            df = new SimpleDateFormat(aMask);
            returnValue = df.format(aDate);
        }

        return (returnValue);
    }

    /**
     * 根据Date 对象返回 其日期格式的字符串，日期格式：yyyy-MM-dd
     * @param aDate A date to convert
     * @return a string representation of the date
     */
    public static final String convertDateToString(Date aDate) {
        return getDateTime(datePattern, aDate);
    }

    /**
     * 使用日期格式（yyyy-MM-dd）转换一个字符串为Date对象
     * @param strDate the date to convert (in format yyyy-MM-dd)
     * @return a date object
     * @throws ParseException
     */
    public static Date convertStringToDate(String strDate)
            throws ParseException {
        Date aDate = null;

        try {
            if (log.isDebugEnabled()) {
                log.debug("converting date with pattern: " + datePattern);
            }

            aDate = convertStringToDate(datePattern, strDate);
        } catch (ParseException pe) {
            log.error("Could not convert '" + strDate
                    + "' to a date, throwing exception");
            pe.printStackTrace();
            throw new ParseException(pe.getMessage(),
                    pe.getErrorOffset());

        }

        return aDate;
    }

    /**
     * 得到当前月的最后一天的日期
     * @param da
     * @return
     */
    public static String getLastDate(Date da) {
        int monthSpan = 0;
        int monthValue = da.getMonth();
        if ((monthValue) == 2) {
            if (LeapYear(da.getYear() + 1900)) {
                monthSpan = 29;
            } else {
                monthSpan = month[monthValue - 1];
            }
        } else {
            if (monthValue == 0) {//因为以前都设置12月，但是data方法中只有0-11月
                monthSpan = month[11];
                monthValue = 12;
            } else {
                monthSpan = month[monthValue - 1];
            }

        }
        String MonthLastDay = da.getYear() + "";
        MonthLastDay += "-" + impleMonth(monthValue);
        MonthLastDay += "-" + monthSpan;
        return MonthLastDay;
    }

    /**
     * 得到一个月的最后一天的日期
     *
     * @param year
     * @param month
     * @return
     */
    public static String getEndDayOfMonth(int year, int month) {
        String str = null;
        java.util.Date date = new java.util.Date();
        date.setYear(year);
        date.setMonth(month);
        str = DateUtil.getLastDate(date);
        return str;
    }
}