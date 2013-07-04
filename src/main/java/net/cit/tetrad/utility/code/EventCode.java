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
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

public class EventCode implements CodeInterface {

	private static ResourceBundle eventResource;
	private static Map<String, String> eventCode;

	static {
		EventCode code = new EventCode();
		if(eventResource == null) code.initEventResource();
		if(eventCode == null) code.initEventCode(Locale.getDefault());
	}
	
	private void initEventResource(){
		eventResource = ResourceBundle.getBundle("properties/eventMsg",Locale.getDefault());
	}
	
	private void initEventCode(Locale locale){
		eventCode = new HashMap<String, String>();
		Enumeration<String> keySet = eventResource.getKeys();
		while(keySet.hasMoreElements()){
			String key = keySet.nextElement();
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
