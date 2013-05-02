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
package net.cit.tetrad.rrd.batch.job;

import static net.cit.tetrad.utility.CommonUtils.appCtx;
import net.cit.tetrad.rrd.service.TetradRrdDbService;

import org.apache.log4j.Logger;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;

public class TotalInfoJob implements Job {
	private static Logger logger = Logger.getLogger(TotalInfoJob.class);
	
	private TetradRrdDbService rrdService;
	
	public void setRrdService(TetradRrdDbService rrdService) {
		this.rrdService = rrdService;
	}
	
	@Override
	public void execute(JobExecutionContext context)
			throws JobExecutionException {
		
		try {
			rrdService = (TetradRrdDbService)  appCtx.getBean("tetradRrdDbService");			
			rrdService.makeTotalMongodInfo();
		} catch (Exception e) {
			logger.error(e, e);
			e.printStackTrace();
		}
	}
	
	public static void main(String[] args) throws JobExecutionException {
		TotalInfoJob job = new TotalInfoJob();
		job.execute(null);
	}
}
