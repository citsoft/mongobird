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

import net.cit.tetrad.common.Config;
import net.cit.tetrad.model.Alarm;

public class MultieventMapHelper implements Runnable{	
	private DataAccessObjectForMongo daoForMongo;
	private int limitIntervalTime;
	
	public MultieventMapHelper(DataAccessObjectForMongo daoForMongo) {
		this.daoForMongo = daoForMongo;
		limitIntervalTime = Integer.parseInt(Config.getConfig("event.multi.intervalTime"));
	}
	
	@Override
	public void run() {
		for ( ; ; ) {
			try {
				int size = MultieventMap.groupQueue.size();
				for (int i=0; i<size; i++) {
					Alarm groupAlarm = MultieventMap.groupQueue.poll();
					boolean flag =  attrTimeCheck(groupAlarm.getSubLst());
					
					if (flag) {
						daoForMongo.upsertAlarm(groupAlarm);
					}					
				}
			} catch (Exception e) {
				e.printStackTrace();
			} finally {
				try {
					Thread.sleep(10000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	private boolean attrTimeCheck(List<Alarm> subAlarms) {
		long prevTime = 0;
		
		for (Alarm alarm : subAlarms) {
			long curTime = alarm.getGrpAttrIncidentTime();
			if (prevTime > 0) {
				long interval = prevTime - curTime;
				if (interval > limitIntervalTime) {
					return false;
				}
			}
			prevTime = curTime;
		}
		return true;
	}
}
