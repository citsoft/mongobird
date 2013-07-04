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

import static net.cit.tetrad.common.ColumnConstent.COLL_DASHBOARD;
import static net.cit.tetrad.common.ColumnConstent.COL_DEVICECODE;
import static net.cit.tetrad.common.ColumnConstent.DBSTATUS_DBNAME;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_REGTIME;
import static net.cit.tetrad.common.ColumnConstent.SUB_GRAPH_HEIGHT;
import static net.cit.tetrad.common.ColumnConstent.SUB_GRAPH_WIDTH;
import static net.cit.tetrad.common.ColumnConstent.SUB_SMALL_GRAPH_HEIGHT;
import static net.cit.tetrad.common.ColumnConstent.SUB_SMALL_GRAPH_WIDTH;
import static net.cit.tetrad.common.ColumnConstent.COLL_DBSTATUS;
import static net.cit.tetrad.utility.QueryUtils.setDeviceCode;
import static net.cit.tetrad.utility.QueryUtils.setIdx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import net.cit.tetrad.common.DateUtil;
import net.cit.tetrad.dao.management.MainDao;
import net.cit.tetrad.dao.management.SubDao;
import net.cit.tetrad.model.CommonDto;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.model.GraphDto;
import net.cit.tetrad.monad.MonadService;
import net.cit.tetrad.rrd.bean.DbStatus;
import net.cit.tetrad.rrd.bean.GraphDefInfo;
import net.cit.tetrad.rrd.bean.ServerStatus;
import net.cit.tetrad.rrd.dao.CriticalHelper;
import net.cit.tetrad.rrd.utils.TetradRrdConfig;
import net.cit.tetrad.rrd.utils.TimestampUtil;
import net.cit.tetrad.utility.StringUtils;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;

import com.mongodb.BasicDBObject;
import com.mongodb.CommandResult;
import com.mongodb.DBObject;

public class SubDaoImpl implements SubDao{
	private Logger dhtmlxLogger = Logger.getLogger("dhtmlx");
	private MonadService monadService;
	private MainDao mainDao;
	
	public void setMonadService(MonadService monadService) {
		this.monadService = monadService;
	}
	public void setMainDao(MainDao mainDao) {
		this.mainDao = mainDao;
	}
	private Logger log = Logger.getLogger(this.getClass());
	@SuppressWarnings("unchecked")
	public void convertValue(GraphDto graphDto) {
		int interval = Integer.parseInt(TetradRrdConfig.getTetradRrdConfig("default_log_generation_interval"));
		List<HashMap<String, Object>> newStatusList = new ArrayList<HashMap<String, Object>>();
		List<HashMap<String, Object>> statusList = (List<HashMap<String, Object>>) graphDto.getStatusListObj();
		
		for (HashMap<String, Object> obj : statusList) {
			HashMap<String, Object> status = new HashMap<String, Object>();
			
			Set<String> keys = obj.keySet();
			Iterator<String> it = keys.iterator();
			while(it.hasNext()){
				String key = it.next().toString();
				Object value = obj.get(key);
				Object newValue = value;
				if (value instanceof java.lang.Double) {
					double dvalue = (Double) value;
					if (!Double.isNaN(dvalue)) newValue = dvalue * interval;
				}
				status.put(key, newValue);
			}
			newStatusList.add(status);
		}		
		graphDto.setStatusListObj(newStatusList);
	}

	
	/**
	 * 서브 팝업 그래프 검색 정보 생성하기
	 * @param dto
	 * @return gdInfo
	 * @throws Exception
	 */
	public GraphDefInfo getGraphDefInfoForSubGraph(CommonDto dto){
		GraphDefInfo gdInfo = new GraphDefInfo();

		try  {
			String graphPeriod = StringUtils.getDefaultValueIfNull(dto.getGraph_period(), "1");
			dto.setGraph_period(graphPeriod);
			String startDate = dto.getSdate();
			int deviceCode = dto.getDeviceCode();
			String dsname = dto.getDsname();
			
			Query query = setIdx(deviceCode);
			Device ddto = (Device) monadService.getFind(query,  Device.class);
			dto.setUid(ddto.getUid());

			Date stDate =  DateUtil.plusHour(-2);
			Date edDate = new Date();
			
			long startTime =  mainDao.getSubGraphSearchTime(graphPeriod, DateUtil.getCurrentDate(stDate, "yyyy-MM-dd"), stDate.getHours()+"", stDate.getMinutes()+"");
			long endTime = mainDao.getSubGraphSearchTime(graphPeriod, DateUtil.getCurrentDate(edDate, "yyyy-MM-dd"), edDate.getHours()+"", edDate.getMinutes()+"");
			
			if(!StringUtils.isNull(startDate)){				
				startTime = mainDao.getSubGraphSearchTime(graphPeriod, startDate, dto.getGraph_shour(), dto.getGraph_smin());
				endTime = mainDao.getSubGraphSearchTime(graphPeriod, dto.getEdate(), dto.getGraph_ehour(), dto.getGraph_emin());				
			} else {
				String stFullDate = DateUtil.getCurrentDate(stDate, "yyyy-MM-dd HH:mm");
				String edFullDate = DateUtil.getCurrentDate(edDate, "yyyy-MM-dd HH:mm");
				
				String[] stSplitDate = stFullDate.split(" ");
				String[] edSplitDate = edFullDate.split(" ");
				dto.setSdate(stSplitDate[0]);
				dto.setEdate(edSplitDate[0]);
				
				String[] stSplitTime = stSplitDate[1].split(":");
				String[] edSplitTime = edSplitDate[1].split(":");
				
				dto.setGraph_shour(stSplitTime[0]);
				dto.setGraph_smin(stSplitTime[1]);
				dto.setGraph_ehour(edSplitTime[0]);
				dto.setGraph_emin(edSplitTime[1]);
			}
 
			String[] filters = mainDao.setFilters(dsname, graphPeriod, "");
			String[] graphLegend = mainDao.setGraphLegend(dsname);
			
			gdInfo.setFileName(dsname);
			gdInfo.setDevice(ddto);
			gdInfo.setStartTime(startTime);
			gdInfo.setEndTime(endTime);
			gdInfo.setFilters(filters);
			gdInfo.setGraphLegend(graphLegend);
			gdInfo.setWidth(SUB_SMALL_GRAPH_WIDTH);
			gdInfo.setHeight(SUB_SMALL_GRAPH_HEIGHT);
			gdInfo.setAxisTimeUnitDiv(graphPeriod);
		} catch (Exception e) {
			log.error(e, e);
		}
		
		return gdInfo;			
	}
	
	/**
	 * 서브 팝업 그래프 검색 정보 생성하기
	 * @param dto
	 * @return gdInfo
	 * @throws Exception
	 */
	public GraphDefInfo getNoDeviceGraphDefInfoForSubGraph(CommonDto dto){
		GraphDefInfo gdInfo = new GraphDefInfo();

		try  {
			String graphPeriod = StringUtils.getDefaultValueIfNull(dto.getGraph_period(), "1h");
			String dsType = StringUtils.getDefaultValueIfNull(dto.getDstype(), "");
			dto.setGraph_period(graphPeriod);
			String startDate = dto.getSdate();
			String endDate = dto.getEdate();
			String dsname = dto.getDsname();
			
			long	startTime = TimestampUtil.readTimestamp(startDate, "yyyy-MM-dd HH:mm");
			long	endTime = TimestampUtil.readTimestamp(endDate, "yyyy-MM-dd HH:mm");
 
			String[] filters = mainDao.setFilters(dsname, graphPeriod,dsType);
			String[] graphLegend = mainDao.setGraphLegend(dsname);
			
			gdInfo.setFileName(dsname);
			gdInfo.setStartTime(startTime);
			gdInfo.setEndTime(endTime);
			gdInfo.setFilters(filters);
			gdInfo.setGraphLegend(graphLegend);
			gdInfo.setStep(setStep(dto.getGraph_step()));
			gdInfo.setConsolFun(dto.getConsolFun());
			if(dto.getSortItem().equals("big")){
				gdInfo.setWidth(SUB_GRAPH_WIDTH);
				gdInfo.setHeight(SUB_GRAPH_HEIGHT);
			}else if(dto.getSortItem().equals("daemon")){
				gdInfo.setWidth(205);
				gdInfo.setHeight(100);
			}else if(dto.getSortItem().equals("event")){
				gdInfo.setWidth(233);
				gdInfo.setHeight(141);
			}else{
				gdInfo.setWidth(SUB_SMALL_GRAPH_WIDTH);
				gdInfo.setHeight(SUB_SMALL_GRAPH_HEIGHT);
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		
		return gdInfo;			
	}

	/**
	 * lock 그래프 검색 정보 생성하기
	 * @param dto
	 * @return gdInfo
	 * @throws Exception
	 */
	public GraphDefInfo getLockGraphDefInfoForSubGraph(CommonDto dto){
		GraphDefInfo gdInfo = new GraphDefInfo();

		try  {
			String graphPeriod = StringUtils.getDefaultValueIfNull(dto.getGraph_period(), "1h");
			dto.setGraph_period(graphPeriod);
			String startDate = dto.getSdate();
			String endDate = dto.getEdate();
			String dbname = dto.getDbname();
			
			long	startTime = TimestampUtil.readTimestamp(startDate, "yyyy-MM-dd HH:mm");
			long	endTime = TimestampUtil.readTimestamp(endDate, "yyyy-MM-dd HH:mm");
 
			String[] filters = new String[]{dbname};
			String[] graphLegend = new String[]{"DB lock"};
			
			gdInfo.setStartTime(startTime);
			gdInfo.setEndTime(endTime);
			gdInfo.setFilters(filters);
			gdInfo.setGraphLegend(graphLegend);
			gdInfo.setStep(setStep(dto.getGraph_step()));
			gdInfo.setConsolFun(dto.getConsolFun());
			if(dto.getSortItem().equals("big")){
				gdInfo.setWidth(SUB_GRAPH_WIDTH);
				gdInfo.setHeight(SUB_GRAPH_HEIGHT);
			}else if(dto.getSortItem().equals("daemon")){
				gdInfo.setWidth(205);
				gdInfo.setHeight(100);
			}else if(dto.getSortItem().equals("event")){
				gdInfo.setWidth(233);
				gdInfo.setHeight(141);
			}else{
				gdInfo.setWidth(SUB_SMALL_GRAPH_WIDTH);
				gdInfo.setHeight(SUB_SMALL_GRAPH_HEIGHT);
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		
		return gdInfo;			
	}

	/**
	 * db 그래프 검색 정보 생성하기
	 * @param dto
	 * @return gdInfo
	 * @throws Exception
	 */
	public GraphDefInfo getDbGraphDefInfoForSubGraph(CommonDto dto){
		GraphDefInfo gdInfo = new GraphDefInfo();

		try  {
			String graphPeriod = StringUtils.getDefaultValueIfNull(dto.getGraph_period(), "1h");
			dto.setGraph_period(graphPeriod);
			String startDate = dto.getSdate();
			String endDate = dto.getEdate();
			String gubun = dto.getSearch_gubun();
			String dsType = dto.getDstype();
			
			long	startTime = TimestampUtil.readTimestamp(startDate, "yyyy-MM-dd HH:mm");
			long	endTime = TimestampUtil.readTimestamp(endDate, "yyyy-MM-dd HH:mm");

			String[] filters = mainDao.setDbFilters(dsType, gubun);
			String[] graphLegend = mainDao.setDbGraphLegend(gubun);

			gdInfo.setFileName(gubun);
			gdInfo.setStartTime(startTime);
			gdInfo.setEndTime(endTime);
			gdInfo.setFilters(filters);
			gdInfo.setGraphLegend(graphLegend);
			gdInfo.setStep(setStep(dto.getGraph_step()));
			gdInfo.setConsolFun(dto.getConsolFun());
			if(dto.getSortItem().equals("big")){
				gdInfo.setWidth(SUB_GRAPH_WIDTH);
				gdInfo.setHeight(SUB_GRAPH_HEIGHT);
			}else if(dto.getSortItem().equals("daemon")){
				gdInfo.setWidth(205);
				gdInfo.setHeight(100);
			}else if(dto.getSortItem().equals("event")){
				gdInfo.setWidth(233);
				gdInfo.setHeight(141);
			}else{
				gdInfo.setWidth(SUB_SMALL_GRAPH_WIDTH);
				gdInfo.setHeight(SUB_SMALL_GRAPH_HEIGHT);
			}
		} catch (Exception e) {
			log.error(e, e);
		}
		
		return gdInfo;			
	}
	
	/**
	 * step을 long형으로 변경
	 * @param stepStr
	 * @return
	 */
	public long setStep(String stepStr){
		long stepLong = 1*10;
		if(stepStr.equals("min")){
			stepLong = 1*6 * 10;
		}else if(stepStr.equals("5min")){
			stepLong = 1*6*5*10;
		}else if(stepStr.equals("30min")){
			stepLong = 1*6*30*10;
		}else if(stepStr.equals("hour")){
			stepLong = 1*6*60*10;
		}
		return stepLong;
	}
	
	public Object getIncludeOptionResult(CommonDto dto) {
		dhtmlxLogger.info("start getIncludeOptionResult");
		String dsname = dto.getDsname();
		
		Query query = new Query(Criteria.where(SERVERSTATUS_REGTIME).gt(dto.getSearch_sdate()).lte(dto.getSearch_edate()));
		query.addCriteria(Criteria.where(COL_DEVICECODE).is(dto.getDeviceCode()));
		if (dto.getCollname().equals(COLL_DBSTATUS)) query.addCriteria(Criteria.where(DBSTATUS_DBNAME).is(dto.getDbname()));
		query.sort().on(SERVERSTATUS_REGTIME, Order.ASCENDING);
		query.fields().include("_id").include(SERVERSTATUS_REGTIME).include(dsname).include(COL_DEVICECODE);
		
		List<Map<String, Object>> result = new ArrayList<Map<String, Object>>();
		
		long start = System.currentTimeMillis();
		List<Object> serverStatusList = monadService.getListWithStrCollName(query, Map.class, dto.getCollname());
		double old = -1;
		for (Object obj : serverStatusList) {
			Map<String, Object> setInfo = new HashMap<String, Object>();
			Map<String, Object> statusInfo = (Map<String, Object>)obj;
			double recent = CriticalHelper.convertToDouble(statusInfo.get(dsname));
			String regtime = (String) statusInfo.get(SERVERSTATUS_REGTIME);
			int deviceCode = (Integer) statusInfo.get(COL_DEVICECODE);
			if (old == -1) {
				setInfo.put(dsname, 0);
			} else {
				setInfo.put(dsname, recent -  old);
			}
			setInfo.put(COL_DEVICECODE, deviceCode);
			setInfo.put(SERVERSTATUS_REGTIME, regtime);
			old = recent;
			result.add(setInfo);	
		}
		long end = System.currentTimeMillis();
		dhtmlxLogger.info("    getDiff Data : " + (end - start));
		String collname = "temp_"+TimestampUtil.readTimestamp();
		start = System.currentTimeMillis();
		monadService.insert(result, collname);
		end = System.currentTimeMillis();
		dhtmlxLogger.info("    insertDiff Data : " + (end - start));
		dto.setCollname(collname);
		Object obj = getCurrentResult(dto);
		
		// 임시 컬렉션 삭제
		dhtmlxLogger.info("drop temp collection!");
		monadService.dropCollection(collname);
		dhtmlxLogger.info("end getIncludeOptionResult");
		return obj;
	}
	
	public Object getAccumResult(CommonDto dto) {
		dhtmlxLogger.info("start getAccumResult");
		long start = System.currentTimeMillis();
		List<BasicDBObject> pipeline = commonQuery(dto);
				
		DBObject groupKey = new BasicDBObject("defaultGroupKey", "$groupkey1");
		groupKey.put("groupKey2", "$groupkey2");
		DBObject group = new BasicDBObject("_id", groupKey);
		
		DBObject colcounter = new BasicDBObject("$sum", 1);		
		DBObject colfirst = new BasicDBObject("$first", new BasicDBObject("$ifNull", Arrays.asList("$dsname", 0)));
		DBObject collast = new BasicDBObject("$last", new BasicDBObject("$ifNull", Arrays.asList("$dsname", 0)));
		DBObject regtime = new BasicDBObject("$first", "$regtime");
		
		group.put("colcounter", colcounter);
		group.put("colfirst", colfirst);
		group.put("collast", collast);
		group.put("regtime", regtime);
		
		DBObject finalSort = new BasicDBObject("_id", 1);
		
		DBObject resultProject = new BasicDBObject();
		resultProject.put("couter", "$colcounter");
		resultProject.put("first", "$colfirst");
		resultProject.put("last", "$collast");
		resultProject.put("diff", new BasicDBObject("$subtract", Arrays.asList("$collast", "$colfirst")));
		resultProject.put("regtime", "$regtime");
		
		DBObject finalProject = new BasicDBObject();
		finalProject.put("avg", new BasicDBObject("$divide", Arrays.asList("$diff", "$couter")));
		finalProject.put("point", "$last");
		finalProject.put("diff", "$diff");
		finalProject.put("regtime", "$regtime");
		
		pipeline.add(new BasicDBObject("$group", group));
		pipeline.add(new BasicDBObject("$sort", finalSort));
		pipeline.add(new BasicDBObject("$project", resultProject));
		pipeline.add(new BasicDBObject("$project", finalProject));
		
		DBObject cmdBody = new BasicDBObject("aggregate", dto.getCollname()); 
		cmdBody.put("pipeline", pipeline);
		
		CommandResult result = monadService.command(cmdBody);
		long end = System.currentTimeMillis();
		dhtmlxLogger.info("    accum aggregation : " + (end - start));
		dhtmlxLogger.info("    accum aggregation query : " + cmdBody);
		dhtmlxLogger.info("end getAccumResult");
		return result.get("result");
	}
	
	public Object getCurrentResult(CommonDto dto) {
		dhtmlxLogger.info("start getCurrentResult");
		long start = System.currentTimeMillis();
		List<BasicDBObject> pipeline = commonQuery(dto);
		
		DBObject groupKey = new BasicDBObject("defaultGroupKey", "$groupkey1");
		groupKey.put("groupKey2", "$groupkey2");
		
		DBObject group = new BasicDBObject("_id", groupKey);
		DBObject colsum = new BasicDBObject("$sum", new BasicDBObject("$ifNull", Arrays.asList("$dsname", 0)));
		DBObject colavg = new BasicDBObject("$avg", new BasicDBObject("$ifNull", Arrays.asList("$dsname", 0)));
		DBObject colfirst = new BasicDBObject("$first", new BasicDBObject("$ifNull", Arrays.asList("$dsname", 0)));
		DBObject collast = new BasicDBObject("$last", new BasicDBObject("$ifNull", Arrays.asList("$dsname", 0)));
		DBObject regtime = new BasicDBObject("$first", "$regtime");
		
		group.put("colsum", colsum);
		group.put("colavg", colavg);
		group.put("colfirst", colfirst);
		group.put("collast", collast);
		group.put("regtime", regtime);
		
		DBObject finalSort = new BasicDBObject("_id", 1);
		
		DBObject resultProject = new BasicDBObject();
		resultProject.put("avg", "$colavg");
		resultProject.put("point", "$collast");
		resultProject.put("regtime", "$regtime");
		resultProject.put("diff", new BasicDBObject("$subtract", Arrays.asList("$collast", "$colfirst")));
		
		pipeline.add(new BasicDBObject("$group", group));
		pipeline.add(new BasicDBObject("$sort", finalSort));
		pipeline.add(new BasicDBObject("$project", resultProject));
		
		DBObject cmdBody = new BasicDBObject("aggregate", dto.getCollname()); 
		cmdBody.put("pipeline", pipeline);
		CommandResult result = monadService.command(cmdBody);
		long end = System.currentTimeMillis();
		dhtmlxLogger.info("    current aggregation : " + (end - start));
		dhtmlxLogger.info("    current aggregation query : " + cmdBody);
		dhtmlxLogger.info("end getCurrentResult");
		return result.get("result");
	}
	
	
	private List<BasicDBObject> commonQuery(CommonDto dto) {
		dhtmlxLogger.info("start make commonQuery");
		String period = dto.getGraph_period();
		String dsname = dto.getDsname();
		
		DBObject match1 = new BasicDBObject();
		if (dto.getCollname().equals(COLL_DBSTATUS)) match1.put(DBSTATUS_DBNAME, dto.getDbname());
		
		DBObject timeRange = new BasicDBObject();
		timeRange.put("$gt", dto.getSearch_sdate());
		timeRange.put("$lte", dto.getSearch_edate());		
		match1.put(SERVERSTATUS_REGTIME, timeRange);
		
		DBObject match2 = new BasicDBObject("deviceCode", dto.getDeviceCode());
		DBObject extract = new BasicDBObject("dsname", "$"+ dsname);
		extract.put("regtime", "$"+SERVERSTATUS_REGTIME);
		DBObject substr = new BasicDBObject("$substr", Arrays.asList("$"+SERVERSTATUS_REGTIME, 10, 2));
		extract.put("min", substr);
		
		DBObject project = new BasicDBObject("dsname", "$dsname");
		project.put("regtime", "$regtime");
		project.put("groupkey1", new BasicDBObject("$substr", Arrays.asList("$regtime", 0, 10)));
		DBObject cond = getGroupCond(period);
		project.put("groupkey2", cond);
		
		List<BasicDBObject> pipeline = new ArrayList<BasicDBObject>();
		pipeline.add(new BasicDBObject("$match", match1));
		pipeline.add(new BasicDBObject("$match", match2));
		pipeline.add(new BasicDBObject("$project", extract));
		pipeline.add(new BasicDBObject("$project", project));
		dhtmlxLogger.info("end make commonQuery");
		return pipeline;
	}
		
	private BasicDBObject getGroupCond(String period) {
		if (period.equals("1")) {
			return getSubstr(12);
		} else if (period.equals("5")) {
			return getCond(55, 11, 5);			
		} else if (period.equals("30")) {
			return getCond(29, 1, 30);
		} else {
			return getSubstr(10);
		}
	}
	
	private BasicDBObject getCond(int targetValue, int groupValue, int diff) {
		if (groupValue == 1) {
			return new BasicDBObject("$cond", Arrays.asList(new BasicDBObject("$gte", Arrays.asList("$min", targetValue+"")) , groupValue, 0)); 
		} else {
			return new BasicDBObject("$cond", Arrays.asList(new BasicDBObject("$gte", Arrays.asList("$min", targetValue+"" )), groupValue, getCond(targetValue-diff, groupValue-1, diff )));
		}
	}
	
	private BasicDBObject getSubstr(int endPoint) {
		return new BasicDBObject("$substr", Arrays.asList("$regtime", 0,endPoint));
	}
	
	
	
//	private int getSubStr(String period) {
//		int suffixPoint = 12;
//		if (period.equals(SUBGRAPH_HOUR)) {
//			suffixPoint = 10;
//		}else if (period.equals(SUBGRAPH_DAY)) {
//			suffixPoint = 8;
//		}
//		return suffixPoint;
//	}
	
	private int getlimitCount(String period) {
		int limitCount = 180;
//		int limitCount = 18;
		if (period.equals("5")) {
			limitCount = 900;
		}else if (period.equals("30")) {
			limitCount = 180*30;
		}else if (period.equals("60")) {
			limitCount = 360*30;
		}
		return limitCount;
	}

	public List<DbStatus> dbLstNan(int deviceCode){
		Query query = setDeviceCode(deviceCode);
		ServerStatus serverStatus = new ServerStatus();
		serverStatus = (ServerStatus) monadService.getFindOne(query, ServerStatus.class, COLL_DASHBOARD);		

		List<DbStatus> dbStatus = new ArrayList<DbStatus>();
		if(serverStatus!=null)dbStatus = serverStatus.getDbInfos();
		
		for(DbStatus db : dbStatus){
			Double avg = db.getAvgObjSize();
			if(avg.isNaN()){
				db.setAvgObjSize(0.0d);
			}
		}
		return dbStatus;
	}
	
}
