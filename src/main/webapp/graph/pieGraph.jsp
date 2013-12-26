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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/daemonGraph.css">
	<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.flot.pie.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-latest.js"></script>
	<!--[if lte IE 8]><script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/js/excanvas.min.js"></script><![endif]-->
    <script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-latest.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/commonUtil.js"></script>
	<script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.flot.min.js"></script>
    <script language="javascript" type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.flot.pie.js"></script>
    <style type="text/css">
   .demon_detail2 {width:630px; border-top:2px solid #444;}
   .demon_detail2 div.post_name {width:310px; margin:5px 0 5px 0px; float:left;}
		.demon_detail2 div.post_icon {width:320px; margin:5px 0 5px 0px; text-align:right; float:left;}
    </style>
<script type="text/javascript">

$(document).ready( function() {
	if('${comm.enlarge}' == 'enlarge')$("#chartcontainer").attr('style','margin-top:20px;width: 700px;height: 400px;');
		setGraph();

		$('#enlargeView').click(function(){
			goEnlargePieGraph('${comm.deviceCode}');
		});
		
		$('#pop_close').click(function (event) {
			window.parent.$.smartPop.close();
		});	
});

function goEnlargePieGraph(deviceCode){
	var paramStr = "?deviceCode="+deviceCode+"&enlarge=enlarge";
	var urlStr = "pieGraphView.do"+paramStr;
	parent.$.smartPop.open({ background: "gray", width: 900, height: 550, url: urlStr });			
}

function setGraph(){
	$.ajax({
		type:'POST',
		url : 'pieGraphData.do?deviceCode=${comm.deviceCode}',
		dataType:'html',
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ')');
			pie(jsonData.aaData);
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});	
}

function pie(data){
	$.plot($("#chartcontainer"), data, 
			{
				series: {
					pie: { 
						show: true,
						radius: 1,
						label: {
							show: true,
							radius: 3/4,
							formatter: function(label, series){
								return '<div style="font-size:8pt;text-align:center;padding:2px;color:white;">'+label+'<br/>'+decimal(series.percent)+'%</div>';
							},
							background: { 
								opacity: 0.5,
								color: '#000'
							},
							threshold:0.001 //값이 0.01이면,  1%미만의 데이터는 라벨을 표시하지 않음. 여기서는 0.001이니까 0.1%미만의 데이터는 표시하지 않음
						}
					}
				},
				legend: {
					show: true
				}
			});
}
</script>
</head>
<body>
			<c:if test="${comm.enlarge != 'enlarge'}">
				<div class="demon_detail2">
					<div class="post_name">
						<img src="./img/daemon_subtit02<spring:message code="common.img"/>.png">
					</div>
					<div class="post_icon">
						<a id="enlargeView"><img src="./img/search_icon.png" width="19" height="19" class="petit_icon"></a>
					</div>
					<div id="chartcontainer"class="graph" style="width: 500px;height: 170px;"></div>
				</div>
				</c:if>
				<c:if test="${comm.enlarge == 'enlarge'}">
				 <div class="demon_detail">
						<h3>
							<img src="./img/daemon_subtit02<spring:message code="common.img"/>.png">
						</h3>
						<a href="#" id="pop_close" class="popup_close" style="margin:10px;"><img src="./img/ico_x.gif" width="27" height="21" alt="X"></a>
						<div id="chartcontainer"class="graph" style="width: 500px;height: 170px;"></div>
				 </div>			
				</c:if>
<!-- 		</div> -->
</body>
</html>
