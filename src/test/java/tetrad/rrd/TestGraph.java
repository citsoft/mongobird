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
import java.util.HashMap;
import java.util.List;

import net.cit.tetrad.common.DateUtil;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.rrd.bean.DbStatus;
import net.cit.tetrad.rrd.bean.GraphDefInfo;
import net.cit.tetrad.rrd.service.TetradRrdDbService;
import net.cit.tetrad.rrd.utils.StringUtil;
import net.cit.tetrad.rrd.utils.TimestampUtil;
import net.cit.tetrad.utility.CommonUtils;

import org.apache.commons.beanutils.BeanUtils;
import org.rrd4j.ConsolFun;
import org.rrd4j.core.FetchData;
import org.rrd4j.core.FetchRequest;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.Util;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestGraph {
	
	public GraphDefInfo getGraphDefInfo() {
		GraphDefInfo graph = new GraphDefInfo();
		
		List<Device> deviceGroup = new ArrayList<Device>();
		Device device = new Device();
		device.setGroupCode(1);
		device.setUid("mine");
		device.setType("mongod");
		device.setGroupCode(1);
		deviceGroup.add(device);
		graph.setDeviceGroup(deviceGroup);
		graph.setDevice(device);
		
		String[] filters = {"dbDataSize"};
		graph.setFilters(filters);
		
		long endTime = setEndTime();
		long startTime = setStartTime();
		
		System.out.println("endTime	:" + TimestampUtil.convTimestampToString(endTime));
		System.out.println("startTime	:" + TimestampUtil.convTimestampToString(startTime));
		
		graph.setEndTime(endTime);
		graph.setStartTime(startTime);
		
//		graph.setWidth(230-80);
//		graph.setHeight(140-54+19);
		graph.setWidth(400);
		graph.setHeight(300);
		
		return graph;
	}
	
	public long setEndTime() {
		return Util.getTimestamp(DateUtil.plusSecond(0));
	}
	
	public long setStartTime() {
		return Util.getTimestamp(DateUtil.plusSecond(-30));
	}
	

	public List<DbStatus> fetchTetradRrdDb(String dsName, long startTime, long endTime) {
		List<DbStatus> result = new ArrayList<DbStatus>();
		RrdDb rrdDb = null;
		FetchData fetchData = null;
		String rrdPath = null;
		
		try {
				// rrdPath 취득
				rrdPath = CommonUtils.getDefaultRrdPath();
				rrdPath += "LOCALHOST\\mongod\\mine\\" + dsName + ".rrd";
				
				// RrdDb 취득
				rrdDb = new RrdDb(rrdPath);
				
				FetchRequest request = rrdDb.createFetchRequest(ConsolFun.AVERAGE, startTime, endTime);
				request.setFilter(StringUtil.rightSubstring(dsName, 20));
				
				// RrdDb로부터 데이터를 fetch한다.
				fetchData = request.fetchData();
				System.out.println(fetchData.dump());
				
				int columnCount = fetchData.getColumnCount();
				int rowCount = fetchData.getRowCount();
				long[] timestamps = fetchData.getTimestamps();
				double[][] values = fetchData.getValues();
				double[] dsValue = fetchData.getValues(dsName);
				
				for (int row = 0 ; row < rowCount ; row++) {
					HashMap<String, Object> fetchRow = new HashMap<String, Object>();
					DbStatus fetchRowToBean = new DbStatus();
					fetchRow.put("regtime", TimestampUtil.convTimestampToString(timestamps[row]));
					for (int dsIndex = 0 ; dsIndex < columnCount ; dsIndex++) {
						fetchRow.put(dsName, Util.formatDouble(values[dsIndex][row]));
					}
					BeanUtils.populate(fetchRowToBean, fetchRow);
					result.add(fetchRowToBean);
				}				
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

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] configLocations = new String[]{"applicationContext_rrd.xml"};
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
		
		TetradRrdDbService rrd = (TetradRrdDbService)context.getBean("tetradRrdDbService");
		
		TestGraph testGraph = new TestGraph();
		GraphDefInfo graph = testGraph.getGraphDefInfo();
		graph.setFileName("tt");
//		rrd.graphPerRrdDb("dbDataSize", graph);
//		rrd.graphRrdDbGroup(graph);
//		rrd.graphTetradRrdDb(graph);
		
		testGraph.fetchTetradRrdDb("dbDataSize", graph.getStartTime(), graph.getEndTime());
	}

}
