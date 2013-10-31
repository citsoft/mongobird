<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%> 
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/style.css">
<link href="${pageContext.request.contextPath}/css/jquery.smartPop.css" rel="stylesheet" type="text/css"/>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-latest.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/commonUtil.js"></script>
<script src="${pageContext.request.contextPath}/js/jquery.smartPop.js" type="text/javascript"></script>    
<script type="text/javascript">
	$(document).ready( function() {
		$('#btn_login').click( function(event) {goCheck();return false;});
		$('#passwd, #uid').keypress( function(event) {
		    var evCode = ( window.netscape ) ? Ev.which : event.keyCode ; 
			if ( evCode == 13 ) {
				goCheck();
			}
		});
		
		function goCheck(){
			if($('input:text[name=uid]').val()==''){
				alert("<spring:message code="login.writeid"/>");
				$('input:text[name=uid]').focus();
			}else if($('input:password[name=passwd]').val().length < 4){
				alert("<spring:message code="login.requestid"/>");
				$('input:password[name=passwd]').focus();
			}else if($('input:password[name=passwd]').val()==''){
				alert("<spring:message code="login.writepassword"/>");
				$('input:password[name=passwd]').focus();
			}else{
				document.g_login.action="login.do";
				document.g_login.submit();
			}
		}
	});
	
	function init(){
		$('input:text[name=uid]').focus();
		if("${comm.message}"!=null && "${comm.message}"!=""){
			var left = (screen.availWidth - 512) / 2;
		    var top = (screen.availHeight - 300) / 2;
			window.open("popup_login.do","Login","width=562,height=300,menubar=no,toolbar=no,location=no,status=no,resizable=no,scrollbars=no,top="+top+", left="+left);
		}
	}
	
	function openLayerdPop(dsname, devicecode, collname, dbname, title) {
	    $.smartPop.open({ background: "gray", width: 300, height: 100, url: "./registLicense.jsp"});                
	}
</script>
</head>
<body id="login" onload='init()'>
<div id="wrap_login">	
	<div class="login_header">
		<h1 class="h1_login"><a href="#"><img src="./img/mm_loginTitle.png" alt="<spring:message code="login.monadmanagementsystem"/>"></a></h1>
	    <h2 class="h2_login"><img src="./img/logo_mongoDB_login.png" width="82" height="30" alt="mongoDB"></h2>
	    <span class="login_license">
	       mongobird v${releaseVersion} | 
	       <c:choose>
	        <c:when test="${licensekey eq null or licensekey eq ''}">
	            <a href="javascript:openLayerdPop();">non-commercial version </a>
	         </c:when>
	         <c:otherwise>
	             ${licensetype} version
	         </c:otherwise>
	       </c:choose>
	    </span>	
	</div>
	<form method="post" id="g_login" name="g_login" class="login_box">  
		<fieldset>  
		<legend>login</legend> 
		<p class="p_login"><img src="./img/txt_login.gif" width="82" height="43" alt="Login"></p>
		<ul class="login_input">
			<li class="login_lb"><label class="i_label"><spring:message code="login.id"/></label></li>  
			<li><input type="text" name="uid" id="uid" maxlength="100" OnKeyPress='num_eng(event)' style='ime-mode:disabled' ></li>
			<li class="login_lb"><label class="i_label"><spring:message code="login.password"/></label></li>  
			<li><input type="password" name="passwd" id="passwd" maxlength="100" style='ime-mode:disabled' ></li> 
		</ul>
		<span class="btn_login" >  
			<input value="로그인" type="image" src="./img/btn_login<spring:message code="common.img"/>.gif" id="btn_login">  
		</span>
		</fieldset> 
	</form>
	<div class="login_copyright">COPYRIGHT(C) Cardinal Info Tech. ALL RIGHTS RESERVED</div>
	<img src="./img/common_notify/glyphicons-halflings.png" style="display:none;" width="1" height="1"/>
	<img src="./img/common_notify/glyphicons-halflings-white.png" style="display:none;" width="1" height="1"/>
</div>
</body>
</html>
