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
import net.cit.tetrad.rrd.utils.MongoWrapper;

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
			ConcurrentMap<Integer, MongoWrapper> mongoGroup = MongoInMemory.getMongoGroup();
			mongo = mongoGroup.get(curDevice.getIdx()).getMongo();
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
