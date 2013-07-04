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
package tetrad.rrd;

import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;

public class TestInsert {

	/**
	 * @param args
	 * @throws MongoException 
	 * @throws UnknownHostException 
	 */
	public static void main(String[] args) throws UnknownHostException, MongoException {
		Mongo mongo = new Mongo("localhost");
		DB db = mongo.getDB("tetrad");
		
		List<DBObject> arrDoc = new ArrayList<DBObject>();
		for (int i=0; i<10000; i++) {
			BasicDBObject doc = new BasicDBObject();
			doc.put("kkk", i);
			doc.put("rkdjfd", i);
			doc.put("ndnkf", i);
			doc.put("text", "song song happy song!");
			arrDoc.add(doc);
		}
		
		DBCollection  coll = db.getCollection("test");
		coll.insert(arrDoc);
		System.out.println("done");
	}

}
