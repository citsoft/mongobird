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
