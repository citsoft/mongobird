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
package net.cit.tetrad.rrd.batch;

import java.net.UnknownHostException;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.cit.tetrad.model.Device;
import net.cit.tetrad.rrd.dao.DataAccessObjectForMongo;

import com.mongodb.DB;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoInMemory {	
	private DataAccessObjectForMongo dataAccessObjectForMongo;	
	private static Mongo mongo = null;
	public static ConcurrentMap<Integer, Mongo> mongoGroup = new ConcurrentHashMap<Integer, Mongo>(); 	
	
	public void setDataAccessObjectForMongo(
			DataAccessObjectForMongo dataAccessObjectForMongo) {
		this.dataAccessObjectForMongo = dataAccessObjectForMongo;
	}
	
	public void createMongoGroup(){
		List<Device> deviceLst = dataAccessObjectForMongo.readDeviceList();
		for(Device device : deviceLst){
			setMongo(device.getIp(), Integer.parseInt(device.getPort()), device.getAuthUser(), device.getAuthPasswd());
			mongoGroup.putIfAbsent(device.getIdx(), mongo);
		}
	}
	
	public static ConcurrentMap<Integer, Mongo> getMongoGroup() {
		return mongoGroup;
	}
	
	public static void addMongoIntoMap(Device device) {
		setMongo(device.getIp(), Integer.parseInt(device.getPort()), device.getAuthUser(), device.getAuthPasswd());
		mongoGroup.putIfAbsent(device.getIdx(), mongo);
	}
	
	public static void updateMongoMap(Device device) {
		setMongo(device.getIp(), Integer.parseInt(device.getPort()), device.getAuthUser(), device.getAuthPasswd());
		mongoGroup.replace(device.getIdx(), mongo);
	}
	
	public static void deleteMongoMap(int idx) {		
		mongoGroup.remove(idx);
	}
	
	public static Mongo setMongo(String ip, int port, String authUser, String authPasswd){
		try {
			mongo = new Mongo(ip, port);
			DB db = mongo.getDB("admin");
			if(!authUser.equals("") && !authPasswd.equals("")){
				boolean auth = db.authenticate(authUser, authPasswd.toCharArray());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mongo;
	}
}
