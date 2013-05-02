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
package net.cit.tetrad.resource;

import static net.cit.tetrad.common.ColumnConstent.GRAPH_SUFFIX_DAY;
import static net.cit.tetrad.common.ColumnConstent.GRAPH_SUFFIX_MONTH;
import static net.cit.tetrad.common.ColumnConstent.GRAPH_SUFFIX_WEEK;
import static net.cit.tetrad.common.ColumnConstent.IMGTOTALFIELD2_2;
import static net.cit.tetrad.common.ColumnConstent.MONGOVER2_2;
import static net.cit.tetrad.common.ColumnConstent.RRD_SUFFIXPATH;
import static net.cit.tetrad.common.ColumnConstent.SUB_GRAPH_WIDTH;
import static net.cit.tetrad.common.ColumnConstent.SUB_GRAPH_HEIGHT;
import static net.cit.tetrad.common.ColumnConstent.PROCESS_MONGOD;
import static net.cit.tetrad.common.ColumnConstent.IMGTOTALFIELD2_0;
import static net.cit.tetrad.common.ColumnConstent.SERVERSTATUS_TRUE;
import static net.cit.tetrad.common.ColumnConstent.SUB_SMALL_GRAPH_HEIGHT;
import static net.cit.tetrad.common.ColumnConstent.SUB_SMALL_GRAPH_WIDTH;
import static net.cit.tetrad.common.ColumnConstent.TOTALFILEARR;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import net.cit.tetrad.common.DateUtil;
import net.cit.tetrad.common.Utility;
import net.cit.tetrad.model.CommonDto;
import net.cit.tetrad.model.Device;
import net.cit.tetrad.rrd.bean.GraphDefInfo;
import net.cit.tetrad.rrd.bean.ServerStatus;
import net.cit.tetrad.utility.StringUtils;
import net.cit.tetrad.utility.code.Code;

import org.apache.log4j.Logger;
import org.rrd4j.core.Util;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

@Controller
public class TotalGraphResource extends DefaultResource{
	private Logger log = Logger.getLogger(this.getClass());

	private final int PERIOD_DAY = -1;
	private final int PERIOD_WEEK = -7;
	private final int PERIOD_MONTH = -1;
	private final String SUBNAME = "_detail_";
	
	@RequestMapping("/totalDetailedGraph.do")
	public ModelAndView totalDetailedGraph(CommonDto dto) throws Exception{
		log.debug("start - totalDetailedGraph()");		
		ModelAndView mav = commMav();
		
		String fileName = "";
		String graphGubun = dto.getGraph_period();
		if(graphGubun == null || graphGubun.equals("")){
			graphGubun = mainDao.graphGubun();
			dto.setSortItem("big");
			dto.setGraph_period(String.valueOf(graphGubun));
			dto.setConsolFun("LAST");
			dto.setGraph_step("sec");
			dto.setDstype("acc");
		}

		long stepLong = subDao.setStep(dto.getGraph_step());
		if(dto.getDsname().equals("totalDbDataSize")){
			fileName = totalDbGraph(dto,0);
			mav.addObject("gubun", "DB");
		}else if(dto.getDsname().equals("totalDbIndexSize")){
			fileName = totalDbGraph(dto,1);
			mav.addObject("gubun", "DB");
		}else if(dto.getDsname().equals("totalGlobalLock")){
			if(isMongoVer2_2){
				fileName = totalFieldGraph(graphGubun,dto.getConsolFun(),stepLong);
			}else{
				fileName = totalDbGraph(dto,2);
			}
			mav.addObject("gubun", "all");
		}else if(dto.getDsname().equals("totalPageFault")){
			fileName = totalPageFaultGraph(graphGubun,dto.getConsolFun(),stepLong);
			mav.addObject("gubun", "all");
		}
		mav.addObject("file", fileName);
		mav.addObject("comm", dto);
		mav.setViewName("totalGraph");
		
		log.debug("end - totalDetailedGraph()");
		return mav;
	}
	
	public String totalFieldGraph(String grphPeriod, String consolFun, long step) throws ParseException{
		String fileName = "";
		
		GraphDefInfo pageGraphDay = null;
		if(grphPeriod.equals("1")){
			pageGraphDay = createTotalGraphAttribute(DateUtil.plusDay(0), DateUtil.plusDay(PERIOD_DAY));		
		}else if(grphPeriod.equals("2")){
			pageGraphDay = createTotalGraphAttribute(DateUtil.plusDay(0), DateUtil.plusDay(PERIOD_WEEK));		
		}else if(grphPeriod.equals("3")){
			pageGraphDay = createTotalGraphAttribute(DateUtil.plusDay(0), DateUtil.plusMonth(PERIOD_MONTH));		
		}
		pageGraphDay.setFileName("totalLocks");
		pageGraphDay.setFilters(TOTALFILEARR);
		mainDao.setGraphDefInfo(pageGraphDay, grphPeriod, TOTALFILEARR, consolFun, step);
		fileName = rrdService.totalMultiGraphPerRrd(pageGraphDay);
		return fileName;
	}
	
	public String totalDbGraph(CommonDto dto, int num) throws ParseException{
		String fileName = "";
		dto.setSearch_gubun(IMGTOTALFIELD2_0[num]);
		GraphDefInfo gdInfo = null;
		String grphPeriod = dto.getGraph_period();
		Date stDate = DateUtil.plusDay(PERIOD_DAY);
		dto.setEdate(DateUtil.getCurrentDate2("yyyy-MM-dd HH:mm"));
		if(grphPeriod.equals("2")){
			stDate = DateUtil.plusDay(PERIOD_WEEK);
		}else if(grphPeriod.equals("3")){
			stDate = DateUtil.plusMonth(PERIOD_MONTH);
		}
		String strStDate = DateUtil.getCurrentDate(stDate, "yyyy-MM-dd HH:mm");
		dto.setSdate(strStDate);
		gdInfo = subDao.getDbGraphDefInfoForSubGraph(dto);
		fileName = rrdService.detailedGraphPerRrdDb(gdInfo);
		return fileName;
	}
	
	public String totalPageFaultGraph(String grphPeriod, String consolFun, long step) throws ParseException{
		String rrdDb = "extra_info_page_faults";
		String fileName = "";
		String title = "Page Fault";
		
		ArrayList<String> arr = new ArrayList<String>();
		List<Device> deviceGroup = getTotalMongodGroup();
		for (Device device : deviceGroup) {
			String deviceName = Code.device.getCode().getName(device.getIdx());
			String legend = deviceName + " Page Fault";
			arr.add(legend);
		}
		String[] graphLegend = (String[]) arr.toArray(new String[arr.size()]);
		
		GraphDefInfo pageGraphDay = null;
		if(grphPeriod.equals("1")){
			pageGraphDay = createTotalGraphAttribute(DateUtil.plusDay(0), DateUtil.plusDay(PERIOD_DAY));		
		}else if(grphPeriod.equals("2")){
			pageGraphDay = createTotalGraphAttribute(DateUtil.plusDay(0), DateUtil.plusDay(PERIOD_WEEK));
		}else if(grphPeriod.equals("3")){
			pageGraphDay = createTotalGraphAttribute(DateUtil.plusDay(0), DateUtil.plusMonth(PERIOD_MONTH));
		}		
		pageGraphDay.setDeviceGroup(deviceGroup);
		pageGraphDay.setFileName(SUBNAME + rrdDb );
		pageGraphDay = mainDao.setGraphDefInfo(pageGraphDay, grphPeriod, graphLegend, consolFun, step);
		fileName = rrdService.detailedMultiDeviceGraphPerRrd("extra_info_page_faults", pageGraphDay, title);
		return fileName;
	}
	
	private GraphDefInfo createTotalGraphAttribute(Date endDate, Date startDate) {
		GraphDefInfo graph = new GraphDefInfo();
		graph.setEndTime(Util.getTimestamp(endDate));
		graph.setStartTime(Util.getTimestamp(startDate));
//		graph.setEndTime(CommonUtils.getTimeForGraph(endDate));
//		graph.setStartTime(CommonUtils.getTimeForGraph(startDate));
		
		graph.setWidth(SUB_GRAPH_WIDTH);
		graph.setHeight(SUB_GRAPH_HEIGHT);
		
		return graph;
	}
	
	private List<Device> getTotalMongodGroup() {
		List<Device> deviceGroup = new ArrayList<Device>();
		List<ServerStatus> serverStatusList = daoForMongo.readServerStatus(PROCESS_MONGOD);		
		for (ServerStatus serverStatus : serverStatusList) {
			Device device = new Device();
			device.setIdx(serverStatus.getDeviceCode());
			
			deviceGroup.add(device);
		}
		return deviceGroup;
	}
}
