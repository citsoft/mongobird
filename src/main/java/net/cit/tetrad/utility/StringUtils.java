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
package net.cit.tetrad.utility;

import java.util.Locale;
import java.util.ResourceBundle;

import net.cit.tetrad.dao.management.impl.ManagementDaoImpl;
import net.cit.monad.cipher.CipherUtils;

public class StringUtils {
	
	private static ResourceBundle Resource = ResourceBundle.getBundle("monad",Locale.getDefault());
	private static CipherUtils cipher = new CipherUtils(Resource.getString("cipher.keyAlgorithm"), Resource.getString("cipher.cipherAlgorithm"), Resource.getString("cipher.keyString"));
	private static ManagementDaoImpl md = new ManagementDaoImpl();
	
	public static boolean isNull(String str) {
		return str == null || str.isEmpty() ? true : false;
	}
	
	public static boolean isNull(int i) {
		return i == 0 ? true : false;
	}
	
	public static String getDefaultValueIfNull(String str, String defaultValue) {
		if (isNull(str)) {
			return defaultValue;
		} else {
			return str;
		}
	}
	
	public static String getEncStr(String str){
		  String rtn = "";
		try {
			rtn = cipher.encrypt(MD5(str)+str);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		  return rtn;
		}
	
	public static String MD5(String str){
		String change = md.makePasswd(str);
		return change;
	}
}
