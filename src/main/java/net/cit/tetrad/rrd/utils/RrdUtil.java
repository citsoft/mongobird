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

public class RrdUtil {
	private static int logGenerationIntervalSeconds = 0;						// 로그 갱신 주기
	private static int logRetentionPeriodDays = 0;								// 로그 보존 기간
//	private static int createRowCntPerSeconds = 0;								// Rrd Db 생성해야 할 row 갯수
//	private static int createRowCnt = 0;	
//	private static int createRowCntPerHours = 0;	
//	private static int createRowCntPerDays = 0;
	
	public static int readLogGenerationInterval() {
		try {
			String defaultConfigValue = TetradRrdConfig.getTetradRrdConfig("default_log_generation_interval");
			logGenerationIntervalSeconds = Integer.parseInt(defaultConfigValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return logGenerationIntervalSeconds;
	}
	
	public static int readLogRetentionPeriod() {
		try {
			String defaultConfigValue = TetradRrdConfig.getTetradRrdConfig("default_log_retention_period");
			logRetentionPeriodDays = Integer.parseInt(defaultConfigValue);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return logRetentionPeriodDays;
	}
	
//	public static int calculateCreateRowCntPerSeconds() {
//		try {
//			if (createRowCntPerSeconds == 0) {
//				float createRowCntTmp = (float)60 / readLogGenerationInterval() * 60 * 24 * readLogRetentionPeriod();
//				createRowCntPerSeconds = (int)createRowCntTmp;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			createRowCntPerSeconds = 0;
//		}
//		
//		return createRowCntPerSeconds;
//	}
	
//	public static int calculateCreateRowCntPerMinutes() {
//		try {
//			if (createRowCntPerMinutes == 0) {
//				float createRowCntTmp = (float)60 * 24 * readLogRetentionPeriod();
//				createRowCntPerMinutes = (int)createRowCntTmp;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			createRowCntPerMinutes = 0;
//		}
//		
//		return createRowCntPerMinutes;
//	}

	public static int calculateCreateRowCnt(int countPerHour) {
		int createRowCnt = 0;
		try {
			createRowCnt = countPerHour * 24 * readLogRetentionPeriod();
		} catch (Exception e) {
			e.printStackTrace();
			createRowCnt = 0;
		}		
		return createRowCnt;
	}
	
//	public static int calculateCreateRowCntPerMinutes(int minutes) {
//		try {
//			if (createRowCntPerMinutes == 0) {
//				float createRowCntTmp = (float)(60*minutes) * 24 * readLogRetentionPeriod();
//				createRowCntPerMinutes = (int)createRowCntTmp;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			createRowCntPerMinutes = 0;
//		}
//		
//		return createRowCntPerMinutes;
//	}
	
//	public static int calculateCreateRowCntPerHours() {
//		try {
//			if (createRowCntPerHours == 0) {
//				float createRowCntTmp = (float)24 * readLogRetentionPeriod();
//				createRowCntPerHours = (int)createRowCntTmp;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			createRowCntPerHours = 0;
//		}
//		
//		return createRowCntPerHours;
//	}
	
//	public static int calculateCreateRowCntPerDays() {
//		try {
//			if (createRowCntPerDays == 0) {
//				float createRowCntTmp = (float)readLogRetentionPeriod();
//				createRowCntPerDays = (int)createRowCntTmp;
//			}
//		} catch (Exception e) {
//			e.printStackTrace();
//			createRowCntPerDays = 0;
//		}
//		
//		return createRowCntPerDays;
//	}	
}
