package com.kwchina.core.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.joda.time.DateTime;
import org.joda.time.Days;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.joda.time.Months;


public class DateHelper {

	public static String DEFAULT_DATE_FORMAT = "yyyy-MM-dd";
	
	public static Date getDate(String d) {
		try {
			return new SimpleDateFormat(DEFAULT_DATE_FORMAT).parse(d);
		} catch (ParseException e) {
		}
		return null;
	}
	
	public static String getCustomDateTime(String d,String format) {
		try {
			SimpleDateFormat sf = new SimpleDateFormat(format);
			String aString = sf.format(new SimpleDateFormat(format).parse(d));
			return aString;
		} catch (ParseException e) {
		}
		return null;
	}
	
	public static Date stringToDate(String d){
		//String dF = getDate(d).toString();
		String[] dateString = d.split("-");
		Calendar calendar = Calendar.getInstance();
		calendar.set(Integer.valueOf(dateString[0]), Integer.valueOf(dateString[1]), Integer.valueOf(dateString[2]));
		calendar.add(Calendar.MONTH, -1);
		return calendar.getTime();
	}
	
	
	public static String getString(Date d) {
		return new SimpleDateFormat(DEFAULT_DATE_FORMAT).format(d);
	}
	
	
	public static List<Date> getDaysOfMonth(int year, int month) {
		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.set(year, month, DAY_OF_MONTH, 0, 0, 0);
		beginCalendar.add(Calendar.MONTH, -1);

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.set(year, month, DAY_OF_MONTH, 0, 0, 0);

		List<Date> days = new ArrayList<Date>();
		
		while(beginCalendar.before(endCalendar)) {
			days.add(beginCalendar.getTime());
			beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return days;
	}
	
	public static List<String> getWeekendsOfMonth(int year, int month) {
		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.set(year, month, DAY_OF_MONTH, 0, 0, 0);
		beginCalendar.add(Calendar.MONTH, -1);

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.set(year, month, DAY_OF_MONTH, 0, 0, 0);

		List<String> days = new ArrayList<String>();
		while(beginCalendar.before(endCalendar)) {
			if(beginCalendar.get(Calendar.DAY_OF_WEEK)==1 || beginCalendar.get(Calendar.DAY_OF_WEEK) == 7){
				days.add(new SimpleDateFormat("yyyy-MM-dd").format(beginCalendar.getTime()));
			}
			beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		return days;
	}
	
	public static List<String> getFirstWeekendsOfMonth(int year, int month) {
		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.set(year, month, FIRST_DAY_OF_MONTH, 0, 0, 0);

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.set(year, month, FIRST_DAY_OF_MONTH, 0, 0, 0);
		endCalendar.add(Calendar.MONTH, 1);

		List<String> days = new ArrayList<String>();
		while(beginCalendar.before(endCalendar)) {
			if(beginCalendar.get(Calendar.DAY_OF_WEEK)==1 || beginCalendar.get(Calendar.DAY_OF_WEEK) == 7){
				days.add(new SimpleDateFormat("yyyy-MM-dd").format(beginCalendar.getTime()));
			}
			beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		return days;
	}
	
	public static List<Date> getFirstDaysOfMonth(int year, int month) {
		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.set(year, month, FIRST_DAY_OF_MONTH, 0, 0, 0);
		//beginCalendar.add(Calendar.MONTH, -1);

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.set(year, month, FIRST_DAY_OF_MONTH, 0, 0, 0);
		endCalendar.add(Calendar.MONTH, 1);

		List<Date> days = new ArrayList<Date>();
		
		while(beginCalendar.before(endCalendar)) {
			days.add(beginCalendar.getTime());
			beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return days;
	}
	
	public static List<Date> getDaysOfMonth(int year, int month, Date beginDate, Date endDate) {
		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.set(year, month, DAY_OF_MONTH, 0, 0, 0);
		beginCalendar.add(Calendar.MONTH, -1);

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.set(year, month, DAY_OF_MONTH, 0, 0, 0);
		
		Calendar beginScope = Calendar.getInstance();
		beginScope.setTime(beginDate);

		Calendar endScope = Calendar.getInstance();
		endScope.setTime(endDate);
		
		beginCalendar = beginCalendar.before(beginScope) ? beginScope : beginCalendar;
		endCalendar = endCalendar.after(endScope) ? endScope : endCalendar;

		List<Date> days = new ArrayList<Date>();
		
		while(beginCalendar.before(endCalendar)) {
			days.add(beginCalendar.getTime());
			beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return days;
	}
	
	public static List<Date> getFirstDaysOfMonth(int year, int month, Date beginDate, Date endDate) {
		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.set(year, month, FIRST_DAY_OF_MONTH, 0, 0, 0);
		//beginCalendar.add(Calendar.MONTH, -1);

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.set(year, month, FIRST_DAY_OF_MONTH, 0, 0, 0);
		endCalendar.add(Calendar.MONTH, 1);
		
		Calendar beginScope = Calendar.getInstance();
		beginScope.setTime(beginDate);

		Calendar endScope = Calendar.getInstance();
		endScope.setTime(endDate);
		
		beginCalendar = beginCalendar.before(beginScope) ? beginScope : beginCalendar;
		endCalendar = endCalendar.after(endScope) ? endScope : endCalendar;

		List<Date> days = new ArrayList<Date>();
		
		while(beginCalendar.before(endCalendar)) {
			days.add(beginCalendar.getTime());
			beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		
		return days;
	}

	public static Date addYear(Date year, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(year);
		c.add(Calendar.YEAR, amount);
		
		return c.getTime();
	}
	
	public static Date addMonth(Date month, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(month);
		c.add(Calendar.MONTH, amount);
		
		return c.getTime();
	}
	
	public static Date addHour(Date day, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		c.add(Calendar.HOUR, amount);
		
		return c.getTime();
	}
	
	public static Date addMinute(Date day, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		c.add(Calendar.MINUTE, amount);
		
		return c.getTime();
	}
	
	public static Date addSecond(Date day, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		c.add(Calendar.SECOND, amount);
		
		return c.getTime();
	}
	
	public static Date addDay(Date day, int amount) {
		Calendar c = Calendar.getInstance();
		c.setTime(day);
		c.add(Calendar.DATE, amount);
		
		return c.getTime();
	}
	

	
	public static Timestamp addDay(Timestamp date,int amount){ 
		long time = date.getTime(); 
		time = time + amount * 24 * 60 * 60 * 1000; 
		return new Timestamp(time); 
	} 

	
	// TODO: move begin date and end date into system properties
	public static int FIRST_DAY_OF_MONTH = 1;
	public static int DAY_OF_MONTH = 26;
	
	// parameter should already be financial month
	public static List<Date> getDaysOfMonth(String d) {
		Date date = getDate(d);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return getDaysOfMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
	}
	
	public static List<Date> getDaysOfInTime(String beginDay,String endDay) {
		
		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.setTime(getDate(beginDay));

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.setTime(getDate(endDay));

		List<Date> days = new ArrayList<Date>();
		
		while(beginCalendar.before(endCalendar) || getString(beginCalendar.getTime()).equals(getString(endCalendar.getTime()))) {
			days.add(beginCalendar.getTime());
			beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		return days;
	}
	
	
	public static List<String> getWeekendsOfInTime(String beginDay,String endDay) {
		Calendar beginCalendar = Calendar.getInstance();
		beginCalendar.set(Integer.parseInt(beginDay.split("-")[0]), Integer.parseInt(beginDay.split("-")[1]), Integer.parseInt(beginDay.split("-")[2]), 0, 0, 0);
		beginCalendar.add(Calendar.MONTH, -1);

		Calendar endCalendar = Calendar.getInstance();
		endCalendar.set(Integer.parseInt(endDay.split("-")[0]), Integer.parseInt(endDay.split("-")[1]), Integer.parseInt(endDay.split("-")[2]), 0, 0, 0);

		List<String> days = new ArrayList<String>();
		while(beginCalendar.before(endCalendar) || beginCalendar==endCalendar) {
			if(beginCalendar.get(Calendar.DAY_OF_WEEK)==1 || beginCalendar.get(Calendar.DAY_OF_WEEK) == 7){
				days.add(new SimpleDateFormat("yyyy-MM-dd").format(beginCalendar.getTime()));
			}
			beginCalendar.add(Calendar.DAY_OF_MONTH, 1);
		}
		return days;
	}
	
	public static List<String> getWeekendsOfMonth(String d) {
		Date date = getDate(d);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return getWeekendsOfMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
	}
	
	public static List<String> getFirstWeekendsOfMonth(String d) {
		Date date = getDate(d);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return getFirstWeekendsOfMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
	}
	
	
	
	
	public static List<Date> getFirstDaysOfMonth(String d) {
		Date date = getDate(d);
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		
		return getFirstDaysOfMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH));
	}
	
	public static List<Date> getRemainingDaysOfMonth(Date fromDate) {
		Date financialMonth = getFinancialMonth(fromDate);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(financialMonth);
		
		return getDaysOfMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), fromDate, getNextBeginDateOfMonth(financialMonth));
	}
	
	public static Date getFinancialMonth() {
		return getFinancialMonth(new Date());
	}
	
	public static Date getFinancialMonth(Date actualDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(actualDate);
		
		if (calendar.get(Calendar.DAY_OF_MONTH) >= DAY_OF_MONTH)
			calendar.add(Calendar.MONTH, 1);

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}	
	
	public static Date getFinancialDate() {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(new Date());
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		return calendar.getTime();
	}
	
	
	public static List<Date> getFirstRemainingDaysOfMonth(Date fromDate) {
		Date financialMonth = getFirstFinancialMonth(fromDate);
		
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(financialMonth);
		
		return getFirstDaysOfMonth(calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), fromDate, getFirstNextBeginDateOfMonth(financialMonth));
	}
	
	public static Date getFirstFinancialMonth() {
		return getFirstFinancialMonth(new Date());
	}
	public static Date getFirstFinancialMonth(Date actualDate) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(actualDate);
		
		if (calendar.get(Calendar.DAY_OF_MONTH) >= FIRST_DAY_OF_MONTH)
			calendar.add(Calendar.MONTH, 0);

		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);

		return calendar.getTime();
	}
	
	public static Date getThreeBeginDateOfMonth(String month) {
		
		return getBeginDateOfMonth(DateHelper.getDate(month));
	}
	
	public static Date getBeginDateOfMonth(String month) {
		
		return getBeginDateOfMonth(DateHelper.getDate(month));
	}
	
	public static Date getBeginDateOfMonth(Date month) {
		// Get first and last day for month
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(month);
		calendar.add(Calendar.MONTH, -1);
		calendar.set(Calendar.DAY_OF_MONTH, DAY_OF_MONTH);
		Date beginDate = calendar.getTime();
		
		return beginDate;
	}
	
	public static Date getFirstBeginDateOfMonth(String month) {
		
		return getFirstBeginDateOfMonth(DateHelper.getDate(month));
	}
	
	public static Date getFirstBeginDateOfMonth(Date month) {
		// Get first and last day for month
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(month);
		calendar.add(Calendar.MONTH, 0);
		calendar.set(Calendar.DAY_OF_MONTH, FIRST_DAY_OF_MONTH);
		Date beginDate = calendar.getTime();
		
		return beginDate;
	}
	
	public static Date getNextBeginDateOfMonth(String month) {
		
		return getNextBeginDateOfMonth(DateHelper.getDate(month));
	}
	
	public static Date getNextBeginDateOfMonth(Date month) {
		// Get first and last day for month
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(month);
		calendar.set(Calendar.DAY_OF_MONTH, DAY_OF_MONTH);
		Date endDate = calendar.getTime();
		
		return endDate;
	}
	
	public static Date getFirstNextBeginDateOfMonth(String month) {
		
		return getFirstNextBeginDateOfMonth(DateHelper.getDate(month));
	}
	
	public static Date getFirstNextBeginDateOfMonth(Date month) {
		// Get first and last day for month
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(month);
		calendar.add(Calendar.MONTH, 1);
		calendar.set(Calendar.DAY_OF_MONTH, FIRST_DAY_OF_MONTH);
		Date endDate = calendar.getTime();
		
		return endDate;
	}
	
	public static Date getEndDateOfMonth(String month) {
		
		return getEndDateOfMonth(DateHelper.getDate(month));
	}
	
	public static Date getEndDateOfMonth(Date month) {
		// Get first and last day for month
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(month);
		if(calendar.get(Calendar.DAY_OF_MONTH)>=DAY_OF_MONTH){
			month = addMonth(month, 1);
			calendar.setTime(month);
		}
		calendar.set(Calendar.DAY_OF_MONTH, DAY_OF_MONTH-1);
		Date endDate = calendar.getTime();
		
		return endDate;
	}
	
	
	public static Date getFirstEndDateOfMonth(String month) {
		
		return getFirstEndDateOfMonth(DateHelper.getDate(month));
	}
	
	public static Date getFirstEndDateOfMonth(Date month) {
		// Get first and last day for month
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(month);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.roll(Calendar.DAY_OF_MONTH, -1);
		Date endDate = calendar.getTime();
		
		return endDate;
	}

	public static long getNextRuntimeMinutes(int hours) {
		Calendar now = Calendar.getInstance();
		
		Calendar next = Calendar.getInstance();
		
		if (now.get(Calendar.HOUR_OF_DAY) > hours)
			next.add(Calendar.DAY_OF_MONTH, 1);
		next.set(Calendar.HOUR_OF_DAY, hours);
		next.set(Calendar.MINUTE, 0);
		next.set(Calendar.SECOND, 0);
		
		long diffMinutes = (next.getTimeInMillis() - now.getTimeInMillis()) / (60 * 1000); 
		
		return diffMinutes;
	}
	
	public static String formatIntToTime(int numbers){
		String time = "";
		String hourStr = "";
		String minuteStr = "";
		
		int hour = numbers / 60;
		int minute = numbers % 60;
		
		if (hour<10) {
			hourStr = "0" + String.valueOf(hour);
		}else{
			hourStr = String.valueOf(hour);
		}
		
		if (minute<10) {
			minuteStr = "0" + String.valueOf(minute);
		}else{
			minuteStr = String.valueOf(minute);
		}
		
		time = hourStr + ":" + minuteStr;
		return time;
	}
	
	public static int minutesBetween(Date past ,Date today){
		return Minutes.minutesBetween(new DateTime(past), new DateTime(today)).getMinutes();
	}
	
	public static int hoursBetween(Date past ,Date today){
		return Hours.hoursBetween(new DateTime(past), new DateTime(today)).getHours();
	}
	
	public static int daysBetween(Date past, Date today) {
		return Days.daysBetween(new DateTime(past), new DateTime(today)).getDays();
	}
	
	public static int monthsBetween(Date past, Date today) {
		return Months.monthsBetween(new DateTime(past), new DateTime(today)).getMonths();
	}

	public static void main(String[] args)throws Exception {
		Calendar cal = Calendar.getInstance();
		Date date = new Date();
		date = addDay(date, 1);
		cal.setTime(date);
		System.out.println(cal.get(Calendar.DAY_OF_WEEK));
		
		System.out.println(monthsBetween(getDate("2014-08-25"), new Date()));
		
		System.out.println(new DateTime("2015-05-08"));
		System.out.println(new DateTime("2015-05-30"));
		System.out.println(daysBetween(getDate("2015-05-09"), getDate("2015-05-30")));
		
		Timestamp tm = Timestamp.valueOf("2016-07-20 15:14:34");
		System.out.println(addDay(tm, -1));
		
		Timestamp tm1 = Timestamp.valueOf("2016-07-20 15:43:34");
		
		System.out.println(minutesBetween(tm, tm1));
	}
	
	public static long MINUTES_ONE_DAY = 24 * 60;
}
