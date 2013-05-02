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
function goSearch() {
	document.totalGraphForm.action="totalDetailedGraph.do";
	document.totalGraphForm.submit();
}

$(function(){
	$('#pop_close').click(function (event) {
		window.parent.$.smartPop.close();
	});	
	
	$('input[type=radio]').click(function (event) {
		jQuery('#wrap').showLoading();
		goSearch();
	});
});

function loadImage(objImg){
	hideLoding();
	setTimeout(hideLoding, 100);
}

function hideLoding(){
	  jQuery('#wrap').hideLoading();	
}
