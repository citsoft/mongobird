function setDate(stFullDate, edFullDate){
	$("#sdate").val(stFullDate);
	$("#edate").val(edFullDate);

	default_min = dateFormatSplit(stFullDate);
	default_max = dateFormatSplit(edFullDate);
	setDateCenter(stFullDate,edFullDate);
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

function setSliderDate(stFullDate, edFullDate){
	$("#sliderMin").val(stFullDate);
	$("#sliderMax").val(edFullDate);

	global_min = dateFormatSplit(stFullDate);
	global_max = dateFormatSplit(edFullDate);
}