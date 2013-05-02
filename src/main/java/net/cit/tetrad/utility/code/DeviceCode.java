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
import java.util.List;
import java.util.Locale;
import java.util.Map;

import org.springframework.data.mongodb.core.query.Query;

import net.cit.monad.Monad;
import net.cit.monad.Operations;
import net.cit.tetrad.model.Device;
import net.sf.json.JSONObject;

public class DeviceCode implements CodeInterface {

	private static Map<Integer, String> deviceCode;
	private static Map<String, Integer> deviceName;
	
	static {
		DeviceCode code = new DeviceCode();
		if(deviceCode == null || deviceName == null) code.initDeviceCode();
	}
	
	private void initDeviceCode(){
		Operations operations = (Operations)Monad.getBean("operations");
		deviceCode = new HashMap<Integer, String>();
		deviceName = new HashMap<String, Integer>();
		
		List<Device> deviceList = operations.find(getQuery(), Device.class);
		for(Device d : deviceList){
			deviceCode.put(d.getIdx(), d.getUid());
			deviceName.put(d.getUid(), d.getIdx());
		}
	}
	
	private Query getQuery(){
		Query query = new Query();
		query.fields().exclude("_id").include("idx").include("uid");
		return query;
	}
	
	public void updateCode(){
		initDeviceCode();
	}
	
	@SuppressWarnings("unchecked")
	public Map<Integer, String> getCodeMap(){
		return deviceCode;
	}
	
	public JSONObject getCodeJson(){
		JSONObject json = new JSONObject();
		json.putAll(deviceCode);
		return json;
	}
	
	public String getName(String code){
		return deviceCode.get(code);
	}
	
	public String getName(int code){
		String name = deviceCode.get(code);
		return (name==null || name.equals("")?"noName":name);
	}
	
	public int getCode(String name){
		return deviceName.get(name);
	}

	@Override
	public void updateCode(Locale locale) {
		// TODO Auto-generated method stub
		
	}
}
