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
package net.cit.tetrad.rrd.dao;

import java.util.List;
import java.util.Map;

import net.cit.tetrad.model.Alarm;
import net.cit.tetrad.model.Critical;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.model.Global;
import net.cit.tetrad.model.User;
import net.cit.tetrad.rrd.bean.DbStatus;
import net.cit.tetrad.rrd.bean.ServerStatus;

import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.WriteResult;

public interface DataAccessObjectForMongo {

	public List<ServerStatus> readServerStatus();
	public List<ServerStatus> readServerStatus(String process);
	public List<ServerStatus> readServerStatus(String process, String ismaster);
	Map<String, Object> readServerStatusByMap(int deviceIdx);
	public List<Alarm> readCriticalStatus();
	public List<Device> readDeviceList();
	Device readDeviceInfo(int deviceIdx);
	public List<Global> readGlobalList();
	public List<Critical> readCriticalList();
//	public int readLogGenerationInterval();
//	public int readLogRetentionPeriod();
	public List<Critical> readCriticalList(int deviceIdx);
	Critical  readCriticalInfo(int deviceIdx, String dsNames);
//	public int calculateCreateRowCntPerSeconds();
//	public int calculateCreateRowCntPerMinutes();
//	public int calculateCreateRowCntPerHours();
//	public int calculateCreateRowCntPerDays();
	public void insertServerStatusInfo(ServerStatus serverStatusInfo);
	void updateServerStatusInfo(int deviceCode);
	public void insertDetailServerStatusInfo(ServerStatus serverStatusInfo);
	public void insertDbStatusInfo(DbStatus dbStatusInfo);
	public void insertCriticalStatusInfo(Alarm alarmInfo);
	public void updateCriticalStatusInfo(Query query, Update update);
	public Alarm readCriticalStatusInfo(Query query);
	public int getCount(Query query, Class<?> clazz);
	
	public void insertTotalDaemonInfo(Map<String, Object> totalInfo);
	public WriteResult upsertAlarm(Alarm alarm);
	void setGloballLockPageFaults(Map<String, Object> serverStatusInfo);
	void setOpcounter(Map<String, Object> serverStatusFromMongo); 
	List<User> readUser();
	String readEmailFrom();
}
