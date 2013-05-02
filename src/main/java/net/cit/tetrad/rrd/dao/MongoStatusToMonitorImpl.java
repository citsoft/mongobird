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
package net.cit.tetrad.rrd.dao;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.cit.tetrad.model.Device;
import net.cit.tetrad.rrd.utils.JasonUtil;

import org.springframework.dao.DataAccessResourceFailureException;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class MongoStatusToMonitorImpl implements MongoStatusToMonitor {
	
	public Map<String, Object> readMongoStatus(Mongo mongo, Device device, String command, String databaseName) throws MongoException, Exception {
		Map<String, Object> deviceStatus = new HashMap<String, Object>();

		try {
			DB db = mongo.getDB(databaseName);
			CommandResult commandResult = db.command(command);
			deviceStatus = JasonUtil.getEntryValue(commandResult, "", deviceStatus);
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			throw new MongoException(e.getMessage());
		} catch (MongoException e) {
			// alarm 정보 등록
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			
		}
		
		return deviceStatus;
	}
	public List<Object> readMongoShards(Mongo mongo, String collection, String databaseName) throws MongoException, Exception {
		List<Object> dboLst = new ArrayList<Object>();
		try {
			DB db = mongo.getDB(databaseName);
			DBCollection dbcollection = db.getCollectionFromString(collection);
			DBCursor cr = dbcollection.find();
			Iterator<DBObject> it = cr.iterator();
			DBObject dbo;
			while(it.hasNext()) {
				dbo = it.next();
				Map<String, Object> deviceStatus = new HashMap<String, Object>();
				deviceStatus = JasonUtil.getEntryValue(dbo, "", deviceStatus);
				dboLst.add(deviceStatus);
			}
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			throw new MongoException(e.getMessage());
		} catch (MongoException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			
		}
		return dboLst;
	}
	
	public List<Object> readMongoChunksGrp(Mongo mongo, String collName, List<Object> dboLst) throws MongoException, Exception {
		try {
			DB db = mongo.getDB("config");
			DBCollection dbcollection = db.getCollectionFromString("chunks");
			DBObject dbo = dbcollection.group(new BasicDBObject("shard", true), new BasicDBObject("ns", collName), new BasicDBObject("nChunks", 0), "function (doc, out) {out.nChunks++;}");
			Iterator<String> keys = dbo.keySet().iterator();
			BasicDBObject getDbo;
			while(keys.hasNext()) {
				String key = keys.next();
				getDbo = (BasicDBObject) dbo.get(key);
				getDbo.put("collName", collName);
				dboLst.add(getDbo);
			}
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			throw new MongoException(e.getMessage());
		} catch (MongoException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			
		}
		return dboLst;
	}
	
	public Set<String> readMongoCollectionName(Mongo mongo, String dbName) throws MongoException, Exception {
		Set<String> collNameLst = null;
		try {
			DB db = mongo.getDB(dbName);
			collNameLst = db.getCollectionNames();
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			throw new MongoException(e.getMessage());
		} catch (MongoException e) {
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			
		}
		return collNameLst;
	}
	
	public CommandResult getCommandResult(Mongo mongo, Device device, String command, String databaseName) throws MongoException, Exception {
		CommandResult commandResult = null;
		try {
			DB db = mongo.getDB(databaseName);
			commandResult = db.command(command);
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			throw new MongoException(e.getMessage());
		} catch (MongoException e) {
			// alarm 정보 등록
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			
		}
		
		return commandResult;
	}
	
	public Map<String, Object> readMongoStatus(Mongo mongo, Device device, String command) throws MongoException, Exception {
		return readMongoStatus(mongo, device, command, "admin");
	}
	
	public String getAllServerStatus(Mongo mongo, Device device, String command, String databaseName) throws MongoException, Exception {
		String objtoString;
		try {
			DB db = mongo.getDB(databaseName);
			CommandResult commandResult = db.command(command);
			objtoString = commandResult.toString();
		} catch (DataAccessResourceFailureException e) {
			e.printStackTrace();
			throw new MongoException(e.getMessage());
		} catch (MongoException e) {
			// alarm 정보 등록
			e.printStackTrace();
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			
		}
		return objtoString;
	}
	
	public String getAllServerStatus(Mongo mongo, Device device) throws MongoException, Exception {
		return getAllServerStatus(mongo, device, "serverStatus", "admin");
	}
	
	
}
