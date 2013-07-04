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

import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.cit.tetrad.common.DateUtil;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.rrd.bean.GraphDefInfo;
import net.cit.tetrad.rrd.utils.StringUtil;
import net.cit.tetrad.rrd.utils.TimestampUtil;

import org.rrd4j.ConsolFun;
import org.rrd4j.core.FetchData;
import org.rrd4j.core.FetchRequest;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.Util;

import com.ibm.icu.math.BigDecimal;

public class TestTotalGraph {
	
	public GraphDefInfo getGraphDefInfo() {
		GraphDefInfo graph = new GraphDefInfo();
		
		List<Device> deviceGroup = new ArrayList<Device>();
		Device device = new Device();
		device.setGroupCode(3);
		device.setUid("mine");
		device.setType("mongod");
		device.setGroupCode(1);
		deviceGroup.add(device);
		graph.setDeviceGroup(deviceGroup);
		graph.setDevice(device);
		
		String[] filters = {"connections_current", "connections_available", "dbDataSize"};
		graph.setFilters(filters);
		
		long endTime = setEndTime();
		long startTime = setStartTime();
		
//		String t = TimestampUtil.convTimestampToString(endTime, "yyyyMMdd hh:mm:ss");
//		String t2 = TimestampUtil.convTimestampToString(setEndTime2(), "yyyyMMdd hh:mm:ss");
//		
//		System.out.println(t + "=" + t2);
		
		graph.setEndTime(endTime);
		graph.setStartTime(startTime);
		
//		graph.setWidth(230-80);
//		graph.setHeight(140-54+19);
		graph.setWidth(400);
		graph.setHeight(300);
		
		return graph;
	}
	
	public long setEndTime() {
//		String current = DateUtil.getCurrentDate(DateUtil.minusMinute(10), "yyyy,MM,dd,HH,mm");
//		String current = DateUtil.getCurrentDate(DateUtil.minusMinute(10), "yyyy,MM,dd,HH,mm");
//		String current = DateUtil.getCurrentDate2("yyyy,MM,dd,HH,mm");
//		System.out.println("end currnet : " + current);
		
//		int year = Integer.parseInt(current.split(",")[0]);
//		int mon = Integer.parseInt(current.split(",")[1]);
//		int day = Integer.parseInt(current.split(",")[2]);
//		int hour = Integer.parseInt(current.split(",")[3]);
//		int min = Integer.parseInt(current.split(",")[4]);
		
		return Util.getTimestamp(DateUtil.plusSecond(0));
		
//		return  TimestampUtil.readTimestamp(year, mon, day, hour, min);
	}
	
	public long setEndTime2() {
		String current = DateUtil.getCurrentDate(DateUtil.plusMinute(10), "yyyy,MM,dd,HH,mm");
		System.out.println("end currnet : " + current);
		
		int year = Integer.parseInt(current.split(",")[0]);
		int mon = Integer.parseInt(current.split(",")[1]);
		int day = Integer.parseInt(current.split(",")[2]);
		int hour = Integer.parseInt(current.split(",")[3]);
		int min = Integer.parseInt(current.split(",")[4]);
		
//		return Util.getTimestamp(DateUtil.minusMinute(10));
		
		return  TimestampUtil.readTimestamp(year, mon, day, hour, min);
	}
	
	public long setStartTime() {
//		String current = DateUtil.getCurrentDate(DateUtil.minusHour(0), "yyyy,MM,dd,HH,mm");
//		String current = DateUtil.getCurrentDate(DateUtil.plusSecond(-20), "yyyy,MM,dd,HH,mm");
//		System.out.println("start currnet : " + current);
		
//		int year = Integer.parseInt(current.split(",")[0]);
//		int mon = Integer.parseInt(current.split(",")[1]);
//		int day = Integer.parseInt(current.split(",")[2]);
//		int hour = Integer.parseInt(current.split(",")[3]);
//		int min = Integer.parseInt(current.split(",")[4]);
//		
//		return  TimestampUtil.readTimestamp(year, mon, day, hour, min);
		return  Util.getTimestamp(DateUtil.plusSecond(-30));
	}
	
	public List<HashMap<String, Object>> fetchTetradRrdDb(String dsName, long startTime, long endTime) {
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		RrdDb rrdDb = null;
		FetchData fetchData = null;
		String rrdPath = null;

		try {
				// rrdPath 취득
			rrdPath = "/home/jaemin/rrd/15/extra_info_page_faults.rrd";
//				rrdPath = CommonUtils.getDefaultRrdPath();
//				rrdPath += "totalInfo" + ".rrd";
//				rrdPath += "17/local/locks_timeLockedMicros_w" + ".rrd";
				
				// RrdDb 취득
				rrdDb = new RrdDb(rrdPath);
				
				FetchRequest request = rrdDb.createFetchRequest(ConsolFun.TOTAL, startTime, endTime);
				request.setFilter(StringUtil.rightSubstring(dsName, 20));
				
				// RrdDb로부터 데이터를 fetch한다.
				fetchData = request.fetchData();
//				System.out.println(fetchData.dump());
				
				int columnCount = fetchData.getColumnCount();
				int rowCount = fetchData.getRowCount();
				long[] timestamps = fetchData.getTimestamps();
				double[][] values = fetchData.getValues();
				double[] dsValue = fetchData.getValues(StringUtil.rightSubstring(dsName, 20));
				
				for (int row = 0 ; row < rowCount ; row++) {
					HashMap<String, Object> fetchRow = new HashMap<String, Object>();
//					DbStatus fetchRowToBean = new DbStatus();
					fetchRow.put("regtime", TimestampUtil.convTimestampToString(timestamps[row]));
					for (int dsIndex = 0 ; dsIndex < columnCount ; dsIndex++) {
						//						fetchRow.put(dsName, Util.formatDouble(values[dsIndex][row]));
						double myDouble = 0;
						try {
							BigDecimal myNumber = new BigDecimal(values[dsIndex][row]);
							myDouble = myNumber.doubleValue();
						} catch (Exception e) {

						}
						fetchRow.put(dsName, myDouble);
					}
					 //		BeanUtils.populate(fetchRowToBean, fetchRow);
					result.add(fetchRow);
				}				
				rrdDb.close();
		} catch (Exception e) {
			e.printStackTrace();
			result = new ArrayList<HashMap<String, Object>>();
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
	
	public String getCommaNumber(double amt, int dec) {
		NumberFormat nf = NumberFormat.getNumberInstance();
		nf.setMaximumFractionDigits(dec);
		return nf.format(amt);
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
//		String[] configLocations = new String[]{"applicationContext_rrd.xml"};
//		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
//		
//		TetradRrdDbService rrd = (TetradRrdDbService)context.getBean("tetradRrdDbService");
//		
		TestTotalGraph testGraph = new TestTotalGraph();
//		GraphDefInfo graph = testGraph.getGraphDefInfo();
//		graph.setFileName("testTotalDataSizeGraph"+TimestampUtil.readTimestamp());
//		rrd.graphPerRrdDb("totalDbDataSize", graph);
//		
//		graph.setFileName("testTotalIndexSizeGraph"+TimestampUtil.readTimestamp());
//		rrd.graphPerRrdDb("totalDbIndexSize", graph);
//		
//		TimestampUtil.readTimestamp(2012, 11, 1, 10, 30);

		long	startTime = TimestampUtil.readTimestamp("2012-11-01 15:20", "yyyy-MM-dd HH:mm");
		long	endTime = TimestampUtil.readTimestamp("2012-11-01 15:30", "yyyy-MM-dd HH:mm");
		List<HashMap<String, Object>> arrDb = testGraph.fetchTetradRrdDb("extra_info_page_faults", startTime, endTime);
		System.out.println(arrDb);
	}

}
