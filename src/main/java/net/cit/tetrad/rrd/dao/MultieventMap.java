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
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

import net.cit.tetrad.common.DateUtil;
import net.cit.tetrad.model.Alarm;
import net.cit.tetrad.model.Critical;


public class MultieventMap {
	public static ConcurrentMap<String,Map<Integer,Map<String, Alarm>>> groupAlarm = new ConcurrentHashMap<String,Map<Integer,Map<String, Alarm>>>();
	public static Queue<Alarm> groupQueue = new ConcurrentLinkedQueue<Alarm>();
		
	public void addGroup(Critical critical, Alarm alarm) {
		
		String groupname = critical.getGroupBind();
		int deviceCode = critical.getDeviceCode();
		String criticalType = critical.getType();
		int grpCount = critical.getGroupCnt();
		
		boolean hasGrpname = groupAlarm.containsKey(groupname);
		if (hasGrpname) {
			Map<Integer,Map<String, Alarm>> grpAlarm = groupAlarm.get(groupname);
			boolean hasInst = grpAlarm.containsKey(deviceCode);
			
			if (hasInst) {
				Map<String, Alarm> instAlarm = grpAlarm.get(deviceCode);
				instAlarm.put(criticalType, alarm);
				
				grpAlarm.put(deviceCode, instAlarm);
				groupAlarm.put(groupname, grpAlarm);
			} else {
				Map<String, Alarm> alarmMap = new HashMap<String, Alarm>();
				alarmMap.put(criticalType, alarm);
				
				grpAlarm.put(deviceCode, alarmMap);
				groupAlarm.put(groupname, grpAlarm);
			}
		} else {
			Map<String, Alarm> alarmMap = new HashMap<String, Alarm>();
			alarmMap.put(criticalType, alarm);		
			
			Map<Integer, Map<String, Alarm>> deviceAlarm = new HashMap<Integer, Map<String, Alarm>>();
			deviceAlarm.put(deviceCode, alarmMap);
			
			groupAlarm.put(groupname, deviceAlarm);
		}
		
		checkPushQueue(groupname, grpCount);
	}
	
	private void checkPushQueue(String groupname, int grpCount) {		
		Map<Integer,Map<String, Alarm>> grpAlarm = groupAlarm.get(groupname);
		Collection<Map<String, Alarm>> instAlarms = grpAlarm.values();
		
		List<Alarm> subAlarms = new ArrayList<Alarm>(); 
		for (Map<String, Alarm> instAlarm : instAlarms) {
			subAlarms.addAll(instAlarm.values());
		}
		
		if (subAlarms.size() == grpCount) {
			Alarm finalAlarm = new Alarm();
			finalAlarm.setReg_date(DateUtil.getCurrentDate("yyyy-MM-dd"));
			String time = DateUtil.getCurrentDate("HH:mm:ss");
			finalAlarm.setCri_type("multievent");
			finalAlarm.setReg_time(time);
			finalAlarm.setUp_time(time);
			finalAlarm.setGroupBind(groupname);
			finalAlarm.setSubLst(subAlarms);
			finalAlarm.setAlarm(isGroupEvent(subAlarms));
			
			groupAlarm.remove(groupname);
			groupQueue.add(finalAlarm);
		}
	}
	
	private int isGroupEvent(List<Alarm> alarmLst){		
		int tmp = Integer.MIN_VALUE;
		
		for(int i = 0; i < alarmLst.size() ; i++){
			if(tmp <= alarmLst.get(i).getAlarm()){
				tmp = alarmLst.get(i).getAlarm();
			}
		}
		return tmp;
	}
}
