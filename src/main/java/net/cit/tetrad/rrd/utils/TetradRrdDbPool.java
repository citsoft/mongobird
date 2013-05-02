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
