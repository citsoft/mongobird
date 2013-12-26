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

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import net.cit.tetrad.common.Config;
import net.cit.tetrad.common.Utility;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.rrd.dao.DataAccessObjectForMongo;
import net.cit.tetrad.rrd.service.TetradRrdDbService;
import net.cit.tetrad.rrd.utils.MongoWrapper;
import net.cit.tetrad.rrd.utils.RrdUtil;

import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.Mongo;

public class TetradRrdInitializer {
	
	private TetradRrdDbService tetradRrdDbService;
	private DataAccessObjectForMongo dataAccessObjectForMongo;

	public void setTetradRrdDbService(TetradRrdDbService tetradRrdDbService) {
		this.tetradRrdDbService = tetradRrdDbService;
	}
	
	public void setDataAccessObjectForMongo(DataAccessObjectForMongo dataAccessObjectForMongo) {
		this.dataAccessObjectForMongo = dataAccessObjectForMongo;
	}	
	
	public void installTotalRrdDb() throws Exception {
		try {
			tetradRrdDbService.createTotalRrdDb();			
		} catch (Exception e) { 
			e.printStackTrace();
			throw e;
		}
	}
	
	/**
	 * TetradRrd 생성 및 초기화 작업
	 * @throws Exception
	 */
	public void install() throws Exception {
		try {
			// 운영 관리할 데몬 리스트 취득
			List<Device> devices = dataAccessObjectForMongo.readDeviceList();
			
			for (Device device : devices) {
				// 운영 관리할 데몬에 대한 RrdDb 생성
				tetradRrdDbService.createTetradRrdDb(device);
				
				// 데몬에 생성되어 있는 데이터베이스 리스트 위득
				ConcurrentMap<Integer, MongoWrapper> mongoGroup = MongoInMemory.getMongoGroup();
				Mongo mongo = mongoGroup.get(device.getIdx()).getMongo();
				
				List<String> deviceNames = mongo.getDatabaseNames();
				for (String databaseName : deviceNames) {
					// 운영 관리할 데이터베이스에 대한 RrdDb 생성
					tetradRrdDbService.createTetradRrdDb(device, databaseName);
				}
			}
		} catch (Exception e) { 
			e.printStackTrace();
			throw e;
		}
	}

	public void install(Device device) throws Exception {
		
		try {		
			TetradInstallThread inputThread = new TetradInstallThread(tetradRrdDbService, device);
			inputThread.start();
			
		} catch (Exception e) { 
			e.printStackTrace();
			throw e;
		}
	}
	
	public void input() throws Exception {
		try {
			// 운영 관리할 장비 리스트 취득
			ConcurrentMap<Integer, Device> deviceGroup = DeviceInMemory.getDeviceGroup();
			
			// 로그 생성 주기
			int logGenerationIntervalSeconds = RrdUtil.readLogGenerationInterval();
			
			Set<Integer> keys = deviceGroup.keySet();
			Object[] objKeys = keys.toArray();
			if (objKeys != null) {
				Arrays.sort(objKeys, Utility.codeKeySort);
				int thredIndex = 1;
				for (int i=0; i<objKeys.length; i++) {
					int deviceIdx = (Integer)objKeys[i];
					Config.g_inc.put(thredIndex, 0);
					TetradInputThread inputThread = new TetradInputThread(deviceIdx, logGenerationIntervalSeconds, tetradRrdDbService, thredIndex);
					inputThread.start();
					thredIndex++;
					Config.totalThreadCount++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} 
	}
	
	public void input(int deviceIdx) throws Exception {
		try {
			Config.initialG_Inc();
			// 로그 생성 주기
			int logGenerationIntervalSeconds = RrdUtil.readLogGenerationInterval();
			Config.totalThreadCount = dataAccessObjectForMongo.getCount(new Query(), Device.class);
			int threadIndex = Config.totalThreadCount;
			Config.g_inc.put(threadIndex, 0);
			TetradInputThread inputThread = new TetradInputThread(deviceIdx, logGenerationIntervalSeconds, tetradRrdDbService, threadIndex);
			inputThread.start();

		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		} 
	}
}
