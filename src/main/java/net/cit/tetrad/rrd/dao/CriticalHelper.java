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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.cit.tetrad.common.ColumnConstent;
import net.cit.tetrad.model.Alarm;
import net.cit.tetrad.model.Critical;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.utility.CommonUtils;

import org.apache.log4j.Logger;

public class CriticalHelper {	
	private static Logger logger = Logger.getLogger(CriticalHelper.class);
	
	private DataAccessObjectForMongo daoForMongo;	
	private boolean isSubGraphhrule = false;
	
	private int deviceCode;
	private Device device; 
	private Map<String, Object> serverStatusInfoGroup;
	double figure = 0;
	
	long standard = 0;
	private final int MILISECONDS = 1000;
	
	CriticalHelper (int deviceCode) {
		this.deviceCode = deviceCode;
		
		setDaoForMongo();
	}
		
	CriticalHelper (Device device, Map<String, Object> serverStatusInfoGroup) {
		this.device = device;
		this.deviceCode = device.getIdx();
		this.serverStatusInfoGroup = serverStatusInfoGroup;		
		
		setDaoForMongo();
	}
	
	enum DataUnit {
		B,
		KB, 
		MB		
	}
		
	public void setDaoForMongo() {
		this.daoForMongo = (DataAccessObjectForMongo)CommonUtils.appCtx.getBean("dataAccessObjectForMongo");
	}

	public double getCriteriaValue(String dsName) {
		double criteriaValue = 0;
		if (dsName.equals("dbDataSize") || dsName.equals("dbFileSize")) {
			if (device == null) setDeviceInfo();
			
			String dataUnit = "B";
			criteriaValue = convertGb(dataUnit, device.getHddsize());		
		} else if (dsName.equals("mem_resident")) {
			if (device == null) setDeviceInfo();
			
			String dataUnit = "MB";
			if (isSubGraphhrule) {
				dataUnit = "B";
			} 
			criteriaValue = convertGb(dataUnit, device.getMemorysize());			
		} else if (dsName.equals("connections_current")) {
			if (serverStatusInfoGroup == null) setServerStatusInfo();
			
			double availableConnections = convertToDouble(serverStatusInfoGroup.get("connections_available"));
			double currentConnections = convertToDouble(serverStatusInfoGroup.get("connections_current"));
			criteriaValue = currentConnections + availableConnections;
		}
		return criteriaValue;
	}
	
	private void setDeviceInfo() {
		Device device = daoForMongo.readDeviceInfo(deviceCode);
		this.device = device;
	}
	
	private void setServerStatusInfo() {
		Map<String, Object> serverStatusInfo = daoForMongo.readServerStatusByMap(deviceCode);
		this.serverStatusInfoGroup = serverStatusInfo;
	}
	
	private Critical getCriticalInfo(String dsName) {
		
		return daoForMongo.readCriticalInfo(deviceCode, dsName);
	}
	
	public static double getTotalConnections(double connAbailable, double connCurrent) {
		return convertToDouble(connAbailable) + convertToDouble(connCurrent);
	}
	
	public static double convertToDouble(Object dsObjectValue) {
		double dsValue = 0.0;
		
		if (dsObjectValue != null) {
			if (dsObjectValue instanceof java.lang.Integer) {
				dsValue  = (double)((Integer)dsObjectValue);
			}  else if (dsObjectValue instanceof java.lang.String) {
				dsValue = Double.parseDouble(dsObjectValue.toString());
			} else if (dsObjectValue instanceof java.lang.Long) {
				dsValue = Double.parseDouble(dsObjectValue.toString());
			} else {
				dsValue = (Double)dsObjectValue;
			}
		}
		return dsValue;
	}

	public static double convertGb(String unit, double target) {
		double value = 0;
		switch (DataUnit.valueOf(unit)) {
			case B:
				value = target * 1024 * 1024 *  1024;
				break;
			case KB:
				value = target * 1024 * 1024;
				break;
			case MB:
				value = target * 1024;
				break;
				
		}
		return value;
	}

	public static double getPercentValue(double target, int percent) {
		return target * (percent/100d);
	}
	
	List<Critical> findCriticalInfo(Map<String, List<Critical>> groupBindCritical, String criticalkey){
		
		List<Critical> criticalLst = new ArrayList<Critical> ();
		
		criticalLst = groupBindCritical.get(criticalkey);
		
		return criticalLst;
	}

	private double getCriteriaValue(String dsName, String unit, int stdValue) {
		double value = 0;
		
		value = stdValue;
		if (unit.equals("percent")) {
			double criteriaValue = getCriteriaValue(dsName);
			value = getPercentValue(criteriaValue, stdValue);

			this.standard = (long) criteriaValue;
			
		} else if (unit.equals("seconds")) {
			if (isSubGraphhrule) { // 그래프에 임계치 선을 그릴경우 miliseconds로 환산해야하기때문에 이 로직이 들어감..
				value = value / MILISECONDS;
			} 
		}
		return value;
	}

	public void settingGroupAlarm(Map<String, List<Critical>> groupBindCriticals, Map<String,List<Alarm>> alarmMap) {		
	
		Set<String> keyset = groupBindCriticals.keySet();
		
		for(String key : keyset){
			List<Critical> criticalLst = groupBindCriticals.get(key);
			List<Alarm> checkAlarm = new ArrayList<Alarm>();
			
			for(int i = 0 ; i< criticalLst.size() ; i++){
				Critical critical = criticalLst.get(i);
				Alarm alarm = new Alarm();
				settingAlarm(critical.getType(), critical, alarm);
				alarm.setGroupCnt(critical.getGroupCnt());
				checkAlarm.add(alarm);
			}
			
			boolean flag = false;
			for(int i = 0 ; i< checkAlarm.size() ; i++){
				if(checkAlarm.get(i).getAlarm()==0){ flag= false; break;}
				else {flag = true; continue;}
			}
			
			if(flag==true){
				for(int i = 0 ; i < checkAlarm.size() ; i++){
					checkAlarm.get(i).setGroupBind(key);
					checkAlarm.get(i).setCri_type(criticalLst.get(i).getType());
					alarmMap.put(key, checkAlarm);					
				}
			}
		}
	}
	
	public void settingAlarm(String dsName, Critical critical, Alarm alarm) {		
		boolean existCritical = false;	
		double criticalValue = 0;
		double warningValue = 0;
		double infoValue = 0;
		if (critical != null) {
			String unit = critical.getUnit();
			existCritical = true;

			criticalValue =  getCriteriaValue(dsName, unit, critical.getCriticalvalue());
			warningValue =  getCriteriaValue(dsName, unit, critical.getWarningvalue());
			infoValue =  getCriteriaValue(dsName, unit, critical.getInfovalue());
		}		
		double dsValue = convertToDouble(serverStatusInfoGroup.get(dsName));
		
		int alramType = 0; 
		double limitValue = 0;
		double real_limitValue = 0;
		
			if(existCritical){
				if (dsValue > criticalValue) {
					alramType = ColumnConstent.ALRAM_CRITICAL;
					limitValue = critical.getCriticalvalue();
					real_limitValue = criticalValue;
				} else if (dsValue > warningValue) {
					alramType = ColumnConstent.ALRAM_WARNING;
					limitValue = critical.getWarningvalue();
					real_limitValue = warningValue;
				} else if (dsValue > infoValue) {
					alramType = ColumnConstent.ALRAM_INFO;
					limitValue = critical.getInfovalue();
					real_limitValue = infoValue;
				}
			}
		
		alarm.setAlarm(alramType);
		alarm.setCri_value(limitValue);		
		alarm.setFigure(getFigure(critical.getUnit(), dsValue));
		alarm.setReal_cri_value(real_limitValue);
		alarm.setReal_figure(dsValue);
	}
	
	private double getFigure(String unit, double dsValue) {
		double figure = dsValue;
		if (unit.equals("percent")) {
			figure = (dsValue/standard) * 100;
		}
		return figure;
	}
}
