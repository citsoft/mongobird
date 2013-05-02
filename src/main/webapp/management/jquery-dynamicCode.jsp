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
<%@ page language="java" contentType="text/html; charset=utf-8"
    pageEncoding="utf-8"%>
<%@taglib uri="http://www.springframework.org/tags" prefix="spring"%>
<script type="text/javascript">
$(function setGroupSelect(value){
	$("#groupCode > option[value="+value+"]").attr("selected", "true");
});

$(function setDaemonSelect(value){
	$("#deviceCode > option[value="+value+"]").attr("selected", "true");
});

//$(function setEventSelect(value){
//	$("#type > option[value="+value+"]").attr("selected", "true");
//});

function getDaemonArray(paramGroupCode,paramDeviceCode){
	$.ajax({
		type:'POST',
		url : 'groupSelect.do',
		data : {"groupCode" : paramGroupCode},
		dataType:'html',
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ')');
			if(paramGroupCode!=0){
				$("#deviceCode").empty().append("<option value='0'>--<spring:message code="common.chooseone"/>--</option>");
				for(var i=0;i<jsonData.aaData.length;i++){
					var idx = jsonData.aaData[i].idx;
					$("#deviceCode").append("<option value='"+idx+"'>"+deviceCodeTable.getItem(idx)+"</option>");
				}
				$('#setForm #deviceCode').val(paramDeviceCode);
			}else{
				$("#groupCode").empty().append("<option value='0'>--<spring:message code="common.chooseone"/>--</option>");
				for(var i=0;i<jsonData.aaData.length;i++){
					var idx = jsonData.aaData[i].idx;
					$("#groupCode").append("<option value='"+idx+"'>"+groupCodeTable.getItem(idx)+"</option>");
				}
				$("#deviceCode").empty().append("<option value='0'>--<spring:message code="common.chooseone"/>--</option>");
			}
			$("#common_type").empty().append("<option value='0'>--<spring:message code="common.chooseone"/>--</option>").show();
			$('#mongod_type').hide();
			$('#mongos_type').hide();
		},
		error:function(xhr,textStatus, errorThrown){
		}
	});
}

function eventSelect(paramDeviceCode,paramEvent){
	$.ajax({
		type:'POST',
		url : 'deviceSelect.do',
		data : {"deviceCode" : paramDeviceCode},
		dataType:'html',
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ')');
			var type = "" ;
			for(var i=0;i<jsonData.aaData.length;i++){
				type = jsonData.aaData[i].type;
			}
			if(type=='mongos'){
			$('#common_type').hide();
				$('#mongos_type').show();
				$('#setForm #mongos_type').val(paramEvent);
			}else if(type=='mongod'){
				$('#common_type').hide();
				$('#mongod_type').show();
				$('#setForm #mongod_type').val(paramEvent);
			}else{
				
			}

			$('#setForm #type').val(paramEvent);
			$('#setForm #type_gubun').val(type);
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});
}

function typeSelect() {
	var groupCode = $("#groupCode option:selected").val();
	var deviceCode = $("#deviceCode option:selected").val();
	var type = "";
	if($('#setForm #type_gubun').val()=='mongos'){
		type = $("#mongos_type option:selected").val();
	}else{
		type = $("#mongod_type option:selected").val();
	}
	$("#setForm #type").val(type);
	$.ajax({
		type:'POST',
		url : 'eventSelect.do',
		data : {"groupCode" : groupCode, "deviceCode" : deviceCode, "type" : type},
		dataType:'html',
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ')');
			for(var i=0;i<jsonData.aaData.length;i++){
				var criticalvalue = jsonData.aaData[i].criticalvalue;
				var warningvalue = jsonData.aaData[i].warningvalue;
				var infovalue = jsonData.aaData[i].infovalue;
				var idx = jsonData.aaData[i].idx;
				$('#setForm #idx').val(idx);
				if(jsonData.aaData[i].unit=='seconds'){
					$('#setForm #criticalvalue').val(microSecondsFormat(criticalvalue));
					$('#setForm #warningvalue').val(microSecondsFormat(warningvalue));
					$('#setForm #infovalue').val(microSecondsFormat(infovalue));
				}else{
					$('#setForm #criticalvalue').val(criticalvalue);
					$('#setForm #warningvalue').val(warningvalue);
					$('#setForm #infovalue').val(infovalue);
				}
			}
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});
}

$(document).ready( function() {
	$('#groupCode').change(function () {
		var groupCode = $(this).children('option:selected').val();
		getDaemonArray(groupCode,0);
	});
	
	$('#deviceCode').change(function () {
		var deviceCode = $("#deviceCode option:selected").val();
		eventSelect(deviceCode,"");
	});
	
	$('#mongod_type, #mongos_type').change(typeSelect);
	
});

</script>
