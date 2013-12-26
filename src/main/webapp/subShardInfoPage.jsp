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
<link rel="STYLESHEET" type="text/css" href="${pageContext.request.contextPath}/css/dhtmlx/dhtmlxchart.css">
<!-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dhtml_graph.css"> -->
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.basic.tooltip.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.showLoading.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/datatable_paging.css">
<%@ include file="./common.jsp" %>
<script type="text/javascript">
var mainRefreshPeriodMinute = ${mainRefreshPeriodMinute};
	var pwd = "${pageContext.request.contextPath}";
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.showLoading.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.basic.tooltip.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/tooltip.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/commonUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.dataTables.js"></script>
<script src="${pageContext.request.contextPath}/js/dhtmlx/dhtmlxchart_for_sharding.js" type="text/javascript"></script>
<script type="text/javascript">
var shards = "shards";
var databases = "databases";
var settings = "settings";
var divStyle = 'height:45px;';
var barHmaxValue = 0;
var fontwidth = 7;
var shardNameLengthMax = 80;
var deviceCode = ${comm.deviceCode};
function getShardsInfo(collectionName){
	$.ajax({
		type:'POST',
		url : 'shardCllectionCommand.do',
		dataType:'html',
		data : {"deviceCode" : deviceCode, "collectionName" : collectionName},
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ')');
			var tb = "";
			if(collectionName == shards){
				for(var i=0;i<jsonData.aaData.length;i++){
					var host = jsonData.aaData[i].host.split("/");
					var shardName = host[0];
					var daemons = host[1];
					var shardNameLength = (shardName.length+7)*fontwidth;
					if(shardNameLength>shardNameLengthMax)shardNameLengthMax = shardNameLength;
					tb += '<tr><td class="lb tableFiguresLeft">'+shardName+'</td><td class="lb rb tableFiguresLeft">'+daemons+'</td></tr>';
				}
				$('#shardTB tbody').append(tb);
			}else if(collectionName == databases){
				for(var i=0;i<jsonData.aaData.length;i++){
					var dbName = jsonData.aaData[i]._id;
					var partitioned = jsonData.aaData[i].partitioned;
					var primary = jsonData.aaData[i].primary;
					var dbNameTbStr = "";
					if(partitioned){
						var selectDbNameStyle = "";
						if('${comm.dbname}' == dbName)selectDbNameStyle = "color:#DC143C;font-weight:bold;"
						dbNameTbStr = '<a onClick="showChunckInfo(\''+dbName+'\');"><span style="'+selectDbNameStyle+'">'+dbName+'</span></a>';
					}else{
						dbNameTbStr = dbName;
					}
					tb += '<tr><td class="lb tableFiguresLeft">'+dbNameTbStr+'</td><td class="lb tableFiguresLeft">'+partitioned+'</td><td class="lb tableFiguresLeft">'+primary+'</td><td id="'+dbName+'_dbsize" class="lb tableFigures"></td><td id="'+dbName+'_collections" class="lb tableFigures rb"></td></tr>';
				}
				$('#databaseTB tbody').append(tb);
			}else if(collectionName == settings){
				tb = "Chunks";
				var valueOfAaData="";
				
				try{
					valueOfAaData = jsonData.aaData[0].value; 
				}catch(e){
					valueOfAaData = "";
				}finally{
					$('#chunksTH').append(tb+" ("+valueOfAaData+"MB)");
				}
			}
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});	
}

function getDbStatus(){
	$.ajax({
		type:'POST',
		url : 'shardDbStatus.do',
		dataType:'html',
		data : {"deviceCode" : deviceCode},
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ')');
			for(var i=0;i<jsonData.aaData.length;i++){
				var dbName = jsonData.aaData[i].db;
				if(dbName != 'config'){
					$('#'+dbName+'_dbsize').append(sizeFormat(jsonData.aaData[i].dataSize));
					$('#'+dbName+'_collections').append(exFormat(jsonData.aaData[i].collections));
				}
			}
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});	
}

function getChunksInfo(dbNameParam){			
	jQuery('html').showLoading();
	var oTable ;
		var chartCollNm=[];
		var dataset=[];
		var collName ;
		var nameReplace;
		var collNameSplit;
		oTable = $('#collectionTB').dataTable( {
		//테이블의 페이징 검색 등 부가적인 기능 생략
		"bPaginate": true,
		"bLengthChange": false,
		"bFilter": false,
		"bSort": false,
		"bInfo": false,
		"bAutoWidth": false,
		"iDisplayLength": 20 ,
		
		"sPaginationType": "full_numbers",
		"oLanguage" :{
			"oPaginate":{
				"sFirst": " <a><img src=\"./img/img_pre_end.gif\" width=\"11\" height=\"12\" border=\"0\" alt=\"<spring:message code="common.first"/>\"/ class=\"textmiddle\"></a> ",
				"sPrevious": " <a><img src=\"./img/img_pre.gif\" width=\"11\" height=\"12\" border=\"0\" alt=\"<spring:message code="common.back"/>\"/ class=\"textmiddle\"></a> ",
				"sNext": " <a><img src=\"./img/img_next.gif\" width=\"11\" height=\"12\" border=\"0\" alt=\"<spring:message code="common.next"/>\"/ class=\"textmiddle\"></a> ", 
				"sLast": " <a><img src=\"./img/img_next_end.gif\" width=\"11\" height=\"12\" border=\"0\" alt=\"<spring:message code="common.last"/>\"/ class=\"textmiddle\"></a>"
			}
		},
// 		"bProcessing":true,
        "bServerSide":true,
        "bRetrieve":true,
        "cache": false,
    	"sAjaxSource":'shardChunkLstCommand.do?deviceCode=' + deviceCode + "&dbNameParam=" + dbNameParam,
		"aoColumns": [
								{ 
									"sClass": "lb tableFiguresLeft", "fnRender": function ( oObj ) {
										collName = oObj.aData.collName;
										collNameSplit = collName.split(".");
						 				return collNameSplit[1];
									}
					            },
								{ 
									"sClass": "lb tableFigures", "fnRender": function ( oObj ) {
						 				nameReplace = collName.replace(".","_");
						 				return '<label id="'+nameReplace+'_totalchunks">' + oObj.aData.totalChunkCnt + '</label>';
									}
					            },
								{ 
									"sClass": "lb rb", "fnRender": function ( oObj ) {
				 						chartCollNm.push(collName);
				 						var chunkInfoGroup = oObj.aData.chunkInfoGroup;
				 						for(x in chunkInfoGroup){
					 						dataset.push(chunkInfoGroup[x]);
				 						}
				 						var height = chunkInfoGroup.length*31;
						 				return '<div id="'+nameReplace+'_chartDiv" style="height:'+ height +'px;"></div>';
									}
					            }
           ],
        //마우스 오버 시 라인 색이 바뀌도록
		"fnDrawCallback": function(){
			makeChart(dataset,chartCollNm);
			chartCollNm=[];
			dataset=[];
			var oSettings = oTable.fnSettings();
			var iTotalRecords = oSettings.fnRecordsTotal();
				var iLength = oSettings._iDisplayLength;
 	    $("#list_total").text(dbNameParam == ""?"<spring:message code="common.nolist"/>"  :  "Selected DB : " +dbNameParam+ " / Sharding collections : "+ iTotalRecords);
        	if(iTotalRecords <= iLength){
        		$("#collectionTB_paginate").hide();
        	}else{
        		$("#collectionTB_paginate").show();
           				}
			if(!(iTotalRecords > 0)){
				$('table#collectionTB td').eq(0).attr('class', 'lb rb');
			}
			jQuery('html').hideLoading();	
// 		},
// 		"fnServerData": function ( sSource, aoData, fnCallback ) {
// 		    $.getJSON( sSource, aoData, function (json) {  
// 		       fnCallback(json) ;
// 		    }).complete(function(){
// 		    }); 
		}
    });
}

function makeChart(dataset,chartCollNm) {	
// 	function makeChart(chartCollNm) {	
// 	var dataset = [
// 	  { "shard" : "rs_b" , "nChunks" : 311.0 , "collName" : "exampledb.board"}, 
// 	  { "shard" : "rs_a" , "nChunks" : 312.0 , "collName" : "exampledb.board"}, 
// 	  { "shard" : "rs_c" , "nChunks" : 317.0 , "collName" : "exampledb.board"},
// 	  { "shard" : "rs_d" , "nChunks" : 200.0 , "collName" : "exampledb.board"},
// 	  { "shard" : "rs_e" , "nChunks" : 150.0 , "collName" : "exampledb.board"}, 
// 	  { "shard" : "rs_r" , "nChunks" : 500.0 , "collName" : "exampledb.board"}, 
// 	  { "shard" : "rs_b" , "nChunks" : 1.0 , "collName" : "exampledb.board2"}
// 	 ];
var lineColor = ["#ee9336","#eed236","#d3ee36","#a7ee70","#58dccd","#36abee","#476cee","#a244ea","#e33fc7","#ee4339"];
var colorCnt = 0;
	for(var i=0;i<chartCollNm.length;i++){
		var nameReplace = chartCollNm[i].replace(".","_");
		barHmaxValue = parseInt($('#'+nameReplace+'_totalchunks').text());
		   var gubunData = [];
		   for(var j=0;j<dataset.length;j++){
			   if(dataset[j].collName == chartCollNm[i]){
						dataset[j].color=lineColor[colorCnt%10];
						var per = dataset[j].nChunks/barHmaxValue*100;
						dataset[j].percent=per.toFixed(1);
				   gubunData.push(dataset[j]);
					  colorCnt++;
			 		 	}
			   	}
	    var barChart = new dhtmlXChart({
	        view: "barH",
	        container: nameReplace+"_chartDiv",
	        value: "#nChunks#",
	        label:"#percent#%",
	        color: "#color#",
	        width: 15,
	        border: false,
	        yAxis: {
	            template: "#shard# (#nChunks#)",
	            lines: false
	       					 },
	       	 padding: {
		           top: 10,
		           bottom: 10,
	            left: shardNameLengthMax,
	       				right: 60
	       				    }
			  });
	    barChart.parse(gubunData, "json");
	}
}
$(function(){	
	$('input[type=radio]').click(function (event) {
		document.frm_demon.action="subShardInfoView.do";
		document.frm_demon.submit();
	});
});

function showChunckInfo(dbNameParam){
	$('#dbname').val(dbNameParam);
	document.frm_demon.action="subShardInfoView.do";
	document.frm_demon.submit();
}
function refreshAll(){
	deviceCode = $(':input:radio[name=deviceCode]:checked').val();
	if(deviceCode != null){
		getShardsInfo(settings);
		getShardsInfo(shards);
		getShardsInfo(databases);
		getDbStatus();
	 	getChunksInfo('${comm.dbname}');
	}
}

$(document).ready(function() {
	if($(':input:radio[name=autoRefresh]:checked').val() == 'on')setInterval(function() { showChunckInfo('${comm.dbname}'); }, mainRefreshPeriodMinute*6);
	refreshAll();
});
</script>
</head>
<body>
<div id="wrap">
	<!-- header start -->
	<%@ include file="/top_menu.jsp"%>
	<!-- // header end -->
	<!-- container start-->
	<div id="container">
		<!-- content start -->
		<div id="content_graph">
		<h1>
			<img src="./img/subtit_sharding<spring:message code="common.img"/>.png">
		</h1>
			<form method="post" name="frm_demon" id="frm_demon">
			<input type="hidden" id="dbname" name="dbname"/>
				<div id="menu_graph">
					<ul>
						<li class="end"><b><img src="./img/subtit_mongosselect<spring:message code="common.img"/>.png"></b></li>
					<c:forEach var="i" items="${deviceLst}">
						<li>
							<input type="radio" id="deviceCode" name="deviceCode" value="${i.idx}" <c:if test="${i.idx==comm.deviceCode}" >checked</c:if> />  ${i.uid}
						</li>
					</c:forEach>
					</ul>
				</div>
				<div id="contents_graph">
				<h2><img src="./img/SubTitle_Shards.png"></h2><br>
					<table id="shardTB" border="0" class="tb_list_10">
						<thead>
							<tr>
								<th>Shard</th>
								<th>Host</th>
							</tr>
						</thead>
						<tbody>
						</tbody>
					</table>
					<br><br>
					<h2><img src="./img/SubTitle_Databases.png"></h2><br>
					<div class="fr" style="width:480px; margin:5px 20px;"><label class="fr" id="list_total"></label></div>
					<table border="0">
						<tr>
							<td valign=top>
								<table id="databaseTB" border="0" class="tb_list_11">
									<colgroup>
										<col width="100">
										<col width="100">
										<col width="100">
										<col width="100">
										<col width="100">
									</colgroup>
									<thead>
										<tr>
											<th>DB Name</th>
											<th>Partitioned</th>
											<th>Primary</th>
											<th>DB Size</th>
											<th>Collections</th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
								<div style="padding:5px 10px;">
									<span class="lr">
										Auto-refresh :
										<input type="radio" id="autoRefresh" name="autoRefresh" value="on" <c:if test="${'on'==comm.autoRefresh}" >checked</c:if> />On&nbsp;
										<input type="radio" id="autoRefresh" name="autoRefresh" value="off" <c:if test="${'off'==comm.autoRefresh}" >checked</c:if> />Off
									</span>
									<span class="fr"><spring:message code="shard.clickDbName"/></span>
								</div>
							</td>
							<td valign=top>
								<table id="collectionTB" border="0" class="tb_list_11">
									<colgroup>
										<col width="130">
										<col width="90">
										<col width="230">
									</colgroup>
									<thead>
										<tr>
											<th>Collection name</th>
											<th>Total Chunks</th>
											<th id="chunksTH"></th>
										</tr>
									</thead>
									<tbody>
									</tbody>
								</table>
							</td>
						</tr>
					</table>
				</div>				
			</form>
			<!-- License information View -->
            <jsp:include page="/footer.jsp" flush="false"/>
		</div>
	</div>
</div>
</body>
</html>
