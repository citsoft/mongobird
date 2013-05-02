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
	function goCheck(type, deviceCode, dbname){
		var param = "?dsname="+type+"&deviceCode="+deviceCode;
		if (dbname != undefined) param += "&dbname="+dbname;
		var url = "popupGraphView.do"+param;
		parent.document.getElementById("graphframe").src=url; 
// 		var param = "?dsname="+type+"&deviceCode="+deviceCode;
// 		if (dbname != undefined) param += "&dbname="+dbname;
// 		var winHandler = window.open("popupGraphView.do"+param,"Graph_"+type,"width=1000,height=700,left=50,top=50,menubar=no,toolbar=no,location=no,status=no,resizable=yes,scrollbars=yes");
// 		winHandler.focus();
	}
	
	$(function(){
		$('#deviceCode').change(function () {
			document.demon.action="demonPop.do";
			document.demon.submit();
		});
		
		$('#pop_close').click(function (event) {
			window.parent.$.smartPop.close();
		});		
	});
	
	function init(){
		initDeviceCode();
	}
	
	function initDeviceCode(){
		var objCode = document.getElementsByName("deviceCodeObj");
		var objCodeName;
		for (var i=0; i<objCode.length; i++) {
			var strobj = document.getElementById("deviceCode"+i);
			objCodeName = deviceCodeTable.getItem(objCode[i].value);
			strobj.innerText = objCodeName;
		}
	}
	
	function grpSelected(grpname) {
		$("td[id="+grpname+"]").each(function(ind, obj){
			$(this).addClass('row_selected');
		});
	}
	
	function grpUnSelected(grpname) {
		$("td[id="+grpname+"]").each(function(ind, obj){
			$(this).removeClass('row_selected');
		});
		
	}
	
	$(document).ready( function() {
		setTipByTableName("daemonDetail");
		setTipByTableName("dbdetail");
	});
