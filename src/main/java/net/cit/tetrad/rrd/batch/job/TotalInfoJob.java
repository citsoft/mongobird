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
