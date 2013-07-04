<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="main.title"/></title>
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
h1 {width:660px; margin:39px 0 0 50px; padding-bottom:25px; border-bottom:1px solid #AEAEAE;}
h2 {margin:25px 0 0 36px; padding-left:10px; background:url(./img/ico_dot.gif) 0 4px no-repeat; font-size:11px; font-weight:normal; color:#444;}
a.deco_none { text-decoration: none;}
/* Layout */
#wrap{position:relative; overflow:hidden;}
#header{border-top:8px solid #F265CE;}
#content{position:relative; padding:0; margin:0;}

.w425 { width:760px; }
.popup_close {position:absolute; top:-70px; right:14px;}

#menu { position:relative; padding-left:10px; width:250px; float:left; }
#menu ul { margin:0; padding:0}
#menu li { width:240px; padding:5px; border-bottom:1px dotted #cfcfcf; list-style:none}
#menu li img {vertical-align:middle; }
#menu li.end { width:240px; padding:5px; border-bottom:2px solid #cfcfcf; list-style:none;font-size:14px;}
#contents { position:relative; padding:20px; width:400px; float:left; background:url(../img/bg_graph_bar.gif) top left no-repeat; background-size:1px 100%;}

.event_frm {margin:10px 0 0 52px; font-size:11px;}
.event_frm textarea {width:350px; margin-top:7px; padding:5px; height:245px; border:0; background:#F7F7F7;}
.btn_area {margin:10px 0 0 0; width:342px; vertical-align:middle;}
.btn_area02 a {display:inline-block; width:350px; text-align:right;}
</style>
<%@ include file="./management/CodeUtil.jsp" %>
<script type="text/javascript" src="./js/jquery-latest.js"></script>
<script>
	function goConfirm(){
		if($('textarea').val()!=""){
			if(document.confirmForm.check.checked){
				document.confirmForm.dival.value="1";
			}else{
				document.confirmForm.dival.value="0";
			}
			document.confirmForm.action="confirmCheck.do";
			document.confirmForm.submit();
		}else{
			alert('<spring:message code="event.enterMessage"/>');
			return false;
		}
	}
	
	$(document).ready( function() {
		var $checks = $('#confirmForm input:checkbox[name=check]');
	    if($checks.is(':checked') === false) {
	        $checks.attr('checked',true);
	    }
	    
		$('#pop_close').click(function (event) {
			window.parent.$.smartPop.close();
		});	

// 		var img = '<spring:message code="common.img"/>';
// 		var strHtml = "<ul><li class=\"end\"><b><img src=\"./img/event_sub01"+img+".png\"></b></li>"; 
// 		for(var i =0; i<parent.g_title.length; i++){
// 			var str = "<li><b><input type=\"image\" src=\"./img/ico_square.gif\">  " +parent.g_title[i]+ " : </b>" +parent.g_value[i]+ "</li>";
// 			strHtml += str;
// 		}
// 		strHtml += "</ul>";
// 		$('#menu').html(strHtml);
	});
	
	function ismaxlength(obj){
		 var mlength=obj.getAttribute? parseInt(obj.getAttribute("maxlength")) : ""
		 if (obj.getAttribute && obj.value.length>mlength) {
		  alert("<spring:message code="event.charLimited"/>");
		  obj.value=obj.value.substring(0,mlength);
		}
	}
	
	function init(){
		if("${comm.message}"!=null && "${comm.message}"!=""){
			alert("<spring:message code="event.confirmedMessage"/>");
			window.parent.location.reload();
			window.parent.$.smartPop.close();
		}
	}
	
	function figureCheck(criType,value,figure,realValue,realFigure ){
//         var value = parseFloat( oObj.aData.cri_value );
//         var figure = parseFloat( oObj.aData.figure );
//         var realValue = parseFloat( oObj.aData.real_cri_value );
//         var realFigure = parseFloat( oObj.aData.real_figure );
//         var criType = oObj.aData.cri_type;
        var returnValue = "";
        if(criType == "Connection_refused"){
                returnValue = "-";
        }else if(criType == "connections_current"){
                returnValue = decimal(figure)+"/"+decimal(value);
        }else if(criType == "mem_resident"){
                returnValue = decimal(figure)+"/"+decimal(value);
        }else if(criType == "dbDataSize"){
                returnValue = decimal(figure)+"/"+ decimal(value);
        }else if(criType == "diff_extra_info_page_faults"){
                returnValue = exFormat(figure)+"/"+exFormat(value);
        }else if(criType == "diff_globalLock_lockTime" || criType == "diff_locks_timeLockedMicros_R" || criType == "diff_locks_timeLockedMicros_W" || criType == "diff_db_sum_locks_timeLockedMicros_r" || criType == "diff_db_sum_locks_timeLockedMicros_w"){
                returnValue = exFormat(microSecondsFormat(figure))+"/"+ exFormat(microSecondsFormat(value));
        }else{
                returnValue = figure +"/"+ value;
        }
        return returnValue;
	}
	
	function alarmCheck(alarm){
// 		 var alarm = oObj.aData.alarm;
		 var alarmCode = "";
		 if(alarm==0){
			 alarmCode = '<img src="${pageContext.request.contextPath}/img/ico_failure<spring:message code="common.img"/>.gif" height="16" alt="impaired">';
		 }else if(alarm==1){
			 alarmCode = '<img src="${pageContext.request.contextPath}/img/ico_danger<spring:message code="common.img"/>.png" height="16" alt="dnager">';
		 }else if(alarm==2){
			 alarmCode = '<img src="${pageContext.request.contextPath}/img/ico_warning<spring:message code="common.img"/>.gif" height="16" alt="warning">';
		 }else{
			 alarmCode = '<spring:message code="critical.normal"/>';
		 }
		return alarmCode ;
	}
	
	function confirmCheck(confirm){
// 		var confirm = oObj.aData.confirm;
		var confirmCode = "";
		if(confirm==1){
			confirmCode = '<spring:message code="event.confirmed"/>';
		}else{
			confirmCode = '<spring:message code="event.unconfirmed"/>';
		}
		return confirmCode;
	}
</script>
</head>
<!-- Document Size : 359*311 -->
<body onload='init()'>
<div id="wrap" class="w425">
	<div id="header">
		<h1><img src="./img/tit_event<spring:message code="common.img"/>.gif" width="142" height="23" alt="이벤트 처리내역"></h1>
	</div>
	<div id="content">
		<a href="#" id="pop_close" class="popup_close"><img src="./img/ico_x.gif" width="27" height="21" alt="X"></a>
		<div class="event_frm">
			<div id="menu">
				<ul>
					<li class="end"><b><img src="./img/event_sub01<spring:message code="common.img"/>.png"></b></li>
					<li><b><input type="image" src="./img/ico_square.gif">  <spring:message code="common.date"/> : </b>${adto.reg_date}</li>
					<li><b><input type="image" src="./img/ico_square.gif">  <spring:message code="event.firstTime"/> : </b>${adto.reg_time}</li>
					<li><b><input type="image" src="./img/ico_square.gif">  <spring:message code="event.lastTime"/> : </b>${adto.up_time}</li>
					<li><b><input type="image" src="./img/ico_square.gif">  <spring:message code="common.group"/> : </b><script>document.write(groupCodeTable.getItem( parseFloat( '${adto.groupCode}' )));</script></li>
					<li><b><input type="image" src="./img/ico_square.gif">  <spring:message code="common.daemon"/> : </b><script>document.write(deviceCodeTable.getItem( parseFloat( '${adto.deviceCode}' )));</script></li>
					<li><b><input type="image" src="./img/ico_square.gif">  IP : Port : </b>${adto.ip} : ${adto.port}</li>
					<li><b><input type="image" src="./img/ico_square.gif">  <spring:message code="common.criticaltype"/> : </b><script>document.write(eventCodeTable.getItem( '${adto.cri_type}' ));</script></li>
					<li><b><input type="image" src="./img/ico_square.gif">  <spring:message code="common.figure"/> : </b><script>document.write(figureCheck('${adto.cri_type}',parseFloat('${adto.cri_value}'),parseFloat('${adto.figure}'),parseFloat('${adto.real_cri_value}'),parseFloat('${adto.real_figure}')));</script></li>
					<li><b><input type="image" src="./img/ico_square.gif">  <spring:message code="event.event"/> : </b><script>document.write(alarmCheck('${adto.alarm}'));</script></li>
					<li><b><input type="image" src="./img/ico_square.gif">  <spring:message code="event.confirm"/> : </b><script>document.write(confirmCheck('${adto.confirm}'));</script></li>
				</ul>
			</div>
			<div id="contents">
				<form method="post" name="confirmForm" id = "confirmForm">
				<input type="hidden" id="idx" name="idx" value="${comm.idx}"/>
				<input type="hidden" id="dival" name="dival"/>
				<input type="hidden" id="userCode" name="userCode" value="<%=session.getAttribute("loginUserCode")%>"/>
				<fieldset>
				<legend>이벤트 처리내역</legend>
				<textarea cols="35" rows="10" name="memo" id="memo" maxlength="200" onkeyup="javaScript:ismaxlength(this)"></textarea>
				<div class="btn_area">
					<input type="checkbox" name="check"><label> <spring:message code="event.checkItems"/></label>
				</div>
				<div class="btn_area02">
					<a href="#"><img onClick='goConfirm()' src="./img/btn_confirm.gif" width="42" height="20" alt="OK"></a>
				</div>
				</fieldset> 
				</form>
			</div>
		</div>
	</div>
</div>
</body>
</html>
