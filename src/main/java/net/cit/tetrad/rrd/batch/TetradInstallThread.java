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

import java.util.List;
import java.util.concurrent.ConcurrentMap;

import net.cit.tetrad.model.Device;
import net.cit.tetrad.resource.ManagementResource;
import net.cit.tetrad.rrd.service.TetradRrdDbService;

import org.apache.log4j.Logger;

import com.mongodb.Mongo;

public class TetradInstallThread extends Thread {
	
	private Logger logger = Logger.getLogger(this.getClass());

	private Mongo mongo;
	private TetradRrdDbService tetradRrdDbService;
	private Device device;
	
	TetradInstallThread(TetradRrdDbService tetradRrdDbService, Device device) {
		this.tetradRrdDbService = tetradRrdDbService;
		this.device = device;
	}

	public void run() {

		try {
			
			// 운영 관리할 데몬에 대한 RrdDb 생성
			tetradRrdDbService.createTetradRrdDb(device);

			// 데몬에 생성되어 있는 데이터베이스 리스트 위득
			ConcurrentMap<Integer, Mongo> mongoGroup = MongoInMemory.getMongoGroup();
			mongo = mongoGroup.get(device.getIdx());
			
			List<String> deviceNames = mongo.getDatabaseNames();
			for (String databaseName : deviceNames) {
				// 운영 관리할 데이터베이스에 대한 RrdDb 생성
				tetradRrdDbService.createTetradRrdDb(device, databaseName);
			}
			
			device.setFinishedInitailRrd(true);
			DeviceInMemory.updateDeviceMap(device);
			
			//초기화 완료 상태 정보를 db 에 갱신
			ManagementResource managementResource=new ManagementResource();
			managementResource.update(device);
			
		} catch (InterruptedException e) {
			closeConnection();
			logger.info("Thread interrupted.");
		} catch (Exception e) {
			closeConnection();
			throwException();
		}
	}
	
	
	
	private void throwException() {
		try {
			throw new Exception();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
	private void closeConnection() {
		try {
			if (mongo != null) 
				mongo.close();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
	
}
