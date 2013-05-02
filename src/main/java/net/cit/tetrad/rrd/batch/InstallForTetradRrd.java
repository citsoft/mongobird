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
package net.cit.tetrad.rrd.batch;

import static net.cit.tetrad.utility.CommonUtils.appCtx;

import org.apache.log4j.Logger;

public class InstallForTetradRrd {
	
	private static Logger logger = Logger.getLogger(InstallForTetradRrd.class);
	
	public static void main(String[] args) {
		try {
			logger.debug("InstallForTetrad Start!!");
			
			TetradRrdInitializer tetradRrdInitializer = (TetradRrdInitializer)appCtx.getBean("tetradRrdInitializer");
			tetradRrdInitializer.installTotalRrdDb();
			
			logger.debug("InstallForTetrad Done!!");
		} catch (Exception e) {
			logger.info("----------------------------------------------------------------");
			logger.info(e.getMessage());
			logger.info("----------------------------------------------------------------");
		}
	}

}
