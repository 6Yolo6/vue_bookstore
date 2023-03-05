package com.example.bookstore.utils;
import cn.hutool.core.date.DateUtil;

import java.io.File;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

public class DateTool {
	public final static String SHORTYEAR = "yyyy";
	public final static String SHORTMON = "yyyy-MM";
	public final static String SHORTIME = "yyyy-MM-dd";
	public final static String LONGTIME = "yyyy-MM-dd HH:mm:ss";
	private static SimpleDateFormat longFormat = new SimpleDateFormat(LONGTIME);
	private static SimpleDateFormat shortFormat = new SimpleDateFormat(SHORTIME);
	private static SimpleDateFormat shortMonFormat = new SimpleDateFormat(SHORTMON);
	private static SimpleDateFormat shortYearFormat = new SimpleDateFormat(SHORTYEAR);
		
	/**返回当前日期
	 * 
	 * @return yyyyMMdd
	 */
	public static String getCurrDay(){
		Calendar calendar=Calendar.getInstance();
		return shortFormat.format(calendar.getTime());
	}



	/**返回当前月份呢
	 * 
	 * @return yyyyMM
	 */
	public static String getCurrMon(){
		Calendar calendar=Calendar.getInstance();
		return shortMonFormat.format(calendar.getTime());
	}

	/**
	 * 返回当前年份
	 *
	 * @return
	 */
	public static String getCurrYear() {
		Calendar calendar = Calendar.getInstance();
		return shortYearFormat.format(calendar.getTime());
	}
	
	/**返回当前时间
	 * 
	 * @return yyyy-MM-dd HH:mm:ss
	 */
	public static String getCurrTime(){
		Calendar calendar=Calendar.getInstance();
		return longFormat.format(calendar.getTime());
	}
	//	返回转换后的时间
	public static Date parseTime(String time) throws ParseException {
		return longFormat.parse(time);
	}
	/**
	 * 		
	 * @描述  计算time和当前时间相差几天
	 * @作者  chrimer(林江毅)
	 * @创建时间 2015年3月5日 上午10:44:24
	 */
	public static Long countDay(String time) {
		String currentTime = getCurrDay();
		
		DateFormat df=new SimpleDateFormat(SHORTIME);
		Calendar c1=Calendar.getInstance();
		Calendar c2=Calendar.getInstance();
		try
		{
			c1.setTime(df.parse(time));
			c2.setTime(df.parse(currentTime));
		}catch(java.text.ParseException e){
			System.err.println("格式不正确");
		}
		Long day = (c1.getTimeInMillis()-c2.getTimeInMillis())/1000/60/60/24;
		return day;
	}

	public static String getFirstDay(String time) {

		DateFormat df=new SimpleDateFormat(SHORTMON);
		Calendar c1=Calendar.getInstance();
		try
		{
			c1.setTime(df.parse(time));
			c1.set(Calendar.DAY_OF_MONTH, 1);
			Date firstDayOfMonth = c1.getTime();
			return shortFormat.format(firstDayOfMonth.getTime());
		}catch(java.text.ParseException e){
			System.err.println("格式不正确");
		}

		return "";
	}
	public static String getLastDay(String time) {

		DateFormat df=new SimpleDateFormat(SHORTMON);
		Calendar c1=Calendar.getInstance();
		try
		{
			c1.setTime(df.parse(time));
			c1.set(Calendar.DAY_OF_MONTH, 1);
			c1.add(Calendar.MONTH, 1);
			c1.add(Calendar.DAY_OF_MONTH, -1);
			Date lastDayOfMonth = c1.getTime();
			return shortFormat.format(lastDayOfMonth.getTime());
		}catch(Exception e){
			System.err.println("格式不正确");
		}

		return "";
	}

	public static List<String> getEveryDay(String time) {

		String firstDay = getFirstDay(time);
		String lastDay = getLastDay(time);

		return getDaysBetween(firstDay,lastDay);
	}

	public static List<String> getDaysBetween(String firstDay,String lastDay) {


		List<String> listDays = new ArrayList<>();
		DateFormat df=new SimpleDateFormat(SHORTIME);
		Calendar c1=Calendar.getInstance();
		try
		{
			Date dateFirst = new SimpleDateFormat(SHORTIME).parse(firstDay);
			Date dateLast = new SimpleDateFormat(SHORTIME).parse(lastDay);

			c1.setTime(dateFirst);
			listDays.add(df.format(c1.getTime()));
			while(c1.getTime().compareTo(dateLast)<0){
				c1.add(Calendar.DAY_OF_MONTH, 1);
				listDays.add(df.format(c1.getTime()));
			}

			return listDays;
		}catch(Exception e){
			System.err.println("格式不正确");
		}

		return null;
	}

	/**
	 * 获取一年中的12个月份
	 *
	 * @param time
	 * @return
	 */
	public static List<String> getMonthsBetween(String time) {
		List<String> listMonths = new ArrayList<>();
		for (int i = 1; i <= 12; i++) {
			listMonths.add(time + (i >= 10 ? "-" : "-0") + i);
		}
		return listMonths;
	}

	public static void main(String[] args) {
//		System.out.println(DateTool.getFirstDay("2022-02"));
//		System.out.println(DateTool.getLastDay("2022-02"));
//		List<String> list = getEveryDay("2022-02");
//		if(list!=null){
//			for(String item : list){
//				System.out.print(item+" ");
//			}
//		}
//		Date date = DateUtil.parse("2022-11-28 10:48:34");

//		System.out.println(DateUtil.offset(date, DateField.MINUTE, 15));
		Date date = new Date();
		System.out.println(date);
	}
	
	
}
