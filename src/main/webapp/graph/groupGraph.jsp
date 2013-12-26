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
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="main.title"/></title>
<%@ include file="./graphCommon.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/subgraph_js/groupGraph.js"></script>
<!--  이부분에  TypeGraph.js넣어 줄것 -->
</head>
<body onresize="touchSlider()">
		<!-- header start -->
		<%@ include file="/top_menu.jsp"%>
		<!-- // header end -->
	<!-- container start-->
	<div id="container">
		<!-- content start -->
		<div id="content_graph">
		<div class="graph_sub_menu">
			<spring:message code="menu.normal_status" /> > <spring:message code="menu.group" />
		</div>
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
					<li class="end"><b><img src="./img/graph_subtit02_slt01<spring:message code="common.img"/>.png"></b></li>
					<c:forEach var="i" items="${groupLst}">						
						<li><input type="radio" id="groupCode" name="groupCodes" value="${i.idx}" <c:if test="${i.idx==comm.groupCodes}" >checked</c:if> />  ${i.uid}</li>
					</c:forEach>
				 
				 	<br/>
					<li class="end"><b><img src="./img/graph_subtit03_slt01<spring:message code="common.img"/>.png"></b></li>
					<c:forEach var="i" items="${typeModelLst}">
						<li><input type="radio" id="type" name="groupCodes" value="${i.groupList}" <c:if test="${i.groupList==comm.groupCodes}" >checked</c:if> />  ${i.name}</li>
					</c:forEach>
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
