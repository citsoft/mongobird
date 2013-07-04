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
package net.cit.tetrad.rrd.utils;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class StringUtil {
	
	public static boolean isNull(String str) {
		return (str == null || str.trim().length() < 1);
	}
	
	public static String convNull(String str, String def) {
		if (isNull(str))
			return def;
		return str;
	}
	
    public static String leftSubstring(String str, int size) {
    	if (size <= 0)
    		return "";
    	else if (size > str.length()) 
    		return str;
    	else {
    		return str.substring(0, size);
    	}
    }
    
    public static String rightSubstring(String str, int size) {
    	if (size <= 0)
    		return "";
    	else if (size > str.length()) 
    		return str;
    	else {
    		return str.substring(str.length() - size, str.length());
    	}
    }
	
	public static String decodeMD5(String source) throws NoSuchAlgorithmException {
		MessageDigest md5 = MessageDigest.getInstance("MD5");
		md5.update(source.getBytes());
		byte[] md5bytes = md5.digest();
		StringBuffer sb = new StringBuffer();
		for (int i = 0; i < md5bytes.length; i++) {
			sb.append(Integer.toString((md5bytes[i] & 0xf0) >> 4, 16));
			sb.append(Integer.toString(md5bytes[i] & 0x0f, 16));
		}
		String decodeResult = sb.toString();
		return decodeResult;
	}	

	public static String rightSpace(String str, int size) {
		int strLength = str.length();
		if (strLength < size) {
			for (int i=0; i<size-strLength; i++) {
				str += " ";
			}
		} 
        return str;    
    }
}
