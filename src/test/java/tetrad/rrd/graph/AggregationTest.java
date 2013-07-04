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
package tetrad.rrd.graph;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.context.support.ClassPathXmlApplicationContext;

import net.cit.monad.Operations;
import net.cit.tetrad.model.CommonDto;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;

public class AggregationTest {
	private Operations operations;
	
	public void setOperations(Operations operations) {
		this.operations = operations;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		String[] configLocations = new String[]{"applicationContext_test.xml"};
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocations);

		AggregationTest aggregationTest = (AggregationTest)context.getBean("aggregationTest");
		
		CommonDto dto = new CommonDto();
		dto.setDeviceCode(1);
		dto.setDsname("connections_current");
		dto.setCount(900);
		dto.setCollname("serverStatus");
		Object object = aggregationTest.execute(dto);
		
		if (object != null) {
			List result = (List) object;
			for (Object obj : result) {
				System.out.println(obj);
			}
		}
	}
	
	public Object execute(CommonDto dto) {
		List<BasicDBObject> pipeline = new ArrayList<BasicDBObject>();
		
		DBObject sort = new BasicDBObject("_id", -1);		
		DBObject match = new BasicDBObject("deviceCode", dto.getDeviceCode());
		
		//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<project 1
		DBObject project1 = new BasicDBObject("dsname", "$"+ dto.getDsname());
		DBObject hour = new BasicDBObject("$substr", Arrays.asList("$regtime", 0, 10));
		DBObject minute = new BasicDBObject("$substr", Arrays.asList("$regtime", 10, 2));
		project1.put("regtime-hour", hour);
		project1.put("regtime", "$NumberLong($regtime)");
		project1.put("regtime-min", minute);
		
		DBObject project2 = new BasicDBObject("dsname", "$dsname");
		project2.put("regtime", "$regtime");
		project2.put("regtime-hour", "$regtime-hour");
		project2.put("regtime-min", "$regtime-min");
//		project2.put("regtime-multi", new BasicDBObject("$multiply", Arrays.asList("$dsname", 2)));
//		project2.put("regtime-multi", new BasicDBObject("$multiply", Arrays.asList("NumberLong($regtime-min)", 1)));
//		project2.put("regtime-div", new BasicDBObject("$divide", Arrays.asList("$regtime-multi", 5)));
				
		pipeline.add(new BasicDBObject("$sort", sort));
		pipeline.add(new BasicDBObject("$limit", dto.getCount()));
		pipeline.add(new BasicDBObject("$match", match));
		pipeline.add(new BasicDBObject("$project", project1));
		pipeline.add(new BasicDBObject("$project", project2));
		//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>project 1
		
		//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<group 1
		DBObject group1 = new BasicDBObject("_id", "$regtime-hour");		
		group1.put("h_count", new BasicDBObject("$sum", 1));
//		group1.put("h_dsname", "$dsname");
//		group1.put("h-regtime-min", "$regtime-miin");
		//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>group 1
		
		//<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<group 2
//		DBObject group2 = new BasicDBObject("_id", "$h-regtime-mod");		
//		group2.put("h_count", new BasicDBObject("$sum", 1));
//		group2.put("h_dsname", "$dsname");
//		group2.put("h-regtime-min", "$regtime-miin");
//		group2.put("sum", new BasicDBObject("$sum", "$h_dsname"));
//		group2.put("avg", new BasicDBObject("$avg", "$h_dsname"));
//		group2.put("first", new BasicDBObject("$first", "$h_dsname"));
//		group2.put("last", new BasicDBObject("$last", "$h_dsname"));
		//>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>group 2
		
		DBObject finalSort = new BasicDBObject("_id", 1);
		
//		DBObject resultProject = new BasicDBObject();
//		resultProject.put("avg", "$colavg");
//		resultProject.put("point", "$colfirst");
//		resultProject.put("diff", new BasicDBObject("$subtract", Arrays.asList("$collast", "$colfirst")));
		
//		pipeline.add(new BasicDBObject("$group", group1));
//		pipeline.add(new BasicDBObject("$group", group2));
		pipeline.add(new BasicDBObject("$sort", finalSort));
//		pipeline.add(new BasicDBObject("$project", resultProject));
		
		DBObject cmdBody = new BasicDBObject("aggregate", dto.getCollname()); 
		cmdBody.put("pipeline", pipeline);
		CommandResult result = operations.executeCommand(cmdBody);
		System.out.println(result);
		return result.get("result");
	}

}
