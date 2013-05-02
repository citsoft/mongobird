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
function sortItem(item){
	$("#sortItem").val(item);
}

function sortAsc(){
	$("#sort").val("asc");
}

function sortDesc(){
	$("#sort").val("desc");
}

function sortClick(item){
	var count = Number($("#count").val());
	count++;
	$("#count").val(count);
	sortItem(item);
	if(count%2==1){
		sortDesc();
	}else{
		sortAsc();
	}
	loadPage();
}
