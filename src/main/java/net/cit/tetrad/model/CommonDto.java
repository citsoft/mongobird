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
package net.cit.tetrad.model;

import java.util.List;

public class CommonDto {
	private List<Object> lst;
	private int[] idxLst;
	private String[] researchLst = new String[]{};
	private String groupSelect;
	private String groupText;
	private int start = 0;
	private int count = 0;
	private int totcnt = 0;
	private String sdate="";
	private String edate="";
	private String selectDate;
	private int selectHour;
	private int selectMin;
	private List<Integer> integerLst;
	private List<String> stringLst;
	private String[] dsNameLst = new String[]{};
	private String[] dbNameLst = new String[]{};
	private int[] deviceLst = new int[]{};
	private String[] myStateLst;
	private String allPeriod;
	private String consolFun;
	private String graph_step;
	private String sliderMin;
	private String sliderMax;
	private String autoRefresh;

	private String enlarge = "";
	private String batchMemorySize;	
	private String batchHddSize;
	
	private int shour=0;
	private int ehour=0;
	private int smin=0;
	private int emin=0;
	private String message;
	private int dival=0;
	private String uid;
	private int idx=0;
	private String reg_date;
	private String up_date;	
	private int loginUserCode;
	private int loginAuth;
	private String sort;
	private String sortItem;

	private String type;	
	private int groupCode;
	private int deviceCode;	
	private int userCode;
	private String type_gubun;
	
	//device
	private String ip;	
	private String port;	
	private int memorysize=0;	
	private int hddsize=0;
	private String authUser;
	private String authPasswd;
	
	//critical
	private int criticalvalue;	
	private int warningvalue;	
	private int infovalue;
	private String unit;
	
	//user
	private String passwd;
	private String username;
	private String email;
	private String mobileFirst;
	private String mobileSecond;
	private String mobileThird;
	private int authority=0;
	
	//global
	private int value=0;
	private int logKeepPeriodDay = 0;
	private int mainRefreshPeriodMinute = 0;
	private String mongo_version;
	private String hostname = "";
	
	//alarm
	private String cri_type;	
	private String memo	;
	private int figure=0;	
	private int cri_value=0;	
	private int confirm = 0;
	private String user	;
	private String reg_time	;
	private String up_time	;
	private int alarm;
	private String maingraph_period;
	private String graph_period;

	//search
	private String search_gubun;
	private String search_text;
	private String search_sdate;
	private String search_edate;
	
	private String dsname;
	private String dbname;
	private String graph_shour;
	private String graph_ehour;
	private String graph_smin;
	private String graph_emin;
	private String search_type;
	private String search_option;
	private String collname;
	private String dstype;
	private String title;
	private boolean isFinishedInitailRrd = false;

	public String getSelectDate() {
		return selectDate;
	}

	public void setSelectDate(String selectDate) {
		this.selectDate = selectDate;
	}

	public String getSortItem() {
		return sortItem;
	}

	public void setSortItem(String sortItem) {
		this.sortItem = sortItem;
	}
	
	public String getSort() {
		return sort;
	}

	public void setSort(String sort) {
		this.sort = sort;
	}
	
	public int getDival() {
		return dival;
	}

	public void setDival(int dival) {
		this.dival = dival;
	}

	public String getUid() {
		return uid;
	}

	public String getSearch_type() {
		return search_type;
	}

	public void setSearch_type(String search_type) {
		this.search_type = search_type;
	}

	public void setUid(String uid) {
		this.uid = uid;
	}

	public int getIdx() {
		return idx;
	}

	public void setIdx(int idx) {
		this.idx = idx;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	public int getGroupCode() {
		return groupCode;
	}
	public void setGroupCode(int groupCode) {
		this.groupCode = groupCode;
	}	
	public String getIp() {
		return ip;
	}

	public void setIp(String ip) {
		this.ip = ip;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public int getMemorysize() {
		return memorysize;
	}

	public void setMemorysize(int memorysize) {
		this.memorysize = memorysize;
	}

	public int getHddsize() {
		return hddsize;
	}

	public void setHddsize(int hddsize) {
		this.hddsize = hddsize;
	}
	
	public String getPasswd() {
		return passwd;
	}

	public void setPasswd(String passwd) {
		this.passwd = passwd;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public int getAuthority() {
		return authority;
	}

	public void setAuthority(int authority) {
		this.authority = authority;
	}

	public int getValue() {
		return value;
	}

	public void setValue(int value) {
		this.value = value;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getReg_date() {
		return reg_date;
	}

	public void setReg_date(String reg_date) {
		this.reg_date = reg_date;
	}

	public String getUp_date() {
		return up_date;
	}

	public void setUp_date(String up_date) {
		this.up_date = up_date;
	}
	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}

	public int getCount() {
		return count;
	}

	public void setCount(int count) {
		this.count = count;
	}

	public int getTotcnt() {
		return totcnt;
	}

	public void setTotcnt(int totcnt) {
		this.totcnt = totcnt;
	}

	public String getCri_type() {
		return cri_type;
	}

	public void setCri_type(String cri_type) {
		this.cri_type = cri_type;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getFigure() {
		return figure;
	}

	public void setFigure(int figure) {
		this.figure = figure;
	}

	public int getCri_value() {
		return cri_value;
	}

	public void setCri_value(int cri_value) {
		this.cri_value = cri_value;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public int getAlarm() {
		return alarm;
	}

	public void setAlarm(int alarm) {
		this.alarm = alarm;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public int getShour() {
		return shour;
	}

	public void setShour(int shour) {
		this.shour = shour;
	}

	public int getEhour() {
		return ehour;
	}

	public void setEhour(int ehour) {
		this.ehour = ehour;
	}

	public int getSmin() {
		return smin;
	}

	public void setSmin(int smin) {
		this.smin = smin;
	}

	public int getEmin() {
		return emin;
	}

	public void setEmin(int emin) {
		this.emin = emin;
	}

	public String getSdate() {
		return sdate;
	}

	public void setSdate(String sdate) {
		this.sdate = sdate;
	}

	public String getEdate() {
		return edate;
	}

	public void setEdate(String edate) {
		this.edate = edate;
	}

	public String getReg_time() {
		return reg_time;
	}

	public void setReg_time(String reg_time) {
		this.reg_time = reg_time;
	}

	public String getUp_time() {
		return up_time;
	}

	public void setUp_time(String up_time) {
		this.up_time = up_time;
	}

	public int getConfirm() {
		return confirm;
	}

	public void setConfirm(int confirm) {
		this.confirm = confirm;
	}

	public List<Object> getLst() {
		return lst;
	}

	public void setLst(List<Object> lst) {
		this.lst = lst;
	}

	public int[] getIdxLst() {
		return idxLst;
	}

	public void setIdxLst(int[] idxLst) {
		this.idxLst = idxLst;
	}

	public String getGroupSelect() {
		return groupSelect;
	}

	public void setGroupSelect(String groupSelect) {
		this.groupSelect = groupSelect;
	}

	public int getCriticalvalue() {
		return criticalvalue;
	}

	public void setCriticalvalue(int criticalvalue) {
		this.criticalvalue = criticalvalue;
	}

	public int getWarningvalue() {
		return warningvalue;
	}

	public void setWarningvalue(int warningvalue) {
		this.warningvalue = warningvalue;
	}

	public int getInfovalue() {
		return infovalue;
	}

	public void setInfovalue(int infovalue) {
		this.infovalue = infovalue;
	}

	public String getGroupText() {
		return groupText;
	}

	public void setGroupText(String groupText) {
		this.groupText = groupText;
	}

	public int getDeviceCode() {
		return deviceCode;
	}

	public void setDeviceCode(int deviceCode) {
		this.deviceCode = deviceCode;
	}

	public int getUserCode() {
		return userCode;
	}

	public void setUserCode(int userCode) {
		this.userCode = userCode;
	}
	
	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}
	public int getLogKeepPeriodDay() {
		return logKeepPeriodDay;
	}

	public void setLogKeepPeriodDay(int logKeepPeriodDay) {
		this.logKeepPeriodDay = logKeepPeriodDay;
	}

	public int getMainRefreshPeriodMinute() {
		return mainRefreshPeriodMinute;
	}

	public void setMainRefreshPeriodMinute(int mainRefreshPeriodMinute) {
		this.mainRefreshPeriodMinute = mainRefreshPeriodMinute;
	}

	public String getSearch_gubun() {
		return search_gubun;
	}

	public void setSearch_gubun(String search_gubun) {
		this.search_gubun = search_gubun;
	}

	public String getSearch_text() {
		return search_text;
	}

	public void setSearch_text(String search_text) {
		this.search_text = search_text;
	}

	public String getDsname() {
		return dsname;
	}

	public void setDsname(String dsname) {
		this.dsname = dsname;
	}

	public String getGraph_shour() {
		return graph_shour;
	}

	public void setGraph_shour(String graph_shour) {
		this.graph_shour = graph_shour;
	}

	public String getGraph_ehour() {
		return graph_ehour;
	}

	public void setGraph_ehour(String graph_ehour) {
		this.graph_ehour = graph_ehour;
	}

	public String getGraph_smin() {
		return graph_smin;
	}

	public void setGraph_smin(String graph_smin) {
		this.graph_smin = graph_smin;
	}

	public String getGraph_emin() {
		return graph_emin;
	}

	public void setGraph_emin(String graph_emin) {
		this.graph_emin = graph_emin;
	}

	public String getDbname() {
		return dbname;
	}

	public void setDbname(String dbname) {
		this.dbname = dbname;
	}

	public String getType_gubun() {
		return type_gubun;
	}

	public void setType_gubun(String type_gubun) {
		this.type_gubun = type_gubun;
	}

	public String getMaingraph_period() {
		return maingraph_period;
	}

	public void setMaingraph_period(String maingraph_period) {
		this.maingraph_period = maingraph_period;
	}

	public String getGraph_period() {
		return graph_period;
	}

	public void setGraph_period(String graph_period) {
		this.graph_period = graph_period;
	}

	public String getMobileFirst() {
		return mobileFirst;
	}

	public void setMobileFirst(String mobileFirst) {
		this.mobileFirst = mobileFirst;
	}

	public String getMobileSecond() {
		return mobileSecond;
	}

	public void setMobileSecond(String mobileSecond) {
		this.mobileSecond = mobileSecond;
	}

	public String getMobileThird() {
		return mobileThird;
	}

	public void setMobileThird(String mobileThird) {
		this.mobileThird = mobileThird;
	}

	public int getLoginUserCode() {
		return loginUserCode;
	}

	public void setLoginUserCode(int loginUserCode) {
		this.loginUserCode = loginUserCode;
	}

	public int getLoginAuth() {
		return loginAuth;
	}

	public void setLoginAuth(int loginAuth) {
		this.loginAuth = loginAuth;
	}

	public String[] getDsNameLst() {
		return dsNameLst;
	}

	public void setDsNameLst(String[] dsNameLst) {
		this.dsNameLst = dsNameLst;
	}

	public int[] getDeviceLst() {
		return deviceLst;
	}

	public void setDeviceLst(int[] deviceLst) {
		this.deviceLst = deviceLst;
	}

	public List<Integer> getIntegerLst() {
		return integerLst;
	}

	public void setIntegerLst(List<Integer> integerLst) {
		this.integerLst = integerLst;
	}

	public List<String> getStringLst() {
		return stringLst;
	}

	public void setStringLst(List<String> stringLst) {
		this.stringLst = stringLst;
	}

	public int getSelectHour() {
		return selectHour;
	}

	public void setSelectHour(int selectHour) {
		this.selectHour = selectHour;
	}

	public int getSelectMin() {
		return selectMin;
	}

	public void setSelectMin(int selectMin) {
		this.selectMin = selectMin;
	}

	public String[] getMyStateLst() {
		return myStateLst;
	}

	public void setMyStateLst(String[] myStateLst) {
		this.myStateLst = myStateLst;
	}

	public String getAllPeriod() {
		return allPeriod;
	}

	public void setAllPeriod(String allPeriod) {
		this.allPeriod = allPeriod;
	}

	public String getMongo_version() {
		return mongo_version;
	}

	public void setMongo_version(String mongo_version) {
		this.mongo_version = mongo_version;
	}

	public String getConsolFun() {
		return consolFun;
	}

	public void setConsolFun(String consolFun) {
		this.consolFun = consolFun;
	}

	public String getGraph_step() {
		return graph_step;
	}

	public void setGraph_step(String graph_step) {
		this.graph_step = graph_step;
	}

	public String getSliderMin() {
		return sliderMin;
	}

	public void setSliderMin(String sliderMin) {
		this.sliderMin = sliderMin;
	}

	public String getSliderMax() {
		return sliderMax;
	}

	public void setSliderMax(String sliderMax) {
		this.sliderMax = sliderMax;
	}

	public String getSearch_option() {
		return search_option;
	}

	public void setSearch_option(String search_option) {
		this.search_option = search_option;
	}

	public String getCollname() {
		return collname;
	}

	public void setCollname(String collname) {
		this.collname = collname;
	}

	public String getDstype() {
		return dstype;
	}

	public void setDstype(String dstype) {
		this.dstype = dstype;
	}

	public String getSearch_sdate() {
		return search_sdate;
	}

	public void setSearch_sdate(String search_sdate) {
		this.search_sdate = search_sdate;
	}

	public String getSearch_edate() {
		return search_edate;
	}

	public void setSearch_edate(String search_edate) {
		this.search_edate = search_edate;
	}	

	public String[] getDbNameLst() {
		return dbNameLst;
	}

	public void setDbNameLst(String[] dbNameLst) {
		this.dbNameLst = dbNameLst;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getAutoRefresh() {
		return autoRefresh;
	}

	public void setAutoRefresh(String autoRefresh) {
		this.autoRefresh = autoRefresh;
	}

	public String getAuthUser() {
		return authUser;
	}

	public void setAuthUser(String authUser) {
		this.authUser = authUser;
	}

	public String getAuthPasswd() {
		return authPasswd;
	}

	public void setAuthPasswd(String authPasswd) {
		this.authPasswd = authPasswd;
	}

	public String getHostname() {
		return hostname;
	}

	public void setHostname(String hostname) {
		this.hostname = hostname;
	}

	public boolean isFinishedInitailRrd() {
		return isFinishedInitailRrd;
	}

	public void setFinishedInitailRrd(boolean isFinishedInitailRrd) {
		this.isFinishedInitailRrd = isFinishedInitailRrd;
	}

	public String[] getResearchLst() {
		return researchLst;
	}

	public void setResearchLst(String[] researchLst) {
		this.researchLst = researchLst;
	}

	public String getBatchMemorySize() {
		return batchMemorySize;
	}

	public void setBatchMemorySize(String batchMemorySize) {
		this.batchMemorySize = batchMemorySize;
	}

	public String getBatchHddSize() {
		return batchHddSize;
	}

	public void setBatchHddSize(String batchHddSize) {
		this.batchHddSize = batchHddSize;
	}

	public String getEnlarge() {
		return enlarge;
	}

	public void setEnlarge(String enlarge) {
		this.enlarge = enlarge;
	}
	
}
