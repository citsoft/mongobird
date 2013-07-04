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
package net.cit.tetrad.common;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;
import java.util.SimpleTimeZone;

public class DateUtil {

	/**
	 * 현재날짜 리턴 (yyyy-MM-dd HH:mm:ss)
	 * @return String
	 */
	public static String getTime()
	{
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		return sdf.format(d);
	}
	
	/**
	 * 현재날짜를 파라메터로 넘어온 포멧에 맞게 리턴
	 * @param String format
	 * @return String
	 */
	public static String getCurrentDate(String format)
	{
		Date d = new Date();
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(d);
	}
	
	public static String getCurrentDate(Date date, String format)
	{
		SimpleDateFormat sdf = new SimpleDateFormat(format);
		return sdf.format(date);
	}

	public static Date dateformat(String text, String format) throws ParseException{
		SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		Date date = simpleDateFormat.parse(text);
		return date;
	}
	
	/**
	 * 현재날짜 리턴 
	 * @return String
	 */
	public final static String getCurrentDate2(String format) {
		Date dateNow = Calendar.getInstance(new SimpleTimeZone(9 * 60 * 60 * 1000, "KST")).getTime();
		SimpleDateFormat formatter = new SimpleDateFormat(format, Locale.getDefault());
		return formatter.format(dateNow);
	}

	/**
	 *  지정된 날짜에서 원하는 일수 만큼 이동된 날짜를 반환한다.(형식 지정)
	 *
	 * @param strDate 지정된 일자(String)
	 * @param offset 이동할 일수( -2147483648 ~ 2147483647 )
	 * @param pFormat 날짜형식
	 * @return 변경된 날짜
	 */
	public static String getOffsetDate(String strDate, int offset, String pFormat) throws Exception {
	    SimpleDateFormat fmt = new SimpleDateFormat(pFormat);
	    Date date = fmt.parse(strDate);
	    return getOffsetDate(date, offset, pFormat);
	}
	
	/**
	 *  지정된 날짜에서 원하는 일수 만큼 이동된 날짜를 반환한다.(형식 지정)
	 *
	 * @param pDate Date 객체
	 * @param offset 이동할 일수( -2147483648 ~ 2147483647 )
	 * @param pFormat 날짜형식
	 * @return 변경된 날짜
	 */
	public static String getOffsetDate(Date pDate, int offset, String pFormat) throws Exception {
	    SimpleDateFormat fmt = new SimpleDateFormat(pFormat);
	    Calendar c = Calendar.getInstance();
	    String ret = "";

	    try {
	        c.setTime(pDate);
	        c.add(Calendar.DAY_OF_MONTH, offset);
	        ret = fmt.format(c.getTime());
	    }
	    catch(Exception e) {
	    }
	    return ret;
	}
	
	/**
	 *  지정된 날짜에서 원하는 일수 만큼 이동된 날짜를 반환한다.(형식 지정)
	 *
	 * @param strDate 지정된 일자(String)
	 * @param offset 이동할 시간
	 * @param pFormat 날짜형식
	 * @return 변경된 날짜
	 */
	public static String getOffsetDateTime(String strDate, int offset, String pFormat) throws Exception {
	    SimpleDateFormat fmt = new SimpleDateFormat(pFormat);
	    Date date = fmt.parse(strDate);
	    return getOffsetDateTime(date, offset, pFormat);
	}
	
	/**
	 *  지정된 날짜에서 원하는 일수 만큼 이동된 날짜를 반환한다.(형식 지정)
	 *
	 * @param pDate Date 객체
	 * @param offset 이동할 시간
	 * @param pFormat 날짜형식
	 * @return 변경된 날짜
	 */
	public static String getOffsetDateTime(Date pDate, int offset, String pFormat) throws Exception {
	    SimpleDateFormat fmt = new SimpleDateFormat(pFormat);
	    Calendar c = Calendar.getInstance();
	    String ret = "";

	    try {
	        c.setTime(pDate);
	        c.add(Calendar.HOUR_OF_DAY, offset);
	        ret = fmt.format(c.getTime());
	    }
	    catch(Exception e) {
	    }
	    return ret;
	}

	/**
	 *  지정된 날짜에서 원하는 일수 만큼 이동된 날짜를 반환한다.(형식 지정)
	 *
	 * @param strDate 지정된 일자(String)
	 * @param offset 이동할 시간(분)
	 * @param pFormat 날짜형식
	 * @return 변경된 날짜
	 */
	public static String getOffsetDateMin(String strDate, int offset, String pFormat) throws Exception {
	    SimpleDateFormat fmt = new SimpleDateFormat(pFormat);
	    Date date = fmt.parse(strDate);
	    return getOffsetDateMin(date, offset, pFormat);
	}
	
	/**
	 *  지정된 날짜에서 원하는 일수 만큼 이동된 날짜를 반환한다.(형식 지정)
	 *
	 * @param pDate Date 객체
	 * @param offset 이동할 시간(분)
	 * @param pFormat 날짜형식
	 * @return 변경된 날짜
	 */
	public static String getOffsetDateMin(Date pDate, int offset, String pFormat) throws Exception {
	    SimpleDateFormat fmt = new SimpleDateFormat(pFormat);
	    Calendar c = Calendar.getInstance();
	    String ret = "";

	    try {
	        c.setTime(pDate);
	        c.add(Calendar.MINUTE, offset);
	        ret = fmt.format(c.getTime());
	    }
	    catch(Exception e) {
	    }
	    return ret;
	}

	/**
	 *  입력된 두개의 날짜를 비교
	 *
	 * @param strDate1 입력 날짜(문자형)
	 * @param strDate2 입력 날짜(문자형)
	 * @param pFormat 날짜형식
	 * @return int
	 * 		   -1 : cal1 < cal2
	 *          0 : cal1 = cal2
	 *          1 : cal1 > cal2
	 */
	public static int getCompareDate(String strDate1, String strDate2, String pFormat) throws Exception {

	    SimpleDateFormat fmt = new SimpleDateFormat(pFormat);
	    Date date1 = fmt.parse(strDate1);
	    Date date2 = fmt.parse(strDate2);

	    return compareDate(date1, date2);
	}
	/**
	 * 두 Date형 객체 비교
	 * @param Date date1
	 * @param Date date2
	 * @return int
	 * 		   -1 : cal1 < cal2
	 *          0 : cal1 = cal2
	 *          1 : cal1 > cal2
	 */
	public static int compareDate( Date date1, Date date2 )
	{
		Calendar c1 = Calendar.getInstance();
		c1.setTime(date1);
		Calendar c2 = Calendar.getInstance();
		c2.setTime(date2);

		return compareDate(c1, c2);
	}
	
	/**
	 * 두 Calendar 객체비교
	 * @param Calendar cal1
	 * @param Calendar cal2
	 * @return -1 : cal1 < cal2
	 *          0 : cal1 = cal2
	 *          1 : cal1 > cal2
	 */
	public static int compareDate( Calendar cal1, Calendar cal2 )
	{
		int value = 9;

		if (cal1.before(cal2)) {
			value = -1;
		}
		if (cal1.after(cal2)) {
			value = 1;
		}
		if (cal1.equals(cal2)) {
			value = 0;
		}
		return value;
	}
	
	public static Date plusMonth(int month) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MONTH, month);
		
		return cal.getTime();
	}
	
	public static Date plusMonth(Date date, int month) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MONTH, month);
		
		return cal.getTime();
	}
	
	public static Date plusDay(int day) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.DATE, day);
		
		return cal.getTime();
	}
	
	public static Date plusDay(Date date, int day) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.DATE, day);
		
		return cal.getTime();
	}
	
	public static Date plusHour(int hour) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.HOUR, hour);
		
		return cal.getTime();
	}
	
	public static Date plusHour(Date date, int hour) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.HOUR, hour);
		
		return cal.getTime();
	}	

	public static Date plusMinute(int minute) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.MINUTE, minute);
		
		return cal.getTime();
	}
	
	public static Date plusMinute(Date date, int minute) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.MINUTE, minute);
		
		return cal.getTime();
	}
	
	public static Date plusSecond(int second) {
		Calendar cal = Calendar.getInstance();
		cal.add(Calendar.SECOND, second);
		
		return cal.getTime();
	}
	
	public static Date plusSecond(Date date, int second) {
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		cal.add(Calendar.SECOND, second);
		
		return cal.getTime();
	}

	/**
	 * 지정된 두 일자의 차이 일수를 반환한다.
	 *
	 * @param strDate 지정된 일자
	 * @param strDate2 지정된 일자
	 * @param pFormat  지정된 날짜 형식
	 * @return 차이 일수
	 * @throws Exception
	 */
	public static int getOffsetMins(String strDate, String strDate2) throws Exception {
			 String pFormat = "yyyy-MM-dd HH:mm";
	    SimpleDateFormat fmt = new SimpleDateFormat(pFormat);
	    SimpleDateFormat fmt2 = new SimpleDateFormat(pFormat);

	    Date paramDate1 = fmt.parse(strDate);
	    Date paramDate2 = fmt2.parse(strDate2);

	    return getOffsetMins(paramDate1, paramDate2);
	}

	/**
	 * 지정된 두 일자의 차이 일수를 반환한다.
	 *
	 * @param pDate1 지정된 일자1
	 * @param pDate2 지정된 일자2
	 * @return 차이 일수
	 * @throws Exception
	 */
	public static int getOffsetMins(Date pDate1, Date pDate2) throws Exception {
				long aa = pDate1.getTime();
				long bb = pDate2.getTime();
	    return (int)((aa - bb)/(1000 * 60));
	}
}
