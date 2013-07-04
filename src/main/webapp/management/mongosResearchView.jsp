<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<% request.setCharacterEncoding("UTF-8"); %>

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
<style type="text/css">
/* Default Type Selector */
*{ margin:0; padding:0; font-size:12px; font-family:돋움, Dotum, 굴림, Gulim, AppleGothic, Sans-serif;}
a{ color:#004790;}
img,
fieldset{ border:none;}
legend, caption { display:none;}
em{ font-style:normal; color:#258102;}
strong{ color:#258102;}
h1 {width:1030px; margin:39px 0 0 20px; padding-bottom:25px; border-bottom:1px solid #AEAEAE;}
/* h2 {margin:25px 0 0 36px; padding-left:10px; background:url(./img/ico_dot.gif) 0 4px no-repeat; font-size:11px; font-weight:normal; color:#444;} */

/* Layout */
#wrap{position:relative; overflow:hidden;}
#header{border-top:8px solid #F265CE;}
#content{position:relative; padding:0; margin:0; }
#content input{vertical-align:text-top;}

.w425 { width:1070px; }
.popup_close {position:absolute; top:-70px; right:14px;}

#menu { position:relative; padding-left:10px; width:260px; float:left; }
.tb_list_01 { width:250px; padding:5px; }
.tb_list_01 thead th{ height:28px; border-bottom:2px solid #cfcfcf; font-size:14px; text-align:left;}
.tb_list_01 tbody td{ height:28px; border-bottom:1px dotted #cfcfcf; }
.tb_list_01 tbody td.noline { border:0; text-align:right;}

#contents { position:relative; padding:10px 5px 0 10px; width:760px; float:left; background:url(./img/bg_graph_bar.gif) top left no-repeat; background-size:1px 100%;}
.tb_list_02 { width:730px; }
.tb_list_02 thead th{ height:35px; background:#5C5C5C; color:#FFF; }
.tb_list_02 td:nth-child(1), th:nth-child(1) { width: 30px; }
.tb_list_02 td:nth-child(2), th:nth-child(2) { width: 100px; }
.tb_list_02 td:nth-child(3), th:nth-child(3) { width: 150px; }
.tb_list_02 td:nth-child(4), th:nth-child(4) { width: 90px; }
.tb_list_02 td:nth-child(5), th:nth-child(5) { width: 110px; }
.tb_list_02 td:nth-child(6), th:nth-child(6) { width: 80px; }
.tb_list_02 td:nth-child(7), th:nth-child(7) { width: 80px; }
.tb_list_02 td:nth-child(8), th:nth-child(8) { width: 80px; }
.tb_list_02 tbody td{ height:28px; border-bottom:1px dotted #cfcfcf; padding:0 5px; }
.tb_list_02 tbody td.rb {border-right:1px dotted #cfcfcf;}
.tb_list_02 tbody td.lb {border-left:1px dotted #cfcfcf;}

.event_frm {margin:10px 0 0 22px; font-size:11px;}
.btn_area {margin:10px 0 0 0; display:inline-block; padding-top:5px; width:720px; text-align:right;}

</style>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.showLoading.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery-latest.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/commonUtil.js"></script>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.showLoading.js"></script>
<script type="text/javascript">
var searchCnt = 0;
function mongosResearch(){
	jQuery('html').showLoading();
	$.ajax({
		type:'POST',
		url : 'mongosResearch.do',
		data : $('#setForm').serialize(),
		dataType:'html',
		success:function(json,textStatus){
			$('#researchTB tbody').empty();
			if(json == ""){
				alert("<spring:message code="daemons.serverNoRespondAuth"/>");
			}else{
				var memorysizeStr = $('#memorysize').val();
				var hddsizeStr = $('#hddsize').val();
				var authUserStr = $('#authUser').val();
				var authPasswdStr = $('#authPasswd').val();
				
				var groupNameStr = "";
				var uidStr = "";
				var typeStr = "";
				var ipStr = "";
				var portStr = "";
				var ckBoxVal = "";
				var tb = "";
				var isExist = false;
				
				searchCnt = 0;
				var jsonData = eval("(" + json + ")");
				for(var i in jsonData.aaData){
						groupNameStr = jsonData.aaData[i].groupNameStr;
						ipStr = jsonData.aaData[i].ipStr;
						portStr = jsonData.aaData[i].portStr;
						uidStr = jsonData.aaData[i].uidStr;
						typeStr = jsonData.aaData[i].typeStr;
						isExist = jsonData.aaData[i].isExistCheck;
					if(isExist){
						tb += '<tr><td class="lb">-</td><td>'+groupNameStr+'</td><td>'+uidStr+'</td><td>'+typeStr+'</td><td>'+ipStr+'</td><td>'+portStr+'</td><td>'+memorysizeStr+'</td><td class="rb">'+hddsizeStr+'</td></tr>';
					}else{
						ckBoxVal = groupNameStr +"|"+ uidStr +"|"+ typeStr +"|"+ ipStr +"|"+ portStr +"|"+ memorysizeStr +"|"+ hddsizeStr +"|"+ authUserStr +"|"+ authPasswdStr;
						tb += '<tr><td class="lb"><input type="checkbox" id="researchLst" name="researchLst" value="'+ckBoxVal+'"/></td><td>'+groupNameStr+'</td><td>'+uidStr+'</td><td>'+typeStr+'</td><td>'+ipStr+'</td><td>'+portStr+'</td><td>'+memorysizeStr+'</td><td class="rb">'+hddsizeStr+'</td></tr>';
					}
					searchCnt ++;
				}
				$('#researchTB tbody').append(tb);
				$('#cntView').text("<spring:message code="common.total"/> : "+ searchCnt +" <spring:message code="common.totals"/>");
			}
			jQuery('html').hideLoading();	
 		},
 		error:function(xhr,textStatus, errorThrown){
			jQuery('html').hideLoading();	
 		}
	});
}

function deviceTextNullCheck(fnCallback){
	if($.trim($('input:text[name=ip]').val())==''){
		alert("<spring:message code="daemons.writeip"/>");
		$('input:text[name=ip]').focus();
	}else if($.trim($('input:text[name=port]').val())==''){
		alert("<spring:message code="daemons.writeport"/>");
		$('input:text[name=port]').focus();
	}else if($.trim($('input:text[name=hddsize]').val())==''){
		alert("<spring:message code="daemons.writehard"/>");
		$('input:text[name=hddsize]').focus();
	}else if($.trim($('input:text[name=memorysize]').val())==''){
		alert("<spring:message code="daemons.writememory"/>");
		$('input:text[name=memorysize]').focus();
	}else if($('#authCheck').is(':checked') && $.trim($('#setForm input:text[name=authUser]').val())==''){
		alert("<spring:message code="daemons.writeAuthUser"/>");
		$('input:text[name=authUser]').focus();
	}else if($('#authCheck').is(':checked') && $.trim($('#setForm input:password[name=authPasswd]').val())==''){
		alert("<spring:message code="daemons.writeAuthPasswd"/>");
		$('#authPasswd').focus();
	}else{				
		fnCallback();
	}
}

function researchDeviceInsert(){
	jQuery('html').showLoading();
	$.ajax({
		type:'POST',
		url : 'researchDeviceInsert.do',
		data : $('#confirmForm').serialize(),
		dataType:'html',
		success:function(json,textStatus){
			var jsonSplit = json.split("|");
				var nameLst = jsonSplit[0];
				var cnt = jsonSplit[1];
				if(cnt != searchCnt)alert(searchCnt+"개 중, "+cnt+"개가 등록되었음.");
// 				alert(nameLst+"가 등록되었숨 \n총 "+cnt+"개");
				window.parent.location.reload();
				popClose();
			jQuery('html').hideLoading();	
		},
		error:function(xhr,textStatus, errorThrown){
			jQuery('html').hideLoading();	
		}
	});
}

function authCheckFunc(){
	 if($('#authCheck').is(":checked")){
			$('#authUser, #authPasswd').removeAttr('disabled');
	  }else{
		  $('#authUser, #authPasswd').val('');
			$('#authUser, #authPasswd').attr('disabled',true);
	  	}
}

function popClose(){
	window.parent.$.smartPop.close();
}
$(function(){	
	$('#checkAll').click(function(){//checkAll 체크 박스 클릭 시 모두 체크 되도록
		if ($('#checkAll').attr('checked')) {
				$('#researchTB input:checkbox').attr('checked', true);
	  }else{
	    	$('#researchTB input:checkbox').removeAttr('checked');
	    }
	});

	$('#pop_close').click(function (event) {
		popClose();
	});	

	$('#authCheck').click(function(){
		 authCheckFunc();
	 });
	
	$('#btn_search').click(function(){
		deviceTextNullCheck(mongosResearch);
	 });
	
	$('#btn_confirm').click(function(){
		var dataToSend = $('#researchTB tbody input:checkbox:checked').length;
		if(dataToSend==0){
			alert("<spring:message code="common.nolist"/>");
		}else{
			researchDeviceInsert();
		}			
	});
});

function init(){
	$('#authUser, #authPasswd').attr('disabled',true);
}
</script>
</head>
<body id="index" onload='init()'>
<div id="wrap" class="w425">
	<div id="header">
		<h1><img src="./img/autoDetect_txt<spring:message code="common.img"/>.png"></h1>
	</div>
	<div id="content">
		<a href="#" id="pop_close" class="popup_close"><img src="./img/ico_x.gif" width="27" height="21" alt="X"></a>
		<div class="event_frm">
			<div id="menu">
				<form method="post" name="setForm" id="setForm">
					<table class="tb_list_01">
						<thead>
							<tr>
								<th colspan="2"><spring:message code="daemons.daemoninfo"/> </th>
							</tr>
						</thead>
						<tbody>
							<tr>
								<td><spring:message code="daemons.type"/> : </td>
								<td>mongos</td>
							</tr>
							<tr>
								<td>IP : </td>
								<td><input type="text" name="ip" id="ip" size="20" maxlength="50" style='ime-mode:disabled'/></td>
							</tr>
							<tr>
								<td>Port : </td>
								<td><input type="text" name="port" id="port" size="20" maxlength="20" OnKeyPress='num_only(event)' style='ime-mode:disabled'/></td>
							</tr>
							<tr>
								<td><spring:message code="daemons.memory"/> : </td>
								<td><input type="text" name="memorysize" id="memorysize" size="20" maxlength="20" OnKeyPress='num_only(event)' style='ime-mode:disabled'/></td>
							</tr>
							<tr>
								<td><spring:message code="daemons.hard"/> : </td>
								<td><input type="text" name="hddsize" id="hddsize" size="20" maxlength="20" OnKeyPress='num_only(event)' style='ime-mode:disabled'/></td>
							</tr>
							<tr>
								<td colspan="2"><input type="checkbox" id="authCheck" name="authCheck"> with auth</td>
							</tr>
							<tr>
								<td>admin user : </td>
								<td><input type="text" name="authUser" id="authUser" size="20" maxlength="100" style='ime-mode:disabled'></td>
							</tr>
							<tr>
								<td>password : </td>
								<td><input type="password" name="authPasswd" id="authPasswd" size="20" maxlength="200" style='ime-mode:disabled'></td>
							</tr>
							<tr>
								<td class="noline" colspan="2">
									<a href="#" id="btn_search"><img src="${pageContext.request.contextPath}/img/btn_search<spring:message code="common.img"/>.gif"></a> 
								</td>
							</tr>
						</tbody>
					</table>
				</form>
			</div>
			<div id="contents">
			<div id="cntView"></div>
					<table class="tb_list_02" style='padding-top:5px;'>
						<thead>
							<tr style='position:relative;top:expression!(this.offsetParent.scrollTop);"'>
								<th><input type="checkbox" id="checkAll" name="checkAll"/></th>
								<th><p><spring:message code="common.group"/></p></th>
								<th><p><spring:message code="common.daemon"/></p></th>
								<th><p><spring:message code="daemons.type"/></p></th>
								<th><p>IP</p></th>
								<th><p>Port</p></th>
								<th><p><spring:message code="daemons.memory"/> (GB)</p></th>
								<th><p><spring:message code="daemons.hard"/> (GB)</p></th>	
							</tr>
						</thead>
					</table>
				<div style="overflow:auto;width:747px;max-height:300px;">
					<form method="post" name="confirmForm" id = "confirmForm">
					<table id="researchTB" class="tb_list_02">
						<tbody style='width:100%;max-height:100px;overflow:auto;'>
						</tbody>
					</table>
					</form>
				</div>
				<div class="btn_area">
					<a href="#" id="btn_confirm"><img src="./img/btn_register<spring:message code="common.img"/>.gif" width="42" height="20"></a>
				</div>
			</div>
		</div>
	</div>
</div>
</body>
</html>
