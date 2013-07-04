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

import static net.cit.tetrad.common.ColumnConstent.COLL_DASHBOARD;
import static net.cit.tetrad.common.ColumnConstent.DEVICECODE;
import net.cit.monad.Operations;

import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;

public class TestSpringMongo {

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] configLocations = new String[]{"applicationContext_rrd.xml"};
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocations);

		Operations operations = (Operations)context.getBean("operations");
		
		Query query = new Query(Criteria.where("key").is("1"));
		
		Update update = new Update();
		update.set("id", "A");
		
		WriteResult wr = operations.updateMulti(query, update, "test", true);
		System.out.println(wr);
	}

}
