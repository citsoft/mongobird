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
package net.cit.tetrad.data.convert;

import static net.cit.tetrad.common.ColumnConstent.COLL_DASHBOARD;
import static net.cit.tetrad.common.ColumnConstent.COLL_DBSTATUS;
import static net.cit.tetrad.common.ColumnConstent.COLL_SERVERSTATUS;
import static net.cit.tetrad.common.ColumnConstent.RRD_STEP_30MINUTE;
import static net.cit.tetrad.common.ColumnConstent.RRD_STEP_5MINUTE;
import static net.cit.tetrad.common.ColumnConstent.RRD_STEP_HOUR;
import static net.cit.tetrad.common.ColumnConstent.RRD_STEP_MINUTE;
import static net.cit.tetrad.common.ColumnConstent.RRD_STEP_SECOND;

import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.List;
import java.util.Map;

import net.cit.tetrad.common.DateUtil;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.rrd.bean.DbStatus;
import net.cit.tetrad.rrd.bean.ServerStatus;
import net.cit.tetrad.rrd.rule.DbStatusRule;
import net.cit.tetrad.rrd.rule.ServerStatusRule;
import net.cit.tetrad.rrd.rule.StatusDatasourceName;
import net.cit.tetrad.rrd.utils.RrdUtil;
import net.cit.tetrad.rrd.utils.StringUtil;
import net.cit.tetrad.rrd.utils.TetradRrdDbPool;
import net.cit.tetrad.utility.CommonUtils;

import org.apache.log4j.Logger;
import org.rrd4j.ConsolFun;
import org.rrd4j.DsType;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDef;
import org.rrd4j.core.Sample;
import org.rrd4j.core.Util;
import org.springframework.data.mongodb.core.MongoOperations;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.SimpleMongoDbFactory;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.Mongo;

public class DataConvertThread extends Thread{
	private Logger logger = Logger.getLogger("convert.rrd");
	
	public static int CONVERT_STATUS = 0;
	public static long CONVERT_DBDATA_TOT = 0;
	public static long CONVERT_DBDATA_CNT = 0;
	public static long CONVERT_SERVERDATA_TOT = 0;
	public static long CONVERT_SERVERDATA_CNT = 0;
	
	private DBDataSource mongodatasource;
	private Date START_DATE;
	
	List<StatusDatasourceName> tetradServerStatusDatasourceNames = new ServerStatusRule().serverStatusDatasourceNameXMLToObject();
	List<StatusDatasourceName> tetradDbStatusDatasourceNames = new DbStatusRule().dbStatusDatasourceNameXMLToObject();
	
	public void setMongodatasource(DBDataSource mongodatasource) {
		this.mongodatasource = mongodatasource;
	}
	
	public DataConvertThread (String host, String port, String database, String startDate) {		
		Mongo mongo;
		try {
			mongo = new Mongo(host, Integer.parseInt(port));
			this.mongodatasource = new DBDataSource(mongo, database);
			this.START_DATE = DateUtil.dateformat(startDate, "yyyyMMdd");
		} catch (Exception e) {			
			logger.error(e, e);
		} 
	}
	
	private void printStatus() {
		logger.info("status : " + CONVERT_STATUS);
	}
	
	public void run() {		
		try {
			printStatus();
			initial();
			CONVERT_STATUS = 2;
			printStatus();
			input();
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e, e);
			CONVERT_STATUS = -1;
		}
		CONVERT_STATUS = 3;
		printStatus();
		logger.info("done!!");
	}
	
	public void initial() throws Exception {
		
		List<Device> deviceList = mongodatasource.getDeviceList();
		
		for (Device device : deviceList) {
			createTetradRrdDb(device, tetradServerStatusDatasourceNames);
			List<DbStatus> dbList = mongodatasource.getDbList(device.getIdx());
			
			for (DbStatus dbstatus : dbList) {
				createTetradRrdDb(device, dbstatus.getDb(), tetradDbStatusDatasourceNames);
			}
		}
	}

	private void createTetradRrdDb(Device device, List<StatusDatasourceName> dsNameList) throws Exception {
		createTetradRrdDb(device, null, dsNameList);
	}
	
	private void createTetradRrdDb(Device device, String dbname,  List<StatusDatasourceName> dsNameList) throws Exception {
		String rrdPath = null;
		
		try {						
			String dsName;
			for (StatusDatasourceName dbstatusInfo : dsNameList) {
				dsName = dbstatusInfo.getDsName();
				rrdPath = CommonUtils.makeRrdDbPath(device, dbname, dsName);
				
				// 생성할 row 갯수 취득
				int intervalTime = RrdUtil.readLogGenerationInterval();

				RrdDef rrdDef = new RrdDef(rrdPath, Util.getTimestamp(START_DATE), intervalTime);

				DsType dsType = DsType.valueOf(dbstatusInfo.getDsType());
				rrdDef.addDatasource(StringUtil.rightSubstring(dsName, 20), dsType, intervalTime * 2, 0, Double.NaN);
				rrdDef.addArchive(ConsolFun.LAST, 0.5, RRD_STEP_SECOND, RrdUtil.calculateCreateRowCnt(360));
				rrdDef.addArchive(ConsolFun.LAST, 0.5, RRD_STEP_MINUTE, RrdUtil.calculateCreateRowCnt(60));
				rrdDef.addArchive(ConsolFun.LAST, 0.5, RRD_STEP_5MINUTE, RrdUtil.calculateCreateRowCnt(12));
				rrdDef.addArchive(ConsolFun.LAST, 0.5, RRD_STEP_30MINUTE, RrdUtil.calculateCreateRowCnt(2));
				rrdDef.addArchive(ConsolFun.LAST, 0.5, RRD_STEP_HOUR, RrdUtil.calculateCreateRowCnt(1));
				
				rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, RRD_STEP_SECOND, RrdUtil.calculateCreateRowCnt(360));
				rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, RRD_STEP_MINUTE, RrdUtil.calculateCreateRowCnt(60));
				rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, RRD_STEP_5MINUTE, RrdUtil.calculateCreateRowCnt(12));
				rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, RRD_STEP_30MINUTE, RrdUtil.calculateCreateRowCnt(2));
				rrdDef.addArchive(ConsolFun.AVERAGE, 0.5, RRD_STEP_HOUR, RrdUtil.calculateCreateRowCnt(1));
				
				rrdDef.addArchive(ConsolFun.TOTAL, 0.5, RRD_STEP_SECOND, RrdUtil.calculateCreateRowCnt(360));
				rrdDef.addArchive(ConsolFun.TOTAL, 0.5, RRD_STEP_MINUTE, RrdUtil.calculateCreateRowCnt(60));
				rrdDef.addArchive(ConsolFun.TOTAL, 0.5, RRD_STEP_5MINUTE, RrdUtil.calculateCreateRowCnt(12));
				rrdDef.addArchive(ConsolFun.TOTAL, 0.5, RRD_STEP_30MINUTE, RrdUtil.calculateCreateRowCnt(2));
				rrdDef.addArchive(ConsolFun.TOTAL, 0.5, RRD_STEP_HOUR, RrdUtil.calculateCreateRowCnt(1));

				// RrdDb를 생성한다.
				RrdDb rrdDb = new RrdDb(rrdDef);
				rrdDb.close();
			}
		} catch (Exception e) {
			throw e;
		}
	}	
	
	public void input() throws IOException, InterruptedException, ParseException {
		convertDbInfo();
		convertServerInfo();
	}
	
	private void convertDbInfo() throws InterruptedException, ParseException {
		CONVERT_DBDATA_TOT = mongodatasource.getTotalData(COLL_DBSTATUS);
		int pagePerSize = 5000;
		int row = (int) (CONVERT_DBDATA_TOT / pagePerSize) + 1;
		for (int i=0; i<row; i++) {
			List<Map> statusGroup = mongodatasource.getData(COLL_DBSTATUS, i, pagePerSize);

			for (Map statusInfo  : statusGroup) {			
				int deviceCode = (Integer) statusInfo.get("deviceCode");
				String dbname = (String) statusInfo.get("db");
				String regtime = statusInfo.get("regtime").toString();
				
				Date date = DateUtil.dateformat(regtime, "yyyyMMddHHmmssSSS");

				for (StatusDatasourceName dbstatusInfo : tetradDbStatusDatasourceNames) {
					String dsName = dbstatusInfo.getDsName();
					RrdDb rrdDb;
					String rrdPath;
					try {
						if (statusInfo.get(dsName) == null) continue; 
						
						rrdPath = CommonUtils.getRrdDbPath(deviceCode, dbname, dsName);
						if (Util.fileExists(rrdPath)) { 						
							rrdDb = TetradRrdDbPool.getRrdDb(rrdPath);
							Sample sample = rrdDb.createSample();			
							double value = (Double) statusInfo.get(dsName);
							sample.setTime(Util.getTimestamp(date));
							sample.setValue(StringUtil.rightSubstring(dsName, 20), value);
							sample.update();		
						}
					} catch (Exception e) {
						logger.error(e, e);
					}
				}
				CONVERT_DBDATA_CNT++;
			}
			Thread.sleep(1);
			logger.info("db status : " + CONVERT_DBDATA_CNT);
//			System.out.println("db status : " + CONVERT_DBDATA_CNT);
		}		
	}

	private void convertServerInfo() throws InterruptedException, ParseException {
		CONVERT_SERVERDATA_TOT = mongodatasource.getTotalData(COLL_SERVERSTATUS);
		int pagePerSize = 5000;
		int row = (int) (CONVERT_SERVERDATA_TOT / pagePerSize) + 1;
		for (int i=0; i<row; i++) {
			List<Map> statusGroup = mongodatasource.getData(COLL_SERVERSTATUS, i, pagePerSize);

			for (Map statusInfo  : statusGroup) {			
				int deviceCode = (Integer) statusInfo.get("deviceCode");
				String regtime = statusInfo.get("regtime").toString();
				Date date = DateUtil.dateformat(regtime, "yyyyMMddHHmmssSSS");

				for (StatusDatasourceName dbstatusInfo : tetradServerStatusDatasourceNames) {
					String dsName = dbstatusInfo.getDsName();
					RrdDb rrdDb;
					String rrdPath;
					try {
						if (statusInfo.get(dsName) == null) continue; 

						rrdPath = CommonUtils.getRrdDbPath(deviceCode, dsName);
						if (Util.fileExists(rrdPath)) { 						
							rrdDb = TetradRrdDbPool.getRrdDb(rrdPath);

							Sample sample = rrdDb.createSample();					
							double value = (Double) statusInfo.get(dsName);
							sample.setTime(Util.getTimestamp(date));
							sample.setValue(StringUtil.rightSubstring(dsName, 20), value);
							sample.update();		
						}
					} catch (Exception e) {
						logger.error(e, e);
					}
				}			
				CONVERT_SERVERDATA_CNT++;
			}
			Thread.sleep(1);
			logger.info("server status : " + CONVERT_SERVERDATA_CNT);
//			System.out.println("server status : " + CONVERT_SERVERDATA_CNT);
		}  // end of for
	}
	
	static class DBDataSource {
		private MongoOperations operations;
		
		public  DBDataSource(Mongo mongo, String databaseName) {
			this.operations = new MongoTemplate(new SimpleMongoDbFactory(mongo, databaseName));
		}	
		
		public List<Device> getDeviceList(){			
			return operations.find(new Query(), Device.class);
		}
		
		public List<DbStatus> getDbList(int deviceCode){
			ServerStatus serverStatus = operations.findOne(new Query(Criteria.where("deviceCode").is(deviceCode)), ServerStatus.class, COLL_DASHBOARD);
			return serverStatus.getDbInfos();
		}
				
		public int getTotalData(String colname) {
			return (int) operations.count(new Query(), colname);
		}

		public List<Map> getData(String colname, int row, int pagePersize) {
			Query query = new Query();
			query.limit(pagePersize);
			query.skip(getSkipSize(row, pagePersize));
			query.sort().on("regtime", Order.ASCENDING);
			
			List<Map> dbstatus  = (List<Map>)operations.find(query, Map.class, colname);		

			return dbstatus;
		}
		
		private int getSkipSize(int currentPageNum, int pageperSize) {
			if (currentPageNum == 0) {
				return 0;
			} else {
				int size = currentPageNum * pageperSize;
				return size;
			}
		}
	}
	
	/**
	 * @param args
	 */
//	public static void main(String[] args) {
//		DataConvertThread thread = new DataConvertThread("localhost", "27017", "tetrad", "20110701");
//		DataConvertThread.CONVERT_STATUS = 1;
//		thread.start();
//	}

}
