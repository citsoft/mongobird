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
	setDbSizeGraph();
}

function setSumGraph(){
	var postData = $('#frm_demon').serialize();
	$.ajax({
		type:'POST',
		url : 'subGraph.do',
		dataType : 'html',
		data : postData,
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ")");
			imgPath(jsonData);
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});	
}

function setDbSizeGraph(){
	var postData = $('#frm_demon').serialize();
	$.ajax({
		type:'POST',
		url : 'subDbGraph.do?search_gubun=dataSize',
		dataType : 'html',
		data : postData,
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ")");
			
			var defaultSrc = "./sub/graph.jsp?fileName=";
			var timestamp = "&dt="+nowDateNoFormat();
			var strimg = '';
			for(var i=0;i<jsonData.aaData.length;i++){
				var dbName = jsonData.aaData[i];
				var srcStr = defaultSrc + dbName + timestamp;
				strimg += '<img src=' + srcStr + '><br>';
			}
			$('#dbSize_graph_area').html(strimg);
			setIndexGraph();
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});	
} 
function setIndexGraph(){
var postData = $('#frm_demon').serialize();
$.ajax({
	type:'POST',
	url : 'subDbGraph.do?search_gubun=indexSize',
	dataType : 'html',
	data : postData,
	success:function(json,textStatus){
		var jsonData = eval("(" + json + ")");
		
		var defaultSrc = "./sub/graph.jsp?fileName=";
		var timestamp = "&dt="+nowDateNoFormat();
		var strimg = '';
		for(var i=0;i<jsonData.aaData.length;i++){
			var dbName = jsonData.aaData[i];
			var srcStr = defaultSrc + dbName + timestamp;
			strimg += '<img src=' + srcStr + '><br>';
		}
		$('#index_graph_area').html(strimg);
		setSumGraph();
	},
	error:function(xhr,textStatus, errorThrown){
		alert(xhr.responseText);
	}
});	
}

function goSearch() {
	document.frm_demon.action="deviceDbGraphView.do";
	document.frm_demon.submit();
}

$(document).ready(function() {
	setGraph();
	
	$('input:radio[name=deviceCode], input:radio[name=dstype]').change(function (event) {
		goSearch();
	});
});
