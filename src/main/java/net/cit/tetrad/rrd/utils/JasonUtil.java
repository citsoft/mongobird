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
package net.cit.tetrad.rrd.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;

public class JasonUtil {
	public static Map<String, Object> getEntryValue(Object object, String prefix, Map<String, Object> serverStatus) throws Exception {
		try {
			if (object instanceof BasicDBObject) {
				Set<Entry<String, Object>> setData = ((BasicDBObject)object).entrySet();
				Iterator<Entry<String, Object>> i = setData.iterator();
				
				while (i.hasNext()) {
					@SuppressWarnings("rawtypes")
					Map.Entry entry = (Map.Entry)i.next();
					String sKey = (String)entry.getKey();
					Object sValue = entry.getValue();
					if (sValue instanceof BasicDBObject) {
						serverStatus = getEntryValue(sValue, prefix + sKey + "_", serverStatus);
					} else if (sValue instanceof BasicDBList) {
						String key = new String();
						key = prefix + sKey;
						List<Map<String, Object>> pValue = new ArrayList<Map<String, Object>>();
						if (checklll((BasicDBList)sValue)) {
							serverStatus.put(key, sValue);
						} else {
							pValue = getListFromEntryValue((BasicDBList)sValue);
							serverStatus.put(key, pValue);
						}
					} else {
						String key = new String();
						key = prefix + sKey;
						serverStatus.put(key, sValue);
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return serverStatus;
	}
	
	public static boolean checklll(BasicDBList basicDBList) {
		for (Object object : basicDBList) {
			if (object instanceof String) {
				return true;
			} else {
				return false;
			}
		}
		return true;
	}
	
	public static List<Map<String, Object>> getListFromEntryValue(BasicDBList basicDBList) throws Exception {
		List<Map<String, Object>> dbLists = new ArrayList<Map<String, Object>>();
		
		try {
			for (Object object : basicDBList) {
				Map<String, Object> oneDb = new HashMap<String, Object>(); 
				oneDb = getEntryValue(object, "", oneDb);
				dbLists.add(oneDb);
			}	
		} catch (Exception e) {
			e.printStackTrace();
			throw e;
		}
		
		return dbLists;
	}
	
}
