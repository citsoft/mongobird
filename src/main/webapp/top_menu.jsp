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
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%
	if(session.getAttribute("loginAuth")==null||session.getAttribute("loginAuth").toString().length()==0||session.getAttribute("loginAuth").equals("")){
%>
<script type="text/javascript">
	window.location="${pageContext.request.contextPath}/";
</script>
<%
		return;
	}else{
%>

<%if(session.getAttribute("loginAuth").equals(1)||session.getAttribute("loginAuth").equals(2)){}else{%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common_analytics.js"></script>
<%}%>
<%
String strStatusTitle = "MongoDB Dashboard";
if (request.getRequestURL().indexOf("sub_page")>0){
	strStatusTitle = "데몬";
}else if (request.getRequestURL().indexOf("Graph")>0){
	strStatusTitle = "그래프";
}else if (request.getRequestURL().indexOf("sub_alarm_page")>0){
	strStatusTitle = "이벤트";
}else if (request.getRequestURL().indexOf("user.jsp")>0){
	strStatusTitle = "사용자 관리";
}else if (request.getRequestURL().indexOf("device.jsp")>0){
	strStatusTitle = "데몬 관리";
}else if (request.getRequestURL().indexOf("critical.jsp")>0){
	strStatusTitle = "임계값 관리";
}else if (request.getRequestURL().indexOf("global.jsp")>0){
	strStatusTitle = "전역 관리";
}else if (request.getRequestURL().indexOf("dsNameGraph")>0){
	strStatusTitle = "항목";
}else if (request.getRequestURL().indexOf("groupGraph")>0){
	strStatusTitle = "그룹";
}else if (request.getRequestURL().indexOf("typeGraph")>0){
	strStatusTitle = "유형";
}else if (request.getRequestURL().indexOf("deviceGraph")>0){
	strStatusTitle = "장비";
}else if (request.getRequestURL().indexOf("multiGraph")>0){
	strStatusTitle = "다중항목";
}else if (request.getRequestURL().indexOf("Lock")>0){
	strStatusTitle = "락그래프";
}else if (request.getRequestURL().indexOf("deviceLockGraph")>0){
	strStatusTitle = "장비락";
}else if (request.getRequestURL().indexOf("dbLockGraph")>0){
	strStatusTitle = "db락";
}else if (request.getRequestURL().indexOf("typeLockGraph")>0){
	strStatusTitle = "유형락";
}else if (request.getRequestURL().indexOf("groupLockGraph")>0){
	strStatusTitle = "그룹락";
}
%>
<div id="header">
	<div class="top_area">
		<ul class="logo">
			<li class="first"><a href="mainView.do"><img src="${pageContext.request.contextPath}/img/mm_title<spring:message code="common.img"/>.png" alt="운영관리 시스템"></a></li>
			<li><a href="http://www.mongodb.org/" target="_blank"><img src="./img/logo_mongoDB.png" width="96" height="35" alt="mongoDB"></a></li>
		</ul>
		<ul class="top_sign" style="height:60px;">
				<li id="balancing" style="width:55px;" title="Balancing"><a href="subShardInfoView.do"><span id="shardingCheck" style="display:none;"><img id="mongos_img" src="./img/btn_balancing.gif" width="52" height="52"></span></a></li>
				<li style="width:40px;"></li>
				<li id="mongos" title="mongos"><a href="#"><img id="mongos_img" src="./img/btn_sign_01_gray.gif" width="52" height="52" alt="sign_01"></a></li>
				<li id="mongod" title="mongod"><a href="#"><img id="mongod_img" src="./img/btn_sign_02_gray.gif" width="52" height="52" alt="sign_02"></a></li>
				<li id="config" title="config" class="last"><a href="#"><img id="config_img" src="./img/btn_sign_03_gray.gif" width="52" height="52" alt="sign_03"></a></li>
		</ul>
	</div>
	<div class="gnb_area">
		<ul class="gnb" id="nav">
			<li class="first"><a href="mainView.do"><img src="${pageContext.request.contextPath}/img/tab_gnb_01<%=request.getRequestURL().indexOf("main")>0?"_on":"" %><spring:message code="common.img"/>.gif" width="86" height="30" alt="대시보드"></a></li>
			<li>
				<a href="subPageView.do"><img src="${pageContext.request.contextPath}/img/tab_gnb_02<%=request.getRequestURL().indexOf("sub_page")>0||request.getRequestURL().indexOf("ShardInfo")>0?"_on":"" %><spring:message code="common.img"/>.gif" height="30" alt="데몬"></a>
				<ul>
					<li><a href="subPageView.do"><img src="${pageContext.request.contextPath}/img/DaemonSub01_dspec<%=request.getRequestURL().indexOf("sub_page")>0?"_on":"" %><spring:message code="common.img"/>.png" ></a></li>
					<li><a href="subShardInfoView.do"><img src="${pageContext.request.contextPath}/img/DaemonSub02_sharding<%=request.getRequestURL().indexOf("ShardInfo")>0?"_on":"" %><spring:message code="common.img"/>.png" ></a></li>
				</ul>
			</li>
			<li>
				<a href="dsNameGraphView.do"><img src="${pageContext.request.contextPath}/img/tab_gnb_graph<%=request.getRequestURL().indexOf("Graph")>0&&request.getRequestURL().indexOf("Lock")<0&&request.getRequestURL().indexOf("DbGraph")<0?"_on":"" %><spring:message code="common.img"/>.gif" width="72" height="30" alt="그래프"></a>
				<ul>
					<li><a href="dsNameGraphView.do"><img src="${pageContext.request.contextPath}/img/tab_subgnb_itemized<%=request.getRequestURL().indexOf("dsNameGraph")>0?"_on":"" %><spring:message code="common.img"/>.gif" ></a></li>
					<li><a href="multiGraphView.do"><img src="${pageContext.request.contextPath}/img/tab_subgnb_multi<%=request.getRequestURL().indexOf("multiGraph")>0?"_on":"" %><spring:message code="common.img"/>.gif" ></a></li>
					<li><a href="groupGraphView.do"><img src="${pageContext.request.contextPath}/img/tab_subgnb_group-specific<%=request.getRequestURL().indexOf("groupGraph")>0?"_on":"" %><spring:message code="common.img"/>.gif" ></a></li>
					<li><a href="typeGraphView.do"><img src="${pageContext.request.contextPath}/img/tab_subgnb_types<%=request.getRequestURL().indexOf("typeGraph")>0?"_on":"" %><spring:message code="common.img"/>.gif" ></a></li>
					<li><a href="deviceGraphView.do"><img src="${pageContext.request.contextPath}/img/tab_subgnb_equipment<%=request.getRequestURL().indexOf("deviceGraph")>0?"_on":"" %><spring:message code="common.img"/>.png" ></a></li>
				</ul>
			</li>
			<li>
				<a href="allDbGraphView.do"><img src="${pageContext.request.contextPath}/img/tab_gnb_DBgraph<%=request.getRequestURL().indexOf("DbGraph")>0?"_on":"" %><spring:message code="common.img"/>.png" width="86" height="30" ></a>
				<ul>
					<li><a href="allDbGraphView.do"><img src="${pageContext.request.contextPath}/img/gnb_dbgraph_sub01_total<%=request.getRequestURL().indexOf("allDbGraph")>0?"_on":"" %><spring:message code="common.img"/>.png" ></a></li>
					<li><a href="deviceDbGraphView.do"><img src="${pageContext.request.contextPath}/img/gnb_dbgraph_sub02_daemon<%=request.getRequestURL().indexOf("deviceDbGraph")>0?"_on":"" %><spring:message code="common.img"/>.png" ></a></li>
				</ul>
			</li>
	<%if(request.getAttribute("mongoVer").equals("2.2")){%>
			<li>
				<a href="deviceLockGraphView.do"><img src="${pageContext.request.contextPath}/img/tab_gnb_lockgraph<%=request.getRequestURL().indexOf("Lock")>0?"_on":"" %><spring:message code="common.img"/>.png" height="30"></a>
				<ul>
					<li><a href="deviceLockGraphView.do"><img src="${pageContext.request.contextPath}/img/tab_gnb_lockgraph_daemon<%=request.getRequestURL().indexOf("deviceLockGraph")>0?"_on":"" %><spring:message code="common.img"/>.png" height="30"></a><br></li>
					<li><a href="dbLockGraphView.do"><img src="${pageContext.request.contextPath}/img/tab_gnb_lockgraph_db<%=request.getRequestURL().indexOf("dbLockGraph")>0?"_on":"" %><spring:message code="common.img"/>.png" height="30"></a><br></li>
					<li><a href="typeLockGraphView.do"><img src="${pageContext.request.contextPath}/img/tab_gnb_lockgraph_types<%=request.getRequestURL().indexOf("typeLockGraph")>0?"_on":"" %><spring:message code="common.img"/>.png" height="30"></a><br></li>
					<li><a href="groupLockGraphView.do"><img src="${pageContext.request.contextPath}/img/tab_gnb_lockgraph_group<%=request.getRequestURL().indexOf("groupLockGraph")>0?"_on":"" %><spring:message code="common.img"/>.png" height="30"></a><br></li>
				</ul>
			</li>
	<%}%>
			<li><a href="subAlarmView.do?alarm=4"><img src="${pageContext.request.contextPath}/img/tab_gnb_03<%=request.getRequestURL().indexOf("sub_alarm_page")>0?"_on":"" %><spring:message code="common.img"/>.gif" width="77" height="30" alt="이벤트"></a></li>
			<%if(session.getAttribute("loginAuth").equals(1)||session.getAttribute("loginAuth").equals(2)){%>
				<li><a href="listManagement.do?dival=0"><img src="${pageContext.request.contextPath}/img/tab_gnb_04<%=request.getRequestURL().indexOf("user.jsp")>0?"_on":"" %><spring:message code="common.img"/>.gif" width="104" height="30" alt="사용자 관리"></a></li>
				<li><a href="listManagement.do?dival=1"><img src="${pageContext.request.contextPath}/img/tab_gnb_05<%=request.getRequestURL().indexOf("device.jsp")>0?"_on":"" %><spring:message code="common.img"/>.gif" width="94" height="30" alt="데몬 관리"></a></li>
				<li><a href="listManagement.do?dival=2"><img src="${pageContext.request.contextPath}/img/tab_gnb_06<%=request.getRequestURL().indexOf("critical.jsp")>0?"_on":"" %><spring:message code="common.img"/>.gif" width="104" height="30" alt="임계값 관리"></a></li>
				<li class="last"><a href="listManagement.do?dival=3"><img src="${pageContext.request.contextPath}/img/tab_gnb_07<%=request.getRequestURL().indexOf("global.jsp")>0?"_on":"" %><spring:message code="common.img"/>.gif" width="94" height="30" alt="전역 관리"></a></li>
			<%}%>
			<li class="last right"><a href="http://monad.citsoft.net/" target="_blank"><img src="${pageContext.request.contextPath}/img/help_icon_txt<spring:message code="common.img"/>.png" height="25" alt="<spring:message code="common.help"/>"></a></li>
		</ul>
	</div>	
</div>
<%}%>
