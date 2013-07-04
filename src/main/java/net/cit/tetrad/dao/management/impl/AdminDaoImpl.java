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
package net.cit.tetrad.dao.management.impl;

import static net.cit.tetrad.common.PropertiesNames.TYPE;

import java.util.ArrayList;
import java.util.List;

import net.cit.tetrad.dao.management.AdminDao;

public class AdminDaoImpl implements AdminDao {

	public List<Object> typeList(){
		List<Object> typeLst = new ArrayList<Object>();
		for(int i=0;i<TYPE.length;i++){
			typeLst.add(TYPE[i]);
		}
		return typeLst;
	}
}
