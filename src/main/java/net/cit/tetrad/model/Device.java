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

import net.cit.monad.cipher.AnnotationCipherType;
import net.cit.tetrad.utility.StringUtils;

public class Device{

	@AnnotationCipherType(type=true)
	private String uid="";
	private int idx=0;	
	private String reg_date="";
	private String up_date="";
	@AnnotationCipherType(type=true)
	private String type="";
	private int groupCode=0;	
	@AnnotationCipherType(type=true)
	private String ip="";
	@AnnotationCipherType(type=true)
	private String port="";
	private int memorysize=0;	
	private int hddsize=0;
	@AnnotationCipherType(type=true)
	private String authUser="";
	@AnnotationCipherType(type=true)
	private String authPasswd="";
	private boolean isFinishedInitailRrd = false;
	
	public String mk(String str){
		String change = StringUtils.MD5(str);
		return change;
	}
	
	public String getAuthUser() {
		return authUser.length()>64 && authUser.startsWith(mk(authUser.substring(64)))?authUser.substring(64):"";
	}

	public void setAuthUser(String authUser) {
		this.authUser = mk(authUser)+authUser;
	}

	public String getAuthPasswd() {
		return authPasswd.length()>64 && authPasswd.startsWith(mk(authPasswd.substring(64)))?authPasswd.substring(64):"";
	}

	public void setAuthPasswd(String authPasswd) {
		this.authPasswd = mk(authPasswd)+authPasswd;
	}

	public String getType() {
		return type.length()>64 && type.startsWith(mk(type.substring(64)))?type.substring(64):"";
	}
	public void setType(String type) {
		this.type = mk(type)+type;
	}
	public String getIp() {
		return ip.length()>64 && ip.startsWith(mk(ip.substring(64)))?ip.substring(64):"";
	}
	public void setIp(String ip) {
		this.ip = mk(ip)+ip;
	}
	public String getPort() {
		return port.length()>64 && port.startsWith(mk(port.substring(64)))?port.substring(64):"";
	}
	public void setPort(String port) {
		this.port = mk(port)+port;
	}
	public String getUid() {
		return uid.length()>64 && uid.startsWith(mk(uid.substring(64)))?uid.substring(64):"";
	}
	public void setUid(String uid) {
		this.uid = mk(uid)+uid;
	}
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
	public int getMemorysize() {
		return memorysize;
	}
	public void setMemorysize(int memorysize) {
		this.memorysize = memorysize;
	}
	public int getHddsize() {
		return hddsize;
	}
	public void setHddsize(int hddsize) {
		this.hddsize = hddsize;
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
	public boolean isFinishedInitailRrd() {
		return isFinishedInitailRrd;
	}
	public void setFinishedInitailRrd(boolean isFinishedInitailRrd) {
		this.isFinishedInitailRrd = isFinishedInitailRrd;
	}	
}
