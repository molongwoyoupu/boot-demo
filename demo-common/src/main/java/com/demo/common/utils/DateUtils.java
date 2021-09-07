package com.demo.common.utils;

import org.apache.commons.lang3.StringUtils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.temporal.ChronoField;
import java.time.temporal.TemporalAdjusters;
import java.util.*;
import java.util.regex.Pattern;

/**
 * 日期工具类
 *
 * @author molong
 * @date 2021/9/6
 */
public class DateUtils {

    /**
     * 标准日期时间格式,精确到秒(yyyy-MM-dd HH:mm:ss)
     */
    public static final String NORM_DATETIME_PATTERN = "yyyy-MM-dd HH:mm:ss";
    /**
     * 年月格式(yyyy-MM)
     */
    public static final String NORM_MONTH_PATTERN = "yyyy-MM";
    /**
     * 标准日期格式(yyyy-MM-dd)
     */
    public static final String NORM_DATE_PATTERN = "yyyy-MM-dd";

    /**
     * 年月正则格式(yyyy-MM)
     */
    public static final String NORM_MONTH_REGEXP = "\\d{4}-\\d{2}";
    /**
     * 标准日期正则格式(yyyy-MM-dd)
     */
    public static final String NORM_DATE_REGEXP = "\\d{4}-\\d{2}-\\d{2}";
    /**
     * 标准年份正则格式(yyyy-MM-dd)
     */
    public static final String NORM_YEAR_REGEXP = "\\d{4}";

    /**
     * LocalDateTime转成标准时间格式字符串(yyyy-MM-dd HH:mm:ss)
     *
     * @param dateTime 时间
     * @return 字符串
     */
    public static String format(LocalDateTime dateTime) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(NORM_DATETIME_PATTERN);
        return dtf.format(dateTime);
    }

    /**
     * LocalDateTime转成字符串
     *
     * @param dateTime 时间
     * @param pattern  格式
     * @return 字符串
     */
    public static String format(LocalDateTime dateTime, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return dtf.format(dateTime);
    }

    /**
     * LocalDate转成字符串
     *
     * @param date    日期
     * @param pattern 格式
     * @return 字符串
     */
    public static String format(LocalDate date, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return dtf.format(date);
    }

    /**
     * 时间字符串转成LocalDateTime
     *
     * @param dateTime 时间字符串
     * @param pattern  格式
     * @return 字符串
     */
    public static LocalDateTime parseLocalDateTime(String dateTime, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return LocalDateTime.parse(dateTime, dtf);
    }

    /**
     * 日期字符串转成LocalDate
     *
     * @param dateStr 日期字符串
     * @param pattern 格式
     * @return 字符串
     */
    public static LocalDate parseLocalDate(String dateStr, String pattern) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(pattern);
        return LocalDate.parse(dateStr, dtf);
    }

    /**
     * 月份字符串 转LocalDate
     *
     * @param month 月份字符串(yyyy-MM)
     * @return 日期
     */
    public static LocalDate monthStrToLocalDate(String month) {
        DateTimeFormatter fmt = new DateTimeFormatterBuilder()
                .appendPattern(NORM_MONTH_PATTERN)
                .parseDefaulting(ChronoField.DAY_OF_MONTH, 1)
                .toFormatter();
        return LocalDate.parse(month, fmt);
    }

    /**
     * 获取某月下的所有日期字符串
     *
     * @param date    月份的任意日期
     * @param pattern 格式化方式
     * @return 某月下的所有日期
     */
    public static List<String> getAllDateStrOfMonth(LocalDate date, String pattern) {
        //默认格式
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern(NORM_DATE_PATTERN);
        if (StringUtils.isNotEmpty(pattern)) {
            dtf = DateTimeFormatter.ofPattern(pattern);
        }
        //默认格式
        List<String> result = new ArrayList<>();
        //当月天数
        int days = date.lengthOfMonth();
        //从一号开始
        date = date.with(TemporalAdjusters.firstDayOfMonth());
        for (int i = 0; i < days; i++) {
            result.add(dtf.format(date));
            date = date.plusDays(1);
        }
        return result;
    }

    /**
     * 获取某月下的所有日期
     *
     * @param date 月份的任意日期
     * @return 某月下的所有日期
     */
    public static List<LocalDate> getAllDateOfMonth(LocalDate date) {
        List<LocalDate> result = new ArrayList<>();
        //当月天数
        int days = date.lengthOfMonth();
        //从一号开始
        date = date.with(TemporalAdjusters.firstDayOfMonth());
        for (int i = 0; i < days; i++) {
            result.add(date);
            date = date.plusDays(1);
        }
        return result;
    }

    /**
     * 获取某月下的当日之前(包含当日)的所有日期
     *
     * @param date 月份的任意日期
     * @return 某月下的当日之前(包含当日)的所有日期
     */
    public static List<LocalDate> getAllDateNoAfterTodayOfMonth(LocalDate date) {
        List<LocalDate> result = new ArrayList<>();
        //当月天数
        int days = date.getDayOfMonth();
        //从一号开始
        date = date.with(TemporalAdjusters.firstDayOfMonth());
        for (int i = 0; i < days; i++) {
            result.add(date);
            date = date.plusDays(1);
        }
        return result;
    }

    /**
     * 获取日期下的所有时间
     *
     * @return 日期下的所有时间
     */
    public static List<String> getAllTimeOfDate(String dateStr) {
        int times = 24;
        List<String> dayList = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            dayList.add(String.format("%s %s:00", dateStr, repairZero(i)));
        }
        return dayList;
    }

    /**
     * 获取日期下的所有时间
     *
     * @return 日期下的所有时间
     */
    public static List<String> getAllTimeOfDate() {
        int times = 24;
        List<String> dayList = new ArrayList<>();
        for (int i = 0; i < times; i++) {
            dayList.add(String.format("%s:00", repairZero(i)));
        }
        return dayList;
    }

    /**
     * 获取日期下截止到当前时间的所有时间（例如：10:53，0-10点）
     *
     * @return 日期下的所有时间
     */
    public static List<String> getUntilNowTimeOfDate() {
        int untilHour = LocalDateTime.now().getHour();
        List<String> dayList = new ArrayList<>();
        for (int i = 0; i <= untilHour; i++) {
            dayList.add(String.format("%s:00", repairZero(i)));
        }
        return dayList;
    }

    /**
     * 计算某年的 某月 有多少天
     *
     * @param year  年份
     * @param month 月份
     * @return 返回当月的天数
     */
    public static int getDayNumByYearMonth(int year, int month) {

        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        return a.get(Calendar.DATE);
    }

    /**
     * 获取时间的分钟数
     *
     * @param time 时间
     * @return 分钟数
     * @throws ParseException 格式化异常
     */
    public static int getMinutes(String time) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
        Date date = sdf.parse(time);

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        int hour = calendar.get(Calendar.HOUR_OF_DAY);
        int minute = calendar.get(Calendar.MINUTE);
        return 60 * hour + minute;
    }

    /**
     * 获取两个日期中间的所有日期 包含开始与结束日期
     *
     * @param start 开始日期 格式 yyyy-MM-dd
     * @param end   结束日期 格式 yyyy-MM-dd
     * @return 查询结果
     */
    public static List<String> getSubDateList(LocalDate start, LocalDate end) {
        DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        try {
            List<String> result = new ArrayList<>();
            if (Objects.equals(start, end)) {
                String format = dtf.format(start);
                result.add(format);
                return result;
            } else if (start.isAfter(end)) {
                return new ArrayList<>();
            } else {
                //时间相减得到天数
                long daySub = end.toEpochDay() - start.toEpochDay();
                LocalDate date = start;
                for (int i = 0; i <= daySub; i++) {
                    result.add(dtf.format(date));
                    date = date.plusDays(1);
                }
            }
            return result;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return new ArrayList<>();
    }

    /**
     * 获取日期所在季度
     *
     * @param date 日期
     * @return 日期所在季度
     */
    public static Integer getCurrentQuarter(LocalDate date) {
        int month = date.getMonthValue();
        return (month - 1) / 3 + 1;
    }

    /**
     * 如果为空 获取当前年
     *
     * @param year 年份
     * @return 年份
     */
    public static Integer getDefaultYearIfNull(Integer year) {
        return Objects.isNull(year) ? LocalDate.now().getYear() : year;
    }

    /**
     * 补0操作
     *
     * @param num 数值
     * @return 补0后的值
     */
    public static String repairZero(int num) {
        return (num >= 10) ? (num + "") : ("0" + num);
    }

    /**
     * 起始日期加上天数
     *
     * @param date    开始日期
     * @param addDays 添加天数
     * @return 结果日期
     */
    public static String addDate(String date, int addDays) {
        LocalDate startDate = parseLocalDate(date, NORM_DATE_PATTERN);
        LocalDate endDate = startDate.plusDays(addDays);
        return format(endDate, NORM_DATE_PATTERN);
    }

    /**
     * 校验日期的值是否符合格式
     *
     * @param dateStr 日期字符串
     * @return 校验结果
     */
    public static boolean validDate(String dateStr) {
        return Pattern.compile(NORM_DATE_REGEXP).matcher(dateStr).matches();
    }

    /**
     * 校验月份的值是否符合格式
     *
     * @param monthStr 月份字符串
     * @return 校验结果
     */
    public static boolean validMonth(String monthStr) {
        return Pattern.compile(NORM_MONTH_REGEXP).matcher(monthStr).matches();
    }

    /**
     * 校验季度的值是否符合格式
     *
     * @param quarterStr 季度字符串
     * @return 校验结果
     */
    public static boolean validQuarter(String quarterStr) {
        String quarterPattern = "^[0-9]";
        return Pattern.compile(quarterPattern).matcher(quarterStr).matches();
    }

    /**
     * 校验年份的值是否符合格式
     *
     * @param yearStr 季度字符串
     * @return 校验结果
     */
    public static boolean validYear(String yearStr) {
        return Pattern.compile(NORM_YEAR_REGEXP).matcher(yearStr).matches();
    }
}
