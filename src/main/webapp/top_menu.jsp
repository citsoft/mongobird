<%@ page import="net.cit.tetrad.common.LicenseTypeEnum"%>
<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%
	if(session.getAttribute("loginAuth")==null||session.getAttribute("loginAuth").toString().length()==0||session.getAttribute("loginAuth").equals("")){
%>
<script type="text/javascript">
	window.location="${pageContext.request.contextPath}/";
</script>

<%
		return;
	}else{
%>

<%if(session.getAttribute("loginAuth").equals(1)||session.getAttribute("loginAuth").equals(2)){}else{%>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/common_analytics.js"></script>
<%}%>
<%
String strStatusTitle = "MongoDB Dashboard";
if (request.getRequestURL().indexOf("sub_page")>0){
	strStatusTitle = "데몬";
}else if (request.getRequestURL().indexOf("Graph")>0){
	strStatusTitle = "그래프";
}else if (request.getRequestURL().indexOf("sub_alarm_page")>0){
	strStatusTitle = "이벤트";
}else if (request.getRequestURL().indexOf("user.jsp")>0){
	strStatusTitle = "사용자 관리";
}else if (request.getRequestURL().indexOf("device.jsp")>0){
	strStatusTitle = "데몬 관리";
}else if (request.getRequestURL().indexOf("critical.jsp")>0){
	strStatusTitle = "임계값 관리";
}else if (request.getRequestURL().indexOf("global.jsp")>0){
	strStatusTitle = "전역 관리";
}else if (request.getRequestURL().indexOf("dsNameGraph")>0){
	strStatusTitle = "항목";
}else if (request.getRequestURL().indexOf("groupGraph")>0){
	strStatusTitle = "그룹";
}else if (request.getRequestURL().indexOf("typeGraph")>0){
	strStatusTitle = "유형";
}else if (request.getRequestURL().indexOf("deviceGraph")>0){
	strStatusTitle = "장비";
}else if (request.getRequestURL().indexOf("multiGraph")>0){
	strStatusTitle = "다중항목";
}else if (request.getRequestURL().indexOf("Lock")>0){
	strStatusTitle = "락그래프";
}else if (request.getRequestURL().indexOf("deviceLockGraph")>0){
	strStatusTitle = "장비락";
}else if (request.getRequestURL().indexOf("dbLockGraph")>0){
	strStatusTitle = "db락";
}else if (request.getRequestURL().indexOf("typeLockGraph")>0){
	strStatusTitle = "유형락";
}else if (request.getRequestURL().indexOf("groupLockGraph")>0){
	strStatusTitle = "그룹락";
}
%>

<link rel="stylesheet" href="${pageContext.request.contextPath}/css/lato.css"/>
<link rel="stylesheet" href="${pageContext.request.contextPath}/css/nanum.css"/>

<style>

 A:link{text-decoration:none}
 A:visited{text-decoration:none}

#nav ul {
	display: none;
	position: absolute;
	top: 33px;
	left: 0;
}

#nav ul ul {
	top: 3px;
	left: 144px;
}

<% String lo = request.getHeader("accept-language"); %>

.arrow{
	text-align: right;
	display: inline-block;
	width: 10px;
}

.label_of_arrow{
	text-align: left;
	display: inline-block;
	width: 115px;
}


.menu {
	<%
	if(!lo.startsWith("ko")){
%>
	font-family: 'Lato Reg';  
<%}else{%>
	font-family: 'Nanum Reg'; 
<%}%>
	font-size: 1.4em;
	font-weight: bold;
	padding-top:7px;
	text-align: center;
	color: white;
	height:25px;
	width:100px;
}


.menu_gradient {
background: #a9de00; /* Old browsers */
background: -moz-linear-gradient(-45deg,  #a9de00 0%, #55ab02 100%); /* FF3.6+ */
background: -webkit-gradient(linear, left top, right bottom, color-stop(0%,#a9de00), color-stop(100%,#55ab02)); /* Chrome,Safari4+ */
background: -webkit-linear-gradient(-45deg,  #a9de00 0%,#55ab02 100%); /* Chrome10+,Safari5.1+ */
background: -o-linear-gradient(-45deg,  #a9de00 0%,#55ab02 100%); /* Opera 11.10+ */
background: -ms-linear-gradient(-45deg,  #a9de00 0%,#55ab02 100%); /* IE10+ */
background: linear-gradient(135deg,  #a9de00 0%,#55ab02 100%); /* W3C */
filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#a9de00', endColorstr='#55ab02',GradientType=1 ); /* IE6-9 fallback on horizontal gradient */

	<%
	if(!lo.startsWith("ko")){
%>
	font-family: 'Lato Reg';
<%}else{%>
	font-family: 'Nanum Reg';
<%}%>
font-size: 1.4em;
font-weight: bold;
padding-top:7px;
text-align: center;
color: white;
height:25px;
width:100px;
}

.menu_gradient_for_configuration {
background: #a9de00; /* Old browsers */
background: -moz-linear-gradient(-45deg,  #a9de00 0%, #55ab02 100%); /* FF3.6+ */
background: -webkit-gradient(linear, left top, right bottom, color-stop(0%,#a9de00), color-stop(100%,#55ab02)); /* Chrome,Safari4+ */
background: -webkit-linear-gradient(-45deg,  #a9de00 0%,#55ab02 100%); /* Chrome10+,Safari5.1+ */
background: -o-linear-gradient(-45deg,  #a9de00 0%,#55ab02 100%); /* Opera 11.10+ */
background: -ms-linear-gradient(-45deg,  #a9de00 0%,#55ab02 100%); /* IE10+ */
background: linear-gradient(135deg,  #a9de00 0%,#55ab02 100%); /* W3C */
filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#a9de00', endColorstr='#55ab02',GradientType=1 ); /* IE6-9 fallback on horizontal gradient */

	<%
	if(!lo.startsWith("ko")){
%>
	font-family: 'Lato Reg';
<%}else{%>
	font-family: 'Nanum Reg';
<%}%>
font-size: 1.25em;
font-weight: bold;
padding-top:7px;
text-align: center;
color: white;
height:25px;
width:100px;
}

.sub_menu {
		<%
	if(!lo.startsWith("ko")){
%>
	font-family: 'Lato Reg';
<%}else{%>
	font-family: 'Nanum Reg';
<%}%>
	font-size: 1.4em;
	font-weight: bold;
	text-align: left;
	background-color: #383838;
	color: white;
	height:26px;
	width:132px;
	padding-top:3px;
	padding-left:13px;
}

.sub_menu_mid{
<%
	if(!lo.startsWith("ko")){
%>
	font-family: 'Lato Reg';
<%}else{%>
	font-family: 'Nanum Reg';
<%}%>
	background-color: #383838;
	height:26px;
	width:132px;
	margin: 0px; border: solid 1px red; padding: 0px;
}

.middle_menu_arrow{
	<%
	if(!lo.startsWith("ko")){
%>
	font-family: 'Lato Reg';
<%}else{%>
	font-family: 'Nanum Reg';
<%}%>
	margin: 0px; border: 0px; padding: 0px;
}

.sub_menu_gradient {
	background: #a9de00; /* Old browsers */
	background: -moz-linear-gradient(-45deg,  #a9de00 0%, #55ab02 100%); /* FF3.6+ */
	background: -webkit-gradient(linear, left top, right bottom, color-stop(0%,#a9de00), color-stop(100%,#55ab02)); /* Chrome,Safari4+ */
	
	background: -webkit-linear-gradient(-45deg,  #a9de00 0%,#55ab02 100%); /* Chrome10+,Safari5.1+ */
	background: -o-linear-gradient(-45deg,  #a9de00 0%,#55ab02 100%); /* Opera 11.10+ */
	background: -ms-linear-gradient(-45deg,  #a9de00 0%,#55ab02 100%); /* IE10+ */
	background: linear-gradient(135deg,  #a9de00 0%,#55ab02 100%); /* W3C */
	filter: progid:DXImageTransform.Microsoft.gradient( startColorstr='#a9de00', endColorstr='#55ab02',GradientType=1 ); /* IE6-9 fallback on horizontal gradient */

		<%
	if(!lo.startsWith("ko")){
%>
	font-family: 'Lato Reg';
<%}else{%>
	font-family: 'Nanum Reg';
<%}%>
	font-size: 1.4em;
	font-weight: bold;
	text-align: left;
	color: white;
	height:26px;
	width:132px;
	padding-top:3px;
	padding-left:13px;
}
</style>

<script>

$(function(){
	var lists = ['#sub_page','#ShardInfo','#normal_status','#_db','#_Lock',
	             '#dsNameGraph','#multiGraph','#groupGraph','#deviceGraph'
	             ,'#allDb','#deviceDb','#deviceLock','#dbLock','#groupLock'
	             ,'#_user','#_instance','#_global','#_threshold'];
	
	for(var i in lists){
		(function(){
			var id = lists[i];
			$(id).hover(function(){
				mouserenter(id);
			},function(){
				mouseleave(id);
			});	
		})();
}});
var mouserenter=function(id){
	mouseHover(id,'#7d7d7d');
};

var mouseleave = function(id){
	mouseHover(id,'#383838');
};

var mouseHover = function(id,bgc){
	var div = $(id);
	<%
	if(!lo.startsWith("ko")){
	%>
		div.css('font-family','Lato Reg');
	<%}else{%>
		div.css('font-family','Nanum Reg');
	<%}%>
	div.css('font-size','1.4em');
	div.css('background-color',bgc);
};
</script>

<div id="header">
	<div class="top_area">
		<ul class="logo">
			<li class="first"><a href="http://mongobird.citsoft.net/"><img src="${pageContext.request.contextPath}/img/mm_title.png" alt="운영관리 시스템"></a></li>
			<li><a href="http://www.mongodb.org/" target="_blank"><img src="./img/logo_mongoDB.png" width="96" height="35" alt="mongoDB"></a></li>
		</ul>
		<ul class="banner_area">
			<li><div><iframe id="banner" frameborder="0" src="" align="left" scrolling="no"></iframe></div></li>
		</ul>
		<ul class="top_sign" style="height:60px;">
				<li id="balancing" style="width:55px;" title="Balancing"><a href="subShardInfoView.do"><span id="shardingCheck" style="display:none;"><img id="mongos_img" src="./img/btn_balancing.gif" width="52" height="52"></span></a></li>
				<li style="width:40px;"></li>
				<li id="mongos" title="mongos"><a href="#"><img id="mongos_img" src="./img/btn_sign_01_gray.gif" width="52" height="52" alt="sign_01"></a></li>
				<li id="mongod" title="mongod"><a href="#"><img id="mongod_img" src="./img/btn_sign_02_gray.gif" width="52" height="52" alt="sign_02"></a></li>
				<li id="config" title="config" class="last"><a href="#"><img id=	"config_img" src="./img/btn_sign_03_gray.gif" width="52" height="52" alt="sign_03"></a></li>
		</ul>
	</div>
	<div class="gnb_area" id="globalNavi">
		<ul class="gnb" id="nav">
			<li class="first">
				<a href="mainView.do">
					<div class="<%=request.getRequestURL().indexOf("main")>0?"menu_gradient":"menu" %>"><spring:message code="menu.dashboard"/></div>
				</a>
			</li>
			<li>
				<a href="subPageView.do">
					<div class="<%=request.getRequestURL().indexOf("sub_page")>0||request.getRequestURL().indexOf("ShardInfo")>0?"menu_gradient":"menu" %>"><spring:message code="menu.instance"/></div>
				</a>
				<ul>
					<li>
						<a href="subPageView.do">
								<div id="sub_page" class="<%=request.getRequestURL().indexOf("sub_page")>0?"sub_menu_gradient":"sub_menu" %>"><spring:message code="menu.status"/></div>
						</a>
					</li>
					<li>
						<a href="subShardInfoView.do">
							<div id="ShardInfo" class="<%=request.getRequestURL().indexOf("ShardInfo")>0?"sub_menu_gradient":"sub_menu" %>"><spring:message code="menu.sharding"/></div>
						</a>
					</li>
				</ul>
			</li>
			<li>
				<a href="dsNameGraphView.do">  
					<div class="<%=request.getRequestURL().indexOf("dsNameGraph")>0||request.getRequestURL().indexOf("multiGraph")>0
 ||request.getRequestURL().indexOf("groupGraph")>0||request.getRequestURL().indexOf("deviceGraph")>0
 ||request.getRequestURL().indexOf("allDb")>0||request.getRequestURL().indexOf("deviceDb")>0
 ||request.getRequestURL().indexOf("deviceLock")>0||request.getRequestURL().indexOf("dbLock")>0
 ||request.getRequestURL().indexOf("groupLock")>0?"menu_gradient":"menu" %>"><spring:message code="menu.graph"/></div>
				</a>
				<ul>
					<li>  
						<a href="dsNameGraphView.do">
							<div  id='normal_status' class="<%=request.getRequestURL().indexOf("dsNameGraph")>0||request.getRequestURL().indexOf("multiGraph")>0
  ||request.getRequestURL().indexOf("groupGraph")>0||request.getRequestURL().indexOf("deviceGraph")>0?"sub_menu_gradient":"sub_menu" %>">
  
 					<label class='label_of_arrow'><spring:message code="menu.normal_status"/></label><label class='arrow'>></label>
 
  							</div>
						</a>
						<ul>
							<li>
								<a href="dsNameGraphView.do">
									<div id="dsNameGraph" class="<%=request.getRequestURL().indexOf("dsNameGraph")>0?"sub_menu_gradient":"sub_menu" %>"><spring:message code="menu.single_item"/></div>
								</a>
							</li>
							<li>
								<a href="multiGraphView.do">
									<div id="multiGraph" class="<%=request.getRequestURL().indexOf("multiGraph")>0?"sub_menu_gradient":"sub_menu" %>"><spring:message code="menu.multi_item"/></div>
								</a>
							</li>
							<li>
								<a href="groupGraphView.do">
									<div id="groupGraph" class="<%=request.getRequestURL().indexOf("groupGraph")>0?"sub_menu_gradient":"sub_menu" %>"><spring:message code="menu.group"/></div>
								</a>
							</li>
							<li>
								<a href="deviceGraphView.do">
									<div id="deviceGraph" class="<%=request.getRequestURL().indexOf("deviceGraph")>0?"sub_menu_gradient":"sub_menu" %>"><spring:message code="menu.instance"/></div>
								</a>
							</li>
						</ul>
					</li>
					<li>
						<a href="multiGraphView.do">
							<div id='_db'class="<%=request.getRequestURL().indexOf("allDb")>0||request.getRequestURL().indexOf("deviceDb")>0?"sub_menu_gradient":"sub_menu" %>">
							
							<label class='label_of_arrow'><spring:message code="menu.db"/></label><label class='arrow'>></label>
  							
  							</div>
						</a>
						<ul>
							<li>
								<a href="allDbGraphView.do">
									<div id="allDb" class="<%=request.getRequestURL().indexOf("allDb")>0?"sub_menu_gradient":"sub_menu" %>"><spring:message code="menu.total_db_graph"/></div>
								</a>
							</li>
							<li>
								<a href="deviceDbGraphView.do">
									<div id="deviceDb" class="<%=request.getRequestURL().indexOf("deviceDb")>0?"sub_menu_gradient":"sub_menu" %>"><spring:message code="menu.instance"/></div>
								</a>
							</li>
						</ul>
					</li>
					<li>
						<a href="groupGraphView.do">
							<div id='_Lock' class="<%=request.getRequestURL().indexOf("deviceLock")>0||request.getRequestURL().indexOf("dbLock")>0
  ||request.getRequestURL().indexOf("groupLock")>0?"sub_menu_gradient":"sub_menu" %>">
  							
  							<label class='label_of_arrow'><spring:message code="menu.lock"/></label><label class='arrow'>></label>
  							
  							</div>
						</a>
						<ul>
							<li>
								<a href="deviceLockGraphView.do">
									<div id="deviceLock" class="<%=request.getRequestURL().indexOf("deviceLock")>0?"sub_menu_gradient":"sub_menu" %>"><spring:message code="menu.instance"/></div>
								</a>
							</li>
							<li>
								<a href="dbLockGraphView.do">
									<div id="dbLock" class="<%=request.getRequestURL().indexOf("dbLock")>0?"sub_menu_gradient":"sub_menu" %>"><spring:message code="menu.db"/></div>
								</a>
							</li>
							<li>
								<a href="groupLockGraphView.do">
									<div id="groupLock" class="<%=request.getRequestURL().indexOf("groupLock")>0?"sub_menu_gradient":"sub_menu" %>"><spring:message code="menu.group"/></div>
								</a>
							</li>
						</ul>
					</li>
				</ul>
			</li>
			<li>
				<a href="subAlarmView.do?alarm=4">
					<div class="<%=request.getRequestURL().indexOf("sub_alarm_page")>0?"menu_gradient":"menu" %>"><spring:message code="menu.event"/></div>
				</a>
			</li>
			<%if(session.getAttribute("loginAuth").equals(1)||session.getAttribute("loginAuth").equals(2)){%>
				<li class="last">
					<a href="listManagement.do?dival=3">
						
						<div class="<%=request.getRequestURL().indexOf("user.jsp")>0||request.getRequestURL().indexOf("device.jsp")>0
 ||request.getRequestURL().indexOf("global.jsp")>0||request.getRequestURL().indexOf("critical.jsp")>0?"menu_gradient_for_configuration":"menu" %>"><spring:message code="menu.configuration"/></div>
					</a>
					<ul>
						<li>
								<a href="listManagement.do?dival=0">
									<div id="_user" class="<%=request.getRequestURL().indexOf("user.jsp")>0?"sub_menu_gradient":"sub_menu" %>"><spring:message code="menu.user"/></div>
								</a>
							</li>
							<li>
								<a href="listManagement.do?dival=1">
									<div id="_instance" class="<%=request.getRequestURL().indexOf("device.jsp")>0?"sub_menu_gradient":"sub_menu" %>"><spring:message code="menu.instance"/></div>
								</a>
							</li>
							<li>
								<a href="listManagement.do?dival=3">
									<div id="_global" class="<%=request.getRequestURL().indexOf("global.jsp")>0?"sub_menu_gradient":"sub_menu" %>"><spring:message code="menu.global"/></div>
								</a>
							</li>
							<li>
								<a href="listManagement.do?dival=2">
									<div id="_threshold" class="<%=request.getRequestURL().indexOf("critical.jsp")>0?"sub_menu_gradient":"sub_menu" %>"><spring:message code="menu.event"/></div>
								</a>
							</li>
					</ul>
				</li>
			<%}%>
			<li class="last right"><a href="http://monad.citsoft.net/" target="_blank"><img src="${pageContext.request.contextPath}/img/help_icon_txt<spring:message code="common.img"/>.png" height="25" alt="<spring:message code="common.help"/>"></a></li>
		</ul>
	</div>	
</div>
<%}%>
