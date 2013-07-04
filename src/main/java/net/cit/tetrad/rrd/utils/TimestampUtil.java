/**
*    Copyright (C) 2012 Cardinal Info.Tech.Co.,Ltd.
*
*    This program is free software: you can redistribute it and/or modify
*    it under the terms of the GNU Affero General Public License, version 3,
*    as published by the Free Software Foundation.
*
*    This program is distributed in the hope that it will be useful,
*    but WITHOUT ANY WARRANTY; without even the implied warranty of
*    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
*    GNU Affero General Public License for more details.
*
*    You should have received a copy of the GNU Affero General Public License
*    along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/
package net.cit.tetrad.rrd.utils;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

import org.rrd4j.core.Util;

public class TimestampUtil {
	
	public static long readTimestamp(int year, int month, int day, int hour, int min) {
		return Util.getTimestamp(year, month, day, hour, min);
	}
	
	public static long readTimestamp(int year, int month, int day) {
		return Util.getTimestamp(year, month, day, 0, 0);
	}
	
	public static long readTimestamp(String day, String format) {
		long timestamp = 0L;
		try {
			SimpleDateFormat sdf = new SimpleDateFormat(format);
			Date t = sdf.parse(day);
			timestamp = Util.getTimestamp(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timestamp;
	}

	public static long readTimestamp(String key) {
		long timestamp = 0L;
		try {
			String strTimestamp = TetradRrdConfig.getTetradRrdConfig(key);
			SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmm");
			Date t = sdf.parse(strTimestamp);
			timestamp = Util.getTimestamp(t);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return timestamp;
	}
	
	public static long readTimestamp() {
		Date t = new Date();
		return Util.getTimestamp(t);
	}
	
	public static String readCurrentTime() {
		Date dateNow = Calendar.getInstance(new SimpleTimeZone(9 * 60 * 60 * 1000, "KST")).getTime();
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS", Locale.getDefault());
		return formatter.format(dateNow);
	}
	
	/**
	 * String 날짜를 yyyyMMddHHmmssSSS 포멧으로 받아 시간을 더한 값을 string으로 return
	 * @param dateStr
	 * @param hour
	 * @return
	 * @throws ParseException
	 */
	public static String plusHour(String dateStr, int hour) throws ParseException{
		SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
		Date date = formatter.parse(dateStr);
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, hour);
		String resultStr = formatter.format(cal.getTime());
		return resultStr;
	}
	
	public static long convStringToTimestamp(String dateStr){
		long timestamp = 0L;
		try {
			SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmssSSS");
			Date t = formatter.parse(dateStr);
			timestamp = Util.getTimestamp(t);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return timestamp;
	}
	
	public static String convTimestampToString(long timestamp) {
		Date date = Util.getDate(timestamp);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmss");
		return sdf.format(date);
	}
	
	public static String convTimestampToString(long timestamp, String format) {
		Date date = Util.getDate(timestamp);
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

}
