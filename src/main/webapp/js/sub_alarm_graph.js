/*******************************************************************************
 * "mongobird" is released under a dual license model designed to developers 
 * and commercial deployment.
 * 
 * For using OEMs(Original Equipment Manufacturers), ISVs(Independent Software
 * Vendor), ISPs(Internet Service Provider), VARs(Value Added Resellers) 
 * and another distributors, or for using include changed issue
 * (modify / application), it must have to follow the Commercial License policy.
 * To check the Commercial License Policy, you need to contact Cardinal Info.Tech.Co., Ltd.
 * (http://www.citsoft.net)
 *  *
 * If not using Commercial License (Academic research or personal research),
 * it might to be under AGPL policy. To check the contents of the AGPL terms,
 * please see "http://www.gnu.org/licenses/"
 ******************************************************************************/
function goGraph(){	
	var selectDate = $('#selectDate').val();
		var period = $('#graph_period').val();
		var selectHour = $('#selectHour').val();
		var selectMin = $('#selectMin').val();
		$.ajax({
			type:'GET',
			url : 'setGraphDate.do',
			data : {"period" : period, "selectDate" : selectDate, "selectHour" : selectHour, "selectMin" : selectMin},
			dataType:'html',
			success:function(json,textStatus){
				var checkDate = settingDefaultGraph(json);
				if(checkDate == -1){
					serverDate();
				}else{
					getDbLst();
				}
			},
			error:function(xhr,textStatus, errorThrown){
				
			}
		});
}

function settingDefaultGraph(subDate) {
	var arrDate = subDate.split("|");
	var stFullDate = arrDate[0];
	var edFullDate = arrDate[1];
	var checkDate = "";
	if(arrDate[2] != null || arrDate[2] != ""){
		checkDate = arrDate[2];
	}
	$("#sdate").val(stFullDate);
	$("#edate").val(edFullDate);
	
	return checkDate;
}

function serverDate(){
	var period = $('#graph_period').val();
	$.ajax({
		type:'POST',
		url : 'setDefaultGraphDate.do',
		data : {"period" : period},
		dataType:'html',
		success:function(json,textStatus){
			settingDefaultGraph(json);
			getDbLst();
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});
}

function setGraph(){
	parent.jQuery('html').showLoading();	
	imgTotalCount = 0;
	imgCount = 0;
	$("#graph_area_event_graph").empty();
	var postData = $('#frm_demon').serialize();
	$.ajax({
		type:'POST',
		url : 'subGraph.do',
		dataType : 'html',
		data : postData,
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ")");
			imgPath(jsonData);
			sumDBLockGraph();
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});	
}

function sumDBLockGraph(){
	var postData = $('#frm_demon').serialize();
	$.ajax({
		type:'POST',
		url : 'lockSumGraph.do',
		dataType : 'html',
		data : postData,
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ")");
			
			var count = 0;
			for (var i in jsonData.aaData[0]) {
				count++;
			}
			var isExistDsDev = (count == 0);
			if(isExistDsDev){
				var imgPath = pwd+'/img/sub_nodaemon_graph'+imgLang+'.gif';
				$("#graph_area_event_graph").append('<img onerror="defaultImg_graph(\'./img/sub_nodaemon_graph'+imgLang+'.gif\', this);" src="'+imgPath+'">');
			}else{				
				var defaultSrc = "./sub/graph.jsp?fileName=";
				var timestamp = "&dt="+nowDateNoFormat();
				for(var i=0;i<jsonData.aaData.length;i++){
					var dbSumName = jsonData.aaData[i];
					var srcStr = defaultSrc + dbSumName + timestamp;
				$("#graph_area_event_graph").append('<img onerror="defaultImg_graph(\'./img/sub_nograph'+imgLang+'.gif\', this);" src="'+srcStr+'">');
				}
			}
			dbLockGraph();
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});	
}

function dbLockGraph(){
	var postData = $('#frm_demon').serialize();
	$.ajax({
		type:'POST',
		url : 'lockGraph.do',
		dataType : 'html',
		data : postData,
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ")");

			var count = 0;
			for (var i in jsonData.aaData) {
				count++;
				imgTotalCount++;
			}
			var isExistDsDev = (count == 0);
			if(isExistDsDev){
				var imgPath = pwd+'/img/sub_nodaemon_graph'+imgLang+'.gif';
				$("#graph_area_event_graph").append('<img onerror="defaultImg_graph(\'./img/sub_nodaemon_graph'+imgLang+'.gif\', this);" src="'+imgPath+'">');
				  jQuery('html').hideLoading();	
			}else{				
				var defaultSrc = "./sub/graph.jsp?fileName=";
				var timestamp = "&dt="+nowDateNoFormat();
				for(var i=0;i<jsonData.aaData.length;i++){
					var dbName = jsonData.aaData[i];
					var srcStr = defaultSrc + dbName + timestamp;
					$("#graph_area_event_graph").append('<img onload="loadImage(this)" onerror="defaultImg_graph(\'./img/sub_nograph'+imgLang+'.gif\', this);" src="'+srcStr+'">');
				}
			}
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});	
}

function getDbLst(){
	var deviceCode = $('#deviceCode').val();
	var type_gubun = $('#type_gubun').val();
	$.ajax({
		type:'POST',
		url : 'selectDbLst.do',
		data : {"deviceCode" : deviceCode, "type_gubun":type_gubun},
		dataType:'html',
		success:function(json,textStatus){
			$("#selectLstFrame").empty();
			var jsonData = eval("(" + json + ')');
				for(var i=0;i<jsonData.aaData.length;i++){
					var dbName = jsonData.aaData[i];
					$("#selectLstFrame").append('<input type="hidden" id="dbNameLst" name="dbNameLst" value="'+dbName+'"/>');
				}
				setGraph();
		},
		error:function(xhr,textStatus, errorThrown){
		}
	});
}

var imgTotalCount = 0;
var imgCount = 0;
function loadImage(objImg){
	if(objImg.src.indexOf("nograph")<0)imgCount++;
	if(imgTotalCount == imgCount){
		parent.suframeSize($('#contents_graph').outerHeight(),500,"eventframe");
		parent.jQuery('html').hideLoading();			
	}
}

function imgPath(jsonData){
	var count = 0;
	for (var i in jsonData.aaData) {
		count++;
	}
	var isExistDsDev = (count == 0);
	if(isExistDsDev){
		var imgPath = pwd+'/img/sub_nodaemon_graph'+imgLang+'.gif';
		$("#graph_area_event_graph").append('<img id="noGraph" onerror="defaultImg_graph(\'./img/sub_nodaemon_graph'+imgLang+'.gif\', this);" src="'+imgPath+'">');
		  jQuery('html').hideLoading();	
	}else{				
		var defaultSrc = "./sub/graph.jsp?fileName=";
		var timestamp = "&dt="+nowDateNoFormat();
		for(var i=0;i<jsonData.aaData.length;i++){
			var dsName = jsonData.aaData[i];
			var srcStr = defaultSrc + dsName + timestamp;
			$("#graph_area_event_graph").append('<img onerror="defaultImg_graph(\'./img/sub_nograph'+imgLang+'.gif\', this);" src="'+srcStr+'">');
		}
	}
}

