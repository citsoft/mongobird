<!--
"mongobird" is released under a dual license model designed to developers 
and commercial deployment.

For using OEMs(Original Equipment Manufacturers), ISVs(Independent Software
Vendor), ISPs(Internet Service Provider), VARs(Value Added Resellers) 
and another distributors, or for using include changed issue
(modify / application), it must have to follow the Commercial License policy.
To check the Commercial License Policy, you need to contact Cardinal Info.Tech.Co., Ltd.
(http://www.citsoft.net)
 *
If not using Commercial License (Academic research or personal research),
it might to be under AGPL policy. To check the contents of the AGPL terms,
please see "http://www.gnu.org/licenses/"
-->
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>    
<%@ include file="./management/eventPro.jsp" %>
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="main.title"/></title>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.smartPop.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/datatable_paging.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.basic.tooltip.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.showLoading.css">
<%@ include file="./common.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dataTables.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.smartPop.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.basic.tooltip.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.showLoading.js"></script>
<script type="text/javascript">
	var mainRefreshPeriodMinute = ${mainRefreshPeriodMinute};
	var pwd = "${pageContext.request.contextPath}";
	var imgLang = "<spring:message code="common.img"/>";
	var mainReloadTimeMilli = ${mainReloadTimeMilli};
	 var cookieName = 'divHeight';
	
	function loadPage(){
		document.daemon.action="mainView.do";
		document.daemon.submit();
	}
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/main_js/main_sort.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/main_js/main_total.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/main_js/main_event.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/main_js/main_graph.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/dateUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/tooltip.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/main_js/getMyState.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery_ui_css/jquery.ui.all.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery_ui/jquery.ui.core.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery_ui/jquery.ui.widget.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery_ui/jquery.ui.mouse.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery_ui/jquery.ui.resizable.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery_ui/jquery.ui.cookie.js"></script>
<%@ include file="./management/CodeUtil.jsp" %>
<script type="text/javascript">
	function goMore(alarm,deviceCode){
		document.daemon.action="subAlarmView.do?alarm="+alarm+"&deviceCode="+deviceCode;
		document.daemon.submit();
	}
	
	function goTotalGraph(dsname){
		var param = "?dsname="+dsname+"&dt"+nowDateNoFormat();
// 		var winHandler = window.open("totalDetailedGraph.do"+param,"Graph_"+dsname,"width=1000,height=700,left=50,top=50,menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=yes");
// 		winHandler.focus();
		$.smartPop.open({ background: "gray", width: 1075, height: 600, url: "totalDetailedGraph.do"+param });
	}
	
	function mainReload(){
		if($('#smartPop_frame').attr('src')==''||$('#smartPop_frame').attr('src')==null){
			location.replace(window.location.href);
		}else{
			if(mainReloadTimeMilli<10000){
				setTimeout( mainReload, mainReloadTimeMilli);
			}else{
				setTimeout( mainReload, 10000);
			}
		}
	}

	//중앙 데몬 리스트
	$(document).ready( function() {
		if(mainReloadTimeMilli!=0){
			setTimeout( mainReload, mainReloadTimeMilli);
		}
		var oTableDaemonList;
	    oTableDaemonList = $('#list').dataTable( {
			//테이블의 페이징 검색 등 부가적인 기능 생략
			"bPaginate": false,
			"bLengthChange": false,
			"bFilter": false,
			"bSort": false,
			"bInfo": false,
			"bAutoWidth": false,
			
			"bProcessing":false,
	        "bServerSide":true,
	        "bRetrieve":true,
            "cache": false,
	    	"sAjaxSource":'mainList.do',
	    	"fnServerParams": function ( aoData ) {
	    		var nowDate = nowDateNoFormat();
	            aoData.push( 
	            	{"name": "dt", "value": nowDate},
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
	            	"sClass": "tableFigures rb", "fnRender": function ( oObj ) {
	            		var value = parseFloat( oObj.aData.opcounters_insert) + parseFloat(oObj.aData.opcounters_query) + parseFloat(oObj.aData.opcounters_update) + parseFloat(oObj.aData.opcounters_delete );
	            		return '<a href="#" class="deco_none" title="'+ toolTip_title(value,"") +'">'+ exFormat(value) +'</a>';
	            	}
	            }
			],
	    	//특정 값 라인 색이 바뀌도록
	    	"fnRowCallback": function( nRow, aData, iDisplayIndex,aaData ) {
				if ( aData.ok == 1 ){
	    	    	$(nRow).children().each(function(){$(this).removeClass('row_stop_demon_status');});
				}else if ( aData.ok == 0 ){
					//dbg("stop status row");
	    	    	nRow.className = "row_stop_demon_status";
	    	    	$(nRow).children().each(function(){$(this).addClass('row_stop_demon_status');});
				}
		    	return nRow;
    	    },
	    	//마우스 오버 시 라인 색이 바뀌도록
			"fnDrawCallback": function(sData){ 
				setTipByTableName("list");				
				var oSettings = oTableDaemonList.fnSettings();
				var iTotalRecords = oSettings.fnRecordsTotal();
				if(iTotalRecords>0){
					getMyState($('#myStateLst').val());
					$('table#list td').bind('mouseenter', function () { $(this).parent().children().each(function(){$(this).addClass('row_selected');}); });
				    $('table#list td').bind('mouseleave', function () { $(this).parent().children().each(function(){$(this).removeClass('row_selected');}); });
				}else{
					$('table#list td').eq(0).attr('class', 'lb rb');
				}
			    if(iTotalRecords>4){
					$("div.scroll_box").width(1295);
			    }else{
					$("div.scroll_box").width(1280);
			    }
			},
			"fnServerData": function ( sSource, aoData, fnCallback ) {
			    $.getJSON( sSource, aoData, function (json) {  
			       fnCallback(json) ;
			    }).complete(function(){
			    }); 
			}
	    });
	    setInterval(function() { oTableDaemonList.fnDraw(); $('#myStateLst').val("");}, mainRefreshPeriodMinute); 
	    
		/* Add a click handler to the rows */
		$("#list tbody").delegate("tr", "click", function() {
			var position = oTableDaemonList.fnGetPosition(this); // getting the clicked row position
			if(position!=null){
				var contactId = oTableDaemonList.fnGetData(position).deviceCode;
				var className = $(this).attr("class");
				if( className == "row_stop_demon_status"){
					goMore(0,contactId);
				}else{
	 				oTableDaemonList.fnDraw();
	 				goDaemon(contactId);
				}
			}
		});
		
		function goDaemon(deviceCode){
			document.daemon.action="subPageView.do?deviceCode="+deviceCode;
			document.daemon.submit();
		}

		var fn_saveHeightInCookie = function(event,ui){
			$.cookie(cookieName, ui.size.height);
		}
		
		$( "#scroll_box" ).resizable({
			minHeight: 125,
			resize: fn_saveHeightInCookie
		});
		
		var savedHeight = $.cookie(cookieName);  

		if(savedHeight != null)
			$('#scroll_box').css('height', savedHeight + 'px');
		
	});

</script>
</head>
<body id="index">
<div id="wrap">
	<!-- header start -->
	<%@ include file="/top_menu.jsp"%>
	<!-- // header end -->
	<!-- container start-->
	<div id="container">
		<!-- content start -->
		<div id="content_m">	
			<form method="post" name="daemon" id = "daemon">
			<input type="hidden" name="myStateLst" id="myStateLst"/>
			<input type="hidden" id="sortItem" name="sortItem" value="${comm.sortItem}"/>
			<input type="hidden" id="sort" name="sort" value="${comm.sort}"/>
			<input type="hidden" id="count" name="count" value="${comm.count}"/>
			<table id="totalInfo" border="0" class="tb_list_09" summary="메인 디비 상태">
				<caption>메인 디비 상태</caption>
				<colgroup>
				<col width="138">
				<col width="130">
				<col width="138">
				<col width="130">
				<col width="136">
				<col width="130">	 
				<col width="137">
				<col width="130">
				</colgroup>
				<tbody>
				<tr>
				<th><spring:message code="main.totaldbsize"/></th>
				<td><a href="#" class="deco_none" id="totalInfoDBSize">&nbsp;</a></td>
				<th><spring:message code="main.totalindexsize"/></th>
				<td><a href="#" class="deco_none" id="totalInfoIndexSize">&nbsp;</a></td>
				<th>
					<%if(request.getAttribute("mongoVer").equals("2.2")){%>
						<spring:message code="main.locksSum"/>
					<% }else{ %>
						<spring:message code="main.globallocktime"/>
					<% }%>
				</th>
				<td><a href="#" class="deco_none" id="totalInfoGlobalLockTime">&nbsp;</a></td>
				<th><spring:message code="main.countofpagefaults"/></th>
				<td><a href="#" class="deco_none" id="totalInfoPageFault">&nbsp;</a></td>
				</tr>
				</tbody>
			</table>
			<div class="main_graph">
				<ul class="ls_graph">
					<li class="first"><a href="#" onClick='goTotalGraph("totalDbDataSize")'><img id="img_dbsize" onerror="defaultImg_graph('${pageContext.request.contextPath}/img/main_nograph<spring:message code="common.img"/>.gif', this);" src="./sub/graph.jsp?fileName=${filename.totalDbDataSize}" width="267" height="133" alt="총 DB사이즈"></a></li>
					<li><a href="#" onClick='goTotalGraph("totalDbIndexSize")'><img id="img_dbindex" onerror="defaultImg_graph('${pageContext.request.contextPath}/img/main_nograph<spring:message code="common.img"/>.gif', this);" src="./sub/graph.jsp?fileName=${filename.totalDbIndexSize}" width="267" height="133" alt="총 Index사이즈"></a></li>
					<li><a href="#" onClick='goTotalGraph("totalGlobalLock")'><img id="img_global" onerror="defaultImg_graph('${pageContext.request.contextPath}/img/main_nograph<spring:message code="common.img"/>.gif', this);" src="./sub/graph.jsp?fileName=${filename.globalLock_lockTime}" width="267" height="133" alt="최고 글로벌락타임"></a></li>
					<li class="last"><a href="#" onClick='goTotalGraph("totalPageFault")'><img id="img_pagefault" onerror="defaultImg_graph('${pageContext.request.contextPath}/img/main_nograph<spring:message code="common.img"/>.gif', this);" src="./sub/graph.jsp?fileName=${filename.extra_info_page_faults}" width="267" height="133" alt="최고 페이지폴트타임"></a></li>
				</ul>
			</div>

				<table id="list_title" border="0" class="tb_list_02" summary="데몬현황">
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
						<th rowspan="2" id="masterColumn"><spring:message code="common.mode"/></th>
						<th rowspan="2"><p><a href="#" onClick='javaScript:sortClick("groupCode")'><spring:message code="common.group"/></a></p></th>
						<th rowspan="2"><p><spring:message code="common.daemon"/></p></th>
						<th rowspan="2"><p><a href="#" onClick='javaScript:sortClick("type")'><spring:message code="common.daemontype"/></a></p></th>
						<th colspan="5"><p><spring:message code="common.dbSize"/></p></th>
						<th colspan="2"><p><spring:message code="common.connections"/></p></th>
						<th colspan="2"><p><spring:message code="common.memory"/></p></th>
						<th rowspan="2"><p><a href="#" onClick='javaScript:sortClick("diff_extra_info_page_faults")'><spring:message code="main.countofpagefaults"/></a></p></th>
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
				</table>
			<div class="scroll_box" id="scroll_box">
				<table id="list" border="0" class="tb_list_02_scroll" summary="데몬현황">
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
					<tbody>
						<tr>
							<td colspan="13">Loading data from server</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="fl">
				<h4 class="m_danger"><spring:message code="common.danger"/><img src="${pageContext.request.contextPath}/img/bg_status_slash.gif"></h4>
				<p class="m_more"><strong><a href="#" onClick='javaScript:goMore(1,0)'>more <img src="${pageContext.request.contextPath}/img/ico_plus.gif" width="6" height="6" alt="+"></a></strong></p>
				<table id="alarm_critical" border="0" class="tb_list_03" summary="위험 이벤트">
				<caption>메인 디비 상태</caption>
					<colgroup>
						<col width="79">
						<col width="136">
						<col width="65">
						<col width="65">
						<col width="90">
						<col width="76">
					</colgroup>
					<thead id="alarm_critical_header">
						<tr>
						<th><spring:message code="common.date"/></th>
						<th>IP : Port</th>
						<th><spring:message code="common.group"/></th>
						<th><spring:message code="common.daemon"/></th>
						<th><spring:message code="common.criticaltype"/></th>
					<th><spring:message code="common.figure"/>/
									<spring:message code="common.criticalvalue"/></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td colspan="6">Loading data from server</td>
						</tr>
					</tbody>
				</table>
			</div>
			<div class="fr">
				<h4 class="m_danger"><spring:message code="common.warning"/><img src="${pageContext.request.contextPath}/img/bg_status_slash.gif"></h4>
				<p class="m_more"><strong><a href="#" onClick='javaScript:goMore(2,0)'>more <img src="${pageContext.request.contextPath}/img/ico_plus.gif" width="6" height="6" alt="+"></a></strong></p>
				<table id="alarm_warning" border="0" class="tb_list_03" summary="경고 이벤트">
				<caption>메인 디비 상태</caption>
					<colgroup>
						<col width="79">
						<col width="136">
						<col width="65">
						<col width="65">
						<col width="90">
						<col width="76">
					</colgroup>
					<thead id="alarm_warning_header">
						<tr>
						<th><spring:message code="common.date"/></th>
						<th>IP : Port</th>
						<th><spring:message code="common.group"/></th>
						<th><spring:message code="common.daemon"/></th>
						<th><spring:message code="common.criticaltype"/></th>
						<th><spring:message code="common.figure"/>/
									<spring:message code="common.criticalvalue"/></th>
						</tr>
					</thead>
					<tbody>
						<tr>
							<td colspan="6">Loading data from server</td>
						</tr>
					</tbody>
				</table>
			</div>
			</form>
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
