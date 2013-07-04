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
import static net.cit.tetrad.common.ColumnConstent.MAIN_GRAPH_HEIGHT;
import static net.cit.tetrad.common.ColumnConstent.MAIN_GRAPH_WIDTH;
import static net.cit.tetrad.common.ColumnConstent.PROCESS_MONGOD;
import static net.cit.tetrad.common.ColumnConstent.PROCESS_MONGOS;
import static net.cit.tetrad.common.ColumnConstent.REQ_SECHO;
import static net.cit.tetrad.common.ColumnConstent.MYSTATE;
import static net.cit.tetrad.common.ColumnConstent.TOTALFILEARR;
import static net.cit.tetrad.common.PropertiesNames.MAINRELOADTIMEMILLI;
import static net.cit.tetrad.utility.QueryUtils.*;

import java.io.Writer;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentMap;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cit.tetrad.common.DateUtil;
import net.cit.tetrad.common.Utility;
import net.cit.tetrad.model.Alarm;
import net.cit.tetrad.model.CommonDto;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.model.PersonJson;
import net.cit.tetrad.rrd.bean.GraphDefInfo;
import net.cit.tetrad.rrd.bean.ServerStatus;
import net.cit.tetrad.rrd.bean.TotalInfo;
import net.cit.tetrad.rrd.utils.StringUtil;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.rrd4j.core.Util;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class MainResource extends DefaultResource{

	private Logger log = Logger.getLogger(this.getClass());
	
	/**
	 * 메인 화면 view
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mainView.do")
	public ModelAndView mainView(CommonDto dto) throws Exception{
		log.debug("start - mainView()");
		ModelAndView mav = commMav();
		String message = dto.getMessage();
		dto.setMessage(message);
		
		Map<String, String> fileName = setMainGraphInfo();

		mav.addObject("filename", fileName);
		mav.addObject("mainReloadTimeMilli", MAINRELOADTIMEMILLI);
		mav.addObject("comm", dto);
				
		mav.setViewName("main");
		log.debug("end - mainView()");
		return mav;
	}
	
	@RequestMapping("/mainGraph.do")
	public void mainGraph(HttpServletRequest request, HttpServletResponse response) {		
		log.debug("start - mainGraph.do");
		try{
			PersonJson result = new PersonJson();
			
			Map<String, String> fileName = setMainGraphInfo();
			
			List<Object> arrTotInfo = new ArrayList<Object>();
			arrTotInfo.add(fileName);
			
			result.setsEcho(Integer.parseInt(Utility.isNullNumber(request.getParameter("sEcho"))));
			result.setAaData(arrTotInfo);
			
			JSONObject jsonObject = JSONObject.fromObject(result);

			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("UTF-8");

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");

			Writer writer = response.getWriter();
			writer.write(jsonObject.toString());

			log.debug(jsonObject.toString());
			writer.flush();

		}catch(Exception e){
			log.error(e, e);
		}
		log.debug("end - mainGraph.do");
	}
	
	public Map<String, String> setMainGraphInfo() {
		String graphGubun = mainDao.graphGubun();
		
		Map<String, String> fileName = getGraphFileName(graphGubun);
		if(isMongoVer2_2){
			String total_lock = addLockTotal(graphGubun);
			fileName.put("totalGlobalLockTime", total_lock);
		}
		return fileName;
	}
	
	public String addLockTotal(String graphGubun){
		Date dateStr = setGraphDate(graphGubun);
		String lockTotalFilenm = "";
			GraphDefInfo pageGraphDay = createDefaultMainGraphAttribute(DateUtil.plusDay(0), dateStr);		
			pageGraphDay.setFilters(TOTALFILEARR);
			pageGraphDay.setFileName("totalGlobalLockTime");
			pageGraphDay.setAxisTimeUnitDiv(graphGubun);
			lockTotalFilenm = rrdService.totalMultiGraphPerRrd(pageGraphDay);
			return lockTotalFilenm;
	}
	
	public Map<String, String> getGraphFileName(String graphGubun) {	
		Map<String, String> fileName = new HashMap<String, String>(); 
		Date dateStr = setGraphDate(graphGubun);
		for (String rrdDb : MONGOVER_TOTALFILE) {
			GraphDefInfo graphDay = createDefaultMainGraphAttribute(DateUtil.plusDay(0), dateStr);
			graphDay.setFileName(rrdDb);
			graphDay.setAxisTimeUnitDiv(graphGubun);
			fileName.put(rrdDb, rrdService.graphPerRrdDb(rrdDb, graphDay));
		}
		
		List<Device> deviceGroup = getTotalMongodGroup();
		
		String pageFaultFilenm = "";
		if(deviceGroup.size() != 0){
			GraphDefInfo pageGraphDay = createDefaultMainGraphAttribute(DateUtil.plusDay(0), dateStr);		
			pageGraphDay.setDeviceGroup(deviceGroup);
			pageGraphDay.setFileName("extra_info_page_faults");
			pageGraphDay.setAxisTimeUnitDiv(graphGubun);
			pageFaultFilenm = rrdService.multiDeviceGraphPerRrd("extra_info_page_faults", pageGraphDay);
		}
		fileName.put("extra_info_page_faults", pageFaultFilenm);
		
		return fileName;
	}
	
	public Date setGraphDate(String graphGubun){
		Date dateStr = DateUtil.plusDay(-1);
		if(graphGubun.equals("2")){
			dateStr = DateUtil.plusDay(-7);
		}else if(graphGubun.equals("3")){
			dateStr = DateUtil.plusMonth(-1);
		}
		return dateStr;
	}
	
	private GraphDefInfo createDefaultMainGraphAttribute(Date endDate, Date startDate) {
		GraphDefInfo graph = new GraphDefInfo();
		graph.setEndTime(Util.getTimestamp(endDate));
		graph.setStartTime(Util.getTimestamp(startDate));
		graph.setConsolFun("LAST");
		graph.setStep(10);
//		graph.setEndTime(CommonUtils.getTimeForGraph(endDate));
//		graph.setStartTime(CommonUtils.getTimeForGraph(startDate));
		
		graph.setWidth(MAIN_GRAPH_WIDTH);
		graph.setHeight(MAIN_GRAPH_HEIGHT);
		
		return graph;
	}
	
	private List<Device> getTotalMongodGroup() {
		List<Device> deviceGroup = new ArrayList<Device>();
		List<ServerStatus> serverStatusList = daoForMongo.readServerStatus(PROCESS_MONGOD);		
		if(serverStatusList.size() != 0){
			for (ServerStatus serverStatus : serverStatusList) {
				Device device = new Device();
				device.setIdx(serverStatus.getDeviceCode());
				
				deviceGroup.add(device);
			}
		}
		return deviceGroup;
	}

	/**
	 * sharding중 체크
	 * @param request
	 * @param response
	 * @param dto
	 * @throws Exception
	 */
	@SuppressWarnings("unchecked")
	@RequestMapping("/shadingCheck.do")
	public void shadingCheck(HttpServletRequest request, HttpServletResponse response,CommonDto dto) throws Exception{
		log.debug("start - shadingCheck()");
		try{
			List<ServerStatus> serverStatusList = daoForMongo.readServerStatus(PROCESS_MONGOS);
			List<Object> readMongolocks = new ArrayList<Object>();
			boolean isExistShading = false;
			if(serverStatusList.size() != 0){
				for (ServerStatus serverStatus : serverStatusList) {
					readMongolocks = comandService.collectionCommand(serverStatus.getDeviceCode(),"locks");
					for(Object locksObj : readMongolocks){
						Map<String, Object> locks = (Map<String, Object>) locksObj;
						if(locks.get("_id").equals("balancer") && locks.get("state").equals(2)){ //_id가  balancer이고 state가 2 이면 sharding중
							isExistShading=true;
							break;
						}
					}
					if(isExistShading)break;
				}
			}
			String result = "false";
			if(isExistShading)result = "true";
			Writer writer = setResponse(response).getWriter();
			writer.write(result);
			writer.flush();
		}catch(Exception e){
			log.error(e, e);
		}
		
		log.debug("end - shadingCheck()");
	}
	
	/**
	 * 장비 데이터 리스트
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/mainList.do")
	public void mainList(HttpServletRequest request, HttpServletResponse response,CommonDto dto) throws Exception{
		log.debug("start - mainList()");
		
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
		
		try{
			int sEcho = Integer.parseInt(Utility.isNullNumber(request.getParameter(REQ_SECHO)));
			
			PersonJson result = setPersonJson(sEcho, dto, pageNumber, nPerPage);
			JSONObject jsonObject = JSONObject.fromObject(result);
			
			Writer writer = setResponse(response).getWriter();
			writer.write(jsonObject.toString());
			
			log.debug(jsonObject.toString());
			writer.flush();
		}catch(Exception e){
			log.error(e, e);
		}
		
		log.debug("end - mainList()");
	}
	
	/**
	 * 장비 Master/Slave를 구분
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/getMyState.do")
	public void getMyState(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String deviceLst = request.getParameter("myStateLst");
		try{
			if (!StringUtil.isNull(deviceLst)) {
				String[] deviceCode = deviceLst.split(",");
				Map<String, Object> fromMongo = new HashMap<String, Object>();
				StringBuffer sb = new StringBuffer();
				for(int i=0; i<deviceCode.length; i++){
					fromMongo = comandService.insertCommand(Integer.parseInt(deviceCode[i]),MYSTATE);
					sb.append(deviceCode[i]+":"+fromMongo.get("myState"));
					if(i<deviceCode.length-1)sb.append(",");
				}
	
				Writer writer = setResponse(response).getWriter();
				writer.write(sb.toString());
				writer.flush();
			}
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	private PersonJson setPersonJson(int sEcho, CommonDto dto,int pageNumber, int nPerPage){
		Query query = setGroupCode(dto);
		int cnt = (int)monadService.getCount(query, COLL_DASHBOARD);
		List<Object> resultList = monadService.getListWithStrCollName(query.skip(pageNumber).limit(nPerPage), Map.class, COLL_DASHBOARD);
		
		PersonJson result = new PersonJson();
		result.setsEcho(sEcho);
		result.setiTotalRecords(cnt);
		result.setiTotalDisplayRecords(cnt);
		result.setAaData(getDashBoardData(resultList));
		
		return result;
	}
	
	@SuppressWarnings({ "unchecked", "rawtypes" })
	private List<Object> getDashBoardData(List<Object> paramList){
		List<Object> resultList = new ArrayList<Object>();
		for(Object obj : paramList) {
			Map<String, String> result = new HashMap<String, String>();
			Map param = (Map)obj;
			Set<String> keySet = param.keySet();
			Iterator<String> keyIt = keySet.iterator();
			while(keyIt.hasNext()){
				String key = keyIt.next().toString();
				String value = param.get(key).toString();
				result.put(key, value);
			}
			resultList.add(result);
		}
		return resultList;
	}
	
	private HttpServletResponse setResponse(HttpServletResponse response){
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		
		return response;
	}
	
	/**
	 * 경고 알람
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/alarmList.do")
	public void alarmList(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.debug("start - alarmList()");
		PersonJson result = new PersonJson();

		Class<?> classname = Alarm.class;
		Query query=new Query();
		CommonDto dto = new CommonDto();
		try{
			dto.setAlarm(Integer.parseInt(request.getParameter("alarm")));
			query = setAlarmSort(dto);
			int cnt = (int)monadService.getCount(query, classname);
			List<Object> resultList = monadService.getList(query.skip(0).limit(5), classname);
			
			result.setsEcho(Integer.parseInt(request.getParameter("sEcho")));
			result.setiTotalRecords(cnt);
			result.setiTotalDisplayRecords(cnt);
			result.setAaData(resultList);
			
			JSONObject jsonObject = JSONObject.fromObject(result);
			
			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("UTF-8");
			
			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");
			
			Writer writer = response.getWriter();
			writer.write(jsonObject.toString());
			
			log.debug(jsonObject.toString());
			writer.flush();
			
		}catch(Exception e){
			log.error(e, e);
		}
		log.debug("end - alarmList()");
	}
	
	@RequestMapping("/totalInfo.do")
	public void totalInfo(HttpServletRequest request, HttpServletResponse response) {		
		log.debug("start - totalInfo.do");
		try{
			PersonJson result = new PersonJson();
			TotalInfo totInfo = (TotalInfo) monadService.getFind(new Query(), TotalInfo.class);
			
			List<Object> arrTotInfo = new ArrayList<Object>();
			arrTotInfo.add(totInfo);
			
			result.setsEcho(Integer.parseInt(Utility.isNullNumber(request.getParameter("sEcho"))));
			result.setiTotalRecords(arrTotInfo.size());
			result.setiTotalDisplayRecords(arrTotInfo.size());					
			result.setAaData(arrTotInfo);
			
			JSONObject jsonObject = JSONObject.fromObject(result);

			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("UTF-8");

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");

			Writer writer = response.getWriter();
			writer.write(jsonObject.toString());

			log.debug(jsonObject.toString());
			writer.flush();

		}catch(Exception e){
			log.error(e, e);
		}
		log.debug("end - totalInfo.do");
	}

}
