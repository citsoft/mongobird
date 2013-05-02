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
package net.cit.tetrad.rrd.bean;

import org.springframework.data.mongodb.core.index.Indexed;

public class DbStatus {
	
	@Indexed
	private String regtime;
	private int groupCode;
	private int deviceCode;
	private String type;
	private String db;
	private double collections;
	private double objects;
	private double avgObjSize;
	private double dataSize;
	private double diff_dataSize;
	private double storageSize;
	private double numExtents;
	private double indexes;
	private double indexSize;
	private double diff_indexSize;
	private double fileSize;
	private double nsSizeMB;
	
	private double locks_timeLockedMicros_r;
	private double locks_timeLockedMicros_w;
	private double locks_timeAcquiringMicros_r;
	private double locks_timeAcquiringMicros_w;
		
	private double recordStats_accessesNotInMemory;
	private double recordStats_pageFaultExceptionsThrown;
	
	private double ok;
	
	public String getRegtime() {
		return regtime;
	}
	public void setRegtime(String regtime) {
		this.regtime = regtime;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getDb() {
		return db;
	}
	public void setDb(String db) {
		this.db = db;
	}
	public double getCollections() {
		return collections;
	}
	public void setCollections(double collections) {
		this.collections = collections;
	}
	public double getObjects() {
		return objects;
	}
	public void setObjects(double objects) {
		this.objects = objects;
	}
	public double getAvgObjSize() {
		return avgObjSize;
	}
	public void setAvgObjSize(double avgObjSize) {
		this.avgObjSize = avgObjSize;
	}
	public double getDataSize() {
		return dataSize;
	}
	public void setDataSize(double dataSize) {
		this.dataSize = dataSize;
	}
	public double getStorageSize() {
		return storageSize;
	}
	public void setStorageSize(double storageSize) {
		this.storageSize = storageSize;
	}
	public double getNumExtents() {
		return numExtents;
	}
	public void setNumExtents(double numExtents) {
		this.numExtents = numExtents;
	}
	public double getIndexes() {
		return indexes;
	}
	public void setIndexes(double indexes) {
		this.indexes = indexes;
	}
	public double getIndexSize() {
		return indexSize;
	}
	public void setIndexSize(double indexSize) {
		this.indexSize = indexSize;
	}
	public double getFileSize() {
		return fileSize;
	}
	public void setFileSize(double fileSize) {
		this.fileSize = fileSize;
	}
	public double getNsSizeMB() {
		return nsSizeMB;
	}
	public void setNsSizeMB(double nsSizeMB) {
		this.nsSizeMB = nsSizeMB;
	}
	public double getOk() {
		return ok;
	}
	public void setOk(double ok) {
		this.ok = ok;
	}
	public int getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(int groupCode) {
		this.groupCode = groupCode;
	}
	public int getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(int deviceCode) {
		this.deviceCode = deviceCode;
	}	
	public double getLocks_timeLockedMicros_r() {
		return locks_timeLockedMicros_r;
	}
	public void setLocks_timeLockedMicros_r(double locks_timeLockedMicros_r) {
		this.locks_timeLockedMicros_r = locks_timeLockedMicros_r;
	}
	public double getLocks_timeLockedMicros_w() {
		return locks_timeLockedMicros_w;
	}
	public void setLocks_timeLockedMicros_w(double locks_timeLockedMicros_w) {
		this.locks_timeLockedMicros_w = locks_timeLockedMicros_w;
	}
	public double getLocks_timeAcquiringMicros_r() {
		return locks_timeAcquiringMicros_r;
	}
	public void setLocks_timeAcquiringMicros_r(double locks_timeAcquiringMicros_r) {
		this.locks_timeAcquiringMicros_r = locks_timeAcquiringMicros_r;
	}
	public double getLocks_timeAcquiringMicros_w() {
		return locks_timeAcquiringMicros_w;
	}
	public void setLocks_timeAcquiringMicros_w(double locks_timeAcquiringMicros_w) {
		this.locks_timeAcquiringMicros_w = locks_timeAcquiringMicros_w;
	}
	public double getRecordStats_accessesNotInMemory() {
		return recordStats_accessesNotInMemory;
	}
	public void setRecordStats_accessesNotInMemory(
			double recordStats_accessesNotInMemory) {
		this.recordStats_accessesNotInMemory = recordStats_accessesNotInMemory;
	}
	public double getRecordStats_pageFaultExceptionsThrown() {
		return recordStats_pageFaultExceptionsThrown;
	}
	public void setRecordStats_pageFaultExceptionsThrown(
			double recordStats_pageFaultExceptionsThrown) {
		this.recordStats_pageFaultExceptionsThrown = recordStats_pageFaultExceptionsThrown;
	}
	public double getDiff_dataSize() {
		return diff_dataSize;
	}
	public void setDiff_dataSize(double diff_dataSize) {
		this.diff_dataSize = diff_dataSize;
	}
	public double getDiff_indexSize() {
		return diff_indexSize;
	}
	public void setDiff_indexSize(double diff_indexSize) {
		this.diff_indexSize = diff_indexSize;
	}	
}
