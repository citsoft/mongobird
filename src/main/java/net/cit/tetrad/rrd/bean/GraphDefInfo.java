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

import java.util.List;

import net.cit.tetrad.model.Device;

public class GraphDefInfo {
	
	private List<Device> deviceGroup;
	private Device device;
	private String[] filters;
	private String[] graphLegend;
	private String graphType;													// AREA, LINE
	private String comment;
	private long startTime;
	private long endTime;
	private int width;
	private int height;
	private String fileName;
	private int limitValue;
	private int warningValue;
	private String axisTimeUnitDiv;
	private long step;
	private String consolFun;
	private String gubun = "";
	
	public List<Device> getDeviceGroup() {
		return deviceGroup;
	}
	public void setDeviceGroup(List<Device> deviceGroup) {
		this.deviceGroup = deviceGroup;
	}
	public Device getDevice() {
		return device;
	}
	public void setDevice(Device device) {
		this.device = device;
	}
	public String[] getFilters() {
		return filters;
	}
	public void setFilters(String[] filters) {
		this.filters = filters;
	}
	public String getGraphType() {
		return graphType;
	}
	public void setGraphType(String graphType) {
		this.graphType = graphType;
	}
	public String getComment() {
		return comment;
	}
	public void setComment(String comment) {
		this.comment = comment;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	public int getWidth() {
		return width;
	}
	public void setWidth(int width) {
		this.width = width;
	}
	public int getHeight() {
		return height;
	}
	public void setHeight(int height) {
		this.height = height;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getLimitValue() {
		return limitValue;
	}
	public void setLimitValue(int limitValue) {
		this.limitValue = limitValue;
	}
	public int getWarningValue() {
		return warningValue;
	}
	public void setWarningValue(int warningValue) {
		this.warningValue = warningValue;
	}
	public String getAxisTimeUnitDiv() {
		return axisTimeUnitDiv;
	}
	public void setAxisTimeUnitDiv(String axisTimeUnitDiv) {
		this.axisTimeUnitDiv = axisTimeUnitDiv;
	}
	public String[] getGraphLegend() {
		return graphLegend;
	}
	public void setGraphLegend(String[] graphLegend) {
		this.graphLegend = graphLegend;
	}
	public long getStep() {
		return step;
	}
	public void setStep(long step) {
		this.step = step;
	}
	public String getConsolFun() {
		return consolFun;
	}
	public void setConsolFun(String consolFun) {
		this.consolFun = consolFun;
	}
	public String getGubun() {
		return gubun;
	}
	public void setGubun(String gubun) {
		this.gubun = gubun;
	}
	
}
