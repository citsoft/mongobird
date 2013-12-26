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

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;

import net.cit.tetrad.common.Config;
import net.cit.tetrad.common.MongobirdLicenseManager;
import net.cit.tetrad.common.PropertiesNames;
import net.cit.tetrad.rrd.dao.DataAccessObjectForMongo;
import net.cit.tetrad.rrd.dao.MultieventMapHelper;
import net.cit.tetrad.rrd.utils.TetradRrdConfig;
import net.cit.tetrad.rrd.utils.TetradRrdDbPool;
import net.citsoft.communication.DistrCommunication;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.rrd4j.core.RrdNioBackendFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class InitializeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());

	public void init() throws ServletException {		
		WebApplicationContext context = 
					WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		
		rrdInitialize(context);
		loggingStartup(context);
	}
	
	/**
	 * RRD 파일 생성 및 관련 쓰레드 기동.
	 * @param context
	 */
	private void rrdInitialize(WebApplicationContext context) {
		try {
			String execute = TetradRrdConfig.getTetradRrdConfig("initservlet");
			
			DeviceInMemory deviceInMemory = (DeviceInMemory)context.getBean("deviceInMemory");
			MongoInMemory mongoInMemory = (MongoInMemory)context.getBean("mongoInMemory");
			DataAccessObjectForMongo daoForMongo = (DataAccessObjectForMongo) context.getBean("dataAccessObjectForMongo");
			 
			deviceInMemory.createDeviceGroup();
			mongoInMemory.createMongoGroup();
			
			if (execute.equals("Y")) {
				logger.info("start init servlet!");

				RrdNioBackendFactory.setSyncPeriod(3600);
				
				String poolSize = TetradRrdConfig.getTetradRrdConfig("default_rrdPoolSize");
				TetradRrdDbPool.setPoolCount(Integer.parseInt(poolSize));
				
				TetradRrdInitializer tetradRrdInitializer = (TetradRrdInitializer)context.getBean("tetradRrdInitializer");
				tetradRrdInitializer.input();
				
				TotalInfoScheduling totalInfoSchedul = new TotalInfoScheduling();
				totalInfoSchedul.run();				
				
				Thread checkingMongo = new Thread(new ConnectMongoHandlerThread());
				checkingMongo.start();
				
				Thread helper = new Thread(new MultieventMapHelper(daoForMongo));
				helper.start();
			}
		} catch (Exception ex) {
			logger.error(ex, ex);
		}
	}
	
	/**
	 * Distr 웹 어플리케이션(배포싸이트) 에 라이센스 정보 logging
	 * @param context
	 */
	private void loggingStartup(WebApplicationContext context) {
		new Thread(new LoggingMessageThread(context), "loggingMessage").start();
	}
	
	private class LoggingMessageThread implements Runnable {
		private Logger logger = Logger.getLogger(this.getClass());
		private WebApplicationContext context;
		
		private LoggingMessageThread(WebApplicationContext context) {
			this.context = context;
		}
		
		@Override
		public void run() {
			String version = "";
			String licensekey = "";
			MongobirdLicenseManager licenseManager = null;
			try {
				licenseManager = (MongobirdLicenseManager)context.getBean("mongobirdLicenseManager");
				
				licensekey = licenseManager.getLicensekey();
				version = PropertiesNames.RELEASEVERSIONINFO;
				
				String useros = System.getProperty("os.name");
				String userencoding = System.getProperty("file.encoding");
 				String userlanguage = System.getProperty("user.language");

				JSONObject jsonObj = new JSONObject();
				jsonObj.put("version", version);
				jsonObj.put("useros", useros);
				jsonObj.put("userencoding", userencoding);
				jsonObj.put("userlanguage", userlanguage);
				jsonObj.put("licensekey", licensekey);
				
				DistrCommunication.logging("mongobird", jsonObj);

			} catch (Exception e) {
				logger.error(e, e);
			} finally {
				Config.LICENSETYPE = licenseManager.getLicenseType(licensekey);
				Config.LICENSEKEY = licenseManager.convertLicensekey(licensekey);
			}
		}
	}
}
