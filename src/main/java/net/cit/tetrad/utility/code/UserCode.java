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

import net.cit.monad.Monad;
import net.cit.monad.Operations;
import net.cit.tetrad.model.User;
import net.sf.json.JSONObject;

import org.springframework.data.mongodb.core.query.Query;

public class UserCode implements CodeInterface {

	private static Map<Integer, String> userCode;
	private static Map<String, Integer> userName;
	
	static {
		UserCode code = new UserCode();
		if(userCode == null || userName == null) code.inituserCode();
	}
	
	private void inituserCode(){
		Operations operations = (Operations)Monad.getBean("operations");
		userCode = new HashMap<Integer, String>();
		userName = new HashMap<String, Integer>();
		
		List<User> userList = operations.find(getQuery(), User.class);
		for(User g : userList){
			userCode.put(g.getIdx(), g.getUid());
			userName.put(g.getUid(), g.getIdx());
		}
	}
	
	private Query getQuery(){
		Query query = new Query();
		query.fields().exclude("_id").include("idx").include("uid");
		return query;
	}
	
	public void updateCode(){
		inituserCode();
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, String> getCodeMap(){
		return userCode;
	}
	
	public JSONObject getCodeJson(){
		JSONObject json = new JSONObject();
		json.putAll(userCode);
		return json;
	}
	
	public String getName(String code){
		return userCode.get(code);
	}
	
	public String getName(int code){
		String name = userCode.get(code);
		return (name==null || name.equals("")?"noName":name);
	}
	
	public int getCode(String name){
		return userName.get(name);
	}

	@Override
	public void updateCode(Locale locale) {
		// TODO Auto-generated method stub
		
	}

}
