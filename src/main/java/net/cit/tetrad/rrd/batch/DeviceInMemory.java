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
