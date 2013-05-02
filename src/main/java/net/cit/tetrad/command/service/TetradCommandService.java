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
package net.cit.tetrad.command.service;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Map;
import com.mongodb.MongoException;

public interface TetradCommandService {

	public Map<String, Object> insertCommand(int deviceIdx, String command) throws UnknownHostException, MongoException;
	public String allServerStatus(int deviceIdx) throws MongoException;
	public List<Object> collectionCommand(int deviceIdx, String collection) throws MongoException;
	public List<Object> chunksGrpCommand(int deviceIdx, String collName, List<Object> dboLst) throws MongoException;
}
