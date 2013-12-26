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

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import net.cit.tetrad.model.Device;
import net.cit.tetrad.rrd.dao.DataAccessObjectForMongo;
import net.cit.tetrad.rrd.utils.MongoWrapper;

import com.mongodb.DB;

public class MongoInMemory {	
	private DataAccessObjectForMongo dataAccessObjectForMongo;
	private static MongoWrapper mongoWrapper;
	public static ConcurrentMap<Integer, MongoWrapper> mongoGroup = new ConcurrentHashMap<Integer, MongoWrapper>(); 	
	public static ConcurrentLinkedQueue<MongoWrapper> toReconnectingMongos = new ConcurrentLinkedQueue<MongoWrapper>(); 	
	

	public void setDataAccessObjectForMongo(
			DataAccessObjectForMongo dataAccessObjectForMongo) {
		this.dataAccessObjectForMongo = dataAccessObjectForMongo;
	}
	
	public void createMongoGroup(){
		List<Device> deviceLst = dataAccessObjectForMongo.readDeviceList();
		for(Device device : deviceLst){
			setMongo(device.getIp(), Integer.parseInt(device.getPort()), device.getAuthUser(), device.getAuthPasswd());
			mongoGroup.putIfAbsent(device.getIdx(), mongoWrapper);
		}
	}
	
	public static ConcurrentMap<Integer, MongoWrapper> getMongoGroup() {
		return mongoGroup;
	}
	
	public static void addMongoIntoMap(Device device) {
		setMongo(device.getIp(), Integer.parseInt(device.getPort()), device.getAuthUser(), device.getAuthPasswd());
		mongoGroup.putIfAbsent(device.getIdx(), mongoWrapper);
	}
	
	public static void updateMongoMap(Device device) {
		setMongo(device.getIp(), Integer.parseInt(device.getPort()), device.getAuthUser(), device.getAuthPasswd());
		mongoGroup.replace(device.getIdx(), mongoWrapper);
	}
	
	public static void deleteMongoMap(int idx) {		
		mongoGroup.remove(idx);
	}
	
	public static MongoWrapper setMongo(String ip, int port, String authUser, String authPasswd){
		try {
			mongoWrapper = new MongoWrapper();
			mongoWrapper.connect(ip, port);
			
			DB db = mongoWrapper.getMongo().getDB("admin");
			if(!authUser.equals("") && !authPasswd.equals("")){
				boolean auth = db.authenticate(authUser, authPasswd.toCharArray());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mongoWrapper;
	}
	
	public static ConcurrentLinkedQueue<MongoWrapper> getToReconnectingMongos() {
		return toReconnectingMongos;
	}
	
	public static void putToReconnectingMongo(MongoWrapper mongoWrapper) {
		if (!toReconnectingMongos.contains(mongoWrapper)) {
			toReconnectingMongos.add(mongoWrapper);
		}
	}
}
