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
<%@ page import="java.util.Calendar"%>
<%@ page import="net.cit.tetrad.common.Utility"%>
<%@ page import="net.cit.tetrad.rrd.utils.TimestampUtil"%>

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
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="main.title"/></title>
<script type="text/javascript">
var pwd = "${pageContext.request.contextPath}";
var imgLang = "<spring:message code="common.img"/>";
var mongoVer = '${mongoVer}';
</script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/daemonGraph.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-latest.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/dateUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/commonUtil.js"></script>
<script type="text/javascript">
var dbGraphShow = true;
var lockGraphShow = true;
$(document).ready(function () {
  $('#dbgraph_area_btn').click(function (event) {
	  parent.jQuery('html').showLoading();
      $('#dbgraph_area').toggleClass('hidden');
      $("#dbgraph_area_btn span").toggle();
      toggleDBGraph();
    });
  $('#lockGraph_area_btn').click(function (event) {
	  parent.jQuery('html').showLoading();
      $('#lockGraph_area').toggleClass('hidden');
      $("#lockGraph_area_btn span").toggle();
      toggleLockGraph();
    });
});
function toggleDBGraph(){
  if(dbGraphShow){
  		getDbLst(setDbGraph);
  		dbGraphShow = false;
	 }else{
			parent.jQuery('html').hideLoading();	
	 }
}
function toggleLockGraph(){
  if(lockGraphShow && mongoVer == '2.2'){
  		getDbLst(setDbLockGraph);
  		lockGraphShow = false;
	 }else{
			parent.jQuery('html').hideLoading();	
	 }
}
function getDbLst(fnCallback){
	$.ajax({
		type:'POST',
		url : 'setDbLst.do',
		dataType : 'html',
		data : {'deviceCode' : '${comm.deviceCode}'},
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ")");
			fnCallback(jsonData.aaData);
		},
		error:function(xhr,textStatus, errorThrown){
			alert(xhr.responseText);
		}
	});	
}
	
function setDbGraph(dbInfos){
var postData = $('#frm_demon').serialize();
$.ajax({
	type:'POST',
	url : 'subDbGraph.do',
	dataType : 'html',
	data : postData,
	success:function(json,textStatus){
		var jsonData = eval("(" + json + ")");
		$('#dbgraph_area').empty();
		
		var defaultSrc = "./sub/graph.jsp?fileName=";
		var timestamp = "&dt="+nowDateNoFormat();
		var strimg = '';
		for(var i=0;i<jsonData.aaData.length;i++){
			var dbName = jsonData.aaData[i];
			var srcStr = defaultSrc + dbName + timestamp;
			strimg += '<img src="' + srcStr + '">';
		}
		$('#dbgraph_area').html(strimg);
		setTimeout('frameResize()',1000);
		parent.jQuery('html').hideLoading();	
	},
	error:function(xhr,textStatus, errorThrown){
		parent.jQuery('html').hideLoading();	
	}
});	
}

function setDbLockGraph(dbInfos){
	var dbNameLst = new Array();
	for(var i=0; i<dbInfos.length; i++){
		dbNameLst[i] = dbInfos[i].db;
		$('#hidden_area').append('<input type="hidden" id="dbNameLst" name="dbNameLst" value="'+dbNameLst[i]+'"/>');
	}
	var postData = $('#frm_demon').serialize();
	$.ajax({
		type:'POST',
		url : 'lockGraph.do',
		dataType : 'html',
		data : postData,
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ")");

			var count = 0;
			for (var i in jsonData.aaData) {
				count++;
			}
			var isExistDsDev = (count == 0);
			if(!isExistDsDev){				
				var defaultSrc = "./sub/graph.jsp?fileName=";
				var timestamp = "&dt="+nowDateNoFormat();
				var strimg = '';
				for(var i=0;i<jsonData.aaData.length;i++){
					var dbName = jsonData.aaData[i];
					var srcStr = defaultSrc + dbName + timestamp;
					strimg += '<img src=' + srcStr + '>';
				}
				$("#dbLockGraph_area").html(strimg);
			}
			
			$('#hidden_area').empty();
			sumDBLockGraph();
			
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});	
}

function sumDBLockGraph(){
	var postData = $('#frm_demon').serialize();
	$.ajax({
		type:'POST',
		url : 'lockSumGraph.do',
		dataType : 'html',
		data : postData,
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ")");
			$('#dbLockSumGraph_area').empty();
			
			var count = 0;
			for (var i in jsonData.aaData) {
				count++;
			}
			var isExistDsDev = (count == 0);
			if(isExistDsDev){
				var imgPath = pwd+'/img/sub_nodaemon_graph'+imgLang+'.gif';
				$("#dbLockSumGraph_area").append('<img src="'+imgPath+'">');
			}else{				
				var defaultSrc = "./sub/graph.jsp?fileName=";
				var timestamp = "&dt="+nowDateNoFormat();
				for(var i=0;i<jsonData.aaData.length;i++){
					var dbSumName = jsonData.aaData[i];
					var srcStr = defaultSrc + dbSumName + timestamp;
				$("#dbLockSumGraph_area").append('<img src="'+srcStr+'">');
				}
			}
			setTimeout('frameResize()',1000);
			parent.jQuery('html').hideLoading();	
		},
		error:function(xhr,textStatus, errorThrown){
			parent.jQuery('html').hideLoading();	
		}
	});	
}

var imgTotalCount = 0;
var imgCount = 0;
function loadImage(objImg){
	if(objImg.src.indexOf("nograph")<0)imgCount++;
	if(imgTotalCount == imgCount){
		frameResize();
			parent.jQuery('html').hideLoading();	
	}
}

function setGraph(){
	imgTotalCount = 0;
	imgCount = 0;
	var postData = $('#frm_demon').serialize();
	$.ajax({
		type:'POST',
		url : 'subGraph.do',
		dataType : 'html',
		data : postData,
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ")");
			$('#sysLockGraph_area').empty();
			$('#graph_area_graph').empty();
			
			var count = 0;
			for (var i in jsonData.aaData) {
				count++;
				imgTotalCount++;
			}
			var isExistDsDev = (count == 0);
			if(isExistDsDev){
				var imgPath = pwd+'/img/sub_nodaemon_graph'+imgLang+'.gif';
				$("#graph_area_graph").append('<img id="noGraph" onerror="defaultImg_graph(\'./img/sub_nodaemon_graph'+imgLang+'.gif\', this);" src="'+imgPath+'">');
			}else{				
				var defaultSrc = "./sub/graph.jsp?fileName=";
				var timestamp = "&dt="+nowDateNoFormat();
				for(var i=0;i<jsonData.aaData.length;i++){
					var dsName = jsonData.aaData[i];
					var srcStr = defaultSrc + dsName + timestamp;
					if(dsName.indexOf("locks")>-1 || dsName.indexOf("global")>-1){
						$("#sysLockGraph_area").append('<img id="'+dsName+'" onload="loadImage(this)" onerror="defaultImg_graph(\'./img/sub_nograph'+imgLang+'.gif\', this);" src="'+srcStr+'">');
					}else{
						$("#graph_area_graph").append('<img id="'+dsName+'" onload="loadImage(this)" onerror="defaultImg_graph(\'./img/sub_nograph'+imgLang+'.gif\', this);" src="'+srcStr+'">');
					}
				}
			}
		},
		error:function(xhr,textStatus, errorThrown){
		}
	});	
}

function frameResize(){
	parent.suframeSize($('#content2').outerHeight(),650,"graphframe");
}

$(function(){
	$('input[type=radio]').click(function (event) {
		parent.jQuery('html').showLoading();
		dbGraphShow = true;
		lockGraphShow = true;
		setGraph();
		toggleDBGraph();
		toggleLockGraph();
	});
});

$(document).ready(function() {
	setGraph();
});

</script>
</head>
<body>
	<div id="wrap" class="w586">
		<div id="content2">
				<form method="post" name="frm_demon" id="frm_demon">
				<input type="hidden" id="sdate" name="sdate" value="${comm.sdate}" />
				<input type="hidden" id="edate" name="edate" value="${comm.edate}" />
				<input type="hidden" id="type_gubun" name="type_gubun" value="device"/>
				<input type="hidden" id="search_gubun" name="search_gubun" value="dataSize"/>
				<input type="hidden" id="sortItem" name="sortItem" value="daemon"/>
				<input type="hidden" id="graph_period" name="graph_period" value="1h"/>
				<input type="hidden" id="deviceCode" name="deviceCode" value="${comm.deviceCode}" />
				<div id="hidden_area"></div>
							<b><input type="image" src="./img/ico_square.gif">&nbsp;Function :&nbsp;</b>			
							<input type="radio" id="last_consolFun" name="consolFun" value="LAST" <c:if test="${comm.consolFun=='LAST'}" >checked</c:if> ><label>Last</label>&nbsp;&nbsp; 
							<input type="radio" id="avg_consolFun" name="consolFun" value="AVERAGE" <c:if test="${comm.consolFun=='AVERAGE'}" >checked</c:if> ><label>Avg</label>&nbsp;&nbsp; 
							<input type="radio" id="total_consolFun" name="consolFun" value="TOTAL" <c:if test="${comm.consolFun=='TOTAL'}" >checked</c:if> ><label>Total</label>&nbsp;&nbsp;
							<b>&nbsp;&nbsp; by :&nbsp;</b>		
							<input type="radio" id="secStep" name="graph_step" value="sec" <c:if test="${comm.graph_step=='sec'}" >checked</c:if> ><label>10sec</label>&nbsp;&nbsp; 
							<input type="radio" id="minStep" name="graph_step" value="min" <c:if test="${comm.graph_step=='min'}" >checked</c:if> ><label>1min</label>&nbsp;&nbsp; 
							<input type="radio" id="5minStep" name="graph_step" value="5min" <c:if test="${comm.graph_step=='5min'}" >checked</c:if> ><label>5min</label>&nbsp;&nbsp; 
							<input type="radio" id="30minStep" name="graph_step" value="30min" <c:if test="${comm.graph_step=='30min'}" >checked</c:if> ><label>30min</label>&nbsp;&nbsp; 
							<input type="radio" id="hourStep" name="graph_step" value="hour" <c:if test="${comm.graph_step=='hour'}" >checked</c:if> ><label>1hour</label>&nbsp;&nbsp;
				</form>
					<div class="demon_detail_margin">
						<h2>
							<img src="./img/daemon_subtit05<spring:message code="common.img"/>.png">&nbsp;
							<a id="dbgraph_area_btn">
								<span><img src="./img/arrow_down.png" width="18" height="18"></span>
								<span style="display: none"><img src="./img/arrow_up.png" width="18" height="18"></span>
							</a>
						</h2>			
						<div class="dbgraph_area hidden" id="dbgraph_area"></div>
					</div>
					<div class="demon_detail_margin">
						<h2>
							<img src="./img/daemon_subtit_lock<spring:message code="common.img"/>.png">&nbsp;
							<a id="lockGraph_area_btn">
								<span><img src="./img/arrow_down.png" width="18" height="18"></span>
								<span style="display: none"><img src="./img/arrow_up.png" width="18" height="18"></span>
							</a>
						</h2>			
						<div class="hidden" id="lockGraph_area">
							<div class="dbgraph_area" id="sysLockGraph_area"></div>			
							<div class="dbgraph_area" id="dbLockSumGraph_area"></div>			
							<div class="dbgraph_area" id="dbLockGraph_area"></div>
						</div>
					</div>
				<div class="demon_detail">
					<h2>
						<img src="./img/daemon_subtit04<spring:message code="common.img"/>.png">
					</h2>		
				</div>	
				<div class="graph_area_graph" id="graph_area_graph"></div>
		</div>		
	</div>
</body>
</html>
