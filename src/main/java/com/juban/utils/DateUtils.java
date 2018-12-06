package com.juban.utils;


import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.*;
import java.time.temporal.ChronoUnit;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Class Name: DateUtils Description: TODO
 *
 * @author zjzc
 */
@Component
public final class DateUtils {

    private static final Logger LOGGER = LoggerFactory
            .getLogger(DateUtils.class);

    public static final String YYYY_MM_DD_HH_MM_SS = "yyyy-MM-dd HH:mm:ss";
    public static final String YYYY_MM_DD_HH_MM = "yyyy-MM-dd HH:mm";
    public static final String YYYYMMDD = "yyyyMMdd";
    public static final String YYYY_MM_DD = "yyyy-MM-dd";
    public static final String YYYY_MM_DD_LINE = "yyyy_MM_dd";
    public static final String YYYY_MM_DD_CHINESE = "yyyy年MM月dd日";
    public static final String YYYYMMDDHHMMSS = "yyyyMMddHHmmss";
    public static final String YYYYMMDDHHMMSSSSS = "yyyyMMddHHmmssSSS";
    public static final String YYYYMMDD_HHMM = "yyyyMMdd_HHmm";
    public static final String HH_MM_SS = "HH:mm:ss";
    public static final String HHMMSS = "HHmmss";

    /**
     * 以下这种方式是非线程安全的，在多个线程使用时可能会发生前一个线程取到了后一个线程的时间，造成时间混乱
     */
    public static final SimpleDateFormat SDF_SHORT = new SimpleDateFormat("yyyy-MM-dd");
    public static final SimpleDateFormat SDF_LONG = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    public static final SimpleDateFormat SDF_LONG_NOSECOND = new SimpleDateFormat("yyyy-MM-dd HH:mm");
    public static final SimpleDateFormat SDF_SHORT_CN = new SimpleDateFormat("yyyy年MM月dd日");

    /**
     * 存放不同的日期模板格式的sdf的Map
     */
    private static ThreadLocal<HashMap<String, SimpleDateFormat>> sdfCache = new ThreadLocal<HashMap<String, SimpleDateFormat>>() {
        @Override
        protected synchronized HashMap<String, SimpleDateFormat> initialValue() {
            return new HashMap<String, SimpleDateFormat>();
        }
    };


    private static SimpleDateFormat getSdf(final String pattern) {
        Map<String, SimpleDateFormat> localCache = sdfCache.get();
        SimpleDateFormat format = localCache.get(pattern);
        // 此处的双重判断和同步是为了防止sdfMap这个单例被多次put重复的sdf
        if (format == null) {
            format = new SimpleDateFormat(pattern);
            localCache.put(pattern, new SimpleDateFormat(pattern));
        }
        return format;
    }


    public static String format(Date date, String pattern) {
        return getSdf(pattern).format(date);
    }

    public static Date format(String dateStr, String pattern) throws ParseException {
        return getSdf(pattern).parse(dateStr);
    }

    /**
     * Get date by the date string and format.
     *
     * @param dateStr
     * @param format
     */
    public static Date getDate(String dateStr, String format) {
        if (StringUtils.isBlank(dateStr)) {
            return null;
        }
        SimpleDateFormat dateformat = new SimpleDateFormat(format);
        try {
            return dateformat.parse(dateStr);
        } catch (ParseException e) {
            // 出生年月
            if (dateStr.matches("\\d{4}-\\d{2}")) {
                dateStr = dateStr + "-01";
                return getDate(dateStr, format);
            }
            LOGGER.error("The date string: {} is not correct format.", dateStr);
        }
        return null;
    }


    /*
     * 获取当前时间的前一天日期
     */
    public static Date getPreDate() {
        Date preDate = null;
        Date now = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(now);
        calendar.add(Calendar.DAY_OF_MONTH, -1);
        preDate = calendar.getTime();
        return preDate;
    }

    /*
     * 获取给定日期的后N天日期
     */
    public static Date getAfterADate(Date date, int num) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(date);
        calendar.add(Calendar.DAY_OF_MONTH, num);
        return calendar.getTime();
    }

    /**
     * Format date by the date/format provided.
     *
     * @param date
     * @param format
     */
    public static String formatDate(Date date, String format) {
        if (null == date) {
            return "";
        }
        SimpleDateFormat dateformat = new SimpleDateFormat(format);
        return dateformat.format(date);
    }

    public static Date getStartDate(Date date) throws ParseException {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateformat = new SimpleDateFormat(YYYY_MM_DD);
        SimpleDateFormat startFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        return startFormat.parse(dateformat.format(date) + " 00:00:00");
    }

    public static Date getEndDate(Date date) throws ParseException {
        if (date == null) {
            return null;
        }
        SimpleDateFormat dateformat = new SimpleDateFormat(YYYY_MM_DD);
        SimpleDateFormat startFormat = new SimpleDateFormat(YYYY_MM_DD_HH_MM_SS);
        return startFormat.parse(dateformat.format(date) + " 23:59:59");
    }


    public static int betweenDays(Date startDate, Date endDate) {
        Calendar beginCalendar = Calendar.getInstance();
        Calendar endCalendar = Calendar.getInstance();

        beginCalendar.setTime(startDate);
        endCalendar.setTime(endDate);

        if (beginCalendar.get(Calendar.YEAR) == endCalendar.get(Calendar.YEAR)) {
            return endCalendar.get(Calendar.DAY_OF_YEAR) - beginCalendar.get(Calendar.DAY_OF_YEAR);
        } else {
            if (beginCalendar.getTimeInMillis() < endCalendar.getTimeInMillis()) {
                int days = beginCalendar.getActualMaximum(Calendar.DAY_OF_YEAR)
                        - beginCalendar.get(Calendar.DAY_OF_YEAR) + endCalendar.get(Calendar.DAY_OF_YEAR);
                for (int i = beginCalendar.get(Calendar.YEAR) + 1; i < endCalendar.get(Calendar.YEAR); i++) {
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, i);
                    days += c.getActualMaximum(Calendar.DAY_OF_YEAR);
                }
                return days;
            } else {
                int days = endCalendar.getActualMaximum(Calendar.DAY_OF_YEAR) - endCalendar.get(Calendar.DAY_OF_YEAR)
                        + beginCalendar.get(Calendar.DAY_OF_YEAR);
                for (int i = endCalendar.get(Calendar.YEAR) + 1; i < beginCalendar.get(Calendar.YEAR); i++) {
                    Calendar c = Calendar.getInstance();
                    c.set(Calendar.YEAR, i);
                    days += c.getActualMaximum(Calendar.DAY_OF_YEAR);
                }
                return days;
            }
        }
    }

    /**
     * 计算月份差
     *
     * @param startDate
     * @param endDate
     */
    public static int betweenMonths(Date startDate, Date endDate) {
        Calendar cal = Calendar.getInstance();
        cal.setTime(startDate);
        int startDateMonth = (cal.get(Calendar.MONTH) + 1) + (cal.get(Calendar.YEAR)) * 12;
        int startDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);//当月日期
        cal.setTime(endDate);
        int endDateMonth = (cal.get(Calendar.MONTH) + 1) + (cal.get(Calendar.YEAR)) * 12;
        int endDayOfMonth = cal.get(Calendar.DAY_OF_MONTH);//当月日期
        int months = endDateMonth - startDateMonth - (endDayOfMonth >= startDayOfMonth ? 0 : 1);
        return months;
    }

    /**
     * SimpleDateFormat
     *
     * @param dateFormat
     */
    public static SimpleDateFormat dateFormat(String dateFormat) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat(dateFormat);
        return simpleDateFormat;
    }

    /**
     * @param happenDate 指定的日期
     * @param weekPatten 每周的的周几,例如"1,2,7"
     * @param size       需要返回指定日期后的日期数量
     *
     * @return List<Date>
     */
    public static List<Date> listIntervalWeekDates(Date happenDate, String weekPatten, int size) {
        Instant instant = happenDate.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate date = instant.atZone(zoneId).toLocalDate();
        List<LocalDate> collect = Stream.of(weekPatten.split(",")).map(s -> date.with(DayOfWeek.of(Integer.valueOf(s)))).collect(Collectors.toList());
        List<LocalDate> result = collect.stream().filter(c -> c.isAfter(date) || c.isEqual(date)).collect(Collectors.toList());
        int[] a = new int[]{1};
        while (result.size() < size) {
            List<LocalDate> tempList = collect.stream().map(c -> c.plusWeeks(a[0])).collect(Collectors.toList());
            a[0]++;
            result.addAll(tempList);
        }
        return result.subList(0, size).stream().map(r -> Date.from(r.atStartOfDay(zoneId).toInstant())).collect(Collectors.toList());
    }

    /**
     * @param getDate 取日期
     * @param getTime 取时分秒
     *
     * @return Date
     */
    public static Date mergeDateTime(Date getDate, Date getTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        Instant instant = getDate.toInstant();
        LocalDate localDate = instant.atZone(zoneId).toLocalDate();

        Instant instant2 = getTime.toInstant();
        LocalTime localTime = instant2.atZone(zoneId).toLocalTime();
        LocalDateTime localDateTime = LocalDateTime.of(localDate, localTime);
        return Date.from(localDateTime.atZone(zoneId).toInstant());
    }

    /**
     * 判断两个日期是否同一天
     *
     * @param day1
     * @param day2
     */
    public static boolean sameDay(Date day1, Date day2) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate date1 = day1.toInstant().atZone(zoneId).toLocalDate();
        LocalDate date2 = day2.toInstant().atZone(zoneId).toLocalDate();
        return date1.isEqual(date2);
    }

    /**
     * 判断活动状态,相对于当前日期
     *
     * @param startTime
     * @param endTime
     *
     * @return 1活动未开始, 2活动进行中, 3活动结束
     */
    public static int activityStatus(Date startTime, Date endTime) {
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDateTime startDay = startTime.toInstant().atZone(zoneId).toLocalDateTime();
        LocalDateTime endDay = endTime.toInstant().atZone(zoneId).toLocalDateTime();
        LocalDateTime now = LocalDateTime.now();
        if (now.isBefore(startDay)) {
            return 1;
        }
        if (now.isAfter(startDay) && now.isBefore(endDay)) {
            return 2;
        } else {
            return 3;
        }
    }

    public static boolean checkWeekDate(String weekDay) {
        LocalDate date = LocalDate.now();
        return Stream.of(weekDay.split(",")).map(s -> date.with(DayOfWeek.of(Integer.valueOf(s)))).anyMatch(i -> i.isEqual(date));
    }

    /**
     * 两个时间之间的周期时间
     *
     * @param startTime
     * @param endTime
     * @param weekDay
     */
    public static List<Date> listIntervalWeekDates(@NotNull Date startTime, @NotNull Date endTime, @NotBlank String weekDay) {
        Instant instant = startTime.toInstant();
        ZoneId zoneId = ZoneId.systemDefault();
        LocalDate startDate = instant.atZone(zoneId).toLocalDate().plus(-1, ChronoUnit.DAYS);
        LocalDate endDate = endTime.toInstant().atZone(zoneId).toLocalDate().plus(1, ChronoUnit.DAYS);
        int length = weekDay.split(",").length;
        long weeks = startDate.until(endDate, ChronoUnit.WEEKS);
        long days = startDate.until(endDate, ChronoUnit.DAYS);
        long a = days % 7;
        long t = 0;
        if (a > 0) {
            for (long i = 1; i < a; i++) {
                int value = startDate.plus(i, ChronoUnit.DAYS).getDayOfWeek().getValue();
                if (weekDay.contains(value + "")) {
                    t++;
                }
            }
        }
        return listIntervalWeekDates(startTime, weekDay, (int) (weeks * length + t));
    }



    public static Integer getRemainSecondsOneDay(Date currentDate) {
        Calendar midnight = Calendar.getInstance();
        midnight.setTime(currentDate);
        midnight.add(Calendar.DAY_OF_MONTH, 1);
        midnight.set(Calendar.HOUR_OF_DAY, 0);
        midnight.set(Calendar.MINUTE, 0);
        midnight.set(Calendar.SECOND, 0);
        midnight.set(Calendar.MILLISECOND, 0);
        Integer seconds = (int) ((midnight.getTime().getTime() - currentDate.getTime()) / 1000);
        return seconds;
    }


    public static Date mergeDateAndTime(Date date, Date time) {
        String endDate = DateUtils.formatDate(date, DateUtils.YYYY_MM_DD);
        String endTime = DateUtils.formatDate(time, DateUtils.HH_MM_SS);
        Date awardExpireTime = DateUtils.getDate(endDate + " " + endTime, DateUtils.YYYY_MM_DD_HH_MM_SS);
        return awardExpireTime;
    }

    /**
     * 计算两个时间相隔多少秒
     * @param start
     * @param end
     * @return
     */
    public static int betweenMinute(Date start , Date end){
        return (int)(end.getTime()/1000- start.getTime()/1000);
    }


}

