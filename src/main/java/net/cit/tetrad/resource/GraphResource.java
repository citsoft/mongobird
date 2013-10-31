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

import java.io.Writer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import static net.cit.tetrad.common.ColumnConstent.PATH_GRAPH;
import static net.cit.tetrad.common.ColumnConstent.PATH_LOCKGRAPH;
import static net.cit.tetrad.common.ColumnConstent.PATH_DBGRAPH;
import static net.cit.tetrad.common.ColumnConstent.DEVICE_TYPE;
import static net.cit.tetrad.common.ColumnConstent.DEVICECODE;
import static net.cit.tetrad.common.ColumnConstent.DEVICE_GROUPCODE;
import static net.cit.tetrad.common.ColumnConstent.DBLOCKFILEARR;
import static net.cit.tetrad.common.ColumnConstent.MYSTATE;
import static net.cit.tetrad.utility.QueryUtils.setIdx;
import net.cit.tetrad.common.ColumnConstent;
import net.cit.tetrad.common.DateUtil;
import net.cit.tetrad.common.Utility;
import net.cit.tetrad.model.CommonDto;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.model.Group;
import net.cit.tetrad.model.PersonJson;
import net.cit.tetrad.model.Type;
import net.cit.tetrad.rrd.bean.DbStatus;
import net.cit.tetrad.rrd.bean.GraphDefInfo;
import net.cit.tetrad.rrd.bean.ServerStatus;
import net.cit.tetrad.utility.StringUtils;
import net.sf.json.JSONObject;

import org.apache.log4j.Logger;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class GraphResource extends DefaultResource {

	private Logger log = Logger.getLogger(this.getClass());
	private List<Integer> masterLst = new ArrayList<Integer>();
	private List<Integer> slaveLst = new ArrayList<Integer>();

	// private final String[] DSNAME =
	// {"p_fault","global","r_mem","v_mem","m_mem","conn","op_insert","op_query","op_update","op_delete","in_network","out_network"};

	@RequestMapping("/dsNameGraphView.do")
	public ModelAndView dsNameGraphView(CommonDto dto) throws Exception {
		log.debug("start - dsNameGraphView()");
		ModelAndView mav = commMav();

		List<Object> deviceLst = readDeviceList();
		ArrayList<String> dsLst = new ArrayList<String>(Arrays.asList(DSNAME));
		mav.addObject("dsNameLst", dsLst);
		mav.addObject("deviceLst", deviceLst);

		if (Utility.isNull(dto.getGraph_period()).equals("")) {
			dto.setDsname(DSNAME[0]);
			if (deviceLst.size() != 0) {
				setDeviceLst(dto, deviceLst);
				setStateLst(deviceLst);
			}

			dto.setType_gubun("dsName");
			dto.setGraph_period("1h");
		} else {
			if (deviceLst.size() != 0) {
				ArrayList<Integer> arr = new ArrayList<Integer>();
				for (int getDevice : dto.getDeviceLst()) {
					arr.add(getDevice);
				}
				dto.setIntegerLst(arr);
			}
		}
		dateNull(dto);
		mav.addObject("comm", dto);

		mav.setViewName(PATH_GRAPH + "dsNameGraph");
		log.debug("end - dsNameGraphView()");
		return mav;
	}

	/**
	 * master/slave 리스트 취득
	 * 
	 * @param deviceLst
	 * @throws Exception
	 */
	public void setStateLst(List<Object> deviceLst) throws Exception {
		masterLst = new ArrayList<Integer>();
		slaveLst = new ArrayList<Integer>();
		Map<String, Object> fromMongo = new HashMap<String, Object>();
		try {
			boolean isExistDevice = true;
			if (deviceLst == null)
				isExistDevice = false;
			if (isExistDevice) {
				for (Object deviceObj : deviceLst) {
					Device device = (Device) deviceObj;
					int idx = device.getIdx();
					fromMongo = comandService.insertCommand(idx, MYSTATE);
					if (fromMongo.get("myState") != null) {
						int myst = (Integer) fromMongo.get("myState");
						if (myst == 1)
							masterLst.add(idx);
						if (myst == 2)
							slaveLst.add(idx);
					}
				}
			}
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	@RequestMapping("/getStateLst.do")
	public void getStateLst(HttpServletRequest request,
			HttpServletResponse response) throws Exception {
		String checkState = request.getParameter("checkState");
		try {
			PersonJson result = new PersonJson();
			if (checkState.equals("master")) {
				result.setAaData(masterLst);
			} else if (checkState.equals("slave")) {
				result.setAaData(slaveLst);
			}
			JSONObject jsonObject = JSONObject.fromObject(result);

			Writer writer = setResponse(response).getWriter();
			writer.write(jsonObject.toString());
			writer.flush();
		} catch (Exception e) {
			log.error(e, e);
		}
	}

	@RequestMapping("/groupGraphView.do")
	public ModelAndView groupGraphView(CommonDto dto) throws Exception {
		log.debug("start - groupGraphView()");
		ModelAndView mav = commMav();

		Query query = new Query();
		List<Object> groupLst = monadService.getList(query, Group.class);
		mav.addObject("groupLst", groupLst);

		if (Utility.isNull(dto.getGraph_period()).equals("")) {
			Group group = new Group();
			if (groupLst.size() != 0) {
				group = (Group) groupLst.get(0);
				int grpIdx = group.getIdx();
				dto.setGroupCodes(grpIdx + "");
			}
			dto.setType_gubun("group");
			dto.setGraph_period("1h");
		}

		dateNull(dto);
		mav.addObject("comm", dto);

		mav.setViewName(PATH_GRAPH + "groupGraph");

		List<Object> typeLst = adminDao.typeList();
		List<Type> typeModelLst = new ArrayList<Type>();

		for (Object type : typeLst) {
			Query qry = new Query(Criteria.where("type").is(
					StringUtils.getEncStr((String) type)));
			List<Object> deviceLst = monadService.getList(qry, Device.class);
			String groups = "";

			for (Object device : deviceLst) {
				groups += ((Device) device).getGroupCode() + ",";
			}
			typeModelLst.add(new Type((String) type, groups));
		}

		mav.addObject("typeModelLst", typeModelLst);

		log.debug("end - groupGraphView()");
		return mav;
	}

	@RequestMapping("/typeGraphView.do")
	public ModelAndView typeGraphView(CommonDto dto) throws Exception {
		log.debug("start - typeGraphView()");
		ModelAndView mav = commMav();

		List<Object> typeLst = adminDao.typeList();
		mav.addObject("typeLst", typeLst);

		if (Utility.isNull(dto.getGraph_period()).equals("")) {
			dto.setType((String) typeLst.get(1));

			dto.setType_gubun("type");
			dto.setGraph_period("1h");
		}

		dateNull(dto);
		mav.addObject("comm", dto);

		mav.setViewName(PATH_GRAPH + "typeGraph");
		log.debug("end - typeGraphView()");
		return mav;
	}

	@RequestMapping("/deviceGraphView.do")
	public ModelAndView deviceGraphView(CommonDto dto) throws Exception {
		log.debug("start - deviceGraphView()");
		ModelAndView mav = commMav();

		List<Object> deviceLst = readDeviceList();
		mav.addObject("deviceLst", deviceLst);

		if (Utility.isNull(dto.getGraph_period()).equals("")) {
			Device device = new Device();
			if (deviceLst.size() != 0) {
				device = (Device) deviceLst.get(0);
				dto.setDeviceCode(device.getIdx());
			}
			dto.setType_gubun("device");
			dto.setGraph_period("1h");
		}

		dateNull(dto);
		mav.addObject("comm", dto);

		mav.setViewName(PATH_GRAPH + "deviceGraph");
		log.debug("end - deviceGraphView()");
		return mav;
	}

	@RequestMapping("/multiGraphView.do")
	public ModelAndView multiGraphView(CommonDto dto) throws Exception {
		log.debug("start - multiGraphView()");
		ModelAndView mav = commMav();

		List<Object> deviceLst = readDeviceList();
		ArrayList<String> dsLst = new ArrayList<String>(Arrays.asList(DSNAME));
		mav.addObject("dsNameLst", dsLst);
		mav.addObject("deviceLst", deviceLst);

		if (Utility.isNull(dto.getGraph_period()).equals("")) {
			Device device = new Device();
			if (deviceLst.size() != 0) {
				device = (Device) deviceLst.get(0);
				dto.setDeviceCode(device.getIdx());
			}

			dto.setDsNameLst(DSNAME);
			dsLst = new ArrayList<String>(Arrays.asList(dto.getDsNameLst()));
			dto.setStringLst(dsLst);

			dto.setType_gubun("multi");
			dto.setGraph_period("1h");
		} else {
			if (dto.getDsNameLst().length != 0) {
				ArrayList<String> arr = new ArrayList<String>();
				for (String getdsName : dto.getDsNameLst()) {
					arr.add(getdsName);
				}
				dto.setStringLst(arr);
			}
		}
		dateNull(dto);
		mav.addObject("comm", dto);

		mav.setViewName(PATH_GRAPH + "multiGraph");
		log.debug("end - multiGraphView()");
		return mav;
	}

	@RequestMapping("/eventGraphView.do")
	public ModelAndView eventGraphView(CommonDto dto) throws Exception {
		log.debug("start - eventGraphView()");
		ModelAndView mav = commMav();

		if (StringUtils.isNull(dto.getConsolFun()))
			dto.setConsolFun("LAST");
		if (StringUtils.isNull(dto.getGraph_step()))
			dto.setGraph_step("sec");
		mav.addObject("comm", dto);

		mav.setViewName(PATH_GRAPH + "eventGraph");
		log.debug("end - eventGraphView()");
		return mav;
	}

	@RequestMapping("/pieGraphView.do")
	public ModelAndView pieGraphView(CommonDto dto) throws Exception {
		log.debug("start - pieGraphView()");
		ModelAndView mav = commMav();
		mav.addObject("comm", dto);

		mav.setViewName(PATH_GRAPH + "pieGraph");
		log.debug("end - pieGraphView()");
		return mav;
	}

	/**
	 * 서브 팝업 그래프 페이지 보기
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/popupGraphView.do")
	public ModelAndView popupGraphView(CommonDto dto) throws Exception {
		log.debug("start - popupGraphView()");
		ModelAndView mav = commMav();

		dateNull(dto);
		mav.addObject("comm", dto);
		mav.setViewName("sub_graph");

		log.debug("end - popupGraphView()");
		return mav;
	}

	/**
	 * 장비 별 락 그래프
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/deviceLockGraphView.do")
	public ModelAndView deviceLockGraphView(CommonDto dto) throws Exception {
		log.debug("start - deviceLockGraphView()");
		ModelAndView mav = commMav();

		List<Object> deviceLst = readDeviceList();
		mav.addObject("deviceLst", deviceLst);

		if (Utility.isNull(dto.getGraph_period()).equals("")) {
			Device device = new Device();
			if (deviceLst.size() != 0) {
				device = (Device) deviceLst.get(0);
				dto.setDeviceCode(device.getIdx());
			}
			dto.setType_gubun("deviceLock");
			dto.setGraph_period("1h");
		}

		dateNull(dto);
		mav.addObject("comm", dto);

		mav.setViewName(PATH_LOCKGRAPH + "deviceLockGraph");
		log.debug("end - deviceLockGraphView()");
		return mav;
	}

	/**
	 * 유형별 락 그래프
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/typeLockGraphView.do")
	public ModelAndView typeLockGraphView(CommonDto dto) throws Exception {
		log.debug("start - typeLockGraphView()");
		ModelAndView mav = commMav();

		List<Object> typeLst = adminDao.typeList();
		mav.addObject("typeLst", typeLst);

		if (Utility.isNull(dto.getGraph_period()).equals("")) {
			dto.setType((String) typeLst.get(1));

			dto.setType_gubun("typeLock");
			dto.setGraph_period("1h");
		}

		dateNull(dto);
		mav.addObject("comm", dto);

		mav.setViewName(PATH_LOCKGRAPH + "typeLockGraph");
		log.debug("end - typeLockGraphView()");
		return mav;
	}

	/**
	 * 그룹별 락 그래프
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/groupLockGraphView.do")
	public ModelAndView groupLockGraphView(CommonDto dto) throws Exception {
		log.debug("start - groupLockGraphView()");
		ModelAndView mav = commMav();

		Query query = new Query();
		List<Object> groupLst = monadService.getList(query, Group.class);
		mav.addObject("groupLst", groupLst);

		if (Utility.isNull(dto.getGraph_period()).equals("")) {
			Group group = new Group();
			if (groupLst.size() != 0) {
				group = (Group) groupLst.get(0);
				int grpIdx = group.getIdx();
				dto.setGroupCodes(grpIdx + "");
			}
			dto.setType_gubun("groupLock");
			dto.setGraph_period("1h");
		}

		dateNull(dto);
		mav.addObject("comm", dto);

		mav.setViewName(PATH_LOCKGRAPH + "groupLockGraph");

		List<Object> typeLst = adminDao.typeList();
		List<Type> typeModelLst = new ArrayList<Type>();

		for (Object type : typeLst) {
			Query qry = new Query(Criteria.where("type").is(
					StringUtils.getEncStr((String) type)));
			List<Object> deviceLst = monadService.getList(qry, Device.class);
			String groups = "";

			for (Object device : deviceLst) {
				groups += ((Device) device).getGroupCode() + ",";
			}
			typeModelLst.add(new Type((String) type, groups));
		}

		mav.addObject("typeModelLst", typeModelLst);

		log.debug("end - groupLockGraphView()");
		return mav;
	}

	/**
	 * DB별 락 그래프
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/dbLockGraphView.do")
	public ModelAndView dbLockGraphView(CommonDto dto) throws Exception {
		log.debug("start - dbLockGraphView()");
		ModelAndView mav = commMav();

		List<String> dbLst = mkDbLst(new Query());

		mav.addObject("dbLst", dbLst);
		List<Object> deviceLst = readDeviceList();
		mav.addObject("deviceLst", deviceLst);

		if (Utility.isNull(dto.getGraph_period()).equals("")) {
			if (deviceLst.size() != 0) {
				setDeviceLst(dto, deviceLst);
				setStateLst(deviceLst);
			}
			if (dbLst.size() != 0)
				dto.setDbname(dbLst.get(0));

			dto.setType_gubun("dbLock");
			dto.setGraph_period("1h");
		} else {
			if (deviceLst.size() != 0) {
				ArrayList<Integer> arr = new ArrayList<Integer>();
				for (int getDevice : dto.getDeviceLst()) {
					arr.add(getDevice);
				}
				dto.setIntegerLst(arr);
			}
		}
		dateNull(dto);
		mav.addObject("comm", dto);

		mav.setViewName(PATH_LOCKGRAPH + "dbLockGraph");
		log.debug("end - dbLockGraphView()");
		return mav;
	}

	/**
	 * DB 전체 그래프
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/allDbGraphView.do")
	public ModelAndView allDbGraphView(CommonDto dto) throws Exception {
		log.debug("start - allDbGraphView()");
		ModelAndView mav = commMav();

		if (Utility.isNull(dto.getGraph_period()).equals("")) {
			dto.setType_gubun("allDb");
			dto.setGraph_period("1h");
			dto.setSearch_gubun("totalDbDataSize");
			dto.setDstype("cur");
		}

		dateNull(dto);
		mav.addObject("comm", dto);

		mav.setViewName(PATH_DBGRAPH + "allDbGraph");
		log.debug("end - allDbGraphView()");
		return mav;
	}

	/**
	 * 장비별 DB 그래프
	 * 
	 * @param dto
	 * @return
	 * @throws Exception
	 */
	@RequestMapping("/deviceDbGraphView.do")
	public ModelAndView deviceDbGraphView(CommonDto dto) throws Exception {
		log.debug("start - deviceDbGraphView()");
		ModelAndView mav = commMav();

		List<Object> deviceLst = readDeviceList();
		mav.addObject("deviceLst", deviceLst);

		if (Utility.isNull(dto.getGraph_period()).equals("")) {
			Device device = new Device();
			if (deviceLst.size() != 0) {
				device = (Device) deviceLst.get(0);
				dto.setDeviceCode(device.getIdx());
			}

			dto.setType_gubun("deviceDb");
			dto.setGraph_period("1h");
			dto.setDstype("cur");
		}

		dateNull(dto);
		mav.addObject("comm", dto);

		mav.setViewName(PATH_DBGRAPH + "deviceDbGraph");
		log.debug("end - deviceDbGraphView()");
		return mav;
	}

	public void setDeviceLst(CommonDto dto, List<Object> deviceLst) {
		ArrayList<Integer> arr = new ArrayList<Integer>();
		for (Object getDevice : deviceLst) {
			Device device = new Device();
			device = (Device) getDevice;
			arr.add(device.getIdx());
		}
		dto.setIntegerLst(arr);

		int[] ints = new int[arr.size()];
		int i = 0;
		for (Integer n : arr) {
			ints[i++] = n;
		}
		dto.setDeviceLst(ints);
	}

	public void setDsName(CommonDto dto) {
		String[] strs = { dto.getDsname() };
		dto.setDsNameLst(strs);
	}

	public void setGroupCode(CommonDto dto) {
		dto.setDsNameLst(DSNAME);

		String[] groups = dto.getGroupCodes().split(",");
		List<Integer> list = new ArrayList<Integer>();

		for (String group : groups) {
			list.add(new Integer(group));
		}

		Query query = new Query(Criteria.where("groupCode").in(list));
		List<Object> deviceLst = monadService.getList(query, Device.class);
		setDeviceLst(dto, deviceLst);
	}

	public void setType(CommonDto dto) {
		dto.setDsNameLst(DSNAME);

		Query query = new Query(Criteria.where("type").is(
				StringUtils.getEncStr(dto.getType())));
		List<Object> deviceLst = monadService.getList(query, Device.class);
		setDeviceLst(dto, deviceLst);
	}

	public void setDevice(CommonDto dto) {
		dto.setDsNameLst(DSNAME);
		setMulti(dto);
	}

	public void setMulti(CommonDto dto) {
		Query query = new Query(Criteria.where("idx").is(dto.getDeviceCode()));
		List<Object> deviceLst = monadService.getList(query, Device.class);
		setDeviceLst(dto, deviceLst);
	}

	public void dateNull(CommonDto dto) {
		if (Utility.isNull(dto.getSdate()).equals("")) {
			Date stDate = DateUtil.plusHour(-1);
			Date edDate = new Date();

			String stFullDate = DateUtil.getCurrentDate(stDate,
					"yyyy-MM-dd HH:mm");
			String edFullDate = DateUtil.getCurrentDate(edDate,
					"yyyy-MM-dd HH:mm");
			dto.setSdate(stFullDate);
			dto.setEdate(edFullDate);

			dto.setSliderMin(stFullDate);
			dto.setSliderMax(edFullDate);

			dto.setConsolFun("LAST");
			dto.setGraph_step("sec");
		}
	}

	/**
	 * DB명 리스트 출력
	 * 
	 * @param mav
	 * @return
	 */
	@RequestMapping("/selectDbLst.do")
	public void dbLst(HttpServletRequest request, HttpServletResponse response) {
		String typeGubun = Utility.isNull(request.getParameter("type_gubun"));
		try {
			PersonJson result = new PersonJson();
			Query query = new Query();
			if (typeGubun.equals("deviceLock") || typeGubun.equals("event")) {
				int deviceCode = (Integer.parseInt(Utility.isNullNumber(request
						.getParameter("deviceCode"))));
				if (deviceCode != 0)
					query.addCriteria(Criteria.where(DEVICECODE).is(deviceCode));
			} else if (typeGubun.equals("typeLock")) {
				String type = Utility.isNull(request.getParameter("type"));
				if (!type.equals(""))
					query.addCriteria(Criteria.where(DEVICE_TYPE).is(type));
			} else if (typeGubun.equals("groupLock")) {
				String[] groups = request.getParameter("groupCode").split(",");
				List<Integer> list = new ArrayList<Integer>();
				for (String group : groups) {
					list.add(new Integer(group));
				}
				if (groups.length != 0)
					query.addCriteria(Criteria.where(DEVICE_GROUPCODE).in(list));

			}

			List<String> dbLst = mkDbLst(query);

			result.setsEcho(Integer.parseInt(Utility.isNullNumber(request
					.getParameter("sEcho"))));
			result.setAaData(dbLst);

			JSONObject jsonObject = JSONObject.fromObject(result);

			Writer writer = setResponse(response).getWriter();
			writer.write(jsonObject.toString());

			log.debug(jsonObject.toString());
			writer.flush();

		} catch (Exception e) {
			log.error(e, e);
		}
	}

	public List<String> mkDbLst(Query query) {
		List<String> dbLst = new ArrayList<String>();
		List<Object> serverStatusLst = monadService.getListWithStrCollName(
				query, ServerStatus.class, ColumnConstent.COLL_DASHBOARD);
		boolean isExistDB = true;
		if (serverStatusLst == null)
			isExistDB = false;
		if (isExistDB) {
			for (Object obj : serverStatusLst) {
				ServerStatus serverStatus = (ServerStatus) obj;
				for (DbStatus dbStatus : serverStatus.getDbInfos()) {
					String getDBName = dbStatus.getDb();
					boolean isExistDb = true;
					for (String dbStr : dbLst) {
						if (dbStr.equals(getDBName))
							isExistDb = false;
					}
					if (isExistDb)
						dbLst.add(getDBName);
				}
			}
		}
		return dbLst;
	}

	@RequestMapping("/dbGraph.do")
	public void dbGraph(HttpServletRequest request,
			HttpServletResponse response, CommonDto dto) {
		log.debug("start - dbGraph");
		try {
			PersonJson result = new PersonJson();
			GraphDefInfo gdInfo = null;

			List<String> fileName = new ArrayList<String>();
			gdInfo = subDao.getDbGraphDefInfoForSubGraph(dto);
			fileName.add(rrdService.detailedGraphPerRrdDb(gdInfo));

			result.setsEcho(Integer.parseInt(Utility.isNullNumber(request
					.getParameter("sEcho"))));
			result.setAaData(fileName);

			JSONObject jsonObject = JSONObject.fromObject(result);

			Writer writer = setResponse(response).getWriter();
			writer.write(jsonObject.toString());

			log.debug(jsonObject.toString());
			writer.flush();

		} catch (Exception e) {
			log.error(e, e);
		}
		log.debug("end - dbGraph");
	}

	@RequestMapping("/lockGraph.do")
	public void lockGraph(HttpServletRequest request,
			HttpServletResponse response, CommonDto dto) {
		log.debug("start - lockGraph.do");
		try {
			PersonJson result = new PersonJson();

			String type_gubun = dto.getType_gubun();
			if (type_gubun.equals("deviceLock") || type_gubun.equals("device")
					|| type_gubun.equals("event")) {
				setMulti(dto);
			} else if (type_gubun.equals("typeLock")) {
				setType(dto);
			} else if (type_gubun.equals("groupLock")) {
				setGroupCode(dto);
			} else if (type_gubun.equals("dbLock")) {
				dto.setSortItem("big");
				String[] strs = { dto.getDbname() };
				dto.setDbNameLst(strs);
			}

			List<String> fileName = new ArrayList<String>();
			boolean isExistDeviceLst = dto.getDeviceLst().length != 0;
			boolean isExistDsNameLst = dto.getDbNameLst().length != 0;
			if (isExistDeviceLst && isExistDsNameLst)
				fileName = setLockGraphInfo(dto);

			result.setsEcho(Integer.parseInt(Utility.isNullNumber(request
					.getParameter("sEcho"))));
			result.setAaData(fileName);

			JSONObject jsonObject = JSONObject.fromObject(result);

			Writer writer = setResponse(response).getWriter();
			writer.write(jsonObject.toString());

			log.debug(jsonObject.toString());
			writer.flush();

		} catch (Exception e) {
			log.error(e, e);
		}
		log.debug("end - lockGraph");
	}

	@RequestMapping("/soloLockGraph.do")
	public void soloLockGraph(HttpServletRequest request,
			HttpServletResponse response, CommonDto dto) {
		log.debug("start - soloLockGraph");
		try {
			PersonJson result = new PersonJson();

			List<String> fileName = new ArrayList<String>();
			fileName = setSoloLockGraphInfo(dto);

			result.setsEcho(Integer.parseInt(Utility.isNullNumber(request
					.getParameter("sEcho"))));
			result.setAaData(fileName);

			JSONObject jsonObject = JSONObject.fromObject(result);

			Writer writer = setResponse(response).getWriter();
			writer.write(jsonObject.toString());

			log.debug(jsonObject.toString());
			writer.flush();

		} catch (Exception e) {
			log.error(e, e);
		}
		log.debug("end - soloLockGraph");
	}

	@RequestMapping("/lockSumGraph.do")
	public void lockSumGraph(HttpServletRequest request,
			HttpServletResponse response, CommonDto dto) {
		log.debug("start - lockSumGraph");
		try {
			PersonJson result = new PersonJson();

			List<String> fileName = new ArrayList<String>();
			String[] dsNameArr = { "totalDbLocksTimeLockedMicros_w_sum",
					"totalDbLocksTimeLockedMicros_r_sum" };
			for (String dsName : dsNameArr) {
				String[] filters = { dsName };
				GraphDefInfo graphInfo = subDao
						.getLockGraphDefInfoForSubGraph(dto);
				graphInfo.setFileName(dsName);
				graphInfo.setFilters(filters);
				graphInfo.setGraphLegend(filters);
				fileName.add(rrdService.totalMultiGraphPerRrd(graphInfo));
			}

			result.setsEcho(Integer.parseInt(Utility.isNullNumber(request
					.getParameter("sEcho"))));
			result.setAaData(fileName);

			JSONObject jsonObject = JSONObject.fromObject(result);

			Writer writer = setResponse(response).getWriter();
			writer.write(jsonObject.toString());

			log.debug(jsonObject.toString());
			writer.flush();

		} catch (Exception e) {
			log.error(e, e);
		}
		log.debug("end - lockSumGraph");
	}

	@RequestMapping("/pieGraphData.do")
	public void pieGraphData(HttpServletRequest request,
			HttpServletResponse response) {
		int deviceCode = Integer.parseInt(request.getParameter("deviceCode"));
		Query query = new Query(Criteria.where("deviceCode").is(deviceCode));
		ServerStatus serverStatus = (ServerStatus) monadService.getFindOne(
				query, ServerStatus.class, ColumnConstent.COLL_DASHBOARD);

		try {
			PersonJson result = new PersonJson();

			List<Object> arrTotInfo = new ArrayList<Object>();
			String[] lineColor = { "#d3ee36", "#a7ee70", "#58dccd", "#36abee",
					"#476cee", "#a244ea", "#e33fc7", "#ee4339", "#FF7F50",
					"#eed236" };
			int colorCnt = 0;
			for (DbStatus dbStatus : serverStatus.getDbInfos()) {
				List<Object> arrInfo = new ArrayList<Object>();
				Map hm = new HashMap();
				hm.put("label", dbStatus.getDb());
				hm.put("data", dbStatus.getDataSize());
				hm.put("color", lineColor[colorCnt % 10]);
				arrTotInfo.add(hm);
				colorCnt++;
			}
			result.setAaData(arrTotInfo);
			JSONObject jsonObject = JSONObject.fromObject(result);

			response.setContentType("text/html;charset=utf-8");
			response.setCharacterEncoding("UTF-8");

			response.setContentType("text/html");
			response.setHeader("Cache-Control", "no-cache");

			Writer writer = response.getWriter();
			writer.write(jsonObject.toString());

			writer.flush();

		} catch (Exception e) {
			log.error(e, e);
		}
	}

	@RequestMapping("/subGraph.do")
	public void subGraph(HttpServletRequest request,
			HttpServletResponse response, CommonDto dto) {
		log.debug("start - subGraph.do");
		String gubun = Utility.isNull(request.getParameter("gubun"));
		
		if (!gubun.equals("")) {
			String typeGubunStr = Utility.isNull(request
					.getParameter("type_gubun"));
			String dsName = Utility.isNull(request.getParameter("dsNameLst"));
			dto.setType_gubun(typeGubunStr);
			String[] dsNameLst = { dsName };
			dto.setDsNameLst(dsNameLst);
		}
		try {
			PersonJson result = new PersonJson();

			String type_gubun = dto.getType_gubun();
			if (type_gubun.equals("dsName")) {
				setDsName(dto);
			} else if (type_gubun.equals("group")) {
				setGroupCode(dto);
			} else if (type_gubun.equals("type")) {
				setType(dto);
			} else if (type_gubun.equals("device")
					|| type_gubun.equals("event")) {
				setDevice(dto);
			} else if (type_gubun.equals("multi")
					|| type_gubun.equals("deviceDb")) {
				setMulti(dto);
			}

			List<String> fileName = new ArrayList<String>();
			boolean isExistDeviceLst = dto.getDeviceLst().length != 0;
			boolean isExistDsNameLst = dto.getDsNameLst().length != 0;
			if (isExistDeviceLst && isExistDsNameLst)
				fileName = setGraphInfo(dto);

			result.setsEcho(Integer.parseInt(Utility.isNullNumber(request
					.getParameter("sEcho"))));
			result.setAaData(fileName);

			JSONObject jsonObject = JSONObject.fromObject(result);
			
			Writer writer = setResponse(response).getWriter();
			writer.write(jsonObject.toString());

			log.debug(jsonObject.toString());
			writer.flush();

		} catch (Exception e) {
			log.error(e, e);
		}
		log.debug("end - subGraph.do");
	}

	@RequestMapping("/subDbGraph.do")
	public void subDbGraph(HttpServletRequest request,
			HttpServletResponse response, CommonDto dto) {
		log.debug("start - subDbGraph.do");
		try {
			PersonJson result = new PersonJson();

			List<String> fileName = setDbGraphInfo(dto);

			result.setsEcho(Integer.parseInt(Utility.isNullNumber(request
					.getParameter("sEcho"))));
			result.setAaData(fileName);

			JSONObject jsonObject = JSONObject.fromObject(result);

			Writer writer = setResponse(response).getWriter();
			writer.write(jsonObject.toString());

			log.debug(jsonObject.toString());
			writer.flush();

		} catch (Exception e) {
			log.error(e, e);
		}
		log.debug("end - subDbGraph.do");
	}

	@RequestMapping("/setDbLst.do")
	public void setDbLst(HttpServletRequest request,
			HttpServletResponse response) {
		int deviceCode = Integer.parseInt(request.getParameter("deviceCode"));
		try {
			PersonJson result = new PersonJson();
			List<DbStatus> dbStatus = subDao.dbLstNan(deviceCode);

			result.setsEcho(Integer.parseInt(Utility.isNullNumber(request
					.getParameter("sEcho"))));
			result.setAaData(dbStatus);

			JSONObject jsonObject = JSONObject.fromObject(result);

			Writer writer = setResponse(response).getWriter();
			writer.write(jsonObject.toString());

			log.debug(jsonObject.toString());
			writer.flush();

		} catch (Exception e) {
			log.error(e, e);
		}
	}

	public List<String> setDbGraphInfo(CommonDto dto) {
		List<String> fileName = new ArrayList<String>();

		Query query = setIdx(dto.getDeviceCode());
		Device ddto = (Device) monadService.getFind(query, Device.class);

		List<DbStatus> dbStatus = subDao.dbLstNan(dto.getDeviceCode());
		String gubun = dto.getSearch_gubun();
		for (DbStatus status : dbStatus) {
			String dbname = status.getDb();
			// dto.setDbname(dbname);
			GraphDefInfo graphInfo = subDao.getDbGraphDefInfoForSubGraph(dto);
			graphInfo.setDevice(ddto);
			graphInfo.setGubun(gubun);
			fileName.add(rrdService.graphTetradRrdDb(graphInfo, dbname,
					dto.getSortItem()));
		}
		return fileName;
	}

	public List<String> setGraphInfo(CommonDto dto) {
		List<String> fileName = new ArrayList<String>();
		int[] deviceLst = dto.getDeviceLst();
		List<Device> deviceGroup = getDeviceGroup(deviceLst);

		String[] dsNameStr = dto.getDsNameLst();
		List<String> dsNameLst = new ArrayList<String>();
		for (int i = 0; i < dsNameStr.length; i++) {
			if (dsNameStr[i].equals("global") && isMongoVer2_2) {
				dsNameLst.add("w_locks");
				dsNameLst.add("r_locks");
			} else {
				dsNameLst.add(dsNameStr[i]);
			}
		}
		for (String dsName : dsNameLst) {
			dto.setDsname(dsName);
			GraphDefInfo graphInfo = subDao
					.getNoDeviceGraphDefInfoForSubGraph(dto);
			graphInfo.setDeviceGroup(deviceGroup);
			fileName.add(rrdService.graphSubRrdDb(graphInfo, dto.getSortItem()));
		}
		return fileName;
	}

	public List<String> setLockGraphInfo(CommonDto dto) {
		List<String> fileName = new ArrayList<String>();
		int[] deviceLst = dto.getDeviceLst();
		List<Device> deviceGroup = getDeviceGroup(deviceLst);

		String[] dbNameLst = dto.getDbNameLst();
		String[] dsType = DBLOCKFILEARR;
		for (String dbName : dbNameLst) {
			for (int i = 0; i < dsType.length; i++) {
				dto.setDbname(dbName);
				GraphDefInfo graphInfo = subDao
						.getLockGraphDefInfoForSubGraph(dto);
				graphInfo.setFileName(dbName + "_" + dsType[i]);
				graphInfo.setGubun(dsType[i]);
				graphInfo.setDeviceGroup(deviceGroup);
				String fileNameStr = rrdService.lockGraphRrdDb(graphInfo,
						dto.getSortItem());
				if (!fileNameStr.equals(""))
					fileName.add(fileNameStr);
			}
		}
		return fileName;
	}

	public List<String> setSoloLockGraphInfo(CommonDto dto) {
		List<String> fileName = new ArrayList<String>();
		int[] deviceLst = dto.getDeviceLst();

		String[] dsType = DBLOCKFILEARR;
		for (int deviceCode : deviceLst) {
			Device device = new Device();
			device.setIdx(deviceCode);

			for (int i = 0; i < dsType.length; i++) {
				GraphDefInfo graphInfo = subDao
						.getLockGraphDefInfoForSubGraph(dto);
				graphInfo.setFileName(device + "_" + dto.getDbname() + "_"
						+ dsType[i]);
				graphInfo.setGubun(dsType[i]);
				graphInfo.setDevice(device);
				String filenm = rrdService.soloLockGraphRrdDb(graphInfo,
						dto.getSortItem());
				if (!filenm.equals(""))
					fileName.add(filenm);
			}
		}
		return fileName;
	}

	private List<Device> getDeviceGroup(int[] deviceLst) {
		List<Device> deviceGroup = new ArrayList<Device>();
		if (deviceLst == null) {
			List<ServerStatus> serverStatusList = daoForMongo
					.readServerStatus();
			for (ServerStatus serverStatus : serverStatusList) {
				Device device = new Device();
				device.setIdx(serverStatus.getDeviceCode());

				deviceGroup.add(device);
			}
		} else {
			for (int deviceCode : deviceLst) {
				Device device = new Device();
				device.setIdx(deviceCode);

				deviceGroup.add(device);
			}
		}
		return deviceGroup;
	}

	private List<Object> readDeviceList() {
		List<Object> deviceLst = monadService
				.getList(new Query(), Device.class);
		return deviceLst;
	}

	private HttpServletResponse setResponse(HttpServletResponse response) {
		response.setContentType("text/html;charset=utf-8");
		response.setCharacterEncoding("UTF-8");
		response.setContentType("text/html");
		response.setHeader("Cache-Control", "no-cache");

		return response;
	}
}
