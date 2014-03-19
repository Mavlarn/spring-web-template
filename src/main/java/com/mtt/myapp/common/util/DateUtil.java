package com.mtt.myapp.common.util;


import java.util.LinkedHashMap;
import java.util.Map;
import java.util.TimeZone;

import org.apache.commons.lang3.StringUtils;

/**
 * Date Utility.
 * 
 * @author Mavlarn
 * @since 1.0
 */
public abstract class DateUtil {

	private static final int CONSTANT_10 = 10;
	private static final int CONSTANT_24 = 24;
	private static final int CONSTANT_60 = 60;
	private static final int CONSTANT_1000 = 1000;
	private static final int SS = CONSTANT_1000;
	private static final int MI = SS * CONSTANT_60;
	private static final int HH = MI * CONSTANT_60;
	private static final int DD = HH * CONSTANT_24;

	private static Map<String, String> timezoneIDMap;

	/**
	 * Get time zones.
	 * 
	 * @return map typezone id and GMT
	 */
	public static Map<String, String> getFilteredTimeZoneMap() {
		if (timezoneIDMap == null) {
			timezoneIDMap = new LinkedHashMap<String, String>();
			String[] ids = TimeZone.getAvailableIDs();
			for (String id : ids) {
				TimeZone zone = TimeZone.getTimeZone(id);
				int offset = zone.getRawOffset();
				int offsetSecond = offset / CONSTANT_1000;
				int hour = offsetSecond / (CONSTANT_60 * CONSTANT_60);
				int minutes = (offsetSecond % (CONSTANT_60 * CONSTANT_60)) / CONSTANT_60;
				timezoneIDMap.put(TimeZone.getTimeZone(id).getDisplayName(),
								String.format("(GMT%+d:%02d) %s", hour, minutes, id));
			}
		}
		return timezoneIDMap;
	}


	/**
	 * Convert millisecond to DD:HH:MM:SS style.
	 *
	 * @param ms
	 *            Millisecond
	 * @return DD:HH:MM:SS formated string
	 */
	public static String ms2Time(long ms) {
		long day = ms / DD;
		long hour = (ms - day * DD) / HH;
		long minute = (ms - day * DD - hour * HH) / MI;
		long second = (ms - day * DD - hour * HH - minute * MI) / SS;

		String strDay = day < CONSTANT_10 ? "0" + day : "" + day;
		String strHour = hour < CONSTANT_10 ? "0" + hour : "" + hour;
		String strMinute = minute < CONSTANT_10 ? "0" + minute : "" + minute;
		String strSecond = second < CONSTANT_10 ? "0" + second : "" + second;
		strDay = (StringUtils.equals(strDay, "00")) ? "" : strDay + ":";
		return strDay + strHour + ":" + strMinute + ":" + strSecond;
	}

	/**
	 * Convert time to millisecond.
	 *
	 * @param day
	 *            day
	 * @param hour
	 *            hour
	 * @param min
	 *            min
	 * @param sec
	 *            sec
	 * @return converted millisecond
	 */
	public static long timeToMs(int day, int hour, int min, int sec) {
		return ((long) CONSTANT_1000) * (((day * CONSTANT_24 + hour) * CONSTANT_60 + min) * CONSTANT_60 + sec);
	}

}
