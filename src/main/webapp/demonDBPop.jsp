<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %> 
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
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
<title><spring:message code="graph.daemonDetails"/></title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/daemonGraph.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.basic.tooltip.css">
<%@ include file="./management/CodeUtil.jsp" %>
<%if(session.getAttribute("loginAuth").equals(1)||session.getAttribute("loginAuth").equals(2)){}else{%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common_analytics.js"></script>
<%}%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-latest.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/commonUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.basic.tooltip.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/tooltip.js"></script>
<script type="text/javascript">
$(document).ready( function() {
	goPieGraph('${comm.deviceCode}');
 	goGraph('${comm.deviceCode}');
	setTipByTableName("dbdetail");
// 	var outerHeight = $('#dbdetail').outerHeight();
// 	parent.suframeSize(outerHeight,50,"listdbframe");
// 	if(outerHeight > parent.$('#dbgraphframe').outerHeight())parent.suframeSize(outerHeight,50,"dbgraphframe");
});

function goPieGraph(deviceCode){
	var param = "?deviceCode="+deviceCode;
	var url = "pieGraphView.do"+param;
	parent.document.getElementById("dbgraphframe").src=url;
}

function goGraph(deviceCode){
	var param = "?deviceCode="+deviceCode;
	var url = "popupGraphView.do"+param;
	parent.document.getElementById("graphframe").src=url;
}
</script>
</head>
<body>
<form method="post" name="demon" id="demon">
<input type="hidden" name="deviceCode" id="deviceCode" value="${comm.deviceCode}"/>
		<!-- div window start -->
		<div class="demon_detail">
			<h3>
				<img src="./img/daemon_subtit01<spring:message code="common.img"/>.png">
			</h3>
			<table id="dbdetail" border="0" class="tb_list_04">
				<colgroup>
					<col width="200">
					<col width="200">
					<col width="200">
					<col width="200">
					<col width="200">
					<col width="200">
				</colgroup>
				<thead>
					<tr>
						<th class="bar" rowspan="2"><p><spring:message code="graph.dbName"/></p></th>
						<th class="bar" colspan="4"><p><spring:message code="graph.dbSize"/></p></th>
						<th class="bar" rowspan="2"><p><spring:message code="graph.collections"/></p></th>
					</tr>
					<tr>
						<th class="bar"><p><spring:message code="common.data"/></p></th>
						<th class="bar"><p><spring:message code="common.storage"/></p></th>
						<th class="bar"><p><spring:message code="common.index"/></p></th>
						<th class="bar"><p><spring:message code="common.file"/></p></th>
				</thead>
				<tbody>
						<c:choose>
							<c:when test="${fn:length(dbStatus) == 0}">
								<tr>
									<td colspan="3" class="rb tableFigures last"><spring:message code="daemons.nodb"/></td>
								</tr>
							</c:when>
							<c:otherwise>
								<c:forEach var="dbstatus" items="${dbStatus}" varStatus="loop">
									<c:choose>
										<c:when test="${fn:length(dbStatus)-1 == loop.index}">
											<tr>
												<td class="lb rb tableFiguresLeft last">${dbstatus.db}</td>
												<td class="rb tableFigures last">
													<script>document.write('<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat('${dbstatus.dataSize}'),"Byte") +'">'+ sizeFormat('${dbstatus.dataSize}') +'</a>');</script>
												</td>
												<td class="rb tableFigures last">
													<script>document.write('<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat('${dbstatus.storageSize}'),"Byte") +'">'+ sizeFormat('${dbstatus.storageSize}') +'</a>');</script>
												</td>
												<td class="rb tableFigures last">
													<script>document.write('<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat('${dbstatus.indexSize}'),"Byte") +'">'+ sizeFormat('${dbstatus.indexSize}') +'</a>');</script>
												</td>
												<td class="rb tableFigures last">
													<script>document.write('<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat('${dbstatus.fileSize}'),"Byte") +'">'+ sizeFormat('${dbstatus.fileSize}') +'</a>');</script>
												</td>
												<td class="rb tableFigures last">
													<script>document.write('<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat('${dbstatus.collections}'),"") +'">'+ exFormat('${dbstatus.collections}') +'</a>');</script>
												</td>
											</tr>
										</c:when>
										<c:otherwise>
											<tr>
												<td class="lb rb tableFiguresLeft">${dbstatus.db}</td>
												<td class="rb tableFigures">
													<script>document.write('<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat('${dbstatus.dataSize}'),"Byte") +'">'+ sizeFormat('${dbstatus.dataSize}') +'</a>');</script>
												</td>
												<td class="rb tableFigures">
													<script>document.write('<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat('${dbstatus.storageSize}'),"Byte") +'">'+ sizeFormat('${dbstatus.storageSize}') +'</a>');</script>
												</td>
												<td class="rb tableFigures">
													<script>document.write('<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat('${dbstatus.indexSize}'),"Byte") +'">'+ sizeFormat('${dbstatus.indexSize}') +'</a>');</script>
												</td>
												<td class="rb tableFigures">
													<script>document.write('<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat('${dbstatus.fileSize}'),"Byte") +'">'+ sizeFormat('${dbstatus.fileSize}') +'</a>');</script>
												</td>
												<td class="rb tableFigures">
													<script>document.write('<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat('${dbstatus.collections}'),"") +'">'+ exFormat('${dbstatus.collections}') +'</a>');</script>
												</td>
											</tr>
										</c:otherwise>
									</c:choose>
								</c:forEach>
							</c:otherwise>
						</c:choose>
					<tr>
					</tr>
				</tbody>
			</table>
				
		</div>
		<!-- // div window end -->

</form>
</body>
</html>
