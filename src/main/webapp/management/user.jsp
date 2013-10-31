<%@ page language="java" contentType="text/html; charset=utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<%
	Integer loginUserCode = (Integer)session.getAttribute("loginUserCode");
	Integer loginAuth = (Integer)session.getAttribute("loginAuth");
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
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="main.title"/></title>
<%@ include file="./manageCommon.jsp" %>
<script type="text/javascript">
	var mainRefreshPeriodMinute = ${mainRefreshPeriodMinute};
</script>
<script type="text/javascript">	
var INSERT_MODE = "insertMode";
var UPDATE_MODE = "updateMode";
	$(document).ready( function() {
	    var oTable;
	    oTable = $('#list').dataTable( {
			//테이블의 페이징 검색 등 부가적인 기능 생략
			"bPaginate": true,
			"bLengthChange": false,
			"bFilter": false,
			"bSort": false,
			"bInfo": false,
			"bAutoWidth": false,
			"iDisplayLength": 11 ,
			
			"sPaginationType": "full_numbers",
			"oLanguage" :{
				"oPaginate":{
					"sFirst": " <img src=\"./img/img_pre_end.gif\" width=\"11\" height=\"12\" border=\"0\" alt=\"<spring:message code="common.first"/>\"/ class=\"textmiddle\"> ",
					"sPrevious": " <img src=\"./img/img_pre.gif\" width=\"11\" height=\"12\" border=\"0\" alt=\"<spring:message code="common.back"/>\"/ class=\"textmiddle\"> ",
					"sNext": " <img src=\"./img/img_next.gif\" width=\"11\" height=\"12\" border=\"0\" alt=\"<spring:message code="common.next"/>\"/ class=\"textmiddle\"> ", 
					"sLast": " <img src=\"./img/img_next_end.gif\" width=\"11\" height=\"12\" border=\"0\" alt=\"<spring:message code="common.last"/>\"/ class=\"textmiddle\">"
				}
			},
			"bProcessing":true,
	        "bServerSide":true,
	        "bRetrieve":true,
            "cache": false,
        	"sAjaxSource":'list.do?dival=${comm.dival}&loginUserCode=<%=loginUserCode%>&loginAuth=<%=loginAuth%>',
			"aoColumns": [
				{ 
					"sClass": "lb", "fnRender": function ( oObj ) {
						var box = "";
	            		if(oObj.aData.idx == <%=loginUserCode%>){
	            			box = "-";
	            		}else{
	            			box = '<input type="checkbox" id="idxLst" name="idxLst" value="'+oObj.aData.idx+'"/>';
	            		}
	            		return box;
	            	}
	            },
	            { "sClass": "tableFiguresLeft", "mDataProp": "uid" },
	            { "sClass": "tableFiguresLeft", "mDataProp": "username" },
	            { "sClass": "tableFiguresLeft", "mDataProp": "email" },
	            {
	            	"fnRender": function ( oObj ) {
	            		return  oObj.aData.mobileFirst + "-" +  oObj.aData.mobileSecond + "-" + oObj.aData.mobileThird ;
	            	}
	            },
	            { 
	            	"sClass": "rb", "fnRender": function ( oObj ) {
	            		if(oObj.aData.authority == 0){
	            			return "<spring:message code="users.user"/>";
	            		}else if(oObj.aData.authority == 1){
	            			return "<spring:message code="users.sysop"/>";
	            		}else if(oObj.aData.authority == 2){
	            			return "Admin";
	            		}
	            	}
	            }
	        ],
	        //마우스 오버 시 라인 색이 바뀌도록
			"fnDrawCallback": function(){
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
			}
	    });
	    
	    jQuery.fn.dataTableExt.oPagination.iFullNumbersShowPages = 10;
	    
	    $('#checkAll').click(function(){//checkAll 체크 박스 클릭 시 모두 체크 되도록
			if ($('#checkAll').attr('checked')) {
				$('input:checkbox', oTable.fnGetNodes()).attr('checked', true);
		    }else{
		    	$('input:checkbox', oTable.fnGetNodes()).removeAttr('checked');
		    }
	    });
	    
	    var $radios = $('#setForm input:radio[name=rdoAuth]');
	    if($radios.is(':checked') == false) {
	    	reset();
	    }
	    
		$("#list tbody").delegate("tr", "click", function() {//리스트 클릭 시 정보 창으로 내용이 전송되도록
			var position = oTable.fnGetPosition(this); // getting the clicked row position
			var data = oTable.fnGetData(position);
			if(position!=null){
				reset();
                $('#editMode').val(UPDATE_MODE);
				$('#setForm #idx').val(data.idx);
				$('#setForm #uid').val(data.uid).attr('readonly',true);
				$('#setForm #username').val(data.username);
				$('#setForm #email').val(data.email);
				$('#setForm #mobileFirst').val(data.mobileFirst);
				$('#setForm #mobileSecond').val(data.mobileSecond);
				$('#setForm #mobileThird').val(data.mobileThird);
				$('#originalAuth').val(data.authority);

			    $radios.removeAttr('disabled');
				if(data.authority == 2){
					$radios.filter('[value=2]').show();
		        	$('#authority_label').show();
				}
				$radios.filter('[value='+data.authority+']').attr('checked', true);
				authSet();
				if(data.authority == <%=loginAuth%> || <%=loginAuth%> == 1)radioDisabled();
				passwordFocus('on');
			}
		});
	    
		function radioDisabled() {
		    $("input[name='rdoAuth']").each(function() {
		        $(this).attr('disabled', true);
			});
		}
		
		function passwordFocus(focus){
			if(focus=='on'){
				passwdClass('box','box focus');
				$("#passwd").focus(function(){passwdClass('box focus','box');}).blur(function(){
					var passwd = $(this);
					if(passwd.val().length==0){
						passwdClass('box','box focus');
					}else{
						passwdClass('box focus','box');
					}
				});
			}else{
				passwdClass('','box');
			}
		}
		
		function passwdClass(addClassName,removeClassName){
			$("#passwd").removeClass(removeClassName);
			$("#passwd").addClass(addClassName);
		}
		
		function authSet(){
			var value = 0;
			if($radios.filter('[value=0]').is(':checked')==true){
				value = 0;
			}else if($radios.filter('[value=1]').is(':checked')==true){
				value = 1;
			}else if($radios.filter('[value=2]').is(':checked')==true){
				value = 2;
			}
			$("#authority").val(value);
		}
		    
		$('#listForm #delete').click( function(event) {//체크박스 선택 후 삭제 되도록
			var answer = confirm("<spring:message code="common.wanttodelete"/>");
			if (answer){
				event.preventDefault();
				var url = "delete.do?dival=${comm.dival}";
				var dataToSend = $('input:checkbox', oTable.fnGetNodes()).serialize();
				if(dataToSend==""){
					alert("<spring:message code="common.nolist"/>");
				}else{
					var callback = function(dataReceived){
						oTable.fnDraw();//테이블 다시 불러오게ㅋㅎ
						reset();
					};
					var typeOfData = 'html';
					$.get(url,dataToSend,callback,typeOfData);
				}
			}else{}
		});

	    function authValue(event){
	    	$.ajax({
	    		type:'POST',
	    		url : 'authCheck.do',
	    		data : {"authority" : 1},
	    		dataType:'html',
	    		success:function(json,textStatus){
	    			if($('#editMode').val()==UPDATE_MODE){
	    				updateAuthCheck(json,event);
	    			}else{
		    			insertAuthCheck(json,event);
	    			}
	    		},
	    		error:function(xhr,textStatus, errorThrown){
	    			
	    		}
	    	});
	    }
	    
	    function updateAuthCheck(auth,event){
			event.preventDefault();
			authSet();
			var originalAuth = $('#originalAuth').val();
			if($.trim($('input:text[name=uid]').val())==''){
				alert("<spring:message code="login.writeid"/>");
				$('input:text[name=uid]').focus();
			}else if($.trim($('input:text[name=uid]').val()).length < 2){
				alert("<spring:message code="login.requestid"/>");
				$('input:text[name=uid]').focus();
			}else if(0 < $.trim($('input:password[name=passwd]').val()).length && $.trim($('input:password[name=passwd]').val()).length < 4){
				alert("<spring:message code="users.requestpassword"/>");
				$('input:password[name=passwd]').focus();
			}else if($.trim($('input:text[name=username]').val())==''){
				alert("<spring:message code="users.writename"/>");
				$('input:text[name=username]').focus();
			}else if($.trim($('input:text[name=email]').val())==''){
				alert("<spring:message code="users.writeemail"/>");
				$('input:text[name=email]').focus();
			}else if($.trim($('input:text[name=mobileSecond]').val())==''){
				alert("<spring:message code="users.writemobilenumber"/>");
				$('input:text[name=mobileSecond]').focus();
			}else if($.trim($('input:text[name=mobileThird]').val())==''){
				alert("<spring:message code="users.writemobilenumber"/>");
				$('input:text[name=mobileThird]').focus();
			}else if(originalAuth!=1 && $radios.filter('[value=1]').is(':checked')==true && auth >= 5){
				alert("<spring:message code="users.maxsysop"/>");
			}else{
				postDate("update.do");
			}
	    }
	    
		function insertAuthCheck(auth,event){
			event.preventDefault();
			authSet();
			if($.trim($('input:text[name=uid]').val())==''){
				alert("<spring:message code="login.writeid"/>");
				$('input:text[name=uid]').focus();
			}else if($.trim($('input:text[name=uid]').val()).length < 2){
				alert("<spring:message code="login.requestid"/>");
				$('input:text[name=uid]').focus();
			}else if($.trim($('input:password[name=passwd]').val())==''){
				alert("<spring:message code="login.writepassword"/>");
				$('input:password[name=passwd]').focus();
			}else if($.trim($('input:password[name=passwd]').val()).length < 4){
				alert("<spring:message code="users.requestpassword"/>");
				$('input:password[name=passwd]').focus();
			}else if($.trim($('input:text[name=username]').val())==''){
				alert("<spring:message code="users.writename"/>");
				$('input:text[name=username]').focus();
			}else if($.trim($('input:text[name=email]').val())==''){
				alert("<spring:message code="users.writeemail"/>");
				$('input:text[name=email]').focus();
			}else if($.trim($('input:text[name=mobileSecond]').val())==''){
				alert("<spring:message code="users.writemobilenumber"/>");
				$('input:text[name=mobileSecond]').focus();
			}else if($.trim($('input:text[name=mobileThird]').val())==''){
				alert("<spring:message code="users.writemobilenumber"/>");
				$('input:text[name=mobileThird]').focus();
			}else if($radios.filter('[value=1]').is(':checked')==true && auth >= 5){
				alert("<spring:message code="users.maxsysop"/>");
			}else{
				postDate("insert.do");
			}
		}
		
		function postDate(setUrl){
			var url = setUrl;
			var dataToSend = $('#setForm').serialize();
			var callback = function(dataReceived){
				oTable.fnDraw();//테이블 다시 불러오게ㅋㅎ
				reset();
			};
			var typeOfData = 'html';
			$.get(url,dataToSend,callback,typeOfData);
		}
		
		$('#setForm #insert').click( function(event) {
			authValue(event);
		});
		
		$('#setFormCancelButton').click( function() {//setForm 취소 버튼 클릭
			reset();
		});
		
		function reset(){
	        $('#setForm').each(function(){
                this.reset();
                $('#editMode').val(INSERT_MODE);
                $('#setForm #uid').removeAttr('readonly');
                $("input[name='rdoAuth']").removeAttr('disabled');
    	        $radios.filter('[value=0]').attr('checked', true);
                $radios.filter('[value=2]').hide();
    	        $('#authority_label').hide();
    	        if(<%=loginAuth%> == 1)radioDisabled();
    	        passwordFocus('out');
        	});
		}
	});
	
	function init(){
		if("${comm.message}"!=null && "${comm.message}"!=""){
			alert("${comm.message}");
		}
	}
	
</script>
</head>
<body id="index" onload='init()'>
<div id="wrap">
	<!-- header start -->
	<%@ include file="/top_menu.jsp"%>
	<!-- // header end -->
	<!-- container start-->
	<div id="container">
		<!-- content start -->
		<div id="content_m">
			<form id = "listForm">
			<div class="top_area">
				<span class="fl"><a href="#"><img id="delete" src="./img/btn_seldelete<spring:message code="common.img"/>.gif" height="20" alt="선택삭제"></a>&nbsp;<label id="list_total"></label></span>
				<span class="fr"><img src="./img/ico_square.gif" width="3" height="3" alt="ico"><label> <spring:message code="users.clickid"/></label></span>
			</div>
				<table id="list" border="0" class="tb_list_05" summary="메인 테이블 05">
					<caption>메인 테이블 05</caption>
					<colgroup>
						<col width="44">
						<col width="177">
						<col width="167">
						<col width="333">
						<col width="178">
						<col width="165">
					</colgroup>
					<thead>
						<tr>
							<th><p><input type="checkbox" id="checkAll" name="checkAll"></p></th>
							<th><spring:message code="login.id"/></th>
							<th><spring:message code="users.name"/></th>
							<th><spring:message code="users.email"/></th>
							<th><spring:message code="users.mobilephone"/></th>
							<th><spring:message code="users.permission"/></th>
						</tr>
					</thead>
					<tbody>
					</tbody>
				</table>
			</form>
			<form method="post" name="setForm" id = "setForm">
			<input type="hidden" id="dival" name="dival" value="${comm.dival}"/>
			<input type="hidden" id="idx" name="idx" value="${comm.idx}"/>
			<input type="hidden" id="loginUserCode" name="loginUserCode" value="<%=loginUserCode%>"/>
			<input type="hidden" id="authority" name="authority"/>
			<input type="hidden" id="editMode" name="editMode"/>
			<input type="hidden" id="originalAuth" name="originalAuth"/>
				<table  border="0" class="tb_list_06" summary="테이블 06">
					<caption>테이블 06</caption>
					<colgroup>
						<col width="135">
						<col width="80">
						<col width="189">
						<col width="80">
						<col width="">	 
					</colgroup>
					<tbody>
						<tr>
							<th><spring:message code="users.userinfo"/></th>
							<td><spring:message code="login.id"/> </td>
							<td><input type="text" name="uid" id="uid" maxlength="100" OnKeyPress='num_eng(event)' style='ime-mode:disabled'></td>
							<td><spring:message code="login.password"/></td>
<!-- 							<td class="rb lf"><input type="password" class="box" id="passwd" name="passwd" maxlength="100" style='ime-mode:disabled' onfocus="this.className='box focus'" onBlur="if(this.value.length==0){this.className='box';}else{this.className='box focus';}"></td> -->
							<td class="rb lf"><input type="password" id="passwd" name="passwd" maxlength="100" style='ime-mode:disabled'></td>
						</tr>
					</tbody>
				</table>
				<table  border="0" class="tb_list_06" summary="테이블 06">
					<caption>테이블 06</caption>
						<colgroup>
						<col width="135">
						<col width="80">
						<col width="189">
						<col width="80">
						<col width="">	 
					</colgroup>
					<tbody>
						<tr>
							<th rowspan="2"><spring:message code="users.additionalinfo"/></th>
							<td><spring:message code="users.name"/></td>
							<td><input type="text" id="username" name="username" maxlength="50"></td>
							<td><spring:message code="users.email"/></td>
							<td class="rb lf"><input type="text" id="email" name="email" maxlength="100" style='ime-mode:disabled'></td>
						</tr>
						<tr>
							<td><spring:message code="users.mobilephone"/></td>
							<td>
								<select id="mobileFirst" name="mobileFirst">
									<option value="010">010</option>
									<option value="011">011</option>
									<option value="016">016</option>
									<option value="017">017</option>
									<option value="018">018</option>
									<option value="019">019</option>
								</select>
								-<input type="text" id="mobileSecond" name="mobileSecond" maxlength="4" size="5" OnKeyPress='num_only(event)' style='ime-mode:disabled'>
								-<input type="text" id="mobileThird" name="mobileThird" maxlength="4" size="5" OnKeyPress='num_only(event)' style='ime-mode:disabled'>
							</td>
							<td><spring:message code="users.permission"/></td>
							<td class="rb lf">
								<input type="radio" id="rdoAuth" name="rdoAuth" value="0"/> <label><spring:message code="users.user"/></label>&nbsp;&nbsp;
								<input type="radio" id="rdoAuth" name="rdoAuth" value="1"/> <label><spring:message code="users.sysop"/></label>&nbsp;&nbsp;
								<input type="radio" id="rdoAuth" name="rdoAuth" value="2"/> <label id="authority_label">Admin</label>
							</td>
						</tr>
					</tbody>
				</table>
			<div class="btn_area">
				<a href="#"><img id="insert" src="./img/btn_register<spring:message code="common.img"/>.gif" width="42" height="20" alt="<spring:message code="common.save"/>"></a> 
				<a href="#"><img id="setFormCancelButton" src="./img/btn_cancel<spring:message code="common.img"/>.gif" width="42" height="20" alt="<spring:message code="common.cancel"/>"></a>
			</div>
			</form>
			<!-- License information View -->
            <jsp:include page="/footer.jsp" flush="false"/>
		</div>
		<!-- // content end -->
		<div class="clear">
		</div>
	</div>
	<!-- // container end  -->
	
	<!-- footer start -->
	<div id="footer">&nbsp;
	</div>
	<!-- // footer end -->
</div>
</body>
</html>
