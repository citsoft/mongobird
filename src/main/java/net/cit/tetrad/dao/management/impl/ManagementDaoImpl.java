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
package net.cit.tetrad.dao.management.impl;

import static net.cit.tetrad.common.ColumnConstent.*;

import java.lang.reflect.InvocationTargetException;
import java.security.MessageDigest;
import java.util.HashMap;
import java.util.Map;

import net.cit.tetrad.common.CriticalInfo;
import net.cit.tetrad.common.DateUtil;
import net.cit.tetrad.common.SHAHashing;
import net.cit.tetrad.common.Utility;
import net.cit.tetrad.dao.management.ManagementDao;
import net.cit.tetrad.model.*;
import net.cit.tetrad.rrd.utils.JasonUtil;

import org.apache.commons.beanutils.BeanUtils;
import org.springframework.data.mongodb.core.query.Update;

import com.mongodb.CommandResult;
import com.mongodb.DB;
import com.mongodb.Mongo;

public class ManagementDaoImpl implements ManagementDao{
	
	private static Mongo mongo = null;
	int a;
	/**
	 * classnm에 따라 dto 선택
	 */
	@Override
	public Class<?> getDtoClassNm(String dtoClassNm){
		Class<?> dtoClass = null;
		if(dtoClassNm.equals("user"))dtoClass=User.class;
		else if(dtoClassNm.equals("group"))dtoClass=Group.class;
		else if(dtoClassNm.equals("device"))dtoClass=Device.class;
		else if(dtoClassNm.equals("critical"))dtoClass=Critical.class;
		else if(dtoClassNm.equals("global"))dtoClass=Global.class;
		return dtoClass;
		
	}
	
	/**
	 * classnm에 따라 dto에 값 set
	 */
	@Override
	public Object setDto(String dtoClassNm,CommonDto dto){
		Object obj = null;
		if(dtoClassNm.equals("group")){
			Group sdto = new Group();
			sdto.setIdx(dto.getIdx());
			sdto.setUid(dto.getUid());
			sdto.setReg_date(DateUtil.getTime());
			obj=sdto;
		}else if(dtoClassNm.equals("device")){
			Device sdto = new Device();
			sdto.setIdx(dto.getIdx());
			sdto.setUid(dto.getUid());
			sdto.setGroupCode(dto.getGroupCode());
			sdto.setType(dto.getType());
			sdto.setHddsize(dto.getHddsize());
			sdto.setIp(dto.getIp());
			sdto.setMemorysize(dto.getMemorysize());
			sdto.setPort(dto.getPort());
			sdto.setReg_date(DateUtil.getTime());
			sdto.setAuthUser(dto.getAuthUser());
			sdto.setAuthPasswd(dto.getAuthPasswd());
			obj=sdto;
		}else if(dtoClassNm.equals("critical")){
			Critical sdto = new Critical();
			sdto.setIdx(dto.getIdx());
			sdto.setDeviceCode(dto.getDeviceCode());
			sdto.setGroupCode(dto.getGroupCode());
			sdto.setType(dto.getType());
			
			String unit = getUnit(dto.getType());
			sdto.setUnit(unit);
			if(unit.equals(UNIT_SECONDS)){
				sdto.setCriticalvalue(dto.getCriticalvalue()*1000000);
				sdto.setWarningvalue(dto.getWarningvalue()*1000000);
				sdto.setInfovalue(dto.getInfovalue()*1000000);
			}else{
				sdto.setCriticalvalue(dto.getCriticalvalue());
				sdto.setWarningvalue(dto.getWarningvalue());
				sdto.setInfovalue(dto.getInfovalue());
			}
			sdto.setReg_date(DateUtil.getTime());
			obj=sdto;
		}else if(dtoClassNm.equals("user")){
			User sdto = new User();
			sdto.setPasswd(makePasswd(dto.getPasswd()));
			sdto.setIdx(dto.getIdx());
			sdto.setUid(dto.getUid());
			sdto.setUsername(dto.getUsername());
			sdto.setAuthority(dto.getAuthority());
			sdto.setEmail(dto.getEmail());
			sdto.setMobile(dto.getMobile());
			sdto.setReg_date(DateUtil.getTime());
			obj=sdto;
		}
		return obj;
	}
	
	private String getUnit(String criticalType) {
		Map<String, Object> criticalMap = new CriticalInfo().criticalInfoXMLToMap();
		Critical critical = (Critical) criticalMap.get(criticalType);
		return critical.getUnit();
	}
	
	/**
	 * classnm에 따라 update에 값 set
	 */
	@Override
	public Update setUpdate(String dtoClassNm,CommonDto dto){
		Update update = new Update();
		if(dtoClassNm.equals("device")){
			Device ddto = new Device();
			try {
				BeanUtils.copyProperties(ddto, dto);
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (InvocationTargetException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}

			update.set(ddto);
		}else if(dtoClassNm.equals("critical")){
			update.set("groupCode",dto.getGroupCode());
			update.set("deviceCode",dto.getDeviceCode());
			update.set("type",dto.getType());
			
			String unit = getUnit(dto.getType());
			update.set("unit", unit);			
			if(unit.equals(UNIT_SECONDS)){
				update.set("criticalvalue",dto.getCriticalvalue()*1000000);
				update.set("warningvalue",dto.getWarningvalue()*1000000);
				update.set("infovalue",dto.getInfovalue()*1000000);
			}else{
				update.set("criticalvalue",dto.getCriticalvalue());
				update.set("warningvalue",dto.getWarningvalue());
				update.set("infovalue",dto.getInfovalue());
			}
		}else if(dtoClassNm.equals("user")){
			String passwd = Utility.isNull(dto.getPasswd());
			if(!passwd.equals(""))update.set("passwd",makePasswd(dto.getPasswd()));
			update.set("uid",dto.getUid());
			update.set("username",dto.getUsername());
			update.set("email",dto.getEmail());
			update.set("mobile",dto.getMobile());
			update.set("authority",dto.getAuthority());
		}else if(dtoClassNm.equals("group")){
			update.set("uid",dto.getUid());
			update.set("reg_date",DateUtil.getTime());
		}
		return update;
	}
	
	@Override
	public String makePasswd(String passwd){
		SHAHashing imp = new SHAHashing();
		MessageDigest md;
		String hashPasswd ="";
		try {
			md = MessageDigest.getInstance("SHA-256");
			md = imp.update(passwd, md);
			hashPasswd = imp.getValue(md);
		} catch (Exception e) {
			
		}
		return hashPasswd;
	}

	/**
	 * yyyyMMddHHmmssSSS 형식의 timestamp를 yyyy-MM-dd형식의 string으로 변경
	 * @param timeStampStr
	 * @return
	 */
	public String timeStampToString(String timeStampStr){
		String str = "";
		str = timeStampStr.substring(0,4) + "-" + timeStampStr.substring(4,6) + "-" + timeStampStr.substring(6,8);
		return str;
	}
	
	/**
	 * mongo ping 확인
	 */
	public String mongoExistCheck(String ip, int port, String userStr, String passwdStr, String isExistMongo){
		Map<String, Object> resultMap = new HashMap<String, Object>();
			try {
				mongo = new Mongo(ip, port);
				DB db = mongo.getDB("admin");
				if(!userStr.equals("") && !passwdStr.equals("")){
					boolean auth = db.authenticate(userStr, passwdStr.toCharArray());
				}
				CommandResult pingResult = db.command("ping");
				resultMap = JasonUtil.getEntryValue(pingResult, "", resultMap);
				Double resultOkVal = (Double) resultMap.get("ok");
				if(resultOkVal!=1.0){
					isExistMongo = "3";
				}else{
					try{
						mongo.slaveOk();
					}catch(Exception e){
						
					}
					CommandResult commandResult = db.command("serverStatus");
					resultMap = JasonUtil.getEntryValue(commandResult, "", resultMap);
					resultOkVal = (Double) resultMap.get("ok");
					if(resultOkVal!=1.0)isExistMongo = "4";
				}
			} catch (Exception e) {
				isExistMongo = "3"; 
			} finally {
				if(mongo!=null)mongo.close();
			}
			return isExistMongo;
	}
}
