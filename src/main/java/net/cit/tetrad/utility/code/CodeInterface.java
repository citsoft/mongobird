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

import java.util.Locale;
import java.util.Map;

import net.sf.json.JSONObject;

public interface CodeInterface {

	public void updateCode();
	public void updateCode(Locale locale);
	public <K, V> Map<K, V> getCodeMap();
	public JSONObject getCodeJson();
	public String getName(String code);
	public String getName(int code);
	public int getCode(String name);
	
}
