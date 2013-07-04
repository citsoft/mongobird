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
