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
import net.cit.tetrad.rrd.bean.ServerStatus;
import net.cit.tetrad.rrd.utils.TimestampUtil;

public class ServerStatusDeleteThread extends Thread {
	private MonadService monadService;
	private String deleteLogStartDate = null;
	private String deleteLogEndDate = null;
	private Class<?> serverStatus = ServerStatus.class;
	
	public ServerStatusDeleteThread(MonadService monadService){
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
			StatusInMemory.setDeleteLogServerSt(true);
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
					
					monadService.delete(query, serverStatus);
					Thread.sleep(1000);
					int remainCnt = remainderCnt();
					if(remainCnt == 0 || !StatusInMemory.isDeleteLogServerSt()){
						StatusInMemory.setDeleteLogServerSt(false);
						break;
					}else if(remainCnt != 0 && longsdate > longEndDate){
						query.addCriteria(Criteria.where("regtime").gte(deleteLogStartDate).lte(deleteLogEndDate));
						monadService.delete(query, serverStatus);
					}
				}catch(Exception e){
					e.printStackTrace();
					StatusInMemory.setDeleteLogServerSt(false);
				}
			}
		}catch(Exception e){
			e.printStackTrace();
			StatusInMemory.setDeleteLogServerSt(false);
		}
	}
	
	private int remainderCnt(){
		Query query = new Query();
		query.addCriteria(Criteria.where("regtime").gte(deleteLogStartDate).lte(deleteLogEndDate));
		int serverStCnt = (int) monadService.getCount(query, serverStatus);
		StatusInMemory.setServerCnt(serverStCnt);
		return serverStCnt;
	}
}
