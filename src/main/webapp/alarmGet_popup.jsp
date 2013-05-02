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
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%> 
<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="main.title"/></title>
<%if(session.getAttribute("loginAuth").equals(1)||session.getAttribute("loginAuth").equals(2)){}else{%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common_analytics.js"></script>
<%}%>
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
h1 {width:660px; margin:39px 0 0 50px; padding-bottom:25px; border-bottom:1px solid #AEAEAE;}
h2 {margin:25px 0 0 36px; padding-left:10px; background:url(./img/ico_dot.gif) 0 4px no-repeat; font-size:11px; font-weight:normal; color:#444;}

/* Layout */
#wrap{position:relative; overflow:hidden;}
#header{border-top:8px solid #F265CE;}
#content{position:relative; padding:0; margin:0;}

.w425 { width:760px; }
.popup_close {position:absolute; top:-70px; right:14px;}

#menu { position:relative; padding-left:10px; width:250px; float:left; }
#menu ul { margin:0; padding:0}
#menu li { width:240px; padding:5px; border-bottom:1px dotted #cfcfcf; list-style:none}
#menu li img {vertical-align:middle; }
#menu li.end { width:240px; padding:5px; border-bottom:2px solid #cfcfcf; list-style:none;font-size:14px;}
#contents { position:relative; padding:3px 20px 20px 20px; width:400px; float:left; background:url(../img/bg_graph_bar.gif) top left no-repeat; background-size:1px 100%;}

.event_frm {margin:10px 0 0 52px; font-size:11px;}
.event_frm textarea {width:350px; margin-top:10px; padding:5px; height:245px; border:0; background:#F7F7F7;}
.btn_area {margin:10px 0 0 0; width:342px; vertical-align:middle;}
.btn_area02 a {display:inline-block; width:350px; margin-top:10px; text-align:right;}
</style>
<script type="text/javascript" src="./js/jquery-latest.js"></script>
<script type="text/javascript">
$(document).ready( function() {
	$('#pop_close, #btn_close').click(function (event) {
		window.parent.$.smartPop.close();
	});	
	
	var img = '<spring:message code="common.img"/>';
	var strHtml = "<ul><li class=\"end\"><b><img src=\"./img/event_sub01"+img+".png\"></b></li>"; 
	for(var i =0; i<parent.g_title.length; i++){
		var str = "<li><b><input type=\"image\" src=\"./img/ico_square.gif\">  " +parent.g_title[i]+ " : </b>" +parent.g_value[i]+ "</li>";
		strHtml += str;
	}
	strHtml += "</ul>";
	$('#menu').html(strHtml);
});
</script>
</head>
<!-- Document Size : 359*311 -->
<body>
<div id="wrap" class="w425">
	<div id="header">
		<h1><img src="./img/tit_event<spring:message code="common.img"/>.gif" width="142" height="23" alt="이벤트 처리내역"></h1>
	</div>
	<div id="content">
		<a href="#" id="pop_close" class="popup_close"><img src="./img/ico_x.gif" width="27" height="21" alt="X"></a>
		<div class="event_frm">
			<div id="menu"></div>
			<div id="contents">
				<form method="post" name="confirmForm" id = "confirmForm">
				<fieldset>
				<legend>이벤트 처리내역</legend>
				<span>[ ${confirm.date} ]</span>
				<textarea readonly cols="35" rows="10" name="memo" class="">${confirm.memo}</textarea>
				<div class="btn_area">
				</div>
				<div class="btn_area02">
					<a href="#"><img id="btn_close" src="./img/btn_confirm.gif" width="42" height="20" alt="OK"></a>
				</div>
				</fieldset> 
				</form>
			</div>
		</div>
	</div>
</div>
</body>
</html>
