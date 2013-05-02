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
			var jsonData = eval("(" + json + ")");
			imgPath(jsonData);
			setSoloGraph();	
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});	
}

function setSoloGraph(){
	var postData = $('#frm_demon').serialize();
	$.ajax({
		type:'POST',
		url : 'soloLockGraph.do',
		dataType : 'html',
		data : postData,
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ")");
			
			$("#solo_graph_area").empty();
			
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
					$("#solo_graph_area").append('<img id="'+dbName+'" onerror="defaultImg_graph(\'./img/sub_nograph.gif\', this);" src="'+srcStr+'">');
				}
			}
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});	
}

function goSearch(){
	document.frm_demon.action="dbLockGraphView.do";
	document.frm_demon.submit();	
}

$(document).ready(function() {
	setGraph();
	$('#frm_demon input:radio[name=dbname]').change(function (event) {
		checkNull(goSearch);
	});
});
