<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="main.title"/></title>
<%@ include file="./manageCommon.jsp" %>
	<link rel="stylesheet" href="${pageContext.request.contextPath}/css/jquery_ui_css/slider_css/jquery-ui-1.8.10.custom.css">  
	<script src="${pageContext.request.contextPath}/js/jquery_ui/slider_js/jquery-ui-1.8.16.custom.min.js"></script>
<script type="text/javascript">
	var mainRefreshPeriodMinute = ${mainRefreshPeriodMinute};
</script>
<script type="text/javascript">

	$(document).ready( function() {
		$.ajax({
			type:'GET',
			url : 'list.do?dival=${comm.dival}',
			dataType:'html',
			success:function(json,textStatus){
				// 특정 키에 따른 값 검색하기
				var jsonData = eval("(" + json + ')');
				for(var i=0;i<jsonData.aaData.length;i++){
					var uid = jsonData.aaData[i].uid;
					if(uid=="logKeepPeriodDay"){
						$('#logKeepPeriodDay').text(jsonData.aaData[i].value +" <spring:message code="global.day"/>");
					}else if(uid=="hostname"){
						$('#hostnameView').text(jsonData.aaData[i].value);
						$('#hostname').val(jsonData.aaData[i].value);
					}else if(uid=="mainRefreshPeriodMinute"){
						var min = PeriodMinuteFormat(jsonData.aaData[i].value);
						$("#mainRefreshPeriodMinute").val(min);
					}else if(uid=="maingraph_period"){
					    if(jsonData.aaData[i].value==1){
							$('#maingraph_period').text("<spring:message code="global.day"/>");
					    }else if(jsonData.aaData[i].value==2){
							$('#maingraph_period').text("<spring:message code="global.week"/>");
					    }else if(jsonData.aaData[i].value==3){
							$('#maingraph_period').text("<spring:message code="global.month"/>");
					    }
					}else if(uid=="email"){
						$('#emailView').text(jsonData.aaData[i].value);
					}else if(uid=="mongo_version"){
						var version = jsonData.aaData[i].value;
						$('#globalForm input:radio[name=mongo_version]').filter('[value="'+version+'"]').attr('checked', true);
					}
				}
			},
			error:function(xhr,textStatus, errorThrown){
				
			}
		});
		
		function update(){
			var url = "insertGlobal.do";
			var dataToSend = $('#globalForm').serialize();
			var callback = function(dataReceived){
				alert('<spring:message code="global.restartMsg"/>');
				parent.location.reload();
			};
			var typeOfData = 'html';
			$.get(url,dataToSend,callback,typeOfData);
		}
		
		$('#hostnameView').click( function(event){
			hostnameShow();
		});

		$('#insert_btn').click( function(event){
			if($.trim($('#hostname').val()) == ''){
				alert('<spring:message code="global.writeSystemName"/>');
				$('#hostname').focus();
			}else{
				update();
			}
		});
		
		$('#cancel_btn').click( function(event){
			$('#hostname').val($('#hostnameView').text());
			hostnameViewShow();
		});
		
		$('#mainRefreshPeriodMinute, #mongo_version').change(update);
	});
	
	function hostnameViewShow(){
		$('#hostnameView').show();
		$('#hostname, #insert_btn, #cancel_btn').hide();
	}
	
	function hostnameShow(){
		$('#hostnameView').hide();
		$('#hostname').show().focus();
		$('#insert_btn, #cancel_btn').show();
	}
	
	function deleteCntCheck(){
		$.ajax({
			type:'POST',
			url : 'deleteMongoLogCnt.do',
			dataType:'html',
			success:function(json,textStatus){
				var result = json.split("|");
				var processPer = result[0];
				var deleteState = result[1];
				$('#processPerState').html("( "+processPer+"% <spring:message code="global.deletePro"/> )");
				if(deleteState == "false"){
					clearInterval(cntCheckInterval);
					alert("<spring:message code="global.deleteSucc"/>");
					location.reload();
				}
			},
			error:function(xhr,textStatus, errorThrown){
				
			}
		});
	}
	
	function deleteDateCheck(){
		var endDateStr = $('#endDateStr').val();
		if($.trim(endDateStr) == ''){
			alert("<spring:message code="event.chooseDate"/>");
			$('#endDateStr').focus();
		}else{
			$.ajax({
				type:'POST',
				url : 'deleteMongoDateCheck.do',
				data : {"endDateStr": endDateStr},
				dataType:'html',
				success:function(json,textStatus){
					if(json == -1){
						alert("<spring:message code="event.chooseDate"/>");
						$('#endDateStr').focus();
					}else{
						deleteLogStart();
					}
				},
				error:function(xhr,textStatus, errorThrown){
					
				}
			});
		}
	}
	
	function deleteLogStart(){
		var answer = confirm("<spring:message code="common.wanttodelete"/>");
		if (answer){
			var startDateStr = $('#startDateStr').val();
			var endDateStr = $('#endDateStr').val();
			$.ajax({
				type:'POST',
				url : 'deleteMongoLogStart.do',
				data : {"startDateStr" : startDateStr, "endDateStr": endDateStr},
				dataType:'html',
				success:function(json,textStatus){
					if(json == "nodate"){
						alert("<spring:message code="global.nodate"/>");
					}else{
						location.reload();
					}
				},
				error:function(xhr,textStatus, errorThrown){
					
				}
			});
		}
	}
	
	function deleteLogStop(){
		var answer = confirm("<spring:message code="global.deleteStop"/>");
		if (answer){
			$.ajax({
				type:'POST',
				url : 'deleteMongoLogStop.do',
				dataType:'html',
				success:function(json,textStatus){
					location.reload();
				},
				error:function(xhr,textStatus, errorThrown){
					
				}
			});
		}
	}
	
	var cntCheckInterval;
	function init(){
		if("${comm.message}"!=null && "${comm.message}"!=""){
			alert("${comm.message}");
		}
		hostnameViewShow();
		 var pickerOpts = { dateFormat:"yy-mm-dd" };
			$('#endDateStr').datepicker(pickerOpts);
	 		if('${deleteState}' == "true")cntCheckInterval = setInterval(function() { deleteCntCheck(); }, 1000);
	}
	
</script>
</head>
<body id="index" onload="init()">
<div id="wrap">
<!-- header start -->
	<%@ include file="/top_menu.jsp"%>
<!-- // header end -->
<!-- container start-->
	<div id="container">
		<!-- content start -->
		<div id="content_m">
			<form method="post" name="globalForm" id="globalForm">
			<input type="hidden" id="dival" name="dival" value="${comm.dival}"/>
				<table id="list" border="0" class="tb_list_05" summary="메인 테이블 05">
					<colgroup>
						<col width="533">
						<col width="178">
						<col width="178">
						<col width="178">
					</colgroup>
					<thead>
					<tr>
						<th><p><spring:message code="global.items"/></p></th>
						<th colspan="3"><spring:message code="global.content"/></th>
					</tr>
					</thead>
					<tbody>
						<tr>
							<td class="lb tableFiguresLeft2">System name</td>
							<td class="rb tableFiguresLeft2" colspan="3">
								<div id="hostnameView"></div>
								<input type="text" id="hostname" name="hostname" size="20" maxlength="30" style='ime-mode:disabled'/>
								<a href="#">
									<img id="insert_btn" src="./img/btn_register<spring:message code="common.img"/>.gif" width="42" height="20">
									<img id="cancel_btn" src="./img/btn_cancel<spring:message code="common.img"/>.gif" width="42" height="20">
								</a>
							</td>
						</tr>
						<tr>
							<td class="lb tableFiguresLeft2"><spring:message code="global.periodoflogpreservation"/></td>
							<td class="rb tableFiguresLeft2" colspan="3">
								<div id="logKeepPeriodDay"></div>
<!-- 								<input type="text" id="logKeepPeriodDay" name="logKeepPeriodDay" maxlength="5" OnKeyPress='num_only(event)'> 일 -->
							</td>
						</tr>
						<tr>
							<td class="lb tableFiguresLeft2"><spring:message code="global.refreshperiodofdashboard"/></td>
							<td class="rb tableFiguresLeft2" colspan="3">
								<select id="mainRefreshPeriodMinute" name="mainRefreshPeriodMinute">
									<c:forEach var="i" begin="1" end="60" step="1" varStatus ="status">
										<option value="${i}">
											${i}
										</option>
									</c:forEach>
									<c:forEach var="i" begin="70" end="200" step="10" varStatus ="status">
										<option value="${i}">
											${i}
										</option>
									</c:forEach>
								</select>
								<spring:message code="global.sec"/>
<!-- 								<div id="mainRefreshPeriodMinute"></div> -->
<!-- 								<input type="text" id="mainRefreshPeriodMinute" name="mainRefreshPeriodMinute" maxlength="5" OnKeyPress='num_only(event)'> 초 -->
							</td>
						</tr>
						<tr>
							<td class="lb tableFiguresLeft2"><spring:message code="global.periodofdashboardgraph"/></td>
							<td class="tableFiguresLeft2">
								<label id="maingraph_period"></label>
							</td>
							<td class="tableFiguresLeft2">
							</td>
							<td class="rb tableFiguresLeft2">
							</td>
						</tr>
						<tr>
							<td class="lb tableFiguresLeft2"><spring:message code="global.emailAddr"/></td>
							<td class="rb tableFiguresLeft2" colspan="3">
								<div id="emailView"></div>
							</td>
						</tr>
						<tr>
							<td class="lb tableFiguresLeft2">MongoDB Version</td>
							<td class="rb tableFiguresLeft2" colspan="3">
								<input type="radio" id="mongo_version" name="mongo_version" value="2">2.0 &nbsp;&nbsp;&nbsp;
								<input type="radio" id="mongo_version" name="mongo_version" value="2.2">2.2(over)
							</td>
						</tr>
					</tbody>
				</table>
			</form>
			<div class="fr" style="margin:5px;border:5px;">${releaseVersionInfo}</div>
			<table class="tb_list_06" >
				<colgroup>
					<col width="200">
					<col width="300">
					<col width="300">
					<col width="250">
				</colgroup>
				<tr>
					<td class="lb tableFiguresLeft2">
						<spring:message code="global.deletelog"/>
					</td>
					<td>
						<span id="processPerState"></span>
					</td>
					<td>
					<span><input type="hidden" id="startDateStr" value="${startDateStr}"/>${startDateStr}</span>
						<c:if test="${deleteState == false}">
							~ <input type="text" id="endDateStr" name="endDateStr" value="<c:out value="${endDateStr}"/>"/>
							<input type="button" value='<spring:message code="global.startDeletelog"/>'  onClick="deleteDateCheck()"/>
						</c:if>
						<c:if test="${deleteState == true}">
							~ <span>${endDateStr}</span>
							<input type="button" value='<spring:message code="global.stopDeletelog"/>'  onClick="deleteLogStop()"/>
						</c:if>
					</td>
					<td></td>
				</tr>
			</table>
		</div>
		
		<!-- // content end -->
		<div class="clear">
		</div>
	</div>
	</div>
	<!-- // container end  -->
	<!-- footer start -->
	<div id="footer">
	</div>
	<!-- // footer end -->
</body>
</html>
