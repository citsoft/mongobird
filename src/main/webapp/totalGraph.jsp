<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ page import="java.util.Calendar"%>
<%@ page import="net.cit.tetrad.common.Utility"%>
<%@ page import="net.cit.tetrad.rrd.utils.TimestampUtil"%>
<%@ page import="net.cit.tetrad.common.DateUtil"%>

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
<%if(session.getAttribute("loginAuth").equals(1)||session.getAttribute("loginAuth").equals(2)){}else{%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common_analytics.js"></script>
<%}%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/subgraph.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.showLoading.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-latest.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.showLoading.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/dateUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/commonUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/main_js/main_totalGraph.js"></script>
<script type="text/javascript">

var imgLang = "<spring:message code="common.img"/>";
$(document).ready(function() {
	var defaultSrc = "./sub/graph.jsp?fileName=";
	var timestamp = "&dt="+nowDateNoFormat();
	var dsName = '${file}';
	var srcStr = defaultSrc + dsName + timestamp;
	$("#graph_area_graph").append('<img onload="loadImage(this)" onerror="defaultImg_graph(\'./img/sub_nograph'+imgLang+'.gif\', this);" src="'+srcStr+'">');
});
</script>
</head>
<body>
	<div id="wrap" class="w586">
		<div id="header">
			<h1>
				<c:choose>
			       <c:when test="${comm.dsname == 'totalDbDataSize'}">
			           <img src="${pageContext.request.contextPath}/img/dd_subtit_01.png" height="14">
			       </c:when>
			       <c:when test="${comm.dsname == 'totalDbIndexSize'}">
			           <img src="${pageContext.request.contextPath}/img/dd_subtit_02.png" height="14">
			       </c:when>
			       <c:when test="${comm.dsname == 'totalGlobalLock' && mongoVer == '2'}">
			           <img src="${pageContext.request.contextPath}/img/dd_subtit_03.png" height="14">
			       </c:when>
			       <c:when test="${comm.dsname == 'totalGlobalLock' && mongoVer == '2.2'}">
			           <img src="${pageContext.request.contextPath}/img/subtit_total_locks.png" height="14">
			       </c:when>
			       <c:when test="${comm.dsname == 'totalPageFault'}">
			           <img src="${pageContext.request.contextPath}/img/dd_subtit_04.png" height="14">
			       </c:when>
			   </c:choose>
			</h1>
			<a href="#" id="pop_close" class="popup_close"><img src="./img/ico_x.gif" width="27" height="21" alt="X"></a>			
		</div>
		<c:choose>
    	<c:when test="${gubun == 'DB' && 'cur'==comm.dstype}">	
					<div id="content">
							<form method="post" name="totalGraphForm" id="totalGraphForm">
							<input type="hidden" id="dsname" name="dsname" value="${comm.dsname}"/>
							<input type="hidden" id="sortItem" name="sortItem" value="${comm.sortItem}"/>
								<fieldset class="total_graph">
									<b><img src="./img/ico_square.gif">  <spring:message code="graph.choosePeriodOfGraph"/> :  </b>						
										<input type="radio" id="minPeriod" name="graph_period" value="1" <c:if test="${comm.graph_period==1}" >checked</c:if> ><label><spring:message code="graph.day"/></label> 
										<input type="radio" id="dayPeriod" name="graph_period" value="2" <c:if test="${comm.graph_period==2}" >checked</c:if> ><label><spring:message code="graph.week"/></label> 
										<input type="radio" id="weekPeriod" name="graph_period" value="3" <c:if test="${comm.graph_period==3}" >checked</c:if> ><label><spring:message code="global.month"/></label>
										<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="./img/ico_square.gif">&nbsp;Function :&nbsp;</b>			
										<input type="radio" id="last_consolFun" name="consolFun" value="LAST" <c:if test="${comm.consolFun=='LAST'}" >checked</c:if> ><label>Last</label>&nbsp;&nbsp; 
										<input type="radio" id="avg_consolFun" name="consolFun" value="AVERAGE" <c:if test="${comm.consolFun=='AVERAGE'}" >checked</c:if> ><label>Avg</label>&nbsp;&nbsp; 
										<input type="radio" id="total_consolFun" name="consolFun" value="TOTAL" <c:if test="${comm.consolFun=='TOTAL'}" >checked</c:if> ><label>Total</label>&nbsp;&nbsp;
										<b>&nbsp;&nbsp; by :&nbsp;</b>
										<input type="radio" id="secStep" name="graph_step" value="sec" <c:if test="${comm.graph_step=='sec'}" >checked</c:if> ><label>10sec</label>&nbsp;&nbsp; 
										<input type="radio" id="minStep" name="graph_step" value="min" <c:if test="${comm.graph_step=='min'}" >checked</c:if> ><label>1min</label>&nbsp;&nbsp; 
										<input type="radio" id="5minStep" name="graph_step" value="5min" <c:if test="${comm.graph_step=='5min'}" >checked</c:if> ><label>5min</label>&nbsp;&nbsp; 
										<input type="radio" id="30minStep" name="graph_step" value="30min" <c:if test="${comm.graph_step=='30min'}" >checked</c:if> ><label>30min</label>&nbsp;&nbsp; 
										<input type="radio" id="hourStep" name="graph_step" value="hour" <c:if test="${comm.graph_step=='hour'}" >checked</c:if> ><label>1hour</label>&nbsp;&nbsp;&nbsp;&nbsp;
								</fieldset>
								<fieldset class="total_graph">
										<b><img src="./img/ico_square.gif">  <spring:message code="graph.selectGraphType"/> :  </b><input type="radio" id="dstype" name="dstype" value="cur" <c:if test="${'cur'==comm.dstype}" >checked</c:if> />  difference &nbsp;&nbsp;
										<input type="radio" id="dstype" name="dstype" value="acc" <c:if test="${'acc'==comm.dstype}" >checked</c:if> />  accumulation &nbsp;&nbsp;
								</fieldset>
							</form>
							<div class="graph_area_graph" id="graph_area_graph">
							</div>
					</div>	
			</c:when>
    	<c:when test="${gubun == 'DB' && 'acc'==comm.dstype}">	
					<div id="content">
							<form method="post" name="totalGraphForm" id="totalGraphForm">
							<input type="hidden" id="dsname" name="dsname" value="${comm.dsname}"/>
							<input type="hidden" id="consolFun" name="consolFun" value="${comm.consolFun}"/>
							<input type="hidden" id="sortItem" name="sortItem" value="${comm.sortItem}"/>
								<fieldset class="total_graph">
									<b><img src="./img/ico_square.gif">  <spring:message code="graph.choosePeriodOfGraph"/> :  </b>						
										<input type="radio" id="minPeriod" name="graph_period" value="1" <c:if test="${comm.graph_period==1}" >checked</c:if> ><label><spring:message code="graph.day"/></label> 
										<input type="radio" id="dayPeriod" name="graph_period" value="2" <c:if test="${comm.graph_period==2}" >checked</c:if> ><label><spring:message code="graph.week"/></label> 
										<input type="radio" id="weekPeriod" name="graph_period" value="3" <c:if test="${comm.graph_period==3}" >checked</c:if> ><label><spring:message code="global.month"/></label>
										<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="./img/ico_square.gif">&nbsp;Function &nbsp;</b>	
										<b>by :&nbsp;</b>		
										<input type="radio" id="secStep" name="graph_step" value="sec" <c:if test="${comm.graph_step=='sec'}" >checked</c:if> ><label>10sec</label>&nbsp;&nbsp; 
										<input type="radio" id="minStep" name="graph_step" value="min" <c:if test="${comm.graph_step=='min'}" >checked</c:if> ><label>1min</label>&nbsp;&nbsp; 
										<input type="radio" id="5minStep" name="graph_step" value="5min" <c:if test="${comm.graph_step=='5min'}" >checked</c:if> ><label>5min</label>&nbsp;&nbsp; 
										<input type="radio" id="30minStep" name="graph_step" value="30min" <c:if test="${comm.graph_step=='30min'}" >checked</c:if> ><label>30min</label>&nbsp;&nbsp; 
										<input type="radio" id="hourStep" name="graph_step" value="hour" <c:if test="${comm.graph_step=='hour'}" >checked</c:if> ><label>1hour</label>&nbsp;&nbsp;&nbsp;&nbsp;
								</fieldset>
								<fieldset class="total_graph">
										<b><img src="./img/ico_square.gif">  <spring:message code="graph.selectGraphType"/> :  </b><input type="radio" id="dstype" name="dstype" value="cur" <c:if test="${'cur'==comm.dstype}" >checked</c:if> />  difference &nbsp;&nbsp;
										<input type="radio" id="dstype" name="dstype" value="acc" <c:if test="${'acc'==comm.dstype}" >checked</c:if> />  accumulation &nbsp;&nbsp;
								</fieldset>
							</form>
							<div class="graph_area_graph" id="graph_area_graph">
							</div>
					</div>	
			</c:when>
			 <c:otherwise>
					<div id="content">
							<form method="post" name="totalGraphForm" id="totalGraphForm">
							<input type="hidden" id="dsname" name="dsname" value="${comm.dsname}"/>
							<input type="hidden" id="sortItem" name="sortItem" value="${comm.sortItem}"/>
								<fieldset class="total_graph">
									<b><img src="./img/ico_square.gif">  <spring:message code="graph.choosePeriodOfGraph"/> :  </b>						
										<input type="radio" id="minPeriod" name="graph_period" value="1" <c:if test="${comm.graph_period==1}" >checked</c:if> ><label><spring:message code="graph.day"/></label> 
										<input type="radio" id="dayPeriod" name="graph_period" value="2" <c:if test="${comm.graph_period==2}" >checked</c:if> ><label><spring:message code="graph.week"/></label> 
										<input type="radio" id="weekPeriod" name="graph_period" value="3" <c:if test="${comm.graph_period==3}" >checked</c:if> ><label><spring:message code="global.month"/></label>
										<b>&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<img src="./img/ico_square.gif">&nbsp;Function :&nbsp;</b>			
										<input type="radio" id="last_consolFun" name="consolFun" value="LAST" <c:if test="${comm.consolFun=='LAST'}" >checked</c:if> ><label>Last</label>&nbsp;&nbsp; 
										<input type="radio" id="avg_consolFun" name="consolFun" value="AVERAGE" <c:if test="${comm.consolFun=='AVERAGE'}" >checked</c:if> ><label>Avg</label>&nbsp;&nbsp; 
										<input type="radio" id="total_consolFun" name="consolFun" value="TOTAL" <c:if test="${comm.consolFun=='TOTAL'}" >checked</c:if> ><label>Total</label>&nbsp;&nbsp;
										<b>&nbsp;&nbsp; by :&nbsp;</b>		
										<input type="radio" id="secStep" name="graph_step" value="sec" <c:if test="${comm.graph_step=='sec'}" >checked</c:if> ><label>10sec</label>&nbsp;&nbsp; 
										<input type="radio" id="minStep" name="graph_step" value="min" <c:if test="${comm.graph_step=='min'}" >checked</c:if> ><label>1min</label>&nbsp;&nbsp; 
										<input type="radio" id="5minStep" name="graph_step" value="5min" <c:if test="${comm.graph_step=='5min'}" >checked</c:if> ><label>5min</label>&nbsp;&nbsp; 
										<input type="radio" id="30minStep" name="graph_step" value="30min" <c:if test="${comm.graph_step=='30min'}" >checked</c:if> ><label>30min</label>&nbsp;&nbsp; 
										<input type="radio" id="hourStep" name="graph_step" value="hour" <c:if test="${comm.graph_step=='hour'}" >checked</c:if> ><label>1hour</label>&nbsp;&nbsp;
								</fieldset>
							</form>
							<div class="graph_area_graph" id="graph_area_graph">
							</div>
					</div>	
			 </c:otherwise>
		</c:choose>
	</div>
</body>
</html>
