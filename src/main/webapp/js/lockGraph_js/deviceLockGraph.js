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
function setGraph(){
	jQuery('html').showLoading();
	imgTotalCount = 0;
	imgCount = 0;
	var postData = $('#frm_demon').serialize();
	$.ajax({
		type:'POST',
		url : 'lockGraph.do',
		dataType : 'html',
		data : postData,
		success:function(json,textStatus){
			sysLockGraph();
			var jsonData = eval("(" + json + ")");
			imgPath(jsonData);
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});	
}

function sysLockGraph(){
	var postData = $('#frm_demon').serialize();
	var type_gubun = "multi";
	var dsName = "global";
	$.ajax({
		type:'POST',
		url : 'subGraph.do?type_gubun='+type_gubun+'&dsNameLst='+dsName+'&gubun=lockGraph',
		dataType : 'html',
		data : postData,
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ")");
			
			$("#all_graph_area").empty();
			
			var dbName = "global";
			var count = 0;
			for (var i in jsonData.aaData) {
				count++;
			}
			var isExistDsDev = (count == 0);
			if(!isExistDsDev){				
				var defaultSrc = "./sub/graph.jsp?fileName=";
				var timestamp = "&dt="+nowDateNoFormat();
				for(var i=0;i<jsonData.aaData.length;i++){
					var dbName = jsonData.aaData[i];
					var srcStr = defaultSrc + dbName + timestamp;
				$("#all_graph_area").append('<img id="'+dbName+'" src="'+srcStr+'">');
				}
			}
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
			for (var i in jsonData.aaData) {
				count++;
			}
			var isExistDsDev = (count == 0);
			if(!isExistDsDev){				
				var defaultSrc = "./sub/graph.jsp?fileName=";
				var timestamp = "&dt="+nowDateNoFormat();
				for(var i=0;i<jsonData.aaData.length;i++){
					var dbSumName = jsonData.aaData[i];
					var srcStr = defaultSrc + dbSumName + timestamp;
				$("#all_graph_area").append('<img src="'+srcStr+'">');
				}
			}
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});	
}

function getDbLst(){
	var deviceCode = $('input:radio[name=deviceCode]:checked').val();
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
					$("#selectLstFrame").append('<li><input type="checkbox" class="paramckbox" id="dbNameLst" name="dbNameLst" onClick="checkLst();" value="'+dbName+'">  '+dbName+'</li>');
				}
				$("#menu_graph input:checkbox").attr ( "checked" , true );
				checkLst();
				setGraph();
		},
		error:function(xhr,textStatus, errorThrown){
		}
	});
}

function goSearch(){
	document.frm_demon.action="deviceLockGraphView.do";
	document.frm_demon.submit();	
}

$(document).ready(function() {
	getDbLst();
	
	$('#frm_demon input:radio[name=deviceCode]').change(function (event) {
		getDbLst();
	});
});
