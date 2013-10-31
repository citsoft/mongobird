<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/functions"  prefix="fn"%>
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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/datatable_paging.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.basic.tooltip.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.showLoading.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.smartPop.css">
<%@ include file="./management/CodeUtil.jsp" %>
<%@ include file="./common.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.basic.tooltip.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.showLoading.js"></script>
<script type="text/javascript">
	var mainRefreshPeriodMinute = ${mainRefreshPeriodMinute};
	
	function loadPage(){
		document.form.action="subPageView.do";
		document.form.submit();
	}
</script>
<%
	if(session.getAttribute("loginAuth")==null||session.getAttribute("loginAuth").toString().length()==0||session.getAttribute("loginAuth").equals("")){
%>
<script type="text/javascript">
	window.location="${pageContext.request.contextPath}/";
</script>
<%
		return;
	}else{
if(session.getAttribute("loginAuth").equals(1)||session.getAttribute("loginAuth").equals(2)){%>
<script type="text/javascript">
	function commandPop(dival,deviceCode){
		window.open("commamdView.do?deviceCode="+deviceCode,"command"+deviceCode,"width=905,height=700,menubar=no,toolbar=no,location=no,status=no,resizable=no,scrollbars=yes");
	}
</script>
<%}else{%>
<script type="text/javascript">
	function commandPop(dival,deviceCode){
		window.open("cmdExView.do","commandExample","width=900,height=700,menubar=no,toolbar=no,location=no,status=no,resizable=no,scrollbars=yes");
	}
</script>
<%}}%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/tooltip.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/main_js/main_sort.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/main_js/getMyState.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.showLoading.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.smartPop.js"></script>
<script type="text/javascript">	
	$(function(){
		$('#groupCode,#type').change(function () {
			loadPage();
		});
	});
	
	$(document).ready( function() {
		$('#listframe,#listdbframe,#graphframe,#dbgraphframe').hide();
		var oTable = $('#list').dataTable( {
			//테이블의 페이징 검색 등 부가적인 기능 생략
			"bPaginate": true,
			"bLengthChange": false,
			"bFilter": false,
			"bSort": false,
			"bInfo": false,
			"bAutoWidth": false,
			"iDisplayLength": 19 ,
			
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
	    	"sAjaxSource":'mainList.do',
			"fnServerParams": function ( aoData ) {
	            aoData.push( 
		            {"name": "groupCode", "value": "${comm.groupCode}"},
	            	{"name": "type", "value": "${comm.type}"},
	            	{"name": "sortItem", "value": "${comm.sortItem}"},
	            	{"name": "sort", "value": "${comm.sort}"},
	            	{"name": "count", "value": "${comm.count}"}
	            );
			},
			"aoColumns": [
				{
					"sClass": "lb", "fnRender": function ( oObj ) {
						if(oObj.aData.type == "mongod" && oObj.aData.ok != 0){
							if($('#myStateLst').val()==null||$('#myStateLst').val()==""){
								$('#myStateLst').val(oObj.aData.deviceCode);
							}else{
								$('#myStateLst').val($('#myStateLst').val()+","+oObj.aData.deviceCode);
							}
						}
						return '<label id="state_' + oObj.aData.deviceCode + '"></label>';
					}
				},
				{
					"sClass": "tableFiguresLeft", "fnRender": function ( oObj ) {
	            		var str = groupCodeTable.getItem( parseFloat( oObj.aData.groupCode ) );
	            		return !str?"&nbsp;":str;
	            	}
	            },
	            {
	            	"sClass": "tableFiguresLeft", "fnRender": function ( oObj ) {
	            		var str = deviceCodeTable.getItem( parseFloat( oObj.aData.deviceCode ) );
	            		return !str?"&nbsp;":'<a href="#" class="deco_none" title="'+ oObj.aData.ip + ":" + oObj.aData.port +'">'+ str +'</a>';
	            	}
	            },
	            { "sClass": "tableFiguresLeft", "mDataProp": "type" },
				{
	            	"sClass": "tableFigures", "fnRender": function ( oObj ) {
	            		return '<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat(oObj.aData.dbDataSize),"Byte") +'">'+ sizeFormat( parseFloat(oObj.aData.dbDataSize) ) +'</a>';
	            	}
	            },
				{
	            	"sClass": "tableFigures", "fnRender": function ( oObj ) {
	            		return '<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat(oObj.aData.dbStorageSize),"Byte") +'">'+ sizeFormat( parseFloat(oObj.aData.dbStorageSize) ) +'</a>';
	            	}
	            },
				{
	            	"sClass": "tableFigures", "fnRender": function ( oObj ) {
	            		return '<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat(oObj.aData.dbIndexSize),"Byte") +'">'+ sizeFormat(parseFloat( oObj.aData.dbIndexSize ) ) +'</a>';
	            	}
	            },
				{
	            	"sClass": "tableFigures", "fnRender": function ( oObj ) {
	            		return '<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat(oObj.aData.dbFileSize),"Byte") +'">'+ sizeFormat( parseFloat(oObj.aData.dbFileSize) ) +'</a>';
	            	}
	            },
	            {
	            	"sClass": "tableFigures", "fnRender": function ( oObj ) {
	            		return '<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat(oObj.aData.dbCount),"") +'">'+ exFormat(parseFloat( oObj.aData.dbCount ) ) +'</a>';
	            	}
	            },
				{
	            	"sClass": "tableFigures", "fnRender": function ( oObj ) {
	            		return '<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat(oObj.aData.connections_current),"") +'">'+ exFormat(parseFloat( oObj.aData.connections_current ) ) +'</a>';
	            	}
	            },
				{
	            	"sClass": "tableFigures", "fnRender": function ( oObj ) {
	            		return '<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat(oObj.aData.connections_available),"") +'">'+ exFormat(parseFloat( oObj.aData.connections_available ) ) +'</a>';
	            	}
	            },
				{
	            	"sClass": "tableFigures", "fnRender": function ( oObj ) {
	            		return '<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat(oObj.aData.mem_resident),"MB") +'">'+ megaSizeFormat( parseFloat( oObj.aData.mem_resident ) ) +'</a>';
	            	}
	            },
				{
	            	"sClass": "tableFigures", "fnRender": function ( oObj ) {
	            		return '<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat(oObj.aData.mem_virtual),"MB") +'">'+ megaSizeFormat( parseFloat( oObj.aData.mem_virtual ) ) +'</a>';
	            	}
	            },
				{
	            	"sClass": "tableFigures", "fnRender": function ( oObj ) {
	            		return '<a href="#" class="deco_none" title="'+ toolTip_title(parseFloat(oObj.aData.diff_extra_info_page_faults),"") +'">'+ exFormat(parseFloat( oObj.aData.diff_extra_info_page_faults ) ) +'</a>';
	            	}
	            },
				{
	            	"sClass": "rb tableFigures", "fnRender": function ( oObj ) {
	            		var value = parseFloat( oObj.aData.opcounters_insert) + parseFloat(oObj.aData.opcounters_query) + parseFloat(oObj.aData.opcounters_update) + parseFloat(oObj.aData.opcounters_delete );
	            		return '<a href="#" class="deco_none" title="'+ toolTip_title(value,"") +'">'+ exFormat(value) +'</a>';
	            	}
	            }
			],
	    	//특정 값 라인 색이 바뀌도록
	    	"fnRowCallback": function( nRow, aData, iDisplayIndex,aaData ) {
	    		if("${comm.deviceCode}"!=0){
						if ( aData.deviceCode != "${comm.deviceCode}" ){
			    	    	$(nRow).children().each(function(){$(this).removeClass('row_demon_graph');});
						}else if ( aData.deviceCode == "${comm.deviceCode}" ){
			    	    	nRow.className = "row_demon_graph";
			    	    	$(nRow).children().each(function(){$(this).addClass('row_demon_graph');});
						}
	    			}
		    	return nRow;
    	    },
	    	//마우스 오버 시 라인 색이 바뀌도록
			"fnDrawCallback": function(){
				setTipByTableName("list");
				var oSettings = oTable.fnSettings();
				var iTotalRecords = oSettings.fnRecordsTotal();
				var iLength = oSettings._iDisplayLength;
	    	    $("#list_total").html("<a href='subPageView.do?deviceCode=${comm.deviceCode}'><img src='./img/refresh_icon.png' id='refresh' width='17' height='17'></a> <spring:message code='common.total'/> : "+ iTotalRecords +" <spring:message code='common.totals'/>");
            	if(iTotalRecords <= iLength){
            		$("#list_paginate").hide();
            	}else{
            		$("#list_paginate").show();
            	}
				if(iTotalRecords > 0){
					getMyState($('#myStateLst').val());
		      	$('table#list td').bind('mouseenter', function () { $(this).parent().children().each(function(){$(this).addClass('row_selected');}); });
		      	$('table#list td').bind('mouseleave', function () { $(this).parent().children().each(function(){$(this).removeClass('row_selected');}); });
				}else{
					$('table#list td').eq(0).attr('class', 'lb rb');
				}
			}
	    });
	    
		jQuery.fn.dataTableExt.oPagination.iFullNumbersShowPages = 10;
		
		/* Add a click handler to the rows (e.which==1 left click / e.which==3 right click) */
		$("#list tbody").delegate("tr", "mousedown", function(e) {
			var position = oTable.fnGetPosition(this); // getting the clicked row position
			if(position!=null){
				var contactId = oTable.fnGetData(position).deviceCode;
		      	$('.row_demon_graph').removeClass('row_demon_graph');
	      		$("td", this).addClass('row_demon_graph');
				if(e.which==1){
					detailedLst(0,contactId);
				}else if(e.which==3){
// 					document.oncontextmenu = new Function('return false');
// 					commandPop(0,contactId);
				}
			}
		});
	});
	
	function detailedLst(dival,deviceCode){
		jQuery('html').showLoading();
		$('#listframe,#listdbframe,#graphframe,#dbgraphframe').show();
		$('#list_total a').attr('href', 'subPageView.do?deviceCode='+deviceCode);
		var param = "?deviceCode="+deviceCode+"&dival="+dival;
		$("#listframe").attr("src", "demonPop.do"+param);
		$("#listdbframe").attr("src", "demonDBPop.do"+param);

		$("#graphframe").attr("src", "about:blank");
		$("#dbgraphframe").attr("src", "about:blank");
	}
	
	function runCommand(deviceCode){
		$.ajax({
			type:'POST',
			url : 'daemonDetailServerStatus.do',
			data : {"deviceCode" : deviceCode},
			dataType:'html',
			success:function(json,textStatus){
				$("#listframe").contents().find('[id=jsonInput]').val(json);
				$("#listframe").contents().find('[id=cmdRender]').click();
				autoResize("listframe");
				$("#listframe")[0].contentWindow.clickAttach();
			},
			error:function(xhr,textStatus, errorThrown){
				alert("<spring:message code="graph.commandError"/>");
			}
		});
	}
	function autoResize(frameid){
	     var objFrm=document.getElementById(frameid);
	     var frameHeight=objFrm.contentWindow.document.documentElement.scrollHeight+150;
	     if(frameHeight!=0)  {
	      objFrm.style.height=frameHeight + "px";
	     }
	  }
	function suframeSize(height,size,name){
	     var objFrm=document.getElementById(name);
	     var frameHeight = height + size;
	     if(frameHeight!=0)  {
	      objFrm.style.height=frameHeight + "px";
	     }
	  }
	
	function init(){
		if("${comm.deviceCode}"!=0){
			detailedLst(0,"${comm.deviceCode}");
		}
	}
	
	function isShowGraph(dsname, devicecode, dbname){
		$.ajax({
			type:'POST',
			url : './isShowGraph.do',
			dataType:'html',
			data:"dsname="+dsname+"&dbname="+dbname,
			success:function(json,textStatus){
				var jsonData = eval("(" + json + ")");
				var isshow = jsonData.isshow;
				var collname = jsonData.collname;
				var dbname = jsonData.dbname;
				var title = dsname;
				if (isshow == true) {
					daemonLayerdPop(jsonData.dsname, devicecode, collname, dbname, title);
				}else{
					alert("<spring:message code="graph.noGraphValue"/>");
				}
			},
			error:function(xhr,textStatus, errorThrown){
				alert("error");
			}
		});	
	}
	
// 	var target = ["pid", "uptime", "uptimeMilis", "uptimeEstimate"];
	function daemonLayerdPop(dsname, devicecode, collname, dbname, title) {
		$.smartPop.open({ background: "gray", width: 1030, height: 600, url: "viewDaemonInfoGraph.do?dsname="+dsname+"&deviceCode="+devicecode+"&collname="+collname+"&dbname="+dbname + "&title="+title});			
		
	}
</script>
</head>
<body id="index" onLoad="init()">
<div id="wrap">
	<!-- header start -->
	<%@ include file="/top_menu.jsp"%>
	<!-- // header end -->
	<!-- container start-->
	<div id="container">
		<!-- content start -->
		<div id="content_m">
		<form method="post" name="form" id = "form">
			<input type="hidden" name="myStateLst" id="myStateLst"/>
			<input type="hidden" id="sortItem" name="sortItem" value="${comm.sortItem}"/>
			<input type="hidden" id="sort" name="sort" value="${comm.sort}"/>
			<input type="hidden" id="count" name="count" value="${comm.count}"/>
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
					<select id="groupCode" name="groupCode">
						<option value="0" <c:if test="${comm.groupCode==0}" >selected</c:if>>
							<spring:message code="common.allGroup"/>
						</option>
						<c:forEach var="i" items="${group}">
							<option value="${i.idx}" <c:if test="${comm.groupCode==i.idx}" >selected</c:if>>
								${i.uid}
							</option>
						</c:forEach>
					</select>
				</span>
			</div>
		</form>	
			<table id="list" border="0" class="tb_list_02" summary="메인 테이블 02">
			<caption>메인 테이블 02</caption>
				<colgroup>
						<col width="40">
						<col width="86">
						<col width="109">
						<col width="85">
						<col width="96">
						<col width="85">
						<col width="85">
						<col width="85">
						<col width="85">
						<col width="64">
						<col width="64">
						<col width="79">
						<col width="78">
						<col width="99">
						<col width="80">
				</colgroup>
				<thead>
					<tr>
						<th rowspan="2"><spring:message code="common.mode"/></th>
						<th class="bar" rowspan="2"><p><a href="#" onClick='javaScript:sortClick("groupCode")'><spring:message code="common.group"/></a></p></th>
						<th class="bar" rowspan="2"><p><spring:message code="common.daemon"/></p></th>
						<th class="bar" rowspan="2"><p><a href="#" onClick='javaScript:sortClick("type")'><spring:message code="common.daemontype"/></a></p></th>
						<th class="bar" colspan="5"><p><spring:message code="common.dbSize"/></p></th>
						<th class="bar" colspan="2"><p><spring:message code="common.connections"/></p></th>
						<th class="bar" colspan="2"><p><spring:message code="common.memory"/></p></th>
						<th class="bar" rowspan="2"><p><a href="#" onClick='javaScript:sortClick("diff_extra_info_page_faults")'><spring:message code="main.countofpagefaults"/></a></p></th>
						<th rowspan="2"><p><spring:message code="common.totalopqueries"/></p></th>
					</tr>
					<tr>
						<th><a href="#" onClick='javaScript:sortClick("dbDataSize")'><spring:message code="common.data"/></a></th>
						<th><p><a href="#" onClick='javaScript:sortClick("dbStorageSize")'><spring:message code="common.storage"/></a></p></th>
						<th><a href="#" onClick='javaScript:sortClick("dbIndexSize")'><spring:message code="common.index"/></a></th>
						<th><p><a href="#" onClick='javaScript:sortClick("dbFileSize")'><spring:message code="common.file"/></a></p></th>
						<th><p><a href="#" onClick='javaScript:sortClick("dbCount")'><spring:message code="common.count"/></a></p></th>
						<th><a href="#" onClick='javaScript:sortClick("connections_current")'><spring:message code="common.current"/></a></th>
						<th><p><a href="#" onClick='javaScript:sortClick("connections_available")'><spring:message code="common.available"/></a></p></th>
						<th><a href="#" onClick='javaScript:sortClick("mem_resident")'><spring:message code="common.resident"/></a></th>
						<th><p><a href="#" onClick='javaScript:sortClick("mem_virtual")'><spring:message code="common.virtual"/></a></p></th>
					</tr>
				</thead>
				<tbody>
					<tr>
					</tr>
				</tbody>
			</table>	
			<iframe id="listdbframe" frameborder="0" src="" width="635" height="245" align="left"></iframe>
			<iframe id="dbgraphframe" frameborder="0" src="" width="635" height="245" align="right"></iframe>
			<iframe id="listframe" frameborder="0" src="" width="635" align="left"></iframe>
			<iframe id="graphframe" frameborder="0" src="" width="635" height="500" align="right"></iframe>
			<!-- License information View -->
			<jsp:include page="/footer.jsp" flush="false"/>
		</div>
		<!-- // content end -->
		<div class="clear">
		</div>
	</div>
	<!-- // container end  -->
	
	<!-- footer start -->
	<div id="footer">&nbsp;</div>
	<!-- // footer end -->
</div>
</body>
</html>
