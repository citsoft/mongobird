<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
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
<title><spring:message code="graph.daemonDetails"/></title>
<link rel="stylesheet" type="text/css" href="./css/style.css">
<%if(session.getAttribute("loginAuth").equals(1)||session.getAttribute("loginAuth").equals(2)){}else{%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common_analytics.js"></script>
<%}%>
</head>
<body>
<form method="post" name="demon">
		<!-- div window start -->
		<div class="demon_detail">
			<h1>Tetrad Demo</h1>	
			<h3>
				<spring:message code="cmd.exMsg"/>
			</h3>
			<a href="#" id="pop_close" class="popup_close"><img src="./img/ico_x.gif" width="27" height="21" alt="X"></a>
			<p>
				<img src="${pageContext.request.contextPath}/img/cmdEx<spring:message code="common.img"/>.gif" width="752" alt="Example">
			</p>
				
		</div>
		<!-- // div window end -->

</form>
</body>
</html>
