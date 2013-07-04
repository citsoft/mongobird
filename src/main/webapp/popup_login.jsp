<%@ page language="java" contentType="text/html; charset=utf-8"%>
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
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common_analytics.js"></script>
<style type="text/css">
/* Default Type Selector */
*{ margin:0; padding:0; font-size:12px; font-family:돋움, Dotum, 굴림, Gulim, AppleGothic, Sans-serif;}
a{ color:#004790;}
img,
fieldset{ border:none;}
legend, caption { display:none;}
em{ font-style:normal; color:#258102;}
strong{ color:#258102;}
li{ list-style:none;}
h1 {width:460px; margin:39px 0 0 50px; padding-bottom:25px; border-bottom:1px solid #AEAEAE;}
h2 {margin:25px 0 0 36px; padding-left:10px; background:url(./img/ico_dot.gif) 0 4px no-repeat; font-size:11px; font-weight:normal; color:#444;}

/* Layout */
#wrap{position:relative; overflow:hidden;}
#header{border-top:8px solid #F265CE;}
#content{position:relative; padding:0; margin:0;}



.w561 { width:561px;}
.popup_close {position:absolute; top:-70px; right:14px;}
.msg_area {padding:39px 0 0 182px; background:url(./img/bg_loginimg.gif) 65px 25px no-repeat; line-height:17px;}
.btn_area {display:block; margin:22px 0 0 0; width:42px;}
</style>
</head>
<!-- Document Size : 359*311 -->
<body>
<div id="wrap" class="w561">
	<div id="header">
		<h1><img src="./img/tit_login.gif" width="119" height="22" alt="로그인 메시지"></h1>
	</div>
	<div id="content">
		<a href="" onClick="self.close()" class="popup_close"><img src="./img/ico_x.gif" width="7" height="6" alt="X"></a>
		<p class="msg_area"><spring:message code="login.invalidMessage"/><br><spring:message code="login.checkMessage"/><br>
		<a href="" onClick="self.close()" class="btn_area"><img src="./img/btn_confirm<spring:message code="common.img"/>.gif" width="42" height="20" alt="OK"></a>
		</p>
	</div>
</div>
</body>
</html>
