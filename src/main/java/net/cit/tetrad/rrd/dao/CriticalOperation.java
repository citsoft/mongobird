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

import java.util.List;
import java.util.Map;

import net.cit.tetrad.common.ColumnConstent;
import net.cit.tetrad.common.DateUtil;
import net.cit.tetrad.model.Alarm;
import net.cit.tetrad.model.Critical;
import net.cit.tetrad.model.Device;

import org.apache.log4j.Logger;

import com.mongodb.WriteResult;

public class CriticalOperation {
	
	private Logger logger = Logger.getLogger("process.rrd");
	private DataAccessObjectForMongo daoForMongo;
	
	private CriticalHelper helper;
	
	public void setDaoForMongo(DataAccessObjectForMongo daoForMongo) {
		this.daoForMongo = daoForMongo;
	}
		
	public List<Critical> getCriticalInfo(int deviceIdx) {
		return daoForMongo.readCriticalList(deviceIdx);
	}
	
	public void settingAlarm(Device device, Map<String, Object> serverStatusInfoGroup) {
		logger.info("\t start settingAlarm");
		helper = new CriticalHelper(device, serverStatusInfoGroup);
		
		List<Critical> criticals = getCriticalInfo(device.getIdx());
		
		String dsName = ""; 
		if(criticals != null){
			for (Critical critical : criticals) {
				try {
					dsName = critical.getType();	
					Alarm alarm = new Alarm();	
					helper.settingAlarm(dsName, critical, alarm);
					
					if (isExistEvent(alarm)) {
						setBasicAlarmInfo(alarm, dsName, device);
						upsertAlarm(alarm);
					}
				} catch (Exception e) {
					logger.error(device.getIdx() + "	" + dsName + " setting alarm fail!! ", e);
				}
			}
		}
		logger.info("\t end settingAlarm");
	}
	
	private boolean isExistEvent(Alarm alarm) {
		return alarm.getAlarm() == 0 ? false : true;
	}
	
	public void setBasicAlarmInfo(Alarm alarm, String dsName, Device device) {
		alarm.setDeviceCode(device.getIdx());
		alarm.setType(device.getType());
		alarm.setGroupCode(device.getGroupCode());
		alarm.setIp(device.getIp());
		alarm.setPort(Integer.parseInt(device.getPort()));
		alarm.setCri_type(dsName);
		
		String current_date = DateUtil.getCurrentDate("yyyy-MM-dd");
		String current_time = DateUtil.getCurrentDate("HH:mm:ss");
		
		alarm.setReg_date(current_date);
		alarm.setReg_time(current_time);
		alarm.setUp_date(current_date);
		alarm.setUp_time(current_time);
	}
	
	public WriteResult upsertAlarm(Alarm alarm) {
		return daoForMongo.upsertAlarm(alarm);		
	}

	public WriteResult insertConnectionTimeoutError(Device device) {
		final String criType = "Connection_refused";
		
		Alarm alarm = new Alarm();
		alarm.setDeviceCode(device.getIdx());
		alarm.setType(device.getType());
		alarm.setGroupCode(device.getGroupCode());
		alarm.setIp(device.getIp());
		alarm.setPort(Integer.parseInt(device.getPort()));
		alarm.setCri_type(criType);
		
		String current_date = DateUtil.getCurrentDate("yyyy-MM-dd");
		String current_time = DateUtil.getCurrentDate("HH:mm:ss");
		
		alarm.setReg_date(current_date);
		alarm.setReg_time(current_time);
		alarm.setUp_date(current_date);
		alarm.setUp_time(current_time);
		alarm.setAlarm(ColumnConstent.ALRAM_RISK);
		
		return upsertAlarm(alarm);
	}
}
