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

import java.util.List;

import org.springframework.data.mongodb.core.index.Indexed;

public class Alarm {

	@Indexed(unique=true)
	private int idx	= 0;
	private int deviceCode;	
	private String type;
	private int groupCode;	
	private String ip;
	private int port = 0;	
	private String cri_type;	
	private String memo;
	private double figure = 0;	
	private double cri_value = 0;	
	private double real_figure = 0;	
	private double real_cri_value = 0;	
	private int confirm	= 0;
	private int userCode;
	private String reg_date;
	private String up_date;
	private String reg_time;
	private String up_time;
	private int alarm;	
	private int count=0;
	private String groupBind;
	private List<Alarm> subLst;
	private int groupCnt;
	private long grpAttrIncidentTime;
	
	public int getIdx() {
		return idx;
	}
	public void setIdx(int idx) {
		this.idx = idx;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getIp() {
		return ip;
	}
	public void setIp(String ip) {
		this.ip = ip;
	}
	public int getPort() {
		return port;
	}
	public void setPort(int port) {
		this.port = port;
	}
	public String getCri_type() {
		return cri_type;
	}
	public void setCri_type(String cri_type) {
		this.cri_type = cri_type;
	}
	public String getMemo() {
		return memo;
	}
	public void setMemo(String memo) {
		this.memo = memo;
	}
	public double getFigure() {
		return figure;
	}
	public void setFigure(double figure) {
		this.figure = figure;
	}
	public double getCri_value() {
		return cri_value;
	}
	public void setCri_value(double cri_value) {
		this.cri_value = cri_value;
	}
	public int getCount() {
		return count;
	}
	public void setCount(int count) {
		this.count = count;
	}
	public int getAlarm() {
		return alarm;
	}
	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}
	public int getConfirm() {
		return confirm;
	}
	public void setConfirm(int confirm) {
		this.confirm = confirm;
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
	public String getReg_time() {
		return reg_time;
	}
	public void setReg_time(String reg_time) {
		this.reg_time = reg_time;
	}
	public String getUp_time() {
		return up_time;
	}
	public void setUp_time(String up_time) {
		this.up_time = up_time;
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
	public int getUserCode() {
		return userCode;
	}
	public void setUserCode(int userCode) {
		this.userCode = userCode;
	}
	public double getReal_figure() {
		return real_figure;
	}
	public void setReal_figure(double real_figure) {
		this.real_figure = real_figure;
	}
	public double getReal_cri_value() {
		return real_cri_value;
	}
	public void setReal_cri_value(double real_cri_value) {
		this.real_cri_value = real_cri_value;
	}	
	public String getGroupBind(){
		return groupBind;
	}
	public void setGroupBind(String groupBind){
		this.groupBind = groupBind;
	}
	public List<Alarm> getSubLst(){
		return subLst;
	}
	public void setSubLst(List<Alarm> subLst){
		this.subLst = subLst;
	}	
	public int getGroupCnt() {
		return groupCnt;
	}
	public void setGroupCnt(int groupCnt) {
		this.groupCnt = groupCnt;
	}
	public long getGrpAttrIncidentTime() {
		return grpAttrIncidentTime;
	}
	public void setGrpAttrIncidentTime(long grpAttrIncidentTime) {
		this.grpAttrIncidentTime = grpAttrIncidentTime;
	}
	
}
