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
package net.cit.tetrad.rrd.service;

import static net.cit.tetrad.common.ColumnConstent.DBSTATUS_DIFF_DATASIZE;
import static net.cit.tetrad.common.ColumnConstent.DBSTATUS_DIFF_INDEXSIZE;
import static net.cit.tetrad.common.ColumnConstent.DEVICECODE;
import static net.cit.tetrad.common.ColumnConstent.DEVICE_GROUPCODE;
import static net.cit.tetrad.common.ColumnConstent.PROCESS_MONGOS;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DBAVGOBJSIZE;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DBCOUNT;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DBDATASIZE;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DBFILESIZE;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DBINDEXES;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DBINDEXSIZE;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DBNSSIZEMB;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DBNUMEXTENTS;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DBOBJECTS;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DBSTORAGESIZE;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DBSUMLOCKSLOCKED_R;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DBSUMLOCKSLOCKED_W;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DIFF_DBDATASIZE;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DIFF_DBINDEXSIZE;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_ID;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_IP;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_PORT;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_REGTIME;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_TRUE;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_TYPE;
import static net.cit.tetrad.common.ColumnConstent.TOTAL_DIFF_TOTALDBDATASIZE;
import static net.cit.tetrad.common.ColumnConstent.TOTAL_DIFF_TOTALDBINDEXSIZE;
import static net.cit.tetrad.common.ColumnConstent.TOTAL_MAXIMUMPAGEFAULTS;
import static net.cit.tetrad.common.ColumnConstent.TOTAL_TOTALDBDATASIZE;
import static net.cit.tetrad.common.ColumnConstent.TOTAL_TOTALDBLOCKSTIMELOCKEDMICROS_R_SUM;
import static net.cit.tetrad.common.ColumnConstent.TOTAL_TOTALDBLOCKSTIMELOCKEDMICROS_W_SUM;
import static net.cit.tetrad.common.ColumnConstent.TOTAL_TOTALGLOBALLOCKTIME;
import static net.cit.tetrad.common.ColumnConstent.TOTAL_TOTALSYSTEMLOCKSTIMELOCKEDMICROS_R_SUM;
import static net.cit.tetrad.common.ColumnConstent.TOTAL_TOTALSYSTEMLOCKSTIMELOCKEDMICROS_W_SUM;
import static net.cit.tetrad.common.ColumnConstent.TOTAL_TTOTALDBINDEXSIZE;
import static net.cit.tetrad.common.PropertiesNames.SMS_SCRIPT_PATH;

import java.awt.image.BufferedImage;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import net.cit.tetrad.common.ColumnConstent;
import net.cit.tetrad.common.Config;
import net.cit.tetrad.common.DateUtil;
import net.cit.tetrad.common.Utility;
import net.cit.tetrad.model.Alarm;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.model.User;
import net.cit.tetrad.resource.MailResource;
import net.cit.tetrad.rrd.batch.MongoInMemory;
import net.cit.tetrad.rrd.batch.ReInitializeMongoInMemory;
import net.cit.tetrad.rrd.bean.DbStatus;
import net.cit.tetrad.rrd.bean.GraphDefInfo;
import net.cit.tetrad.rrd.bean.ServerStatus;
import net.cit.tetrad.rrd.dao.CriticalOperation;
import net.cit.tetrad.rrd.dao.DataAccessObjectForMongo;
import net.cit.tetrad.rrd.dao.MongoStatusToMonitor;
import net.cit.tetrad.rrd.dao.TetradRrdGraphDef;
import net.cit.tetrad.rrd.rule.DbStatusRule;
import net.cit.tetrad.rrd.rule.ServerStatusRule;
import net.cit.tetrad.rrd.rule.StatusDatasourceName;
import net.cit.tetrad.rrd.rule.TotalStatusRule;
import net.cit.tetrad.rrd.utils.JasonUtil;
import net.cit.tetrad.rrd.utils.StringUtil;
import net.cit.tetrad.rrd.utils.TetradRrdConfig;
import net.cit.tetrad.rrd.utils.TimestampUtil;
import net.cit.tetrad.utility.CommonUtils;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.log4j.Logger;
import org.rrd4j.ConsolFun;
import org.rrd4j.core.FetchData;
import org.rrd4j.core.FetchRequest;
import org.rrd4j.core.RrdDb;
import org.rrd4j.core.Sample;
import org.rrd4j.graph.RrdGraph;
import org.rrd4j.graph.RrdGraphDef;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.CommandResult;
import com.mongodb.DBObject;
import com.mongodb.Mongo;
import com.mongodb.MongoException;
import com.mongodb.WriteResult;

public class TetradRrdDbServiceImpl implements TetradRrdDbService {
	
	private Logger logger = Logger.getLogger("process.rrd");
	
	private TetradRrdGraphDef tetradRrdGraphDef;
	private MongoStatusToMonitor mongoStatusToMonitor;
	private DataAccessObjectForMongo daoForMongo;
	private CriticalOperation criticalOperation;
	private MailResource mailResource; 
	
	private static String exportXmlYn = "0";
	private static String exportXmlFilePath = null;
	
	private final String SERVER_STATUS_COMMAND = "serverStatus";
	private final String DB_STATUS_COMMAND = "dbStats";
			
	public TetradRrdDbServiceImpl() {
		exportXmlYn = TetradRrdConfig.getTetradRrdConfig("exportXmlYn");
		exportXmlFilePath = TetradRrdConfig.getTetradRrdConfig("xml_path");
	}

	public void setCriticalOperation(CriticalOperation criticalOperation) {
		this.criticalOperation = criticalOperation;
	}

	public void setTetradRrdGraphDef(TetradRrdGraphDef tetradRrdGraphDef) {
		this.tetradRrdGraphDef = tetradRrdGraphDef;
	}
	
	public void setMongoStatusToMonitor(MongoStatusToMonitor mongoStatusToMonitor) {
		this.mongoStatusToMonitor = mongoStatusToMonitor;
	}
	
	public void setDataAccessObjectForMongo(DataAccessObjectForMongo daoForMongo) {
		this.daoForMongo = daoForMongo;
	}
	
	public void setMailResource(MailResource mailResource) {
		this.mailResource = mailResource;
	}

	/**
	 * 데이타베이스별 TetradRrdDb 생성
	 * @param device
	 * @param databaseName
	 * @return
	 */
	public void createTetradRrdDb(Device device, String databaseName) throws Exception {
		logger.info("createTetradRrdDef");
		String rrdPath = null;
		try {			
			List<StatusDatasourceName> tetradDbStatusDatasourceNames = new DbStatusRule().dbStatusDatasourceNameXMLToObject();

			String dsName;
			for (StatusDatasourceName dbstatusInfo : tetradDbStatusDatasourceNames) {
				dsName = dbstatusInfo.getDsName();
				rrdPath = CommonUtils.makeRrdDbPath(device, databaseName, dsName);
				
				InitialRrdService rrdSvc = new InitialRrdService(rrdPath);
				rrdSvc.initialRrd(dsName, dbstatusInfo.getDsType());				
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(rrdPath + " Tetrad Rrd Db not created!!");
			throw e;
		}
	}
	
	/**
	 * 데몬별 TetradRrdDb 생성
	 * @param device
	 * @return
	 */
	public void createTetradRrdDb(Device device) throws Exception {
		logger.info("createTetradRrdDef");
		String rrdPath = null;
		
		try {			
			List<StatusDatasourceName> tetradServerStatusDatasourceNames = new ServerStatusRule().serverStatusDatasourceNameXMLToObject();
			String dsName;
			for (StatusDatasourceName dbstatusInfo : tetradServerStatusDatasourceNames) {
				dsName = dbstatusInfo.getDsName();
				rrdPath = CommonUtils.makeRrdDbPath(device, dsName);

				InitialRrdService rrdSvc = new InitialRrdService(rrdPath);
				rrdSvc.initialRrd(dsName, dbstatusInfo.getDsType());					
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(rrdPath + " Tetrad Rrd Db not created!!");
			throw e;
		}
	}	
	
	/**
	 * 특정 기간동안의 장비별 TetradRrdDb 조회
	 * @param device
	 * @param filters
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public List<HashMap<String, Object>> fetchTetradRrdDb(Device device, String[] filters, long startTime, long endTime, String databaseName) {
		List<HashMap<String, Object>> result = new ArrayList<HashMap<String, Object>>();
		RrdDb rrdDb = null;
		FetchData fetchData = null;
		String rrdPath = null;
		String detailRrdPath = null;
		
		try {
			for (int i = 0 ; i < filters.length ; i++) {
				String dsName = CommonUtils.getDsnameInRrdPath(filters[i]);
				// rrdPath 취득
				rrdPath = CommonUtils.getRrdDbPath(device, databaseName, filters[i]) + ".rrd";
				
				// RrdDb 취득
				rrdDb = new RrdDb(rrdPath);
				
				FetchRequest request = rrdDb.createFetchRequest(ConsolFun.AVERAGE, startTime, endTime, 10000);
				request.setFilter(StringUtil.rightSubstring(dsName, 20));
				
				// RrdDb로부터 데이터를 fetch한다.
				fetchData = request.fetchData();
				
				int columnCount = fetchData.getColumnCount();
				int rowCount = fetchData.getRowCount();
				long[] timestamps = fetchData.getTimestamps();
				double[][] values = fetchData.getValues();
				
				for (int row = 0; row <rowCount; row++) {
					if (i != 0) {						
						for (int dsIndex = 0; dsIndex < columnCount; dsIndex++) {
							result.get(row).put(dsName, values[dsIndex][row]);
						}						
					} else {
						HashMap<String, Object> fetchRow = new HashMap<String, Object>();
						fetchRow.put("uptime", TimestampUtil.convTimestampToString(timestamps[row], "yyyy-MM-dd HH:mm:ss"));
						
						for (int dsIndex = 0; dsIndex < columnCount; dsIndex++) {
							fetchRow.put(dsName, values[dsIndex][row]);
						}
						result.add(row, fetchRow);
					}
				}
				
				exportXmlIfNecessary(fetchData, detailRrdPath);
				rrdDb.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rrdDb != null) 
					rrdDb.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		return result;
	}
	
	/**
	 * 데이터베이스별 TetradRrdDb 조회
	 * @param device
	 * @param databaseName
	 * @return
	 */
	public FetchData fetchTetradRrdDb(String rrdPath) {
		FetchData fetchData = null;
		RrdDb rrdDb = null;
		
		try {
			// RrdDb 취득
			rrdDb = new RrdDb(rrdPath);
			
			// rrdDef에 설정한 starttime을 취득한다.
			long startTime = TimestampUtil.readTimestamp("startTime");
			long endTime = TimestampUtil.readTimestamp();																// endTime이 현재 이후의 시각일 경우, 기존 데이터가 overwrite될 수 있음
			
			FetchRequest request = rrdDb.createFetchRequest(ConsolFun.AVERAGE, startTime, endTime);
			
			// RrdDb로부터 데이터를 fetch한다.
			fetchData = request.fetchData();
			
			exportXmlIfNecessary(fetchData, rrdPath);
			rrdDb.close();
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			try {
				if (rrdDb != null) 
					rrdDb.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		
		return fetchData;
	}
	
	/**
	 * fetch한 데이터를 신규로 생성한 rrdDb에 복원한다.
	 * @param device
	 * @param databaseName
	 * @param fetchData
	 */
	public void restoreTetradRrdDb(String rrdPath, FetchData fetchData) throws Exception {
		RrdDb rrdDb = null;
		
		try {
			// RrdDb 취득
			rrdDb = new RrdDb(rrdPath);
			
			int columnCount = fetchData.getColumnCount();
			int rowCount = fetchData.getRowCount();
			long[] timestamps = fetchData.getTimestamps();
			String[] keys = fetchData.getDsNames();
			double[][] values = fetchData.getValues();
			Sample sample = null;
			for (int row = 0 ; row < rowCount ; row++) {
				try {
					long t = timestamps[row];
					sample = rrdDb.createSample(t);
					for (int dsIndex = 0 ; dsIndex < columnCount ; dsIndex++) {
						if (rrdDb.containsDs((String)keys[dsIndex])) { 
						sample.setValue(keys[dsIndex], values[dsIndex][row]);
						}
					}
					sample.update();
				} catch (IllegalArgumentException e) {
					// rrdDb의 lastupdate time 보다 이전의 데이터를 입력하는 경우, IllegalArgumentException 발생한다.
					// rrdDb의 lastupdate time 보다 이전의 데이터는 무시하고 입력하는 것이 맞는 것으로 보여, exception 처리 따로 하지 않았음.
					e.printStackTrace();
				}
			}
			rrdDb.close();
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} finally {
			try {
				if (rrdDb != null) 
					rrdDb.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}
	
	/**
	 * fetch한 데이터를 신규로 생성한 rrdDb에 복원한다.
	 * @param device
	 * @param fetchData
	 */
//	public void restoreTetradRrdDb(Device device, FetchData fetchData) throws Exception {
//		restoreTetradRrdDb(device, "", fetchData);
//	}	
	
	/**
	 * 데이터베이스 상태 정보를 rrdDb에  등록한다.
	 * @param device
	 * @param databaseName
	 */
	public Map<String, Object> insertTetradRrdDb(Mongo mongo, Device device, String databaseName, CommandResult serverResult) throws FileNotFoundException {		
		Map<String, Object> dbStatus = new HashMap<String, Object>();
		
		try {
			// 데이터베이스 상태 정보 취득
			dbStatus = mongoStatusToMonitor.readMongoStatus(mongo, device, DB_STATUS_COMMAND, databaseName);

			if(serverResult.get("process").equals(PROCESS_MONGOS)){
				double collCnt = 0;
				Set<String> collNameLst = mongoStatusToMonitor.readMongoCollectionName(mongo, databaseName);
				if(collNameLst != null && collNameLst.size() != 0)collCnt = collNameLst.size() + 1;
				dbStatus.put("collections", collCnt);
			}
			
			dbStatus.put(SERVERSTATUS_REGTIME, TimestampUtil.readCurrentTime());
			dbStatus.put(DBSTATUS_DIFF_DATASIZE, dbStatus.get("dataSize"));
			dbStatus.put(DBSTATUS_DIFF_INDEXSIZE, dbStatus.get("indexSize"));
			
			DBObject locks = (DBObject) serverResult.get("locks");			
			if (locks != null) {
				DBObject dbLocks = (DBObject) locks.get(databaseName);			
				JasonUtil.getEntryValue(dbLocks, "locks_", dbStatus);
			}
			DBObject recordStats = (DBObject) serverResult.get("recordStats");			
			if (recordStats != null) {
				DBObject dbRecordStats = (DBObject) recordStats.get(databaseName);			
				JasonUtil.getEntryValue(dbRecordStats, "recordStats_", dbStatus);
			}
			
			InitialRrdService rrdSvc = new InitialRrdService();
			rrdSvc.setDataValues(dbStatus);
			rrdSvc.executeDbStatus(device, databaseName);
			
		} catch (MongoException e) {
			e.printStackTrace();
			insertConnectionTimeoutError(device);
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			try {
				createTetradRrdDb(device, databaseName);
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		} catch (Exception e) {
			e.printStackTrace();
		} 		
		return dbStatus;
	}
	
	/**
	 * 서버 상태 정보를 rrdDb에 등록한다.
	 * @param device
	 */
	public void insertTetradRrdDb(Mongo mongo, Device device) throws FileNotFoundException {
		logger.info("=====================================");
		logger.info("start insrt rrd, Device Id : " + device.getIdx());
		logger.debug("ip : " + device.getIp() + ", port : " + device.getPort());
		Map<String, Object> dbStatusFromMongo = new HashMap<String, Object>();											// 각 데이터베이스의 status 정보 
		Map<String, Object> serverStatusFromMongo = new HashMap<String, Object>();				// 각 데몬의 status 정보
		List<DbStatus> dbInfos = new ArrayList<DbStatus>();
		
		double dbObjects = 0;
		double dbAvgObjSize = 0;
		double dbDataSize = 0;
		double dbStorageSize = 0;
		double dbNumExtents = 0;
		double dbCount = 0;
		double dbIndexes = 0;
		double dbIndexSize = 0;
		double dbFileSize = 0;
		double dbNsSizeMB = 0;
		double dbSumTimeLocked_r = 0;
		double dbSumTimeLocked_w = 0;
		
		try {
			CommandResult serverResult = mongoStatusToMonitor.getCommandResult(mongo, device, SERVER_STATUS_COMMAND, "admin");
			
			try {
				// 데몬의 데이터베이스 리스트 취득
				List<String> dbNames = mongo.getDatabaseNames();
				Pattern patt = Pattern.compile(Config.DBNAME_PATTERN);
				for (String databaseName : dbNames) {
					// 패턴에 맞는 DB만 RrdDB 및 Mongodb에 insert
					Matcher m = patt.matcher(databaseName);
					if (!m.matches()) {
						continue;
					}
					
					// 운영 관리할 데이터베이스에 대한 RrdDb 데이타 입력					
					dbStatusFromMongo = insertTetradRrdDb(mongo, device, databaseName, serverResult);
					DbStatus dbstats = new DbStatus();
					BeanUtils.populate(dbstats, dbStatusFromMongo);
					
					dbstats.setDb(databaseName);
					dbstats.setGroupCode(device.getGroupCode());
					dbstats.setDeviceCode(device.getIdx());
					dbstats.setType(device.getType());

					dbCount++;
					dbObjects += dbstats.getObjects();
					dbAvgObjSize += dbstats.getAvgObjSize();
					dbDataSize += dbstats.getDataSize();
					dbStorageSize += dbstats.getStorageSize();
					dbNumExtents += dbstats.getNumExtents();
					dbIndexes += dbstats.getIndexes();
					dbIndexSize += dbstats.getIndexSize();
					dbFileSize += dbstats.getFileSize();
					dbNsSizeMB += dbstats.getNsSizeMB();
					dbSumTimeLocked_r += dbstats.getLocks_timeLockedMicros_r();
					dbSumTimeLocked_w += dbstats.getLocks_timeLockedMicros_w();
									
					daoForMongo.insertDbStatusInfo(dbstats);
					dbInfos.add(dbstats);
				}
			} catch (MongoException e) {
				e.printStackTrace();
				logger.error(e, e);
				ReInitializeMongoInMemory.reInit(device, e.toString());
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e, e);
			} 

			try {
				// 데몬 상태 정보 취득
				DBObject locks = (DBObject) serverResult.get("locks");
				
				serverResult.remove("locks");
				JasonUtil.getEntryValue(serverResult, "", serverStatusFromMongo);
				
				if (locks != null) {
					DBObject systemLock = (DBObject) locks.get(".");
					JasonUtil.getEntryValue(systemLock, "locks_", serverStatusFromMongo);
				}
				
				// 데이타베이스 상태 정보로부터 취득한 정보와 데몬 기본 정보를 추가해준다.
				int groupCode = device.getGroupCode();
				String uid = device.getUid();
				String type = device.getType();
				String key = groupCode + "_" + uid + "_" + type;
				String regtime = TimestampUtil.readCurrentTime();
				serverStatusFromMongo.put(SERVERSTATUS_ID, key);
				serverStatusFromMongo.put(SERVERSTATUS_REGTIME, regtime);
				serverStatusFromMongo.put(DEVICE_GROUPCODE, device.getGroupCode());
				serverStatusFromMongo.put(DEVICECODE, device.getIdx());
				serverStatusFromMongo.put(SERVERSTATUS_TYPE, device.getType());
				serverStatusFromMongo.put(SERVERSTATUS_IP, device.getIp());
				serverStatusFromMongo.put(SERVERSTATUS_PORT, device.getPort());
				serverStatusFromMongo.put(SERVERSTATUS_DBOBJECTS, dbObjects);
				serverStatusFromMongo.put(SERVERSTATUS_DBAVGOBJSIZE, dbAvgObjSize);
				serverStatusFromMongo.put(SERVERSTATUS_DBDATASIZE, dbDataSize);
				serverStatusFromMongo.put(SERVERSTATUS_DBSTORAGESIZE, dbStorageSize);
				serverStatusFromMongo.put(SERVERSTATUS_DBNUMEXTENTS, dbNumExtents);
				serverStatusFromMongo.put(SERVERSTATUS_DBCOUNT, dbCount);
				serverStatusFromMongo.put(SERVERSTATUS_DBINDEXES, dbIndexes);
				serverStatusFromMongo.put(SERVERSTATUS_DBINDEXSIZE, dbIndexSize);
				serverStatusFromMongo.put(SERVERSTATUS_DBFILESIZE, dbFileSize);
				serverStatusFromMongo.put(SERVERSTATUS_DBNSSIZEMB, dbNsSizeMB);
				serverStatusFromMongo.put(SERVERSTATUS_DIFF_DBDATASIZE, dbDataSize);
				serverStatusFromMongo.put(SERVERSTATUS_DIFF_DBINDEXSIZE, dbIndexSize);				
				serverStatusFromMongo.put(SERVERSTATUS_DBSUMLOCKSLOCKED_R, dbSumTimeLocked_r);
				serverStatusFromMongo.put(SERVERSTATUS_DBSUMLOCKSLOCKED_W, dbSumTimeLocked_w);
				
				InitialRrdService rrdSvc = new InitialRrdService();
				rrdSvc.setDataValues(serverStatusFromMongo);
				rrdSvc.executeServerStatus(device);
						
				daoForMongo.setGloballLockPageFaults(serverStatusFromMongo);
				daoForMongo.setOpcounter(serverStatusFromMongo);
				
				// main dashboard 데몬별 현황 데이터 입력
				ServerStatus serverStatusInfo = new ServerStatus();
				BeanUtils.populate(serverStatusInfo, serverStatusFromMongo);				
				
				// main dashboard 데몬 상태 데이터 입력
				serverStatusInfo.setDbInfos(dbInfos);
				serverStatusInfo.setError(1);
				daoForMongo.insertServerStatusInfo(serverStatusInfo);
				
				// 서버 상세 정보 데이터 입력
				String detailKey = key + "_" + regtime;
				serverStatusInfo.setId(detailKey);
				daoForMongo.insertDetailServerStatusInfo(serverStatusInfo);
				
				// main dashboard 경고 / 위험 데이터 입력				
				criticalOperation.settingAlarm(device, serverStatusFromMongo);
			} catch (Exception e) {
				e.printStackTrace();
				logger.error(e, e);
			}
			exportXmlIfNecessary(device);
		} catch (MongoException e) {
			e.printStackTrace();
			logger.error(e, e);
			insertConnectionTimeoutError(device);		
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			logger.error(e, e);
			throw e;			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e, e);
			if (mongo == null) {
				insertConnectionTimeoutError(device);
				throw new MongoException("Mongo Object is null");
			}
		}		
		logger.info("end insrt rrd");
	}
	
	/**
	 * 장비별 TetradRrdDb 조회
	 * @param device
	 * @param filters
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String graphTetradRrdDb(GraphDefInfo graphDefInfo) {
		return graphTetradRrdDb(graphDefInfo, "", "");
	}
	
	/**
	 * 데이터베이스 상태 정보 그래프 생성
	 * @param device
	 * @param filters
	 * @param startTime
	 * @param endTime
	 * @return
	 */
	public String graphTetradRrdDb(GraphDefInfo graphDefInfo, String databaseName, String sortItem) {
		logger.info("start draw sub graph");
		String fileName = null;
		RrdGraphDef rrdGraphDef = null;
		
		try {
			String imagePath = CommonUtils.getDefaultRrdImgPath();
			fileName = graphDefInfo.getFileName() + "_" + databaseName+ ".png";
			
			//rrd 그래프 설정
			rrdGraphDef = tetradRrdGraphDef.createRrdDbGraphDef(graphDefInfo, databaseName, sortItem);
			rrdGraphDef.setFilename(imagePath + fileName);
			
			//실제 이미지 파일 생성
			RrdGraph rrdGraph = new RrdGraph(rrdGraphDef);
			BufferedImage bim = new BufferedImage(graphDefInfo.getWidth(), graphDefInfo.getHeight(), BufferedImage.TYPE_INT_RGB);
			rrdGraph.render(bim.getGraphics());
			
		} catch (Exception e) {
			logger.error(e, e);
			fileName = "";
		}		
		logger.info("end draw sub graph");
		return fileName;
	}
	
	public String graphPerRrdDb(String rrdDb, GraphDefInfo graphDefInfo) {
		logger.info("start draw main graph rrdDb:" + rrdDb);
		String fileName = null;
		RrdGraphDef rrdGraphDef = null;		
		try {
			String imagePath = CommonUtils.getDefaultRrdImgPath();
			fileName = graphDefInfo.getFileName() + ".png";
			
			rrdGraphDef = tetradRrdGraphDef.createGraphPerRrdDb(rrdDb, graphDefInfo);			
			rrdGraphDef.setFilename(imagePath + fileName);
			
			RrdGraph rrdGraph = new RrdGraph(rrdGraphDef);
			BufferedImage bim = new BufferedImage(graphDefInfo.getWidth(), graphDefInfo.getHeight(), BufferedImage.TYPE_INT_RGB);
			rrdGraph.render(bim.getGraphics());
		} catch (Exception e) {
			logger.error(e, e);
			fileName = "";
		}
		logger.info("end draw main graph rrdDb:" + rrdDb);
		return fileName;
	}
	
	public String graphSubRrdDb(GraphDefInfo graphDefInfo, String sortItem) {
		logger.info("start draw sub graph");
		String fileName = null;
		RrdGraphDef rrdGraphDef = null;
		
		try {
			String imagePath = CommonUtils.getDefaultRrdImgPath();
			fileName = graphDefInfo.getFileName() + ".png";
			
			//rrd 그래프 설정
			rrdGraphDef = tetradRrdGraphDef.createSubMultiDeviceGraphPerRrd(graphDefInfo, sortItem);
			rrdGraphDef.setFilename(imagePath + fileName);
			
			//실제 이미지 파일 생성
			RrdGraph rrdGraph = new RrdGraph(rrdGraphDef);
			BufferedImage bim = new BufferedImage(graphDefInfo.getWidth(), graphDefInfo.getHeight(), BufferedImage.TYPE_INT_RGB);
			rrdGraph.render(bim.getGraphics());
			
		} catch (Exception e) {
			logger.error(e, e);
			fileName = "";
		}		
		logger.info("end draw sub graph");
		return fileName;
	}
	
	public String lockGraphRrdDb(GraphDefInfo graphDefInfo, String sortItem) {
		logger.info("start draw lockGraph");
		String fileName = null;
		RrdGraphDef rrdGraphDef = null;
		
		try {
			String imagePath = CommonUtils.getDefaultRrdImgPath();
			fileName = graphDefInfo.getFileName() + ".png";
			
			//rrd 그래프 설정
			rrdGraphDef = tetradRrdGraphDef.createLockMultiGraphPerRrd(graphDefInfo, sortItem);
			rrdGraphDef.setFilename(imagePath + fileName);
			
			//실제 이미지 파일 생성
			RrdGraph rrdGraph = new RrdGraph(rrdGraphDef);
			BufferedImage bim = new BufferedImage(graphDefInfo.getWidth(), graphDefInfo.getHeight(), BufferedImage.TYPE_INT_RGB);
			rrdGraph.render(bim.getGraphics());
			
		} catch (Exception e) {
			logger.error(e, e);
			fileName = "";
		}		
		logger.info("end draw lockGraph");
		return fileName;
	}
	
	public String soloLockGraphRrdDb(GraphDefInfo graphDefInfo, String sortItem) {
		logger.info("start draw lockGraph");
		String fileName = null;
		RrdGraphDef rrdGraphDef = null;
		
		try {
			String imagePath = CommonUtils.getDefaultRrdImgPath();
			fileName = graphDefInfo.getFileName() + ".png";
			
			//rrd 그래프 설정
			rrdGraphDef = tetradRrdGraphDef.createSoloLockMultiGraphPerRrd(graphDefInfo, sortItem);
			rrdGraphDef.setFilename(imagePath + fileName);
			
			//실제 이미지 파일 생성
			RrdGraph rrdGraph = new RrdGraph(rrdGraphDef);
			BufferedImage bim = new BufferedImage(graphDefInfo.getWidth(), graphDefInfo.getHeight(), BufferedImage.TYPE_INT_RGB);
			rrdGraph.render(bim.getGraphics());
			
		} catch (Exception e) {
			logger.error(e, e);
			fileName = "";
		}		
		logger.info("end draw lockGraph");
		return fileName;
	}
	
	public String multiDeviceGraphPerRrd(String rrdDb, GraphDefInfo graphDefInfo) {
		logger.info("start draw main graph rrdDb:" + rrdDb);
		String fileName = null;
		RrdGraphDef rrdGraphDef = null;		
		try {
			String imagePath = CommonUtils.getDefaultRrdImgPath();
			fileName = graphDefInfo.getFileName() + ".png";
			
			rrdGraphDef = tetradRrdGraphDef.createMultiDeviceGraphPerRrd(rrdDb, graphDefInfo);			
			rrdGraphDef.setFilename(imagePath + fileName);
			
			RrdGraph rrdGraph = new RrdGraph(rrdGraphDef);
			BufferedImage bim = new BufferedImage(graphDefInfo.getWidth(), graphDefInfo.getHeight(), BufferedImage.TYPE_INT_RGB);
			rrdGraph.render(bim.getGraphics());
		} catch (Exception e) {
			logger.error(e, e);
			fileName = "";
		}
		logger.info("end draw main graph rrdDb:" + rrdDb);
		return fileName;
	}
	
	public String totalMultiGraphPerRrd(GraphDefInfo graphDefInfo) {
		String fileName = null;
		RrdGraphDef rrdGraphDef = null;		
		try {
			String imagePath = CommonUtils.getDefaultRrdImgPath();
			fileName = graphDefInfo.getFileName() + ".png";
			
			rrdGraphDef = tetradRrdGraphDef.createTotalMultiGraphPerRrd(graphDefInfo);			
			rrdGraphDef.setFilename(imagePath + fileName);
			
			RrdGraph rrdGraph = new RrdGraph(rrdGraphDef);
			BufferedImage bim = new BufferedImage(graphDefInfo.getWidth(), graphDefInfo.getHeight(), BufferedImage.TYPE_INT_RGB);
			rrdGraph.render(bim.getGraphics());
		} catch (Exception e) {
			logger.error(e, e);
			fileName = "";
		}
		return fileName;
	}
	
	public String detailedGraphPerRrdDb(GraphDefInfo graphDefInfo) {
		logger.info("start draw main graph rrdDb:" + graphDefInfo.getFileName());
		String fileName = null;
		RrdGraphDef rrdGraphDef = null;		
		try {
			String imagePath = CommonUtils.getDefaultRrdImgPath();
			fileName = graphDefInfo.getFileName() + "_detailed.png";
			
			rrdGraphDef = tetradRrdGraphDef.detailedCreateGraphPerRrdDb(graphDefInfo);			
			rrdGraphDef.setFilename(imagePath + fileName);
			
			RrdGraph rrdGraph = new RrdGraph(rrdGraphDef);
			BufferedImage bim = new BufferedImage(graphDefInfo.getWidth(), graphDefInfo.getHeight(), BufferedImage.TYPE_INT_RGB);
			rrdGraph.render(bim.getGraphics());
		} catch (Exception e) {
			logger.error(e, e);
			fileName = "";
		}
		logger.info("end draw main graph rrdDb:" + graphDefInfo.getFileName());
		return fileName;
	}
	
	public String detailedMultiDeviceGraphPerRrd(String rrdDb, GraphDefInfo graphDefInfo, String title) {
		logger.info("start draw main graph rrdDb:" + rrdDb);
		String fileName = null;
		RrdGraphDef rrdGraphDef = null;		
		try {
			String imagePath = CommonUtils.getDefaultRrdImgPath();
			fileName = graphDefInfo.getFileName() + ".png";
			
			rrdGraphDef = tetradRrdGraphDef.detailedCreateMultiDeviceGraphPerRrd(rrdDb, graphDefInfo, title);			
			rrdGraphDef.setFilename(imagePath + fileName);
			
			RrdGraph rrdGraph = new RrdGraph(rrdGraphDef);
			BufferedImage bim = new BufferedImage(graphDefInfo.getWidth(), graphDefInfo.getHeight(), BufferedImage.TYPE_INT_RGB);
			rrdGraph.render(bim.getGraphics());
		} catch (Exception e) {
			logger.error(e, e);
			fileName = "";
		}
		logger.info("end draw main graph rrdDb:" + rrdDb);
		return fileName;
	}
	
	/**
	 * 메인 대쉬 보드에 표시하는 데몬 상태 정보 리스트를 취득한다.
	 */
	public List<ServerStatus> readTetradServerStatus() {
		List<ServerStatus> mainServerStatus = new ArrayList<ServerStatus>();
		
		try {
			mainServerStatus = daoForMongo.readServerStatus();
		} catch (Exception e) {
			
		}
		
		return mainServerStatus;
	}
	
	/**
	 * 메인 대쉬 보드에 표시하는 위험, 경고 정보 리스트를 취득한다.
	 */
	public List<Alarm> readTetradCriticalStatus() {
		List<Alarm> mainCriticalStatus = new ArrayList<Alarm>();
		
		try {
			mainCriticalStatus = daoForMongo.readCriticalStatus();
		} catch (Exception e) {
			
		}
		
		return mainCriticalStatus;
	}
	
	
	/**
	 * 디버깅을 위해 환경 설정 파일에 xml로 export할 것인지 설정(exportXmlYn=1)한 경우, fetch한 데이터를  xml로 export한다.
	 * @param fetchData
	 * @param rrdPath
	 */
	private void exportXmlIfNecessary(FetchData fetchData, String rrdPath) {
		try {
			if ("1".equals(exportXmlYn)) {
				String filepath = exportXmlFilePath + rrdPath + "_" + TimestampUtil.readTimestamp() + ".xml";
				fetchData.exportXml(filepath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 디버깅을 위해 환경 설정 파일에 xml로 export할 것인지 설정(exportXmlYn=1)한 경우, 해당 Rrd db의 데이터를  xml로 export한다.
	 * @param device
	 * @param rrdPath
	 */
	private void exportXmlIfNecessary(Device device) {
		try {
			
			if ("1".equals(exportXmlYn)) {
//				String rrdPath.
//				RrdDb rrdDb = new RrdDb(rrdPath);
//				String filepath = exportXmlFilePath + rrdPath + "_" + TimestampUtil.readTimestamp() + ".xml";
//				rrdDb.exportXml(filepath);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	private void insertConnectionTimeoutError(Device device) {
		logger.info("	Start insert connections timneout error");
		try {			
			daoForMongo.updateServerStatusInfo(device.getIdx());
			WriteResult wr = criticalOperation.insertConnectionTimeoutError(device);
			
			boolean isUpdate = (Boolean) wr.getField("updatedExisting");
			if (!isUpdate) sendEmail(device);
			if (!isUpdate && !Utility.isNull(SMS_SCRIPT_PATH).isEmpty()) sendSMS(device);
			
		} catch (Exception e) {
			e.printStackTrace();
			logger.error(e, e);
		}
		logger.info("	End insert connections timneout error");
	}

	@Override
	public void createTotalRrdDb() throws Exception {
		String rrdPath = null;

		try {
			List<StatusDatasourceName> totalDatasourceNames = new TotalStatusRule().totalStatusDatasourceNameXMLToObject();
			
			String dsName;
			for (StatusDatasourceName dbstatusInfo : totalDatasourceNames) {
				dsName = dbstatusInfo.getDsName();
				InitialRrdService rrdSvc = new InitialRrdService(CommonUtils.getRrdDbPath(dsName));
				rrdSvc.initialRrd(dsName, dbstatusInfo.getDsType());							
			}
		} catch (Exception e) {
			e.printStackTrace();
			logger.info(rrdPath + " Tetrad Rrd Db not created!!");
			throw e;
		}

	}

	@Override
	public void makeTotalMongodInfo() throws Exception {
		Map<String, Object> totalInfo = new HashMap<String, Object>();

		List<ServerStatus> serverStatusList = daoForMongo.readServerStatus(ColumnConstent.PROCESS_MONGOD, SERVERSTATUS_TRUE);
		calculateAndSetTotalInfo(totalInfo, serverStatusList);

		daoForMongo.insertTotalDaemonInfo(totalInfo);

		InitialRrdService rrdSvc = new InitialRrdService();
		rrdSvc.setDataValues(totalInfo);
		rrdSvc.executeTotal();

	}
	
	private void calculateAndSetTotalInfo(Map<String, Object> totalInfo, List<ServerStatus> serverStatusList) {

		double totalDbDataSize = 0;
		double totalDbIndexSize = 0;
		double totalGlobalLockTime = 0;
		double totalSystemLocksTimeLockedMicros_R = 0;
		double totalSystemLocksTimeLockedMicros_W = 0;
		double totalLocksTimeLockedMicros_r = 0;
		double totalLocksTimeLockedMicros_w = 0;
		double maxDiffPageFaultLock = 0;
		
		for (ServerStatus serverStatus : serverStatusList) {
			totalDbDataSize += serverStatus.getDbDataSize();
			totalDbIndexSize += serverStatus.getDbIndexSize();
			totalGlobalLockTime += serverStatus.getGlobalLock_lockTime();
			
			totalSystemLocksTimeLockedMicros_R += serverStatus.getLocks_timeLockedMicros_R();
			totalSystemLocksTimeLockedMicros_W += serverStatus.getLocks_timeLockedMicros_W();
			
			totalLocksTimeLockedMicros_r += serverStatus.getDb_sum_locks_timeLockedMicros_r();
			totalLocksTimeLockedMicros_w += serverStatus.getDb_sum_locks_timeLockedMicros_w();
			
			
			if (serverStatus.getDiff_extra_info_page_faults() > maxDiffPageFaultLock) 
				maxDiffPageFaultLock = serverStatus.getDiff_extra_info_page_faults();
		}
		
		totalInfo.put(TOTAL_TOTALDBDATASIZE, totalDbDataSize);
		totalInfo.put(TOTAL_TTOTALDBINDEXSIZE, totalDbIndexSize);
		totalInfo.put(TOTAL_DIFF_TOTALDBDATASIZE, totalDbDataSize);
		totalInfo.put(TOTAL_DIFF_TOTALDBINDEXSIZE, totalDbIndexSize);
		totalInfo.put(TOTAL_TOTALGLOBALLOCKTIME, totalGlobalLockTime);
		totalInfo.put(TOTAL_MAXIMUMPAGEFAULTS, maxDiffPageFaultLock);
		
		totalInfo.put(TOTAL_TOTALSYSTEMLOCKSTIMELOCKEDMICROS_R_SUM, totalSystemLocksTimeLockedMicros_R);
		totalInfo.put(TOTAL_TOTALSYSTEMLOCKSTIMELOCKEDMICROS_W_SUM, totalSystemLocksTimeLockedMicros_W);
		totalInfo.put(TOTAL_TOTALDBLOCKSTIMELOCKEDMICROS_R_SUM, totalLocksTimeLockedMicros_r);
		totalInfo.put(TOTAL_TOTALDBLOCKSTIMELOCKEDMICROS_W_SUM, totalLocksTimeLockedMicros_w);
	}
	
	private void sendEmail(Device device) {
		logger.info("	send barrier mail");
		try {
			List<User> to = daoForMongo.readUser();			
			if (to == null) throw new NullPointerException();
				
			String from = daoForMongo.readEmailFrom();
			if(from == null)from = TetradRrdConfig.getTetradRrdConfig("mail_from");
			final String subject = "Daemon Failure Alert";
			
			Map<String, Object> map = new HashMap<String, Object>();
			map.put("name", device.getUid());
			map.put("time", DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss"));
			map.put("ip", device.getIp());
			map.put("port", device.getPort());
			 
			mailResource.sendMail(from, to, subject, map);
			
		} catch (NullPointerException e) {
			logger.error("There is no recipient", e);
		} catch (Exception e) {
		 	logger.error("failed send mail ", e);
		}
		logger.info("	end barrier mail");
	}
	
	private void sendSMS(Device device) {
		logger.info("	send barrier mail");
		try {
			String daemonName = device.getUid();
			String time = DateUtil.getCurrentDate("yyyy-MM-dd HH:mm:ss");
			String daemonIp = device.getIp();
			String daemonPort = device.getPort();
			String cmd = SMS_SCRIPT_PATH.replace("${DAEMON_NAME}", daemonName).replace("${DATE}", time).replace("${DAEMON_IP}", daemonIp).replace("${DAEMON_PORT}", daemonPort);
			Process process = Runtime.getRuntime().exec(cmd);
		} catch (NullPointerException e) {
			logger.error("There is no recipient", e);
		} catch (Exception e) {
		 	logger.error("failed send sms ", e);
		}
		logger.info("	end barrier sms");
	}
	
	public int getThreadIndex(int deviceCode) {
		Query query = new Query(Criteria.where(ColumnConstent.IDX).lte(deviceCode));		
		int deviceOrder = daoForMongo.getCount(query, Device.class);
		
		return deviceOrder;
	}
}
