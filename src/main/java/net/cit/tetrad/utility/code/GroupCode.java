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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;

import net.cit.monad.Monad;
import net.cit.monad.Operations;
import net.cit.tetrad.model.Group;
import net.sf.json.JSONObject;

public class GroupCode implements CodeInterface {

	private static Map<Integer, String> groupCode;
	private static Map<String, Integer> groupName;
	
	static {
		GroupCode code = new GroupCode();
		if(groupCode == null || groupName == null) code.initGroupCode();
	}
	
	private void initGroupCode(){
		Operations operations = (Operations)Monad.getBean("operations");
		groupCode = new HashMap<Integer, String>();
		groupName = new HashMap<String, Integer>();
		
		List<Group> groupList = operations.find(getQuery(), Group.class);
		for(Group g : groupList){
			groupCode.put(g.getIdx(), g.getUid());
			groupName.put(g.getUid(), g.getIdx());
		}
	}
	
	private Query getQuery(){
		Query query = new Query();
		query.fields().exclude("_id").include("idx").include("uid");
		return query;
	}
	
	public void updateCode(){
		initGroupCode();
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, String> getCodeMap(){
		return groupCode;
	}
	
	public JSONObject getCodeJson(){
		JSONObject json = new JSONObject();
		json.putAll(groupCode);
		return json;
	}
	
	public String getName(String code){
		return groupCode.get(code);
	}
	
	public String getName(int code){
		String name = groupCode.get(code);
		return (name==null || name.equals("")?"noName":name);
	}
	
	public int getCode(String name){
		return groupName.get(name);
	}

	@Override
	public void updateCode(Locale locale) {
		// TODO Auto-generated method stub
		
	}
}
