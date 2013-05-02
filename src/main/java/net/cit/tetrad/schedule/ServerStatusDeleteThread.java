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
