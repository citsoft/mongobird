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

import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import net.cit.tetrad.common.Config;
import net.cit.tetrad.common.Utility;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.rrd.dao.DataAccessObjectForMongo;
import net.cit.tetrad.rrd.service.TetradRrdDbService;
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
				ConcurrentMap<Integer, Mongo> mongoGroup = MongoInMemory.getMongoGroup();
				Mongo mongo = mongoGroup.get(device.getIdx());
				
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
