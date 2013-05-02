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
