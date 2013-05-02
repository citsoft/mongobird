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
