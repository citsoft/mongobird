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
