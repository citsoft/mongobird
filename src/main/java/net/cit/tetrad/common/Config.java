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

import java.util.HashMap;
import java.util.Iterator;
import java.util.Locale;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.Set;

public class Config {
	static ResourceBundle res = ResourceBundle.getBundle("properties/tetrad",Locale.getDefault());

	public static String getConfig(String value){   
		return res.getString(value);   
	}
		
	public static int totalThreadCount = 0;
	public static Map g_inc = new HashMap<Integer, Integer>();
	
	public static void initialG_Inc() {
		Map<Integer,Integer> map = Config.g_inc;
		Set<Integer> set = map.keySet();
		Iterator<Integer> it = set.iterator();
		while(it.hasNext()) {
			int key = it.next();
			map.put(key, -1);
		}
	}
	
	/**라이센스 관련 전역 변수 추가 */
	public static String LICENSEKEY = "";
	public static String LICENSETYPE = "";
	
	public static String DBNAME_PATTERN = ".*";
}
