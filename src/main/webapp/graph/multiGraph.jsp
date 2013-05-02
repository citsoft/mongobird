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
<%@ page import="java.util.Calendar"%>
<%@ page import="net.cit.tetrad.common.Utility"%>

<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>  

<%
	String path = request.getContextPath();
	response.setHeader("Cache-Control", "no-store");
	response.setHeader("Pragma", "no-cache");
	response.setDateHeader("Expires", 0);
	if (request.getProtocol().equals("HTTP/1.1"))
		response.setHeader("Cache-Control", "no-cache");
%>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="main.title"/></title>
<%@ include file="./graphCommon.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/subgraph_js/sub_graph.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	var strResult="${comm.stringLst}";
	var idx = strResult.substring(1,strResult.length-1).split(", ");
	for (var i=0; i<idx.length; i++) {
		$('#frm_demon input:checkbox').filter('[value=' + idx[i] + ']').attr('checked', true);
	}
	checkLst();
});
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/subgraph_js/multiGraph.js"></script>
</head>
<body onresize="touchSlider()">
		<!-- header start -->
		<%@ include file="/top_menu.jsp"%>
		<!-- // header end -->
	<!-- container start-->
	<div id="container">
		<!-- content start -->
		<div id="content_graph">
		<h1>
			<img src="./img/graph_subtit05<spring:message code="common.img"/>.png">
		</h1>
		<form method="post" name="frm_demon" id="frm_demon">
		<input type="hidden" id="sdate" name="sdate" value="${comm.sdate}" />
		<input type="hidden" id="edate" name="edate" value="${comm.edate}" />
		<input type="hidden" id="type_gubun" name="type_gubun" value="${comm.type_gubun}"/>
		<input type="hidden" id="sortItem" name="sortItem" value="${comm.sortItem}"/>
		<input type="hidden" id="count" name="count" value="${comm.count}"/>
		<input type="hidden" id="graph_period" name="graph_period" value="${comm.graph_period}"/>
		<input type="hidden" id="sliderMin" name="sliderMin" value="${comm.sliderMin}"/>
		<input type="hidden" id="sliderMax" name="sliderMax" value="${comm.sliderMax}"/>
			<div id="menu_graph">
				<ul>
					<li class="end"><b><img src="./img/graph_subtit01_slt02<spring:message code="common.img"/>.png"></b></li>
					<c:forEach var="i" items="${deviceLst}">
						<li><input type="radio" id="deviceCode" name="deviceCode" value="${i.idx}" <c:if test="${i.idx==comm.deviceCode}" >checked</c:if> >  ${i.uid}</li>
					</c:forEach>
					<li class="noline"></li>
					<li class="end"><b><img src="./img/graph_subtit01_slt01<spring:message code="common.img"/>.png"></b></li>
					<c:forEach var="lst" items="${dsNameLst}">
							<c:choose>
					       <c:when test="${lst == 'conn'}">
					       			<li><input type="checkbox" class="paramckbox" id="dsNameLst" name="dsNameLst" value="${lst}"/>  <spring:message code="graph.connectionState"/></li>
					       </c:when>
					       <c:when test="${lst == 'r_mem'}">
					           <li><input type="checkbox" class="paramckbox" id="dsNameLst" name="dsNameLst" value="${lst}"/>  <spring:message code="graph.residentMemory"/></li>
					       </c:when>
					       <c:when test="${lst == 'v_mem'}">
					           <li><input type="checkbox" class="paramckbox" id="dsNameLst" name="dsNameLst" value="${lst}"/>  <spring:message code="graph.virtualMemory"/></li>
					       </c:when>
					       <c:when test="${lst == 'p_fault'}">
					           <li><input type="checkbox" class="paramckbox" id="dsNameLst" name="dsNameLst" value="${lst}"/>  <spring:message code="graph.pageFault"/></li>
					       </c:when>
					       <c:when test="${lst == 'op_insert'}">
					           <li><input type="checkbox" class="paramckbox" id="dsNameLst" name="dsNameLst" value="${lst}"/>  <spring:message code="graph.queryState"/> - Insert</li>
					       </c:when>
					       <c:when test="${lst == 'op_update'}">
					           <li><input type="checkbox" class="paramckbox" id="dsNameLst" name="dsNameLst" value="${lst}"/>  <spring:message code="graph.queryState"/> - Update</li>
					       </c:when>
					       <c:when test="${lst == 'op_delete'}">
					           <li><input type="checkbox" class="paramckbox" id="dsNameLst" name="dsNameLst" value="${lst}"/>  <spring:message code="graph.queryState"/> - Delete</li>
					       </c:when>
					       <c:when test="${lst == 'op_query'}">
					           <li><input type="checkbox" class="paramckbox" id="dsNameLst" name="dsNameLst" value="${lst}"/>  <spring:message code="graph.queryState"/> - Query</li>
					       </c:when>
					       <c:when test="${lst == 'global'}">
					           <li><input type="checkbox" class="paramckbox" id="dsNameLst" name="dsNameLst" value="${lst}"/>  <spring:message code="graph.globalLock"/></li>
					       </c:when>
					       <c:when test="${lst == 'totdb'}">
					           <li><input type="checkbox" class="paramckbox" id="dsNameLst" name="dsNameLst" value="${lst}"/>  <spring:message code="graph.dbInfo"/> - Total</li>
					       </c:when>
					       <c:when test="${lst == 'db'}">
					           <li><input type="checkbox" class="paramckbox" id="dsNameLst" name="dsNameLst" value="${lst}"/>  <spring:message code="graph.dbInfo"/> - <c:out value="${dbinfo.db }" /></li>
					       </c:when>
					       <c:when test="${lst == 'in_network'}">
					           <li><input type="checkbox" class="paramckbox" id="dsNameLst" name="dsNameLst" value="${lst}"/>  <spring:message code="graph.networkIn"/></li>
					       </c:when>
					       <c:when test="${lst == 'out_network'}">
					           <li><input type="checkbox" class="paramckbox" id="dsNameLst" name="dsNameLst" value="${lst}"/>  <spring:message code="graph.networkOut"/></li>
					       </c:when>
					   	</c:choose>
					</c:forEach>
					<li class="noline"></li>
					<li class="noline">
						<input type="checkbox" id="checkAll"/> check all <input type="image" class="alignRight" src="./img/btn_apply<spring:message code="common.img"/>.gif" width="45" height="20" alt="apply" class="btn_apply" id="btn_apply" onclick='checkNull(setGraph); return false;'>
					</li>
				</ul>
			</div>
				<%@ include file="./graphCommonImage.jsp" %>
		</form>
	</div>
</div>
</body>
</html>
