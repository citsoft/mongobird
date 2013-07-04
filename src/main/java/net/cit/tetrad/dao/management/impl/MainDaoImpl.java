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

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;

import static net.cit.tetrad.utility.QueryUtils.setUid;

import net.cit.tetrad.common.Config;
import net.cit.tetrad.common.Utility;
import net.cit.tetrad.dao.management.MainDao;
import net.cit.tetrad.model.Global;
import net.cit.tetrad.monad.MonadService;
import net.cit.tetrad.rrd.bean.GraphDefInfo;
import net.cit.tetrad.rrd.utils.RrdUtil;
import net.cit.tetrad.rrd.utils.TimestampUtil;
import static net.cit.tetrad.common.ColumnConstent.*;

public class MainDaoImpl implements MainDao {

	private Logger log = Logger.getLogger(this.getClass());
	
	private final String PERIOD_MINUTEY = "1";
	private final String PERIOD_DAY = "2";
	private MonadService monadService;
	
	public void setMonadService(MonadService monadService) {
		this.monadService = monadService;
	}
	/**
	 * 데몬,데이터베이스 별 상세 내역 요청 시 항목 설정
	 */
	@Override
	public String[] setFilters(String type, String period, String dsType){
		String[] filters = null ;
//		String graphPeriod = getGraphPeriod(period);
		String getDsType = getDsType(dsType);
		
		if(type.equals("conn")){
			filters = new String[]{"connections_current"};
			
		} else if(type.equals("r_mem")){
			filters = new String[]{"mem_resident"};
			
		} else if(type.equals("v_mem")){
			filters = new String[]{"mem_virtual"};
			
		} else if(type.equals("p_fault")){
			filters = new String[]{"extra_info_page_faults"};
			
		} else if(type.equals("op_insert")){
			filters = new String[]{"opcounters_insert"};
			
		} else if(type.equals("op_delete")){
			filters = new String[]{"opcounters_delete"};
			
		} else if(type.equals("op_update")){
			filters = new String[]{"opcounters_update"};
			
		} else if(type.equals("op_query")){
			filters = new String[]{"opcounters_query"};
			
		} else if(type.equals("global")){
			filters = new String[]{"globalLock_lockTime"};
			
		} else if(type.equals("w_locks")){
			filters = new String[]{"locks_timeLockedMicros_W"};
			
		} else if(type.equals("r_locks")){
			filters = new String[]{"locks_timeLockedMicros_R"};
			
		} else if(type.equals("in_network")){
			filters = new String[]{"network_bytesIn"};
			
		} else if(type.equals("out_network")){
			filters = new String[]{"network_bytesOut"};
			
		} else if (type.equals("totdb")) {
			filters = new String[]{"dbDataSize"};			
			
		} else if(type.equals("dbDataSize")){
			filters = new String[]{getDsType+"dbDataSize"};
			
		} else if(type.equals("dbIndexSize")){
			filters = new String[]{getDsType+"dbIndexSize"};
			
		}
		
		return filters;
	}
	
	public String[] setGraphLegend(String type) {
		String[] graphLegend = null ;
		
		if(type.equals("conn")){
			graphLegend = new String[]{"Current"};
			
		} else if(type.equals("r_mem")){
			graphLegend = new String[]{"Resident Memory"};
			
		} else if(type.equals("v_mem")){
			graphLegend = new String[]{"Virtual Memory"};
			
		} else if(type.equals("p_fault")){
			graphLegend = new String[]{"Page fault"};
			
		} else if(type.equals("op_insert")){
			graphLegend = new String[]{"Insert"};
			
		} else if(type.equals("op_delete")){
			graphLegend = new String[]{"Delete"};
			
		} else if(type.equals("op_update")){
			graphLegend = new String[]{"Update"};
			
		} else if(type.equals("op_query")){
			graphLegend = new String[]{"Query"};
			
		} else if(type.equals("global")){
			graphLegend = new String[]{"Lock Time"};
			
		} else if(type.equals("w_locks")){
			graphLegend = new String[]{"Write locks"};
			
		} else if(type.equals("r_locks")){
			graphLegend = new String[]{"Read locks"};
			
		} else if(type.equals("in_network")){
			graphLegend = new String[]{"In network"};
			
		} else if(type.equals("out_network")){
			graphLegend = new String[]{"Out network"};
			
		} else if (type.equals("totdb")) {
			graphLegend = new String[]{"DB Size"};			
			
		} else if(type.equals("dbDataSize")){
			graphLegend = new String[]{"DB data size sum"};
			
		} else if (type.equals("dbIndexSize")) {
			graphLegend = new String[]{"DB index size sum"};			
		}
		
		return graphLegend;
	}

	public String getDsType(String dsType){
		String gDstype = "";
		if (dsType!=null && dsType.equals("cur"))gDstype = "diff_";
		return gDstype;
	}
	
	public String[] setDbFilters(String dsType, String gubun){
		String[] filters = null ;
		String getDsType = getDsType(dsType);
		
		if(gubun.equals(IMGTOTALFIELD2_0[0])){
			filters = new String[]{getDsType + IMGTOTALFIELD2_0[0]};
		} else if(gubun.equals(IMGTOTALFIELD2_0[1])){
			filters = new String[]{getDsType + IMGTOTALFIELD2_0[1]};
		} else if(gubun.equals(IMGTOTALFIELD2_0[2])){
			filters = new String[]{getDsType + IMGTOTALFIELD2_0[2]};
		} else if(gubun.equals("dataSize")) {
			filters = new String[]{getDsType + "dataSize"};
		} else if(gubun.equals("indexSize")) {
			filters = new String[]{getDsType + "indexSize"};
		}			
		return filters;
	}
	
	public String[] setDbGraphLegend(String gubun) {
		String[] graphLegend = null ;
		
		if(gubun.equals(IMGTOTALFIELD2_0[0])){
			graphLegend = new String[]{"Total DB Size"};
		} else if(gubun.equals(IMGTOTALFIELD2_0[1])){
			graphLegend = new String[]{"Total Index Size"};
		} else if(gubun.equals(IMGTOTALFIELD2_0[2])){
			graphLegend = new String[]{"Global Lock"};
		} else if(gubun.equals("dataSize")) {
			graphLegend = new String[]{"DB Size"};
		} else if(gubun.equals("indexSize")) {
			graphLegend = new String[]{"Index Size"};
		}	
		return graphLegend;
	}
	
	public String getGraphPeriod(String period) {
		String gperiod;
		if (period.equals(PERIOD_MINUTEY)) {
			gperiod = RRD_SUFFIXPATH[0];
		} else {
			gperiod = RRD_SUFFIXPATH[1];
		}
		return gperiod;
	}
	
	public long getSubGraphSearchTime(String period, String date, String hour, String min) {
		long timeStamp;
		String fullDate;
		
		if (period.equals(PERIOD_MINUTEY)) {
			fullDate = date + " " + hour + ":" + min;
			timeStamp = TimestampUtil.readTimestamp(fullDate, "yyyy-MM-dd HH:mm");
		} else if (period.equals(PERIOD_DAY)) {
			fullDate = date + " " + hour;
			timeStamp = TimestampUtil.readTimestamp(fullDate, "yyyy-MM-dd HH");
		} else {
			fullDate = date;
			timeStamp = TimestampUtil.readTimestamp(fullDate, "yyyy-MM-dd");
		}
		return timeStamp;
	}

	public String graphGubun(){
		String graph_period = "2";
		Global global = globalValue(2);
		graph_period = global.getValue();
		return graph_period;
	}
	
	public Global globalValue(int num){
		Query query = new Query();
		String[] essentialGlobalVariable = Config.getConfig("essentialGlobalVariable").split(";");
		query = setUid(essentialGlobalVariable[num]);
		Global global = (Global) monadService.getFind(query, Global.class);
		return global;
	}
	
	public GraphDefInfo setGraphDefInfo(GraphDefInfo gdInfo, String grphPeriod, String[] graphLegend, String consolFun, long step){
		gdInfo.setGraphLegend(graphLegend);
		gdInfo.setAxisTimeUnitDiv(grphPeriod);
		gdInfo.setConsolFun(consolFun);
		gdInfo.setStep(step);
		return gdInfo;
	}
	
	public int mainRefreshPeriodMinute(){
		int main_period = 0;
		Global global = globalValue(1);
		main_period = Integer.parseInt(global.getValue());
		return main_period;
	}
	
	public String getGlobalMongoVersion(){
		String mongoVer = MONGOVER2_2;
		checkExsitEssentialGlobalVariable();
			Global global = globalValue(4);
			mongoVer = global.getValue();
		return mongoVer;
	}
	
	public int getLogRetentionPeriod(){
		Global global = globalValue(0);
		int logRetentionPeriod = Integer.parseInt(Utility.isNullNumber(global.getValue()));
		if(logRetentionPeriod < 7)logRetentionPeriod = 7;
		return logRetentionPeriod;
	}
	
	public String getGlobalHostname(){
			Global global = globalValue(5);
			String globalHostName = Utility.isNull(global.getValue());
		return globalHostName;
	}
	
	public void checkExsitEssentialGlobalVariable(){
		List<String> essentialGlobalVariableList = getEssentialGlobalVariable();
		List<String> exsitGlobalVariableList = getExsitGlobalVariable(essentialGlobalVariableList);
		
		if(exsitGlobalVariableList.size()< essentialGlobalVariableList.size()){
			registerEssentialGlobalVariable(essentialGlobalVariableList, exsitGlobalVariableList);
		}
		default_log_retention_period_update();
	}
	
	public void default_log_retention_period_update(){
		Query query = new Query();
		Update update = new Update();
		String[] essentialGlobalVariable = Config.getConfig("essentialGlobalVariable").split(";");
		query = setUid(essentialGlobalVariable[0]);
		update.set("value",RrdUtil.readLogRetentionPeriod());
		monadService.update(query, update, Global.class);
	}
	
	public List<String> getExsitGlobalVariable(List<String> essentialGlobalVariableList){
		Query query = new Query();
		query.addCriteria(Criteria.where("uid").in(essentialGlobalVariableList));
		query.fields().exclude("_id").include("uid");
		List<Object> globalList = monadService.getList(query, Global.class);
		
		List<String> exsitGlobalVariable = new ArrayList<String>();
		for(Object obj : globalList){
			Global g = (Global)obj;
			exsitGlobalVariable.add(g.getUid());
		}
		return exsitGlobalVariable;
	}
	
	public List<String> getEssentialGlobalVariable(){
		String[] essentialGlobalVariable = Config.getConfig("essentialGlobalVariable").split(";");
		List<String> globalVariable = new ArrayList<String>();
		for(String str : essentialGlobalVariable){
			globalVariable.add(str);
		}
		return globalVariable;
	}
	
	public void registerEssentialGlobalVariable(List<String> essentialList, List<String> exsitList){
		List<Object> globalList = new ArrayList<Object>();
		String[] value = Config.getConfig("essentialGlobalVariableDefaultValue").split(";");
		int index = 0;
		
		for(String essentialGlobalVariable : essentialList){
			if(!exsitList.contains(essentialGlobalVariable)){
				String globalValue = value[index];
				if(essentialGlobalVariable.equals("hostname")){
					String getHostName = Utility.isNull(getServerName());
					globalList.add(setEssentialGlobal(essentialGlobalVariable, getHostName == "" ? globalValue : getHostName));
				}else{
					globalList.add(setEssentialGlobal(essentialGlobalVariable, globalValue));
				}
			}
			index++;
		}
		monadService.listAdd(globalList, Global.class);
	}
	
	public Global setEssentialGlobal(String variable, String value){
		Global global = new Global();
		global.setUid(variable);
		global.setValue(value);
		
		return global;
	}
	
	private String getServerName(){
		String hostname = "";
	    try {
	        InetAddress addr = InetAddress.getLocalHost();
	        // Get hostname
	        hostname = addr.getHostName();
			log.info("no error hostname:"+hostname);
	    } catch (UnknownHostException e) {
					log.error(e, e);
					try {
						// Get hostname by textual representation of IP address
						InetAddress addr = InetAddress.getByName("127.0.0.1");
						// /[127.0.0.1 is always localhost -dmw]/

						// Get the host name from the address
						hostname = addr.getHostName();

						// Get canonical host name
						String hostnameCanonical = addr.getCanonicalHostName();
						log.info("hostname:"+hostname+" || hostnameCanonical:"+hostnameCanonical);
						}
						catch (UnknownHostException e2) {
							// handle exception
							log.error(e2, e2);
						}
	    } 
	    return hostname;
	}
}
