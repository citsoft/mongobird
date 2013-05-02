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
import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;
import static org.quartz.TriggerKey.triggerKey;

import java.util.List;

import net.cit.tetrad.common.ColumnConstent;
import net.cit.tetrad.common.DateUtil;
import net.cit.tetrad.rrd.batch.job.TotalInfoJob;
import net.cit.tetrad.rrd.rule.StatusDatasourceName;
import net.cit.tetrad.rrd.rule.TotalStatusRule;
import net.cit.tetrad.rrd.utils.TetradRrdConfig;
import net.cit.tetrad.utility.CommonUtils;

import org.apache.log4j.Logger;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.rrd4j.core.Util;

public class TotalInfoScheduling {
	private Logger logger = Logger.getLogger(this.getClass());
	
	public void run() throws Exception {
		logger.info("totalInfo Scheduling ...");
		
		checkExistRrdTable();
		
		SchedulerFactory  schedulerFactory = new StdSchedulerFactory();
		Scheduler scheduler = schedulerFactory.getScheduler();

		scheduler.start();        
		JobDetail pasjob = newJob(TotalInfoJob.class).withIdentity("TotalInfoJob").build();

		String triggerKey = DateUtil.getTime();
		
		int intervalSeconds = Integer.parseInt(TetradRrdConfig.getTetradRrdConfig("default_log_generation_interval"));
		Trigger trigger = newTrigger().withIdentity(triggerKey(triggerKey))
				.withSchedule(simpleSchedule().withIntervalInSeconds(intervalSeconds).repeatForever()).startNow().build();

		scheduler.scheduleJob(pasjob, trigger);

		logger.info("done");
	}
	
	private void checkExistRrdTable() {
		String rrdDefaultPath = CommonUtils.getDefaultRrdPath();
		List<StatusDatasourceName> totalDatasourceNames = new TotalStatusRule().totalStatusDatasourceNameXMLToObject();
		
		String dsName;
		for (StatusDatasourceName dbstatusInfo : totalDatasourceNames) {
			dsName = dbstatusInfo.getDsName();		
			String rrdPath = rrdDefaultPath + dsName + ".rrd";
			
			if (!Util.fileExists(rrdPath)) initialTotalInfo();
		}
	}
	
	private void initialTotalInfo() {
		TetradRrdInitializer tetradInitial = (TetradRrdInitializer)appCtx.getBean("tetradRrdInitializer");
		try {
			tetradInitial.installTotalRrdDb();
		} catch (Exception e) {
			logger.error("install error", e);
		}
	}

	public static void main(String[] args) throws Exception {
		TotalInfoScheduling totSchedul = new TotalInfoScheduling();
		totSchedul.run();
	}

}
