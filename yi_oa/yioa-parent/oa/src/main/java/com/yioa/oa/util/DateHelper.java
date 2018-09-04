package com.yioa.oa.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;


/**
 * 2012
 *
 *
 */
public final class DateHelper {

    public static final Map<Integer,String> ALL_FORMAT = new HashMap<Integer,String>();

    static {
        ALL_FORMAT.put(19, "yyyy-MM-dd HH:mm:ss");
        ALL_FORMAT.put(10, "yyyy-MM-dd");
        ALL_FORMAT.put(7, "yyyy-MM");
        ALL_FORMAT.put(14, "yyyyMMddHHmmss");
        ALL_FORMAT.put(8, "yyyyMMdd");
        ALL_FORMAT.put(6, "HHmmss");
        ALL_FORMAT.put(16, "yyyy-MM-dd HH:mm");
        ALL_FORMAT.put(13, "yyyy-MM-dd HH");
        ALL_FORMAT.put(4, "yyyy");
        ALL_FORMAT.put(5, "HH:mm");
    }
	
	private static final Logger logger = Logger.getLogger(DateHelper.class);
	public static final int AffterMinuteAutoOffline = -31;
	public static final int TYPE_DAY= 0;
	public static final int TYPE_HOUR = 3;
	public static final int TYPE_MINITE = 4;
	public static final int TYPE_SECOND = 5;
	public static final int TYPE_MONTH = 1;
	public static final int TYPE_YEAR = 2;

	public static Date DATE_UNLIMIT;

	static {
		try {
			DATE_UNLIMIT = new SimpleDateFormat("yyyyMMddHHmmss").parse("20301230235959");
		} catch (ParseException e) {
			logger.error("DateHelper init DATE_UNLIMIT error", e);
		}
	}

	private DateHelper() {
	}
	/**
	 * 将秒数改为xx天xx小时xx分xx秒<br />
	 * 如 formatTime（60） --> 1分<br />
	 * 如 formatTime（70） --> 1分10秒<br />
	 * 类推
	 * */
	public static String formatTime(Long t){
	    return null == t ? "" : formatTime(t.intValue(),false);
	}
	
	/**
	 * 将秒数改为xx天xx小时xx分xx秒<br />
	 * 如 formatTime（60） --> 1分<br />
	 * 如 formatTime（70） --> 1分10秒<br />
	 * 类推
	 * */
	public static String formatTime(int t){
	    return formatTime(t,false);
	}
	/**
	 * 将秒数改为xx天xx小时xx分xx秒<br />
	 * 如 formatTime（60） --> 1分<br />
	 * 如 formatTime（70） --> 1分10秒<br />
	 * 类推
	 * */
	public static String formatTime(int t, boolean isShowEmpty){
	    int[] s = {60,60,24,365,1000000};
	    String[] ss = {"秒","分","小时","天","年"};
	    String r = "";
	    for(int i = 0;i<s.length;i++){
	       r = (t%s[i] == 0 && !isShowEmpty) ? r : t%s[i] + ss[i]+ r;
	       t = (t/s[i]);
	       if(t==0)break;
	    }
	    return r;
	}

    public static java.util.Date getDate(String date) {
        if (StringUtils.isEmpty(date))
            return null;
        String format = ALL_FORMAT.get(date.length());
        try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			long time = sdf.parse(date).getTime();
			return new java.util.Date(time);
		} catch (ParseException e) {
			 logger.error("unsupported date format : " + date);
		     throw new RuntimeException("unsupported date format : " + date);
		}
    }
    
    public static String formatDate(Date date) {
    	return formatDate(date, "yyyy-MM-dd HH:mm:ss");
    }
       
    public static String formatDate(Date date, String format) {
    	 SimpleDateFormat sdf = new SimpleDateFormat(format);
         String res = sdf.format(date);
         return res;
	}

    public static java.util.Date getDate(String date, String dateFormat) {
        if (StringUtils.isEmpty(date))
            return null;
        try {
            SimpleDateFormat sdf = new SimpleDateFormat(dateFormat);
            long time = sdf.parse(date).getTime();
            return new java.util.Date(time);
        } catch (Exception e) {
            logger.error("unsupported date format : " + date);
            throw new RuntimeException("时间格式不正确：" + date+"，正确格式："+dateFormat);
        }
    }


	/**
	 * 偏移时间
	 *
	 * @param date
	 *            Date 初始时间
	 * @param second
	 *            long 偏移秒数
	 * @return Date
	 */
	public static Date offsetSecond(Date date, long seconds) {
		long time = date.getTime();
		time = time + (seconds * 1000);
		return new Date(time);
	}

	/**
	 * 偏移时间
	 *
	 * @param date
	 *            Date 初始时间
	 * @param minute
	 *            long 偏移分钟数
	 *
	 * @return Date
	 */
	public static Date offsetMinute(Date date, long minutes) {
		return offsetSecond(date, 60 * minutes);
	}

	/**
	 * 偏移时间
	 *
	 * @param date
	 *            Date 初始时间
	 * @param hour
	 *            long 偏移小时数
	 *
	 * @return Date
	 */
	public static Date offsetHour(Date date, long hours) {
		return offsetMinute(date, 60 * hours);
	}

	/**
	 * 偏移时间
	 *
	 * @param date
	 *            Date 初始时间
	 * @param day
	 *            long 偏移天数
	 * @return Date
	 */
	public static Date offsetDay(Date date, int days) {
		if(date == null)
			return null;
		return offsetHour(date, 24 * days);
	}
	/**
	 * @param baseDate 偏移基准
	 * @param unit 偏移数量
	 * @param timeOffsetType 偏移类型：DAY,HOU,MIN
	 * */
	public static Date offset(Date baseDate,int unit,String timeOffsetType) throws Exception{
		switch (timeOffsetType) {
		case "DAY":
			return offsetDay(baseDate, unit);
		case "HOU":
			return offsetHour(baseDate, unit);
		case "MIN":
			return offsetMinute(baseDate, unit);
		default:
			throw new RuntimeException("Unknow time offset type : "+timeOffsetType);
		}
	}

	/**
	 * 偏移时间
	 *
	 * @param date
	 *            Date 初始时间
	 * @param week
	 *            long 偏移周数
	 * @return Date
	 */
	public static java.util.Date offsetWeek(java.util.Date date, int weeks) {
		return offsetDay(date, 7 * weeks);
	}

	/**
	 * 得到本月的最后时间
	 *
	 * @param date
	 *            Date 要偏移的时间
	 * @return Date
	 */
	public static java.util.Date getMonthLastday(java.util.Date date) {
		Date newDate = new Date(date.getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(newDate);
		int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, maxDay);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		newDate.setTime(calendar.getTimeInMillis());
		return newDate;
	}

	/**
	 * 得到传入当月的天数（即最后一天的日子）
	 *
	 * @param date
	 * @return
	 */
	public static int getMonthMaxDay(java.util.Date date) {
		Date newDate = new Date(date.getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(newDate);
		int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		return maxDay;
	}

	/**
	 * 得到传入时间的当月最后一天
	 *
	 * @param date
	 *            Date 要偏移的时间
	 * @return Date
	 */
	public static java.util.Date getMonthLastDay(java.util.Date date) {
		Date newDate = new Date(date.getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(newDate);
		int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		calendar.set(Calendar.DAY_OF_MONTH, maxDay);
		calendar.set(Calendar.HOUR_OF_DAY, 23);
		calendar.set(Calendar.MINUTE, 59);
		calendar.set(Calendar.SECOND, 59);
		newDate.setTime(calendar.getTimeInMillis());
		return newDate;
	}

	/**
	 * 得到传入时间的前一天
	 *
	 * @param date
	 *            Date 要偏移的时间
	 * @return Date
	 */
	public static java.util.Date getYesterday(java.util.Date date) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.DAY_OF_MONTH, -1);
		date = calendar.getTime();
		return date;
	}

	/**
	 * 得到分钟的最开始时间
	 *
	 * @param date
	 *            Date 要偏移的时间
	 * @return Date
	 */
	public static java.util.Date getMinuteFisrt(java.util.Date date) {
		Date newDate = new Date(date.getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(newDate);
		calendar.set(Calendar.SECOND, 00);
		newDate.setTime(calendar.getTimeInMillis());
		return newDate;
	}

	/**
	 * 得到分钟的最开始时间
	 *
	 * @param date
	 *            Date 要偏移的时间
	 * @return Date
	 */
	public static java.util.Date getMinuteLast(java.util.Date date) {
		Date newDate = new Date(date.getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(newDate);
		calendar.set(Calendar.SECOND, 59);
		newDate.setTime(calendar.getTimeInMillis());
		return newDate;
	}

	/**
	 * 得到本月的开始时间
	 *
	 * @param date
	 *            Date 要偏移的时间
	 * @return Date
	 */
	public static java.util.Date getMonthBeginday(java.util.Date date) {
		Date newDate = new Date(date.getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(newDate);
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.set(Calendar.HOUR_OF_DAY, 0);
		calendar.set(Calendar.MINUTE, 0);
		calendar.set(Calendar.SECOND, 0);
		calendar.set(Calendar.MILLISECOND, 0);
		newDate.setTime(calendar.getTimeInMillis());
		return newDate;
	}

	/**
	 * 得到时间天
	 *
	 * @param date
	 * @return
	 */
	public static int getDateDay(java.util.Date date) {
		Date newDate = new Date(date.getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(newDate);
		return calendar.get(Calendar.DAY_OF_MONTH);
	}

	/**
	 * 得到时间月
	 *
	 * @param date
	 * @return
	 */
	public static int getDateMonth(java.util.Date date) {
		Date newDate = new Date(date.getTime());
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(newDate);
		return calendar.get(Calendar.MONTH);
	}

	/**
	 * 偏移时间(按月) 规则: 1. 如果要偏移的时间是月末, 偏移后也是月末
	 *
	 * 2. 要偏移的时间的当前天大于偏移后的月份的最大天数也调整为月末, 比如12月30号(非月末)偏移两个月
	 *
	 * 应变为2月28(29)号
	 *
	 * @param date
	 *            Date 要偏移的时间
	 * @param months
	 *            int 要偏移的月份
	 * @return Date
	 */
	public static java.sql.Date offsetMonth(java.sql.Date date, int months) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		int curDay = calendar.get(Calendar.DAY_OF_MONTH);
		int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 将当前天设置为1号, 然后增加月份数 (先加月份, 再加天)
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, months);
		// 加过月份以后的日期当月的最大天数
		int newMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 如果当前天为月底, 加过以后也调整为月底
		if (curDay == maxDay) {
			calendar.set(Calendar.DAY_OF_MONTH, newMaxDay);
		} else {
			// 如果要加的初始日期的天大于新的月份的最大天数, 则调整为月底, 比如12月30号加两个月
			// 不是2 * 30天 到 3月2号, 而是到2月底
			if (curDay > newMaxDay) {
				calendar.set(Calendar.DAY_OF_MONTH, newMaxDay);
			} else {
				calendar.set(Calendar.DAY_OF_MONTH, curDay);
			}
		}
		date.setTime(calendar.getTimeInMillis());
		return date;
	}

	// 加一个Util.date 类型的
	/**
	 * 偏移时间(按月) 规则: 1. 如果要偏移的时间是月末, 偏移后也是月末
	 *
	 * 2. 要偏移的时间的当前天大于偏移后的月份的最大天数也调整为月末, 比如12月30号(非月末)偏移两个月
	 *
	 * 应变为2月28(29)号
	 *
	 * @param date
	 *            java.util.Date 要偏移的时间
	 * @param months
	 *            int 要偏移的月份
	 * @return Date
	 */
	public static java.util.Date offsetMonth(java.util.Date date, int months) {
		Calendar calendar = Calendar.getInstance();
		Date reDate = new Date();
		calendar.setTime(date);
		int curDay = calendar.get(Calendar.DAY_OF_MONTH);
		int maxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 将当前天设置为1号, 然后增加月份数 (先加月份, 再加天)
		calendar.set(Calendar.DAY_OF_MONTH, 1);
		calendar.add(Calendar.MONTH, months);
		// 加过月份以后的日期当月的最大天数
		int newMaxDay = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
		// 如果当前天为月底, 加过以后也调整为月底
		if (curDay == maxDay) {
			calendar.set(Calendar.DAY_OF_MONTH, newMaxDay);
		} else {
			// 如果要加的初始日期的天大于新的月份的最大天数, 则调整为月底, 比如12月30号加两个月
			// 不是2 * 30天 到 3月2号, 而是到2月底
			if (curDay > newMaxDay) {
				calendar.set(Calendar.DAY_OF_MONTH, newMaxDay);
			} else {
				calendar.set(Calendar.DAY_OF_MONTH, curDay);
			}
		}
		reDate.setTime(calendar.getTimeInMillis());
		return reDate;
	}

	public static boolean isInRange(String date, String beginDate, String endDate) throws Exception {
		boolean asc = isAsc(beginDate, endDate);
		if (asc) {
			if (date.compareTo(beginDate) >= 0 && date.compareTo(endDate) <= 0) {
				return true;
			}
		} else {
			if (date.compareTo(beginDate) >= 0 || date.compareTo(endDate) <= 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 检查指定时间是否在某个时间范围内(闭区间)
	 *
	 * @param date
	 *            Date 指定时间
	 * @param beginDate
	 *            Date 范围开始时间
	 *
	 * @param endDate
	 *            Date 范围结束时间
	 * @return boolean true-在范围内, false-不在范围内
	 */
	public static boolean isInRange(Date date, Date beginDate, Date endDate) {
		// long time = date.getTime();
		// long beginTime = beginDate.getTime();
		// long endTime = endDate.getTime();
		// if (time >= beginTime && time <= endTime) {
		// return true;
		// } else {
		// return false;
		// }
		if (null == (date)) {
			logger.fatal("null date !");
			return false;
		}
		if (null == (beginDate)) {
			logger.fatal("null beginDate !");
			return false;
		}
		if (null == endDate) {
			logger.fatal("null endDate !");
			return false;
		}
		if (date.compareTo(beginDate) >= 0 && date.compareTo(endDate) <= 0) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * test is date1 later than date2 or not<br />
	 * date1 > date2 = true
	 *
	 * @param date1
	 * @param date2
	 * @return boolean
	 * */
	public static boolean isLaterThan(Date date1, Date date2) {
		if (date1 == null) {
			logger.fatal("date1 is null");
		}
		if (date2 == null) {
			logger.fatal("date2 is null");
		}
		return date1.getTime() > date2.getTime();
	}

	/**
	 * test date1 is early than date2 or not<br />
	 * date1 < date2 = true
	 *
	 * @param date1
	 * @param date2
	 * @return boolean
	 * */
	public static boolean isEarlyThan(Date date1, Date date2) {
		if (date1 == null) {
			logger.fatal("date1 is null");
		}
		if (date2 == null) {
			logger.fatal("date2 is null");
		}
		return date1.getTime() < date2.getTime();
	}

	public static boolean isEqual(Date date1, Date date2) {
		if (date1 == null) {
			logger.fatal("date1 is null");
		}
		if (date2 == null) {
			logger.fatal("date2 is null");
		}
		return date1.getTime() == date2.getTime();
	}

	/**
	 * test date1 not early than date2 or not<br />
	 * date1 >= date2 = true
	 *
	 * @param date1
	 * @param date2
	 * @return boolean
	 * */
	public static boolean notEarlyThan(Date date1, Date date2) {
		return !isEarlyThan(date1, date2);
	}

	/**
	 * test date1 not early than date2 or not<br />
	 * date1 <= date2 = true
	 *
	 * @param date1
	 * @param date2
	 * @return boolean
	 * */
	public static boolean notLaterThan(Date date1, Date date2) {
		return !isLaterThan(date1, date2);
	}

	/**
	 * 检查指定时间比较大小
	 *
	 * * @param beginDate Date 范围开始时间
	 *
	 * @param endDate
	 * @deprecated 使用 isEarlyThan , isLaterThan Date, notEarlyThan,notLateThan
	 *             范围结束时间（非空检测，返回false）<br />
	 *             请使用Date 对象的before,after方法（没有非空检测）
	 * @return boolean 0-小于, 1-等于，2-大于
	 */
	@Deprecated
	public static int isCompare(Date beginDate, Date endDate) {
		// int ret=1;
		// long beginTime = beginDate.getTime();
		// long endTime = endDate.getTime();
		//
		// if (beginTime >endTime) {
		// ret=2;
		// }
		// if(beginTime==endTime){
		// ret=1;
		// }
		// if(beginTime<endTime){
		// ret=0;
		// }
		int ret = 1;
		if (beginDate.after(endDate)) {
			ret = 2;
		}
		if (beginDate.equals(endDate)) {
			ret = 1;
		}
		if (beginDate.before(endDate)) {
			ret = 0;
		}
		return ret;
	}

	/**
	 * 判断字符串是否升序的, 第一个字符串小于第二个字符串
	 *
	 * @param firstStr
	 *            String
	 * @param secondStr
	 *            String
	 * @return boolean true-升序, false-降序
	 */
	private static boolean isAsc(String firstStr, String secondStr) {
		return (firstStr.compareTo(secondStr) < 0);
	}

	/**
	 * 获得日期的一天的开始时间 00：00：00
	 *
	 * @param date
	 * @return
	 */
	public static Date getDayStartTime(Date start) {
		Calendar todayStart = Calendar.getInstance();
		todayStart.setTime(start);
		todayStart.set(Calendar.HOUR_OF_DAY, 0);
		todayStart.set(Calendar.MINUTE, 0);
		todayStart.set(Calendar.SECOND, 0);
		todayStart.set(Calendar.MILLISECOND, 0);
		return todayStart.getTime();
	}

	/**
	 * 获得日期的一天的结束时间 23：59：59
	 *
	 * @param date
	 * @return
	 */
	public static Date getDayEndTime(Date start) {
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.setTime(start);
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 999);
		return todayEnd.getTime();
	}

	/**
	 * 不带毫秒
	 *
	 * @param start
	 * @return
	 */
	public static Date getDayEndTimeNotMillisecond(Date start) {
		Calendar todayEnd = Calendar.getInstance();
		todayEnd.setTime(start);
		todayEnd.set(Calendar.HOUR_OF_DAY, 23);
		todayEnd.set(Calendar.MINUTE, 59);
		todayEnd.set(Calendar.SECOND, 59);
		todayEnd.set(Calendar.MILLISECOND, 0);
		return todayEnd.getTime();
	}

	/**
	 * 根据开始时间、结束时间得到两个时间段内所有的日期 要求配置每天时段时，不能跨天，结束时间最大配置成23:59:59
	 *
	 * @param start
	 *            开始日期
	 * @param end
	 *            结束日期
	 * @param calendarType
	 *            类型
	 * @return 两个日期之间的日期,例如如果 开始时间是2012-12-30 20:59:17， 结束时间是2013-1-1 10:45:57
	 *         结果是如下数组信息 2012-12-30 20:59:17 2012-12-30 23:59:59 2012-12-31
	 *         0:00:00 2012-12-31 23:59:59 2013-1-1 10:45:57
	 */
	public static Date[] getDateArrays(Date start, Date end, int calendarType) {
		ArrayList<Date> ret = new ArrayList<Date>();
		Calendar calendar = Calendar.getInstance();
		Date tmpEndDate = getDayEndTime(start);
		calendar.setTime(tmpEndDate);
		// Date tmpDate = calendar.getTime();
		long endTime = end.getTime();
		// System.out.println("tmpEndDate: " + tmpEndDate.toLocaleString());
		ret.add(start);
		int i = 0;
		while (tmpEndDate.before(end) || tmpEndDate.getTime() == endTime) {
			if (i != 0) {
				ret.add(getDayStartTime(calendar.getTime()));
			}
			ret.add(calendar.getTime());
			calendar.add(calendarType, 1);
			tmpEndDate = calendar.getTime();
			// System.out.println("tmpEndDate: " + tmpEndDate.toLocaleString());
			i++;
		}
		ret.add(end);
		Date[] dates = new Date[ret.size()];
		return ret.toArray(dates);
	}

	/**
	 * 根据开始时间、结束时间得到两个时间段内所有的日期 要求配置每天时段时，不能跨天，结束时间最大配置成23:59:59
	 *
	 * @param start
	 *            开始日期
	 * @param end
	 *            结束日期
	 * @param calendarType
	 *            类型
	 * @return 两个日期之间的日期,例如如果 开始时间是2012-12-30 20:59:17， 结束时间是2013-1-1 10:45:57
	 *         结果是如下数组信息 2012-12-30 20:59:17 2012-12-31 0:00:00 2013-1-1 0:00:00
	 *         // 注意这个是循环后添加的 2013-1-1 10:45:57
	 */
	public static ArrayList<Date> getDateArraysWithoutEndTime(Date start, Date end, int calendarType) {
		ArrayList<Date> ret = new ArrayList<Date>();
		Calendar calendar = Calendar.getInstance();
		Date tmpEndDate = getDayEndTime(start);
		calendar.setTime(tmpEndDate);
		long endTime = end.getTime();
		// System.out.println("tmpEndDate: " + tmpEndDate.toLocaleString());
		ret.add(start);
		int i = 0;
		while (tmpEndDate.before(end) || tmpEndDate.getTime() == endTime) {
			if (i != 0) {
				ret.add(getDayStartTime(calendar.getTime()));
			}
			ret.add(calendar.getTime());
			calendar.add(calendarType, 1);
			tmpEndDate = calendar.getTime();
			// System.out.println("tmpEndDate: " + tmpEndDate.toLocaleString());
			i++;
		}
		if (getDayStartTime(tmpEndDate).before(end) && getDayStartTime(tmpEndDate).after(start)) {
			ret.add(getDayStartTime(end));
		}
		ret.add(end);
		// Date[] dates = new Date[ret.size()];
		return ret;
	}

	/**
	 * 拆分两周
	 *
	 * @param start
	 *            开始日期
	 * @param end
	 *            结束日期
	 * @return 两个日期之间的第一个时间周结束时间划分,例如如果 开始时间是2006-9-13 17:38:27， 结束时间是2013-1-14
	 *         13:11:47 2006-9-13 17:38:27 2006-9-17 23:59:59 2013-1-14 13:11:47
	 */
	public static ArrayList<Date> getDateArraysByWeek(Date start, Date end) {
		ArrayList<Date> ret = new ArrayList<Date>();
		Date tmpDate = start;
		ret.add(start);
		Calendar c = new GregorianCalendar();
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6);
		c.setFirstDayOfWeek(Calendar.MONDAY);
		c.setTime(tmpDate);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + 6); // Sunday
		c.set(Calendar.HOUR_OF_DAY, 23);
		c.set(Calendar.MINUTE, 59);
		c.set(Calendar.SECOND, 59);
		c.set(Calendar.MILLISECOND, 0);
		tmpDate = c.getTime();
		// System.out.println("tmpDate" + tmpDate.toLocaleString());
		if (DateHelper.isCompare(tmpDate, end) == 0) {
			// 拆分
			ret.add(tmpDate);
		}
		ret.add(end);
		return ret;
	}

	// 获取和timeSpanDate相同周几，并且和date在同一周的时间
	// 比如timeSpanDate 2013-1-14 13:31:31 周一
	// date 2006-9-13 17:58:11 周三
	// 则返回2006-9-11 17:58:11 周一
	public static Date getWeekDate(Date date, Date timeSpanDate) {
		Calendar c = new GregorianCalendar();
		Date tmpDate = new Date();
		c.setTime(timeSpanDate);
		int week = c.get(Calendar.DAY_OF_WEEK);
		c.setTime(date);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + (week - 1)); // Sunday
		tmpDate = c.getTime();
		return tmpDate;
	}

	// 获取和timeSpanDate相同周周几，
	// 比如timeSpanDate 2006-9-13 18:07:43 周三
	// week 2 ， 本周第几天，周日是 1 周一为二
	// 则返回2006-9-11 17:58:11 周一
	public static Date getWeekDate(Date timeSpanDate, int week) {
		Calendar c = new GregorianCalendar();
		Date tmpDate = new Date();
		c.setTime(timeSpanDate);
		c.set(Calendar.DAY_OF_WEEK, c.getFirstDayOfWeek() + (week - 1)); // Sunday
		tmpDate = c.getTime();
		return tmpDate;
	}

	// 获取start 和 timespanDate相同周几，时分秒 的日期
	public static Date setWeekDateHHMMSS(Date start, Date timeSpanDate) {
		Calendar c = new GregorianCalendar();
		Calendar c1 = new GregorianCalendar();
		Date tmpDate = new Date();
		c1.setTime(timeSpanDate);
		c.setTime(start);
		c.set(Calendar.DAY_OF_WEEK, c1.get(Calendar.DAY_OF_WEEK)); // Sunday
		c.set(Calendar.HOUR_OF_DAY, c1.get(Calendar.HOUR_OF_DAY));
		c.set(Calendar.MINUTE, c1.get(Calendar.MINUTE));
		c.set(Calendar.SECOND, c1.get(Calendar.SECOND));
		tmpDate = c.getTime();
		return tmpDate;
	}
	/**
	 * 获取两个时间的间隔
	 *
	 * @param lastExsitDate
	 * @param lastday
	 * @Unit 单位：年，月，日，时，分，秒
	 * @return 根据单位返回两个时间间隔
	 * 如果两个日期都为null，就返回0
	 * 如果起始时间为null，结束时间不为null，就查结束时间之前，负无穷
	 * 如果结束时间为null，起始时间不为null，就查起始时间之后，正无穷
	 * @throws Exception
	 */
	public static Long getIntervalDay(Date baseDate, Date lastday , int Unit) throws Exception {
		if (baseDate == null && lastday == null) {// 没有时间，返回null
			return 0L;
		}else if(baseDate == null){
			return -999999L;
		}else if(lastday ==null){
			return 999999L;
		}
		Calendar cal = Calendar.getInstance();
		cal.setTime(baseDate);
		int basemonth =  cal.get(Calendar.MONTH)+1;
		int baseyear =  cal.get(Calendar.YEAR);
		cal.setTime(lastday);
		int lastmonth =  cal.get(Calendar.MONTH)+1;
		int lastyear =  cal.get(Calendar.YEAR);
		Long timespan=lastday.getTime()-baseDate.getTime();
		int time=1000;
		switch (Unit) {
		case TYPE_SECOND:
		    return timespan/time;
		case TYPE_MINITE:
			time=60*time;
			return timespan/time;
		case TYPE_HOUR:
			time=60*60*time;
			return timespan/time;
		case TYPE_DAY:
			time=24*60*60*time;
			return timespan/time;
		case TYPE_MONTH:
			if(baseyear==lastyear){
				return (long) (lastmonth-basemonth);
			}else{
				return (long) (12*(lastyear-baseyear)+lastmonth-basemonth);
			}
		case TYPE_YEAR:
			time=12*30*24*60*60*time;
			return timespan/time;
		}

		return 1l;
	}

	// 毫秒转换为日期
	public static Date long2Date(long time) {
		Calendar c = Calendar.getInstance();
		c.setTimeInMillis(time);
		return c.getTime();
	}
	
}
