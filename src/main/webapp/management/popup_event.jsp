<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
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
<style type="text/css">
.popup_close {float:right; margin-bottom:10px;}
</style>
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title><spring:message code="main.title"/></title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
<%@ include file="./manageCommon.jsp" %>
<%@ include file="./jquery-dynamicCode.jsp" %>
<script type="text/javascript">
    String.prototype.trim = function() {    
           return this.replace(/(^\s*)|(\s*$)/gi, "");
     }
</script>
<script type="text/javascript">
var mainRefreshPeriodMinute = 0;

$(document).ready( function() {
	
		$('#pop_close').click(function (event) {
			window.parent.$.smartPop.close();
		});	
	
    var oTable;

    oTable = $('#list').dataTable( {
	
		"bLengthChange": false,
		"bFilter": false,
		"bSort": false,
		"bInfo": false,
		"bAutoWidth": false,
		"iDisplayLength": 13 ,
		"sScrollY" : "300px",
		"bPaginate": false,
		"sServerMethod" : "POST",		
		"bProcessing":true,
        "bServerSide":true,
        "bRetrieve":true,
        "cache": false,
    	"sAjaxSource":'popup_event_list.do?${dataToSend}',
		"aoColumns": [
			{
				"sClass": "tableFiguresLeft", "fnRender": function ( oObj ) {
            		var str = groupCodeTable.getItem(oObj.oSettings.fnFormatNumber( parseFloat( oObj.aData.groupCode ) ));
            		return !str?"&nbsp;":str;
            	}
            },
            {
            	"sClass": "tableFiguresLeft", "fnRender": function ( oObj ) {
            		var str = deviceCodeTable.getItem(oObj.oSettings.fnFormatNumber( parseFloat( oObj.aData.deviceCode ) ));
            		return !str?"&nbsp;":str;
            	}
            },
            {
            	"sClass": "tableFiguresLeft", "fnRender": function ( oObj ) {
            		var str = eventCodeTable.getItem(oObj.aData.type);
            		return !str?"&nbsp;":str;
            	}
            },
            {
            	"fnRender": function ( oObj ) {
            		var str = unitCodeTable.getItem(oObj.aData.unit);
            		return !str?"&nbsp;":str;
            	}
            },
            {
            	"sClass":"tableFigures", "fnRender": function ( oObj ) {
            		if(oObj.aData.unit=='seconds'){
	            		return oObj.oSettings.fnFormatNumber( microSecondsFormat(parseFloat( oObj.aData.criticalvalue )) );
            		}else{
	            		return oObj.oSettings.fnFormatNumber( parseFloat( oObj.aData.criticalvalue ) );
            		}
            	}
            },
            {
            	"sClass":"tableFigures", "fnRender": function ( oObj ) {
            		if(oObj.aData.unit=='seconds'){
	            		return oObj.oSettings.fnFormatNumber( microSecondsFormat(parseFloat( oObj.aData.warningvalue )) );
            		}else{
	            		return oObj.oSettings.fnFormatNumber( parseFloat( oObj.aData.warningvalue ) );
            		}
            	}
            },
            {
            	"sClass":"tableFigures rb", "fnRender": function ( oObj ) {
            		if(oObj.aData.unit=='seconds'){
	            		return oObj.oSettings.fnFormatNumber( microSecondsFormat(parseFloat( oObj.aData.infovalue )) );
            		}else{
	            		return oObj.oSettings.fnFormatNumber( parseFloat( oObj.aData.infovalue ) );
            		}
            	}
            }
        ]
    	
    });
    
 	$('#bind_event').click( function() {//listForm 이미지 버튼 클릭
 		var groupText = document.getElementById("groupText").value;
 		groupText = groupText.trim();
 		if(groupText!=""){
 			if(!isNaN(groupText)){
 				alert("<spring:message code='event.noanynumber'/>");
 				document.getElementById("groupText").value = "";
 			}else{
 				checkGroupText();
 			}
 		}else{
 			alert("<spring:message code='event.noeventname'/>");
 		}
 	});
 	
	$('#listForm').keypress(function(e){
		var key = e.which || e.keyCode;
		if(key == 13){
			return false;
		}
	});
    jQuery.fn.dataTableExt.oPagination.iFullNumbersShowPages = 10;
});



function init(){
    var groupName = '<c:out value="${groupName }"></c:out>';
    //alert("groupName: "+groupName);
	if("${dataToSend}"==null || "${dataToSend}"==""){
		//window.parent.$.smartPop.close();
		window.parent.location.href = "listManagement.do?dival=2&groupSelect="+groupName;
	}
}

function checkGroupText(){
	var groupText = document.getElementById("groupText").value;
	$.ajax({
		type:'POST',
		url : './popup_event_count.do',
		dataType:'html',
		data:"groupBind="+groupText,
		success:function(json){
			var data = JSON.parse(json);
			if(data.count!=0){
				var groupText = document.getElementById("groupText");
				alert("<spring:message code='event.alreadyexist'/>");
				groupText.value = "";
				groupText.focus();
			}else{
				document.listForm.action="popup_event.do";
		 		 document.listForm.submit();
			}
		},
		error:function(xhr,textStatus, errorThrown){
			alert("Server is not responding. Please try again.");
		}
	});
}
</script>
</head>

<body onload='init()'>    
	<div id="group_header" style="border-top:5px solid #36689B; margin-bottom:10px;"></div>
	<div style="margin-bottom:20px;">
	<a href="#" id="pop_close" class="popup_close"><img src="./img/ico_x.gif" width="27" height="21" alt="X"></a>
	<h2 style="color:black;"><spring:message code="event.groupingevent" /></h2>
	</div>
	<form name="listForm" id="listForm">
	<input type="hidden" id="Lst" name="Lst" value="${dataToSend}">
				<table id="list" border="0" class="tb_list_07_1" summary="테이블 07_1">
					<caption>임계값 관리</caption>
					<colgroup>
						<col width="50">
						<col width="120">
						<col width="170">
						<col width="30">
						<col width="40">
						<col width="40">
						<col width="40">
					</colgroup>
					<thead>
					<tr>
						<th rowspan="2"><spring:message code="common.group"/></th>
						<th rowspan="2"><spring:message code="common.daemon"/></th>
						<th rowspan="2"><spring:message code="common.criticaltype"/></th>
						<th rowspan="2"><spring:message code="critical.unit"/></th>
						<th colspan="3"><spring:message code="common.criticalvalue"/></th>
						</tr>
						<tr>
						<th class="noborder"><spring:message code="common.danger"/></th>
						<th class="noborder"><spring:message code="common.warning"/></th>
						<th><spring:message code="critical.normal"/></th>
					</tr>
					</thead>
					<tbody>
						<tr>
							<td colspan="8" class="dataTables_empty">Loading data from server</td>
						</tr>
					</tbody>
				</table>
				<div style="padding-top:10px;">
				<div class="pagtext" id="divText"><spring:message code="event.eventgroupname" /> : <input type="text" id="groupText" name="groupText" style="margin-right:5px"></div>
				<div class="bind_event_btn" id="divBtn"><a href="#"><img src="./img/btn_event_group_reg<spring:message code="common.img"/>.gif" id="bind_event" name="bind_event"></a></div>
				</div>
			</form>
</body>
</html>