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
package net.cit.tetrad.common;

import net.cit.tetrad.common.Config;

/**
 * properties 파일 read 클래스
 * @author jaemin
 *
 */
public abstract class PropertiesNames {
	
	public static final String XML_PATH=Config.getConfig("xml_path");
	public static final String RELEASEVERSIONINFO =Config.getConfig("releaseVersionInfo");
	public static final int MAINRELOADTIMEMILLI=Integer.parseInt(Config.getConfig("mainReloadTimeMilli"));
	
	public static final String[] TABLENAME=Config.getConfig("tablename").split(",");
	public static final String[] TYPE=Config.getConfig("type").split(",");
	
	public static String SMS_SCRIPT_PATH=Config.getConfig("sms_script_path");
}
