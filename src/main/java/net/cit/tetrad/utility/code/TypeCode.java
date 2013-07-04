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

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import net.cit.tetrad.common.Config;
import net.sf.json.JSONObject;

public class TypeCode implements CodeInterface {

	private static String[] typeArr = Config.getConfig("type").split(",");
	private static Map<String, String> typeCode;
	
	static {
		TypeCode code = new TypeCode();
		if(typeCode == null) code.initTypeCode();
	}
	
	private void initTypeCode(){
		typeCode = new HashMap<String, String>();
		for(String str : typeArr){
			typeCode.put(str, str);
		}
	}
	
	public void updateCode() {
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getCodeMap() {
		return typeCode;
	}

	public JSONObject getCodeJson(){
		JSONObject json = new JSONObject();
		json.putAll(typeCode);
		return json;
	}
	
	public String getName(String code) {
		return typeCode.get(code);
	}

	public String getName(int code) {
		return null;
	}

	public int getCode(String name){
		return 0;
	}

	@Override
	public void updateCode(Locale locale) {
		// TODO Auto-generated method stub
		
	}
}
