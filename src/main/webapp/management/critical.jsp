<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%@ include file="./eventPro.jsp" %>
<%
	Code.unit.getCode().updateCode(locale);
	Code.mongodEvent.getCode().updateCode(locale);
	Code.mongosEvent.getCode().updateCode(locale);
	Code.mongodEvent2_2.getCode().updateCode(locale);
%>
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
<title><spring:message code="main.title"/></title>
<%@ include file="./manageCommon.jsp" %>
<%@ include file="./jquery-dynamicCode.jsp" %>
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.smartPop.js"></script>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.smartPop.css">
<script type="text/javascript">
	var mainRefreshPeriodMinute = ${mainRefreshPeriodMinute};
</script>
<script type="text/javascript">
var bookmark = '${bookmark}';

	$(document).ready( function() {
	    var oTable;
			 
	    oTable = $('#list').dataTable( {
			//테이블의 페이징 검색 등 부가적인 기능 생략
			"bPaginate": false,
			"bLengthChange": false,
			"bFilter": false,
			"bSort": false,
			"bInfo": false,
			"bAutoWidth": false,
			"sScrollY" : "240px",
			"bScrollCollapse" : true,
			"iDisplayLength": 13 ,		
			"bProcessing":true,
	        "bServerSide":true,
	        "bRetrieve":true,
            "cache": false,
	    	"sAjaxSource":'list.do?dival=${comm.dival}',
			"aoColumns": [
			          
				{ 
									"sClass": "lb", "fnRender": function ( oObj ) {
				     
				        return oObj.aData.groupBind==""?'<input type="checkbox" name="idxLst" value="'+oObj.aData.idx+'"/>':"&nbsp;";
					}
				},
				{
						"sClass": "tableFiguresLeft", "fnRender": function ( oObj ) {
							// groupBind 이름이 있다면 groupBind를 hidden으로 추가
								if(oObj.aData.groupBind == ""){
									var str = groupCodeTable.getItem(oObj.oSettings.fnFormatNumber( parseFloat( oObj.aData.groupCode ) ));
									return !str?"&nbsp;":str;
								}else{
									var str = "<input type='hidden' value='@"+oObj.aData.groupBind+"'>"+"<img src='./img/ico-m-indent.gif' width='10px' height='10px'>&nbsp;"+ groupCodeTable.getItem(oObj.oSettings.fnFormatNumber( parseFloat( oObj.aData.groupCode ) ));
									return !str?"&nbsp;":str;
								}
					}
	            },
	            {
	            	"sClass": "tableFiguresLeft", "fnRender": function ( oObj ) {
	            		var str = deviceCodeTable.getItem(oObj.oSettings.fnFormatNumber( parseFloat( oObj.aData.deviceCode ) ));
	            		return !str?"&nbsp;":str;
	            	}
	            },
	            {
	            	"sClass": "tableFiguresLeft", "fnRender": function ( oObj ) {
	            		var str = eventCodeTable.getItem(oObj.aData.type);
	            		return !str?"&nbsp;":str;
	            	}
	            },
	            {
	            	"fnRender": function ( oObj ) {
	            		var str = unitCodeTable.getItem(oObj.aData.unit);
	            		return !str?"&nbsp;":str;
	            	}
	            },
	            {
	            	"sClass": "tableFigures2", "fnRender": function ( oObj ) {
	            		if(oObj.aData.unit=='seconds'){
		            		return oObj.oSettings.fnFormatNumber( microSecondsFormat(parseFloat( oObj.aData.criticalvalue )) );
	            		}else{
		            		return oObj.oSettings.fnFormatNumber( parseFloat( oObj.aData.criticalvalue ) );
	            		}
	            	}
	            },
	            {
	            	"sClass": "tableFigures2", "fnRender": function ( oObj ) {
	            		if(oObj.aData.unit=='seconds'){
		            		return oObj.oSettings.fnFormatNumber( microSecondsFormat(parseFloat( oObj.aData.warningvalue )) );
	            		}else{
		            		return oObj.oSettings.fnFormatNumber( parseFloat( oObj.aData.warningvalue ) );
	            		}
	            	}
	            },
	            {
	            	"sClass": "tableFigures2 rb", "fnRender": function ( oObj ) {
	            		if(oObj.aData.unit=='seconds'){
		            		return oObj.oSettings.fnFormatNumber( microSecondsFormat(parseFloat( oObj.aData.infovalue )) );
	            		}else{
		            		return oObj.oSettings.fnFormatNumber( parseFloat( oObj.aData.infovalue ) );
	            		}
	            	}
	            }
	            
	            
	        ],
			//마우스 오버 시 라인 색이 바뀌도록
			"fnDrawCallback": function( oObj ){
				
				var oSettings = oTable.fnSettings();
				var iTotalRecords = oSettings.fnRecordsTotal();
				var iLength = oSettings._iDisplayLength;
	    	    $("#list_total").text("<spring:message code="common.total"/> : "+ iTotalRecords +" <spring:message code="common.totals"/>");
            	if(iTotalRecords <= iLength){
            		$("#list_paginate").hide();
            	}else{
            		$("#list_paginate").show();
            	}
				if(iTotalRecords > 0){
			      	$('table#list td').bind('mouseenter', function () { $(this).parent().children().each(function(){$(this).addClass('row_selected');}); });
			      	
			      	$('table#list td').bind('mouseleave', function () { $(this).parent().children().each(function(){$(this).removeClass('row_selected');}); });
				}else{
					$('table#list td').eq(0).attr('class', 'lb rb');
				}
				
				var before = "";
				
				for(i = 0 ; i < oObj.aoData.length ; i ++){
					var flag = true;	
					var str = oObj.aoData[i].nTr.innerHTML;
					var strar = str.split('@');
					if(strar.length > 1){
						if(flag){
							var temp = strar[1].split("\"");
							if(before != temp[0]){
								$(oObj.aoData[i].nTr).before("<tr><td class='lb'><a name='"+temp[0]+"'></a><input type='checkbox' id='groupBind' name='groupBind' value='"+temp[0]+"'/></td><td class='tableFiguresLeft'>"+"<img src='./img/minus.png'>&nbsp;"+temp[0]+"</td><td class='tableFiguresLeft'></td><td class='tableFiguresLeft'><spring:message code='event.multievent' /></td><td></td><td></td><td></td><td class='rb'></td></tr>");
								before = temp[0];	
							}
						}
					}
				}
			    if (bookmark != undefined) {
			        window.location.hash="#"+bookmark;			    	
			    }
			}	
	    });
	    
	    jQuery.fn.dataTableExt.oPagination.iFullNumbersShowPages = 10;
	    
	    $('#checkAll').click(function(){//checkAll 체크 박스 클릭 시 모두 체크 되도록
	    	var test = $('#list input:checkbox');
			if ($('#checkAll').attr('checked')) {
				//$('input', oTable.fnGetNodes()).attr('checked', true);
				$('#list input:checkbox').attr('checked', true);
		    }else{
		    	$('#list input:checkbox').attr('checked', false);
		    }
	    });
	    

	    
		$("#list tbody").delegate("tr", "click", function() {//리스트 클릭 시 정보 창으로 내용이 전송되도록

			var position = oTable.fnGetPosition(this); // getting the clicked row position
			var data = oTable.fnGetData(position);
			if(position!=null){
				getDaemonArray(data.groupCode,data.deviceCode);
				eventSelect(data.deviceCode,data.type);
				$('#setForm #idx').val(data.idx);
				$('#setForm #groupCode').val(data.groupCode);
				setForm(data);
			}
		});
		
		function setForm(data){
			if(data.unit=='seconds'){
				$('#setForm #criticalvalue').val(microSecondsFormat(data.criticalvalue));
				$('#setForm #warningvalue').val(microSecondsFormat(data.warningvalue));
				$('#setForm #infovalue').val(microSecondsFormat(data.infovalue));
			}else{
				$('#setForm #criticalvalue').val(data.criticalvalue);
				$('#setForm #warningvalue').val(data.warningvalue);
				$('#setForm #infovalue').val(data.infovalue);
			}
		}
		
		$('#listForm').submit( function(event) {//체크박스 선택 후 삭제 되도록

			var answer = confirm("<spring:message code="common.wanttodelete"/>");
			if (answer){
				event.preventDefault();
				var url = "delete.do?dival=${comm.dival}";
// 				var dataToSend = $('input:checkbox', oTable.fnGetNodes()).serialize();
				
				var dataToSend = "";
				var test = $('#list input:checkbox');
				var temp_val = new Array();
				
				for(var i = 0 ; i < test.length ; i ++){
					if(test[i].checked == true){
	 					temp_val.push(test[i].value);
					}
				}
				
				for(var i = 0 ; i <temp_val.length ; i++){
					//isNaN 숫자 false 리턴
					if(i!=0) dataToSend += "&";
					if(isNaN(temp_val[i])){
						dataToSend += "groupBindLst="+temp_val[i];
					}else{
						dataToSend += "idxLst="+temp_val[i];
					}
				}
				
				if(dataToSend==""){
					alert("<spring:message code="common.nolist"/>");
				}else{
					var callback = function(dataReceived){
						oTable.fnDraw();//테이블 다시 불러오게ㅋㅎ
						$('#setForm').each(function(){
			                this.reset();
			        	});
					};
					var typeOfData = 'html';
					$.get(url,dataToSend,callback,typeOfData);
				}
			}else{
				return false;
			}
		});
		
		$('#divBtn').click( function(event){
			var dataToSend = $('input:checkbox', oTable.fnGetNodes()).serialize();
			var hiddendata = document.getElementById("dataToSend");
			hiddendata.value = dataToSend;
			
			var test = $('#list input:checkbox');
			var temp_val = new Array();
			var flag = false;
			
			for(var i = 0 ; i < test.length ; i ++){
				if(test[i].checked == true){
 					temp_val.push(test[i].value);
				}
			}
			
			for(var i = 0 ; i <temp_val.length ; i++){
				//isNaN 숫자 false 리턴
				if(isNaN(temp_val[i])){
					flag = true ; break;
				}
			}
			
			if(temp_val.length == 1){
				if(flag) {alert("<spring:message code="event.nonestedevent"/>"); flag=false;}
				else alert("<spring:message code="event.morechooseone"/>");	
			}else if(flag){
				alert("<spring:message code="event.nonestedevent"/>");
				flag=false;
			}else if(dataToSend==""){
				alert("<spring:message code="common.nolist"/>");
			}else{
				var callback = function(dataReceived){
					oTable.fnDraw();
					$('#setForm').each(function(){
						this.reset();
					});
				};
// 				window.open("","popup_event","width=800,height=200,menubar=no,toolbar=no,location=no,status=no,resizable=no,scrollbars=no");
//				$.smartPop.open({ background: "gray", width: 820, height:400, url: "about:blank" });
				$.smartPop.open({ background: "gray", width: 820, height:400, url: "management/popup_group_event_init.jsp" });
			}

		});
		
		window.addEventListener('message', function(event) {
		    if (event.data == 'MSG_SMARTPOP_FRAME_SUBMIT') {
		    	smartPopFrameSubmit();
		    }
		});
		
		function smartPopFrameSubmit(){
			//alert("smartPopFrameSubmit: "+document.getElementById("smartPop_frame").name);
			window.document.listForm.method = "post";
			window.document.listForm.action = "popup_event.do";
			window.document.listForm.target = "smartPop_frame";
			window.document.listForm.submit();
		}
		
		$('#setForm').submit( function(event) {
			event.preventDefault();
			if($.trim($('input:text[name=criticalvalue]').val())==''){
				alert("<spring:message code="critical.writedangercriticalvalue"/>");
				$('input:text[name=criticalvalue]').focus();
			}else if($.trim($('input:text[name=warningvalue]').val())==''){
				alert("<spring:message code="critical.writewarningcriticalvalue"/>");
				$('input:text[name=warningvalue]').focus();
			}else if($.trim($('input:text[name=infovalue]').val())==''){
				alert("<spring:message code="critical.writenormalcriticalvalue"/>");
				$('input:text[name=infovalue]').focus();
			}else if($('#setForm #type_gubun').val() == "config"){
				alert("<spring:message code="critical.nowriteconfig"/>");
				getDaemonArray(0,0);
			}else if($('#setForm #groupCode').val() == 0){
				alert("<spring:message code="critical.choosegroup"/>");
				$('#setForm #groupCode').focus();
			}else if($('#setForm #deviceCode').val() == 0){
				alert("<spring:message code="critical.choosedaemon"/>");
				$('#setForm #deviceCode').focus();
			}else if($('#setForm #type_gubun').val() == "mongod" && $('#mongod_type').val() == 0){
				alert("<spring:message code="critical.choosetypeofcritical"/>");
				$('#mongod_type').focus();
			}else if($('#setForm #type_gubun').val() == "mongos" && $('#mongos_type').val() == 0){
				alert("<spring:message code="critical.choosetypeofcritical"/>");
				$('#mongos_type').focus();
			}else{
				var url ;
				if($('#idx').val()!=0){//idx가 없으면 insert임
					url = "update.do";
				}else{
					url = "insert.do";
				}
				var dataToSend = $('#setForm').serialize();
				var callback = function(dataReceived){
					oTable.fnDraw();//테이블 다시 불러오게ㅋㅎ
					$('#setForm').each(function(){
		                this.reset();
		        	});
				};
				var typeOfData = 'html';
				$.get(url,dataToSend,callback,typeOfData);
			}
		});
		$('#listFormSubmitButton').click( function() {//listForm 이미지 버튼 클릭
			$('#listForm').submit();
		});
		$('#setFormSubmitButton').click( function() {//setForm 이미지 버튼 클릭
			$('#setForm').submit();
		});
		$('#setFormCancelButton').click( function() {//setForm 취소 버튼 클릭
	        $('#setForm').each(function(){
                this.reset();
                init();
        	});
		});
	});
	
	function init(){
		getDaemonArray(0,0);
		mongod_onload();
		mongos_onload();
	
	}

	function initEventCode(){
		var objCode = document.getElementsByName("eventCodeObj");
		var objCodeName;
		for (var i=0; i<objCode.length; i++) {
			var strobj = document.getElementById("eventCode"+i);
			objCodeName = eventCodeTable.getItem(objCode[i].value);
			strobj.innerText = objCodeName;
		}
	}
	
	function initUnitCode(){
		var objCode = document.getElementsByName("unitCodeObj");
		var objCodeName;
		for (var i=0; i<objCode.length; i++) {
			var strobj = document.getElementById("unitCode"+i);
			objCodeName = unitCodeTable.getItem(objCode[i].value);
			strobj.innerText = objCodeName;
		}
	}
	
	function fnGroupDetails( oTable, nTr){
		
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
			<div class="top_area">
				<span class="fl"><a href="#"><img src="./img/btn_seldelete<spring:message code="common.img"/>.gif" height="20" alt="<spring:message code="common.selecteddelete"/>" id="listFormSubmitButton"></a>&nbsp;<label id="list_total"></label></span>
				<span class="fr"><%-- <img src="./img/ico_square.gif" width="3" height="3" alt="ico"> <spring:message code="critical.clickdaemon"/>--%></span> 
			</div>
			<div>
			<form name="listForm" id="listForm">
			<input type = "hidden" name="dataToSend" id="dataToSend" value="">
				<table id="list" border="0" class="tb_list_07" summary="테이블 07">
					<caption>임계값 관리</caption>
					<colgroup>
						<col width="44">
						<col width="150">
						<col width="176">
						<col width="178">
						<col width="154">
						<col width="105">
						<col width="105">
						<col width="105">
					</colgroup>
					<thead>
					<tr>
						<th rowspan="2"><input type="checkbox" id="checkAll" name="checkAll"></th>
						<th rowspan="2"><spring:message code="common.group"/></th>
						<th rowspan="2"><spring:message code="common.daemon"/></th>
						<th rowspan="2"><spring:message code="common.criticaltype"/></th>
						<th rowspan="2"><spring:message code="critical.unit"/></th>
						<th colspan="3"><spring:message code="common.criticalvalue"/></th>
						</tr>
						<tr>
						<th class="noborder"><spring:message code="common.danger"/></th>
						<th class="noborder"><spring:message code="common.warning"/></th>
						<th><spring:message code="critical.normal"/></th>
					</tr>
					</thead>
					<tbody>
						<tr>
							<td colspan="8" class="dataTables_empty">Loading data from server</td>
						</tr>
					</tbody>
				</table>
				<div class="pagbtn" id="divBtn" ><a href="#"><img src="./img/btn_event_group_definition<spring:message code="common.img"/>.gif" id = "divBtn"></a></div>
			</form>
			</div>
			<form method="post" name="setForm" id="setForm">
			<input type="hidden" id="dival" name="dival" value="${comm.dival}"/>
			<input type="hidden" id="idx" name="idx" value="${comm.idx}"/>
			<input type="hidden" id="type_gubun" name="type_gubun" value="${comm.type_gubun}"/>
			<input type="hidden" id="type" name="type" value="${comm.type}"/>
				<table id="updatetb" border="0" class="tb_list_06" summary="테이블 06">
					<colgroup>
						<col width="135">
						<col width="80">
						<col width="230">
						<col width="135">
						<col width="80">
						<col width="">	 
					</colgroup>
					<tbody>
						<tr>
							<th rowspan="3"><spring:message code="critical.informationofcriticalvalue"/></th>
							<td><spring:message code="common.group"/></td>
							<td class="lf">
								<select name="groupCode" id ="groupCode" tabindex="1"></select>
							</td>
							<th rowspan="3"><spring:message code="common.criticalvalue"/></th>
							<td><spring:message code="common.danger"/> </td>
							<td class="rb lf"><input type="text" id="criticalvalue" name="criticalvalue" maxlength="10" OnKeyPress='num_only(event)' style='ime-mode:disabled' tabindex="4"></td>
						</tr>
						<tr>
							<td><spring:message code="common.daemon"/></td>
							<td class="lf">
								<select name="deviceCode" id="deviceCode" tabindex="2"></select>
							</td>
							<td><spring:message code="common.warning"/> </td>
							<td class="rb lf"><input type="text" id="warningvalue" name="warningvalue" maxlength="10" OnKeyPress='num_only(event)' style='ime-mode:disabled' tabindex="5"></td>
						</tr>
						<tr>
							<td><spring:message code="common.criticaltype"/> </td>
							<td class="lf">
								<select name="common_type" id="common_type" tabindex="3"></select>
								<select name="mongos_type" id="mongos_type" tabindex="3"></select>
								<select name="mongod_type" id="mongod_type" tabindex="3"></select>
							</td>
							<td><spring:message code="critical.normal"/></td>
							<td class="rb lf"><input type="text" id="infovalue" name="infovalue" maxlength="10" OnKeyPress='num_only(event)' style='ime-mode:disabled' tabindex="6"></td>
						</tr>
					</tbody>
				</table>

			<div class="btn_area">
				<a href="#"><img src="./img/btn_register<spring:message code="common.img"/>.gif" width="42" height="20" alt="<spring:message code="common.save"/>" id="setFormSubmitButton"></a> 
				<a href="#"><img src="./img/btn_cancel<spring:message code="common.img"/>.gif" width="42" height="20" alt="<spring:message code="common.cancel"/>" id="setFormCancelButton"></a>
			</div>
			</form>
			<!-- License information View -->
            <jsp:include page="/footer.jsp" flush="false"/>
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
