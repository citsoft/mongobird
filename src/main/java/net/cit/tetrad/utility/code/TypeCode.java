/*******************************************************************************
 * "mongobird" is released under a dual license model designed to developers 
 * and commercial deployment.
 * 
 * For using OEMs(Original Equipment Manufacturers), ISVs(Independent Software
 * Vendor), ISPs(Internet Service Provider), VARs(Value Added Resellers) 
 * and another distributors, or for using include changed issue
 * (modify / application), it must have to follow the Commercial License policy.
 * To check the Commercial License Policy, you need to contact Cardinal Info.Tech.Co., Ltd.
 * (http://www.citsoft.net)
 *  *
 * If not using Commercial License (Academic research or personal research),
 * it might to be under AGPL policy. To check the contents of the AGPL terms,
 * please see "http://www.gnu.org/licenses/"
 ******************************************************************************/
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
