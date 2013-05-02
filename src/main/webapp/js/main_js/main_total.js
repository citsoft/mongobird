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
function setStatusTotal(){	
//	jQuery('table#totalInfo').showLoading();
	$.ajax({
		type:'POST',
		url : 'totalInfo.do',
		dataType:'html',
		success:function(json,textStatus){
			//dbg("json : "+json);
			var jsonData = eval("(" + json + ")");
			
			var totalDbDataSize = jsonData.aaData[0].totalDbDataSize;
			var totalDbIndexSize = jsonData.aaData[0].totalDbIndexSize;
			var totalGlobalLockTime = jsonData.aaData[0].totalGlobalLockTime;
			var maximumPageFaults = jsonData.aaData[0].maximumPageFaults;
			
			$('#totalInfoDBSize').attr('title', toolTip_title(totalDbDataSize,"Byte")); 
			$('#totalInfoDBSize').html(sizeFormat(parseFloat( totalDbDataSize ) ));
			$('#totalInfoIndexSize').attr('title', toolTip_title(totalDbIndexSize,"Byte")); 
			$('#totalInfoIndexSize').html(sizeFormat(parseFloat( totalDbIndexSize ) ));

			$('#totalInfoGlobalLockTime').attr('title', toolTip_title(totalGlobalLockTime,"μs"));
			$('#totalInfoGlobalLockTime').html(decimal(microSecondsFormat(parseFloat( totalGlobalLockTime ) ) ) + " msec");

			$('#totalInfoPageFault').attr('title', toolTip_title(parseFloat(maximumPageFaults),""));
			$('#totalInfoPageFault').html(decimal(parseFloat( maximumPageFaults ) ));
			
			setTipByTableName("totalInfo");
			
//			jQuery('table#totalInfo').hideLoading();
		},
		error:function(xhr,textStatus, errorThrown){
			//dbg("ajax error");
			//dbg(xhr);
			//dbg(textStatus);
			//dbg(errorThrown);
		}
	});	
}

$(document).ready(function() {
		setInterval(function() { setStatusTotal(); }, mainRefreshPeriodMinute);
		setStatusTotal();
});
