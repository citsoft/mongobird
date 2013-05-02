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
function getMyState(myStateLst){
	$.ajax({
		type:'POST',
		url : 'getMyState.do',
		data : {"myStateLst" : myStateLst},
		dataType:'html',
		success:function(json,textStatus){
			var arr = json.split(",");
			for(var i=0; i<arr.length; i++){
				var arr2 = arr[i].split(":");
				var deviceCode = arr2[0];
				var state = arr2[1];
				$stateLabel = $('#state_'+deviceCode);
				if(state=="1"){
					$stateLabel.html("<img src='./img/master.gif' width='24' height='21'>");
				}else if(state=="2"){
					$stateLabel.html("<img src='./img/slave.gif' width='24' height='21'>");
				}else if(state=="3"){
					$stateLabel.html("<img src='./img/recovering.gif' width='21' height='21'>");
				}else if(state=="7"){
					$stateLabel.html("<img src='./img/arbiter.gif' width='24' height='21'>");
				}else if(state=="9"){
					$stateLabel.html("<img src='./img/rollback.gif' width='21' height='21'>");
				}
			}
    	},
		error:function(xhr,textStatus, errorThrown){
			
		}
	});
}
