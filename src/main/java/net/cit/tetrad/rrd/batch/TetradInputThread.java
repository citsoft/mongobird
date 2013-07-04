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
package net.cit.tetrad.rrd.batch;

import static net.cit.tetrad.common.ColumnConstent.RRD_SYNC_PERIOD;

import java.io.IOException;
import java.util.concurrent.ConcurrentMap;

import net.cit.tetrad.common.Config;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.rrd.service.TetradRrdDbService;
import net.cit.tetrad.rrd.utils.TetradRrdConfig;
import net.cit.tetrad.rrd.utils.TetradRrdDbPool;

import org.apache.log4j.Logger;
import org.rrd4j.core.RrdBackendFactory;
import org.rrd4j.core.RrdNioBackendFactory;

import com.mongodb.Mongo;

public class TetradInputThread extends Thread {
	
	private Logger logger = Logger.getLogger("process.rrd");

	private Mongo mongo;
	private TetradRrdDbService tetradRrdDbService;
	private int deviceIdx;
	private int logGenerationIntervalSeconds = 0;
	private int threadIndex = 0;
	
	TetradInputThread(int deviceIdx, int logGenerationIntervalSeconds, TetradRrdDbService tetradRrdDbService, int threadIndex) {
		super("Data input daemon");
		this.deviceIdx = deviceIdx;
		this.logGenerationIntervalSeconds = logGenerationIntervalSeconds;
		this.tetradRrdDbService = tetradRrdDbService;
		this.threadIndex = threadIndex;
	}

	public void run() {
		Device newDevice = null;
		Mongo mongo = null;
		
		try {
			
			for ( ; ; ) {				
				try {					
					newDevice = DeviceInMemory.getDeviceGroup().get(deviceIdx);
					if (!DeviceInMemory.isDeviceAlive(deviceIdx)) {
						logger.info(deviceIdx + " is deleted ");
//						Config.g_inc.remove(threadIndex);
						TetradRrdDbPool.releaseAll(deviceIdx);
						// thread number 초기화,
//						Config.initialG_Inc();
						break;
					}
					
//					settingSyncPeriod();
										
					if (isDeviceAlive(newDevice)) {
						logger.info(deviceIdx + ", Input Start!!!");
						
						mongo = MongoInMemory.getMongoGroup().get(newDevice.getIdx());
						logger.debug("logGenSeconds = " + logGenerationIntervalSeconds);
						tetradRrdDbService.insertTetradRrdDb(mongo, newDevice);
					}
				} catch (Exception e) {
					closeConnection();
					throwException();
				} finally {
					Thread.sleep(logGenerationIntervalSeconds * 1000L);
				}
			}
		} catch (Exception e) {
			closeConnection();
			throwException();
			logger.error(e, e);
		}
	}
	
	private void settingSyncPeriod() {		
		int inc = (Integer)Config.g_inc.get(threadIndex);
		if (inc == -1) {
			// 데몬 삭제, 추가시 재 정렬.. (threadnumber 가 이빨빠지는 현상을 방지하기 위해..)
			int newThreadIndex = tetradRrdDbService.getThreadIndex(deviceIdx);
			threadIndex = newThreadIndex == 0 ? 1 : newThreadIndex;
			Config.g_inc.put(threadIndex, 0);
			inc = 0;
		}
		
		if (inc < Integer.parseInt(TetradRrdConfig.getTetradRrdConfig(RRD_SYNC_PERIOD)) * threadIndex) {
			inc += logGenerationIntervalSeconds;
			Config.g_inc.put(threadIndex, inc);
		}
		
		if (inc == Integer.parseInt(TetradRrdConfig.getTetradRrdConfig(RRD_SYNC_PERIOD)) * threadIndex) {
			initialRrdDb();
			Config.g_inc.put(threadIndex, ++inc);
		}
	}
	
	private void initialRrdDb() {
		try {
			TetradRrdDbPool.releaseAll(deviceIdx);
			RrdNioBackendFactory.setSyncPeriod(Integer.parseInt(TetradRrdConfig.getTetradRrdConfig(RRD_SYNC_PERIOD))  * Config.totalThreadCount);
			logger.info("Device Id : " + deviceIdx + ", initial Rrd DB, Sync Period : " + RrdNioBackendFactory.getSyncPeriod());
		} catch (IOException e) {			
			e.printStackTrace();
			logger.error(e, e);
		}
	}
	
	private boolean isDeviceAlive(Device device) {
		return device == null ? false : device.isFinishedInitailRrd();
	}
	
//	private boolean isDeviceAlive() {
//		
//		DeviceInMemory.getDeviceGroup();
//		int deviceCode = device.getIdx();
//		
//		if (StringUtils.isNull(Code.device.getCode().getName(deviceCode))) {
//			return true;
//		} else {
//			return false;
//		}
//	}
	
	private void throwException() {
		try {
			throw new Exception();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex, ex);
		}
	}
	
	private void closeConnection() {
		try {
			if (mongo != null) 
				mongo.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			logger.error(ex, ex);
		}
	}
}
