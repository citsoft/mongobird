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
