$(document).ready( function() {		
	hideAlertArea();
	var count = $("#count").val();
	enlarge(count%2);
	
	var selectedPeriod = "slider_"+$("#graph_period").val();
	$('#'+selectedPeriod).addClass("fontbold");
	 var pickerOpts = { dateFormat:"yy-mm-dd" };
	$('#selectDate').datepicker(pickerOpts);
});

var imgTotalCount = 0;
var imgCount = 0;
function loadImage(objImg){
	if(objImg.src.indexOf("nograph")<0)imgCount++;
	if(imgTotalCount == imgCount){
		  jQuery('html').hideLoading();			
	}
}

function imgPath(jsonData){
	$("#graph_area_graph").empty();

	var count = 0;
	for (var i in jsonData.aaData) {
		count++;
		imgTotalCount++;
	}
	var isExistDsDev = (count == 0);
	if(isExistDsDev){
		var imgPath = pwd+'/img/sub_nodaemon_graph'+imgLang+'.gif';
		$("#graph_area_graph").append('<img id="noGraph" onerror="defaultImg_graph(\'./img/sub_nodaemon_graph'+imgLang+'.gif\', this);" src="'+imgPath+'">');
		  jQuery('html').hideLoading();	
	}else{				
		var defaultSrc = "./sub/graph.jsp?fileName=";
		var timestamp = "&dt="+nowDateNoFormat();
		for(var i=0;i<jsonData.aaData.length;i++){
			var dsName = jsonData.aaData[i];
			var srcStr = defaultSrc + dsName + timestamp;
			$("#graph_area_graph").append('<img id="'+dsName+'" onload="loadImage(this)" onerror="defaultImg_graph(\'./img/sub_nograph'+imgLang+'.gif\', this);" src="'+srcStr+'">');
		}
	}
}

$(function(){	
	$('#secStep,#minStep,#5minStep,#30minStep,#hourStep,#last_consolFun,#avg_consolFun,#total_consolFun').click(function (event) {
		checkNull(setDefault);
	});
	$('#selectDate,#selectHour,#selectMin').change(function (event) {
		checkNull(setSelectDate);
	});
	
	 $('#masterAll').click(function () {
		 checkState("master", $('#masterAll').attr('checked'));
	 });
	 $('#slaveAll').click(function () {
		 checkState("slave", $('#slaveAll').attr('checked'));
	 });
	
 $('#checkAll').click(function () {
		if ($('#checkAll').attr('checked')) {
//	        $(this).parents('ul').find(':checkbox').attr('checked', true);
			$('#frm_demon input:checkbox').attr('checked', true);
	    }else{
//	        $(this).parents('ul').find(':checkbox').removeAttr('checked');
			$('#frm_demon input:checkbox').removeAttr('checked');
	    }
    });
 
 $('#frm_demon input:checkbox').filter('.paramckbox').click(function () {
	 checkLst();
 	});
});

function checkState(checkState, isChecked){
	$.ajax({
		type:'POST',
		url : 'getStateLst.do',
		data : {"checkState" : checkState},
		dataType:'html',
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ')');
			if(isChecked){
				if($("#frm_demon input:checkbox:checked").filter('.stateckbox').length != 2)$("#frm_demon input:checkbox").filter('.paramckbox').removeAttr('checked');
				for (var i in jsonData.aaData) {
					var aData = jsonData.aaData[i];
					$('#frm_demon input:checkbox').filter('[value=' + aData + ']').attr('checked', true);
				}
			}else{
				for (var i in jsonData.aaData) {
					var aData = jsonData.aaData[i];
					$('#frm_demon input:checkbox').filter('[value=' + aData + ']').removeAttr('checked');
				}
			}
			checkLst();
    	},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});
}

function checkNull(funcName){
	if ($("#frm_demon input:checkbox").filter('.paramckbox').length != 0 && $("#frm_demon input:checkbox:checked").filter('.paramckbox').length == 0) {
		alert("Choose at least one");
	}else{
		funcName();
	}
}

function checkLst(){
	var lstLen = $("#frm_demon input:checkbox").filter('.paramckbox').length;
	var lstCheckedLen = $("#frm_demon input:checkbox:checked").filter('.paramckbox').length;
	if(lstLen == lstCheckedLen){
		$('#checkAll').attr('checked', true);
	}else{
		$('#checkAll').removeAttr('checked');
	}
}

function setGraph_period(period){
	$('#graph_period').val(period);
	var period = $('#graph_period').val();
	var edateStr = $('#edate').val();
	$.ajax({
		type:'POST',
		url : 'subGraphDate.do',
		data : {"period" : period, "edateStr" : edateStr},
		dataType:'html',
		success:function(json,textStatus){
			var arrDate = json.split("|");
			var stFullDate = arrDate[0];
			var edFullDate = arrDate[1];
			setSliderDate(stFullDate,edFullDate);
			compareGraphDate(stFullDate, edFullDate);
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});
}

function compareGraphDate(sliderMin, sliderMax){
	var sdateStr = $('#sdate').val();
	$.ajax({
		type:'POST',
		url : 'compareGraphDate.do',
		data : {"sdateStr" : sdateStr, "sliderMin":sliderMin},
		dataType:'html',
		success:function(json,textStatus){
			$('#sdate').val(json);
			goSearch();
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});
}

function setDefault(){
	var period = $('#graph_period').val();
	$.ajax({
		type:'POST',
		url : 'setDefaultGraphDate.do',
		data : {"period" : period},
		dataType:'html',
		success:function(json,textStatus){
			settingNowGraph(json);
			goSearch();
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});
}

function setNowGraphDate(){
	var period = $('#graph_period').val();
	var sdateStr = $("#sdate").val();
	var edateStr = $("#edate").val();
	$.ajax({
		type:'POST',
		url : 'setNowGraphDate.do',
		data : {"period" : period, "sdateStr" : sdateStr, "edateStr" : edateStr},
		dataType:'html',
		success:function(json,textStatus){
			var arrDate = json.split("|");
			var stFullDate = arrDate[0];
			var stDate = arrDate[1];
			var edDate = arrDate[2];
			setSliderDate(stFullDate,edDate);
			setDate(stDate,edDate);
			goSearch();
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});
}

function setSelectDate(){	
	var selectDate = $('#selectDate').val();
	if(selectDate != ""){
		var period = $('#graph_period').val();
		var selectHour = $('#selectHour').val();
		var selectMin = $('#selectMin').val();
		$.ajax({
			type:'GET',
			url : 'setGraphDate.do',
			data : {"period" : period, "selectDate" : selectDate, "selectHour" : selectHour, "selectMin" : selectMin},
			dataType:'html',
			success:function(json,textStatus){
				var checkDate = settingNowGraph(json);
				if(checkDate == -1){
					setDefault();
				}else{
					goSearch();
				}
			},
			error:function(xhr,textStatus, errorThrown){
				
			}
		});	
	}else{
		setDefault();
	}
}

function settingNowGraph(subDate) {
	var arrDate = subDate.split("|");
	var stFullDate = arrDate[0];
	var edFullDate = arrDate[1];
	var checkDate = "";
	if(arrDate[2] != null || arrDate[2] != ""){
		checkDate = arrDate[2];
	}
	setSliderDate(stFullDate, edFullDate);
	setDate(stFullDate, edFullDate);
	
	return checkDate;
}

function setDateCenter(sdateStr,edateStr){
	$.ajax({
		type:'POST',
		url : 'getDefaultOffsetDays.do',
		data : {"sdateStr" : sdateStr, "edateStr":edateStr},
		dataType:'html',
		success:function(json,textStatus){	
			var arrFullDate = json.split(" ");
			var date = arrFullDate[0];
			var stFullTime = arrFullDate[1];
	
			var arrFullTime = stFullTime.split(":");
	
			var hourStr = arrFullTime[0];
			var minStr = arrFullTime[1];
			
			$("#selectDate").val(date);
			$("#selectHour").val(hourStr);
			$("#selectMin").val(minStr);
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});
}

function setSliderDate(stFullDate, edFullDate){
	$("#sliderMin").val(stFullDate);
	$("#sliderMax").val(edFullDate);

	global_min = dateFormatSplit(stFullDate);
	global_max = dateFormatSplit(edFullDate);
}

function setDate(stFullDate, edFullDate){
	$("#sdate").val(stFullDate);
	$("#edate").val(edFullDate);

	default_min = dateFormatSplit(stFullDate);
	default_max = dateFormatSplit(edFullDate);
	setDateCenter(stFullDate,edFullDate);
}

function dateFormatSplit(str){
	var setDate = new Date();
	
	var arrFullDate = str.split(" ");
	var dateStr = arrFullDate[0];
	var timeStr = arrFullDate[1];
	
	var arrDate = dateStr.split("-");
	var arrTime = timeStr.split(":");
	
	setDate = new Date(arrDate[0], arrDate[1]-1, arrDate[2], arrTime[0], arrTime[1]);
	return setDate;
}

function sortBig(){
	$("#sortItem").val("big");
}

function sortSmall(){
	$("#sortItem").val("small");
}

function sortClick(){
	var count = Number($("#count").val());
	count++;
	$("#count").val(count);
	$("#enlarge").empty();
	var num = count%2;
	if(num == 1){
		sortBig();
	}else{
		sortSmall();
	}	
	enlarge(num);
	setGraph();
}

function enlarge(count){
	if(count == 1){
		$("#enlarge").html("<a href='#'class='deco_none'><img src='./img/png_minus.png' width='16' height='16'> "+ Normal +"</a>");
	}else{
		$("#enlarge").html("<a href='#'class='deco_none'><img src='./img/png_plus.png' width='16' height='16'> "+ Enlarge +"</a>");
	}
}
