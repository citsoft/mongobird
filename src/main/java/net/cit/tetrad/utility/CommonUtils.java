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

import static net.cit.tetrad.common.ColumnConstent.RRD_SUFFIXPATH;

import java.io.File;

import net.cit.tetrad.model.Device;
import net.cit.tetrad.rrd.utils.TetradRrdConfig;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class CommonUtils {
	public static ApplicationContext appCtx = new ClassPathXmlApplicationContext ("applicationContext_rrd.xml");
	public static String pathDelimiter;
		
	static {
		String delimiterType =  TetradRrdConfig.getTetradRrdConfig("delimiter_type");
		if (delimiterType.equals("win")) {
			pathDelimiter = "\\";
		} else {
			pathDelimiter = "/";
		}
	}
	
	public static String makeRrdDbPath(Device device, String dsName) {
		return makeRrdDbPath(device, "", dsName);
	}
	
	public static String makeRrdDbPath(int deviceCode, String dsName) {
		return makeRrdDbPath(deviceCode, "", dsName);
	}
		
	public static String makeRrdDbPath(Device device, String databaseName, String dsName) {
		return makeRrdDbPath(device.getIdx(), databaseName, dsName);
	}
	
	public static String makeRrdDbPath(int deviceCode, String databaseName, String dsName) {
		String rrdPath = getCommonRrdDbPath(deviceCode, databaseName);
		
		File file = new File(rrdPath);
		if (!file.exists()) file.mkdirs();
		
		rrdPath += dsName + ".rrd";
		
		return rrdPath;
	}
		
	private static String getCommonRrdDbPath(int deviceCode, String databaseName) {
		String defaultPath = getDefaultRrdPath();
		defaultPath += deviceCode + pathDelimiter;
		if (!StringUtils.isNull(databaseName)) defaultPath += databaseName + pathDelimiter;
		
		return defaultPath;
	}
	
	public static String getDefaultRrdPath() {
		String rrdPath = TetradRrdConfig.getTetradRrdConfig("rrd_path");
		
		File file = new File(rrdPath);
		if (!file.exists())
			file.mkdirs();
		
		return rrdPath;
	}
	
	public static String getDefaultRrdImgPath() {
		String rrdImgPath = TetradRrdConfig.getTetradRrdConfig("img_path");
		
		File file = new File(rrdImgPath);
		if (!file.exists())
			file.mkdirs();
		
		return rrdImgPath;
	}
	
	public static String getDefaultFileName(String fileName) {
		if(fileName.indexOf("../")>=0||fileName.indexOf("./")>=0){
			fileName = "../wrongRequest.html";
		}
		return fileName;
	}
		
	public static String getRrdDbPath(Device device, String databaseName, String dsName) {
		return getRrdDbPath(device.getIdx(), databaseName, dsName);
	}
	
	public static String getRrdDbPath(int deviceCode, String databaseName, String dsName) {
		String defaultPath = getCommonRrdDbPath(deviceCode, databaseName);
		defaultPath += dsName + ".rrd";
		
		return defaultPath;
	}
	
//	private static String getCommonRrdDbPath(int deviceCode, String databaseName) {
//		return getCommonRrdDbPath(deviceCode, databaseName);
//	}
	
	public static String getRrdDbPath(String dsName) {
		return getDefaultRrdPath() + dsName + ".rrd";
	}
	
	public static String getRrdDbPath(Device device, String dsName) {
		return getRrdDbPath(device, "", dsName);
	}
	
	public static String getRrdDbPath(int deviceCode, String dsName) {
		return getRrdDbPath(deviceCode, "", dsName);
	}
	
	
	public static String getDsnameInRrdPath(String rrdPathName) {
		String dsName = rrdPathName;
		for (int i=0; i<RRD_SUFFIXPATH.length; i++) {
			if (rrdPathName.contains(RRD_SUFFIXPATH[i])) {
				dsName = rrdPathName.replaceAll(RRD_SUFFIXPATH[i], "");
			}
		}
		return dsName;
	}
	/** 
	 * RrdDb가 이미 존재하는 지 확인한다.
	 * @param collectionName
	 * @param rrdPath
	 * @return
	 */
//	public boolean existRrdDb(Device device, String databaseName, String dsName) {
	public boolean existRrdDb(String rrdPath) {
		boolean existResult = false;
		
		try {
//			long count = operations.count(new Query(Criteria.where(RRDPATH_KEY_NAME).is(readRrdPath(device, databaseName))), readCollectionName());
//			existResult = (count > 0)  ? true : false;
//			String rrdPath = readRrdPath(device, databaseName, dsName);
			File file = new File(rrdPath);
			existResult = file.exists()?true:false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return existResult;
	}
}
