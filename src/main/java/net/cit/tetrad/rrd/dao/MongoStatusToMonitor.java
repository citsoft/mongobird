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

import java.net.ConnectException;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.cit.tetrad.model.Device;

import com.mongodb.CommandResult;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public interface MongoStatusToMonitor {
	
	public Map<String, Object> readMongoStatus(Mongo mongo, Device device, String command, String databaseName) throws ConnectException, Exception;
	public CommandResult getCommandResult(Mongo mongo, Device device, String command, String databaseName) throws ConnectException, Exception;
	public Map<String, Object> readMongoStatus(Mongo mongo, Device device, String command) throws ConnectException, Exception;
	public String getAllServerStatus(Mongo mongo, Device device, String command, String databaseName) throws MongoException, Exception ;
	public String getAllServerStatus(Mongo mongo, Device device) throws MongoException, Exception ;
	public List<Object> readMongoShards(Mongo mongo, String collection, String databaseName) throws MongoException, Exception ;
	public List<Object> readMongoChunksGrp(Mongo mongo, String collName, List<Object> dboLst) throws MongoException, Exception ;
	public Set<String> readMongoCollectionName(Mongo mongo, String dbName) throws MongoException, Exception ;
}
