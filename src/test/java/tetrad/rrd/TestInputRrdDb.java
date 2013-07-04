/*******************************************************************************
 * "mongobird" is released under a dual license model designed to developers 
 * and commercial deployment.
 * 
 * For using OEMs(Original Equipment Manufacturers), ISVs(Independent Software
 * Vendor), ISPs(Internet Service Provider), VARs(Value Added Resellers) 
 * and another distributors, or for using include changed issue
 * (modify / application), it must have to follow the Commercial License policy.
 * To check the Commercial License Policy, you need to contact Cardinal Info.Tech.Co., Ltd.
 * (http://www.citsoft.net)
 *  *
 * If not using Commercial License (Academic research or personal research),
 * it might to be under AGPL policy. To check the contents of the AGPL terms,
 * please see "http://www.gnu.org/licenses/"
 ******************************************************************************/
package tetrad.rrd;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import net.cit.tetrad.common.DateUtil;
import net.cit.tetrad.rrd.bean.DbStatus;
import net.cit.tetrad.rrd.utils.StringUtil;
import net.cit.tetrad.rrd.utils.TimestampUtil;
import net.cit.tetrad.utility.CommonUtils;

import org.apache.commons.beanutils.BeanUtils;
import org.rrd4j.ConsolFun;
import org.rrd4j.core.FetchData;
import org.rrd4j.core.FetchRequest;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.Sample;
import org.rrd4j.core.Util;

import tetrad.rrd.RRDTest.GaugeSource;

public class TestInputRrdDb {
	static final long SEED = 1909752002L;
	  static final Random RANDOM = new Random(SEED);
	 static final int MAX_STEP = 300;
	 
	 static final String FILE = "all";
	 static final long START = Util.getTimestamp(2003, 4, 1);
	  static final long END = Util.getTimestamp(2003, 5, 1);
	 
	public static long setEndTime() {
		String current = DateUtil.getCurrentDate2("yyyy,MM,dd,HH,mm");
		System.out.println("end currnet : " + current);
		
		int year = Integer.parseInt(current.split(",")[0]);
		int mon = Integer.parseInt(current.split(",")[1]);
		int day = Integer.parseInt(current.split(",")[2]);
		int hour = Integer.parseInt(current.split(",")[3]);
		int min = Integer.parseInt(current.split(",")[4]);
		
		return  TimestampUtil.readTimestamp(year, mon, day, hour, min);
	}
	
	public static long setStartTime() {
		String current = DateUtil.getCurrentDate(DateUtil.plusMinute(-50), "yyyy,MM,dd,HH,mm");
		System.out.println("start currnet : " + current);
		
		int year = Integer.parseInt(current.split(",")[0]);
		int mon = Integer.parseInt(current.split(",")[1]);
		int day = Integer.parseInt(current.split(",")[2]);
		int hour = Integer.parseInt(current.split(",")[3]);
		int min = Integer.parseInt(current.split(",")[4]);
		
		return  TimestampUtil.readTimestamp(year, mon, day, hour, min);
	}
	
	public List<DbStatus> input(String dsName, Calendar cal) {
		List<DbStatus> result = new ArrayList<DbStatus>();
		RrdDb rrdDb = null;

		try {
			// rrdPath 취득
			String rrdPath = Util.getRrd4jDemoPath(FILE + ".rrd");
			
			System.out.println(DateUtil.getCurrentDate(cal.getTime(), "yyyyMMdd hh:mm:ss"));
			
			
			rrdDb = new RrdDb(rrdPath);			
			Sample sample = rrdDb.createSample();

			if (rrdDb.containsDs(dsName)) {
				System.out.println("rrdDb contains " + dsName);
			}
			
			long start = START;
			long end = END;
			
			long t = start;
			int n = 0;
			GaugeSource sunSource = new GaugeSource(1200, 20);
			GaugeSource shadeSource = new GaugeSource(300, 10);
			while (t <= end + 86400L) {
				sample.setTime(t);
				sample.setValue("totalDbDataSize", sunSource.getValue());
				sample.update();
				t += RANDOM.nextDouble() * MAX_STEP + 1;
				if (((++n) % 1000) == 0) {
					System.out.print("*");
				}
			}
					
			
			System.out.println(StringUtil.rightSubstring(dsName, 20));
			
//			sample.setAndUpdate(dsName + ":" + 2300);
			System.out.println(sample.dump());
			rrdDb.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = new ArrayList<DbStatus>();
		} finally {
			try {
				if (rrdDb != null) 
					rrdDb.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}

	public List<DbStatus> fetchTetradRrdDb(String dsName) {
		List<DbStatus> result = new ArrayList<DbStatus>();
		RrdDb rrdDb = null;
		FetchData fetchData = null;
		
		try {
			String xmlPath = Util.getRrd4jDemoPath(FILE + ".xml");
			String rrdRestoredPath = Util.getRrd4jDemoPath(FILE + "_restored.rrd");
				// rrdPath 취득
			String rrdPath = Util.getRrd4jDemoPath(FILE + ".rrd");
				
				// RrdDb 취득
				rrdDb = new RrdDb(rrdPath, true);
				
				FetchRequest request = rrdDb.createFetchRequest(ConsolFun.AVERAGE, START, END);
				System.out.println(request.dump());
				// RrdDb로부터 데이터를 fetch한다.
				fetchData = request.fetchData();
				System.out.println(fetchData.toString());
				
				 rrdDb.exportXml(xmlPath);
				 System.out.println("== Creating RRD file " + rrdRestoredPath + " from XML file " + xmlPath);
				    RrdDb rrdRestoredDb = new RrdDb(rrdRestoredPath, xmlPath);
				rrdDb.close();
				 rrdRestoredDb.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = new ArrayList<DbStatus>();
		} finally {
			try {
				if (rrdDb != null) 
					rrdDb.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return result;
	}
	public static void main(String[] args) {
		TestInputRrdDb inputDb = new TestInputRrdDb();
		Date date = new Date();
		Calendar cal = Calendar.getInstance();
		cal.setTime(date);
		
//		for (int i=0; i<1000; i++) {
			
			cal.add(Calendar.SECOND, 10);			
//			inputDb.input("totalDbDataSize", cal);
			
			inputDb.fetchTetradRrdDb("totalDbDataSize");
//		}
	}
	
	static class GaugeSource {
		private double value;
		private double step;

		GaugeSource(double value, double step) {
			this.value = value;
			this.step = step;
		}

		long getValue() {
			double oldValue = value;
			double increment = RANDOM.nextDouble() * step;
			if (RANDOM.nextDouble() > 0.5) {
				increment *= -1;
			}
			value += increment;
			if (value <= 0) {
				value = 0;
			}
			return Math.round(oldValue);
		}
	}
}
