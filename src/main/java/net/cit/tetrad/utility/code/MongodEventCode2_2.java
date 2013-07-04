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
package net.cit.tetrad.utility.code;

import java.util.Arrays;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

import net.sf.json.JSONObject;

public class MongodEventCode2_2 implements CodeInterface {

	private static ResourceBundle eventResource;
	private static SortedMap<String, String> eventCode;
	
	static {
		MongodEventCode2_2 code = new MongodEventCode2_2();
		if(eventResource == null) code.initEventResource();
		if(eventCode == null) code.initEventCode(Locale.getDefault());
	}
	
	private void initEventResource(){
		eventResource = ResourceBundle.getBundle("properties/mongodEventMsg2_2",Locale.getDefault());
	}
	
	private void initEventCode(Locale locale){
		eventCode = new TreeMap<String, String>();
		// 정렬
		Set<String> set = eventResource.keySet();
		Object[] keys = set.toArray();
		Arrays.sort(keys);
		for (Object keySet : keys) {
			String key = (String)keySet;
			String value = eventResource.getString(key);
			String value2 = Message.getMessage(value, locale);
			eventCode.put(key, value2);
		}
	}
	
	public void updateCode() {
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getCodeMap() {
		return eventCode;
	}

	public JSONObject getCodeJson(){
		JSONObject json = new JSONObject();
		
		json.putAll(eventCode);
		return json;
	}
	
	public String getName(String code) {
		return eventResource.getString(code);
	}

	public String getName(int code) {
		return null;
	}

	public int getCode(String name){
		return 0;
	}

	@Override
	public void updateCode(Locale locale) {
		initEventCode(locale);
	}
}
