<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>  
<%@ include file="./management/eventPro.jsp" %>
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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.smartPop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/calendar.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/datatable_paging.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.basic.tooltip.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.showLoading.css">
<%@ include file="./management/CodeUtil.jsp" %>
<%@ include file="./common.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.smartPop.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.basic.tooltip.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.showLoading.js"></script>
<script type="text/javascript">
	var mainRefreshPeriodMinute = ${mainRefreshPeriodMinute};
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/tooltip.js"></script>
<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/paging.js"></script> --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/commonUtil.js"></script>
<script type="text/javascript">
	var now = new Date();
	var year= now.getFullYear();
	var mon = (now.getMonth()+1)>9 ? ''+(now.getMonth()+1) : '0'+(now.getMonth()+1);
	var day = now.getDate()>9 ? ''+now.getDate() : '0'+now.getDate();
	var nowTime=year+"-"+mon+"-"+day;
	
	function goCheck(idx){
		$.smartPop.open({ background: "gray", width: 800, height: 520, url: "confirmView.do?idx="+idx });	
	}
	
	function goCheckGet(idx){
		$.smartPop.open({ background: "gray", width: 800, height: 520, url: "confirmGetView.do?idx="+idx });		
	}
	
	function goSearch(){
		if(nowTime<document.form.sdate.value || nowTime<document.form.edate.value ){
			alert("<spring:message code="event.chooseDate"/>");
			return false;
		}
		if(document.form.sdate.value>document.form.edate.value ){
			alert("<spring:message code="event.chooseTime"/>");
			return false;
		}
		document.form.action="subAlarmView.do";
	    document.form.submit();
	}
	
	$(document).ready( function() {
		$('#eventframe').hide();
		var oTable = $('#list').dataTable( {
			//테이블의 페이징 검색 등 부가적인 기능 생략
			"bPaginate": true,
			"bLengthChange": false,
			"bFilter": false,
			"bSort": false,
			"bInfo": false,
			"bAutoWidth": false,
			"iDisplayLength": 10 ,
			
			"sPaginationType": "full_numbers",
			"oLanguage" :{
				"oPaginate":{
					"sFirst": " <img src=\"./img/img_pre_end.gif\" width=\"11\" height=\"12\" border=\"0\" alt=\"처음\"/ class=\"textmiddle\"> ",
					"sPrevious": " <img src=\"./img/img_pre.gif\" width=\"11\" height=\"12\" border=\"0\" alt=\"이전\"/ class=\"textmiddle\"> ",
					"sNext": " <img src=\"./img/img_next.gif\" width=\"11\" height=\"12\" border=\"0\" alt=\"다음\"/ class=\"textmiddle\"> ", 
					"sLast": " <img src=\"./img/img_next_end.gif\" width=\"11\" height=\"12\" border=\"0\" alt=\"끝\"/ class=\"textmiddle\">"
				}
			},
			"bProcessing":false,
	        "bServerSide":true,
	        "bRetrieve":true,
            "cache": false,
	    	"sAjaxSource":'subAlarmList.do',
			"fnServerParams": function ( aoData ) {
	            aoData.push( 
	            	{"name": "type", "value": '${comm.type}'},
	            	{"name": "alarm", "value": '${comm.alarm}'},
	            	{"name": "groupCode", "value": '${comm.groupCode}'},
	            	{"name": "deviceCode", "value": '${comm.deviceCode}'},
	            	{"name": "confirm", "value": '${comm.confirm}'},
	            	{"name": "sdate", "value": '${comm.sdate}'},
	            	{"name": "edate", "value": '${comm.edate}'}
	            );
			},
			"aoColumns": [
				{ "sClass": "lb", "mDataProp": null },
				{ "mDataProp": "reg_date" }, 
				{ "mDataProp": "reg_time" },         
				{ "mDataProp": "up_time" },                 
				{
					"sClass": "tableFiguresLeft", "fnRender": function ( oObj ) {
	            		var str = groupCodeTable.getItem( parseFloat( oObj.aData.groupCode ) );
	            		return !str?"&nbsp;":str;
	            	}
	            },
	            {
	            	"sClass": "tableFiguresLeft", "fnRender": function ( oObj ) {
	            		var str = deviceCodeTable.getItem( parseFloat( oObj.aData.deviceCode ) );
	            		return !str?"&nbsp;":str;
	            	}
	            },
				{
	            	"fnRender": function ( oObj ) {
	            		return oObj.aData.ip + " : " + oObj.aData.port;
	            	}
	            },
	            {
	            	"sClass": "tableFiguresLeft", "fnRender": function ( oObj ) {
	            		var str = eventCodeTable.getItem( oObj.aData.cri_type );
	            		return !str?"&nbsp;":str;
	            	}
	            },
				{
	            	"sClass": "tableFigures", "fnRender": function ( oObj ) {
                        var value = parseFloat( oObj.aData.cri_value );
                        var figure = parseFloat( oObj.aData.figure );
                        var realValue = parseFloat( oObj.aData.real_cri_value );
                        var realFigure = parseFloat( oObj.aData.real_figure );
                        var criType = oObj.aData.cri_type;
                        var returnValue = "";
                        if(criType == "Connection_refused"){
                                returnValue = "-";
                        }else if(criType == "connections_current"){
                                returnValue = "<a href=\"#\" class=\"deco_none\" title=\""+toolTip_title(realFigure,"")+" / "+toolTip_title(realValue,"")+"\">"
                                					+decimal(figure)+"/"+decimal(value)
                                					+"</a>";
                        }else if(criType == "mem_resident"){
                                returnValue = "<a href=\"#\" class=\"deco_none\" title=\""+toolTip_title(realFigure, "MB")+" / "+toolTip_title(realValue, "MB")+"\">"
                                					+decimal(figure)+"/"+decimal(value)
                                					+"</a>";
                        }else if(criType == "dbDataSize" || criType == "dbFileSize"){
                                returnValue = "<a href=\"#\" class=\"deco_none\" title=\""+sizeFormat(parseFloat(realFigure))+" / "+sizeFormat(parseFloat(realValue))+"\">"
                                					+decimal(figure)+"/"+ decimal(value)
                                					+"</a>";
                        }else if(criType == "diff_extra_info_page_faults"){
                                returnValue = "<a href=\"#\" class=\"deco_none\" title=\""+toolTip_title(realFigure,"")+" / "+toolTip_title(realValue,"")+"\">"
                                					+exFormat(figure)+"/"+exFormat(value)
                                					+"</a>" ;
                        }else if(criType == "diff_globalLock_lockTime" || criType == "diff_locks_timeLockedMicros_R" || criType == "diff_locks_timeLockedMicros_W" || criType == "diff_db_sum_locks_timeLockedMicros_r" || criType == "diff_db_sum_locks_timeLockedMicros_w"){
                                returnValue = "<a href=\"#\" class=\"deco_none\" title=\""+toolTip_title(realFigure,"μs")+" / "+toolTip_title(realValue,"μs")+"\">"
                                					+exFormat(microSecondsFormat(figure))+"/"+ exFormat(microSecondsFormat(value))
                                					+"</a>" ;
                        }else{
                                returnValue = figure +"/"+ value;
                        }
                        return returnValue;
	            	}
	            },
	            {
	            	 "fnRender": function ( oObj ) {
	            		 var alarm = oObj.aData.alarm;
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
	            },
				{
	            	"fnRender": function ( oObj ) {
	            		var confirm = oObj.aData.confirm;
	            		var confirmCode = "";
	            		if(confirm==1){
	            			confirmCode = '<a href="javaScript:goCheckGet('+ oObj.aData.idx +')"><font color="green" style="border-bottom:green 1px solid"><spring:message code="event.confirmed"/></font>	</a>';
	            		}else{
	            			confirmCode = '<a href="javaScript:goCheck('+ oObj.aData.idx +')"><font color="red" style="border-bottom:red 1px solid"><spring:message code="event.unconfirmed"/></font></a>';
	            		}
	            		return confirmCode;
	            	}
	            },
	            {
	            	"sClass": "rb tableFiguresLeft", "fnRender": function ( oObj ) {
	            		var str = userCodeTable.getItem( parseFloat( oObj.aData.userCode ) );
	            		return !str?"&nbsp;":str;
	            	}
	            }
			],
	    	//특정 값 라인 색이 바뀌도록
	    	"fnRowCallback": function( nRow, aData, iDisplayIndex,aaData ) {
				if ( aData.alarm == 0 ){
	    	    	nRow.className = "row_stop_demon_status";
	    	    	$(nRow).children().each(function(){$(this).addClass('row_stop_demon_status');});
				}
		    	return nRow;
    	    },
	    	"fnDrawCallback": function ( oSettings ) {
				setTipByTableName("list");
				var iTotalRecords = oSettings.fnRecordsTotal();
				if(iTotalRecords>0){
					$('table#list td').bind('mouseenter', function () { $(this).parent().children().each(function(){$(this).addClass('row_selected');}); });
				    $('table#list td').bind('mouseleave', function () { $(this).parent().children().each(function(){$(this).removeClass('row_selected');}); });
				}else{
					$('table#list td').eq(0).attr('class', 'lb rb');
				}
				var iLength = oSettings._iDisplayLength;
	    	    $("#list_total").html("<a href='#' onClick='location.reload()'><img src='./img/refresh_icon.png' id='refresh' width='17' height='17'></a> <spring:message code='common.total'/> : "+ iTotalRecords +" <spring:message code='common.totals'/>");
            	if(iTotalRecords <= iLength){
            		$("#list_paginate").hide();
            	}else{
            		$("#list_paginate").show();
            	}
				var iLen=oSettings.aiDisplay.length;
				var iStart = oSettings._iDisplayStart ;
	            
	            /* Need to redo the counters if filtered or sorted */
	            for ( var i=0 ; i<iLen ; i++ ){
					$('td:eq(0)', oSettings.aoData[ oSettings.aiDisplay[i] ].nTr ).html( i+1+iStart );
				}
	        },
	        "aaSorting": [[ 1, 'asc' ]]
	    });
		
		jQuery.fn.dataTableExt.oPagination.iFullNumbersShowPages = 10;
		
		/* Add a click handler to the rows (e.which==1 left click / e.which==3 right click) */
		$("#list tbody").delegate("tr", "mousedown", function(e) {
			g_title = new Array();
			g_value = new Array();

			for(var i=1; i<11;i++){
				var title = $("#list thead tr th:eq(" +i+ ")").text();
				g_title.push(title);
				var value = $("td:eq(" +i+ ")", this).text();
				if(i==9)value = $("td:eq(" +i+ ")", this).html();
				g_value.push(value);
			}

			var position = oTable.fnGetPosition(this); // getting the clicked row position
			if(position!=null){
				var selectDate = oTable.fnGetData(position).reg_date;
				var selectTime = (oTable.fnGetData(position).up_time).split(":");
				var selectHour = selectTime[0];
				var selectMin = selectTime[1];
				var deviceCode = oTable.fnGetData(position).deviceCode;
				
     	$('.row_demon_graph').removeClass('row_demon_graph');
    		$("td", this).addClass('row_demon_graph');
				var param = "?selectDate="+selectDate+"&selectHour="+selectHour+"&selectMin="+selectMin+"&deviceCode="+deviceCode;
				var url = "eventGraphView.do"+param;
				$('#eventframe').attr("src", url);
				$('#eventframe').show();
			}
		});
	});
	
	var g_title;
	var g_value;
	
	function init(sdate,edate){
		document.form.sdate.value=sdate;
		document.form.edate.value=edate;
	}

	function suframeSize(height,size,name){
	     var objFrm=document.getElementById(name);
	     var frameHeight = height + size;
	     if(frameHeight!=0)  {
	      objFrm.style.height=frameHeight + "px";
	     }
	  }
</script>
</head>
<body id="index" onload='init("${comm.sdate}","${comm.edate}")'>
<div id="wrap">
	<!-- header start -->
	<%@ include file="./top_menu.jsp"%>
	<!-- // header end -->
	<!-- container start-->
	<div id="container">
		<!-- content start -->
		<div id="content_m">
			<form method="post" name="form" id = "form">		
			<div class="top_area">				
				<span><label id="list_total"></label></span>
				<span class="sel_area">
					<select id=type name="type">
						<option value="" <c:if test="${comm.type==''}" >selected</c:if>>
							<spring:message code="common.allType"/>
						</option>
						<c:forEach var="type" items="${type}">
							<option value="${type}" <c:if test="${comm.type==type}" >selected</c:if>>
								${type}
							</option>
						</c:forEach>
					</select>
					<select id="alarm" name="alarm">
						<option value="4" <c:if test="${comm.alarm==4}" >selected</c:if>><spring:message code="event.allEvent"/></option>
						<option value="0" <c:if test="${comm.alarm==0}" >selected</c:if>><spring:message code="event.failure"/></option>
						<option value="1" <c:if test="${comm.alarm==1}" >selected</c:if>><spring:message code="common.danger"/></option>
						<option value="2" <c:if test="${comm.alarm==2}" >selected</c:if>><spring:message code="common.warning"/></option>
						<option value="3" <c:if test="${comm.alarm==3}" >selected</c:if>><spring:message code="critical.normal"/></option>
					</select>
					<select id="groupCode" name="groupCode">
						<option value="0" <c:if test="${comm.groupCode==0}" >selected</c:if>>
							<spring:message code="common.allGroup"/>
						</option>
						<c:forEach var="group" items="${group}">
							<option value="${group.idx}" <c:if test="${comm.groupCode==group.idx}" >selected</c:if>>
								${group.uid}
							</option>
						</c:forEach>
					</select>
					<select id="deviceCode" name="deviceCode">
						<option value="0" <c:if test="${comm.deviceCode==0}" >selected</c:if>>
							<spring:message code="event.allDaemon"/>
						</option>
						<c:forEach var="device" items="${device}">
							<option value="${device.idx}" <c:if test="${comm.deviceCode==device.idx}" >selected</c:if>>
								${device.uid}
							</option>
						</c:forEach>
					</select>
					<select id="confirm" name="confirm">
						<option value="2" <c:if test="${comm.confirm==2}" >selected</c:if>>
							<spring:message code="daemons.all"/>
						</option>
						<option value="1" <c:if test="${comm.confirm==1}" >selected</c:if>>
							<spring:message code="event.confirmed"/>
						</option>
						<option value="0" <c:if test="${comm.confirm==0}" >selected</c:if>>
							<spring:message code="event.unconfirmed"/>
						</option>
					</select>
					<input type="sdate" name="sdate" value=""/>
					~ <input type="edate" name="edate" value=""/>
					<input type="image" src="${pageContext.request.contextPath}/img/btn_search<spring:message code="common.img"/>.gif" alt="search" class="srch_img" onClick="javaScript:goSearch();return false;">
				</span>
			</div>
		</form>
			<table id="list" name="list" border="0" class="tb_list_04" summary="메인 테이블 04">
			<caption>메인 테이블 04</caption>
				<colgroup>
					<col width="44">
					<col width="83">
					<col width="78">
					<col width="85">
					<col width="92">
					<col width="92">
					<col width="158">
					<col width="108">
					<col width="89">
					<col width="63">
					<col width="77">
					<col width="89">
				</colgroup>
				<thead>
					<tr>
						<th class="bar"><p><spring:message code="event.no"/></p></th>
						<th class="bar"><p><spring:message code="common.date"/></p></th>
						<th class="bar"><p><spring:message code="event.firstTime"/></p></th>
						<th class="bar"><p><spring:message code="event.lastTime"/></p></th>
						<th class="bar"><p><spring:message code="common.group"/></p></th>
						<th class="bar"><p><spring:message code="common.daemon"/></p></th>
						<th class="bar"><p>IP : Port</p></th>
						<th><p><spring:message code="common.criticaltype"/></p></th>
						<th><p><spring:message code="common.figure"/>/<spring:message code="common.criticalvalue"/></p></th>
						<th><p><spring:message code="event.event"/></p></th>
						<th><p><spring:message code="event.confirm"/></p></th>
						<th><spring:message code="login.id"/></th>
					</tr>
				</thead>
				<tbody>
					<tr>
					</tr>
				</tbody>
			</table>
				<div class="paginav" id="divPaging"></div>
		
		<iframe id="eventframe" frameborder="0" scrolling="no" src="" width="1280" ></iframe>
		</div>
		<!-- // content end -->
		<div class="clear">
		</div>
	</div>
	<!-- // container end  -->
	
	<!-- footer start -->
	<div id="footer">&nbsp;
	</div>
	<!-- // footer end -->
</div>
</body>
</html>
