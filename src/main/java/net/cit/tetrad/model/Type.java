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

public class Type {
	private String name;
	private String groupList;

	public Type(String name, String groupList) {
		super();
		this.name = name;
		this.groupList = groupList;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getGroupList() {
		return groupList;
	}

	public void setGroupList(String groupList) {
		this.groupList = groupList;
	}

	@Override
	public String toString() {
		return "Type [name=" + name + ", groupList=" + groupList + "]";
	}
}
