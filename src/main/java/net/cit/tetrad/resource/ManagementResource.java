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

import static net.cit.tetrad.common.ColumnConstent.COLL_ALARM;
import static net.cit.tetrad.common.ColumnConstent.COLL_DASHBOARD;
import static net.cit.tetrad.common.ColumnConstent.COL_DEVICECODE;
import static net.cit.tetrad.common.ColumnConstent.COL_GROUPCODE;
import static net.cit.tetrad.common.ColumnConstent.DEVICE_TYPE;
import static net.cit.tetrad.common.ColumnConstent.IDX;
import static net.cit.tetrad.common.ColumnConstent.MAV_ALARM;
import static net.cit.tetrad.common.ColumnConstent.MAV_COMM;
import static net.cit.tetrad.common.ColumnConstent.MAV_CRITICAL;
import static net.cit.tetrad.common.ColumnConstent.MAV_DEVICE;
import static net.cit.tetrad.common.ColumnConstent.MAV_GLOBAL;
import static net.cit.tetrad.common.ColumnConstent.MAV_GROUP;
import static net.cit.tetrad.common.ColumnConstent.MAV_TYPE;
import static net.cit.tetrad.common.ColumnConstent.MONGOVER2_0;
import static net.cit.tetrad.common.ColumnConstent.MONGOVER2_2;
import static net.cit.tetrad.common.ColumnConstent.PATH_MANAGEMEMT;
import static net.cit.tetrad.common.ColumnConstent.REQ_DIVAL;
import static net.cit.tetrad.common.ColumnConstent.REQ_SECHO;
import static net.cit.tetrad.common.ColumnConstent.SEARCHALL;
import static net.cit.tetrad.common.ColumnConstent.UNIT_SECONDS;
import static net.cit.tetrad.common.PropertiesNames.RELEASEVERSIONINFO;
import static net.cit.tetrad.common.PropertiesNames.TABLENAME;
import static net.cit.tetrad.utility.QueryUtils.notIdxsetUid;
import static net.cit.tetrad.utility.QueryUtils.setAuthority;
import static net.cit.tetrad.utility.QueryUtils.setCriSearch;
import static net.cit.tetrad.utility.QueryUtils.setDeviceCode;
import static net.cit.tetrad.utility.QueryUtils.setGroupCode;
import static net.cit.tetrad.utility.QueryUtils.setIdx;
import static net.cit.tetrad.utility.QueryUtils.setIpPort;
import static net.cit.tetrad.utility.QueryUtils.setUid;
import static net.cit.tetrad.utility.QueryUtils.setgroupBind;
import static net.cit.tetrad.utility.QueryUtils.sortDate;

import java.io.Writer;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.cit.tetrad.common.Config;
import net.cit.tetrad.common.CriticalInfo;
import net.cit.tetrad.common.DateUtil;
import net.cit.tetrad.common.Utility;
import net.cit.tetrad.model.CommonDto;
import net.cit.tetrad.model.Critical;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.model.Group;
import net.cit.tetrad.model.PersonJson;
import net.cit.tetrad.model.User;
import net.cit.tetrad.rrd.batch.DeviceInMemory;
import net.cit.tetrad.rrd.batch.MongoInMemory;
import net.cit.tetrad.rrd.batch.TetradRrdInitializer;
import net.cit.tetrad.rrd.bean.DbStatus;
import net.cit.tetrad.rrd.bean.ServerStatus;
import net.cit.tetrad.rrd.dao.MongoStatusToMonitor;
import net.cit.tetrad.schedule.DbStatusDeleteThread;
import net.cit.tetrad.schedule.ServerStatusDeleteThread;
import net.cit.tetrad.schedule.StatusInMemory;
import net.cit.tetrad.utility.StringUtils;
import net.cit.tetrad.utility.code.Code;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Order;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.mongodb.DB;
import com.mongodb.Mongo;

@Controller
public class ManagementResource extends DefaultResource {

	private Logger log = Logger.getLogger(this.getClass());
	public TetradRrdInitializer rrdInitializer;
	public MongoStatusToMonitor mongoStatusToMonitor;
	
	public ManagementResource(){ }
	
	public void setRrdInitializer(TetradRrdInitializer rrdInitializer) {
		this.rrdInitializer = rrdInitializer;
	}

	public void setMongoStatusToMonitor(MongoStatusToMonitor mongoStatusToMonitor) {
		this.mongoStatusToMonitor = mongoStatusToMonitor;
	}

	/**
	 * 관리 페이지
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	
	@RequestMapping("/listManagement.do")
	public ModelAndView listManagement(CommonDto paramDTO) throws Exception{
		log.debug("start - listManagement()");
		
		ModelAndView mav = commMav();
		int dival = paramDTO.getDival();
		
		String grpSelect = "";
		if (dival == 2) {
			grpSelect = paramDTO.getGroupSelect();
		}
		
		CommonDto resultDTO = new CommonDto();
		resultDTO.setDival(dival);
		resultDTO.setMessage(paramDTO.getMessage());
		resultDTO.setSearch_gubun(paramDTO.getSearch_gubun());
		resultDTO.setSearch_text(paramDTO.getSearch_text());
		
		String tablenm = TABLENAME[dival];
		mav.setViewName(PATH_MANAGEMEMT+tablenm);
		
		if(tablenm.equals(MAV_DEVICE)) getDeviceModelAndView(mav);
		if(tablenm.equals(MAV_GLOBAL)) globalPageSet(mav);

		mav.addObject(MAV_COMM, resultDTO);
		mav.addObject("bookmark", grpSelect);
		
		log.debug("end - listManagement()");
		return mav;
	}
	
	private ModelAndView globalPageSet(ModelAndView mav)throws Exception{
		mav.addObject("releaseVersionInfo", RELEASEVERSIONINFO);
		String startDateStr = "";
		String endDateStr = "";
		String dateFormat = "yyyy-MM-dd";
		String today = DateUtil.getCurrentDate(dateFormat);
		if(StatusInMemory.isDeleteLogState()){
			startDateStr = managementDao.timeStampToString(StatusInMemory.getStartDate());
			endDateStr = managementDao.timeStampToString(StatusInMemory.getEndDate());
		}else{
			String getRegDate = getSmallRegTime();
			if(getRegDate.equals("")){
				startDateStr = today;
				endDateStr = today;
			}else{
				startDateStr = managementDao.timeStampToString(getRegDate);
				endDateStr = DateUtil.getOffsetDate(startDateStr, 10, dateFormat);
				if(DateUtil.getCompareDate(today,endDateStr,dateFormat) == -1)endDateStr = today;
			}
		}
		mav.addObject("startDateStr", startDateStr);
		mav.addObject("endDateStr", endDateStr);
		mav.addObject("deleteState", StatusInMemory.isDeleteLogState());
		return mav;
	}
	
	private String getSmallRegTime(){
		Query query = new Query();
		query.sort().on("regtime", Order.ASCENDING);
		query.limit(1);
		ServerStatus status = (ServerStatus) monadService.getFind(query, ServerStatus.class);
		DbStatus dbStatus = (DbStatus) monadService.getFind(query, DbStatus.class);
		String resultDate = "";
		if(status == null && dbStatus != null){
			resultDate = Utility.isNull(dbStatus.getRegtime());
		}else if(status != null && dbStatus == null){
			resultDate = Utility.isNull(status.getRegtime());
		}else if(status != null && dbStatus != null){
			String serverRegTime = Utility.isNull(status.getRegtime());
			String dbRegTime = Utility.isNull(dbStatus.getRegtime());
			try {
				int result = DateUtil.getCompareDate(serverRegTime, dbRegTime, "yyyyMMddHHmmssSSS");
				if(result == -1 || result == 0){
					resultDate = serverRegTime;
				}else{
					resultDate = dbRegTime;
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return resultDate;
	}
	
	private ModelAndView getDeviceModelAndView(ModelAndView mav){
		mav.addObject(MAV_GROUP, monadService.getList(new Query(), Group.class));
		mav.addObject(MAV_TYPE,getDevicePropertiesList());
		return mav;
	}
	
	private List<Object> getDevicePropertiesList(){
		List<Object> typeLst = new ArrayList<Object>();
		typeLst = adminDao.typeList();
		return typeLst;
	}
	
	@RequestMapping("/groupSelect.do")
	public void groupSelect(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.debug("start - groupSelect()");
		String gcode = request.getParameter("groupCode");
		String setNull = Utility.isNullNumber(gcode);
		int groupCode = Integer.parseInt(setNull);
		CommonDto commDto = new CommonDto();
		commDto.setGroupCode(groupCode);
		Class<?> dtoClass = null;
		if(groupCode==0){
			dtoClass = Group.class;
		}else{
			dtoClass = Device.class;
		}

		Query query = new Query();
		query = setGroupCode(commDto);
		
		PersonJson result = setPersonJson(dtoClass,query);
		JSONObject jsonObject = JSONObject.fromObject(result);
		
		Writer writer = setResponse(response).getWriter();
		writer.write(jsonObject.toString());
		
		log.debug(jsonObject.toString());
		writer.flush();
			
		log.debug("end - groupSelect()");
	}
	
	@RequestMapping("/deviceSelect.do")
	public void deviceSelect(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.debug("start - deviceSelect()");
		int idx = Integer.parseInt(Utility.isNullNumber(request.getParameter("deviceCode")));
		
		Class<?> dtoClass = Device.class;

		Query query = new Query();
		query = setIdx(idx);
		
		PersonJson result = setPersonJson(dtoClass,query);
		JSONObject jsonObject = JSONObject.fromObject(result);
		
		Writer writer = setResponse(response).getWriter();
		writer.write(jsonObject.toString());
		
		log.debug(jsonObject.toString());
		writer.flush();
		Thread.sleep(100);
		log.debug("end - deviceSelect()");
	}
	
	@RequestMapping("/eventSelect.do")
	public void eventSelect(HttpServletRequest request, HttpServletResponse response,CommonDto dto) throws Exception{
		log.debug("start - list()");
		Class<?> dtoClass = Critical.class;

		Query query = new Query();
		query = setCriSearch(dto);
		
		PersonJson result = setPersonJson(dtoClass,query);
		JSONObject jsonObject = JSONObject.fromObject(result);
		
		Writer writer = setResponse(response).getWriter();
		writer.write(jsonObject.toString());
		
		log.debug(jsonObject.toString());
		writer.flush();
			
		log.debug("end - list()");
	}
	
	/**
	 * 특정 권한을 갖고 있는 사용자가 몇명인지 찾는다.
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/authCheck.do")
	public void authCheck(HttpServletRequest request, HttpServletResponse response) throws Exception{
		Class<?> dtoClass = User.class;

		int authority = Integer.parseInt(Utility.isNullNumber(request.getParameter("authority")));
		
		Query query = new Query();
		query = setAuthority(authority);

		int cnt = (int)monadService.getCount(query, dtoClass);
		
		Writer writer = setResponse(response).getWriter();
		writer.write(Integer.toString(cnt));
		
		writer.flush();
			
		log.debug("end - authCheck()");
	}
	
	public PersonJson setPersonJson(Class<?> clazz, Query query){
		int cnt = (int)monadService.getCount(query, clazz);
		List<Object> resultList = monadService.getList(query, clazz);
		
		PersonJson result = new PersonJson();
		result.setiTotalRecords(cnt);
		result.setiTotalDisplayRecords(cnt);
		result.setAaData(resultList);
		
		return result;
	}
	
	/**
	 * 관리 데이터 리스트
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@SuppressWarnings("rawtypes")
	@RequestMapping("/list.do")
	public void list(HttpServletRequest request, HttpServletResponse response,CommonDto dto) throws Exception{
		log.debug("start - list()");

		int dival = Integer.parseInt(request.getParameter(REQ_DIVAL));
		dto.setDival(dival);
		String tablenm = TABLENAME[dival];
		dto.setLoginUserCode(Integer.parseInt(Utility.isNullNumber(request.getParameter("loginUserCode"))));
		dto.setLoginAuth(Integer.parseInt(Utility.isNullNumber(request.getParameter("loginAuth"))));

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
		
		int sEcho = Integer.parseInt(Utility.isNullNumber(request.getParameter(REQ_SECHO)));
		PersonJson result = setPersonGroupJson( sEcho, dto, tablenm, pageNumber, nPerPage);
		JSONObject jsonObject = JSONObject.fromObject(result);
		
		Writer writer = setResponse(response).getWriter();
		writer.write(jsonObject.toString());
		
		log.debug(jsonObject.toString());
		writer.flush();
			
		log.debug("end - list()");
	}
	
	private PersonJson setPersonGroupJson(int sEcho, CommonDto dto,String tablenm,int pageNumber, int nPerPage){
		Class<?> classname = managementDao.getDtoClassNm(tablenm);
		String searchGubun = dto.getSearch_gubun();
		String searchText = dto.getSearch_text();
		
		Query query = new Query();
		if(isSearch(searchGubun, searchText)){
			query = getSearchQuery(searchGubun, searchText);
		}else{
			query.sort().on("groupBind", Order.ASCENDING);
//			query.sort().on("reg_date", Order.DESCENDING);
			query.sort().on("groupCode", Order.ASCENDING);
			query.sort().on("deviceCode", Order.ASCENDING);
			query.sort().on("reg_date", Order.DESCENDING);
//			query = sortDate(dto.getLoginAuth(),dto.getLoginUserCode(),tablenm);
		}
		
		int cnt = (int)monadService.getCount(query, classname);
		List<Object> resultList = monadService.getList(query.skip(pageNumber).limit(nPerPage), classname);
		
		PersonJson result = new PersonJson();
		result.setsEcho(sEcho);
		result.setiTotalRecords(cnt);
		result.setiTotalDisplayRecords(cnt);
		result.setAaData(resultList);
		
		return result;
	}
	
	private PersonJson setPersonJson(int sEcho, CommonDto dto,String tablenm,int pageNumber, int nPerPage){
		Class<?> classname = managementDao.getDtoClassNm(tablenm);
		String searchGubun = dto.getSearch_gubun();
		String searchText = dto.getSearch_text();
		
		Query query = new Query();
		if(isSearch(searchGubun, searchText)){
			query = getSearchQuery(searchGubun, searchText);
		}else{
			query = sortDate(dto.getLoginAuth(),dto.getLoginUserCode(),tablenm);
		}
		
		int cnt = (int)monadService.getCount(query, classname);
		query.sort().on("groupBind", Order.ASCENDING);
		List<Object> resultList = monadService.getList(query.skip(pageNumber).limit(nPerPage), classname);
		
		PersonJson result = new PersonJson();
		result.setsEcho(sEcho);
		result.setiTotalRecords(cnt);
		result.setiTotalDisplayRecords(cnt);
		result.setAaData(resultList);
		
		return result;
	}
	
	private boolean isSearch(String searchGubun, String searchText){
		return (searchGubun != null && !searchGubun.equals("") && 
				searchText != null && !searchText.equals(""));
	}
	
	private Query getSearchQuery(String searchGubun, String searchText){
		Query query = new Query();
		if(searchGubun.equals(COL_GROUPCODE)){
			query.addCriteria(Criteria.where(searchGubun).is(Integer.parseInt(searchText)));
		}else if(searchGubun.equals(COL_DEVICECODE)){
			query.addCriteria(Criteria.where(IDX).is(Integer.parseInt(searchText)));
		}else if(searchGubun.equals(SEARCHALL)){
			
		}else if(searchGubun.equals(DEVICE_TYPE)){
			query.addCriteria(Criteria.where(searchGubun).is(StringUtils.getEncStr(searchText)));
		}else{
			query.addCriteria(Criteria.where(searchGubun).is(searchText));
		}
		return query;
	}
	
	private HttpServletResponse setResponse(HttpServletResponse response){
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");
		
		return response;
	}

	/**
	 * 등록 - index가 필요할 때
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/insert.do")
	public ModelAndView insert(HttpServletRequest request,CommonDto dto) throws Exception{
		log.debug("start - insert()");
		String name ;
		String tablenm = TABLENAME[dto.getDival()];
		if(tablenm.equals("user")){
			name = new String(request.getParameter("username").getBytes("8859_1"),"utf-8");
			dto.setUsername(name);
		}
		Class<?> classname = managementDao.getDtoClassNm(tablenm);
		Query query=new Query();
		Object obj= new Object();
		try{
			if(tablenm.equals("device")){
				List<Object> deviceLst = monadService.getList(query, classname);
				if(isExistDeviceIpPort(dto.getIp(), dto.getPort())==null){//동일한 ip,port가 없을때
					query = setUid(StringUtils.getEncStr(dto.getUid()));
					obj=monadService.getFind(query, classname);//동일한 name 있는지 확인
				}
			}else if(tablenm.equals("critical")){
				query = setCriSearch(dto);
				obj=monadService.getFind(query, classname);//동일한 항목이 있는지 확인
			}else{
				query = setUid(dto.getUid());//동일한 name 확인 쿼리
				obj=monadService.getFind(query, classname);//동일한 name 있는지 확인
			}
			if(obj==null){//동일한게 없을 때
				dto.setIdx(indexDao.createIdx(tablenm));//idx 생성 또는 수정
				if(tablenm.equals("device")){//device는 등록 시 기본 임계값도 함께 등록
					if(dto.getGroupText()!=null)makeGroupCode(dto);//새로운 그룹 선택 시 그룹 추가
					if(Utility.isNull(dto.getMessage()).equals(""))makeCritical(dto);
				}
				if(Utility.isNull(dto.getMessage()).equals("")){
					obj = managementDao.setDto(tablenm, dto);//각각 dto에 commondto에서 받은 값을 set
					monadService.add(obj, classname);
					
					if(tablenm.equals(MAV_DEVICE)){
						updateDevice();
						updateGroup();					
						
						Device rrdDeviceInfo = (Device)obj;
						DeviceInMemory.addDeviceIntoMap(rrdDeviceInfo);
						MongoInMemory.addMongoIntoMap(rrdDeviceInfo);
						rrdInitializer.install(rrdDeviceInfo);
						rrdInitializer.input(rrdDeviceInfo.getIdx());
					}
					
				}
			}else{
				dto.setMessage("이미 등록 된 내용입니다.");
			}
		}catch(Exception e){
			log.error(e, e);
			dto.setMessage("등록 중 에러 발생");
		}
		log.debug("end - insert()");
		return listManagement(dto);
	}
	
	public CommonDto makeGroupCode(CommonDto dto)throws Exception{
		Query query = setUid(dto.getGroupText());
		Object obj=monadService.getFind(query, Group.class);
		if(obj==null){
			CommonDto gdto = new CommonDto();
			int idx = 0;
			idx = indexDao.createIdx("group");
			gdto.setIdx(idx);//idx 생성 또는 수정
			gdto.setUid(dto.getGroupText());
			obj = managementDao.setDto("group", gdto);//각각 dto에 commondto에서 받은 값을 set
			
			monadService.add(obj, Group.class);
			dto.setGroupCode(idx);
		}else{
			dto.setMessage("이미 등록 되어있는 그룹입니다.");
		}
		return dto;
	}
	
	public void makeCritical(CommonDto dto)throws Exception{
		Critical commdto = new Critical();
		commdto.setGroupCode(dto.getGroupCode());
		commdto.setDeviceCode(dto.getIdx());
		String type = dto.getType();
		List<Critical> criticalList = new CriticalInfo().criticalInfoXMLToList();
		for (Critical critical : criticalList) {

			String deviceType = critical.getDeviceType();
			if(deviceType.equals(type) || deviceType.equals("both") && !type.equals("config")){
					critical.setGroupCode(dto.getGroupCode());
					critical.setDeviceCode(dto.getIdx());
					
					// 버전 구분
					if (critical.getVersion().equals(MONGOVER2_2)){
						if (!isMongoVer2_2) continue; 
					} else if (critical.getVersion().equals(MONGOVER2_0)) {
						if (isMongoVer2_2) continue;
					}
					
					if(critical.getUnit().equals(UNIT_SECONDS)){
						critical.setCriticalvalue(critical.getCriticalvalue()*1000000);
						critical.setWarningvalue(critical.getWarningvalue()*1000000);
						critical.setInfovalue(critical.getInfovalue()*1000000);
					}
					critical.setReg_date(DateUtil.getTime());
					critical.setIdx(indexDao.createIdx("critical"));
					monadService.add(critical, Critical.class);
			}
		}
	}
	
	/**
	 * 데몬등록 시 mongo 존재 확인
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/mongoExistCheck.do")
	public void mongoExistCheck(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String ip = Utility.isNull(request.getParameter("ipStr"));
		String port = Utility.isNull(request.getParameter("portStr"));
		String daemonName = Utility.isNull(request.getParameter("nameStr"));
		String userStr = Utility.isNull(request.getParameter("authUserStr"));
		String passwdStr = Utility.isNull(request.getParameter("authPasswdStr"));
		String editModeStr = Utility.isNull(request.getParameter("editMode"));
		int idx = Integer.parseInt(Utility.isNullNumber(request.getParameter("idx")));
		String isExistMongo = "0";
		String INSERT_MODE = "insertMode";
		String UPDATE_MODE = "updateMode";
		Class<?> classname = Device.class;
		Query query = new Query();
		Object obj = null;
		try{
			if(obj==null && isExistMongo.equals("0")){
				query = setIpPort(StringUtils.getEncStr(ip), StringUtils.getEncStr(port),idx);
				obj=monadService.getFind(query, classname);//동일한ip,port가 있는지 확인
				if(obj!=null)isExistMongo = "1";
			}
			if(obj==null && isExistMongo.equals("0")){//동일한 ip,port가 없을때
				if(editModeStr.equals(INSERT_MODE)){
					query = setUid(StringUtils.getEncStr(daemonName));
				}else if(editModeStr.equals(UPDATE_MODE)){
					query = notIdxsetUid(idx,StringUtils.getEncStr(daemonName));
				}
				obj=monadService.getFind(query, classname);//동일한 name 있는지 확인
				if(obj!=null)isExistMongo = "2";
			}
			if(obj==null && isExistMongo.equals("0")){
				isExistMongo = managementDao.mongoExistCheck(ip, Integer.parseInt(port), userStr, passwdStr, isExistMongo);//해당 mongo가 살아있는지 확인
			}
			
			Writer writer = setResponse(response).getWriter();
			writer.write(isExistMongo);
			
			writer.flush();
		}catch(Exception e){
			log.error(e, e);
		}
		log.debug("end - mongoExistCheck()");
	}

	/**
	 * 같은 ip, port를 가진 device정보를 검색
	 * @param ip
	 * @param port
	 * @return
	 */
	private Object isExistDeviceIpPort(String ip, String port){
		Object obj = null;
		Query query = setIpPort(StringUtils.getEncStr(ip), StringUtils.getEncStr(port),0);
		obj = monadService.getFind(query, managementDao.getDtoClassNm("device"));
		return obj;
	}
	
	/**
	 * mongos등록 시 클러스터를 구성하고 있는 데몬들의 정보를 구함
	 * @param request
	 * @param response
	 * @throws Exception
	 */
	@RequestMapping("/mongosResearch.do")
	public void mongosResearch(HttpServletRequest request, HttpServletResponse response, CommonDto commDto) throws Exception{
		String authUser = Utility.isNull(commDto.getAuthUser());
		String authPasswd = Utility.isNull(commDto.getAuthPasswd());
		String tempIp = Utility.isNull(commDto.getIp());
		boolean loopbackCheck = commDto.isLoopbackCheck();
		
		boolean authCheck = false;
		Mongo mongo = null;
		List<Map<String, Object>> resultLst = new ArrayList<Map<String, Object>>();
		boolean auth = false;
		DB db;
		try{
			PersonJson result = new PersonJson();
			if(authUser != "" && authPasswd != "")authCheck = true;
			List<Object> readMongoShards = new ArrayList<Object>();
			List<Object> readMongos = new ArrayList<Object>();
			Object readConfigDb = null;
			mongo = new Mongo(commDto.getIp(), Integer.parseInt(commDto.getPort()));
			if(authCheck){
				db = mongo.getDB("admin");
				auth = db.authenticate(authUser, authPasswd.toCharArray());
			}
			readMongos = mongoStatusToMonitor.readMongoShards(mongo, "mongos", "config");
			readMongoShards = mongoStatusToMonitor.readMongoShards(mongo, "shards", "config");
			
			readConfigDb = mongoStatusToMonitor.readMongoStatus(mongo, new Device(), "getCmdLineOpts").get("parsed_configdb");
			mongo.close();

			//mongos
			int mongoIdNum = 0;
			for(Object mongosObj : readMongos){
				mongoIdNum++;
				Map<String, Object> statusMap = (Map<String, Object>) mongosObj;
				String[] splitName = ((String) statusMap.get("_id")).split(":");
				
				boolean isExistCheck = false;
				if(isExistDeviceIpPort(splitName[0], splitName[1]) != null)isExistCheck = true;
				statusMap.put("isExistCheck", isExistCheck);
				statusMap.put("ipStr", splitName[0]);
				statusMap.put("portStr", splitName[1]);
				statusMap.put("groupNameStr", "mongos_grp");
				statusMap.put("uidStr", "mongos"+ "_" + mongoIdNum +"_"+splitName[1]);
				statusMap.put("typeStr", "mongos");
				
				resultLst.add(statusMap);
			}
			
			// mongod
			for(Object shardObj : readMongoShards){
				Map<String, Object> shardMap = (Map<String, Object>) shardObj;
				String[] host = ((String) shardMap.get("host")).split("/");
				String[] hostLst = host[1].split(",");
					String[] ipPortSplit = hostLst[0].split(":");
					
					if(ipPortSplit[0].equals("127.0.0.1") || ipPortSplit[0].equals("localhost")){
						ipPortSplit[0] = tempIp;
					}
					
					mongo = new Mongo(ipPortSplit[0], Integer.parseInt(ipPortSplit[1]));
					if(authCheck){
						db = mongo.getDB("admin");
						auth = db.authenticate(authUser, authPasswd.toCharArray());
					}
					Map<String, Object> readMongoStatus = mongoStatusToMonitor.readMongoStatus(mongo, new Device(), "replSetGetStatus");
					mongoIdNum = 0;
					for(Object status :(List<Object>) readMongoStatus.get("members")){
						mongoIdNum++;
						Map<String, Object> statusMap = (Map<String, Object>) status;
						
						String[] splitName = ((String) statusMap.get("name")).split(":");
						
						if(loopbackCheck == true && (splitName[0].equals("127.0.0.1") || splitName[0].equals("localhost"))){
							splitName[0] = tempIp;
						}
						
						boolean isExistCheck = false;
						if(isExistDeviceIpPort(splitName[0], splitName[1]) != null)isExistCheck = true;
						statusMap.put("isExistCheck", isExistCheck);
						statusMap.put("ipStr", splitName[0]);
						statusMap.put("portStr", splitName[1]);
						statusMap.put("groupNameStr", readMongoStatus.get("set"));
						statusMap.put("uidStr", readMongoStatus.get("set")+ "_" + mongoIdNum +"_"+statusMap.get("stateStr"));
						statusMap.put("typeStr", "mongod");
						
						resultLst.add(statusMap);
					}
					mongo.close();
			}
			
			//config
			mongoIdNum = 0;
			for(String configStr : ((String)readConfigDb).split(",")){
				mongoIdNum++;
				Map<String, Object> statusMap = new HashMap<String, Object>();
				
				String[] splitName = configStr.split(":");
				
				if(loopbackCheck == true && (splitName[0].equals("127.0.0.1") || splitName[0].equals("localhost"))){
					splitName[0] = tempIp;
				}
				
				boolean isExistCheck = false;
				if(isExistDeviceIpPort(splitName[0], splitName[1]) != null)isExistCheck = true;
				statusMap.put("isExistCheck", isExistCheck);
				statusMap.put("ipStr", splitName[0]);
				statusMap.put("portStr", splitName[1]);
				statusMap.put("groupNameStr", "config_grp");
				statusMap.put("uidStr", "config"+ "_" + mongoIdNum +"_"+splitName[1]);
				statusMap.put("typeStr", "config");
				
				resultLst.add(statusMap);
			}
			
			result.setAaData(resultLst);
			JSONObject jsonObject = JSONObject.fromObject(result);
			
			Writer writer = setResponse(response).getWriter();
			writer.write(jsonObject.toString());
			
			writer.flush();
		}catch(Exception e){
			e.printStackTrace();
			mongo.close();
		}
		log.debug("end - mongosResearch()");
	}

	/**
	 * mongos등록 시 클러스터를 구성하고 있는 데몬들의 정보를 보여주는 페이지
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/mongosResearchView.do")
	public ModelAndView mongosResearchView(CommonDto paramDTO) throws Exception{
		log.debug("start - mongosResearchView()");
		ModelAndView mav = new ModelAndView();		
		mav.setViewName(PATH_MANAGEMEMT+"mongosResearchView");
		log.debug("end - mongosResearchView()");
		return mav;
	}
	
	/**
	 * mongos 클러스터를 구성하고 있는 데몬들의 정보를 등록
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/researchDeviceInsert.do")
	public void researchDeviceInsert(HttpServletRequest request, HttpServletResponse response, CommonDto paramDTO) throws Exception{
		log.debug("start - researchDeviceInsert()");
		String tablenm = "device";
		Class<?> classname = managementDao.getDtoClassNm(tablenm);
		Query query=new Query();
		Object obj= new Object();
		boolean isExistDevice = true;
		String insertLst = "";
		int insertCnt = 0;
		try{
			String[] researchLst = paramDTO.getResearchLst();
			for(String arr : researchLst){
				isExistDevice = true;
				String[] deviceArray = arr.split("\\|");
				CommonDto commDTO = new CommonDto();
				commDTO.setGroupText(deviceArray[0]);
				commDTO.setUid(deviceArray[1]);
				commDTO.setType(deviceArray[2]);
				commDTO.setIp(deviceArray[3]);
				commDTO.setPort(deviceArray[4]);
				commDTO.setMemorysize(Integer.parseInt(deviceArray[5]));
				commDTO.setHddsize(Integer.parseInt(deviceArray[6]));
				if(deviceArray.length>7){
					commDTO.setAuthUser(deviceArray[7]);
					commDTO.setAuthPasswd(deviceArray[8]);
				}
				List<Object> deviceLst = monadService.getList(new Query(), classname);
					if(isExistDeviceIpPort(commDTO.getIp(), commDTO.getPort()) != null)isExistDevice = false;//동일한ip,port가 있는지 확인
					if(isExistDevice){//동일한 ip,port가 없을때
						query = setUid(StringUtils.getEncStr(commDTO.getUid()));//동일한 name 있는지 확인
						if(monadService.getFind(query, classname) != null)isExistDevice = false;
					}
					if(isExistDevice){
						query = setUid(commDTO.getGroupText());
						obj=monadService.getFind(query, Group.class);
						if(obj==null){
							CommonDto gdto = new CommonDto();
							int idx = 0;
							idx = indexDao.createIdx("group");
							gdto.setIdx(idx);//idx 생성 또는 수정
							gdto.setUid(commDTO.getGroupText());
							obj = managementDao.setDto("group", gdto);//각각 dto에 commondto에서 받은 값을 set
							
							monadService.add(obj, Group.class);
							commDTO.setGroupCode(idx);
						}else{
							Group gdto = (Group) obj;
							commDTO.setGroupCode(gdto.getIdx());
						}
					}
					if(isExistDevice){
						commDTO.setIdx(indexDao.createIdx(tablenm));//idx 생성 또는 수정
						makeCritical(commDTO);
						obj = managementDao.setDto(tablenm, commDTO);//각각 dto에 commondto에서 받은 값을 set
						monadService.add(obj, classname);
						if(insertCnt == 0)insertLst += commDTO.getUid();
						else insertLst += ","+commDTO.getUid();
						insertCnt++;
						
						updateDevice();
						updateGroup();					
						
						Device rrdDeviceInfo = (Device)obj;
						DeviceInMemory.addDeviceIntoMap(rrdDeviceInfo);
						MongoInMemory.addMongoIntoMap(rrdDeviceInfo);
						rrdInitializer.install(rrdDeviceInfo);
						rrdInitializer.input(rrdDeviceInfo.getIdx());
						Thread.sleep(100);
					}
			}
			Writer writer = setResponse(response).getWriter();
			writer.write(insertLst+"|"+insertCnt);
			
			writer.flush();
		}catch(Exception e){
			log.error(e, e);
		}
		log.debug("end - researchDeviceInsert()");
	}

	@RequestMapping("/deviceBatchUpdate.do")
	public void deviceBatchUpdate(HttpServletRequest request, HttpServletResponse response, CommonDto paramDTO) throws Exception{
		log.debug("start - deviceBatchUpdate()");
		String tablenm = "device";
		Class<?> classname = managementDao.getDtoClassNm(tablenm);
		Query query = new Query();
		boolean isExistValue = true;
		try{
			int hddBatchStr = Integer.parseInt(Utility.isNullNumber(paramDTO.getBatchHddSize()));
			int memoryBatchStr = Integer.parseInt(Utility.isNullNumber(paramDTO.getBatchMemorySize()));
			int[] idxLst = paramDTO.getIdxLst();
			
			Update update = new Update();
			if(hddBatchStr != 0)update.set("hddsize",hddBatchStr);
			if(memoryBatchStr != 0)update.set("memorysize",memoryBatchStr);
			if(hddBatchStr == 0 && memoryBatchStr == 0)isExistValue = false;
			
			if(isExistValue){
				for(int idx : idxLst){
					query = setIdx(idx);
					monadService.update(query, update, classname);
					
					Device deviceInfo = (Device)monadService.getFind(query, classname);
					DeviceInMemory.updateDeviceMap(deviceInfo);
					Thread.sleep(100);
				}
			}
		}catch(Exception e){
			log.error(e, e);
		}
		log.debug("end - deviceBatchUpdate()");
	}
	
	/**
	 * Global 등록 - 전역 관리는 idx가 필요하지 않아 따로 생성
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/insertGlobal.do")
	public ModelAndView insertGlobal(CommonDto dto) throws Exception{
		log.debug("start - insertGlobal()");
		String tablenm = TABLENAME[dto.getDival()];
		Class<?> classname = managementDao.getDtoClassNm(tablenm);
		Query query=new Query();
		try{
			String[] essentialGlobalVariable = Config.getConfig("essentialGlobalVariable").split(";");
			String mainRefreshPeriodMinute = essentialGlobalVariable[1];
			String versionStr = essentialGlobalVariable[4];
			String hostNameStr = essentialGlobalVariable[5];
			String dateStr = DateUtil.getTime();
			
			query = setUid(mainRefreshPeriodMinute);
			Update update = new Update();
			update.set("value",dto.getMainRefreshPeriodMinute()*1000);
			update.set("up_date",dateStr);
			monadService.update(query, update, classname);

			query = setUid(hostNameStr);
			update = new Update();
			update.set("value",dto.getHostname());
			update.set("up_date",dateStr);
			monadService.update(query, update, classname);
			
			query = setUid(versionStr);
			update = new Update();
			update.set("value",dto.getMongo_version());
			update.set("up_date",dateStr);
			monadService.update(query, update, classname);
		}catch(Exception e){
			log.error(e, e);
			dto.setMessage("등록 중 에러 발생");
		}
		log.debug("end - insertGlobal()");
		return listManagement(dto);
	}
	
	/**
	 * 수정 - 수정을 위해서는 jsp 페이지에서 idx 값을 가져와야 함
	 * @param ddto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/update.do")
	public ModelAndView update(HttpServletRequest request, CommonDto dto) throws Exception{
		log.debug("start - update()");
		String name ;
		String tablenm = TABLENAME[dto.getDival()];
		Class<?> classname = managementDao.getDtoClassNm(tablenm);
		if(tablenm.equals("user")){
			name = new String(request.getParameter("username").getBytes("8859_1"),"utf-8");
			dto.setUsername(name);
		}
		Query query=new Query();
		Update update = new Update();
		Object obj= new Object();
		try{
			//수정을 위해서는 jsp 페이지에서 idx 값을 가져와야 함
			if(tablenm.equals("device")){
				query = setIpPort(StringUtils.getEncStr(dto.getIp()), StringUtils.getEncStr(dto.getPort()),dto.getIdx());
				obj=monadService.getFind(query, classname);//동일한ip,port가 있는지 확인
				if(obj==null){//동일한 ip,port가 없을때
					query = notIdxsetUid(dto.getIdx(),StringUtils.getEncStr(dto.getUid()));
				}
			}else{
				query = notIdxsetUid(dto.getIdx(),dto.getUid());
			}
			obj = monadService.getFind(query, classname);//동일한 name 있는지 확인
			if(tablenm.equals("critical"))obj = null;//임계값은 같은 값이 있을때 무조건 수정으로 들어오기 때문에 동일항목 찾는 과정이 필요 없다.
			if(obj==null){
				if(tablenm.equals("device") && dto.getGroupText()!="")makeGroupCode(dto);//새로운 그룹 선택 시 그룹 추가
				if(Utility.isNull(dto.getMessage()).equals("")){
					if(tablenm.equals("device")){
						Device devInfo = (Device) monadService.getFind(setIdx(dto.getIdx()), classname);
						dto.setFinishedInitailRrd(devInfo.isFinishedInitailRrd());
					}
					update = managementDao.setUpdate(tablenm, dto);
					query = setIdx(dto.getIdx());
					monadService.update(query, update, classname);
					
					if(tablenm.equals("device")){
						updateDevice();
						updateGroup();
						if(!dto.getType().equals("config"))updateCritical(dto);
						
						Device rrdDeviceInfo = (Device)monadService.getFind(new Query(Criteria.where(IDX).is(dto.getIdx())), Device.class);
						
						DeviceInMemory.updateDeviceMap(rrdDeviceInfo);
						MongoInMemory.updateMongoMap(rrdDeviceInfo);
					}else if(tablenm.equals("group")){
						updateGroup();
						dto.setDival(1);
					}
				}
			}else{
				dto.setMessage("이미 사용중인 이름입니다.");
			}
		}catch(Exception e){
			log.error(e, e);
			dto.setMessage("등록 중 에러 발생");
		}
		log.debug("end - update()");
		return listManagement(dto);
	}

	/**
	 * 초기화 완료 후 update
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	public boolean update(Device dto) throws Exception{
		log.debug("start - update(Device dto)");
		boolean isUpdated=false;
		String name ;
		String tablenm = TABLENAME[1];
		Class<?> classname = managementDao.getDtoClassNm(tablenm);

		Query query=new Query();
		Update update = new Update();
		Object obj= new Object();
		try{

			query = setIpPort(dto.getIp(), dto.getPort(),dto.getIdx());
			obj=monadService.getFind(query, classname);//동일한ip,port가 있는지 확인
			query = notIdxsetUid(dto.getIdx(),dto.getUid());
			
			update.set("isFinishedInitailRrd",dto.isFinishedInitailRrd());
			query = setIdx(dto.getIdx());
			monadService.update(query, update, classname);
			
			isUpdated=true;

		}catch(Exception e){
			log.error(e, e);
		}
		log.debug("end - update(Device dto)");
		return isUpdated;
	}
	
	/**
	 * 임계값을 등록한다.
	 * @param dto
	 */
	public void updateCritical(CommonDto dto){
		Update update = new Update();
		Query query = new Query();
		update.set("groupCode", dto.getGroupCode());
		query = setDeviceCode(dto.getIdx());
		Object obj = monadService.getFind(query, Critical.class);//동일한 name 있는지 확인
		if(obj!=null)monadService.updateMulti(query, update, MAV_CRITICAL);
	}
	
	private void updateGroup(){
		Code.group.getCode().updateCode();
	}
	
	private void updateDevice(){
		Code.device.getCode().updateCode();
	}
	
	/**
	 * 삭제 - 삭제를 위해서는 jsp 페이지에서 idx 값을 가져와야 함
	 */
	@RequestMapping("/delete.do")
	public void delete(HttpServletRequest request, HttpServletResponse response, CommonDto dto) throws Exception{
		log.debug("start - delete()");
		String tablenm = TABLENAME[dto.getDival()];
		Class<?> classname = managementDao.getDtoClassNm(tablenm);
		Query query=new Query();
		try{
			String[] groupBindLst = dto.getGroupBindLst();
			String groupBind = dto.getGroupBind();
			int[] idxLst = dto.getIdxLst();
			int loginUserCode = dto.getLoginUserCode();
			
			if(groupBindLst != null){
				for(int i = 0 ; i < groupBindLst.length; i++){
					String tempgroupBind = groupBindLst[i];
					unGroupBind(tempgroupBind);
				}
			}
			
			String groupIdxStr = request.getParameter("groupIdx");
			if(!StringUtils.isNull(groupIdxStr)){
				int groupIdx = Integer.parseInt(groupIdxStr);
				deleteGroupIdx(groupIdx);
			}
			
			if(idxLst != null){
				for(int i=0;i<idxLst.length;i++){
					int idx = idxLst[i];
					query = setIdx(idx);
					if(tablenm.equals("device")) {
						DeviceInMemory.deleteDeviceMap(idx);
						MongoInMemory.deleteMongoMap(idx);
						deleteSubLstDevice(idx);
					}else if(tablenm.equals("group")){
						Query groupCodeQuery = new Query();
						groupCodeQuery = setGroupCode(dto);
						List<Object> resultList = monadService.getList(groupCodeQuery, Device.class);
						for(int resultIdx = 0; resultIdx<resultList.size(); resultIdx++){
							Device device = (Device) resultList.get(resultIdx);
							DeviceInMemory.deleteDeviceMap(device.getIdx());
							MongoInMemory.deleteMongoMap(device.getIdx());
							--Config.totalThreadCount;
						}
						deleteGroup(dto.getGroupCode());
					}
					if(tablenm.equals("user")){
						if(idx != loginUserCode)monadService.delete(query, classname);
					}else{
						monadService.delete(query, classname);
					}
				}
			}
				Thread.sleep(100);
		}catch(Exception e){
			log.error(e, e);
			dto.setMessage("삭제 중 에러 발생");
		}
		Writer writer = setResponse(response).getWriter();
		writer.write("");
		
		writer.flush();
		log.debug("end - delete()");
	}
	
	private void deleteGroup(int groupCode){
		Code.group.getCode().updateCode();
		
		Query query = new Query(Criteria.where("groupCode").is(groupCode));
		monadService.delete(query, Device.class);
		monadService.delete(query, Critical.class);
		monadService.delete(query, COLL_DASHBOARD);
		monadService.delete(query, COLL_ALARM);
	}
	
	private void deleteDevice(int deviceCode){
		Code.device.getCode().updateCode();
		
		Query query = new Query(Criteria.where("deviceCode").is(deviceCode));
		monadService.delete(query, Critical.class);
		monadService.delete(query, COLL_DASHBOARD);

	}
	
	private void deleteSubLstDevice(int deviceCode){
		
		Query query = new Query(Criteria.where("deviceCode").is(deviceCode));
		List<Object> objLst = monadService.getList(query, Critical.class);
		Set<String> groupBindSet = new HashSet<String>();
		
		for(Object obj : objLst){
			String tempGroupBind = ((Critical)obj).getGroupBind();
			if(tempGroupBind != null){// groupBind가 없다면  
				groupBindSet.add(tempGroupBind);
			}
		}
		
		for(String groupBind : groupBindSet){
			Query q = new Query(Criteria.where("groupBind").is(groupBind));
			List<Object> objTempLst = monadService.getList(q, Critical.class);
			Set<Integer> deviceSet = new HashSet<Integer>();
			
			for(Object objtemp : objTempLst){
				int tempDeviceCode = ((Critical)objtemp).getDeviceCode();
				deviceSet.add(tempDeviceCode);
			}
			
			if(deviceSet.size() <= 2){
				unGroupBind(groupBind);
			}
		}
		
		deleteDevice(deviceCode);
		
	}
	
	private void deleteGroupIdx(int groupIdx){
		Query q = new Query();
		q = setIdx(groupIdx);
//		q = setDeviceCode(0);
		monadService.delete(q, MAV_ALARM);
	}
	
	private void unGroupBind(String groupBind){
		Query q = new Query(); 
		q = setgroupBind(q, groupBind);
		Update update = new Update();
		update.unset("groupBind");
		monadService.updateMulti(q, update, MAV_CRITICAL, false);
	}

	/**
	 * confrim 확인 팝업 페이지
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/isFinishedInitialRrd.do")
	public void isFinishedInitailRrd(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.debug("start - isFinishedInitialRrd");
		int deviceCode = 0;
		try {
			String strDeviceCode = request.getParameter("deviceCode");
			if (strDeviceCode == null) throw new NullPointerException();
			
			deviceCode = Integer.parseInt(request.getParameter("deviceCode"));
						
			boolean finished = DeviceInMemory.isFinishedInitialRrd(deviceCode);		
			
			Writer writer = response.getWriter();
			writer.write(finished+"");
		} catch (NullPointerException e) {
			log.error("	device code is null", e);
		} catch (Exception e) {
			log.error("	fail get finishedInitial flag in device id " + deviceCode, e);
		}
		log.debug("end - isFinishedInitialRrd");
	}
	
	@RequestMapping("/deleteMongoLogStart.do")
	public void deleteMongoLogStart(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.debug("start - deleteMongoLogStart");
		String startDateStr = Utility.isNull(request.getParameter("startDateStr")).replace("-", "") + "000000000"; 
		String endDateStr = Utility.isNull(request.getParameter("endDateStr")).replace("-", "") + "235959999"; 
		StatusInMemory.setDate(startDateStr, endDateStr);
		Class<?> ServerStatus = ServerStatus.class;
		Class<?> dbStatus = DbStatus.class;
		try {
			Query query = deleteLogQuery();
			int serverStCnt = (int) monadService.getCount(query, ServerStatus);
			int dbStCnt = (int) monadService.getCount(query, dbStatus);
			
			String result = "";
			if(serverStCnt == 0 && dbStCnt == 0){
				result = "nodate";
			}else{
				StatusInMemory.setTotalCnt(serverStCnt, dbStCnt);
				ServerStatusDeleteThread serverStatusDeleteThread= new ServerStatusDeleteThread(monadService);
				serverStatusDeleteThread.start();
				DbStatusDeleteThread dbStatusDeleteThread = new DbStatusDeleteThread(monadService);
				dbStatusDeleteThread.start();
				result = "okdate";
			}
			
			Writer writer = response.getWriter();
			writer.write(result);
		} catch (Exception e) {
			log.error(e, e);
		}
		log.debug("end - deleteMongoLogStart");
	}

	@RequestMapping("/deleteMongoLogStop.do")
	public void deleteMongoLogStop(HttpServletRequest request, HttpServletResponse response) throws Exception{
		log.debug("start - deleteMongoLogStop");
		try {
			StatusInMemory.resetState();
			
			Writer writer = response.getWriter();
			writer.write("ok");
		} catch (Exception e) {
			log.error(e, e);
		}
		log.debug("end - deleteMongoLogStop");
	}

	@RequestMapping("/deleteMongoLogCnt.do")
	public void deleteMongoLogCnt(HttpServletRequest request, HttpServletResponse response) throws Exception{
		try{	
			DecimalFormat df = new DecimalFormat("#0.00");
			String deletePer = df.format(StatusInMemory.getProcessPer());
			Writer writer = response.getWriter();
			writer.write(deletePer + "|" +StatusInMemory.isDeleteLogState());
		}catch(Exception e){
			
		}
	}
	
	@RequestMapping(value="/popup_event.do", params="!groupText")
	public ModelAndView popup_event(HttpServletRequest request, HttpServletResponse response, CommonDto dto){
		log.debug("start - popup_event");
		ModelAndView mav = new ModelAndView();
		
		String parameter = request.getParameter("dataToSend"); 

		mav.addObject("dataToSend", parameter);
		mav.setViewName(PATH_MANAGEMEMT+"popup_event");
		log.debug("end - popup_event param : groupBind");
		return mav;
	}
	
	@RequestMapping(value="/popup_event.do")
	public ModelAndView popup_event_update(HttpServletRequest request, HttpServletResponse response, CommonDto dto){
		log.debug("start - popup_event");
		
		ModelAndView mav = new ModelAndView();
		
		String groupText = dto.getGroupText();
		String[] strLst = request.getParameter("Lst").split("&");
		int[] idxLst = new int[strLst.length];
		
		for(int i = 0 ; i <strLst.length ; i ++){
			String[] temp = strLst[i].split("=");
			idxLst[i] = Integer.valueOf(temp[1]);
		}
		
		Query query = new Query();
		Update update = new Update();
		
		int idx = 0;
		
		for(int i = 0 ; i<idxLst.length; i++){
			idx = idxLst[i];
			query = setIdx(idx);
			update.set("idx", idx).set("groupBind", groupText).set("groupCnt", idxLst.length);
			monadService.update(query, update, Critical.class);
		}
		
		mav.addObject("groupName", groupText);
		mav.setViewName(PATH_MANAGEMEMT+"popup_event");
		log.debug("end - popup_event param : groupBind");
		return mav;
	}
	
	@RequestMapping("/popup_event_list.do")
	private void popup_event_list(HttpServletRequest request, HttpServletResponse response, CommonDto dto) throws Exception{
		
		int[] idxLst = dto.getIdxLst();
		int idx =0;
		Query query = new Query();
		List<Object> critical = new ArrayList<Object>();
		
		if(idxLst != null){
			for(int i = 0 ; i<idxLst.length; i++){
				idx = idxLst[i];
				query = setIdx(idx);
				critical.add(monadService.getFindOne(query, Critical.class, MAV_CRITICAL));
			}
			
			int sEcho = Integer.parseInt(Utility.isNullNumber(request.getParameter(REQ_SECHO)));
			
			PersonJson result = new PersonJson();
			result.setsEcho(sEcho);
			result.setiTotalRecords(idxLst.length);
			result.setiTotalDisplayRecords(idxLst.length);
			result.setAaData(critical);
			
			JSONObject jsonObject = JSONObject.fromObject(result);
			
			Writer writer = setResponse(response).getWriter();
			writer.write(jsonObject.toString());
			
			log.debug(result.toString());
			writer.flush();
		}	
	}
	
	@RequestMapping("/popup_event_count.do")
	public void popup_event_count(HttpServletRequest request, HttpServletResponse response, CommonDto dto) throws Exception{
		String groupBind = dto.getGroupBind().trim();
		 
		// 이미 동일한 groupBind가 존재하는지 check
		Query check = new Query();
		check = setgroupBind(check, groupBind);
		long count = monadService.getCount(check, Critical.class);

		String countStr = "{ count : " + count + "}";
		JSONObject jsonObject = JSONObject.fromObject(countStr);
		
		Writer writer = setResponse(response).getWriter();
		writer.write(jsonObject.toString());

		log.debug(countStr.toString());
		writer.flush();
		
	}
	
	@RequestMapping("/deleteMongoDateCheck.do")
	public void deleteMongoDateCheck(HttpServletRequest request, HttpServletResponse response) throws Exception{
		String endStr = request.getParameter("endDateStr");
		try{	
			String format = "yyyy-MM-dd";
			String strNow = DateUtil.getCurrentDate(format);
			String compareDate = Integer.toString(DateUtil.getCompareDate(strNow, endStr, format));
			
			Writer writer = response.getWriter();
			writer.write(compareDate);
		}catch(Exception e){
			log.error(e, e);
		}
	}
	
	public Query deleteLogQuery(){
		Query query = new Query();
		query.addCriteria(Criteria.where("regtime").gte(StatusInMemory.getStartDate()).lte(StatusInMemory.getEndDate()));
		return query;
	}
}


