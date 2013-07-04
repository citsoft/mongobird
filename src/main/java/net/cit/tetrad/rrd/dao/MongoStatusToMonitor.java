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
