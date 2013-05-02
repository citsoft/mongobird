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
