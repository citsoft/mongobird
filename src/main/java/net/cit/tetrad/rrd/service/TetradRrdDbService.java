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
package net.cit.tetrad.rrd.service;

import java.io.FileNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import net.cit.tetrad.model.Alarm;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.rrd.bean.GraphDefInfo;
import net.cit.tetrad.rrd.bean.ServerStatus;

import org.rrd4j.core.FetchData;

import com.mongodb.CommandResult;
import com.mongodb.Mongo;

public interface TetradRrdDbService {

	public void createTetradRrdDb(Device device, String databaseName) throws Exception;
	public void createTetradRrdDb(Device device) throws Exception;
	void createTotalRrdDb() throws Exception;
	public List<HashMap<String, Object>> fetchTetradRrdDb(Device device, String[] filters, long startTime, long endTime, String databaseName); 
	public FetchData fetchTetradRrdDb(String rrdPath) throws Exception;
	public void restoreTetradRrdDb(String rrdPath, FetchData fetchData) throws Exception;	
	public Map<String, Object> insertTetradRrdDb(Mongo mongo, Device device, String databaseName, CommandResult serverResult) throws FileNotFoundException;
	public void insertTetradRrdDb(Mongo mongo, Device device) throws FileNotFoundException;
	public String graphTetradRrdDb(GraphDefInfo graphDefInfo, String databaseName, String sortItem);
	public String graphTetradRrdDb(GraphDefInfo graphDefInfo);
	String graphPerRrdDb(String rrdDb, GraphDefInfo graphDefInfo);
	String multiDeviceGraphPerRrd(String rrdDb, GraphDefInfo graphDefInfo);
	public List<ServerStatus> readTetradServerStatus();
	public List<Alarm> readTetradCriticalStatus();
	public void makeTotalMongodInfo() throws Exception;
	public String detailedGraphPerRrdDb(GraphDefInfo graphDefInfo);
	public String detailedMultiDeviceGraphPerRrd(String rrdDb, GraphDefInfo graphDefInfo, String title);
	public String graphSubRrdDb(GraphDefInfo graphDefInfo, String sortItem);
	public String lockGraphRrdDb(GraphDefInfo graphDefInfo, String sortItem);
	public String soloLockGraphRrdDb(GraphDefInfo graphDefInfo, String sortItem);
	public String totalMultiGraphPerRrd(GraphDefInfo graphDefInfo) ;
	int getThreadIndex(int deviceCode);
}
