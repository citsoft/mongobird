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

import net.cit.tetrad.rrd.utils.TetradRrdConfig;
import net.cit.tetrad.rrd.utils.TetradRrdDbPool;

import org.apache.log4j.Logger;
import org.rrd4j.core.RrdNioBackendFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;

public class InitializeServlet extends HttpServlet {
	private static final long serialVersionUID = 1L;
	private Logger logger = Logger.getLogger(this.getClass());

	public void init() throws ServletException {	
		
		try {
			String execute = TetradRrdConfig.getTetradRrdConfig("initservlet");
			
			WebApplicationContext context = 
						WebApplicationContextUtils.getWebApplicationContext(getServletContext());
			
			DeviceInMemory deviceInMemory = (DeviceInMemory)context.getBean("deviceInMemory");
			MongoInMemory mongoInMemory = (MongoInMemory)context.getBean("mongoInMemory");
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
			}
		} catch (Exception ex) {
			logger.error(ex, ex);
		}
	}
}
