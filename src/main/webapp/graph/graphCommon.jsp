<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
<%-- <link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/calendar.css"> --%>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.basic.tooltip.css">
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.showLoading.css">
<%@ include file="../common.jsp" %>
<%-- <script type="text/javascript" src="${pageContext.request.contextPath}/js/calendar.js"></script> --%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/dateUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/commonUtil.js"></script>
<script type="text/javascript">
var mainRefreshPeriodMinute = ${mainRefreshPeriodMinute};
	var Enlarge = "<spring:message code="graph.enlarge"/>";
	var Normal = "<spring:message code="graph.normal"/>";
	var pwd = "${pageContext.request.contextPath}";
	var imgLang = "<spring:message code="common.img"/>";
	var showCount = 0;
</script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.showLoading.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/graphDateSlider.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/subgraph_js/sub_graph.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.basic.tooltip.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/tooltip.js"></script>

<%-- <link rel="stylesheet" id="themeCSS" href="${pageContext.request.contextPath}/css/jquery_ui_css/slider_css/iThing.css">  --%>
<link rel="stylesheet" id="themeCSS" href="${pageContext.request.contextPath}/css/jquery_ui_css/slider_css/iThingSlimline.css"> 
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery_ui_css/slider_css/slider_style.css">  
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery_ui_css/slider_css/jquery-ui-1.8.10.custom.css">  
	<script type="text/javascript">
	var global_min = dateFormatSplit('${comm.sliderMin}');
	var global_max = dateFormatSplit('${comm.sliderMax}');
	var default_min = dateFormatSplit('${comm.sdate}');
	var default_max = dateFormatSplit('${comm.edate}');
// 	var default_min = new Date(2012, 09, 08, 10, 12, 12);
// 	var default_max = new Date(2012, 10, 01, 10, 12, 12);
	</script>
	<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jquery-ui-1.8.16.custom.min.js"></script>
	<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jquery.dateFormat-1.0.js"></script>
	
	<!-- Debug -->
	<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jQRangeSliderMouseTouch.js"></script>
	<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jQRangeSliderDraggable.js"></script>
	<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jQRangeSliderHandle.js"></script>
	<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jQRangeSliderBar.js"></script>
	<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jQRangeSliderLabel.js"></script>
	<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jQRangeSlider.js"></script>

	<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jQDateRangeSliderHandle.js"></script>
	<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jQDateRangeSlider.js"></script>
	<!-- /Debug -->

	<script type="text/javascript">
    window.onload=function(){

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
        el.dateRangeSlider(opt.options);

        //선택값이 변경될 경우 호출되는 함수를 설정한다.
        el.bind("valuesChanged", function(event, data){
            var values = data.values;
            if(showCount != 0)showAlertArea();
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

    //창이 리사이즈 될 경우 사용자가 이전에 선택했던 범위 값을 재 세팅
    function touchSlider(){
        var el=$("#element");
        el.dateRangeSlider("values",default_min,default_max);

    }
    
  function formatDate(value){
		return $.format.date(value, "yyyy-MM-dd HH:mm");
	}
  
	</script>
	
<link rel="stylesheet" href="/tetrad/css/nanum.css">
<link rel="stylesheet" href="/tetrad/css/lato.css">

<% String lo2 = request.getHeader("accept-language"); %>

<style type="text/css">
	#content_graph div.graph_sub_menu{
		<%
	if(!lo2.startsWith("ko")){
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
