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
package net.cit.tetrad.resource;

import static net.cit.tetrad.common.ColumnConstent.COLL_DASHBOARD;
import static net.cit.tetrad.common.ColumnConstent.COLL_DBSTATUS;
import static net.cit.tetrad.common.ColumnConstent.COLL_SERVERSTATUS;
import static net.cit.tetrad.common.ColumnConstent.DHTML_SEARCH_RANGE_1MIN;
import static net.cit.tetrad.common.ColumnConstent.DHTML_SEARCH_RANGE_30MIN;
import static net.cit.tetrad.common.ColumnConstent.DHTML_SEARCH_RANGE_5MIN;
import static net.cit.tetrad.common.ColumnConstent.DHTML_SEARCH_RANGE_60MIN;
import static net.cit.tetrad.common.ColumnConstent.DHTML_TOTAL_RANGE_1MIN;
import static net.cit.tetrad.common.ColumnConstent.DHTML_TOTAL_RANGE_30MIN;
import static net.cit.tetrad.common.ColumnConstent.DHTML_TOTAL_RANGE_5MIN;
import static net.cit.tetrad.common.ColumnConstent.DHTML_TOTAL_RANGE_60MIN;
import static net.cit.tetrad.common.ColumnConstent.DHTML_XUNIT_CRITERION_NUM;
import static net.cit.tetrad.common.ColumnConstent.GRAPH_ACCUM;
import static net.cit.tetrad.common.ColumnConstent.GRAPH_CURENT;
import static net.cit.tetrad.common.ColumnConstent.REQ_SECHO;
import static net.cit.tetrad.common.ColumnConstent.DEVICE_TYPE;
import static net.cit.tetrad.common.ColumnConstent.PROCESS_MONGOS;
import static net.cit.tetrad.common.PropertiesNames.TYPE;
import static net.cit.tetrad.utility.QueryUtils.setAlarmCode;
import static net.cit.tetrad.utility.QueryUtils.setAlarmSearch;
import static net.cit.tetrad.utility.QueryUtils.setConfirm;
import static net.cit.tetrad.utility.QueryUtils.setDeviceCode;
import static net.cit.tetrad.utility.QueryUtils.setIdx;
import static org.springframework.data.mongodb.core.query.Criteria.where;

import java.io.Writer;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cit.tetrad.common.DateUtil;
import net.cit.tetrad.common.Utility;
import net.cit.tetrad.model.Alarm;
import net.cit.tetrad.model.CommonDto;
import net.cit.tetrad.model.Confirm;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.model.DhtmlJson;
import net.cit.tetrad.model.GraphDto;
import net.cit.tetrad.model.Group;
import net.cit.tetrad.model.PersonJson;
import net.cit.tetrad.rrd.bean.DbStatus;
import net.cit.tetrad.rrd.bean.GraphDefInfo;
import net.cit.tetrad.rrd.bean.ServerStatus;
import net.cit.tetrad.rrd.rule.AllStatusRule;
import net.cit.tetrad.rrd.rule.StatusDatasourceName;
import net.cit.tetrad.utility.StringUtils;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mongodb.DBObject;

@Controller
public class SubResource extends DefaultResource {

	private Logger log = Logger.getLogger(this.getClass());
	
	public SubResource(){}

	/**
	 * 메인 장비 상세 보기
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/subPageView.do")
	public ModelAndView subPageView(CommonDto dto) throws Exception{
		log.debug("start - subPageView()");
		ModelAndView mav = commMav();
		Query query = new Query();
		List<Object> groupLst = monadService.getList(query, Group.class);
		List<Object> typeLst = new ArrayList<Object>();
		typeLst = adminDao.typeList();
		
		mav.addObject("type", typeLst);
		mav.addObject("group", groupLst);
		mav.addObject("comm", dto);
		
		mav.setViewName("sub_page");
		
		log.debug("end - subPageView()");
		return mav;
	}
	
	/**
	 * 샤드 정보 페이지 보기
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/subShardInfoView.do")
	public ModelAndView subShardInfoView(CommonDto dto) throws Exception{
		log.debug("start - subShardInfoView()");
		ModelAndView mav = commMav();
		List<Object> deviceLst = monadService.getList(new Query(where(DEVICE_TYPE).is(StringUtils.getEncStr(PROCESS_MONGOS))), Device.class);
		Device device = new Device();
		if(dto.getDeviceCode()==0 && deviceLst.size() != 0){
			device = (Device) deviceLst.get(0);
			dto.setDeviceCode(device.getIdx());
			dto.setAutoRefresh("off");
		}
		
		mav.addObject("deviceLst", deviceLst);
		mav.addObject("comm", dto);
		
		mav.setViewName("subShardInfoPage");
		log.debug("end - subShardInfoView()");
		return mav;
	}
	
	@RequestMapping("/shardCllectionCommand.do")
	public void shardCllectionCommand(HttpServletRequest request, HttpServletResponse response) throws Exception{
		int deviceCode = Integer.parseInt(Utility.isNullNumber(request.getParameter("deviceCode")));
		String collectionName = request.getParameter("collectionName");
		
		List<Object> findMongoCollections = cllectionCommand(deviceCode, collectionName);
		
		PersonJson result = new PersonJson();
		result.setAaData(findMongoCollections);
		
		JSONObject jsonObject = JSONObject.fromObject(result);
		
		Writer writer = setResponse(response).getWriter();
		writer.write(jsonObject.toString());
		
		writer.flush();
			
		log.debug("end - shardCllectionCommand()");
	}

	private	List<Object> chunkResultLst = new ArrayList<Object>();
	@SuppressWarnings("unchecked")
	@RequestMapping("/shardChunkLstCommand.do")
	public void shardChunkLstCommand(HttpServletRequest request, HttpServletResponse response) throws Exception{
		int deviceCode = Integer.parseInt(Utility.isNullNumber(request.getParameter("deviceCode")));
		String dbNameParam  = request.getParameter("dbNameParam");

		int pageNumber = Integer.parseInt(Utility.isNullNumber(request.getParameter("iDisplayStart")));
		int nPerPage = Integer.parseInt(Utility.isNullNumber(request.getParameter("iDisplayLength")));
		log.debug("pageNumber=" + pageNumber + ", nPerPage=" + nPerPage);
		
		Enumeration parameter = request.getParameterNames();
		log.debug(parameter.toString());
		while (parameter.hasMoreElements()) {
			String pName = (String)parameter.nextElement();
			String pValue = request.getParameter(pName);
			log.debug(pName + " = " + pValue);
		}
		
		int sEcho = Integer.parseInt(Utility.isNullNumber(request.getParameter(REQ_SECHO)));
		
		List<Object> resultLst = new ArrayList<Object>();
		//페이징을 하기위해 pageNumber가 0일 때만 chunkResultLst를 설정한다.
		if(pageNumber == 0){
			List<Object> findMongoCollections = cllectionCommand(deviceCode, "collections");
				JSONObject js = new JSONObject();
			for(Object collObj : findMongoCollections){
				String coll = (String) ((Map<String, Object>) collObj).get("_id");
				String[] collSplit = coll.split("\\.");
				if(dbNameParam.equals(collSplit[0])){
					List<Object> lst = comandService.chunksGrpCommand(deviceCode, coll, new ArrayList<Object>());
					js = new JSONObject();
					List<Object> chunkInfoGroup = new ArrayList<Object>();
					double totalChunkCnt = 0;
					String collName = null;
					double nChuncks = 0;
					for(Object lstObj : lst){
						DBObject dbo = (DBObject) lstObj;
						collName = (String) dbo.get("collName");
						nChuncks = (Double) dbo.get("nChunks");
						totalChunkCnt += nChuncks;
						chunkInfoGroup.add(dbo);
					}
					js.put("collName", collName);
					js.put("totalChunkCnt", totalChunkCnt);
					js.put("chunkInfoGroup", chunkInfoGroup);
					resultLst.add(js);
				}
			}
			chunkResultLst = resultLst;
		}
		
		int chunkResultLstSize = chunkResultLst.size();
		//chunkResultLst의 값을 pageNumber와 nPerPage에 따라 선택된 갯수만큼 출력 리스트에 담는다.
		int count = pageNumber+nPerPage;
		resultLst = new ArrayList<Object>();
		for(int i = pageNumber; i < count; i++){
			if(chunkResultLstSize <= i)break;
			resultLst.add(chunkResultLst.get(i));
		}
		
		PersonJson result = new PersonJson();
		result.setAaData(resultLst);
		result.setsEcho(sEcho);
		result.setiTotalRecords(chunkResultLstSize);
		result.setiTotalDisplayRecords(chunkResultLstSize);
		
		JSONObject jsonObject = JSONObject.fromObject(result);
		
		Writer writer = setResponse(response).getWriter();
		writer.write(jsonObject.toString());
		
		writer.flush();
			
		log.debug("end - shardChunkLstCommand()");
	}
	
	@RequestMapping("/shardDbStatus.do")
	public void shardDbStatus(HttpServletRequest request, HttpServletResponse response) throws Exception{
		int deviceCode = Integer.parseInt(Utility.isNullNumber(request.getParameter("deviceCode")));
		
		List<DbStatus> dbStatusLst = subDao.dbLstNan(deviceCode);
		PersonJson result = new PersonJson();
		result.setAaData(dbStatusLst);
		
		JSONObject jsonObject = JSONObject.fromObject(result);
		
		Writer writer = setResponse(response).getWriter();
		writer.write(jsonObject.toString());
		
		writer.flush();
			
		log.debug("end - shardDbStatus()");
	}
	
	public List<Object> cllectionCommand(int deviceCode, String collectionName){
		List<Object> findMongoCollections = new ArrayList<Object>();
		if(deviceCode != 0){
			findMongoCollections = comandService.collectionCommand(deviceCode, collectionName);
		}		
		return findMongoCollections;
	}
	
	/**
	 * 서브 팝업 그래프 데이터 가져오기
	 * @param dto
	 * @return graphDto
	 * @throws Exception
	 */
	private GraphDto getSubGraphList(GraphDefInfo gdInfo,CommonDto dto) {
		GraphDto graphDto = new GraphDto();

		try  {			
			graphDto.setDsname(dto.getDsname());
			List<HashMap<String, Object>> dbstatus = rrdService.fetchTetradRrdDb(gdInfo.getDevice(), gdInfo.getFilters(), gdInfo.getStartTime(), gdInfo.getEndTime(), dto.getDbname());
			graphDto.setStatusListObj(dbstatus);
			graphDto.setStatusSize(dbstatus.size());
			
		} catch (Exception e) {
			e.printStackTrace();
			log.error(e, e);
		}
		
		return graphDto;
	}
	
	/**
	 * 서브 팝업 그래프 데이터 가져와 json 출력하기
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings({ "unchecked", "rawtypes" })
	@RequestMapping("/subGraphList.do")
	public void subGraphList(HttpServletRequest request, HttpServletResponse response,CommonDto dto) throws Exception{
		log.debug("start - subGraphList()");
		
		//iDisplayStart와 iDisplayLength는 datatable에서 페이징을 하기 위해 가지고 있는 값
		int pageNumber = Integer.parseInt(Utility.isNullNumber(request.getParameter("iDisplayStart")));
		int nPerPage = Integer.parseInt(Utility.isNullNumber(request.getParameter("iDisplayLength")));
		log.debug("pageNumber=" + pageNumber + ", nPerPage=" + nPerPage);
		
		Enumeration parameter = request.getParameterNames();
		log.debug(parameter.toString());
		while (parameter.hasMoreElements()) {
			String pName = (String)parameter.nextElement();
			String pValue = request.getParameter(pName);
			log.debug(pName + " = " + pValue);
		}
		
		GraphDefInfo gdInfo = subDao.getGraphDefInfoForSubGraph(dto);
		GraphDto graphDto = getSubGraphList(gdInfo,dto);
		
		List<Object> statusList = (List<Object>) graphDto.getStatusListObj();
		int cnt = statusList.size();
		Class<?> classname;
		if (dto.getDsname().equals("db")) {
			classname = DbStatus.class;
		}else{
			classname = ServerStatus.class;
		}

		try{
			int sEcho = Integer.parseInt(Utility.isNullNumber(request.getParameter(REQ_SECHO)));
			
			PersonJson result = setPersonJson(cnt, sEcho, pageNumber, nPerPage, statusList);
			JSONObject jsonObject = JSONObject.fromObject(result);
			
			Writer writer = setResponse(response).getWriter();
			writer.write(jsonObject.toString());
			
			log.debug(jsonObject.toString());
			writer.flush();
		}catch(Exception e){
			log.error(e, e);
		}

		log.debug("end - subGraphList()");
		
	}
	
	@RequestMapping("/serverDate.do")
	public void serverDate(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String strDate = DateUtil.getTime();
		
		Writer writer = setResponse(response).getWriter();
		writer.write(strDate);
		
		writer.flush();
			
		log.debug("end - serverDate()");
	}
	
	private Date defaultStartDate(String period){
		Date stDate = DateUtil.plusHour(-1);
		if (period.equals("2h")) {
			stDate = DateUtil.plusHour(-2);
		} else if (period.equals("3h")) {
			stDate = DateUtil.plusHour(-3);
		} else if (period.equals("6h")) {
			stDate = DateUtil.plusHour(-6);
		} else if (period.equals("12h")) {
			stDate = DateUtil.plusHour(-12);
		} else if (period.equals("1d")) {
			stDate = DateUtil.plusDay(-1);
		} else if (period.equals("1w")) {
			stDate = DateUtil.plusDay(-7);
		} else if (period.equals("2w")) {
			stDate = DateUtil.plusDay(-14);
		} else if (period.equals("1m")) {
			stDate = DateUtil.plusDay(-30);
		} else if (period.equals("all")) {
			stDate = DateUtil.plusDay(-1 * logRetentionPeriod);
		}
		return stDate;
	}
	
	@RequestMapping("/setNowGraphDate.do")
	public void setNowGraphDate(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String period = request.getParameter("period");
		String sdateStr = request.getParameter("sdateStr");
		String edateStr = request.getParameter("edateStr");
		String format = "yyyy-MM-dd HH:mm";
		
		int diffMins = DateUtil.getOffsetMins(edateStr, sdateStr) * -1;
		Date stDate = DateUtil.plusMinute(diffMins);
		Date edDate = new Date();
		Date fullStDate = defaultStartDate(period);
		
		String strStDate = DateUtil.getCurrentDate(stDate, format);
		String strEdDate = DateUtil.getCurrentDate(edDate, format);
		String strFullStDate = DateUtil.getCurrentDate(fullStDate, format);
		
		String result = strFullStDate + "|" + strStDate + "|" + strEdDate;
		
		Writer writer = setResponse(response).getWriter();
		writer.write(result);
		
		writer.flush();
			
		log.debug("end - setNowGraphDate()");
	}
	
	@RequestMapping("/setDefaultGraphDate.do")
	public void setDefaultGraphDate(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String period = request.getParameter("period");
		String format = "yyyy-MM-dd HH:mm";
		
		Date stDate = defaultStartDate(period);
		Date edDate = new Date();
		
		String strStDate = DateUtil.getCurrentDate(stDate, format);
		String strEdDate = DateUtil.getCurrentDate(edDate, format);
		
		String result = strStDate + "|" + strEdDate;
		
		Writer writer = setResponse(response).getWriter();
		writer.write(result);
		
		writer.flush();
			
		log.debug("end - setDefaultGraphDate()");
	}
	
	@RequestMapping("/subGraphDate.do")
	public void subGraphDate(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String period = request.getParameter("period");
		
		String edDate = request.getParameter("edateStr");
		String stDate = DateUtil.getOffsetDateTime(edDate, -1, "yyyy-MM-dd HH:mm");
		
		if (period.equals("2h")) {
			stDate = DateUtil.getOffsetDateTime(edDate, -2, "yyyy-MM-dd HH:mm");
		} else if (period.equals("3h")) {
			stDate = DateUtil.getOffsetDateTime(edDate, -3, "yyyy-MM-dd HH:mm");
		} else if (period.equals("6h")) {
			stDate = DateUtil.getOffsetDateTime(edDate, -6, "yyyy-MM-dd HH:mm");
		} else if (period.equals("12h")) {
			stDate = DateUtil.getOffsetDateTime(edDate, -12, "yyyy-MM-dd HH:mm");
		} else if (period.equals("1d")) {
			stDate = DateUtil.getOffsetDate(edDate, -1, "yyyy-MM-dd HH:mm");
		} else if (period.equals("1w")) {
			stDate = DateUtil.getOffsetDate(edDate, -7, "yyyy-MM-dd HH:mm");
		} else if (period.equals("2w")) {
			stDate = DateUtil.getOffsetDate(edDate, -14, "yyyy-MM-dd HH:mm");
		} else if (period.equals("1m")) {
			stDate = DateUtil.getOffsetDate(edDate, -30, "yyyy-MM-dd HH:mm");
		} else if (period.equals("all")) {
			stDate = DateUtil.getOffsetDate(edDate, -1 * logRetentionPeriod, "yyyy-MM-dd HH:mm");
		}
		
		String result = stDate + "|" + edDate;
		
		Writer writer = setResponse(response).getWriter();
		writer.write(result);
		
		writer.flush();
			
		log.debug("end - subGraphDate()");
	}
	
	@RequestMapping("/compareGraphDate.do")
	public void compareGraphDate(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String sdateStr = request.getParameter("sdateStr");
		String sliderMin = request.getParameter("sliderMin");
		
		int compareDate = DateUtil.getCompareDate(sdateStr,sliderMin,"yyyy-MM-dd HH:mm");

		String result = sdateStr;
		if(compareDate == -1){
			result = sliderMin;
		}
		
		Writer writer = setResponse(response).getWriter();
		writer.write(result);
		
		writer.flush();
			
		log.debug("end - compareGraphDate()");
	}
	
	@RequestMapping("/setGraphDate.do")
	public void setGraphDate(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String period = request.getParameter("period");
		String selectDate = request.getParameter("selectDate");
		String selectHour = Utility.isNull(request.getParameter("selectHour"), "00");
		String selectMin = Utility.isNull(request.getParameter("selectMin"), "00");
		
		String fullDate = selectDate + " " + selectHour + ":" + selectMin;
		String stDate = DateUtil.getOffsetDateMin(fullDate, -30, "yyyy-MM-dd HH:mm");
		String edDate = DateUtil.getOffsetDateMin(fullDate, 30, "yyyy-MM-dd HH:mm");
		
		String stStr="";
		String edStr="";
		
		if (period.equals("2h")) {
			stDate = DateUtil.getOffsetDateTime(fullDate, -1, "yyyy-MM-dd HH:mm");
			edDate = DateUtil.getOffsetDateTime(fullDate, 1, "yyyy-MM-dd HH:mm");
		} else if (period.equals("3h")) {
			stStr = DateUtil.getOffsetDateTime(fullDate, -1, "yyyy-MM-dd HH:mm");
			stDate = DateUtil.getOffsetDateMin(stStr, -30, "yyyy-MM-dd HH:mm");
			edStr = DateUtil.getOffsetDateTime(fullDate, 1, "yyyy-MM-dd HH:mm");
			edDate = DateUtil.getOffsetDateMin(edStr, 30, "yyyy-MM-dd HH:mm");
		} else if (period.equals("6h")) {
			stDate = DateUtil.getOffsetDateTime(fullDate, -3, "yyyy-MM-dd HH:mm");
			edDate = DateUtil.getOffsetDateTime(fullDate, 3, "yyyy-MM-dd HH:mm");
		} else if (period.equals("12h")) {
			stDate = DateUtil.getOffsetDateTime(fullDate, -6, "yyyy-MM-dd HH:mm");
			edDate = DateUtil.getOffsetDateTime(fullDate, 6, "yyyy-MM-dd HH:mm");
		} else if (period.equals("1d")) {
			stDate = DateUtil.getOffsetDateTime(fullDate, -12, "yyyy-MM-dd HH:mm");
			edDate = DateUtil.getOffsetDateTime(fullDate, 12, "yyyy-MM-dd HH:mm");
		} else if (period.equals("1w")) {
			stStr = DateUtil.getOffsetDate(fullDate, -3, "yyyy-MM-dd HH:mm");
			stDate = DateUtil.getOffsetDateTime(stStr, -12, "yyyy-MM-dd HH:mm");
			edStr = DateUtil.getOffsetDate(fullDate, 3, "yyyy-MM-dd HH:mm");
			edDate = DateUtil.getOffsetDateTime(edStr, 12, "yyyy-MM-dd HH:mm");
		} else if (period.equals("2w")) {
			stDate = DateUtil.getOffsetDate(fullDate, -7, "yyyy-MM-dd HH:mm");
			edDate = DateUtil.getOffsetDate(fullDate, 7, "yyyy-MM-dd HH:mm");
		} else if (period.equals("1m")) {
			stDate = DateUtil.getOffsetDate(fullDate, -15, "yyyy-MM-dd HH:mm");
			edDate = DateUtil.getOffsetDate(fullDate, 15, "yyyy-MM-dd HH:mm");
		} else if (period.equals("3m")) {
			int halfPeriod = Math.round(logRetentionPeriod / 2);
			stDate = DateUtil.getOffsetDate(fullDate, -1 * halfPeriod, "yyyy-MM-dd HH:mm");
			edDate = DateUtil.getOffsetDate(fullDate, halfPeriod, "yyyy-MM-dd HH:mm");
		}

		Date now = new Date();
		String strNow = DateUtil.getCurrentDate(now, "yyyy-MM-dd HH:mm");
		int compareDate = DateUtil.getCompareDate(strNow,edDate,"yyyy-MM-dd HH:mm");
		
		String result = stDate + "|" + edDate + "|" + compareDate;
		
		Writer writer = setResponse(response).getWriter();
		writer.write(result);
		
		writer.flush();
			
		log.debug("end - subGraphDate()");
	}
	
	@RequestMapping("/getDefaultOffsetDays.do")
	public void getDefaultOffsetDays(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String sdateStr = request.getParameter("sdateStr");
		String edateStr = request.getParameter("edateStr");
		
		int compareMin = (DateUtil.getOffsetMins(edateStr,sdateStr))/2;

		String result = DateUtil.getOffsetDateMin(sdateStr, compareMin, "yyyy-MM-dd HH:mm");;
		
		Writer writer = setResponse(response).getWriter();
		writer.write(result);
		
		writer.flush();
			
		log.debug("end - getDefaultOffsetDays()");
	}

	@RequestMapping("/demonPop.do")
	public ModelAndView demonPop(CommonDto dto) throws Exception{
		log.debug("start - demonPop()");
		ModelAndView mav = commMav();
		try{			
			mav = commDemonPop(mav,dto);
			mav.setViewName("demonPop");
		}catch(Exception e){
			
		}
		log.debug("end - demonPop()");
		return mav;
	}
	
	@RequestMapping("/demonDBPop.do")
	public ModelAndView demonDBPop(CommonDto dto) throws Exception{
		log.debug("start - demonDBPop()");
		ModelAndView mav = commMav();
		try{
			mav = commDemonPop(mav,dto);
			mav.setViewName("demonDBPop");
		}catch(Exception e){
			
		}
		log.debug("end - demonDBPop()");
		return mav;
	}
	
	public ModelAndView commDemonPop(ModelAndView mav, CommonDto dto){
		List<Object> deviceLst = monadService.getList(new Query(), Device.class);
		mav.addObject("deviceLst", deviceLst);
			
			mav.addObject("comm", dto);
//			mav.addObject("server", serverStatus);
			mav.addObject("dbStatus", subDao.dbLstNan(dto.getDeviceCode()));
		return mav;
	}
	
	private PersonJson setPersonJson(int cnt, int sEcho, int pageNumber, int nPerPage, List<Object> resultList){
		
		PersonJson result = new PersonJson();
		result.setsEcho(sEcho);
		result.setiTotalRecords(cnt);
		result.setiTotalDisplayRecords(cnt);
		result.setAaData(resultList);
		
		return result;
	}
	
	/**
	 * 경고 알람 상세보기
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/subAlarmView.do")
	public ModelAndView subAlarmView(CommonDto dto){
		log.debug("start - subAlarmView()");
		ModelAndView mav = commMav();
		
		Class<?> classname = Alarm.class;
		Query query=new Query();
		List<Object> lst = new ArrayList<Object>();
		
		try{
			query = new Query();
			List<Object> typeLst = new ArrayList<Object>();
			for(int i=0;i<TYPE.length;i++){
				typeLst.add(TYPE[i]);
			}
			mav.addObject("type", typeLst);
			
			//그룹 리스트를 출력하기 위해 필요
			List<Object> groupLst = monadService.getList(query, Group.class);
			mav.addObject("group", groupLst);
			
			List<Object> deviceLst = monadService.getList(query, Device.class);
			mav.addObject("device", deviceLst);
			
			mav.addObject("comm",dto);
			mav.setViewName("sub_alarm_page");
		}catch(Exception e){
			log.error(e, e);
		}
		log.debug("end - subAlarmView()");
		
		return mav;
	}
	
	@RequestMapping("/subAlarmList.do")
	public void subAlarmList(HttpServletRequest request, HttpServletResponse response,CommonDto dto) throws Exception{
		log.debug("start - subAlarmList()");
		
		//iDisplayStart와 iDisplayLength는 datatable에서 페이징을 하기 위해 가지고 있는 값
		int pageNumber = Integer.parseInt(Utility.isNullNumber(request.getParameter("iDisplayStart")));
		int nPerPage = Integer.parseInt(Utility.isNullNumber(request.getParameter("iDisplayLength")));
		int sEcho = Integer.parseInt(Utility.isNullNumber(request.getParameter(REQ_SECHO)));
		log.debug("pageNumber=" + pageNumber + ", nPerPage=" + nPerPage);
		
		Enumeration parameter = request.getParameterNames();
		log.debug(parameter.toString());
		while (parameter.hasMoreElements()) {
			String pName = (String)parameter.nextElement();
			String pValue = request.getParameter(pName);
			log.debug(pName + " = " + pValue);
		}
			
		try{
			Class<?> classname = Alarm.class;
			Query query=new Query();
			
			query = setAlarmSearch(dto);//날짜 검색일 땐 날짜 + 정렬		
			int cnt = (int)monadService.getCount(query, classname);
			List<Object> resultList = monadService.getList(query.skip(pageNumber).limit(nPerPage), classname);
			
			PersonJson result = setPersonJson(cnt, sEcho, pageNumber, nPerPage, resultList);
			JSONObject jsonObject = JSONObject.fromObject(result);
			
			Writer writer = setResponse(response).getWriter();
			writer.write(jsonObject.toString());
			
			log.debug(jsonObject.toString());
			writer.flush();
		}catch(Exception e){
			log.error(e, e);
		}
		
		log.debug("end - subAlarmList()");
	}
	
	private HttpServletResponse setResponse(HttpServletResponse response){
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		
		return response;
	}
	/**
	 * 메인화면 confrim 확인 팝업 페이지
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mainConfirmView.do")
	public ModelAndView mainConfirmView(CommonDto dto) throws Exception{
		log.debug("start - mainConfirmView()");
		ModelAndView mav = new ModelAndView();
		Alarm adto = new Alarm();
		Query query = setIdx(dto.getIdx());
		adto = (Alarm) monadService.getFind(query, Alarm.class);
		
		mav.addObject("comm",dto);
		mav.addObject("adto",adto);
		mav.setViewName("main_alarm_popup");
		
		log.debug("end - mainConfirmView()");
		return mav;
	}
	
	/**
	 * confrim 확인 팝업 페이지
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/confirmView.do")
	public ModelAndView alarmConfirm(CommonDto dto) throws Exception{
		log.debug("start - alarmConfirm()");
		ModelAndView mav = new ModelAndView();
		
		mav.addObject("comm",dto);
		mav.setViewName("alarm_popup");
		
		log.debug("end - alarmConfirm()");
		return mav;
	}
	
	/**
	 * confirm 확인
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/confirmCheck.do")
	public ModelAndView confirmCheck(CommonDto dto) throws Exception{
		log.debug("start - confirmCheck()");
		ModelAndView mav = new ModelAndView();
		Query query = new Query();
		Update update = new Update();
		Alarm adto = new Alarm();
		try{
			query = setIdx(dto.getIdx());//하나만 confirm 할때는 idx만 가지고 쿼리 만든다
			
			if(dto.getDival()==1){//전체 confirm은 그룹명,장비명,항목,구분으로 쿼리를 생성
				adto = (Alarm) monadService.getFind(query, Alarm.class);
				query = new Query();
				query = setConfirm(adto.getGroupCode(), adto.getDeviceCode(), adto.getCri_type(),adto.getAlarm());
			}
			List<Object> lst = monadService.getList(query, Alarm.class);
			for(int i=0;i<lst.size();i++){
				Confirm cdto = new Confirm();
				Alarm alarm =(Alarm) lst.get(i);
				cdto.setAlarmCode(alarm.getIdx());
				
				cdto.setDate(DateUtil.getTime());
				cdto.setMemo(dto.getMemo());
				cdto.setUserCode(dto.getUserCode());
				monadService.add(cdto, Confirm.class);//알람 확인 컬럼에 정보 insert
			}
			update.set("confirm",1);		
			update.set("userCode", dto.getUserCode());
			monadService.updateMulti(query, update, "alarm");//알람 컬럼에 confirm update
			
			dto.setMessage("확인 되었습니다.");
		}catch(Exception e){
			log.error(e, e);
		}
		log.debug("end - confirmCheck()");

		mav.setViewName("alarm_popup");
		mav.addObject("comm", dto);
		return mav;
	}
	
	/**
	 * confrim 확인 팝업 페이지
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/confirmGetView.do")
	public ModelAndView confirmGetView(CommonDto dto) throws Exception{
		log.debug("start - confirmGetView()");
		ModelAndView mav = new ModelAndView();
		Query query = new Query();
		Confirm cdto = new Confirm();
		query = setAlarmCode(dto.getIdx());
		cdto = (Confirm) monadService.getFind(query, Confirm.class);
		
		mav.addObject("confirm",cdto);
		mav.setViewName("alarmGet_popup");
		
		log.debug("end - confirmGetView()");
		return mav;
	}
	
	@RequestMapping("/viewDaemonInfoGraph.do")
	public ModelAndView viewDaemonInfoGraph(CommonDto dto) throws Exception{
		log.debug("start - viewDaemonInfoGraph()");
		ModelAndView mav = new ModelAndView();
		
		String dsname = dto.getDsname();
		
		Map<String, StatusDatasourceName> allDatasource = new AllStatusRule().allStatusDatasourceNameByMap();
		StatusDatasourceName statusInfo = allDatasource.get(dsname);
		dto.setDstype(statusInfo.getCalculateType());
				
		setSliderBarDateBytimeUnit(dto);
		
		mav.addObject("comm", dto);
		mav.setViewName("pop_dhtmlGraph");
		
		log.debug("end - viewDaemonInfoGraph()");
		return mav;
	}
	
	private void setSliderBarDateBytimeUnit(CommonDto dto) throws ParseException {
		String timeUnit = dto.getGraph_period();
		
		int totalRange = DHTML_TOTAL_RANGE_1MIN;
		int searchRange = DHTML_SEARCH_RANGE_1MIN;
		
		if (timeUnit != null) {
			if (timeUnit.equals("5")) {
				totalRange = DHTML_TOTAL_RANGE_5MIN;
				searchRange = DHTML_SEARCH_RANGE_5MIN;
			} else if (timeUnit.equals("30")) {
				totalRange = DHTML_TOTAL_RANGE_30MIN;
				searchRange = DHTML_SEARCH_RANGE_30MIN;
			} else if (timeUnit.equals("60")) {
				totalRange = DHTML_TOTAL_RANGE_60MIN;
				searchRange = DHTML_SEARCH_RANGE_60MIN;
			} 
		} 
		
		int halfMin = searchRange / 2;
		String inputDate = dto.getSelectDate();
		Date nowDate = new Date();
		Date standardDateObj = DateUtil.plusMinute(nowDate, -(halfMin));     // 중간값 (화면상의 날짜검색)
		Date searchSdateObj = DateUtil.plusMinute(nowDate, -searchRange); 
		Date searchEdateObj = nowDate;
		Date totalMinObj = DateUtil.plusHour(nowDate, -totalRange);
		Date totalMaxObj = nowDate;
		
		// 화면에서 날짜 조작만 했을때
		if ((dto.getMemo() == null || dto.getMemo().isEmpty()) && (inputDate != null && !inputDate.isEmpty())) {
			String fullDate = inputDate + " " + dto.getSelectHour() + ":" + dto.getSelectMin();

			standardDateObj = DateUtil.dateformat(fullDate, "yyyy-MM-dd HH:mm");
			searchSdateObj = DateUtil.plusMinute(standardDateObj, -halfMin);
			searchEdateObj = DateUtil.plusMinute(standardDateObj, halfMin);
			totalMinObj = DateUtil.plusHour(searchEdateObj, -totalRange);
			totalMaxObj = searchEdateObj;
		} 
		
		dto.setSelectDate(DateUtil.getCurrentDate(standardDateObj, "yyyy-MM-dd"));
		dto.setSelectHour(standardDateObj.getHours());
		dto.setSelectMin(standardDateObj.getMinutes());
		
		dto.setSliderMin(DateUtil.getCurrentDate(totalMinObj, "yyyy-MM-dd HH:mm"));
		dto.setSliderMax(DateUtil.getCurrentDate(totalMaxObj, "yyyy-MM-dd HH:mm"));
		dto.setSdate(DateUtil.getCurrentDate(searchSdateObj, "yyyy-MM-dd HH:mm"));
		dto.setEdate(DateUtil.getCurrentDate(searchEdateObj, "yyyy-MM-dd HH:mm"));
	}
	
	@RequestMapping("/isShowGraph.do")
	public void isShowGraph(CommonDto dto, HttpServletResponse response) throws Exception{
		log.debug("start - isShowGraph()");
		boolean isShowGraph = true;
		
		setDataSourceName(dto);
		String dsname = dto.getDsname();
		
		Map<String, StatusDatasourceName> allDatasource = new AllStatusRule().allStatusDatasourceNameByMap();
		StatusDatasourceName statusInfo = allDatasource.get(dsname);
		if (statusInfo == null) {
			isShowGraph = false;
		}
		
		JSONObject data = new JSONObject();
		data.put("dsname", dsname);
		data.put("isshow", isShowGraph);
		data.put("collname", dto.getCollname());
		data.put("dbname", Utility.isNull(dto.getDbname()));
		
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		
		Writer writer = response.getWriter();
		writer.write(data.toString());
		
		log.debug(data.toString());
		writer.flush();
		
		log.debug("end - isShowGraph()");
	}	
	
	
	@RequestMapping("/drawDaemonInfoGraph.do")
	public void drawDaemonInfoGraph(CommonDto dto, HttpServletResponse response) throws Exception{
		log.debug("start - drawDaemonInfoGraph()");
		String dsType = dto.getDstype();
		String queryOption = dto.getSearch_option();
		if (queryOption == null) queryOption = "N"; 
		
		dto.setSearch_sdate(DateUtil.getCurrentDate(DateUtil.dateformat(dto.getSdate(), "yyyy-MM-dd HH:mm"), "yyyyMMddHHmmssSSS"));
		dto.setSearch_edate(DateUtil.getCurrentDate(DateUtil.dateformat(dto.getEdate(), "yyyy-MM-dd HH:mm"), "yyyyMMddHHmmssSSS"));
		
		List<DBObject> resultList = null;
		if (dsType.equals(GRAPH_ACCUM)) {
			if (queryOption.equals("Y")) {
				resultList = (List<DBObject>) subDao.getAccumResult(dto);
			} else {
				resultList = (List<DBObject>) subDao.getIncludeOptionResult(dto);
			}
		} else if (dsType.equals(GRAPH_CURENT)) {
			resultList = (List<DBObject>) subDao.getCurrentResult(dto);
		}
		
		Map<String, StatusDatasourceName> allDatasource = new AllStatusRule().allStatusDatasourceNameByMap();
		StatusDatasourceName statusInfo = allDatasource.get(dto.getDsname());
		String dsUnit = statusInfo.getYunit();		
		
		if (resultList != null) {
			settingXaixUnit(resultList); 
		}
		
		DhtmlJson dhtmlJson = new DhtmlJson();
		dhtmlJson.setDataUnit(dsUnit);
		dhtmlJson.setResultData(resultList);
		JSONObject jsonObject = JSONObject.fromObject(dhtmlJson);		
		
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		
		Writer writer = response.getWriter();
		writer.write(jsonObject.toString());
		
		log.debug(jsonObject.toString());
		writer.flush();
		
		log.debug("end - drawDaemonInfoGraph()");
	}	
	
	private void settingXaixUnit(List<DBObject> result) throws ParseException {
		List<DBObject> newResult = new ArrayList<DBObject>();
		int index = 0;
		int totalCnt = result.size();
		int criterionNum = totalCnt / DHTML_XUNIT_CRITERION_NUM;
		if (totalCnt < DHTML_XUNIT_CRITERION_NUM) {
			criterionNum = 1;
		}
		for (DBObject obj : result) {
			String date = obj.get("regtime").toString();
			if (index % criterionNum == 0) 
				obj.put("xunit", DateUtil.getCurrentDate(DateUtil.dateformat(date, "yyyyMMddHHmmssSSS"), "HH:mm" ));
			else 
				obj.put("xunit", "");
			newResult.add(obj);
			index++;
		}
		result = newResult;
	}
	
	private void setDataSourceName(CommonDto dto) {
		String newDsname = "";
		String collname = COLL_SERVERSTATUS;
		String dsname = dto.getDsname();
		if (dsname.startsWith("locks") || dsname.startsWith("recordStats")) {
			String[] temp = dsname.split("_");
			if (temp.length > 2){ // recordStats에서 DB가 들어가지않은 토탈정보 recordStats.accessesNotInMemory 가 있으므로 구분해줌.
				String dbname = dto.getDbname();
				if (!dbname.equals(".")) {
					collname = COLL_DBSTATUS;
					dto.setDbname(dbname);
					newDsname = dsname.replaceAll(dbname+"_", "");
				} else {
					newDsname = dsname.replaceAll("\\"+dbname+"_", "");
				}
			} else {
				// recordStats.accessesNotInMemory || recordStats.pageFaultExceptionsThrown 인경우..
				newDsname = dsname;
			}
		} else {
			newDsname = dsname;
		}
		dto.setDsname(newDsname);
		dto.setCollname(collname);
	}
}
