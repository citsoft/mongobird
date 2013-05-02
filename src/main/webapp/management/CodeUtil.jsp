<!--
"mongobird" is released under a dual license model designed to developers 
and commercial deployment.

For using OEMs(Original Equipment Manufacturers), ISVs(Independent Software
Vendor), ISPs(Internet Service Provider), VARs(Value Added Resellers) 
and another distributors, or for using include changed issue
(modify / application), it must have to follow the Commercial License policy.
To check the Commercial License Policy, you need to contact Cardinal Info.Tech.Co., Ltd.
(http://www.citsoft.net)
 *
If not using Commercial License (Academic research or personal research),
it might to be under AGPL policy. To check the contents of the AGPL terms,
please see "http://www.gnu.org/licenses/"
-->
<%@page import="org.springframework.web.context.support.WebApplicationContextUtils"%>
<%@page import="org.springframework.web.context.WebApplicationContext"%>
<%@page import="org.springframework.context.ApplicationContext"%>
<%@page import="net.cit.tetrad.dao.management.MainDao"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.*"%>
<%@ page import="net.sf.json.JSONObject"%>
<%@ page import="net.cit.tetrad.utility.code.Code" %>
<%@page import="net.cit.tetrad.common.ColumnConstent"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>

<%
	String mongoVersion = ""; 
	Object mongoVersionObj = request.getAttribute("mongoVer");
	if (mongoVersionObj == null)  {
		WebApplicationContext context =  WebApplicationContextUtils.getWebApplicationContext(getServletContext());
		MainDao mainDao = (MainDao)context.getBean("mainDao");
		mongoVersion = mainDao.getGlobalMongoVersion();
	} else {
		mongoVersion = mongoVersionObj.toString();
	}	

	String searchGcode = ColumnConstent.COL_GROUPCODE;
	String searchUid = ColumnConstent.COL_DEVICECODE;
	String searchType = ColumnConstent.DEVICE_TYPE;
	String searchAll = ColumnConstent.SEARCHALL;

	JSONObject groupCodeJson = Code.group.getCode().getCodeJson();
	JSONObject deviceCodeJson = Code.device.getCode().getCodeJson();
	JSONObject userCodeJson = Code.user.getCode().getCodeJson();
	JSONObject eventCodeJson = Code.event.getCode().getCodeJson();
	JSONObject unitCodeJson = Code.unit.getCode().getCodeJson();
	JSONObject typeCodeJson = Code.type.getCode().getCodeJson();
	JSONObject mongosEventCodeJson = Code.mongosEvent.getCode().getCodeJson();
	JSONObject mongodEventCodeJson = mongoVersion.equals("2.2") ? Code.mongodEvent2_2.getCode().getCodeJson() : Code.mongodEvent.getCode().getCodeJson();
%>

<script type="text/javascript" src="./js/commonUtil.js"></script>
<script type="text/javascript">

	var groupCodeTable = new HashTable(<%=groupCodeJson %>);
	var deviceCodeTable = new HashTable(<%=deviceCodeJson %>);
	var userCodeTable = new HashTable(<%=userCodeJson %>);
	var eventCodeTable = new HashTable(<%=eventCodeJson %>);
	var unitCodeTable = new HashTable(<%=unitCodeJson %>);
	var typeCodeTable = new HashTable(<%=typeCodeJson %>);
	var mongosEventCodeTable = new HashTable(<%=mongosEventCodeJson %>);
	var mongodEventCodeTable = new HashTable(<%=mongodEventCodeJson %>);
	
	function getGroupName(param){
		groupCodeTable.getItem(param);
	}
	
	function getDeviceName(param){
		deviceCodeTable.getItem(param);
	}
	
	function getUserName(param){
		userCodeTable.getItem(param);
	}
	
	function getEventName(param){
		eventCodeTable.getItem(param);
	}
	
	function getUnitName(param){
		unitCodeTable.getItem(param);
	}
	
	var eventOptionKeyList = new Array();
	var eventOptionValueList = new Array();
<%
	Set<Object> keySet = mongodEventCodeJson.keySet();
	Iterator<Object> it = keySet.iterator();
	int idx = 0;
%>
	eventOptionKeyList[<%=idx%>] = "";
	eventOptionValueList[<%=idx%>] = "--<spring:message code="common.chooseone"/>--";
<%
	while(it.hasNext()){
		idx++;
		String key = it.next().toString();
		String value = mongodEventCodeJson.get(key).toString();
%>
		eventOptionKeyList[<%=idx%>] = "<%=key%>";
		eventOptionValueList[<%=idx%>] = "<%=value%>";
<%
	}
%>
	function mongod_onload() {
		for(var i = 0; i < eventOptionKeyList.length; i++){
			document.setForm.mongod_type.options[i] = new Option(eventOptionValueList[i], eventOptionKeyList[i]);
		};
	}
	
	var sEventOptionKeyList = new Array();
	var sEventOptionValueList = new Array();
	
<%
	Set<Object> skeySet = mongosEventCodeJson.keySet();
	Iterator<Object> sit = skeySet.iterator();
	int sidx = 0;
%>
	sEventOptionKeyList[<%=sidx%>] = "";
	sEventOptionValueList[<%=sidx%>] = "--<spring:message code="common.chooseone"/>--";
<%
	while(sit.hasNext()){
		sidx++;
		String skey = sit.next().toString();
		String svalue = mongosEventCodeJson.get(skey).toString();
%>
	sEventOptionKeyList[<%=sidx%>] = "<%=skey%>";
	sEventOptionValueList[<%=sidx%>] = "<%=svalue%>";
<%
	}
%>
	function mongos_onload() {
		for(var i = 0; i < sEventOptionKeyList.length; i++){
			document.setForm.mongos_type.options[i] = new Option(sEventOptionValueList[i], sEventOptionKeyList[i]);
		};
	}

	function makeSubSelect(){
		var gubun = document.listForm.search_gubun.value;
		document.listForm.search_text.length = 0;
		var keys = null;
		var values = null;
		if(gubun!=""){
			if(gubun == "<%=searchGcode%>"){
				keys = groupCodeTable.keys();
				values = groupCodeTable.values();
			}else if(gubun == "<%=searchUid%>"){
				keys = deviceCodeTable.keys();
				values = deviceCodeTable.values();
			}else if(gubun == "<%=searchType%>"){
				keys = typeCodeTable.keys();
				values = typeCodeTable.values();
			}else if(gubun == "<%=searchAll%>"){
				keys = "";
				values = "";
			};
		}
		document.listForm.search_text.options[0] = new Option("--<spring:message code="common.chooseone"/>--", "");
		for(var i = 1; i < keys.length+1; i++){
			document.listForm.search_text.options[i] = new Option(values.slice(i-1, i), keys.slice(i-1, i));
		};
	}

</script>
