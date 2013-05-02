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

public class TotalInfo {

	private double totalDbDataSize;
	private double totalDbIndexSize;
	private double totalGlobalLockTime;
	private double maximumPageFaults;
	
	public double getTotalGlobalLockTime() {
		return totalGlobalLockTime;
	}
	public void setTotalGlobalLockTime(double totalGlobalLockTime) {
		this.totalGlobalLockTime = totalGlobalLockTime;
	}
	
	public double getTotalDbDataSize() {
		return totalDbDataSize;
	}
	public void setTotalDbDataSize(double totalDbDataSize) {
		this.totalDbDataSize = totalDbDataSize;
	}
	public double getTotalDbIndexSize() {
		return totalDbIndexSize;
	}
	public void setTotalDbIndexSize(double totalDbIndexSize) {
		this.totalDbIndexSize = totalDbIndexSize;
	}
	public double getMaximumPageFaults() {
		return maximumPageFaults;
	}
	public void setMaximumPageFaults(double maximumPageFaults) {
		this.maximumPageFaults = maximumPageFaults;
	}	
}
