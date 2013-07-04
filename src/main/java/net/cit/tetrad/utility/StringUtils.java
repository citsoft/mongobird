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
