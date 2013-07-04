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
import net.cit.tetrad.model.Device;
import net.cit.tetrad.rrd.batch.DeviceInMemory;
import net.cit.tetrad.rrd.batch.MongoInMemory;
import net.cit.tetrad.rrd.batch.TetradRrdInitializer;
import net.cit.tetrad.rrd.utils.TetradRrdConfig;
import net.cit.tetrad.rrd.utils.TetradRrdDbPool;

import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestInstallTotalData {
//	  static final String FILE = "all";
//
//	  static final long START = Util.getTimestamp(2003, 4, 1);
//	  static final long END = Util.getTimestamp(2003, 5, 1);
//	  
//	  static void println(String msg) {
//		    //System.out.println(msg + " " + Util.getLapTime());
//		    System.out.println(msg);
//		  }
//
//	public void initaillTotalRrd() throws IOException {
//		int intervalTime = 10;
//	    	    
//		for (String key : ColumnContents.RRD_SET_TOTALFIELD) {
////			String rrdPath = Util.getRrd4jDemoPath(FILE + ".rrd");
//			String rrdPath = CommonUtils.getDefaultRrdPath() + "all.rrd";
//			
//			 RrdDef rrdDef = new RrdDef(rrdPath, START - 1, intervalTime);
//			 
//			rrdDef.addDatasource(key, DsType.GAUGE, intervalTime * 2, 0, Double.NaN);			
//			rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, 1, 7000);
//			println(rrdDef.dump());
//			
//			 RrdDb rrdDb = new RrdDb(rrdDef);
//			if (rrdDb.getRrdDef().equals(rrdDef)) {
//			      println("Checking RRD file structure... OK");
//			    }
//			    else {
//			      println("Invalid RRD file created. This is a serious bug, bailing out");
//			      return;
//			    }
//			
//			rrdDb.close();
////			break;
//		}
//	}
	
	public static void main(String[] args) {
		String[] configLocations = new String[]{"applicationContext_rrd.xml"};
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(configLocations);
		
		TetradRrdInitializer tetradInitial = (TetradRrdInitializer)context.getBean("tetradRrdInitializer");
		DeviceInMemory deviceInMemory = (DeviceInMemory)context.getBean("deviceInMemory");
		MongoInMemory mongoInMemory = (MongoInMemory)context.getBean("mongoInMemory");
		try {
			deviceInMemory.createDeviceGroup();
			mongoInMemory.createMongoGroup();
			String poolSize = TetradRrdConfig.getTetradRrdConfig("default_rrdPoolSize");
			TetradRrdDbPool.setPoolCount(Integer.parseInt(poolSize));
			
//			tetradInitial.installTotalRrdDb();
			tetradInitial.install();
//			tetradInitial.input();
//			TestInstallTotalData a = new TestInstallTotalData();
//			a.initaillTotalRrd();
		} catch (Exception e) {
			System.out.println("install error");
		}
	}
}
