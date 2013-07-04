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

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.cit.tetrad.utility.CommonUtils;

import org.rrd4j.core.RrdDb;
import org.rrd4j.core.RrdDbPool;

public class TetradRrdDbPool {
	private static RrdDbPool pool = null;
	private static Map<String, RrdDb> rrdPool = new HashMap<String, RrdDb>();
	
	static {
		pool = RrdDbPool.getInstance();
	}

	public static RrdDb getRrdDb(String rrdPath) throws IOException {
		RrdDb rrdDb = rrdPool.get(rrdPath) ;
		if (rrdDb == null) {
			rrdDb = pool.requestRrdDb(rrdPath);
			rrdPool.put(rrdPath, rrdDb); 
		} 
		return rrdDb;
	}
	
	public static void setPoolCount(int count) {
		pool.setCapacity(count);
	}

	public static String[] getRrdDbPathes() {
		return pool.getOpenFiles();
	}
	
	public static void releaseAll() throws IOException {
		String[] pathes = TetradRrdDbPool.getRrdDbPathes();
		for (String path : pathes) {
			pool.release(getRrdDb(path));
		}
	}
	
	public static void releaseAll(int deviceCode) throws IOException {
		String[] pathes = TetradRrdDbPool.getRrdDbPathes();
		for (String path : pathes) {
			if (path.startsWith(CommonUtils.getDefaultRrdPath() + deviceCode)){
				pool.release(getRrdDb(path));
				rrdPool.remove(path);
			}
		}
	}
}
