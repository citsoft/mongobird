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
package net.cit.tetrad.utility;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import static net.cit.tetrad.common.ColumnConstent.*;
import net.cit.tetrad.common.Utility;
import net.cit.tetrad.model.CommonDto;

import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;


public class QueryUtils {

	/**
	 * idx 쿼리 반환
	 */
	public static Query setIdx(int idx){
		Query query = new Query();
		if(idx!=0)query.addCriteria(Criteria.where(IDX).is(idx));
		return query;
	}
	
	/**
	 * uid 쿼리 반환
	 */
	public static Query setUid(String uid){
		Query query = new Query();
		if(!Utility.isNull(uid).equals(""))query.addCriteria(Criteria.where(DEVICE_UID).is(uid));
		return query;
	}
	
	/**
	 * index 구하기 위한 collectionnm 쿼리 반환
	 */
	public static Query setCollection(String collectionnm){
		Query query = new Query();
		if(!Utility.isNull(collectionnm).equals(""))query.addCriteria(Criteria.where("collectionnm").is(collectionnm));
		return query;
	}
	
	public static Query notIdxsetUid(int idx, String uid){
		Query query = new Query();
		query.addCriteria(Criteria.where("idx").ne(idx));
		query.addCriteria(Criteria.where("uid").is(uid));
		return query;
	}
	
	public static Query setGroupname(String groupnm){
		Query query = new Query();
		if(!Utility.isNull(groupnm).equals(""))query.addCriteria(Criteria.where("groupname").is(groupnm));
		query.sort().on("groupname", Order.ASCENDING);
		return query;
	}
	
	/**
	 * ip,port,del 쿼리 반환
	 */
	public static Query setIpPort(String ip, String port, int idx){
		Query query = new Query();
		if(idx!=0)query.addCriteria(Criteria.where("idx").ne(idx));
		if(!Utility.isNull(ip).equals(""))query.addCriteria(Criteria.where("ip").is(ip));
		if(!Utility.isNull(port).equals(""))query.addCriteria(Criteria.where("port").is(port));
		return query;
	}
	
	public static Query setUidPasswd(String uid,String passwd){
		Query query = new Query();
		if(!Utility.isNull(uid).equals(""))query.addCriteria(Criteria.where("uid").is(uid));
		if(!Utility.isNull(passwd).equals(""))query.addCriteria(Criteria.where("passwd").is(passwd));
		return query;
	}
	
	public static Query setGroupCode(CommonDto dto){
		Query query = new Query();
		if(dto.getGroupCode()!=0)query.addCriteria(Criteria.where("groupCode").is(dto.getGroupCode()));
		if(!Utility.isNull(dto.getType()).equals(""))query.addCriteria(Criteria.where("type").is(dto.getType()));
		
		if(!Utility.isNull(dto.getSort()).equals("")){
			if(Utility.isNull(dto.getSort()).equals("asc")){
				query.sort().on(dto.getSortItem(), Order.ASCENDING);
			}else if(Utility.isNull(dto.getSort()).equals("desc")){
				query.sort().on(dto.getSortItem(), Order.DESCENDING);
			}
		}
		return query;
	}
	
	public static Query setAlarmCode(int alarmCode){
		Query query = new Query();
		if(alarmCode!=0)query.addCriteria(Criteria.where("alarmCode").is(alarmCode));
		return query;
	}
	
	public static Query setAlarmSearch(CommonDto dto){
		Query query = new Query();
		if(!Utility.isNull(dto.getSdate()).equals("")&&!Utility.isNull(dto.getEdate()).equals(""))
			query.addCriteria(Criteria.where("up_date").gte(dto.getSdate()).lte(dto.getEdate()));
		if(dto.getAlarm()!=4)query.addCriteria(Criteria.where("alarm").is(dto.getAlarm()));
		if(dto.getDeviceCode() != 0)query.addCriteria(Criteria.where("deviceCode").is(dto.getDeviceCode()));
		if(dto.getGroupCode() != 0)query.addCriteria(Criteria.where("groupCode").is(dto.getGroupCode()));
		if(!Utility.isNull(dto.getType()).equals(""))query.addCriteria(Criteria.where("type").is(dto.getType()));
		if(dto.getConfirm()!=2)query.addCriteria(Criteria.where("confirm").is(dto.getConfirm()));
		query.sort().on("confirm", Order.ASCENDING);
		query.sort().on("alarm", Order.ASCENDING);
		query.sort().on("up_date", Order.DESCENDING);
		query.sort().on("up_time", Order.DESCENDING);
		return query;
	}
	
	/**
	 * date, alarm, confirm을 기준으로 sort된 쿼리
	 */
	public static Query setAlarmSort(CommonDto dto){
		Query query = new Query();
		if(dto.getAlarm()!=0)query.addCriteria(Criteria.where("alarm").is(dto.getAlarm()));
		query.addCriteria(Criteria.where("confirm").is(0));
		query.sort().on("confirm", Order.ASCENDING);
		query.sort().on("alarm", Order.ASCENDING);
		query.sort().on("up_date", Order.DESCENDING);
		query.sort().on("up_time", Order.DESCENDING);
		return query;
	}
	
	public static Query setConfirm(int groupCode, int deviceCode, String type,int alarm){
		Query query = new Query();
		if(groupCode != 0)query.addCriteria(Criteria.where("groupCode").is(groupCode));
		if(deviceCode != 0)query.addCriteria(Criteria.where("deviceCode").is(deviceCode));
		if(!Utility.isNull(type).equals(""))query.addCriteria(Criteria.where("cri_type").is(type));
		query.addCriteria(Criteria.where("alarm").is(alarm));
		query.addCriteria(Criteria.where("confirm").is(0));
		return query;
	}
	
	/**
	 * 중복되는 임계값 항목을 찾기 위해 groupname,uid,type 쿼리 생성
	 */
	public static Query setCriSearch(CommonDto dto){
		Query query = new Query();
		if(dto.getIdx()!=0)query.addCriteria(Criteria.where("idx").ne(dto.getIdx()));
		if(dto.getGroupCode() != 0)query.addCriteria(Criteria.where("groupCode").is(dto.getGroupCode()));
		if(dto.getDeviceCode() != 0)query.addCriteria(Criteria.where("deviceCode").is(dto.getDeviceCode()));
		if(!Utility.isNull(dto.getType()).equals(""))query.addCriteria(Criteria.where("type").is(dto.getType()));
		return query;
	}
	
	public static void getUpdate(Update update, Map<String, Object> values) {
		Set<String> keys = values.keySet();
		Iterator<String> it = keys.iterator();
		while(it.hasNext()){
			String key = it.next().toString();
			Object value = values.get(key);

			update.set(key, value);
		}
		
	}
	
	public static Query sortDate(int auth,int userCode,String tablenm) {
		Query query = new Query();
		if(tablenm.equals("user")){
			Criteria c = new Criteria();
			c.orOperator(Criteria.where("idx").is(userCode),
						 Criteria.where("authority").lt(auth));
			query.addCriteria(c);
			query.sort().on("authority", Order.DESCENDING);
		}
		query.sort().on("reg_date", Order.DESCENDING);
		query.sort().on("groupCode", Order.ASCENDING);
		query.sort().on("deviceCode", Order.ASCENDING);
		query.sort().on("reg_date", Order.DESCENDING);
		return query;
	}
	
	public static Query setDeviceCode(int deviceCode) {
		Query query = new Query(Criteria.where(DEVICECODE).is(deviceCode));		
		return query;
	}
	
	public static Query setAuthority(int authority) {
		Query query = new Query(Criteria.where("authority").is(authority));		
		return query;
	}
	
	public static Query setType(Query query, String type) {
		query.addCriteria(Criteria.where(CLRITICAL_TYPE).is(type));
		return query;
	}
}
