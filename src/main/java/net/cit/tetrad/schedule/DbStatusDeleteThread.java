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
package net.cit.tetrad.schedule;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;

import net.cit.tetrad.monad.MonadService;
import net.cit.tetrad.rrd.bean.DbStatus;
import net.cit.tetrad.rrd.utils.TimestampUtil;

public class DbStatusDeleteThread extends Thread {
	private MonadService monadService;
	private String deleteLogStartDate = null;
	private String deleteLogEndDate = null;
	private Class<?> dbStatus = DbStatus.class;
	
	public DbStatusDeleteThread(MonadService monadService){
		this.monadService = monadService;
		this.deleteLogStartDate = StatusInMemory.getStartDate();
		this.deleteLogEndDate = StatusInMemory.getEndDate();
	}
	
	public void run() {
		String sdate = "";
		String edate = "";
		long longEndDate = TimestampUtil.convStringToTimestamp(deleteLogEndDate);
		long longsdate;
		long longedate;
		try{
			StatusInMemory.setDeleteLogDbSt(true);
			for ( ; ; ) {	
				try{
					if(edate.equals(""))
						sdate = deleteLogStartDate;
					else
						sdate = edate;
					edate = TimestampUtil.plusHour(sdate, 1);
					longsdate = TimestampUtil.convStringToTimestamp(sdate);
					longedate = TimestampUtil.convStringToTimestamp(edate);
					if(longedate > longEndDate)edate = deleteLogEndDate;
					
					Query query = new Query();
					query.addCriteria(Criteria.where("regtime").gte(sdate).lte(edate));
					query.sort().on("regtime", Order.ASCENDING);
					
					monadService.delete(query, dbStatus);
					Thread.sleep(1000);
					int remainCnt = remainderCnt();
					if(remainCnt == 0 || !StatusInMemory.isDeleteLogDbSt()){
						StatusInMemory.setDeleteLogDbSt(false);
						break;
					}else if(remainCnt != 0 && longsdate > longEndDate){
						query.addCriteria(Criteria.where("regtime").gte(deleteLogStartDate).lte(deleteLogEndDate));
						monadService.delete(query, dbStatus);
					}
				}catch(Exception e){
					e.printStackTrace();
					StatusInMemory.setDeleteLogDbSt(false);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			StatusInMemory.setDeleteLogDbSt(false);
		}
	}
	
	private int remainderCnt(){
		Query query = new Query();
		query.addCriteria(Criteria.where("regtime").gte(deleteLogStartDate).lte(deleteLogEndDate));
		int dbStCnt = (int) monadService.getCount(query, dbStatus);
		StatusInMemory.setDbCnt(dbStCnt);
		return dbStCnt;
	}
}
