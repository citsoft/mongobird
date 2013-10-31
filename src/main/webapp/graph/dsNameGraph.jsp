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
<!--
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
-->
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="main.title"/></title>
<%@ include file="./graphCommon.jsp" %>
<script type="text/javascript">
$(document).ready(function() {
	var intStr = '${comm.integerLst}';
	if(intStr != ''){
		var idx = eval(intStr);
		for (var i=0; i<idx.length; i++) {
			$('#frm_demon input:checkbox').filter('[value=' + idx[i] + ']').attr('checked', true);
		}
		checkLst();
	}
});
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/subgraph_js/dsNameGraph.js"></script>
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
		<img src="./img/graph_subtit01<spring:message code="common.img"/>.png">
	</h1>
	<form method="post" name="frm_demon" id="frm_demon">
		<input type="hidden" id="sdate" name="sdate" value="${comm.sdate}" />
		<input type="hidden" id="edate" name="edate" value="${comm.edate}" />
		<input type="hidden" id="type_gubun" name="type_gubun" value="${comm.type_gubun}"/>
		<input type="hidden" id="sortItem" name="sortItem" value="big"/>
		<input type="hidden" id="graph_period" name="graph_period" value="${comm.graph_period}"/>
		<input type="hidden" id="sliderMin" name="sliderMin" value="${comm.sliderMin}"/>
		<input type="hidden" id="sliderMax" name="sliderMax" value="${comm.sliderMax}"/>
			<div id="menu_graph">
				<ul>
					<li class="end"><b><img src="./img/graph_subtit01_slt01<spring:message code="common.img"/>.png"></b></li>
					<c:forEach var="i" items="${dsNameLst}">
						<li>
						<input type="radio" id="dsname" name="dsname" value="${i}" <c:if test="${i==comm.dsname}" >checked</c:if> >
							<c:choose>
					       <c:when test="${i == 'conn'}">
					           <spring:message code="graph.connectionState"/>
					       </c:when>
					       <c:when test="${i == 'r_mem'}">
					           <spring:message code="graph.residentMemory"/>
					       </c:when>
					       <c:when test="${i == 'v_mem'}">
					           <spring:message code="graph.virtualMemory"/>
					       </c:when>
					       <c:when test="${i == 'p_fault'}">
					           <spring:message code="graph.pageFault"/>
					       </c:when>
					       <c:when test="${i == 'op_insert'}">
					           <spring:message code="graph.queryState"/> - Insert
					       </c:when>
					       <c:when test="${i == 'op_update'}">
					           <spring:message code="graph.queryState"/> - Update
					       </c:when>
					       <c:when test="${i == 'op_delete'}">
					           <spring:message code="graph.queryState"/> - Delete
					       </c:when>
					       <c:when test="${i == 'op_query'}">
					           <spring:message code="graph.queryState"/> - Query
					       </c:when>
					       <c:when test="${i == 'global'}">
					           <spring:message code="graph.globalLock"/>
					       </c:when>
					       <c:when test="${i == 'in_network'}">
					           <spring:message code="graph.networkIn"/>
					       </c:when>
					       <c:when test="${i == 'out_network'}">
					           <spring:message code="graph.networkOut"/>
					       </c:when>
					       <c:when test="${i == 'totdb'}">
					           <spring:message code="graph.dbInfo"/> - Total
					       </c:when>
					       <c:when test="${i == 'db'}">
					           <spring:message code="graph.dbInfo"/> - <c:out value="${dbinfo.db }" />
					       </c:when>
					   	</c:choose>
						</li>
					</c:forEach>
					<li class="noline"></li>
					<li class="end"><b><img src="./img/graph_subtit01_slt02<spring:message code="common.img"/>.png"></b></li>
					<c:forEach var="lst" items="${deviceLst}">
						<li><input type="checkbox" class="paramckbox" id="deviceLst" name="deviceLst" value="${lst.idx}"/>  ${lst.uid}</li>
					</c:forEach>
					<li class="noline"></li>
					<li class="noline"><input type="checkbox" id="checkAll"/> check all 	</li>
					<li class="noline"><input type="checkbox" class="stateckbox" id="masterAll"/> master all 	</li>
					<li class="noline"><input type="checkbox" class="stateckbox" id="slaveAll"/> slave all 	</li>
					<li class="noline">
						<input type="image" class="alignRight" src="./img/btn_apply<spring:message code="common.img"/>.gif" width="45" height="20" alt="apply" class="btn_apply" id="btn_apply" onclick='checkNull(setGraph); return false;'>
					</li>
				</ul>
			</div>
			<%@ include file="./graphCommonImage.jsp" %>
		</form>
		<!-- License information View -->
		<jsp:include page="/footer.jsp" flush="false"/>
	</div>
</div>
</body>
</html>
