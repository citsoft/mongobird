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
package net.cit.tetrad.command.service.impl;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentMap;

import net.cit.tetrad.command.service.TetradCommandService;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.rrd.batch.DeviceInMemory;
import net.cit.tetrad.rrd.batch.MongoInMemory;
import net.cit.tetrad.rrd.dao.MongoStatusToMonitor;

import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class TetradCommandServiceImpl implements TetradCommandService{

	private MongoStatusToMonitor mongoStatusToMonitor;
	public void setMongoStatusToMonitor(MongoStatusToMonitor mongoStatusToMonitor) {
		this.mongoStatusToMonitor = mongoStatusToMonitor;
	}
	
	private Mongo getMongo(Mongo mongo, int deviceIdx){
		try {
			ConcurrentMap<Integer, Device> deviceGroup = DeviceInMemory.getDeviceGroup();
			Device curDevice = deviceGroup.get(deviceIdx);
			ConcurrentMap<Integer, Mongo> mongoGroup = MongoInMemory.getMongoGroup();
			mongo = mongoGroup.get(curDevice.getIdx());
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return mongo;
	}
	public Map<String, Object> insertCommand(int deviceIdx,String command) throws MongoException{
		Device newDevice = null;
		Mongo mongo = null;
		Map<String, Object> serverStatusFromMongo = new HashMap<String, Object>();
		try {
			mongo = getMongo(mongo, deviceIdx);
			serverStatusFromMongo = mongoStatusToMonitor.readMongoStatus(mongo, newDevice, command);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return serverStatusFromMongo;
	}
	
	public List<Object> collectionCommand(int deviceIdx, String collection) throws MongoException{
		Mongo mongo = null;
		List<Object> readMongoShards = new ArrayList<Object>();
		try {
			mongo = getMongo(mongo, deviceIdx);			
			readMongoShards = mongoStatusToMonitor.readMongoShards(mongo, collection, "config");
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return readMongoShards;
	}
	
	public List<Object> chunksGrpCommand(int deviceIdx, String collName, List<Object> dboLst) throws MongoException{
		Mongo mongo = null;
		List<Object> readMongoChunks = new ArrayList<Object>();
		try {
			mongo = getMongo(mongo, deviceIdx);			
			readMongoChunks = mongoStatusToMonitor.readMongoChunksGrp(mongo, collName, dboLst);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return readMongoChunks;
	}
	
	public String allServerStatus(int deviceIdx) throws MongoException{
		Device newDevice = null;
		Mongo mongo = null;
		String serverStatusFromMongo = null ;
		try {
			mongo = getMongo(mongo, deviceIdx);			
			serverStatusFromMongo = mongoStatusToMonitor.getAllServerStatus(mongo, newDevice);
		} catch (Exception e) {
//			e.printStackTrace();
		}
		return serverStatusFromMongo;
	}
}
