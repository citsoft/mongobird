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

import static net.cit.tetrad.common.ColumnConstent.ALARM_ALARM;
import static net.cit.tetrad.common.ColumnConstent.ALARM_CONFIRM;
import static net.cit.tetrad.common.ColumnConstent.ALARM_COUNT;
import static net.cit.tetrad.common.ColumnConstent.ALARM_CRI_TYPE;
import static net.cit.tetrad.common.ColumnConstent.ALARM_CRI_VALUE;
import static net.cit.tetrad.common.ColumnConstent.ALARM_DEVICECODE;
import static net.cit.tetrad.common.ColumnConstent.ALARM_FIGURE;
import static net.cit.tetrad.common.ColumnConstent.ALARM_GROUPCODE;
import static net.cit.tetrad.common.ColumnConstent.ALARM_IP;
import static net.cit.tetrad.common.ColumnConstent.ALARM_PORT;
import static net.cit.tetrad.common.ColumnConstent.ALARM_REAL_CRI_VALUE;
import static net.cit.tetrad.common.ColumnConstent.ALARM_REAL_FIGURE;
import static net.cit.tetrad.common.ColumnConstent.ALARM_REG_DATE;
import static net.cit.tetrad.common.ColumnConstent.ALARM_REG_TIME;
import static net.cit.tetrad.common.ColumnConstent.ALARM_TYPE;
import static net.cit.tetrad.common.ColumnConstent.ALARM_UP_DATE;
import static net.cit.tetrad.common.ColumnConstent.ALARM_UP_TIME;
import static net.cit.tetrad.common.ColumnConstent.COLL_ALARM;
import static net.cit.tetrad.common.ColumnConstent.COLL_DASHBOARD;
import static net.cit.tetrad.common.ColumnConstent.COLL_TOTALMONGODINFO;
import static net.cit.tetrad.common.ColumnConstent.COL_DEVICECODE;
import static net.cit.tetrad.common.ColumnConstent.DEVICECODE;
import static net.cit.tetrad.common.ColumnConstent.IDX;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DIFF_GLOBALLLOCKTIME;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DIFF_PAGEFAULTS;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_ERROR;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_GLOBALLLOCKTIME;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_OK;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_PAGEFAULTS;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_REGTIME;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_TYPE;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_ISMASTER;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_OPINSERT;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_OPDELETE;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_OPQUERY;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_OPUPDATE;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DIFF_OPDELETE;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DIFF_OPINSERT;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DIFF_OPQUERY;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DIFF_OPUPDATE;
import static net.cit.tetrad.common.ColumnConstent.DBSTATUS_DBNAME;
import static net.cit.tetrad.common.ColumnConstent.USER_EMAIL;
import static net.cit.tetrad.common.ColumnConstent.DEVICE_UID;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_LOCKSTIMELOCKEDMICROS_R;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_LOCKSTIMELOCKEDMICROS_W;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DIFF_LOCKSTIMELOCKEDMICROS_R;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DIFF_LOCKSTIMELOCKEDMICROS_W;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DBSUMLOCKSLOCKED_R;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DBSUMLOCKSLOCKED_W;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DIFF_DBSUMLOCKSLOCKED_R;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_DIFF_DBSUMLOCKSLOCKED_W;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.cit.monad.Operations;
import net.cit.tetrad.common.ColumnConstent;
import net.cit.tetrad.dao.management.IndexDao;
import net.cit.tetrad.model.Alarm;
import net.cit.tetrad.model.Critical;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.model.Global;
import net.cit.tetrad.model.User;
import net.cit.tetrad.rrd.bean.DbStatus;
import net.cit.tetrad.rrd.bean.ServerStatus;
import net.cit.tetrad.rrd.utils.TetradRrdConfig;
import net.cit.tetrad.utility.QueryUtils;

import org.apache.log4j.Logger;
import org.codehaus.jackson.map.ObjectMapper;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.DBCollection;
import com.mongodb.WriteResult;

public class DataAccessObjectForMongoImpl implements DataAccessObjectForMongo {
	private Logger logger = Logger.getLogger("process.rrd");
	
	private Operations operations;
	private IndexDao indexDao; 
	
	private static List<Device> allDevices = null;
	private static List<Global> allGlobals = null;
	private static List<Critical> allCriticals = null;	
	private static int logGenerationIntervalSeconds = 0;						// 로그 갱신 주기
	private static int logRetentionPeriodDays = 0;								// 로그 보존 기간
	private static int createRowCntPerSeconds = 0;								// Rrd Db 생성해야 할 row 갯수
	private static int createRowCntPerMinutes = 0;	
	private static int createRowCntPerHours = 0;	
	private static int createRowCntPerDays = 0;
	
	
	public void setOperations(Operations operations) {
		this.operations = operations;
	}	
	
	public void setIndexDao(IndexDao indexDao) {
		this.indexDao = indexDao;
	}


	public List<ServerStatus> readServerStatus() {
		List<ServerStatus> mainServerStatus = new ArrayList<ServerStatus>();
		
		try {
			mainServerStatus = operations.findAll(ServerStatus.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mainServerStatus;
	}
	
	public List<ServerStatus> readServerStatus(String process) {
		List<ServerStatus> mainServerStatus = new ArrayList<ServerStatus>();		
		try {
			Query query = new Query(where(SERVERSTATUS_TYPE).is(process));
			mainServerStatus = operations.find(query, ServerStatus.class, COLL_DASHBOARD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mainServerStatus;
	}
	
	public List<ServerStatus> readServerStatus(String process, String ismaster) {
		List<ServerStatus> mainServerStatus = new ArrayList<ServerStatus>();		
		try {
			Query query = new Query();
			query.addCriteria(Criteria.where(SERVERSTATUS_TYPE).is(process));
			query.addCriteria(Criteria.where(SERVERSTATUS_ISMASTER).is(ismaster));
			mainServerStatus = operations.find(query, ServerStatus.class, COLL_DASHBOARD);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mainServerStatus;
	}
	
	public Map<String, Object> readServerStatusByMap(int deviceIdx) {
		Map<String, Object> serverStatus = new HashMap<String, Object>();
		try {
			Query query = new Query(where(DEVICECODE).is(deviceIdx));
			serverStatus = (Map<String, Object>)operations.findOne(query, Map.class, COLL_DASHBOARD);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return serverStatus;
	}
	
	public List<Alarm> readCriticalStatus() {
		List<Alarm> mainAlarms = new ArrayList<Alarm>();
		
		try {
			mainAlarms = operations.find(new Query(where("del").is("N")), Alarm.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return mainAlarms;
	}

	public List<Device> readDeviceList() {
		try {
			if (allDevices == null)
				allDevices = operations.find(new Query(), Device.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return allDevices;
	}	
	
	public Device readDeviceInfo(int deviceIdx) {
		Device device = null;
		try {
			device = (Device)operations.find(new Query(where(ColumnConstent.IDX).is(deviceIdx)), Device.class);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return device;
	}	

	public List<Global> readGlobalList() {
		try {
			if (allGlobals == null)
				allGlobals = operations.find(new Query(where("del").is("N")), Global.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return allGlobals;
	}
	
	public List<Critical> readCriticalList() {
		try {
			if (allCriticals == null) 
				allCriticals = operations.find(new Query(), Critical.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return allCriticals;
	}
	
	public List<Critical> readCriticalList(int deviceIdx) {
		List<Critical> criticalInfo = null;
		try {			
			criticalInfo = operations.find(new Query(Criteria.where(ColumnConstent.DEVICECODE).is(deviceIdx)), Critical.class);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return criticalInfo;
	}
	
	public Critical readCriticalInfo(int deviceIdx, String dsName) {
		Critical critical = null;
		try {
			Query query = new Query(Criteria.where(ColumnConstent.DEVICECODE).is(deviceIdx));
			query.addCriteria(Criteria.where(ColumnConstent.CLRITICAL_TYPE).is(dsName));
			critical = (Critical)operations.findOne(query, Critical.class);
		} catch (Exception e) {
			e.printStackTrace();
		}		
		return critical;
	}
	
//	public int readLogGenerationInterval() {
//		try {
//			String defaultConfigValue = TetradRrdConfig.getTetradRrdConfig("default_log_generation_interval");
//			logGenerationIntervalSeconds = Integer.parseInt(defaultConfigValue);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return logGenerationIntervalSeconds;
//	}
//	
//	public int readLogRetentionPeriod() {
//		try {
//			String defaultConfigValue = TetradRrdConfig.getTetradRrdConfig("default_log_retention_period");
//			logRetentionPeriodDays = Integer.parseInt(defaultConfigValue);
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return logRetentionPeriodDays;
//	}
	
//	public int calculateCreateRowCntPerSeconds() {
//		try {
//			if (createRowCntPerSeconds == 0) {
//				float createRowCntTmp = (float)60 / readLogGenerationInterval() * 60 * 24 * readLogRetentionPeriod();
//				createRowCntPerSeconds = (int)createRowCntTmp;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			createRowCntPerSeconds = 0;
//		}
//		
//		return createRowCntPerSeconds;
//	}
//	
//	public int calculateCreateRowCntPerMinutes() {
//		try {
//			if (createRowCntPerMinutes == 0) {
//				float createRowCntTmp = (float)60 * 24 * readLogRetentionPeriod();
//				createRowCntPerMinutes = (int)createRowCntTmp;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			createRowCntPerMinutes = 0;
//		}
//		
//		return createRowCntPerMinutes;
//	}
//	
//	public int calculateCreateRowCntPerHours() {
//		try {
//			if (createRowCntPerHours == 0) {
//				float createRowCntTmp = (float)24 * readLogRetentionPeriod();
//				createRowCntPerHours = (int)createRowCntTmp;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			createRowCntPerHours = 0;
//		}
//		
//		return createRowCntPerHours;
//	}
//	
//	public int calculateCreateRowCntPerDays() {
//		try {
//			if (createRowCntPerDays == 0) {
//				float createRowCntTmp = (float)readLogRetentionPeriod();
//				createRowCntPerDays = (int)createRowCntTmp;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			createRowCntPerDays = 0;
//		}
//		
//		return createRowCntPerDays;
//	}	
	
	public void insertServerStatusInfo(ServerStatus serverStatusInfo) {
		try {
			Query query = new Query(Criteria.where(DEVICECODE).is(serverStatusInfo.getDeviceCode()));
			
			Update update = new Update();
			ObjectMapper converter = new ObjectMapper();
			Map<String,Object> props = converter.convertValue(serverStatusInfo, Map.class);
			
			Set<String> keys = props.keySet();
			Iterator<String> it = keys.iterator();
			while(it.hasNext()){
				String key = it.next().toString();
				Object value = props.get(key);
				
				if (value != null)  update.set(key, value);
			}
			
			WriteResult wr = operations.updateMulti(query, update, COLL_DASHBOARD, true);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateServerStatusInfo(int deviceCode) {
		try {
			Query query = new Query(Criteria.where(DEVICECODE).is(deviceCode));
			Update update = new Update();
			update.set(SERVERSTATUS_OK, 0);
			update.set(SERVERSTATUS_ERROR, 0);
			
			operations.updateMulti(query, update, COLL_DASHBOARD);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	public void insertDetailServerStatusInfo(ServerStatus serverStatusInfo) {
		try {			
			operations.insert(serverStatusInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}		

	public void setGloballLockPageFaults(Map<String, Object> serverStatusFromMongo) {
		logger.info("\t start settingGloballLock and PageFaults");
		try {
			Query query = new Query(Criteria.where(COL_DEVICECODE).is(serverStatusFromMongo.get(COL_DEVICECODE)));
			query.sort().on(SERVERSTATUS_REGTIME, Order.DESCENDING);
		
			ServerStatus recentServerStatus = operations.findOne(query, ServerStatus.class, COLL_DASHBOARD);
			
			if (recentServerStatus != null) {
				double recentGloballlocktime = recentServerStatus.getGlobalLock_lockTime();
				double recentPagefaults =  recentServerStatus.getExtra_info_page_faults();
				double recentGlobalLocksLocked_R = recentServerStatus.getLocks_timeLockedMicros_R();
				double recentGlobalLocksLocked_W = recentServerStatus.getLocks_timeLockedMicros_W();
				double recentDbSumLocksLocked_r = recentServerStatus.getDb_sum_locks_timeLockedMicros_r();
				double recentDbSumLocksLocked_w = recentServerStatus.getDb_sum_locks_timeLockedMicros_w();
				
				double currentGloballLocktime = CriticalHelper.convertToDouble(serverStatusFromMongo.get(SERVERSTATUS_GLOBALLLOCKTIME));
				double currentPagefaults = CriticalHelper.convertToDouble(serverStatusFromMongo.get(SERVERSTATUS_PAGEFAULTS));
				double currentGlobalLocksLocked_R = CriticalHelper.convertToDouble(serverStatusFromMongo.get(SERVERSTATUS_LOCKSTIMELOCKEDMICROS_R));
				double currentGlobalLocksLocked_W = CriticalHelper.convertToDouble(serverStatusFromMongo.get(SERVERSTATUS_LOCKSTIMELOCKEDMICROS_W));
				double currentDbSumLocksLocked_r = CriticalHelper.convertToDouble(serverStatusFromMongo.get(SERVERSTATUS_DBSUMLOCKSLOCKED_R));
				double currentDbSumLocksLocked_w = CriticalHelper.convertToDouble(serverStatusFromMongo.get(SERVERSTATUS_DBSUMLOCKSLOCKED_W));

				double diffGlobalLockTime = currentGloballLocktime - recentGloballlocktime;
				double diffPageFaults = currentPagefaults - recentPagefaults;
				double diffGlobalLocksLocked_R = currentGlobalLocksLocked_R - recentGlobalLocksLocked_R;
				double diffGlobalLocksLocked_W = currentGlobalLocksLocked_W - recentGlobalLocksLocked_W;
				double diffDbSumLocksLocked_r = currentDbSumLocksLocked_r - recentDbSumLocksLocked_r;
				double diffDbSumLocksLocked_w = currentDbSumLocksLocked_w - recentDbSumLocksLocked_w;

				logger.debug("GlobalLockTime : " + currentGloballLocktime + " - " + recentGloballlocktime + " = " + diffGlobalLockTime);
				logger.debug("PageFaults : " + currentPagefaults + " - " + recentPagefaults + " = " + diffPageFaults);
				
				serverStatusFromMongo.put(SERVERSTATUS_DIFF_GLOBALLLOCKTIME, diffGlobalLockTime);
				serverStatusFromMongo.put(SERVERSTATUS_DIFF_PAGEFAULTS, diffPageFaults);
				serverStatusFromMongo.put(SERVERSTATUS_DIFF_LOCKSTIMELOCKEDMICROS_R, diffGlobalLocksLocked_R);
				serverStatusFromMongo.put(SERVERSTATUS_DIFF_LOCKSTIMELOCKEDMICROS_W, diffGlobalLocksLocked_W);
				serverStatusFromMongo.put(SERVERSTATUS_DIFF_DBSUMLOCKSLOCKED_R, diffDbSumLocksLocked_r);
				serverStatusFromMongo.put(SERVERSTATUS_DIFF_DBSUMLOCKSLOCKED_W, diffDbSumLocksLocked_w);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("\t end settingGloballLock and PageFaults");
	}

	public void setOpcounter(Map<String, Object> serverStatusFromMongo) {
		logger.info("\t start setting Opcounter");
		try {
			Query query = new Query(Criteria.where(COL_DEVICECODE).is(serverStatusFromMongo.get(COL_DEVICECODE)));
			query.sort().on(SERVERSTATUS_REGTIME, Order.DESCENDING);
		
			ServerStatus recentServerStatus = operations.findOne(query, ServerStatus.class, COLL_DASHBOARD);
			
			if (recentServerStatus != null) {
				double recentOpInsert = recentServerStatus.getOpcounters_insert();
				double recentOpQuery =  recentServerStatus.getOpcounters_query();
				double recentOpUpdate = recentServerStatus.getOpcounters_update();
				double recentOpDelete =  recentServerStatus.getOpcounters_delete();
				double currentOpInsert= CriticalHelper.convertToDouble(serverStatusFromMongo.get(SERVERSTATUS_OPINSERT));
				double currentpQuery = CriticalHelper.convertToDouble(serverStatusFromMongo.get(SERVERSTATUS_OPQUERY));
				double currentOpUpdate = CriticalHelper.convertToDouble(serverStatusFromMongo.get(SERVERSTATUS_OPUPDATE));
				double currentOpDelete = CriticalHelper.convertToDouble(serverStatusFromMongo.get(SERVERSTATUS_OPDELETE));

				double diffOpInsert = currentOpInsert - recentOpInsert;
				double diffOpQuery = currentpQuery - recentOpQuery;
				double diffOpUpdate = currentOpUpdate - recentOpUpdate;
				double diffOpDelete = currentOpDelete - recentOpDelete;
				
				serverStatusFromMongo.put(SERVERSTATUS_DIFF_OPINSERT, diffOpInsert);
				serverStatusFromMongo.put(SERVERSTATUS_DIFF_OPQUERY, diffOpQuery);
				serverStatusFromMongo.put(SERVERSTATUS_DIFF_OPUPDATE, diffOpUpdate);
				serverStatusFromMongo.put(SERVERSTATUS_DIFF_OPDELETE, diffOpDelete);
			}			
		} catch (Exception e) {
			e.printStackTrace();
		}
		logger.info("\t end setting Opcounter");
	}
	
	public void insertDbStatusInfo(DbStatus dbStatusInfo) {
		try {
			operations.insert(dbStatusInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void insertCriticalStatusInfo(Alarm alarmInfo) {
		try {
			operations.insert(alarmInfo);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void updateCriticalStatusInfo(Query query, Update update) {
		try {
			operations.updateFirst(query, update, Alarm.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Alarm readCriticalStatusInfo(Query query) {
		Alarm alarmInfo = new Alarm();
		
		try {
			alarmInfo = operations.findOne(query, Alarm.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return alarmInfo;
	}
	
	public int getCount(Query query, Class<?> clazz) {
		int count = 0;
		
		try {
			count = (int)operations.count(query, clazz);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return count;
	}
	
	/**
	 * 환경 변수 중 해당 key에 대한 값을 취득한다.
	 * @param key
	 * @return
	 */
//	private int readGlobalValue(String key) {
//		int globalValue = -1;
//		
//		try {
//			Query query = new Query(Criteria.where(GLOBAL_SEARCH_KEY).is(key));
//			Global global = operations.findOne(query, Global.class);
//			if (global != null && global.getValue() != 0) 
//				globalValue = global.getValue();
//		} catch (Exception e) {
//			e.printStackTrace();
//		}
//		
//		return globalValue;
//	}
	
	public void insertTotalDaemonInfo(Map<String, Object> totalInfo) {	
		try {
			Update update = new Update();
			QueryUtils.getUpdate(update, totalInfo);
			
			operations.updateMulti(new Query(), update, COLL_TOTALMONGODINFO, true);			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public WriteResult upsertAlarm(Alarm alarm) {	
		WriteResult wr = null;
		
		try {
			Update update = new Update();
			Query query = new Query(Criteria.where(ALARM_REG_DATE).is(alarm.getReg_date()));
			query.addCriteria(Criteria.where(ALARM_CONFIRM).is(alarm.getConfirm()));
			query.addCriteria(Criteria.where(ALARM_DEVICECODE).is(alarm.getDeviceCode()));
			query.addCriteria(Criteria.where(ALARM_TYPE).is(alarm.getType()));
			query.addCriteria(Criteria.where(ALARM_CRI_TYPE).is(alarm.getCri_type()));			
			
			Alarm alarmInfo = readAlarmInfo(query);
			if (alarmInfo == null) {
				int idx = indexDao.createIdx(COLL_ALARM);
				update.set(IDX, idx);
				update.set(ALARM_REG_DATE, alarm.getReg_date());
				update.set(ALARM_REG_TIME, alarm.getReg_time());
			}
			
			update.set(ALARM_GROUPCODE, alarm.getGroupCode());
			update.set(ALARM_IP, alarm.getIp());
			update.set(ALARM_PORT, alarm.getPort());
			update.set(ALARM_CRI_VALUE, alarm.getCri_value());
			update.set(ALARM_FIGURE, alarm.getFigure());
			update.set(ALARM_REAL_CRI_VALUE, alarm.getReal_cri_value());
			update.set(ALARM_REAL_FIGURE, alarm.getReal_figure());
			update.set(ALARM_UP_DATE, alarm.getUp_date());
			update.set(ALARM_UP_TIME, alarm.getUp_time());
			update.set(ALARM_ALARM, alarm.getAlarm());
			update.inc(ALARM_COUNT, 1);
			
			wr = operations.updateMulti(query, update, COLL_ALARM, true);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		return wr;
	}
	
	private Alarm readAlarmInfo(Query query) {
		Alarm alarmInfo = null;		
		try {			
			alarmInfo = operations.findOne(query, Alarm.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return alarmInfo;
	}
	
	public List<User> readUser() {
		List<User> user = null;
		try {
			Query query = new Query(Criteria.where(USER_EMAIL).ne("").ne(null));
			
			user = operations.find(query, User.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return user;
	}
	
	public String readEmailFrom() {
		String email = null;
		try {
			Query query = new Query(Criteria.where(DEVICE_UID).is(USER_EMAIL));
			
			Global global = operations.findOne(query, Global.class);
			email = global.getValue();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return email;
	}
}
