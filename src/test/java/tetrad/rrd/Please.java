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

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import net.cit.tetrad.rrd.bean.DbStatus;
import net.cit.tetrad.rrd.utils.StringUtil;
import net.cit.tetrad.rrd.utils.TimestampUtil;
import net.cit.tetrad.utility.CommonUtils;

import org.apache.commons.beanutils.BeanUtils;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.FetchData;
import org.rrd4j.core.FetchRequest;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;
import org.rrd4j.core.Util;

public class Please {
	
	public static void createRrd() throws IOException {
		long time = TimestampUtil.readTimestamp();
		RrdDef rrdDef = new RrdDef("D:/jegil/all.rrd");
		
		rrdDef.setStartTime(time);
		rrdDef.setStep(10);
		rrdDef.addDatasource("useMem", DsType.GAUGE, 10, 0, Double.NaN);
		rrdDef.addDatasource("unUseMem", DsType.GAUGE, 10, 0,Double.NaN);
		
		rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 1, 800000);
		RrdDb rrdDb = new RrdDb(rrdDef);
		rrdDb.close();
	}
	
	public static void update() throws IOException {
		long time = TimestampUtil.readTimestamp();
				
		int allMemo = 1024;
		int unUseMemo = 500;
		int useMemo = allMemo - unUseMemo;
		
		RrdDb rrdDb = new RrdDb("D:/jegil/all.rrd");
		Sample sample = rrdDb.createSample();
		System.out.println(time);
		
		sample.setAndUpdate(time+300 + ":" + useMemo + ":" + unUseMemo);		
		rrdDb.close();
	}
	
	public static List<HashMap<String, Object>> fetchTetradRrdDb(String dsName, long startTime, long endTime) {
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		RrdDb rrdDb = null;
		FetchData fetchData = null;
		String rrdPath = null;
		
		try {
				// rrdPath 취득
				rrdPath = "D:/jegil/all.rrd";
				
				// RrdDb 취득
				rrdDb = new RrdDb(rrdPath);
				
				FetchRequest request = rrdDb.createFetchRequest(ConsolFun.AVERAGE, startTime, endTime);
				request.setFilter(StringUtil.rightSubstring(dsName, 20));
				
				// RrdDb로부터 데이터를 fetch한다.
				fetchData = request.fetchData();
				fetchData.dump();
				
				int columnCount = fetchData.getColumnCount();
				int rowCount = fetchData.getRowCount();
				long[] timestamps = fetchData.getTimestamps();
				double[][] values = fetchData.getValues();
				double[] dsValue = fetchData.getValues(dsName);
				
				for (int row = 0 ; row < rowCount ; row++) {
					HashMap<String, Object> fetchRow = new HashMap<String, Object>();

					fetchRow.put("regtime", TimestampUtil.convTimestampToString(timestamps[row]));
					for (int dsIndex = 0 ; dsIndex < columnCount ; dsIndex++) {
						fetchRow.put(dsName, Util.formatDouble(values[dsIndex][row]));
					}
					result.add(fetchRow);
				}				
				rrdDb.close();
		} catch (Exception e) {
			e.printStackTrace();
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
	 * @throws IOException 
	 */
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		Please.createRrd();
		for (int i=0; i<10; i++) {
			Please.update();
		}
	}

}
