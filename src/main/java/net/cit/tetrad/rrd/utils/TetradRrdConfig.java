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
package net.cit.tetrad.rrd.utils;

import java.util.Locale;
import java.util.ResourceBundle;

public class TetradRrdConfig {
	
	static ResourceBundle resourceBundle = ResourceBundle.getBundle("tetrad_rrd", Locale.getDefault());
	static ResourceBundle monadResourceBundle = ResourceBundle.getBundle("monad", Locale.getDefault());
	
	public static String getTetradRrdConfig(String value) {
		return resourceBundle.getString(value);
	}
	
	public static String getMonadConfig(String value) {
		return monadResourceBundle.getString(value);
	}
	
}
