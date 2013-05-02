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
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import net.cit.tetrad.model.Device;
import net.cit.tetrad.rrd.dao.DataAccessObjectForMongo;

public class DeviceInMemory {
	private DataAccessObjectForMongo dataAccessObjectForMongo;	
	public static ConcurrentMap<Integer, Device> deviceGroup = new ConcurrentHashMap<Integer, Device>(); 	
	
	public void setDataAccessObjectForMongo(
			DataAccessObjectForMongo dataAccessObjectForMongo) {
		this.dataAccessObjectForMongo = dataAccessObjectForMongo;
	}

	public void createDeviceGroup() {
		try {
			List<Device> devices = dataAccessObjectForMongo.readDeviceList();
			
			for (Device device : devices) {
				deviceGroup.putIfAbsent(device.getIdx(), device);
			}
		}	catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public static ConcurrentMap<Integer, Device> getDeviceGroup() {
		return deviceGroup;
	}
		
	public static void addDeviceIntoMap(Device device) {
		deviceGroup.putIfAbsent(device.getIdx(), device);
	}
	
	public static void updateDeviceMap(Device device) {
		deviceGroup.replace(device.getIdx(), device);
	}
	
	public static void deleteDeviceMap(int idx) {		
		deviceGroup.remove(idx);
	}
	
	public static boolean isDeviceAlive(int idx) {
		return deviceGroup.containsKey(idx) ? true : false;
	}
	
	public static boolean isFinishedInitialRrd(int idx) {
		Device device = deviceGroup.get(idx);
		return device.isFinishedInitailRrd();
	}
}
