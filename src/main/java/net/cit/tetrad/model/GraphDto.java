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


public class GraphDto {
	String dsname;
	Object statusListObj;
	String fileName;
	int statusSize;
	
	public String getDsname() {
		return dsname;
	}
	public void setDsname(String dsname) {
		this.dsname = dsname;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public int getStatusSize() {
		return statusSize;
	}
	public void setStatusSize(int statusSize) {
		this.statusSize = statusSize;
	}
	public Object getStatusListObj() {
		return statusListObj;
	}
	public void setStatusListObj(Object statusListObj) {
		this.statusListObj = statusListObj;
	}
}
