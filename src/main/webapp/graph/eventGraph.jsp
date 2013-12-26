<%@ page language="java" contentType="text/html; charset=utf-8"%>

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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.showLoading.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-latest.js"></script>
<%@ include file="../management/CodeUtil.jsp" %>
<script type="text/javascript">
var pwd = "${pageContext.request.contextPath}";
var imgLang = "<spring:message code="common.img"/>";
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/dateUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/commonUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.showLoading.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/sub_alarm_graph.js"></script>
<script type="text/javascript">
$(document).ready(function() {
	
// 	document.getElementById("groupIdx").value = parent.g_group_idx;
	
	var img = '<spring:message code="common.img"/>';
    var strHtml = "<ul><li class=\"end\"><b><img src=\"./img/event_sub01"+img+".png\"></b></li>";
    if(parent.g_title.length != 0){
        for(var i =0; i<parent.g_title.length; i++){
            var str = "<li><b><input type=\"image\" src=\"./img/ico_square.gif\">  " +parent.g_title[i]+ " : </b>" +parent.g_value[i]+ "</li>";
            strHtml += str;
        }
    }else{
        for(var i =0; i<parent.g_title_sub.length; i++){
            var str = "<li><b><input type=\"image\" src=\"./img/ico_square.gif\">  " +parent.g_title_sub[i]+ " : </b>" +parent.g_value_sub[i]+ "</li>";
            strHtml += str;
        }
    }
    
    strHtml += "</ul>";
    $('#menu_graph').html(strHtml);
    goGraph();
    
// 	if('${comm.deviceCode}' == '-1'){
// 		var answer = confirm("<spring:message code='event.nomoreinstance' />");
// 		if(answer){
// 			deleteSubAlarm();
// 		}
// 	}else{
		
// 	}
});

$(function(){
	$('input[type=radio]').click(function (event) {
		getDbLst();
	});
});

function reload(){
	alert("test");
// 	parent.document.
}
</script>
</head>
<body>
	<div id="content_graph2"></div>
	<form method="post" name="frm_demon" id="frm_demon" onsubmit="reload()">
		<input type="hidden" id="sdate" name="sdate" value="${comm.sdate}" />
		<input type="hidden" id="edate" name="edate" value="${comm.edate}" />
		<input type="hidden" id="deviceCode" name="deviceCode" value="${comm.deviceCode}"/>	
		<input type="hidden" id="selectDate" name="selectDate"  value="${comm.selectDate}"/>	
		<input type="hidden" id="selectHour" name="selectHour"  value="${comm.selectHour}"/>
		<input type="hidden" id="selectMin" name="selectMin"  value="${comm.selectMin}"/>
		<input type="hidden" id="type_gubun" name="type_gubun" value="event"/>
		<input type="hidden" id="sortItem" name="sortItem" value="event"/>
		<input type="hidden" id="graph_period" name="graph_period" value="2h"/>
<!-- 		<input type="hidden" id="groupIdx" name="groupIdx" value=""/> -->
		<div id="selectLstFrame"></div>
			<div id="menu_graph">
			</div>
			<div id="contents_graph">
				<b><input type="image" src="./img/ico_square.gif">&nbsp;Function :&nbsp;</b>			
				<input type="radio" id="last_consolFun" name="consolFun" value="LAST" <c:if test="${comm.consolFun=='LAST'}" >checked</c:if> ><label>Last</label>&nbsp;&nbsp; 
				<input type="radio" id="avg_consolFun" name="consolFun" value="AVERAGE" <c:if test="${comm.consolFun=='AVERAGE'}" >checked</c:if> ><label>Avg</label>&nbsp;&nbsp; 
				<input type="radio" id="total_consolFun" name="consolFun" value="TOTAL" <c:if test="${comm.consolFun=='TOTAL'}" >checked</c:if> ><label>Total</label>&nbsp;&nbsp;
							<b>&nbsp;&nbsp; by :&nbsp;</b>		
							<input type="radio" id="secStep" name="graph_step" value="sec" <c:if test="${comm.graph_step=='sec'}" >checked</c:if> ><label>10sec</label>&nbsp;&nbsp; 
							<input type="radio" id="minStep" name="graph_step" value="min" <c:if test="${comm.graph_step=='min'}" >checked</c:if> ><label>1min</label>&nbsp;&nbsp; 
							<input type="radio" id="hourStep" name="graph_step" value="hour" <c:if test="${comm.graph_step=='hour'}" >checked</c:if> ><label>1hour</label>&nbsp;&nbsp;
				<div class="graph_area_event_graph" id="graph_area_event_graph"></div>
			</div>
	</form>
</body>
</html>
