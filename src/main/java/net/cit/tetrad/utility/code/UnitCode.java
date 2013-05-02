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

import java.util.Enumeration;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;

import net.sf.json.JSONObject;

public class UnitCode implements CodeInterface {

	private static ResourceBundle unitResource;
	private static Map<String, String> unitCode;
	
	static {
		UnitCode code = new UnitCode();
		if(unitResource == null) code.initUnitResource();
		if(unitCode == null) code.initUnitCode(Locale.getDefault());
	}
	
	private void initUnitResource(){
		unitResource = ResourceBundle.getBundle("properties/unitMsg",Locale.getDefault());
	}
	
	private void initUnitCode(Locale locale){
		unitCode = new HashMap<String, String>();
		Enumeration<String> keySet = unitResource.getKeys();
		while(keySet.hasMoreElements()){
			String key = keySet.nextElement();
			String value = unitResource.getString(key);
			String value2 = Message.getMessage(value, locale);
			unitCode.put(key, value2);
		}
	}
	
	public void updateCode() {
	}

	@SuppressWarnings("unchecked")
	public Map<String, String> getCodeMap() {
		return unitCode;
	}

	public JSONObject getCodeJson(){
		JSONObject json = new JSONObject();
		json.putAll(unitCode);
		return json;
	}
	
	public String getName(String code) {
		return unitResource.getString(code);
	}

	public String getName(int code) {
		return null;
	}

	public int getCode(String name){
		return 0;
	}

	@Override
	public void updateCode(Locale locale) {
		initUnitCode(locale);		
	}

}
