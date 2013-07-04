<%@ page language="java" contentType="text/html; charset=utf-8"%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-latest.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.droppy.js"></script>
<%@ include file="./common_notify.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/main_js/main_trafficLight.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/banner.js"></script>
<script type="text/javascript">
	$(function(){
		$('#nav').droppy();
	});
</script>
