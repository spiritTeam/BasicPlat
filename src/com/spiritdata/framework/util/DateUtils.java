package com.spiritdata.framework.util;

import java.sql.Timestamp;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

/**
 * 对时间日期类型做了统一处理，可以满足绝大多数对日期型操作的要求
 * @author zhuhua
 */
public abstract class DateUtils {
    //获取时间的字符串，把时间转换为字符串
    /**
     * 获取本地自定义格式的时间字符串
     * @param format 自定义格式,如"yyyy-MM-dd HH:mm:ss"等
     * @param d 欲转换的时间
     * @return 本地自定义格式的时间字符串
     */
    public static String convert2LocalStr(String format, Date d) {
        if (d==null) return null;
        SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.CHINESE);
        return formatter.format(d);
    }

    /**
     * 获取本地时间字符串：长格式, "yyyy-MM-dd HH:mm:ss"
     * @param d 欲转换的时间
     * @return 长格式字符串,精确到秒
     */
    public static String convert2LongLocalStr(Date d) {
        return convert2LocalStr("yyyy-MM-dd HH:mm:ss", d);
    }

    /**
     * 获取本地时间字符串：长格式压缩（14位）"yyyyMMddHHmmss"
     * @param d 欲转换的时间
     * @return 长格式压缩字符串,精确到秒
     */
    public static String convert2LongCompresstStr(Date d) {
        return convert2LocalStr("yyyyMMddHHmmss", d);
    }

    /**
     * 获取本地时间字符串：只包括年月日"
     * @param d 欲转换的时间
     * @return 本地日期字符串,只包括年月日
     */
    public static String convert2DateStr(Date d) {
        return convert2LocalStr("yyyy-MM-dd", d);
    }

    /**
     * 获取本地时间字符串：只日期
     * @param d 欲转换的时间
     * @return 本地时间
     */
    public static String convert2TimeStr(Date d) {
        return convert2LocalStr("HH:mm:ss", d);
    }

    /**
     * 把时间转换为中文格式(YYYY年MM月DD日 HH24时MI分SS秒)的日期字符串。
     * @param calendar 欲转换的时间
     * @return 生成的日期，格式为YYYY年MM月DD日 HH24时MI分SS秒
     */
    public static String convert2TimeChineseStr(GregorianCalendar calendar) {
        String timeStr[] = DateUtils.splitDateTime(calendar);
        return timeStr[0]+"年"+timeStr[1]+"月"+timeStr[2]+"日 "+timeStr[3]+"时"+timeStr[4]+"分"+timeStr[5]+"秒";
    }

    /**
     * 把时间转换为中文格式(YYYY年MM月DD日 HH24时MI分SS秒)的日期字符串。
     * @param date 欲转换的时间
     * @return 生成的日期，格式为YYYY年MM月DD日 HH24时MI分SS秒
     */
    public static String convert2TimeChineseStr(Date d) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(d);
        return DateUtils.convert2TimeChineseStr(calendar);
    }

    /**
     * 以字符串数组的形式返回日期的各字段(年/月/日/时/分/秒/毫秒)的值
     *
     * @param calendar 欲转换的时间
     * @return 包括日期各字段的字符串值得字符串数组：times[0]=年/time[1]=月/time[2]=日/time[3]=时/time[4]=分/time[5]=秒/time[6]=毫秒
     */
    public static String[] splitDateTime(GregorianCalendar calendar) {
        if (null != calendar) {
            String[] times = new String[7];
            times[0] = String.valueOf(calendar.get(Calendar.YEAR));
            times[1] = String.copyValueOf(String.valueOf(calendar.get(Calendar.MONTH) + 101).toCharArray(), 1, 2);
            times[2] = String.copyValueOf(String.valueOf(calendar.get(Calendar.DAY_OF_MONTH) + 100).toCharArray(), 1, 2);
            times[3] = String.copyValueOf(String.valueOf(calendar.get(Calendar.HOUR_OF_DAY) + 100).toCharArray(), 1, 2);
            times[4] = String.copyValueOf(String.valueOf(calendar.get(Calendar.MINUTE) + 100).toCharArray(), 1, 2);
            times[5] = String.copyValueOf(String.valueOf(calendar.get(Calendar.SECOND) + 100).toCharArray(), 1, 2);
            times[6] = String.copyValueOf(String.valueOf(calendar.get(Calendar.MILLISECOND) + 10000).toCharArray(), 1, 4);
            return times;
        } else return null;
    }

    /**
     * 以字符串数组的形式返回日期的各字段(年/月/日/时/分/秒/毫秒)的值
     *
     * @param date 欲转换的时间
     * @return 包括日期各字段的字符串值得字符串数组：times[0]=年/time[1]=月/time[2]=日/time[3]=时/time[4]=分/time[5]=秒/time[6]=毫秒
     */
    public static String[] splitDateTime(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        return DateUtils.splitDateTime(calendar);
    }

    //时间计算方法
    /**
     * 将指定的字符串按照给的日期和时间格式转化成Date对象
     * 
     * @param patten 表示日期和时间的格式的字符串 例如: "yyyy-MM-dd HH:mm:ss"
     *    请参考java.text.SimpleDateFormat类
     * @param strDateTime 需要检验的字符串
     * @return 成功则返回一个Date句柄，否则返回null
     *   注：调用GregorianCalendar的setTime(Datedate)函数可将Date对象转化为 GregorianCalendar对象
     */
    public final static Date getDateTime(String patten, String strDateTime) throws ParseException {
        if (null!=strDateTime) {
            SimpleDateFormat formatter = new SimpleDateFormat(patten);
            Date date = (Date)formatter.parse(strDateTime);
            return date;
        }
        return null;
    }

    /**
     * 获取某个日期所在那一天的最开始时刻
     * 
     * @param date 日期
     * @return 那一天的开始时刻
     */
    public final static Date getDayStart(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        DateUtils.setDayStart(calendar);
        return calendar.getTime();
    }

    /**
     * 获取某个日期所在那一天的最后时刻
     * 
     * @param date 日期
     * @return 那一天的最后时刻
     */
    public final static Date getDayEnd(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        DateUtils.setDayEnd(calendar);
        return calendar.getTime();
    }

    /**
     * 获取某个日期所在的那个星期的第一天（周日是第一天）的最开始时刻
     * 
     * @param date 日期
     * @return 那个星期的第一天的开始时刻
     */
    public final static Date getWeekStart(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SUNDAY);
        DateUtils.setDayStart(calendar);
        return calendar.getTime();
    }

    /**
     * 获取某个日期所在的那个星期的最后一天（周六是第一天）的最后时刻
     *
     * @param date 日期
     * @return 那个星期的第一天的最后时刻
     */
    public final static Date getWeekEnd(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_WEEK, Calendar.SATURDAY);
        DateUtils.setDayEnd(calendar);
        return calendar.getTime();
    }

    /**
     * 获取当月最开始时间
     *
     * @param date 日期
     * @return 所在月的第一天
     */
    public final static Date getMonthStart(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        DateUtils.setDayStart(calendar);
        return calendar.getTime();
    }

    /**
     * 根据日期，获取当月最后一天
     * 
     * @param date 日期
     * @return 所在月的最后一天
     */
    public final static Date getMonthEnd(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        calendar.add(Calendar.MONTH, 1);
        calendar.add(Calendar.DATE, -1);
        DateUtils.setDayEnd(calendar);
        return calendar.getTime();
    }

    /**
     * 获取日期数据所在年的第一天
     * 
     * @param date 日期
     * @return 所在年的第一天
     */
    public final static Date getYearStart(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        DateUtils.setDayStart(calendar);
        return calendar.getTime();
    }

    /**
     * 获取日期数据所在年的最后一天
     *
     * @param date 日期
     * @return 所在年的最后一天
     */
    public final static Date getYearEnd(Date date) {
        GregorianCalendar calendar = new GregorianCalendar();
        calendar.setTime(date);
        calendar.add(Calendar.YEAR, 1);
        calendar.set(Calendar.DAY_OF_YEAR, 1);
        calendar.add(Calendar.DAY_OF_YEAR, -1);
        DateUtils.setDayEnd(calendar);
        return calendar.getTime();
    }

    /**
     * 计算总耗时
     * 
     * @param starttime
     * @param endtime
     * @return 毫秒数
     */
    public long caculateTime(Timestamp starttime, Timestamp endtime) {
        return ((Date) endtime).getTime() - ((Date) starttime).getTime();
    }

    //设置时间为一天开始
    private final static void setDayStart(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 0);
        calendar.set(Calendar.MINUTE, 0);
        calendar.set(Calendar.SECOND, 0);
    }

    //设置时间为一天结束
    private final static void setDayEnd(Calendar calendar) {
        calendar.set(Calendar.HOUR_OF_DAY, 23);
        calendar.set(Calendar.MINUTE, 59);
        calendar.set(Calendar.SECOND, 59);
    }
}