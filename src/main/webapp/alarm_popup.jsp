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
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="main.title"/></title>
<link rel="stylesheet" href="/tetrad/css/nanum.css">
<link rel="stylesheet" href="/tetrad/css/lato.css">
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
#table_area .dataTables_wrapper { position:relative; padding-left:10px; width:250px; float:left; padding-top:20px;}
#list td.tableFiguresLeft {width:240px; padding:5px; border-bottom:1px dotted #cfcfcf; list-style:none; margin:0;}
#list_paginate{text-align:center;}
#contents { position:relative; padding:20px; width:400px; float:left; background:url(../img/bg_graph_bar.gif) top left no-repeat; background-size:1px 100%;}
#menu_table {position:relative; float:left;}

.event_frm {margin:10px 0 0 52px; font-size:11px;}
.event_frm textarea {width:350px; margin-top:7px; padding:5px; height:245px; border:0; background:#F7F7F7;}
.btn_area {margin:10px 0 0 0; width:342px; vertical-align:middle;}
.btn_area02 a {display:inline-block; width:360px; text-align:right;}

#list_paginate {margin:5px 0 10px 0; text-align:center;}
#list_paginate #list_first,#list_paginate #list_previous,#list_paginate #list_next,#list_paginate #list_last {cursor:pointer;display:-moz-inline-box;display:inline-block;_position:relative;margin-left:7px;margin-right:7px;padding:0 0 0 2px;color:#676767;font-family:'돋움', Dotum;font-size:11px;font-weight:bold;line-height:17px;text-decoration:none !important;}
#list_paginate #list_first.paginate_button_disabled,#list_paginate #list_previous.paginate_button_disabled,#list_paginate #list_next.paginate_button_disabled,#list_paginate #list_last.paginate_button_disabled {cursor:auto;display:-moz-inline-box;display:inline-block;_position:relative;margin-left:7px;margin-right:7px;padding:0 0 0 2px;color:#676767;font-family:'돋움', Dotum;font-size:11px;font-weight:bold;line-height:17px;text-decoration:none !important;}
#list_paginate span span.paginate_button {cursor:pointer;display:inline-block; padding:0 7px 0 7px;  color:#676767; font-family:'돋움', Dotum; font-size:11px; font-weight:bold; line-height:normal;}
#list_paginate span span.paginate_active {cursor:auto;display:-moz-inline-box; display:inline-block; _position:relative; padding:0 7px 0 7px; border:1px solid #E742A8; color:#535353; font-family:'돋움', Dotum; font-size:11px; font-weight:bold;line-height:17px; text-decoration:none !important}

<% String lo = request.getHeader("accept-language"); %>

h1.top_menu{
	<%
	if(!lo.startsWith("ko")){
%>
	font-family: 'Lato Reg';  
<%}else{%>
	font-family: 'Nanum Reg'; 
<%}%>
		font-size: 1.5em;
		font-weight: bold;
		padding-top: 10px;
		text-align: left;
		color: black;
}
</style>
<%@ include file="./management/CodeUtil.jsp" %>
<script type="text/javascript" src="./js/jquery-latest.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dataTables.js"></script>
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


	var titlelength = parent.g_title.length;
	var img = '<spring:message code="common.img"/>';
	if(titlelength > 10){
		
		var strHtml = "<ul><li class=\"end\"> &nbsp;</li> ";
		for(var i =0; i<5; i++){
			var str = "";
			str += "<li><b><input type=\"image\" src=\"./img/ico_square.gif\">  " +parent.g_title[i]+ " : </b>" +parent.g_value[i]+ "</li>";
			strHtml += str;
		}
		strHtml += "</ul>";
		$('#menu').html(strHtml);
		
		var oTable;
		 
	    oTable = $('#list').dataTable( {
			//테이블의 페이징 검색 등 부가적인 기능 생략
			
			"bPaginate": true,
			"sPaginationType": "full_numbers",
			"oLanguage" :{
				"oPaginate":{
					"sFirst": " <img src=\"./img/img_pre_end.gif\" width=\"11\" height=\"12\" border=\"0\" alt=\"처음\"/ class=\"textmiddle\"> ",
					"sPrevious": " <img src=\"./img/img_pre.gif\" width=\"11\" height=\"12\" border=\"0\" alt=\"이전\"/ class=\"textmiddle\"> ",
					"sNext": " <img src=\"./img/img_next.gif\" width=\"11\" height=\"12\" border=\"0\" alt=\"다음\"/ class=\"textmiddle\"> ", 
					"sLast": " <img src=\"./img/img_next_end.gif\" width=\"11\" height=\"12\" border=\"0\" alt=\"끝\"/ class=\"textmiddle\">"
				}
			},
			"bLengthChange": false,
			"bFilter": false,
			"bSort": false,
			"bInfo": false,
			"bAutoWidth": true,
			"iDisplayLength": 5 ,		
	   "aaData": JSON.parse(parent.json_str),
			"aoColumns": [
	            {
	            	"sClass": "tableFiguresLeft", "fnRender" : function(oObj){
	            		return "<input type=\"image\" src=\"./img/ico_square.gif\">&nbsp;"+oObj.aData;
	            	} 
	            }
	        ]
	    });
	}else{
		var strHtml = "<ul><li class=\"end\"> &nbsp;</li> ";
		var titlelength = parent.g_title.length;
		for(var i =0; i<titlelength; i++){
			var str = "";
			if(titlelength>10 && i == 3) str += "<div style='overflow-x:hidden; overflow-y:auto; width:100%; height:125px;'>" ; 
			str += "<li><b><input type=\"image\" src=\"./img/ico_square.gif\">  " +parent.g_title[i]+ " : </b>" +parent.g_value[i]+ "</li>";
			if(titlelength>10 && i == titlelength-2) str = "</div>";
			strHtml += str;
		}
		strHtml += "</ul>";
		$('#menu').html(strHtml);
	}
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
			window.parent.goSearch();
			window.parent.$.smartPop.close();
		}
	}
	
</script>
</head>
<!-- Document Size : 359*311 -->
<body onload='init()'>
<div id="wrap" class="w425">
	<div id="header">
		<h1 class="top_menu"><spring:message code="event.information" /></h1>
	</div>
	<div id="content">
		<a href="#" id="pop_close" class="popup_close"><img src="./img/ico_x.gif" width="27" height="21" alt="X"></a>
		<div class="event_frm">
		<div id="menu_table">
			<div id="menu">
			</div>
			<div id = "table_area"style="width:250px;">
				<table id="list" name="list"></table>
			</div>
			<div class="paginav" id="divPaging"></div>
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
