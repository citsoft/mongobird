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
//상단 4 개 total 정보
function setMainGraph(){
	$.ajax({
		type:'POST',
		url : 'mainGraph.do',
		dataType:'html',
		success:function(json,textStatus){
			var jsonData = eval("(" + json + ")");

			for (var i in jsonData.aaData[0]) {
				var imgPath = pwd+'/img/main_nodaemon_graph'+imgLang+'.gif';
				if(jsonData.aaData[0][i] == ""){
				}else{
					var defaultSrc = "./sub/graph.jsp?fileName=";
					var timestamp = "&dt="+nowDateNoFormat();
					imgPath = defaultSrc+jsonData.aaData[0][i]+timestamp;
				}
				$("#" + arr[i]).attr("src", imgPath);
			}
			
		},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});	
}

var arr = new Array();
arr = {"totalDbDataSize":"img_dbsize","totalDbIndexSize":"img_dbindex",	"totalGlobalLockTime":"img_global","extra_info_page_faults":"img_pagefault"};	

$(document).ready(function() {
		
		setInterval(function() { setMainGraph(); }, mainRefreshPeriodMinute);
		setMainGraph();
});
