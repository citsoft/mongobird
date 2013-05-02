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
package net.cit.tetrad.monad;

import java.util.Collection;
import java.util.List;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.CommandResult;
import com.mongodb.DBObject;


public interface MonadService {

	public void updateMulti(Query query, Update update, String collectionName);

	public long getCount(Query query, Class<?> className);

	public List<Object> getList(Query query, Class<?> className);

	public List<Object> getListWithStrCollName(Query query,Class<?> className, String collectionName);
	
	public void delete(Query query, Class<?> className);
	
	public void delete(Query query, String className) ;

	public void listAdd(List<Object> userList, Class<?> className);

	public Object getFind(Query query, Class<?> className);

	public void add(Object obj, Class<?> className);

	public void update(Query query, Update update, Class<?> className);

	public Object getFindOne(Query query, Class<?> className, String collectionName);

	public long getCount(Query query, String collectionName);
	
	CommandResult command(DBObject cmd);
	void insert(Collection<? extends Object> batchToSave, String collectionName);
	void dropCollection(String collection);
}
