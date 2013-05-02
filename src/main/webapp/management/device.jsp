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
<%@ page language="java" contentType="text/html; charset=utf-8" pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<% request.setCharacterEncoding("UTF-8"); %>

<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=utf-8">
<title><spring:message code="main.title"/></title>
<%@ include file="./manageCommon.jsp" %>
<link rel="stylesheet" type="text/css" href="${pageContext.request.contextPath}/css/jquery.smartPop.css">
<script type="text/javascript" src="${pageContext.request.contextPath}/js/jquery.smartPop.js"></script>
<script type="text/javascript">
	var mainRefreshPeriodMinute = ${mainRefreshPeriodMinute};

	var LIST_REFRESH_PERIOD_MSEC = 3000;
	var INSERT_MODE = "insertMode";
	var UPDATE_MODE = "updateMode";
	var initCheckTimerHandlerId = 0;
	var initCheckDeviceCode = 0;
	var waitForMainRefreshPeriodMinuteHandlerId = 0;
	var oTable;
	$(document).ready( function() {
	    oTable = $('#list').dataTable( {
			//테이블의 페이징 검색 등 부가적인 기능 생략
			"bPaginate": true,
			"bLengthChange": false,
			"bFilter": false,
			"bSort": false,
			"bInfo": false,
			"bAutoWidth": false,
			"iDisplayLength": 12 ,
			
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
	    	"sAjaxSource":'list.do?dival=${comm.dival}&search_gubun=${comm.search_gubun}&search_text=${comm.search_text}&search_type=${comm.search_type}',
			"aoColumns": [
				{ 
					"sClass": "lb", "fnRender": function ( oObj ) {
	                    //return (oObj.aData.finishedInitailRrd == false)?'-':'<input type="checkbox" name="idxLst" value="'+oObj.aData.idx+'"/>';
						return '<input type="checkbox" name="idxLst" id="idxLst" value="'+oObj.aData.idx+'"/>';
	            	}
	            },
	            {
	            	"sClass": "tableFiguresLeft2", "fnRender": function ( oObj ) {
	            		var str = groupCodeTable.getItem( parseFloat( oObj.aData.groupCode ) );
	            		return !str?"&nbsp;":str;
	            	}
	            },
	            { "sClass": "tableFiguresLeft2", "mDataProp": "uid" },
	            { "sClass": "tableFiguresLeft2", "mDataProp": "type" },
	            { "mDataProp": "ip" },
	            { "mDataProp": "port" },
	            {
	            	"fnRender": function ( oObj ) {
	            		return oObj.oSettings.fnFormatNumber( parseFloat( oObj.aData.memorysize ) );
	            	}
	            },
	            {
	            	"sClass": "rb", "fnRender": function ( oObj ) {
	            		return oObj.oSettings.fnFormatNumber( parseFloat( oObj.aData.hddsize ) );
	            	}
	            }
	        ],
	    	//특정 값 라인 색이 바뀌도록
	    	"fnRowCallback": function( nRow, aData, iDisplayIndex,aaData ) {
				if ( aData.finishedInitailRrd == false ){
	    	    	$(nRow).children().each(function(){$(this).addClass('row_init_demon_status');});
	    	    	$(nRow).attr("title","<spring:message code="daemons.initializing"/>");
	    	    	setTipByObjectForInitDemon($(nRow));
	    	    	//dbg("initCheckTimerHandlerId="+initCheckTimerHandlerId);
	    	    	if ( initCheckTimerHandlerId==0){
	    	    		//dbg("setTimeout call");
	    	    		initCheckDeviceCode=aData.idx;
	    	    		initCheckTimerHandlerId = setTimeout(initCheckDeviceInitialized, LIST_REFRESH_PERIOD_MSEC);
	    	    	};
				}else if ( aData.finishedInitailRrd == true ){
	    	    	$(nRow).children().each(function(){$(this).removeClass('row_init_demon_status');});
				}
		    	return nRow;
    	    },
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
			},
			"fnServerData": function ( sSource, aoData, fnCallback ) { 
				//alert("start");
// 				jQuery('#list_title').showLoading();
			    $.getJSON( sSource, aoData, function (json) {  
			       fnCallback(json) ;
			    }).complete(function(){
			    	//alert("end");
// 			    	jQuery('#list_title').hideLoading();
			    }); 
			}
	    });

	    jQuery.fn.dataTableExt.oPagination.iFullNumbersShowPages = 10;
	    
	    function initCheckDeviceInitialized(){
	    	//dbg("initCheckDeviceInitialized()");
	    	//dbg("initCheckDeviceCode="+initCheckDeviceCode);
	    	initCheckTimerHandlerId = 0;
	    	
	    	$.ajax({
	    		type:'POST',
	    		url : 'isFinishedInitialRrd.do?deviceCode='+initCheckDeviceCode,
	    		dataType:'html',
	    		success:function(receivedData,textStatus){
	    			//dbg("receivedData : "+receivedData);
	    	    	if ( receivedData=="false"){
	    	    		//dbg("setTimeout call");
	    	    		initCheckTimerHandlerId = setTimeout(initCheckDeviceInitialized, LIST_REFRESH_PERIOD_MSEC);
	    	    	}else if ( receivedData=="true"){
	    	    		receivedData=0;
	    	    		waitForMainRefreshPeriodMinuteHandlerId = setTimeout(waitForMainRefreshPeriodMinute, mainRefreshPeriodMinute);
	    	    	}
	    		},
	    		error:function(xhr,textStatus, errorThrown){
	    			//dbg("ajax error");
	    			//dbg(xhr);
	    			//dbg(textStatus);
	    			//dbg(errorThrown);
	    		}
	    	});	    	
	    }
	    
	    function waitForMainRefreshPeriodMinute(){
	    	if ( initCheckTimerHandlerId==0){
    			oTable.fnDraw();
	    	}
	    }
	    
	    var $radios = $('#setForm input:radio[name=groupSelect]');
	    
	    function rdoinit(){
	        $radios.filter('[value=regGroup]').attr('checked', true);
	        $('#setForm input:text[name=groupText]').attr('disabled',true);
	    }
	  	//그룹선택/추가
	    if($radios.is(':checked') == false) {
	    	rdoinit();
	    }

	    $radios.click(function(){
	    	$radioChecked = $('#setForm input:radio[name=groupSelect]:checked');
	    	if($radioChecked.val()=='newGroup'){
	    		editGroupHide();
		    	$('#setForm input:text[name=groupText]').removeAttr('disabled');
// 		        $('#setForm input:select[name=groupList]').attr('disabled',true);
		  	}else if($radioChecked.val()=='regGroup'){
		        $('#setForm input:text[name=groupText]').attr('disabled',true);
// 		        $('#setForm input:select[name=groupList]').removeAttr('disabled');
		    }
	  	});

		 $('#authCheck').click(function(){
			 authCheckFunc();
		  });
		 
		 function authCheckFunc(){
			 if($('#authCheck').is(":checked")){
					$('#authUser, #authPasswd').removeAttr('disabled');
			  }else{
				  $('#authUser, #authPasswd').val('');
	    	  passwordFocus('out');
					$('#authUser, #authPasswd').attr('disabled',true);
			  	}
		 }
	    $('#checkAll').click(function(){//checkAll 체크 박스 클릭 시 모두 체크 되도록
			if ($('#checkAll').attr('checked')) {
				$('input:checkbox', oTable.fnGetNodes()).attr('checked', true);
		    }else{
		    	$('input:checkbox', oTable.fnGetNodes()).removeAttr('checked');
		    }
	    });
	    
		$("#list tbody").delegate("tr", "click", function() {//리스트 클릭 시 정보 창으로 내용이 전송되도록
			var position = oTable.fnGetPosition(this); // getting the clicked row position
			var data = oTable.fnGetData(position);
			if(position!=null){
				editGroupHide();
				rdoinit();
				$('#setForm #idx').val(data.idx);
				$('#setForm #groupList').val(data.groupCode);
				$('#setForm #uid').val(data.uid);
				$('#setForm #ip').val(data.ip);
				$('#setForm #type').val(data.type);
				$('#setForm #selectType').val(data.type);
				$('#setForm #port').val(data.port);
				$('#setForm #memorysize').val(data.memorysize);
				$('#setForm #hddsize').val(data.hddsize);
				if(data.authUser!=''&&data.authPasswd!=''){
					$('#authCheck').attr('checked', true);
					authCheckFunc();
					$('#setForm #authUser').val(data.authUser);
					passwordFocus('on');
				}
                $('#editMode').val(UPDATE_MODE);
				typeDisabled();
			};
			//dbg($('#editMode').val());
		});

		function passwordFocus(focus){
			if(focus=='on'){
				passwdClass('box','box focus');
				$("#authPasswd").focus(function(){passwdClass('box focus','box');}).blur(function(){
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
			$("#authPasswd").removeClass(removeClassName);
			$("#authPasswd").addClass(addClassName);
		}
		
		function typeDisabled(){
			$('#setForm #selectType').attr('disabled', true);
		}
		
		function typeEnabled(){
			$('#setForm #selectType').removeAttr('disabled');
		}
		
		function isExistMongo(fnCallback){
			jQuery('html').showLoading();
			var ipStr = $('#ip').val();
			var portStr = $('#port').val();
			var nameStr = $('#uid').val();
			var authUserStr = $('#authUser').val();
			var authPasswdStr = $('#authPasswd').val();
			var editMode = $('#editMode').val();
			var idx = $('#idx').val();
	    	$.ajax({
	    		type:'POST',
	    		url : 'mongoExistCheck.do',
	    		dataType:'html',
	    		data : {"ipStr":ipStr, "portStr":portStr, "nameStr":nameStr, "authUserStr":authUserStr, "authPasswdStr":authPasswdStr, "editMode":editMode, "idx":idx},
	    		success:function(json,textStatus){
	    				fnCallback(json);
	    				jQuery('html').hideLoading();	
		    		},
		    		error:function(xhr,textStatus, errorThrown){
	    				jQuery('html').hideLoading();	
		    		}
	    	});
	    }
		
		$('#autoResearch').click( function(event) {
			$.smartPop.open({ background: "gray", width: 1100, height: 600, url: "mongosResearchView.do"});	
		});
		
		$('#registDevice').click( function(event) {
				deviceTextNullCheck(isExistMongo);
		});

		function deviceTextNullCheck(fnCallback){
			$radios = $('#setForm input:radio[name=groupSelect]:checked');
			if($radios.val()=='regGroup'){
				$('#setForm #groupCode').val($('#setForm #groupList').val());
				$('#setForm #groupText').val("-");
			}
			var selectType = $('#setForm #selectType').val();
			if($('#editMode').val()!=UPDATE_MODE)$('#setForm #type').val(selectType);
			if($.trim($('input:text[name=uid]').val())==''){
				alert("<spring:message code="daemons.writedaemon"/>");
				$('input:text[name=uid]').focus();
			}else if($.trim($('input:text[name=ip]').val())==''){
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
			}else if($.trim($('input:text[name=groupText]').val())==''){
				alert("<spring:message code="daemons.writegroup"/>");
				$('input:text[name=groupText]').focus();
			}else if($radios.val()=='regGroup' && $.trim($('#setForm #groupCode').val())==''){
				alert("<spring:message code="daemons.writegroup"/>");
				$radios = $('#setForm input:radio[name=groupSelect]');
		        $radios.filter('[value=newGroup]').attr('checked', true);
		        $('#setForm #groupCode').val(0);
		    	$('#setForm input:text[name=groupText]').removeAttr('disabled');
				$('input:text[name=groupText]').focus();
			}else if($radios.val()=='newGroup' && $.trim($('#setForm input:text[name=groupText]').val())==''){
				alert("<spring:message code="daemons.writegroup"/>");
		        $('#setForm #groupCode').val(0);
				$('input:text[name=groupText]').focus();
			}else if($('#authCheck').is(':checked') && $.trim($('#setForm input:text[name=authUser]').val())==''){
				alert("<spring:message code="daemons.writeAuthUser"/>");
				$('input:text[name=authUser]').focus();
			}else if($('#authCheck').is(':checked') && $.trim($('#setForm input:password[name=authPasswd]').val())==''){
				alert("<spring:message code="daemons.writeAuthPasswd"/>");
				$('#authPasswd').focus();
			}else{				
					fnCallback(deviceInsertnUpdate);
			}
		}
		
		function deviceInsertnUpdate(jsonResult){
			if(jsonResult == "0"){
				var $radios = $('#setForm input:radio[name=groupSelect]:checked');
				var url ;
				if($('#editMode').val()==UPDATE_MODE){
					url = "update.do";
				}else{
					url = "insert.do";
				}
				var dataToSend = $('#setForm').serialize();
				var callback = function(dataReceived){
					if($radios.val()=='newGroup'){
						location.reload();
					}else{
						editGroupHide();
						oTable.fnDraw();//테이블 다시 불러오게ㅋㅎ
					}
					clearInputForm();
				};
				var typeOfData = 'html';
				$.get(url,dataToSend,callback,typeOfData);
			}else if(jsonResult == "1"){
				alert("<spring:message code="daemons.sameIpPort"/>");
			}else if(jsonResult == "2"){
				alert("<spring:message code="daemons.sameName"/>");
			}else if(jsonResult == "3"){
					alert("<spring:message code="daemons.serverNoRespond"/>");
			}else if(jsonResult == "4"){
					alert("<spring:message code="daemons.serverNoRespondAuth"/>");
			}
		}
		
		$('#checkRemove').click( function(event) {			
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
						clearInputForm();
					};
					var typeOfData = 'html';
					$.get(url,dataToSend,callback,typeOfData);
				}			
			}else{
				
			}
		});
		
		$('#search_text').change(function () {
			if($('#search_text').val()!=""){
				document.listForm.action="./listManagement.do";
				document.listForm.submit();
			}
		});
		
		$('#search_gubun').change(function () {
			if($('#search_gubun').val() == '<%=searchAll%>'){
				document.listForm.action="./listManagement.do";
				document.listForm.submit();
			}
		});
		
		$('#setFormCancelButton').click( function() {//setForm 취소 버튼 클릭
	        clearInputForm();
		});
		
		$('#setForm #deleteGroup').click( function(event) {
			var idxLst = $('#setForm #groupList').val();
			if(idxLst != null){
				var answer = confirm("<spring:message code="common.wanttodelete"/>");
				if (answer){
					event.preventDefault();
					var url = "delete.do?idxLst="+idxLst+"&dival=4&groupCode="+idxLst;
					var dataToSend ;
					var callback = function(dataReceived){
						location.reload();
					};
					var typeOfData = 'html';
					$.get(url,dataToSend,callback,typeOfData);
				}else{}
			}else{
				alert("<spring:message code="daemons.noGroup"/>");
			}
		});
		
		$('#editGroup').click( function(event) {
			rdoinit();
			var groupCodeInt = $('#groupList').val();
			var groupNameStr = groupCodeTable.getItem( parseFloat( groupCodeInt ) );
			if(groupCodeInt != null){
				editGroupShow();
				$('#groupCode').val(groupCodeInt);
				$('#editGroupText').val(groupNameStr);
			}else{
				alert("<spring:message code="daemons.noGroup"/>");
			}
		});

		$('#editGroupOk').click( function(event) {			
			var answer = confirm("<spring:message code="daemons.editGroup"/>");
			var groupCodeInt = $('#groupCode').val();
			var groupNameStr = $('#editGroupText').val();
			if (answer){
				if(groupNameStr==""){
					alert("<spring:message code="daemons.writegroup"/>");
				}else{
					$.ajax({
						type:'POST',
						url : 'update.do',
						data : {"dival":4, "idx":groupCodeInt, "uid":groupNameStr},
						dataType:'html',
						success:function(json,textStatus){
							alert("<spring:message code="daemons.editSucc"/>");
	 						location.reload();
						},
						error:function(xhr,textStatus, errorThrown){

						}
					});
				}
			}else{
				
			}
		});
		
		$('#editGroupCancel').click( function(event) {
			editGroupHide();
		});
		
		function clearInputForm(){
	        $('#setForm').each(function(){
                this.reset();
                typeEnabled();
				$('#authCheck').removeAttr('checked');
    	        passwordFocus('out');
        	});
	        $('#editMode').val(INSERT_MODE);
	        //dbg($('#editMode').val());
		}
		
		$('#batchUpdateBtn').click( function() {			
			jQuery('html').showLoading();
			var answer = confirm("<spring:message code="daemons.editGroup"/>");
			var dataToSend = $('input:checkbox', oTable.fnGetNodes()).serialize();
			if (answer){
				if(dataToSend==""){
					alert("<spring:message code="common.nolist"/>");
				}else if($.trim($('input:text[name=batchHddSize]').val())=='' && $.trim($('input:text[name=batchMemorySize]').val())==''){
					alert("<spring:message code="daemons.writeModifyAtOnce"/>");
					$('input:text[name=batchMemorySize]').focus();
				}else{
					var batchMemorySize = $('#batchMemorySize').val();
					var batchHddSize = $('#batchHddSize').val();

					var url = "deviceBatchUpdate.do?batchMemorySize="+batchMemorySize+"&batchHddSize="+batchHddSize;
					var callback = function(dataReceived){
						oTable.fnDraw();
						clearInputForm();
					};
					var typeOfData = 'html';
					$.get(url,dataToSend,callback,typeOfData);
				}			
			}
			jQuery('html').hideLoading();	
		});
		
	});
	
	function editGroupHide(){
		$('#lstGroupLabel').show();
		$('#editGroupLabel').hide();
	}
	
	function editGroupShow(){
		$('#lstGroupLabel').hide();
		$('#editGroupLabel').show();
	}
	
	function init(){
		if("${comm.message}"!=null && "${comm.message}"!=""){
			alert("${comm.message}");
		}

		editGroupHide();
		if('${comm.search_text}'!=""){
			$('#search_gubun').val('${comm.search_gubun}');
			makeSubSelect();
			$('#search_text').val('${comm.search_text}');
		}
		$('#editMode').val(INSERT_MODE);
		$('#authUser, #authPasswd').attr('disabled',true);
		//dbg($('#editMode').val());
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
				<form  method="post" id="listForm" name="listForm"><input type="hidden" id="dival" name="dival" value="${comm.dival}"/>
				<div class="top_area">				
					<span class="fl"><a href="#" id="checkRemove"><img src="./img/btn_seldelete<spring:message code="common.img"/>.gif" height="20" alt="<spring:message code="common.selecteddelete"/>"></a>&nbsp;<label id="list_total"></label></span> 
					<span class="fr">	<img src="./img/ico_square.gif" width="3" height="3" alt="ico"> <spring:message code="daemons.typeofsearch"/> 
						<select id="search_gubun" name="search_gubun" onchange="javaScript:makeSubSelect();">
							<option value="">--<spring:message code="common.chooseone"/>--</option>
							<option value="<%=searchAll%>"><spring:message code="daemons.all"/></option>
							<option value="<%=searchGcode%>"><spring:message code="common.group"/></option>
							<option value="<%=searchUid%>"><spring:message code="common.daemon"/></option>
							<option value="<%=searchType%>"><spring:message code="daemons.type"/></option>
						</select>
						<select id="search_text" name="search_text" >
							<option>--<spring:message code="common.chooseone"/>--</option>
						</select>
					</span>
				</div>
				</form>
				<table id="list" border="0" class="tb_list_05" summary="메인 테이블 05">
					<caption>메인 테이블 05</caption>
					<colgroup>
						<col width="50">
						<col width="95">
						<col width="95">
						<col width="95">
						<col width="95">
						<col width="95">
						<col width="100">
						<col width="95">
					</colgroup>
					<thead>
						<tr id="list_title">
							<th><p><input type="checkbox" id="checkAll" name="checkAll"></p></th>
							<th><p><spring:message code="common.group"/></p></th>
							<th><p><spring:message code="common.daemon"/></p></th>
							<th><p><spring:message code="daemons.type"/></p></th>
							<th><p>IP</p></th>
							<th><p>Port</p></th>
							<th><p><spring:message code="daemons.memory"/> (GB)</p></th>
							<th><p><spring:message code="daemons.hard"/> (GB)</p></th>							
						</tr>
					</thead>
					<tbody>
						<tr>
							<td colspan="8" class="dataTables_empty">Loading data from server</td>
						</tr>			
					</tbody>
				</table>
				
				<form method="post" name="setForm" id="setForm">
				<input type="hidden" id="dival" name="dival" value="${comm.dival}"/>
				<input type="hidden" id="idx" name="idx" value="${comm.idx}"/>
				<input type="hidden" id="groupCode" name="groupCode" value="${comm.groupCode}"/>
				<input type="hidden" id="type" name="type"/>
				<input type="hidden" id="editMode" name="editMode"/>
				<table id="updatetb" border="0" class="tb_list_06" summary="데몬 등록/수정">
					<caption>데몬 등록/수정</caption>
					<colgroup>
						<col width="135">
						<col width="110">
						<col width="260">
						<col width="80">
						<col width="">
					</colgroup>
					<tbody>
						<tr>
							<th rowspan="5"><spring:message code="daemons.daemoninfo"/></th>
							<td><spring:message code="common.daemon"/></td>
							<td class="lf"><input type="text" id="uid" name="uid" size="20" maxlength="30" style='ime-mode:disabled'></td>
							<td><spring:message code="daemons.type"/></td>
							<td class="rb lf">
								<select id="selectType" name="selectType">
									<c:forEach var="i" items="${type}">
										<option value="${i}">
											${i}
										</option>
									</c:forEach>
								</select>
							</td>
						</tr>
						<tr>
							<td>IP</td>
							<td class="lf"><input type="text" name="ip" id="ip" size="20" maxlength="50" style='ime-mode:disabled'></td>
							<td>Port</td>
							<td class="rb lf"><input type="text" name="port" id="port" size="20" maxlength="20" OnKeyPress='num_only(event)' style='ime-mode:disabled'></td>
						</tr>
						<tr>
							<td><spring:message code="daemons.memory"/></td>
							<td class="lf"><input type="text" name="memorysize" id="memorysize" size="20" maxlength="20" OnKeyPress='num_only(event)' style='ime-mode:disabled'> GB</td>
							<td><spring:message code="daemons.hard"/></td>
							<td class="rb lf"><input type="text" name="hddsize" id="hddsize" size="20" maxlength="20" OnKeyPress='num_only(event)' style='ime-mode:disabled'> GB</td>
						</tr>
						<tr>
							<td>
								<input type="radio" id="groupSelect" name="groupSelect" value="regGroup"/>
								<spring:message code="daemons.choosegroup"/>
							</td>
							<td class="lf">
								<label id="lstGroupLabel">
									<select id="groupList" name="groupList">
										<c:forEach var="i" items="${group}">
											<option value="${i.idx}">
												${i.uid}
											</option>
										</c:forEach>
									</select> 
									<a href="#"><img id="editGroup" src="./img/btn_gedit.gif" height="16"><img id="deleteGroup" src="./img/btn_gdelete.gif" height="16" alt="<spring:message code="daemons.deletegroup"/>"></a>
								</label>
								<label id="editGroupLabel">
									<input type="text" id="editGroupText" size="20" maxlength="30" style='ime-mode:disabled'/>
									<a href="#"><img id="editGroupOk" src="./img/btn_gok.gif" height="16"><img id="editGroupCancel" src="./img/btn_gcancel.gif" height="16"></a>
								</label>
							</td>
							<td>
								<input type="radio" id="groupSelect" name="groupSelect" value="newGroup"/>
								<spring:message code="daemons.addgroup"/>
							</td>
							<td class="rb lf">								
								<input type="text" id="groupText" name="groupText" size="20" maxlength="30" style='ime-mode:disabled'>
							</td>
						</tr>
						<tr>
							<td><input type="checkbox" id="authCheck" name="authCheck"> with auth</td>
							<td>admin user : <input type="text" name="authUser" id="authUser" size="20" maxlength="100" style='ime-mode:disabled'></td>
							<td>password : </td>
							<td class="rb lf"><input type="password" name="authPasswd" id="authPasswd" size="20" maxlength="200" style='ime-mode:disabled'></td>
						</tr>
					</tbody>
				</table>
				<div class="btn_area">
					<a><img class="fl" id="autoResearch" src="./img/autoDetect_btn<spring:message code="common.img"/>.png"></a>
					<a href="#" id="registDevice" ><img src="./img/btn_register<spring:message code="common.img"/>.gif" width="42" height="20" alt=<spring:message code="common.save"/>></a> 
					<a href="#"><img src="./img/btn_cancel<spring:message code="common.img"/>.gif" width="42" height="20" alt="<spring:message code="common.cancel"/>" id="setFormCancelButton"></a>
				</div>
					<table class="tb_list_06">
						<colgroup>
							<col width="135">
							<col width="110">
							<col width="260">
							<col width="">
						</colgroup>
						<tbody>
							<tr>
								<th rowspan="2"><spring:message code="daemons.modifyAtOnce"/></th>
								<td><spring:message code="daemons.memory"/></td>
								<td><input type="text" id="batchMemorySize" name="batchMemorySize" size="20" maxlength="20" OnKeyPress='num_only(event)' style='ime-mode:disabled'/> GB</td>
								<td class="rb"></td>
							</tr>
							<tr>
								<td><spring:message code="daemons.hard"/></td>
								<td><input type="text" id="batchHddSize" name="batchHddSize" size="20" maxlength="20" OnKeyPress='num_only(event)' style='ime-mode:disabled'/> GB</td>
								<td class="rb"></td>
							</tr>
						</tbody>
					</table>
				<div class="btn_area"><a><img id="batchUpdateBtn" src="./img/modifyatOnce_btn<spring:message code="common.img"/>.png"></a></div>
				</form>
			</div>
			<!-- // content end -->
			<div class="clear"></div>
		</div>
		<!-- // container end  -->

		<!-- footer start -->
		<div id="footer">&nbsp;</div>
		<!-- // footer end -->
	</div>
</body>
</html>
