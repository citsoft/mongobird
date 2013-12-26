<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>  
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
<title>Command</title>
<%if(session.getAttribute("loginAuth").equals(1)||session.getAttribute("loginAuth").equals(2)){}else{%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common_analytics.js"></script>
<%}%>
<style type="text/css">
/* Default Type Selector */
*{ margin:0; padding:0; font-size:12px; font-family:돋움, Dotum, 굴림, Gulim, AppleGothic, Sans-serif;}
a{ color:#004790;}
img,
fieldset{ border:none;}
legend, caption { display:none;}
em{ font-style:normal; color:#258102;}
strong{ color:#258102;}
li{ list-style:none;}
h1 {width:885px; margin:39px 0 0 50px; padding-bottom:25px; border-bottom:1px solid #AEAEAE; font-size:17px;}
h2 {margin:25px 0 0 36px; padding-left:10px; background:url(./img/ico_dot.gif) 0 4px no-repeat; font-size:11px; font-weight:normal; color:#444;}

/* Layout */
#wrap{position:relative; overflow:hidden;}
#header{border-top:8px solid #F265CE;}
#content{position:relative; padding:0; margin:0;}


.w425 { width:890px;}
.popup_close {position:absolute; top:-70px; right:14px;}
.event_frm {float:left; width:890px; margin:10px 0 0 52px; font-size:11px;}
.event_frm textarea {width:323px; padding:5px; height:168px; border:0; background:#F7F7F7;}
.event_frm2 {text-align:right; width:800px; margin:5px 0 10px 0; font-size:11px;}
.btn_area {margin:10px 0 0 0; width:342px; vertical-align:middle;}
.btn_area02 a {display:inline-block; width:330px; text-align:right;}

.tb_list {margin:0; width:800; border-collapse:collapse;background:#8256B6; border-bottom:2px solid #8256B6;word-wrap:break-all;}
.tb_list thead {height:26px; text-align:center; color:#FFF;}
.tb_list thead tr th {height:26px; line-height:26px; border-right:1px solid #673FA6;}
.tb_list tbody td {background:#fff; border-bottom:1px solid #DFDFDF; text-align:left; color:#4C4C4C; border-right:1px solid #EFEFEF; border-left:1px solid #EFEFEF;}
</style>
<script type="text/javascript" src="./js/jquery-latest.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/commonUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/json2table.js"></script>
<script type="text/javascript">

	function j2t(divId, jsonValue) {
		var div = document.getElementById(divId);
		div.innerHTML = "";
		var jsonObj;
		eval("jsonObj="+jsonValue);
		var before = new Date();
		jsonDisplay(jsonObj);
		var after = new Date();
		div.appendChild(document.createTextNode((after-before)+"ms"));
	}
	
	function goConfirm(){
		$.ajax({
    		type:'POST',
    		url : 'runCommamd.do',
    		data : {"deviceCode" : $('#deviceCode').val(), "memo" : $('#memo').val()},
    		dataType:'html',
    		success:function(json,textStatus){
    			deleteTable();
    			showTables();
    			j2t('jsontable', json);
    		},
    		error:function(xhr,textStatus, errorThrown){
    			
    		}
    	});
	}
	
	function showTables(){
		$('#myTable_title, #myTable').show();
	}
	
	function deleteTable(){
		 $('#myTable tbody tr td').parent().remove();
	}
	
	function init(){
		$('#myTable_title, #myTable').hide();
	}
</script>
<%@ include file="./management/CodeUtil.jsp" %>
</head>
<!-- Document Size : 359*311 -->
<body onLoad="init()">
<div id="wrap" class="w425">
	<div id="header">
		<h1><script>document.write(deviceCodeTable.getItem('${comm.deviceCode}'));</script> <spring:message code="cmd.execute"/></h1>
	</div>
	<div id="content">
		<a href="#" onclick="self.close()" class="popup_close"><img src="./img/ico_x.gif" width="27" height="21" alt="X"></a>
		<div class = event_frm><a href="http://www.mongodb.org/display/DOCS/List+of+Database+Commands" target="_blank">» <spring:message code="cmd.cmdList"/></a></div>
		<div class=event_frm>
			<form method="post" name="confirmForm" id = "confirmForm">
			<input type="hidden" id="deviceCode" name="deviceCode" value="${comm.deviceCode}"/>
			<fieldset>
			<legend>Command</legend>
			<textarea cols="35" rows="10" name="memo" id="memo"></textarea>
			<div class="btn_area"></div>
			<div class="btn_area02">
				<a href="#"><img onClick='goConfirm()' src="./img/btn_confirm.gif" width="42" height="20" alt="OK"></a>
			</div>
			</fieldset> 
			</form>
		</div>
		<div class=event_frm>
			<div id="jsontable" class="event_frm2"></div>
			<table id="myTable_title" class="tb_list">
				<thead>
					<tr>
						<th width="800"><spring:message code="cmd.response"/></th>
					</tr>
				</thead>
			</table>
			
			<table id="myTable" class="tb_list">
				<tbody>
					<tr>
					</tr>
				</tbody>
			</table>
			<div><br></div>
		</div>
	</div>
</div>
</body>
</html>
