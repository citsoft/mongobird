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
package net.cit.tetrad.model;

import com.thoughtworks.xstream.annotations.XStreamAlias;
import com.thoughtworks.xstream.annotations.XStreamAsAttribute;

@XStreamAlias("xstream.datasourceNames")
public class Critical {

	private int deviceCode;
	private int idx=0;
	private String reg_date;
	private String up_date;
	private int groupCode;
	
	@XStreamAsAttribute 
	private String type;
	private int criticalvalue;	
	private int warningvalue;	
	private int infovalue;
	private String unit;
	private String version; //monadVersion 구분
	private String deviceType;

	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public int getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(int groupCode) {
		this.groupCode = groupCode;
	}	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getReg_date() {
		return reg_date;
	}
	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}
	public String getUp_date() {
		return up_date;
	}
	public void setUp_date(String up_date) {
		this.up_date = up_date;
	}
	public String getUnit() {
		return unit;
	}
	public void setUnit(String unit) {
		this.unit = unit;
	}
	public int getCriticalvalue() {
		return criticalvalue;
	}
	public void setCriticalvalue(int criticalvalue) {
		this.criticalvalue = criticalvalue;
	}
	public int getWarningvalue() {
		return warningvalue;
	}
	public void setWarningvalue(int warningvalue) {
		this.warningvalue = warningvalue;
	}
	public int getInfovalue() {
		return infovalue;
	}
	public void setInfovalue(int infovalue) {
		this.infovalue = infovalue;
	}
	public int getDeviceCode() {
		return deviceCode;
	}
	public void setDeviceCode(int deviceCode) {
		this.deviceCode = deviceCode;
	}
	public String getVersion() {
		return version;
	}
	public void setVersion(String version) {
		this.version = version;
	}
	public String getDeviceType() {
		return deviceType;
	}
	public void setDeviceType(String deviceType) {
		this.deviceType = deviceType;
	}	

}
