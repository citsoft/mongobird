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
