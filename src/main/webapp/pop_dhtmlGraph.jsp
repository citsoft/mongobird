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
<%@page import="net.cit.tetrad.common.ColumnConstent"%>
<%@ page language="java" contentType="text/html; charset=UTF-8"
    pageEncoding="UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ page import="net.cit.tetrad.common.Utility"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>  

<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
<title>dhtml graph</title>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-latest.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.droppy.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/commonUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/dateUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common_sliderbar.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.showLoading.js"></script>
<script type="text/javascript">
// var Enlarge;
// var Normal;

// var global_min = new Date(2012, 09, 08, 10, 12, 12);
// var global_max = new Date(2012, 10, 01, 10, 12, 12);
var global_min = dateFormatSplit('${comm.sliderMin}');
var global_max = dateFormatSplit('${comm.sliderMax}');

// var default_min = new Date(2012, 09, 08, 10, 12, 12);
// var default_max = new Date(2012, 10, 01, 10, 12, 12);
var default_min = dateFormatSplit('${comm.sdate}');
var default_max = dateFormatSplit('${comm.edate}');

</script>

<link rel="STYLESHEET" type="text/css" href="${pageContext.request.contextPath}/css/dhtmlx/dhtmlxchart.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/dhtml_graph.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.showLoading.css">
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery_ui_css/slider_css/slider_style.css">  
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery_ui_css/slider_css/jquery-ui-1.8.10.custom.css">
<link rel="stylesheet" id="themeCSS" href="${pageContext.request.contextPath}/css/jquery_ui_css/slider_css/iThingSlimline.css"> 

<script src="${pageContext.request.contextPath}/js/dhtmlx/dhtmlxchart.js" type="text/javascript"></script>
<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jquery-ui-1.8.16.custom.min.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jQRangeSliderMouseTouch.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jQRangeSliderDraggable.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jQRangeSliderHandle.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jQRangeSliderBar.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jQRangeSliderLabel.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jQRangeSlider.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jQDateRangeSliderHandle.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jQDateRangeSlider.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jquery.dateFormat-1.0.js"></script>
		
</head>

<script>

var g_chart;
var g_dataset;
var g_dataUnit;
var g_labelStep;
var g_labelStepCount;
var g_LABEL_VIEW_MAX= 7;
var totalDataSize= 0;
var showCount = 0;
$(document).ready(function() {
	settingDisplay();
	getDataSet();
	settingSliderBar();
	hideAlertArea();
});

function settingSliderBar() {
	//날짜 슬라이더를 생성할 div 객체 참조 변수
    var el=$("#element");
  	//bounds 는 슬라이더의 선택 가능 범위의 min, max
    //defaultValues 는 슬라이더의 선택된 범위의 min, max 초기 값 
    var opt={        
        options: {
            bounds: {min: global_min, max: global_max},
            defaultValues: {min: default_min, max: default_max}
        }
    }
  	
    $("#element").dateRangeSlider(opt.options);
  	
	 //선택값이 변경될 경우 호출되는 함수를 설정한다.
    el.bind("valuesChanged", function(event, data){
        var values = data.values;
        if(showCount!=0)showAlertArea();
    			showCount++;
		setDate(formatDate(values.min), formatDate(values.max));
    });
}

function showAlertArea(){
	$("#alert_area").show();
	setTimeout(hideAlertArea, 2000);
}
function hideAlertArea(){
	$("#alert_area").hide();
}

function settingDisplay() {
	var dstype = $('#dstype').val();
	if (dstype == 'ACC') {
		$('#ds_option').show();
	} else {
		$('#ds_option').hide();
	}

    var pickerOpts = { dateFormat:"yy-mm-dd" };
    $('#selectDate').datepicker(pickerOpts);
}

function getDataSet(){
	jQuery('html').showLoading();
	var data = $('#frm_dhtml').serialize();
	$.ajax({
		type:'POST',
		url : './drawDaemonInfoGraph.do',
		dataType:'html',
		data:data,
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ")");
			g_dataset = jsonData.resultData;
			g_dataUnit = jsonData.dataUnit;
			totalDataSize = g_dataset.length;
			if (totalDataSize < 10) {
				g_labelStep = 1;
			} else {
				g_labelStep = parseInt(totalDataSize/g_LABEL_VIEW_MAX);
			}
			
			drawGraph();
			jQuery('html').hideLoading();
		},
		error:function(xhr,textStatus, errorThrown){
			alert("Server is not responding. Please try again.");
		}
	});	
}

function noData() {
	$('#chartHolder').html('');
	$('<div id="noData" style="margin-top:50px; "><h1 style="font-size: 25px; text-align:center;">No Data</h1></div>').appendTo('#chartHolder');
}

function drawGraph() {
	if (totalDataSize == 0) {
		noData();					
	} else {
		g_labelStepCount = 0;
		$('#chartHolder').html('');
		$('<div id="chart" style="width:910px;height:370px;margin: 0 0 0 8px;"></div>').appendTo('#chartHolder');
		var dataType = $('input:radio[name="search_type"]:checked').val();
		
		g_chart =  new dhtmlXChart({
		 view:"line",
		 container:"chart",
		 value:"#"+dataType+"#",
	// 	 label: "#avg#",
		 label: 
			 function(obj){
				 var value = 0;
				 if (dataType == "point") {
					 value = obj.point;
				 } else if (dataType == "avg") {
					 value = obj.avg;
				 } else if (dataType == "diff") {
					 value = obj.diff;
				 }
				 var v = "";
				 
				 g_labelStepCount++;
				 if ((g_labelStepCount % g_labelStep) == 0) {
					 v = convertUnit(value);
				 }
				 return v;
		      }
		 ,
		 tooltip:{		 
			 template:function(obj){
				 var value = 0;
				 if (dataType == "point") {
					 value = obj.point;
				 } else if (dataType == "avg") {
					 value = obj.avg;
				 } else if (dataType == "diff") {
					 value = obj.diff;
				 }
				 var v =  convertUnit(value);
				 return v;
		         },
			 dx:-8,
		     dy:-25
		 },
		 item:{
		     borderColor: "#1293f8",
		     color: "#ffffff"
		 },
		 line:{
		     color:"#1293f8",
		     width:3
		 },
		 xAxis:{
		     template:"#xunit#"
		 },
		 yAxis:{
	         template:function(obj){
	        	 var yValue = convertUnit(obj);	        		 
	        	 var v = "<span style='font-size:9px;'>"+yValue+"</span>";
		         return v;
	         },
	         title:"<span style='font-size:9px;'>"+g_dataUnit+"</span>"
	     }
// 		 ,
// 		 offset: 1
		});
		g_chart.parse(g_dataset,"json");
	}
}

function convertUnit(obj) {
	if (g_dataUnit == "number") {
		return exFormat(obj);
	} else if (g_dataUnit == "microsec") {
		return dhtmlTimeFormat(obj);
	} else if (g_dataUnit == "msec") {
		return dhtmlTimeFormat(obj*1000);
	} else if (g_dataUnit == "bytes") {
		return sizeFormat(obj);
	} else if (g_dataUnit == "megabytes") {
		return sizeFormat(obj*(1024*1024));
	} else if (g_dataUnit == "ratio") {
		return addCommas(decimal(obj)); 
	} else {
		return obj;
	}
}

$(function(){
	$('#pop_close').click(function (event) {
		window.parent.$.smartPop.close();
	});

	$('#type_avg, #type_point, #type_diff').click(function (event) {
		drawGraph();
	});
	
	$('#period_omin, #graph_fmin, #graph_thymin, #graph_ohour').click(function (event) {
		currentGraph();
	});
	
	$('#option_y, #option_n').click(function (event) {
		getDataSet();
	});		
	
	$('#selectDate,#selectHour,#selectMin').change(function (event) {
		settingSliderBarDate();
	});
});

function settingSliderBarDate() {	
	reflashPage();
}

function currentGraph(){
	$('#memo').val("now");
	reflashPage();
}

function reflashPage() {
	document.frm_dhtml.action="viewDaemonInfoGraph.do";
	document.frm_dhtml.submit();
}

</script>
<body>
<form method="POST" name="frm_dhtml" id="frm_dhtml">
<input type="hidden" id="dstype" name="dstype" value="${comm.dstype}"/>
<input type="hidden" id="dsname" name="dsname" value="${comm.dsname}"/>
<input type="hidden" id="dbname" name="dbname" value="${comm.dbname}"/>
<input type="hidden" id="collname" name="collname" value="${comm.collname}"/>
<input type="hidden" id="deviceCode" name="deviceCode" value="${comm.deviceCode}"/>
<input type="hidden" id="sdate" name="sdate" value="${comm.sdate}" />
<input type="hidden" id="edate" name="edate" value="${comm.edate}" />
<input type="hidden" id="sliderMin" name="sliderMin" value="${comm.sliderMin}"/>
<input type="hidden" id="sliderMax" name="sliderMax" value="${comm.sliderMax}"/>
<input type="hidden" id="memo" name="memo" value=""/>
<input type="hidden" id="title" name="title" value="${comm.title}"/>

<div id="wrap" class="w586">
	<div id="header">
			<div class="title">
				<h1>${comm.title}</h1>
			</div>
			<a href="#" id="pop_close" class="popup_close"><img src="./img/ico_x.gif" width="27" height="21" alt="X"></a>			
	</div>
	<div id="content">
		<fieldset id="dhtml_search">
				<div class="dhtml_layout">
					<b><img alt="구분자 아이콘" src="./img/ico_square.gif" class="img_middle"><span>Function </span></b>
					<input type="radio" id="type_point" name="search_type" value="point" <c:if test="${comm.search_type eq null || comm.search_type eq 'point'}" >checked</c:if>/><span>Last </span>
					<input type="radio" id="type_avg" name="search_type" value="avg" <c:if test="${comm.search_type eq 'avg'}" >checked</c:if> /><span>Avg </span>
					<input type="radio" id="type_diff" name="search_type" value="diff" <c:if test="${comm.search_type eq 'diff'}" >checked</c:if> /><span>Diff</span>
				</div>
				<div class="dhtml_layout">
					<b><span>by</span></b>
					<input type="radio" id="period_omin" name="graph_period" value="1" <c:if test="${comm.graph_period eq null || comm.graph_period eq '1'}" >checked</c:if> /><span>1min </span>
					<input type="radio" id="graph_fmin" name="graph_period" value="5" <c:if test="${comm.graph_period eq '5'}" >checked</c:if> /><span>5min</span>
					<input type="radio" id="graph_thymin" name="graph_period" value="30" <c:if test="${comm.graph_period eq '30'}" >checked</c:if> /><span>30min</span>
					<input type="radio" id="graph_ohour" name="graph_period" value="60" <c:if test="${comm.graph_period eq '60'}" >checked</c:if> /><span>1hour</span>
				</div>
				<div class="dhtml_layout_right">
					<span id="ds_option">
						<img alt="구분자 아이콘" src="./img/ico_square.gif" class="img_middle">
						<input type="radio" id="option_n" name="search_option" value="N" <c:if test="${comm.search_option eq null || comm.search_option eq 'N'}" >checked</c:if> /> <b><span>difference</span></b>						
						<input type="radio" id="option_y" name="search_option" value="Y" <c:if test="${comm.search_option eq 'Y'}" >checked</c:if> /> <b><span>accumulation</span></b>
					</span>
				</div>
		</fieldset>
		<div id="g_slider_area">
			<div class="slider_bar"><div id="element"></div></div>
		
			<div class="slider_btn">
				<input type="image" src="./img/btn_apply<spring:message code="common.img"/>.gif" width="45" height="20" alt="apply" class="btn_apply" id="btn_apply" onclick='getDataSet(); return false;'>
			</div>						
		</div>
		<div id="g_date_area">
			<div class="g_slider_quickclick_area" id="g_slider_quickclick_area"><label onClick="currentGraph()" class="text_top"><a href="#" class="deco_none">Now</a></label></div>
			<div class="g_slider_date_area" id="g_slider_date_area">
			<label id="alert_area"><spring:message code="graph.pressBtn"/></label>
				<input type="text" id="selectDate" name="selectDate" value="<c:out value="${comm.selectDate}" />"/>
				<select id="selectHour" name="selectHour">
					<%for(int h=0;h<24;h++){%>
						<c:set var ="h" value="<%=Utility.intFormat(h)%>"></c:set>
						<option value="${h}" <c:if test="${comm.selectHour eq h}" >selected</c:if>>${h}</option>
					<%}%>
				</select> 
				<select id="selectMin" name="selectMin">
					<%for(int m=0;m<60;m++){%>
						<c:set var ="m" value="<%=Utility.intFormat(m)%>"></c:set>
						<option value="${m}" <c:if test="${comm.selectMin == m}" >selected</c:if>>
							${m}
						</option>
					<%}%>
				</select> 
			</div>
		</div>
<!-- 		style="border:1px solid #A4BED4;"  -->
		<div id="chartHolder"></div>
	</div>
</div>
</form>
</body>
</html>
